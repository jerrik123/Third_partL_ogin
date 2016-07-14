package com.mangocity.thirdparty.login.helper.impl;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.mbr.unionlogin.utils.DateUtil;
import com.mangocity.mbr.unionlogin.utils.StringUtil;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.services.crm.CrmRegisterServiceException;
import com.mangocity.services.crm.ICrmRegisterService;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.services.mbrship.IMbrshipCategoryService;
import com.mangocity.services.mbrship.MbrshipCategoryServiceException;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdparty.login.helper.IRegisterHelper;
import com.mangocity.thirdparty.login.vo.RegisterVO;
import com.mangocity.util.ConstantArgs;

public class TencentRegisterHelper implements IRegisterHelper {
	
	protected Log log = LogFactory.getLog(this.getClass());
    
    private IThirdPartyRegisterService thirdPartyRegisterService;
    
    private IRegisterService registerService; 
    private ICrmRegisterService crmRegisterService;
    /**
	 * 会籍类型 服务接口
	 */
	private IMbrshipCategoryService mbrhisCateService;

    /**
	 * 与芒果会员绑定操作
	 * 绑定前，对输入的用户名和密码进入验证
	 * 1.若输入的手机号码不为空，且数据库不存在，则不能进行绑定
	 * 2.若输入的Email不为空，且数据库不存在，则不能进行绑定
	 * 否则，执行绑定
	 * @param registerBean
	 * @return
	 */
	public void bindMango(RegisterVO registerBean) throws Exception {
		String resultMessage = "";
		log.info("Enter into TencentRegisterProcessor.bindMango()");
		TsIntUser tsIntUser;
		try {
			tsIntUser = createUserBean(registerBean);
			
			// 绑定前，对输入的用户名和密码进入验证
			if (registerBean.getPhonenumber() != null
					&& registerService.validateLoginNameUnique(registerBean.getPhonenumber(), "M")) {
				resultMessage = "1";
			} else if (registerBean.getEmail() != null
					&& registerService.validateLoginNameUnique(registerBean.getEmail(), "E")) {
				resultMessage = "1";
			} else {
				// 校验合法后进行绑定 
				String loginSubType = "";
				String loginId = "";
				if (StringUtils.isNotEmpty(registerBean.getPhonenumber())) {
					loginSubType = "M";
					loginId = registerBean.getPhonenumber();
				} else {
					loginSubType = "E";
					loginId = registerBean.getEmail();
				}
				PersonMainInfo personMainInfo = null;
				// 登录成功后获取会员ID
				personMainInfo = crmRegisterService.crmMbrLogin(loginSubType, loginId, registerBean.getPassword());
				if(null != personMainInfo && personMainInfo.getMbrId() != null && personMainInfo.getMbrId().toString().length() > 0) {
					tsIntUser.setCsn(personMainInfo.getMbrId());
					
					// 执行绑定
					thirdPartyRegisterService.addThirdPartyUser(tsIntUser);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			resultMessage = "1";
		}
		
		if(resultMessage.equals("1")) {
			throw new Exception("Bind failed!!");
		}
		log.info("Enter into TencentRegisterProcessor.bindMango()");
		
	}

	/**
	 * 注册新的芒果会员，并将其与第三方应用绑定
	 * @param registerBean
	 * @return
	 * @throws Exception 
	 */
	public void registerMango(RegisterVO registerBean) throws Exception {
		String resultMessage = "";
		log.info("Enter into TencentRegisterProcessor.registerMango()");
		try {
			TsIntUser tsIntUser = createUserBean(registerBean);
			PersonMainInfo personMainInfo = registerMbr(registerBean);
			
			if (personMainInfo != null && personMainInfo.getMbrId() != null) {
				tsIntUser.setCsn(personMainInfo.getMbrId());
				thirdPartyRegisterService.addThirdPartyUser(tsIntUser);
			}else{
				log.info("personMainInfo : ===> null");
				resultMessage = "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMessage = "1";
		}
		
		if(resultMessage.equals("1")) {
			throw new Exception("Bind failed!!");
		}
		log.info("Exit TencentRegisterProcessor.registerMango()");
		
	}
	
	/**
	 * 构造接口用户表记录
	 * @param registerBean
	 * @return
	 * @throws Exception
	 */
	private TsIntUser createUserBean(RegisterVO registerBean) throws Exception  {		
		log.info("Enter into TencentRegisterProcessor.createUserBean()");
		TsIntUser user = new TsIntUser();
		user.setType(Integer.parseInt(registerBean.getRegistertype()));
		user.setLoginCode(registerBean.getMessage().get("openid"));
		user.setLoginName(registerBean.getMessage().get("openid"));
		user.setNick(registerBean.getMessage().get("nickname"));
		String gendar = registerBean.getMessage().get("gender");
		if(StringUtil.isStringNull(gendar)) {
			gendar = "1";
		}
		user.setGendar(Integer.parseInt(gendar));
		String date = registerBean.getMessage().get("birthday");
		Date birthday = Calendar.getInstance().getTime();
		if(date != null) {
			birthday = DateUtil.getFormatDate(date.substring(0, date.indexOf(":")));
		}
		user.setBirthday(birthday);
		user.setRegion(registerBean.getMessage().get("region"));			
		log.info("Exit TencentRegisterProcessor.createUserBean()");
		return user;
	}
	
	private PersonMainInfo registerMbr(RegisterVO registerVO) {
		PersonMainInfo personMainInfo = new PersonMainInfo();
		personMainInfo.setReqIp("");

		personMainInfo.setUpdateBy("ThirdParty");
		personMainInfo.setCreateBy("ThirdParty");

		// 设置是否上传会员信息到CRM的标识
		try {
			personMainInfo.setAttribute(mbrhisCateService.isCTSMbr("9303400001"));
		} catch (MbrshipCategoryServiceException e1) {
			e1.printStackTrace();
			return null;
		}
		
		// 添加会员类型为散客类型
		personMainInfo.setMbrTyp(ConstantArgs.MBR_TYP_INDIVIDUAL);
		
		personMainInfo.setLoginName("");
		
		personMainInfo.setMbrNetName("");
		personMainInfo.setIsAgreeSendPromotion("0");
		personMainInfo.setLastName(ConstantArgs.SYMBOL);
		personMainInfo.setFirstName(ConstantArgs.SYMBOL);
		personMainInfo.setMiddleName(ConstantArgs.SYMBOL);
		personMainInfo.setLoginPwd(registerVO.getPassword());
		personMainInfo.setEmailAddr(registerVO.getEmail());
		personMainInfo.setMobileNo(registerVO.getPhonenumber());
		
		// 若为直接直接登录，则构造email注册
		if ("3".equals(registerVO.getRegistermode())) {
			personMainInfo.setEmailAddr(registerVO.getMessage().get("openid") + "@qq.mangocity.com");
			personMainInfo.setLoginPwd("1234561");
		}
		
		personMainInfo.setGender("");
		personMainInfo.setSrc(ConstantArgs.SRC_SIX);
		personMainInfo.setRgstWay("1");
		
		//注册渠道   11表示web注册
		personMainInfo.setChannelNo(ConstantArgs.ENROLLMENTCHANNEL_WEB_11);
		
		// 芒果网简体网站
		personMainInfo.setRegisterSrcId(ConstantArgs.REGISTER_SRC_ID_WEB);
		personMainInfo.setMbrshipCategoryCd("9303400001");
		personMainInfo.setMobileCountryCd("86");
		personMainInfo.setAliasNo("");
		
		// 固定不动
		personMainInfo.setIsLoginType(ConstantArgs.IS_LOGIN_TYPE);
		
		// 默认腾讯合作会籍
		try {
			personMainInfo.setCategoryName(mbrhisCateService.mbrshipCategoryByCategoryCd("9303400001").getMbrshipCategoryId().toString());
		} catch (MbrshipCategoryServiceException e) {
			e.printStackTrace();
			return null;
		}
		
		// 创建会员并返回会员ID
		try {
			personMainInfo = (PersonMainInfo) this.crmRegisterService.crmMbrRegister(personMainInfo);
		} catch (CrmRegisterServiceException e) {
			e.printStackTrace();
			return null;
		}
		
		if(ConstantArgs.OBILENO_EXIST.equals(personMainInfo.getReturnTyp()) || ConstantArgs.EMAILADDR_EXIST.equals(personMainInfo.getReturnTyp())
				|| ConstantArgs.MBR_NET_NAME_EXIST.equals(personMainInfo.getReturnTyp()) || ConstantArgs.CERT_NO_IS_EXIST.equals(personMainInfo.getReturnTyp())
				|| ConstantArgs.BLOC_EXIST.equals(personMainInfo.getReturnTyp()) || ConstantArgs.CARD_NO_EXIST.equals(personMainInfo.getReturnTyp())){
			log.info("==调用EJB注册接口返回的信息personMainInfo.getReturnTyp()====>"+personMainInfo.getReturnTyp());
			return null;
		}
		return personMainInfo;
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

	public IThirdPartyRegisterService getThirdPartyRegisterService() {
		return thirdPartyRegisterService;
	}

	public ICrmRegisterService getCrmRegisterService() {
		return crmRegisterService;
	}

	public void setCrmRegisterService(ICrmRegisterService crmRegisterService) {
		this.crmRegisterService = crmRegisterService;
	}

	public IMbrshipCategoryService getMbrhisCateService() {
		return mbrhisCateService;
	}

	public void setMbrhisCateService(IMbrshipCategoryService mbrhisCateService) {
		this.mbrhisCateService = mbrhisCateService;
	}
	
	
	

}
