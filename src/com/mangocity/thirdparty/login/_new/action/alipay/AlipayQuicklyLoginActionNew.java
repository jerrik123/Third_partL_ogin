package com.mangocity.thirdparty.login._new.action.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.util.AlipayMd5Encrypt;
import com.alipay.util.AlipayNotify;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.services.crm.CrmRegisterServiceException;
import com.mangocity.services.crm.ICrmRegisterService;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.services.mbr.RegisterServiceException;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdpartservice.service.exception.ThirdPartyRegisterServiceException;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.utils.CookieUtil;
import com.mangocity.thirdparty.login.utils.LoginUtils;
import com.mangocity.thirdparty.login.vo.RegisterVO;

@SuppressWarnings("serial")
public class AlipayQuicklyLoginActionNew extends ApplicationAction {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	private IThirdPartyRegisterService thirdPartyRegisterService;
	
	private IRegisterService registerService;
	/**
	 * 会员登陆用
	 */
	private ICrmRegisterService crmRegisterService;
	/**
	 * 注册使用的vo对象
	 */
	private RegisterVO registerBean;
	
	private String rederictURL;
	
	private String req_url = "https://mapi.alipay.com/gateway.do";
	
	private String sign = "";
	private String key = "v3gdh1a74end4cb18osr8lsdn1g1ojwd";
	//支付宝酒店合作 渠道号：
	private String ALIPAY_CATEGORY_PROJECT_CODE = "0029003"; 
	
	private String[] parameters = {
			"service=alipay.auth.authorize",
			"partner=2088701341186430",
			"return_url=http://www.mangocity.com/ThirdPartyLogin/alipay/deallogin.action",
			"target_service=user.auth.quick.login",
			"_input_charset=UTF-8"
	};
	private Map<String, String> params = new HashMap<String, String>();
	/**
	 * 支付宝用户的称呼 和 唯一id
	 */
	private String real_name;
	private String user_id;
	/**
	 * 生成待签名字符串
	 * @return
	 */
	private String getPrepareSignStr()
	{
		StringBuffer sb = new StringBuffer();
		List<String> sortList = new ArrayList<String>();
		for(String s:this.parameters)
		{
			sortList.add(s);
		}
		Collections.sort(sortList);
		for(int i = 0;i < sortList.size(); i++)
		{
			if(i == 0)
				sb.append(sortList.get(i));
			else
			{
				sb.append('&');
				sb.append(sortList.get(i));
			}
		}
		if(log.isInfoEnabled())
		{
			log.info("支付宝快捷登陆待字符串 " + sb.toString());
		}
		return sb.toString();
	}
	
	/**
	 * 对查询用的参数map进行初始化
	 */
	private void initParams()
	{
		if(null == params)
			params = new HashMap<String,String>();
		params.clear(); //清空原有数据
		params.put("partner", "2088701341186430");
		params.put("_input_charset", "UTF-8");
		params.put("service", "user.logistics.address.query");
		params.put("return_url", "http://www.mangocity.com/ThirdPartyLogin/alipay/dealQuery.action");
		if(log.isInfoEnabled())
		{
			log.info("支付宝物流查询接口参数列表 " + params);
		}
	}
	/**
	 * 支付宝物流查询接口的返回处理
	 * @throws UnsupportedEncodingException 
	 * @throws ThirdPartyRegisterServiceException 
	 * @throws RegisterServiceException 
	 */
	public String dealQuery()
	{
		HttpServletRequest request = getRequest();
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
			params.put(name, valueStr);
		}
		
		//打印回传的参数
		List<String> logList = new ArrayList<String>(params.keySet());
		for(String s:logList)
		{
			log.info(s+"=" + params.get(s));
		}
		
		String user_id = request.getParameter("user_id");
		String mobile = request.getParameter("mobile");
		String email = request.getParameter("email");
		String cert_no = request.getParameter("cert_no");
		String real_name = request.getParameter("real_name");
		
