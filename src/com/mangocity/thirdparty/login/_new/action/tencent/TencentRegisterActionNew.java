package com.mangocity.thirdparty.login._new.action.tencent;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.services.crm.ICrmRegisterService;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.services.mbr.RegisterServiceException;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.helper.IRegisterHelper;
import com.mangocity.thirdparty.login.utils.CodeFilter;
import com.mangocity.thirdparty.login.utils.Constants;
import com.mangocity.thirdparty.login.utils.HTTPMbrServerHelper;
import com.mangocity.thirdparty.login.utils.LoginUtils;
import com.mangocity.thirdparty.login.vo.RegisterVO;

/**
 * QQ会员在芒果中注册Action
 * 
 * @author chenjun
 * 
 */
@SuppressWarnings("serial")
public class TencentRegisterActionNew extends ApplicationAction {
	private final static String REG_SUCCESS = "register_success";
	private final static String BIND_SUCCESS = "bind_success";
	private final static String SYN_RAND = "sysrand";

	protected Log log = LogFactory.getLog(this.getClass());

	private IRegisterHelper registerHelper;

	private RegisterVO registerBean;

	private String nick;

	private String randCode;

	private int errorId;// 1 phone not exists 2 mail not exists 3 phone exists 4
						// mail is exists 5 repassword is wrong.

	private IThirdPartyRegisterService thirdPartyRegisterService;
	
	private IRegisterService registerService; 
	
	private ICrmRegisterService crmRegisterService;



