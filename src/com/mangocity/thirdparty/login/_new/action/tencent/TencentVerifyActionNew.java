package com.mangocity.thirdparty.login._new.action.tencent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mangocity.mbr.unionlogin.UnionLogin;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.services.mbr.RegisterServiceException;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdpartservice.service.exception.ThirdPartyRegisterServiceException;
import com.mangocity.thirdparty.login._new.action.model.MemberCacheInfo;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.utils.CodeFilter;
import com.mangocity.thirdparty.login.utils.Constants;
import com.mangocity.thirdparty.login.utils.CookieUtil;
import com.mangocity.thirdparty.login.utils.HTTPMbrServerHelper;
import com.mangocity.thirdparty.login.utils.LoginUtils;
import com.mangocity.util.ConstantArgs;

@SuppressWarnings("serial")
public class TencentVerifyActionNew extends ApplicationAction {
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
		System.out.println("sessionId "+getSession().getId());
		loginUrl = loginUrl.replace("tencent", "tencent_new");
		log.info("loginUrl: " + loginUrl);
//		loginUrl = loginUrl.replace("ThirdPartyLogin", "TencentLogin").replace("tencent", "tencent_new");
		/*loginUrl = "http://openapi.qzone.qq.com/oauth/qzoneoauth_authorize?oauth_consumer_key=205324&oauth_token="
				+ "10051913205812220211&oauth_callback=http%3A%2F%2Fwww.mangocity.com%2FTencentLogin%2Ftencent%2FgetRejump.action";*/
		//检查是否有来源字段
		String from = getRequest().getParameter("fromZg");
		if(from!=null&&from.length()>0) {			
			sessionMap.put("fromZg", from);
		}
		System.out.println("sessionId "+getSession().getId());
		getSession().setAttribute(QQ_LOGIN_SESSION, sessionMap);
		log.info("TencentVerifyAction,sessionMap:"+sessionMap);
		return SUCCESS;
	}

	/**
	 * 收接第三方网站完成登录后的请求.主要有以下两种可能： 1.通过验证的第三方网站会员是否与网站会员绑定,如果绑定则执行登操作
	 * 目的主要是完成cookie的写入(写入会员信息).然后直接跳转到会员中心的首页. 2.如果没有绑定则进入会员注册与绑定页。
	 * 
	 * @return
	 * @throws RegisterServiceException 
	 * @throws Exception
	 */
	public String getRejump() throws RegisterServiceException {
		
		 String token = CookieUtil.readCookie("token", ServletActionContext.getRequest());
		 String mbrId="";
		 if(StringUtils.isNotBlank(token)){
			 MemberCacheInfo memberCacheInfo = loadLoginMemberInfo(token);
			 if(null != memberCacheInfo){
			 mbrId = memberCacheInfo.getMbrID();
			 }
		 }
		
		log.info("Enter into TencentAction.getRejump()");
		String app_openid = getRequest().getParameter("openid");
		log.info("app_openid: " + app_openid);
		String oauth_vericode = getRequest().getParameter("oauth_vericode");
		System.out.println("sessionId getRejump "+getSession().getId());
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
			if(oTsIntUser!=null){
			//String newMbrId= String.valueOf(registerService.selectByLongName(oTsIntUser.getLoginName()));
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("loginName",oTsIntUser.getLoginName());
			jsonObj = JSON.parseObject(HTTPMbrServerHelper.requestMS(Constants.GET_MBRID_BY_LOGIN_NAME,JSON.toJSONString(jsonObj)));
			if(jsonObj.getLong("result")!=0){
				String newMbrId = String.valueOf(jsonObj.getLong("result"));
					if(StringUtils.isNotBlank(mbrId) && !mbrId.equals(newMbrId)){//如果从redis中获得的mbrId和绑定关联表中取得的mbrId不相等,则删除数据,进行解绑。
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("loginCode",oTsIntUser.getLoginCode());
						String result = HTTPMbrServerHelper.requestMS(Constants.DELETE_DUPLICATE,JSON.toJSONString(jsonObject));
						jsonObject = JSON.parseObject(result);
						if(jsonObject.getString("result").equals("success")){
							JSONObject newJsonObject = new JSONObject();
							newJsonObject.put("login_name", oTsIntUser.getLoginName()+"@qq.mangocity.com");
							newJsonObject.put("id", 0);
						String content = HTTPMbrServerHelper.requestMS(Constants.UNBINDING_ACCOUNT,JSON.toJSONString(newJsonObject));
						log.info(content);
							oTsIntUser=null;
						}
					}
				}
			}
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
	
	   public MemberCacheInfo loadLoginMemberInfo(String token) {
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("token",token);
	        String content = HTTPMbrServerHelper.requestMS(Constants.LOGIN_URL, JSON.toJSONString(jsonObject));
	        log.info("请求mbrService获取memCache后返回："+content);
	        if(content!=null&&!content.equals("")){
	            try{
	                JSONObject json = JSON.parseObject(content);
	                String code = json.getString("code");
	                if(code.equals("200")){//200=请求成功
	                    String data=json.getString("data");
	                    MemberCacheInfo memberCacheInfo = JSON.parseObject(data,MemberCacheInfo.class);
	                    return memberCacheInfo;
	                }
	            }catch (Exception e){
	                log.info("请求mbrService 异常");
	            }
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
