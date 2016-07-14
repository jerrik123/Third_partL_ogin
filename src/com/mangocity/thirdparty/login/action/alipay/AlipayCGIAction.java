package com.mangocity.thirdparty.login.action.alipay;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.mangocity.model.mbr.MbrLoginLog;
import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.services.crm.CrmRegisterServiceException;
import com.mangocity.services.crm.ICrmRegisterService;
import com.mangocity.services.mbr.IMbrService;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.services.mbr.MbrServiceException;
import com.mangocity.services.mbr.RegisterServiceException;
import com.mangocity.services.mbrship.IMbrshipCategoryService;
import com.mangocity.services.mbrship.MbrshipCategoryServiceException;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdpartservice.service.exception.ThirdPartyRegisterServiceException;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.utils.CodeFilter;
import com.mangocity.thirdparty.login.utils.CookieUtil;
import com.mangocity.thirdparty.login.utils.LoginUtils;
import com.mangocity.thirdparty.login.vo.RegisterVO;
import com.mangocity.util.ConstantArgs;

@SuppressWarnings("serial")
public class AlipayCGIAction extends ApplicationAction {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	private IThirdPartyRegisterService thirdPartyRegisterService;
	
	private IRegisterService registerService;
	/**
	 * 会员登陆用
	 */
	private ICrmRegisterService crmRegisterService;
	
	/**
	 * 会籍类型 服务接口
	 */
	private IMbrshipCategoryService mbrhisCateService;
	
	/**
	 * 记录登录日志用
	 */
	private IMbrService mbrService;
	/**
	 * 注册使用的vo对象
	 */
	private RegisterVO registerBean;
	
	private String rederictURL;
	
	private final static String DEFAULT_REDIRECT_URL = "http://www.mangocity.com";
	
	private String req_url = "https://mapi.alipay.com/gateway.do";
	
	//支付宝酒店合作 渠道号：
	private String ALIPAY_CATEGORY_PROJECT_CODE = "0133001"; 
	
	private final static String ALIPAY_CATEGORY_CD = "9313500001";
	
	private static String alipay_category_name ;
	
	private Map<String, String> params = null;
	/**
	 * 支付宝用户的称呼 和 唯一id
	 */
	private String real_name;
	private String user_id;
	
	/**
	 * 对查询用的参数map进行初始化
	 */
	private void initParams()
	{
		if(null == params){
			params = new HashMap<String,String>();
			params.put("partner", AlipayConfig.partner);
			params.put("_input_charset", AlipayConfig.input_charset);
			params.put("return_url", "http://www.mangocity.com/ThirdPartyLogin/alipay/cgi.action");
			params.put("service", "alipay.auth.authorize");
			params.put("target_service", "user.auth.quick.login");
		}
		//防钓鱼时间戳
		String anti_phishing_key = null;
		try{
			anti_phishing_key = AlipaySubmit.query_timestamp();
			params.put("anti_phishing_key", anti_phishing_key);
		}catch(Exception e){
			log.error("获取防钓鱼时间戳异常",e);
		}
		log.debug("支付宝快捷登陆接口参数列表 " + params);
	}
	
	/**
	 * 构建alipay登录请求
	 * @return
	 */
	public String linkAlipay(){
		initParams();
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(params,"get","确认");
		log.debug("建立alipay请求页面"+sHtmlText);
		PrintWriter out = null ;
		try{
			getResponse().setContentType("text/html;charset=UTF-8");
			out = getResponse().getWriter();
			out.write(sHtmlText);
		}catch(IOException e){
			log.error("建立alipay请求页面输出流异常",e);
		}finally{
			if(out != null){
				out.close();
			}
		}
		return NONE;
	}
	