	public String register() {
		System.out.println(registerBean);
		Object sessionObj = getRequest().getSession().getAttribute(SYN_RAND);
		if (sessionObj == null) {
			log.info("*********************sessionObj is null,验证码没有生成");
			errorId = 6;// 验证码没有生成
			return ERROR;
		}

		if (!registerBean.getRegistermode().equals(RegisterVO.JUMP)
				&& (randCode == null || "".equals(randCode))) {
			log.info("*********************randCode is null:7");
			errorId = 7;
			return ERROR;
		}

		if (!registerBean.getRegistermode().equals(RegisterVO.JUMP)
				&& !sessionObj.toString().equals(randCode)) {
			log.info("*********************randCode not equal:8");
			errorId = 8;
			return ERROR;
		}

		if (registerBean.getRegistermode().equals(RegisterVO.BND)) {
			log.info("*********************sessionObj:bind");
			if (!bindValidate()){
				log.info("*********************sessionObj:bindvalidate");
				return ERROR;
			}
		}

		if (registerBean.getRegistermode().equals(RegisterVO.REG)) {
			log.info("*********************sessionObj:reg");
			if (!registValidate()) {
				log.info("*********************sessionObj:regvalidate");
				return ERROR;
			}
		}

		// Result result = tencentRegisterManager.register(registerBean);

		Object resultTmp = null;
		resultTmp = getSession().getAttribute(QQ_LOGIN_SESSION);
		log.info("*********************resultTmp:"+resultTmp.toString()+"************");
		if (null == resultTmp) {
			return ERROR;
		}
		Map<String, String> result = (Map<String, String>) resultTmp;
		if (registerBean.getRegistermode().equals(RegisterVO.BND)) {
			registerBean.setMessage(result);
			try {
				//判断绑定账户是否已经绑定
				long csn = 0l;
				String loginCode = result.get("openid").toString();
				String loginId = null;
				if(StringUtils.isNotBlank(registerBean.getPhonenumber())){
					loginId = registerBean.getPhonenumber();
				}else{
					loginId = registerBean.getEmail();
				}
				long mbrId= registerService.selectByLongName(loginId);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("csn",mbrId);
				String isDupBinding = HTTPMbrServerHelper.requestMS(Constants.IS_QQ_DUPLICATE_BINDING,JSON.toJSONString(jsonObject));
				JSONObject jsonObj = JSON.parseObject(isDupBinding);
				if(null!=jsonObj && jsonObj.getString("result").equals("hasBinded")){
					log.info("该账户已经有QQ账户绑定："+isDupBinding);
					errorId = 11;
					return ERROR;
				}
				registerHelper.bindMango(registerBean);
			} catch (Exception e) {
				log.error("bindMango error:",e);
				errorId = 1;
				return ERROR;
			}

		}

		if (registerBean.getRegistermode().equals(RegisterVO.REG)||registerBean.getRegistermode().equals(RegisterVO.JUMP)) {
			registerBean.setMessage(result);
			try {
				registerHelper.registerMango(registerBean);
			} catch (Exception e) {
				log.error("registerMango error:",e);
				errorId = 4;
				return ERROR;
			}

		}

		// 注册或绑定成功后自动登陆
		String loginCode = result.get("openid").toString();

		Long csn = null;
		try {			
			TsIntUser oTsIntUser = thirdPartyRegisterService.queryThirdPartyUserByLoginCode(loginCode);
			if(null != oTsIntUser){
				csn = oTsIntUser.getCsn();
			}
			MbrSession mbrSession = registerService.checkLogin(csn.toString(), null);
			List<Mbrship> mbrShipList = mbrSession.getSessionMbrShipList();
			nick = CodeFilter.toHtml(nick);
			
			try {
				mbrSession.setSessionMbrNetName(java.net.URLEncoder.encode(nick, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				log.error("TencentRegisterAction-register:",e);
			}
			log.info("method register nick=" + nick);

			//T腾讯合作：9303400001
			for (Mbrship mbrship : mbrShipList) {
				if ("9303400001".equals(mbrship.getMbrshipCategoryCd())) {
					mbrSession.setSessionDefaultMbrshipCd(mbrship.getOldMbrshipCd());
					break;
				}
			}
			if (!mbrShipList.isEmpty()) {
				LoginUtils.setCookies(mbrSession, this.getResponse());				
				// add Caibei Cookie in 2011.08.15 start
				LoginUtils.setCaibeiCookies(loginCode, null, null,null,null,null, this.getRequest(), this.getResponse());
			}
			
		}catch (RegisterServiceException e) {
			log.error("TencentRegisterAction-register:",e);
		}catch (ServiceException e) {
			log.error("TencentRegisterAction-register:",e);
		} 

		if (registerBean.getRegistermode().equals(RegisterVO.REG))
			return REG_SUCCESS;
		else if (registerBean.getRegistermode().equals(RegisterVO.BND))
			return BIND_SUCCESS;
		else
			return SUCCESS;

	}

	private boolean bindValidate() {
		String loginType = "";
		String loginId;
		if (registerBean.getPhonenumber() != null) {
			loginType = "M";
			loginId = registerBean.getPhonenumber();
			errorId = 1;
		} else {
			loginType = "E";
			loginId = registerBean.getEmail();
			errorId = 2;
		}
		
		PersonMainInfo personMainInfo = null;
		try {
			personMainInfo = crmRegisterService.crmMbrLogin(loginType, loginId, registerBean.getPassword());
		} catch (RegisterServiceException e) {
			e.printStackTrace();
			return false;
		}
		if(null != personMainInfo && personMainInfo.getMbrId() != null && personMainInfo.getMbrId().toString().length() > 0) {
			return true;
		}
		return false;
	}

	private boolean registValidate() {
		try {

			if (registerBean.getPhonenumber() == null) {
				errorId = 3;
				return false;
			}
			if (registerBean.getEmail() == null) {
				errorId = 4;
				return false;
			}

			if (registerBean.getPhonenumber() != null
					&& !registerService.validateLoginNameUnique(registerBean.getPhonenumber(), "M")) {
				errorId = 3;
				return false;
			}
			if (registerBean.getEmail() != null
					&& !registerService.validateLoginNameUnique(registerBean.getEmail(), "E")) {
				errorId = 4;
				return false;
			}

			if (registerBean.getPassword() == null || registerBean.equals("")) {
				errorId = 5;
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	public RegisterVO getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(RegisterVO registerBean) {
		this.registerBean = registerBean;
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

	public int getErrorId() {
		return errorId;
	}

	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}

	public void setRegisterHelper(IRegisterHelper registerHelper) {
		this.registerHelper = registerHelper;
	}

	public String getRandCode() {
		return randCode;
	}

	public void setRandCode(String randCode) {
		this.randCode = randCode;
	}

	public IRegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}

	public IThirdPartyRegisterService getThirdPartyRegisterService() {
		return thirdPartyRegisterService;
	}

	public ICrmRegisterService getCrmRegisterService() {
		return crmRegisterService;
	}

	public void setCrmRegisterService(ICrmRegisterService crmRegisterService) {
		this.crmRegisterService = crmRegisterService;
	}

}