		if(log.isInfoEnabled())
		{
			log.info("支付宝回传的参数信息:" + 
					"mobile :" + mobile +
					"email :" + email +
					"cert_no :" + cert_no +
					"real_name :" + real_name +
					"user_id :" + user_id 
					);
		}
		
		boolean verify_result = AlipayNotify.verify(params);
		
		if(verify_result){
			readAlipayCookie();
			long mbrid = 0l; //会员id
			try{
				if(!isEmpty(user_id))
				{
					TsIntUser user = thirdPartyRegisterService.queryThirdPartyUserByLoginCode(user_id);
					log.info("user = " + user);
					//已关联
					if(user != null)
					{
						log.info(" user.csn = " + user.getCsn()+ " user.nick = " + user.getNick() + "user.logincode = " + user.getLoginCode());
						mbrid = user.getCsn();		 
						writeMbrCookies(mbrid);	//根据会员id 查询出 会员，并写会员cookie				
						//已绑定成功，直接跳到目的页面
						return "querysucc";
					}else //未关联
					{
						//手机号码和邮箱地址都为空
						if(isEmpty(email) && isEmpty(mobile))
						{
							//跳到 注册绑定页面
							request.setAttribute("regist", "regist");
							if(registerBean == null)
								registerBean = new RegisterVO();
							registerBean.setRegistermode("1");
							if(log.isInfoEnabled())
							{
								log.info("支付宝用户 :" + user_id + " 没有传回手机号码和邮箱地址等联系方式，要求其进行芒果网注册!");
							}
							return "queryfail";
						}				
						//检查手机号码 和 邮箱是否已经注册，如果 有一个 已经注册，跳转至绑定页面				 
						if(!isEmpty(mobile) && !registerService.validateLoginNameUnique(mobile, "M"))
						{
							//跳到登陆绑定页面  默认选择手机号码进行绑定
							request.setAttribute("mobile", mobile);
							if(registerBean == null)
								registerBean = new RegisterVO();
							registerBean.setRegistermode("0");
							if(log.isInfoEnabled())
							{
								log.info("手机号码 :" + mobile + " 与系统中已注册的手机号码冲突");
							}
							return "queryfail";
						}
						if(!isEmpty(email) && !registerService.validateLoginNameUnique(email, "E"))
						{
							//跳转至绑定选择页面   默认选择邮箱地址进行绑定
							request.setAttribute("email", email);
							if(registerBean == null)
								registerBean = new RegisterVO();
							registerBean.setRegistermode("0");
							if(log.isInfoEnabled())
							{
								log.info("邮箱地址 :" + email + " 与系统中已注册的邮箱地址冲突");
							}
							return "queryfail";
						}				
						/**
						 * mobile 、email 和 real_name 进行注册，根据注册成功的会员id进行绑定
						 * 如果未注册 直接帮忙注册，并绑定 
						 * 再根据注册生成的会员id   查询会员，并写会员cookie
						 */
						PersonMainInfo mainInfo = new PersonMainInfo();
						mainInfo.setMobileNo(mobile);
						mainInfo.setNameCn(real_name);
						mainInfo.setCertNo(cert_no);
						mainInfo.setEmailAddr(email);
						mainInfo.setMbrshipCategoryCd("0100100001");
						mainInfo.setProjectCode(ALIPAY_CATEGORY_PROJECT_CODE);
						mbrid = registerMbr(mainInfo);						
						//绑定
						if(mbrid != -1)
							bandUserMbr(user_id, mbrid,real_name);
						else
						{
							request.setAttribute("message", "注册绑定用户时发生异常");
							log.info("注册芒果网用户时失败!");
							return "queryfail";
						}
						//写芒果本身的cookies				 
						writeMbrCookies(mbrid);						
						//直接跳到目的页面
						return "querysucc";
					}
				}else
				{
					//支付宝返回来的用户为空
					log.info("支付宝返回的用户唯一码user_id为空");
					return null;
				}
			}catch (Exception e) {
				request.setAttribute("message", "绑定时发生异常");
				log.info(e);
				return "queryfail";
			}
		}else{
			log.info("支付宝返回参数验证失败 ");
			return null;
		}
	}
	
	public boolean isEmpty(String str)
	{
		return str == null || "".equals(str);
	}
	
	/**
	 * 根据待签名字符串、拼接key之后生成的字符串进行md5加密，返回密文
	 */
	private String getMD5Sign()
	{
		String str = getPrepareSignStr();
		str = str+key;
		sign = AlipayMd5Encrypt.md5(str);
		if(log.isInfoEnabled())
		{
			log.info("支付宝快捷登陆时生成的密文sign= " + sign);
		}
		return sign;
	}
	
	/**
	 * 生成请求快捷登陆的url链接
	 */
	private String getRedirectURL()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.req_url);
		sb.append("?");
		sb.append(getPrepareSignStr());
		
		sb.append("&sign_type=MD5");
		sb.append("&sign=").append(getMD5Sign());
		log.info("支付宝快捷登陆链接url = " + sb.toString());
		return sb.toString();
	}
	/**
	 * 快捷登陆的action
	 * @return
	 */
	public String alipayLogin()
	{
		rederictURL = getRedirectURL();
		return "succ";
	}
	
	/**
	 * 从map得到签名字符串
	 */
	private String getQianmingString(Map<String, String> map)
	{
		String prestr = "";
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		
		for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = (String)map.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }		
		log.info("支付宝物流查询接口待签名字符串" + prestr);
		return prestr;
	}
	
	/**
	 * 回调地址方法
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String dealLogin() throws UnsupportedEncodingException
	{
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
			params.put(name, valueStr);
		}
		
		String user_id = request.getParameter("user_id");
		//授权令牌
		String token = request.getParameter("token");	
		String real_name = request.getParameter("real_name");
			//real_name = new String(real_name.getBytes("ISO-8859-1"), "UTF-8");  //对支付宝的名字中的汉字可能出现的乱码进行转化
			//打印回传的参数
			List<String> logList = new ArrayList<String>(params.keySet());
			for(String s:logList)
			{
				log.info(s+"=" + params.get(s));
			}
			
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);		
		if(verify_result){//验证成功
			try{
				//登陆验证成功, 写支付宝的cookies				 
				writeAlipayCookies(user_id,token,real_name);					
				// 加入token参数				 
				initParams();
				this.params.put("token", token);
				log.info("快捷登陆返回参数"
						+ "user_id:" + user_id
						+ "token:" + token
						+ "real_name:" + real_name);
				
				String miwen = AlipayMd5Encrypt.md5(getQianmingString(this.params)+key);
				log.info("物流查询密文：" + miwen);
				this.params.put("sign",miwen);
				this.params.put("sign_type", "MD5");
				
				String url = req_url + "?" + getQianmingString(this.params);
				log.info("物流查询url ：" + url);
				response.sendRedirect(url);
				return null;										
				/*Enumeration<String> ps = request.getParameterNames();
				while(ps.hasMoreElements())
				{
					String s = ps.nextElement();
					System.out.println(s + " = " + new String(request.getParameter(s).getBytes("ISO-8859-1"), "UTF-8"));
				}
				System.out.println("验证成功<br />token:"+token);*/
			}catch (Exception e) {
				log.info(e);
				return null;
			}			
		}else{
			//支付宝 验证失败	
			log.info("快捷登陆的返回验证失败");
			return null;
		}
	}
	/**
	 * 用支付宝有关的cookie 初始化成员变量
	 */
	private void readAlipayCookie()
	{
		HttpServletRequest req = getRequest();
		user_id = CookieUtil.readCookie("user_id", req);
		real_name = CookieUtil.readCookie("real_name", req);		
		try {
			if(null != real_name)
				real_name = URLDecoder.decode(real_name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.info(e);
			e.printStackTrace();
		}
		req.setAttribute("nick", real_name);
	}
	
	/**
	 * 绑定页面的处理action
	 * 
	 * 如果输入的用户名 密码 登陆成功，进行绑定
	 * 
	 * 失败  跳转回 绑定页面
	 * @return
	 */
	public String mbrLoginBand()
	{
		String message = null;
		HttpServletRequest req = getRequest();	
		readAlipayCookie();
		
		String sessionRand = (String)req.getSession().getAttribute("sysrand");
		String reqRand = (String)req.getParameter("randCode");//getAttribute("randCode");
		log.info("sessionRand:" + sessionRand + ",reqRand: " + reqRand);
		if(sessionRand == null || !sessionRand.equals(reqRand))
		{			
			message = "验证码输入错误!";
			log.info(message);
			req.setAttribute("message", message);
			return "loginfail";
		}
		
		String login_name = registerBean.getEmail()!=null ? registerBean.getEmail():registerBean.getPhonenumber();//req.getParameter("login_name");
		String password = registerBean.getPassword();
		long mbrid = mbrLogin(login_name,password);
		if(mbrid != -1)
		{
			//绑定会员和支付宝  写会员和支付宝的cookie  跳转至 绑定成功页面
			bandUserMbr(user_id, mbrid, real_name);
			writeMbrCookies(mbrid);
			//writeAlipayCookies(user_id, token, real_name);
			return "loginsucc";
		}else
		{
			//会员登陆失败，继续跳转至绑定选择页面
			if(registerBean == null)
				registerBean = new RegisterVO();
			registerBean.setRegistermode("0");
			message = "会员登录绑定失败,用户名或者密码错误";
			log.info(message);
			req.setAttribute("message", message);
			return "loginfail";
		}
	}
	
	/**
	 * 对注册时的必需参数 赋予默认值
	 * @param personInfo
	 * @return
	 */
	private PersonMainInfo filtPersonInfo(PersonMainInfo personInfo)
	{
		if(personInfo.getCategoryName()== null || "".equals(personInfo.getCategoryName()))
		{
			personInfo.setCategoryName("555");
		}
		//性别
		if(personInfo.getGender()== null || "".equals(personInfo.getGender()))
		{
			personInfo.setGender("99");
		}
		//状态
		if(personInfo.getStus()== null || "".equals(personInfo.getStus()))
		{
			personInfo.setStus("1");
		}		
		//公司来源
		if(personInfo.getSrc()== null || "".equals(personInfo.getSrc()))
		{
			personInfo.setSrc("66");
		}
		//用于区分注册与登陆
		if(personInfo.getIsLoginType()== null || "".equals(personInfo.getIsLoginType()))
		{
			personInfo.setIsLoginType("register");
		}
		return personInfo;
	}
	
	/**
	 * 直接进入芒果网  功能处理action 
	 * @return
	 * @throws RegisterServiceException 
	 * @throws UnsupportedEncodingException 
	 */
	public String mbrDerectLogin()
	{

		readAlipayCookie();
		
		String message = null;
		long mbrid = 0l;
		PersonMainInfo mainInfo = new PersonMainInfo();
		String email = "";
		String r = getRandomStr();//随机值
		
		email = r+"@alipay.mangocity.com";
		try {
			while(!registerService.validateLoginNameUnique(email, "E"))
			{
				r = getRandomStr();	
				email = r+"@alipay.mangocity.com";
			}
		} catch (RegisterServiceException e) {
			message = "注册绑定会员时异常!";
			getRequest().setAttribute("message", message);
			log.info(e);
			return "loginfail";
		}
		log.info("直接进入芒果网时生成的随机邮箱地址：" + email);
		mainInfo.setEmailAddr(email);
		mainInfo.setMbrshipCategoryCd("0100100001");
		mainInfo.setProjectCode(ALIPAY_CATEGORY_PROJECT_CODE);
		
		mbrid = registerMbr(mainInfo);
		
		if(mbrid !=-1)
		{	
			bandUserMbr(user_id, mbrid, real_name);
			writeMbrCookies(mbrid);
			return "loginsucc";
		}else{
			//注册失败，返回绑定页  绑定失败			
			message = "注册绑定失败!";
			log.info("注册芒果网会员时发生异常");
			getRequest().setAttribute("message", message);
			return "loginfail";
		}		
	}
	
	/**
	 * 生成一个随机数字串
	 * @return
	 */
	private String getRandomStr()
	{
		//为了降低冲突的概率，每个串有两个随机数字拼接成
		return getRandom() + "" + getRandom();
	}
	/**
	 * 返回一个正的随机整数
	 * @return
	 */
	private int getRandom()
	{
		Random dom = new Random();
		int r = dom.nextInt();
		return r>0?r:0-r;
	}
	
	/**
	 * 会员注册 绑定action
	 */
	public String mbrRegisterBand()
	{
		/**
		 * 1 注册会员
		 * 
		 * 2 绑定会员 与 支付宝
		 * 
		 * 3写会员cookie
		 * 
		 * 跳到 固定页面
		 */
		readAlipayCookie();		
		HttpServletRequest request = getRequest();
		String message = "";
		String sessionRand = (String)request.getSession().getAttribute("sysrand");
		String reqRand = (String)request.getParameter("randCode");
		log.info("sessionRand:" + sessionRand + ",reqRand: " + reqRand);
		if(sessionRand == null || !sessionRand.equals(reqRand))
		{			
			message = "验证码输入错误!";
			log.info(message);
			request.setAttribute("message", message);
			return "registfail";
		}
		try{
			if(registerBean == null)
				registerBean = new RegisterVO();
			registerBean.setRegistermode("1");
			//验证邮箱和手机号码的唯一性
			if(!isEmpty(registerBean.getPhonenumber()) && !registerService.validateLoginNameUnique(registerBean.getPhonenumber(), "M"))
			{
				message = "手机号码 :" + registerBean.getPhonenumber() + " 与系统中已注册的手机号码冲突";
				log.info(message);
				request.setAttribute("message", message);
				return "registfail";
			}
			if(!isEmpty(registerBean.getEmail()) && !registerService.validateLoginNameUnique(registerBean.getEmail(), "E"))
			{
				message = "邮箱地址 :" + registerBean.getEmail() + " 与系统中已注册的邮箱地址冲突";
				log.info(message);
				request.setAttribute("message", message);
				return "registfail";
			}
			
			long mbrid = 0l;
			PersonMainInfo mainInfo = new PersonMainInfo();		
			mainInfo.setMobileNo(registerBean.getPhonenumber());
			mainInfo.setEmailAddr(registerBean.getEmail());
			mainInfo.setLoginPwd(registerBean.getPassword());		
			mainInfo = this.filtPersonInfo(mainInfo);
			
			mbrid = registerMbr(mainInfo);
			if(mbrid != -1)
			{
				if(!bandUserMbr(user_id, mbrid, real_name)){
					message = "注册后绑定失败!";
					log.info(message);
					request.setAttribute("message", message);
					return "registfail";
				}				
				writeMbrCookies(mbrid);
				return "registsucc";
			}else
			{
				message = "用户注册失败";
				log.info(message
						+" registerBean.getPhonenumber " + registerBean.getPhonenumber() 
						+" registerBean.getEmail() " + registerBean.getEmail() 
						+" registerBean.getPassword() " + registerBean.getPassword() );
				request.setAttribute("message", message);
				return "registfail";
			}	
		}catch (Exception e) {
			message = "注册绑定时发生未知异常!";
			log.info(message);
			request.setAttribute("message", message);
			return "registfail";
		}
	}
	
	/**
	 * 会员登录action
	 * 
	 * 登录成功失败 返回同样的页面，只是写的cookie不一样，页面显示不一样
	 * @return
	 */
	public String mbrlogin()
	{
		String name = getRequest().getParameter("name");
		String password = getRequest().getParameter("password");
		
		long mbrid = mbrLogin(name, password);
		if(mbrid != -1)
		{
			try {
				MbrSession mbrSession = registerService.checkLogin(mbrid+"", null);
				writeMbrCookies(mbrSession);
				if(mbrSession.getSessionMbrShipList()!= null)
				{
					for(int i = 0;i<mbrSession.getSessionMbrShipList().size();i++)
					{
						 Mbrship mbrship = mbrSession.getSessionMbrShipList().get(i);
						 if (StringUtils.isNotEmpty(mbrship.getOldMbrshipCd()) && StringUtils.isNotEmpty(mbrSession.getSessionDefaultMbrshipCd()) &&
									mbrship.getOldMbrshipCd().equals(mbrSession.getSessionDefaultMbrshipCd())){
							 CookieUtil.writeCookie("membercd", mbrship.getOldMbrshipCd(),LoginUtils.cookie_max_age,LoginUtils.domain, "/", getResponse());
							 getRequest().setAttribute("membercd", mbrship.getOldMbrshipCd());
						 }
					}
				}
			} catch (RegisterServiceException e) {
				log.info(e);
				e.printStackTrace();
			}	
		}else
		{
			log.info("芒果网会员登陆失败,登陆的用户名：" + name + ",密码：" + password);
			getRequest().setAttribute("login_flag", "f");
			CookieUtil.writeCookie("login_flag", "f",LoginUtils.cookie_max_age,LoginUtils.domain, "/", getResponse());
			//返回登录页面
		}
		return "login";
	}
	
	/**
	 * 登陆成功后，返回会员id
	 * 失败返回-1
	 * @param userName
	 * @param password
	 * @return
	 * @throws RegisterServiceException 
	 */
	private long mbrLogin(String userName, String password)
	{
		String login_subtype = "A";
		PersonMainInfo info = null;
		try {
			info = crmRegisterService.crmMbrLogin(login_subtype, userName, password);
		} catch (RegisterServiceException e) {
			log.info(e);
			log.info("login fail : name = " + userName + ",password = " + password);
			e.printStackTrace();
		}
		if(info != null)
		{
			return info.getMbrId();
		}else
		{
			return -1;
		}
	}
	/**
	 * 根据构造好的 PersonMainInfo 对象，进行注册会员，如果注册成功，返回会员的id
	 * 失败返回-1
	 * @param info
	 * @return
	 */
	private long registerMbr(PersonMainInfo info)
	{
		try {
			info = filtPersonInfo(info);
			PersonMainInfo resultInfo = crmRegisterService.crmMbrRegister(info);
			if(resultInfo != null && isDigitStr(resultInfo.getReturnTyp()))
			{
				return resultInfo.getMbrId();
			}
		} catch (CrmRegisterServiceException e) {
			log.info("mbr register fail," + info);
			log.info(e);
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 判断一个字符串是否为纯数字的字符串
	 */
	private boolean isDigitStr(String str)
	{
		boolean flag = true;
		//当为空字符串时，按照非纯数字
		if(null==str || str.length()==0)
		{
			return false;
		}
		for(int i=0;i<str.length();i++)
		{
			if(!Character.isDigit(str.charAt(i))){
				flag = false;
				return flag;
			}
		}
		return flag;
	}
	
	/**
	 * 把支付宝用户和芒果的会员绑定在一起 绑定成功，返回true  失败返回false
	 * @param user_id 支付宝会员id
	 * @param mbrid  芒果网会员id
	 * @param real_name  支付宝的用户名
	 * @return
	 * @throws ThirdPartyRegisterServiceException 
	 */
	private boolean bandUserMbr(String user_id,long mbrid,String real_name)
	{
		boolean flag = false;
		TsIntUser user = new TsIntUser();
		user.setCsn(mbrid); //会员id
		user.setLoginCode(user_id); //支付宝唯一号
		user.setType(6); //表示类型为 支付宝
		user.setStatus(1); //有效
		
		user.setLoginName(user_id);
		user.setNick(real_name);
		
		try {
			thirdPartyRegisterService.addThirdPartyUser(user);
			flag = true;
		} catch (ThirdPartyRegisterServiceException e) {
			log.info(e);
			log.info("band exception: user_id =" +user_id + "mbrid =" + mbrid + "real_name =" +real_name);
			e.printStackTrace();
		}		
		return flag;		
	}
	
	/**
	 * 根据 芒果网会员id  写此会员的cookies
	 * @param mbrid
	 */
	private void writeMbrCookies(MbrSession mbrSession)
	{
		getRequest().getSession().setAttribute("mbrSession", mbrSession);
		LoginUtils.setCookies(mbrSession, this.getResponse());
		//写一个会员是否登录成功的 标示cookie
		CookieUtil.writeCookie("login_flag", "t",LoginUtils.cookie_max_age,LoginUtils.domain, "/", getResponse());
		getRequest().setAttribute("login_flag", "t");
	}
	
	/**
	 * 根据 芒果网会员id  写此会员的cookies
	 * @param mbrid
	 */
	private void writeMbrCookies(long mbrid)
	{
		try {
			MbrSession mbrSession = registerService.checkLogin(mbrid+"", null);
			getRequest().getSession().setAttribute("mbrSession", mbrSession);
			LoginUtils.setCookies(mbrSession, this.getResponse());
			//写一个会员是否登录成功的 标示cookie
			CookieUtil.writeCookie("login_flag", "t",LoginUtils.cookie_max_age,LoginUtils.domain, "/", getResponse());
			getRequest().setAttribute("login_flag", "t");
		} catch (RegisterServiceException e) {
			e.printStackTrace();
		}
	}
	
	private void writeAlipayCookies(String user_id,String token,String real_name)
	{
		//先写一个 project_code cookie  value = zhifubao
		try {
			real_name = URLEncoder.encode(real_name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		CookieUtil.writeCookie("projectcode", "zhifubao", LoginUtils.cookie_max_age, LoginUtils.domain, "/",this.getResponse());
		CookieUtil.writeCookie("user_id", user_id, LoginUtils.cookie_max_age, LoginUtils.domain, "/",this.getResponse());
		CookieUtil.writeCookie("token", token, LoginUtils.cookie_max_age, LoginUtils.domain, "/",this.getResponse());
		CookieUtil.writeCookie("real_name", real_name, LoginUtils.cookie_max_age, LoginUtils.domain, "/",this.getResponse());
	}
	public String getReq_url() {
		return req_url;
	}

	public void setReq_url(String req_url) {
		this.req_url = req_url;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getRederictURL() {
		return rederictURL;
	}

	public void setRederictURL(String rederictURL) {
		this.rederictURL = rederictURL;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public IThirdPartyRegisterService getThirdPartyRegisterService() {
		return thirdPartyRegisterService;
	}

	public void setThirdPartyRegisterService(
			IThirdPartyRegisterService thirdPartyRegisterService) {
		this.thirdPartyRegisterService = thirdPartyRegisterService;
	}

	public IRegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}

	public ICrmRegisterService getCrmRegisterService() {
		return crmRegisterService;
	}

	public void setCrmRegisterService(ICrmRegisterService crmRegisterService) {
		this.crmRegisterService = crmRegisterService;
	}

	public RegisterVO getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(RegisterVO registerBean) {
		this.registerBean = registerBean;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