	/**
	 * alipay联合登录回调方法
	 * @return
	 */
	public String cgi(){
		
		Map<String,String> paramMaps = new HashMap<String,String>();
		rederictURL = DEFAULT_REDIRECT_URL;
		String target_url = getRequest().getParameter("target_url");
		if(StringUtils.isNotEmpty(target_url)){
			rederictURL = target_url;
		}
		
		if(!verifyResponse(getRequest(),paramMaps)){
			log.error("非合法的alipay请求,requestStr:"+getRequest().getQueryString());
			return SUCCESS;
		}
		String is_success = paramMaps.get("is_success");
		if(!"T".equals(is_success)){
			log.error("alipay请求验证失败,requestStr:"+getRequest().getQueryString());
			return SUCCESS;
		}
		
		user_id = paramMaps.get("user_id");
		
		log.info("alipay会员快捷登录user_id:"+user_id);
		
		real_name = paramMaps.get("real_name");
		String token = paramMaps.get("token");
		//记录cookie
		writeAlipayCookies(user_id,token,real_name);
		long csn = -1; //会员id
		if (StringUtils.isEmpty(user_id)) {
			// 支付宝返回来的用户为空
			log.error("支付宝返回的用户唯一码user_id为空");
			return SUCCESS;
		}
		TsIntUser user = null;
		try {
			user = thirdPartyRegisterService
					.queryThirdPartyUserByLoginCode(user_id);
		} catch (ThirdPartyRegisterServiceException e) {
			log.error("alipay根据loginCode查询用户异常,loginCode:"+user_id,e);
			return SUCCESS;
		}
		// 已关联
		if (user != null) {
			log.debug("alipay 已关联用户,user.csn = " + user.getCsn()
					+ " user.nick = " + user.getNick() + "user.logincode = "
					+ user.getLoginCode());
			csn = user.getCsn();
			dealCookieAndLog(csn);
			// 已绑定成功
			return SUCCESS;
		}
		// 未关联

		csn = registerMango();
		// 绑定
		if (csn != -1 && bandUserMbr(user_id, csn, real_name)) {
			dealCookieAndLog(csn);
		} else {
			log.error("注册芒果网用户时失败!user_id:" + user_id + ",csn:" + csn
					+ ",real_name" + real_name);
		}

		return SUCCESS;
	}
	
	/**
	 * mobile 、email 和 real_name 进行注册，根据注册成功的会员id进行绑定 如果未注册 直接帮忙注册，并绑定
	 * 再根据注册生成的会员id 查询会员，并写会员cookie
	 */
	private long registerMango(){
		PersonMainInfo mainInfo = new PersonMainInfo();
		mainInfo.setNameCn(real_name);
		mainInfo.setEmailAddr(user_id + "@alipay.com");
		mainInfo.setMbrshipCategoryCd(ALIPAY_CATEGORY_CD);
		mainInfo.setCategoryName(getCategoryName());
		mainInfo.setProjectCode(ALIPAY_CATEGORY_PROJECT_CODE);
		mainInfo.setCreateBy("ThirdParty");
		mainInfo.setAttribute(0);
		// 添加会员类型为散客类型
		mainInfo.setMbrTyp(ConstantArgs.MBR_TYP_INDIVIDUAL);
		return registerMbr(mainInfo);
	}
	
	/**
	 * 处理cookie和登录日志表
	 */
	private void dealCookieAndLog(long mbrid){
		writeMbrCookies(mbrid);	//根据会员id 查询出 会员，并写会员cookie	
		//记录登录日志表
		addMbrLoginLog(mbrid,user_id, "E");
	}
	
