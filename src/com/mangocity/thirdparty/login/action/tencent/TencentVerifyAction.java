package com.mangocity.thirdparty.login.action.tencent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.mbr.unionlogin.UnionLogin;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdpartservice.service.exception.ThirdPartyRegisterServiceException;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.utils.CodeFilter;
import com.mangocity.thirdparty.login.utils.CookieUtil;
import com.mangocity.thirdparty.login.utils.LoginUtils;
import com.mangocity.util.ConstantArgs;

@SuppressWarnings("serial")
public class TencentVerifyAction extends ApplicationAction {
	protected Log log = LogFactory.getLog(this.getClass());

	private UnionLogin unionLogin;

	private String loginUrl;

	private String nick;
	
	//腾讯中干用户登录，根据参数跳转到不同页面 默认到腾讯中干首页面
	private String toPageUrl = "http://zt.mangocity.com/zhonggan/index.html";

	private IThirdPartyRegisterService thirdPartyRegisterService;
	
	private IRegisterService registerService;	
	
	public String getToPageUrl() {
		return toPageUrl;
	}

	public void setToPageUrl(String toPageUrl) {
		this.toPageUrl = toPageUrl;
	}

	/**
	 * 用户登陆时得到跳转到第三方网站的登录地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		nick = "";
		loginUrl = "";
		Map<String, String> sessionMap = new LinkedHashMap<String, String>();
		loginUrl = unionLogin.getLoginUrl(sessionMap);
		//检查是否有来源字段
		String from = getRequest().getParameter("fromZg");
		if(from!=null&&from.length()>0) {			
			sessionMap.put("fromZg", from);
		}
		getSession().setAttribute(QQ_LOGIN_SESSION, sessionMap);
		log.info("TencentVerifyAction,sessionMap:"+sessionMap);
		return SUCCESS;
	}

	/**
	 * 收接第三方网站完成登录后的请求.主要有以下两种可能： 1.通过验证的第三方网站会员是否与网站会员绑定,如果绑定则执行登操作
	 * 目的主要是完成cookie的写入(写入会员信息).然后直接跳转到会员中心的首页. 2.如果没有绑定则进入会员注册与绑定页。
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRejump() {
		log.info("Enter into TencentAction.getRejump()");
		String app_openid = getRequest().getParameter("openid");
		String oauth_vericode = getRequest().getParameter("oauth_vericode");

		Object sessionObj = getSession().getAttribute(QQ_LOGIN_SESSION);
		log.info("*********************sessionObj**********************:"+sessionObj);
		if (sessionObj == null || !(sessionObj instanceof LinkedHashMap)){
			log.info("***************错误返回***************");
			return ERROR;
		}

		Map<String, String> jumpParams = (Map<String, String>) sessionObj;
		jumpParams.put("openid", app_openid);
		jumpParams.put("oauth_vericode", oauth_vericode);
		getSession().setAttribute(QQ_LOGIN_SESSION, jumpParams);

		verify();		
		Long csn = null;
		TsIntUser oTsIntUser =null;
		try {
			oTsIntUser = thirdPartyRegisterService.queryThirdPartyUserByLoginCode(app_openid);
		} catch (ThirdPartyRegisterServiceException e) {
			log.error("queryThirdPartyUserByLoginCode error:",e);
		}
		if(oTsIntUser!=null){
			csn = oTsIntUser.getCsn();
		}
		log.info("*********************csn:"+csn+"**************************************");
		// will goto bind and register page
		if (csn == null){
			return "register";
		}
		try {
			MbrSession mbrSession = registerService.checkLogin(csn.toString(), null);
			List<Mbrship> mbrShipList = mbrSession.getSessionMbrShipList();
			nick = CodeFilter.toHtml(nick);
			mbrSession.setSessionMbrNetName(java.net.URLEncoder.encode(nick, "utf-8"));
			
			//腾讯中干用户登录 2014-5-19
			for(Mbrship ship:mbrShipList) {
				if("9316400001".equals(ship.getMbrshipCategoryCd())) {
					mbrSession.setSessionDefaultMbrshipCd(ship.getOldMbrshipCd());
					LoginUtils.setCookies(mbrSession, this.getResponse());					
					LoginUtils.setCaibeiCookies(app_openid, null, null, null,null,null, this.getRequest(), this.getResponse());
					
					//中干用户添加特定cookie
					CookieUtil.writeCookie("member.isTencentZgUser", "Y", -1, ConstantArgs.domain, "/", this.getResponse());
					//腾讯中干用户不使用QQ彩贝积分
					CookieUtil.writeCookie("projectcode", "", -1, ConstantArgs.domain, "/", this.getResponse());
					
					String toPage = jumpParams.get("fromZg");
					if(toPage!=null) {
						if("mp".equals(toPage)) {
							setToPageUrl("http://zt.mangocity.com/zhonggan/index.html");
						} else if("zb".equals(toPage)) {
							setToPageUrl("http://zt.mangocity.com/zhonggan/index-zb.html");
						} else if("gn".equals(toPage)) {
							setToPageUrl("http://zt.mangocity.com/zhonggan/index-gn.html");
						} else if("yl".equals(toPage)) {
							setToPageUrl("http://zt.mangocity.com/zhonggan/index-yl.html");
						} else if("ga".equals(toPage)) {
							setToPageUrl("http://zt.mangocity.com/zhonggan/index-ga.html");
						} else if("hw".equals(toPage)) {
							setToPageUrl("http://zt.mangocity.com/zhonggan/index-hw.html");
						} 
 					}
					return "Tencent_zhonggan";
				}
			}
	
			//T腾讯合作：9303400001
			for (Mbrship mbrship : mbrShipList) {
				if ("9303400001".equals(mbrship.getMbrshipCategoryCd())) {
					mbrSession.setSessionDefaultMbrshipCd(mbrship.getOldMbrshipCd());
					LoginUtils.setCookies(mbrSession, this.getResponse());					
					// add Caibei Cookie in 2011.08.15 start
					LoginUtils.setCaibeiCookies(app_openid, null, null, null,null,null, this.getRequest(), this.getResponse());				
					return SUCCESS;
				}
			}
							
			if (!mbrShipList.isEmpty()) {
				LoginUtils.setCookies(mbrSession, this.getResponse());				
				// add Caibei Cookie in 2011.08.15 start
				LoginUtils.setCaibeiCookies(app_openid, null, null, null,null,null, this.getRequest(), this.getResponse());
			}
		}catch(Exception ex) {
			log.error("getRejump error:",ex);
			return ERROR;
		}
		log.info("Exit TencentAction.getRejump()");
		return SUCCESS;
	}

	public String verify() {
		Object sessionObj = this.getSession().getAttribute(QQ_LOGIN_SESSION);
		if (sessionObj != null){
			Map<String, String> sessionParams = (Map<String, String>) sessionObj;
			sessionParams.put("app_userip", getRequest().getRemoteAddr());
			try {
				Object resultTmp = unionLogin.syncUserInfo(sessionObj);
				Map<String, Object> result = (Map<String, Object>) resultTmp;
				if (result != null) {
					Object tmp = result.get("nickname");
					if (tmp != null) {						
						nick = result.get("nickname").toString();
					}
				}
			} catch (Exception e) {
				log.info("TencentVerifyAction-verify:",e);
			}			
		} else {
			log.info("TencentVerifyAction-verify:sessionObj is null");
		}	
		return null;
	}

	public String logout() throws Exception {

		return SUCCESS;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setThirdPartyRegisterService(
			IThirdPartyRegisterService thirdPartyRegisterService) {
		this.thirdPartyRegisterService = thirdPartyRegisterService;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public void setUnionLogin(UnionLogin unionLogin) {
		this.unionLogin = unionLogin;
	}

	public IRegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}

	public UnionLogin getUnionLogin() {
		return unionLogin;
	}

	public IThirdPartyRegisterService getThirdPartyRegisterService() {
		return thirdPartyRegisterService;
	}

}
