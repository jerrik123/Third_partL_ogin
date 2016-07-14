package com.mangocity.thirdparty.login.action.caibei;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.services.mbr.RegisterServiceException;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdpartservice.service.exception.ThirdPartyRegisterServiceException;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.helper.IRegisterHelper;
import com.mangocity.thirdparty.login.utils.LoginUtils;
import com.mangocity.thirdparty.login.vo.RegisterVO;

/**
 * <p>
 * @author chenjun
 *
 */
@SuppressWarnings("serial")
public class CaibeiVerifyAction extends ApplicationAction {
	
	private static final String QQ_FANLI_CONNECT_KEY1 = "Qp1ubz9k2fmcRR$zh14*z%fw4mvfwh%!"; //TODO:这里先填写测试的key,后续上线前需要QQ彩贝给到正式的key替换这里
	private static final String QQ_FANLI_CONNECT_KEY2 = "@Y45oQq$1oo8cI1W12d0$cx7R6p74pOx"; //TODO:这里先填写测试的key,后续上线前需要QQ彩贝给到正式的key替换这里
	//private static final String QQ_FANLI_CONNECT_KEY1 = "QQConnect_&~!gcndy2~56rt534$@#4ttwer4576rt2";//测试密匙1
	//private static final String QQ_FANLI_CONNECT_KEY2 =	"&er56656^y3453412(768fgh456345$!46567867990";//测试密匙2
	/**
	 * 彩贝项目的编号
	 */
	private static final String CAIBEI_PROJECT_CODE = "0019009";
	
	/**
	 * 第三方注册服务接口
	 * 目前包括：1:QQ;2:青芒果；3：彩贝
	 */
	private IThirdPartyRegisterService thirdPartyRegisterService;
	
	/**
	 * 第三方注册服务接口
	 * 目前包括：1:QQ;2:青芒果；
	 */
	private IRegisterHelper registerHelper;
	
	private IRegisterService registerService; 
	
	private String url;
	/**
	 * 功能：
	 * 步骤如下：
	 * 1:验证彩贝参数合法性
	 * 2:获取彩贝传递过来的参数
	 * 3:查询数据库中是否已经绑定会员
	 * 3.1:若没有绑定，则注册会员并绑定，并继续步骤4
	 * 3.2:若已经绑定，则继续步骤4
	 * 4:将参数存入cookie,并跳转到指定页面
	 * 4.1将会员信息参数存入cookie
	 * 4.2将彩贝信息参数存入cookie
	 * 4.3登录后跳转到指定页面
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String caibeicgi() {
		//1：验证彩贝参数合法性
		boolean caibeiVerifyFlag = false;
		caibeiVerifyFlag = caibeiVerify();
		if (!caibeiVerifyFlag) {
		    //如果vkey检测不通过，那么跳转到商家首页
		    return "homepage";
		}
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++getRequest().getQueryString()+++++++++++++++++++++++++++++++++"+getRequest().getQueryString());
		//-------------2：获取彩贝传递过来的参数start
		//获取Acct,跟用户QQ帐户一一对应的字符串，该字段是商户作为识别帐户的唯一依据
		String acct = getRequest().getParameter("Acct");
		
		//跟用户QQ帐户一一对应的字符串(大小为32个字符的hash串)，该字段主要作为订单推送的时候使用。
		String openId = acct;
		
		/*联合登录的跳转来源：
		 * 1当用户在合作伙伴网站直接登录时，LoginFrom=[商户id],商户id由彩贝平台分配，如乐淘，则LoginFrom=letao等。
		 *2， 当用户在彩贝平台直接跳转到商户时候：LoginFrom=caibei
		 */
		String lginFrom = getRequest().getParameter("LoginFrom");
		
		/* ViewInfo是一个结构化字符串，解析后的结果是：
		 * array( 
		 * 'ShowMsg' => 展示在页面顶部，给用户提示语, 
		 * 'NickName' => QQ昵称, 
		 * 'CBPoints' => 用户剩余彩贝积分, 
		 * 'CBBonus' => 返利比率 )
		 */
		String viewInfo = getRequest().getParameter("ViewInfo");
		
