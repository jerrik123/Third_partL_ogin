package com.mangocity.thirdparty.login.helper.impl;

import java.util.Calendar;
import java.util.Date;

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
import com.mangocity.thirdparty.login.utils.Constants;
import com.mangocity.thirdparty.login.vo.RegisterVO;
import com.mangocity.util.ConstantArgs;

public class BigouRegisterHelper implements IRegisterHelper {
	
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
	 * @param registerBean
	 * @return
	 */
	public void bindMango(RegisterVO registerBean) throws Exception {
		
	}

	/**
	 * 注册新的芒果会员，并将其与第三方应用绑定
	 * @param registerBean
	 * @return
	 * @throws Exception 
	 */
	public void registerMango(RegisterVO registerBean) throws Exception {
		String resultMessage = "";
		log.info("Enter into BigouRegisterHelper.registerMango()");
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
			log.error("注册新的芒果会员，并将其与第三方应用绑定出现异常",e);
			resultMessage = "1";
		}
		if(resultMessage.equals("1")) {
			throw new Exception("Bind failed!!");
		}
		log.info("Exit BigouRegisterHelper.registerMango()");
		
	}
	
	/**
	 * 构造接口用户表记录
	 * @param registerBean
	 * @return
	 * @throws Exception
	 */
	private TsIntUser createUserBean(RegisterVO registerBean) throws Exception  {		
		log.info("Enter into BigouRegisterHelper.createUserBean()");
		TsIntUser user = new TsIntUser();
		user.setType(7);
		user.setLoginCode(registerBean.getMessage().get("username"));
		user.setLoginName(registerBean.getMessage().get("username"));
		user.setNick(registerBean.getMessage().get("username"));
		user.setSaftkey(registerBean.getMessage().get("usersafekey"));
		user.setGendar(1);
		Date birthday = Calendar.getInstance().getTime();
		user.setBirthday(birthday);
		log.info("Exit BigouRegisterHelper.createUserBean()");
		return user;
	}
	
	private PersonMainInfo registerMbr(RegisterVO registerVO) {
		PersonMainInfo personMainInfo = new PersonMainInfo();
		personMainInfo.setReqIp("");

		personMainInfo.setUpdateBy("ThirdParty");
		personMainInfo.setCreateBy("ThirdParty");

		// 设置是否上传会员信息到CRM的标识
		try {
			personMainInfo.setAttribute(mbrhisCateService.isCTSMbr(Constants.CON_MEMBERSHIPCODE_BIGOU));
		} catch (MbrshipCategoryServiceException e1) {
			log.error(e1);
			personMainInfo.setAttribute(0);
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
		
		// 若为直接直接登录，则构造email注册
		if ("3".equals(registerVO.getRegistermode())) {
			personMainInfo.setEmailAddr(registerVO.getMessage().get("username"));
			personMainInfo.setLoginPwd("1234561");
		}
		
		personMainInfo.setGender("99");
		personMainInfo.setSrc(ConstantArgs.SRC_SIX);
		personMainInfo.setRgstWay("1");
		
		//注册渠道   11表示web注册
		personMainInfo.setChannelNo(ConstantArgs.ENROLLMENTCHANNEL_WEB_11);
		
		// 芒果网简体网站
		personMainInfo.setRegisterSrcId(ConstantArgs.REGISTER_SRC_ID_WEB);
		personMainInfo.setMbrshipCategoryCd(Constants.CON_MEMBERSHIPCODE_BIGOU);
		personMainInfo.setMobileCountryCd("86");
		personMainInfo.setAliasNo("");
		
		// 固定不动
		personMainInfo.setIsLoginType(ConstantArgs.IS_LOGIN_TYPE);
		
		// 默认51比购合作会籍
		try {
			personMainInfo.setCategoryName(mbrhisCateService.mbrshipCategoryByCategoryCd(Constants.CON_MEMBERSHIPCODE_BIGOU).getMbrshipCategoryId().toString());
		} catch (MbrshipCategoryServiceException e) {
			log.error("添加51比购会籍失败",e);
			return null;
		}
		
		// 创建会员并返回会员ID
		try {
			personMainInfo = (PersonMainInfo) this.crmRegisterService.crmMbrRegister(personMainInfo);
		} catch (CrmRegisterServiceException e) {
			log.error("51比购创建会员失败",e);
			return null;
		}
		
		if( ConstantArgs.EMAILADDR_EXIST.equals(personMainInfo.getReturnTyp())
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
