package com.mangocity.thirdparty.login.helper.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

public class QmangoRegisterHelper implements IRegisterHelper {
	protected Log log = LogFactory.getLog(this.getClass());
    
    private IThirdPartyRegisterService thirdPartyRegisterService;
    
    private IRegisterService registerService; 
    private ICrmRegisterService crmRegisterService;
    /**
	 * 会籍类型 服务接口
	 */
	private IMbrshipCategoryService mbrhisCateService;
	  
	 
	public void registerMango(RegisterVO registerBean) throws Exception {
		
//		TsIntUser user = createUserBean(registerBean);
//		RegisterCheckBean checkBean = createRegisterCheckBean(registerBean);
//		LoyaltyAccount loyaltyAccount = createLoyaltyAccount(registerBean);
//		String resultMessage = thirdPartyRegisterService.registerMango(user, checkBean, loyaltyAccount);
		String resultMessage = "";
		log.info("Enter into QmangoRegisterProcessor.registerMango()");
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
		log.info("Exit QmangoRegisterProcessor.registerMango()");
		

	}

	public void bindMango(RegisterVO registerBean) throws Exception {
//		TsIntUser user = createUserBean(registerBean);	
//		LoyaltyAccount loyaltyAccount = createLoyaltyAccount(registerBean);
//		String resultMessage = thirdPartyRegisterService.bindMango(user, loyaltyAccount);
//		if(resultMessage.equals("1"))
//			throw new Exception("Bind failed!!");
		String resultMessage = "";
		log.info("Enter into QmangoRegisterProcessor.bindMango()");
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
		log.info("Enter into QmangoRegisterProcessor.bindMango()");

	}
	
	
	
	/**
	 * 构造接口用户表记录
	 * @param registerBean
	 * @return
	 * @throws Exception
	 */
	private TsIntUser createUserBean(RegisterVO registerBean) throws Exception  {
		log.info("Enter into QmangoRegisterProcessor.createUserBean()");
		TsIntUser user = new TsIntUser();
		user.setType(Integer.parseInt(registerBean.getRegistertype()));
		user.setLoginCode(registerBean.getMessage().get("uid"));
		user.setLoginName(registerBean.getMessage().get("uid"));
		String gendar = registerBean.getMessage().get("gender");
		user.setGendar(gendar.equals("F")?0:1);		
		log.info("Exit QmangoRegisterProcessor.createUserBean()");
		return user;
	}
	
	
	private PersonMainInfo registerMbr(RegisterVO registerVO) {
		PersonMainInfo personMainInfo = new PersonMainInfo();
		personMainInfo.setReqIp("");

		personMainInfo.setUpdateBy("ThirdParty");
		personMainInfo.setCreateBy("ThirdParty");

		// 设置是否上传会员信息到CRM的标识
		try {
			personMainInfo.setAttribute(mbrhisCateService.isCTSMbr("0100100001"));
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
		
		personMainInfo.setGender("");
		personMainInfo.setSrc(ConstantArgs.SRC_SIX);
		personMainInfo.setRgstWay("1");
		
		//注册渠道   11表示web注册
		personMainInfo.setChannelNo(ConstantArgs.ENROLLMENTCHANNEL_WEB_11);
		
		// 芒果网简体网站
		personMainInfo.setRegisterSrcId(ConstantArgs.REGISTER_SRC_ID_WEB);
		personMainInfo.setMbrshipCategoryCd("0100100001");
		personMainInfo.setMobileCountryCd("86");
		personMainInfo.setAliasNo("");
		
		// 固定不动
		personMainInfo.setIsLoginType(ConstantArgs.IS_LOGIN_TYPE);
		
		// 默认芒果网会籍
		try {
			personMainInfo.setCategoryName(mbrhisCateService.mbrshipCategoryByCategoryCd("0100100001").getMbrshipCategoryId().toString());
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

	public void setMbrhisCateService(IMbrshipCategoryService mbrhisCateService) {
		this.mbrhisCateService = mbrhisCateService;
	}

	public void setCrmRegisterService(ICrmRegisterService crmRegisterService) {
		this.crmRegisterService = crmRegisterService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}

	
	

}