		HashMap viewinfoMap = new HashMap();
		if (StringUtils.isNotEmpty(viewInfo)) {
			String[] arr1 = viewInfo.split("&");
			String[] arr2 = {};
			int arrLen = arr1.length;
			for(int i=0 ; i<arrLen ; i++) {
			    arr2 = arr1[i].split("=");
			    if( arr2.length > 1 ) {
			        try {
						viewinfoMap.put(arr2[0] , URLDecoder.decode(arr2[1], "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
			    }
			}
		} else {
			//如果参数非法，那么跳转到商家首页
		    return "homepage";
		}
		
		//展示在页面顶部，给用户提示语
		String showmsg = (String)viewinfoMap.get("ShowMsg");
		
		//QQ昵称
		String nickname = (String)viewinfoMap.get("NickName");
		
		//用户可使用的彩贝积分
		int point = Integer.parseInt( (String)viewinfoMap.get("CBPoints") );

		//购买后的返利比率
		String bonus = (String)viewinfoMap.get("CBBonus");
		
		/**
		 * psl-2011/10/24新增2个cookie字段，qqheadshow和qqjifenurl，参数值为：HeadShow和JifenUrl
		 * psl-2011/10/24新增1个cookie字段，qqopenkey，参数值为：OpenKey
		 */
		//合作商家返积分信息
		String qqheadshow = (String)viewinfoMap.get("HeadShow");
		try{
			qqheadshow = URLEncoder.encode(qqheadshow,"utf-8");
			qqheadshow = qqheadshow.replace("+","%20");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		//QQ用户彩贝积分
		String qqjifenurl = (String)viewinfoMap.get("JifenUrl");
		try{
			qqjifenurl = URLEncoder.encode(qqjifenurl,"utf-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		//OpenKey
		String qqopenkey = (String)viewinfoMap.get("OpenKey");
		
		// ViewInfo字段获取值end
		
		/*
		 * 联合登录完成后要跳转的的Url，
		 * 若该值没有输入，则商户处理联合登录后跳转商户站点首页即可，
		 * 否则商户则必须跳转到该Url指定的页面。
		 * 注：该Url主要的目的是用于商户正常联合登录后商户需要跳转到的某个具体的页面，
		 * 如某个具体的活动页面或者某个商品详情页面，防止每次都跳转到首页，
		 * 需要用户自己二次跳转到某个指定页面的问题。
		 */
		url = getRequest().getParameter("Url");
		try {
			url = URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		
		/*
		 * 字符串格式的时间戳，格式为20090724120401 
		 * （为了方便unix和windows格式统一，采用年月日时分秒格式，比如：20090720010000）
		 */
		String ts = getRequest().getParameter("Ts");
		
		/*
		 * 数字签名串
		 * 该值是对联合登录各个字段进行签名后的32位的字符串。
		 */
		String vkey = getRequest().getParameter("Vkey");
		System.out.println("===vkey===:"+vkey);

		/*
		 * 用户点击的跟踪代码（推送订单的时候需要把该字段的内容透传回腾讯,包括我们去查询订单的记录也需要出现这个字段的值），
		 * 该字段是一个最大长度为300个字节的字符串。
		 */
		String attach = getRequest().getParameter("Attach");
		System.out.println("===attach===:"+attach);
		// 2：获取彩贝传递过来的参数end
		
		//3:查询数据库中是否已经绑定会员
		Long csnLong = null;
		TsIntUser tsIntUser = null;
		try {
			tsIntUser = thirdPartyRegisterService.queryThirdPartyUserByLoginCode(acct);
		} catch (ThirdPartyRegisterServiceException e1) {
			e1.printStackTrace();
			return null;
		}
		if (tsIntUser != null && (Long)tsIntUser.getCsn() != null) {
			csnLong = tsIntUser.getCsn();
		}
		
		if (csnLong == null) {
			//3.1:若没有绑定，则注册会员并绑定
			RegisterVO registerBean = new RegisterVO();
			
			//构造默认注册的email
			String eamilConstruct = acct + "@qq.mangocity.com";
			registerBean.setEmail(eamilConstruct);
			
			//默认注册的用户设置随机密码
			registerBean.setPassword(java.util.UUID.randomUUID().toString());
			
			//注册类型:3：QQ彩贝
			registerBean.setRegistertype("3");
			
			//注册方式 1-注册 2-绑定 3-跳过
			registerBean.setRegistermode("3");
			Map<String, String> messageMap = new HashMap<String, String>();
			messageMap.put("openid", acct);
			messageMap.put("Acct", acct);
			messageMap.put("nickname", showmsg);
			registerBean.setMessage(messageMap);
			try {
				registerHelper.registerMango(registerBean);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			// 注册完成后，需要再次查询该会员信息并登录
			try {
				tsIntUser = thirdPartyRegisterService.queryThirdPartyUserByLoginCode(acct);
			} catch (ThirdPartyRegisterServiceException e1) {
				e1.printStackTrace();
				return null;
			}
			csnLong = tsIntUser.getCsn();
		}
		
		MbrSession mbrSession = null;
		if (csnLong != null) {
			try {
				mbrSession = registerService.checkLogin(csnLong.toString(), null);
			} catch (RegisterServiceException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		List<Mbrship> mbrShipList = mbrSession.getSessionMbrShipList();
		try {
			if(null != showmsg && !"".equals(showmsg)){
				mbrSession.setSessionMbrNetName(java.net.URLEncoder.encode(showmsg, "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//T腾讯合作：9303400001
		for (Mbrship mbrship : mbrShipList) {
			if ("9303400001".equals(mbrship.getMbrshipCategoryCd())) {
				mbrSession.setSessionDefaultMbrshipCd(mbrship.getOldMbrshipCd());
				break;
			}
		}
		
		//4:信息参数存入cookie
		if (!mbrShipList.isEmpty()) {
			
			//4.1将会员信息参数存入cookie
			LoginUtils.setCookies(mbrSession, this.getResponse());				

			//4.2将彩贝信息参数存入cookie
			LoginUtils.setCaibeiCookies(tsIntUser.getLoginCode(), qqheadshow, qqjifenurl, qqopenkey, vkey, attach, this.getRequest(), this.getResponse());
		}
		
		//4.3登录后跳转到指定页面
		if (StringUtils.isNotEmpty(url)) {
			return "redirectCB";

		} else {
		    return "homepage";
		}
	}
	
	/**
	 * 验证彩贝参数合法性
	 * 若合法，返回true;否则，返回false
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private boolean caibeiVerify() {
		//将form参数按照字典序进行排序
		Enumeration params = getRequest().getParameterNames();
		Vector vector = new Vector();
		while(params.hasMoreElements()) {
			vector.add((String)params.nextElement());
		}
		
		String[] paramArr = (String[])vector.toArray(new String[1]);
		int paramLen = paramArr.length;
		int tempLen  = paramLen - 1;
		int i,j;
		String tempStr = "";
		for(i=0 ; i<tempLen ; i++) {
		    for(j=i+1 ; j<paramLen ; j++) {
		        if( paramArr[i].compareTo( paramArr[j] ) > 0 ) {
		            tempStr = paramArr[i];
		            paramArr[i] = paramArr[j];
		            paramArr[j] = tempStr;
		        }
		    }
		}

		System.out.println("获取到的参数列表如下：");
		LOG.info("CaibeiVerifyAction.caibeiVerify");
		//进行md5加密比较
		String rawMd5Str = "";
		for(i=0 ; i<paramLen ; i++) {
		    if( paramArr[i].compareTo("Vkey") != 0 ) { //签名串不要Vkey这个参数
		        rawMd5Str += getRequest().getParameter(paramArr[i]);
		        
		        System.out.println(paramArr[i] + "=" + getRequest().getParameter(paramArr[i]));
		        LOG.info(paramArr[i] + "=" + getRequest().getParameter(paramArr[i]));
		    }
		}
		System.out.println("组装后的原串如下：");
		System.out.println("rawMd5Str="+rawMd5Str);
		LOG.info("rawMd5Str="+rawMd5Str);
		String md5_1 = (MD5(rawMd5Str + QQ_FANLI_CONNECT_KEY1)).toLowerCase();
		String md5_2 = (MD5(md5_1 + QQ_FANLI_CONNECT_KEY2)).toLowerCase();

		String vkey = getRequest().getParameter("Vkey");
		if( vkey != null && md5_2.compareTo(vkey) != 0 ) {
		    return false;
		}
		return true;
	}
	
	/**
	 * 根据彩贝提供的MD5算法设计
	 * @param s
	 * @return
	 */
	private String MD5(String s) {
	    char hexDigits[] = {'0', '1', '2', '3', '4',
	                        '5', '6', '7', '8', '9',
	                        'A', 'B', 'C', 'D', 'E', 
	                        'F' };
	    try {
	        byte[] btInput = s.getBytes();
	        
	        //获得MD5摘要算法的 MessageDigest 对象
	        MessageDigest mdInst = MessageDigest.getInstance("MD5");
	        
	        //使用指定的字节更新摘要
	        mdInst.update(btInput);
	        
	        //获得密文
	        byte[] md = mdInst.digest();
	        
	        //把密文转换成十六进制的字符串形式
	        int j = md.length;
	        char str[] = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	            byte byte0 = md[i];
	            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            str[k++] = hexDigits[byte0 & 0xf];
	        }
	        return new String(str);
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	
	/**
	 * 在cookie中根据参数名称获取参数值
	 * @param name
	 * @param request
	 * @return 指定参数名称的参数值
	 */
	private String getCookieValueByName(String name, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String verifyCode = "";
		if (cookies != null && cookies.length > 0) {
			if (cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					String tempName = cookie.getName();
					if (tempName.equals(name)) {
						verifyCode = cookie.getValue();
						break;
					}//end if
				}//end for
			}
		}//end if
		return verifyCode;
	}
	
	/**
	 * 添加参数到cookie中保存
	 * @param name
	 * @param value
	 * @param day
	 * @param response
	 */
	private void addCookieValueByName(String name, String value, int day,
			HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setDomain(".mangocity.com");

		//cookie有效期为30天 60*60*24*30
		cookie.setMaxAge(day);
		response.addCookie(cookie);
	}

	public IThirdPartyRegisterService getThirdPartyRegisterService() {
		return thirdPartyRegisterService;
	}

	public void setThirdPartyRegisterService(
			IThirdPartyRegisterService thirdPartyRegisterService) {
		this.thirdPartyRegisterService = thirdPartyRegisterService;
	}

	public IRegisterHelper getRegisterHelper() {
		return registerHelper;
	}

	public void setRegisterHelper(IRegisterHelper registerHelper) {
		this.registerHelper = registerHelper;
	}

	public IRegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