	private boolean verifyResponse(HttpServletRequest request,Map<String,String> params) {
		//获取支付宝GET过来反馈信息
		Map requestParams = getRequest().getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getByte("iso8859-1"),"utf-8");
			params.put(name, valueStr);
		}
		
		//计算得出通知验证结果
		return AlipayNotify.verify(params);
	}
	
	private String getCategoryName(){
		if(null == alipay_category_name || "".equals(alipay_category_name)){
			try {
				alipay_category_name = mbrhisCateService.mbrshipCategoryByCategoryCd(ALIPAY_CATEGORY_CD).getMbrshipCategoryId().toString();
			} catch (MbrshipCategoryServiceException e) {
				log.error("查询会籍类型异常",e);
			}
		}
		return alipay_category_name;
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
			log.error("alipay注册芒果会员异常,personMainInfo" + info.getMbrId(),e);
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
		user.setType(8); //表示类型为 支付宝
		user.setStatus(1); //有效
		user.setBirthday(new Date());
		user.setLoginName(user_id);
		user.setNick(real_name);
		user.setGendar(1);
		
		try {
			thirdPartyRegisterService.addThirdPartyUser(user);
			flag = true;
		} catch (ThirdPartyRegisterServiceException e) {
			log.error("alipay绑定会员异常: user_id =" +user_id + "mbrid =" + mbrid + "real_name =" +real_name,e);
		}		
		return flag;		
	}
	
	/**
	 * 根据 芒果网会员id  写此会员的cookies
	 * @param mbrId
	 */
	private void writeMbrCookies(long mbrId)
	{
		MbrSession mbrSession = null;
		try {
			mbrSession = registerService.checkLogin(mbrId+"", null);
		} catch (RegisterServiceException e) {
			log.error("alipay会员登录异常,mbrId:"+mbrId,e);
			return;
		}
		//显示支付宝昵称
		mbrSession.setSessionMbrNetName(realNametoHtml());
		getRequest().getSession().setAttribute("mbrSession", mbrSession);
		LoginUtils.setCookies(mbrSession, this.getResponse());
		//写一个会员是否登录成功的 标示cookie
		CookieUtil.writeCookie("login_flag", "t",LoginUtils.cookie_max_age,LoginUtils.domain, "/", getResponse());
		getRequest().setAttribute("login_flag", "t");
	}
	
	private void writeAlipayCookies(String user_id,String token,String real_name)
	{
		String realName = realNametoHtml();
		CookieUtil.writeCookie("projectcode", ALIPAY_CATEGORY_PROJECT_CODE, LoginUtils.cookie_max_age, LoginUtils.domain, "/",this.getResponse());
		CookieUtil.writeCookie("user_id", user_id, LoginUtils.cookie_max_age, LoginUtils.domain, "/",this.getResponse());
		CookieUtil.writeCookie("token", token, LoginUtils.cookie_max_age, LoginUtils.domain, "/",this.getResponse());
		CookieUtil.writeCookie("real_name", realName, LoginUtils.cookie_max_age, LoginUtils.domain, "/",this.getResponse());
		CookieUtil.writeCookie("nick", realName, LoginUtils.cookie_max_age, LoginUtils.domain, "/",this.getResponse());
	}
	
	 /* 登陆成功后记录日志
	 * @param loginName
	 * @param subType
	 */
	private void addMbrLoginLog(Long mbrId,String loginName,String loginSubtyp) {
		MbrLoginLog mbrLoginLog = new MbrLoginLog();
		mbrLoginLog.setMbrId(mbrId);
		mbrLoginLog.setOpType("1");
		mbrLoginLog.setLoginSubtyp(loginSubtyp);
		mbrLoginLog.setLoginName(loginName);
		mbrLoginLog.setLoginIp(getIpAddr());
		mbrLoginLog.setStatus("1");
		mbrLoginLog.setCreateBy("system");
		mbrLoginLog.setCreateTime(new Date());
		try {
			mbrService.addRecordLoginNumByMasterStation(mbrLoginLog);
		} catch (MbrServiceException e) {
			log.error("记录alipay登录日志表异常",e);
		}
	}
	
	private String realNametoHtml(){
		String realName = real_name;
		try {
			realName = URLEncoder.encode(CodeFilter.toHtml(real_name), "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("alipay encode编码异常,str"+real_name,e);
		}
		return realName;
	}
	
	/**
	 * 获取会员登录的IP地址
	 * @return String ip
	 */
	public String getIpAddr(){
		HttpServletRequest request = getRequest();
        String ip = request.getHeader("clientip");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
	}
	
	public String getReq_url() {
		return req_url;
	}

	public void setReq_url(String req_url) {
		this.req_url = req_url;
	}

	public String getRederictURL() {
		return rederictURL;
	}

	public void setRederictURL(String rederictURL) {
		this.rederictURL = rederictURL;
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

	public void setMbrService(IMbrService mbrService) {
		this.mbrService = mbrService;
	}

	public IMbrshipCategoryService getMbrhisCateService() {
		return mbrhisCateService;
	}

	public void setMbrhisCateService(IMbrshipCategoryService mbrhisCateService) {
		this.mbrhisCateService = mbrhisCateService;
	}
}
