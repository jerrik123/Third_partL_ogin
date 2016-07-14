package com.mangocity.thirdparty.login.helper.impl;

import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.services.crm.CrmRegisterServiceException;
import com.mangocity.services.crm.ICrmRegisterService;
import com.mangocity.services.mbrship.IMbrshipCategoryService;
import com.mangocity.services.mbrship.MbrshipCategoryServiceException;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdparty.login.helper.IFanliRegisterHelper;
import com.mangocity.thirdparty.login.utils.CommonUtil;
import com.mangocity.thirdparty.login.vo.FanliParamVo;
import com.mangocity.util.ConstantArgs;

public class FanliRegisterHelper implements IFanliRegisterHelper {
	
	/**
	 * 会籍类型 服务接口
	 */
	private IMbrshipCategoryService mbrhisCateService;
	/**
	 * 调用EJB会员注册接口
	 */
	private ICrmRegisterService crmRegisterService;
	
	private IThirdPartyRegisterService thirdPartyRegisterService;
	
	/**
	 * 返利用户注册芒果会员接口
	 * @author panshilin
	 *
	 */
	public PersonMainInfo registerMangoFromFanli(FanliParamVo fanliParamVo) throws Exception {
		System.out.println("Enter into 51fanli registerMangoFromFanli()");
		PersonMainInfo personMainInfo = null;
		try {
			TsIntUser tsIntUser = createUserBean(fanliParamVo);
			personMainInfo = registerMbr(fanliParamVo);
			//插入数据库
			if (personMainInfo != null && personMainInfo.getMbrId() != null) {
				tsIntUser.setCsn(personMainInfo.getMbrId());
				thirdPartyRegisterService.addThirdPartyUser(tsIntUser);
			}else{
				System.out.println("personMainInfo : ===> null");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("Out from 51fanli registerMangoFromFanli()");
		return personMainInfo;
	}
	
	
	/**
	 * 构造接口用户表记录(51返利)
	 * @param registerBean
	 * @return
	 * @throws Exception
	 */
	private TsIntUser createUserBean(FanliParamVo fanliParamVo) throws Exception  {		
		System.out.println("构造51返利用户记录表对象start");
		TsIntUser user = new TsIntUser();
		user.setType(5);//5标识为51返利
		user.setLoginCode(fanliParamVo.getU_id());
		user.setLoginName(fanliParamVo.getUsername());
		user.setNick(fanliParamVo.getUsername());
		user.setGendar(1);//默认1，男性
		user.setSaftkey(fanliParamVo.getUsersafekey());
		System.out.println("构造51返利用户记录表对象end");
		return user;
	}
	
	/**
	 * 调用注册接口
	 * @param registerVO
	 * @return
	 */
	private PersonMainInfo registerMbr(FanliParamVo fanliParamVo) {
		PersonMainInfo personMainInfo = new PersonMainInfo();
		personMainInfo.setReqIp("");
		//projecode合作字段
		personMainInfo.setProjectCode(fanliParamVo.getChannelid());
		personMainInfo.setUpdateBy("ThirdParty");
		personMainInfo.setCreateBy("ThirdParty");

		// 设置是否上传会员信息到CRM的标识，默认51注册的是芒果网站会籍0100100001
		try {
			personMainInfo.setAttribute(mbrhisCateService.isCTSMbr("0100100001"));
		} catch (MbrshipCategoryServiceException e1) {
			e1.printStackTrace();
			return null;
		}
		
		// 添加会员类型为散客类型
		personMainInfo.setMbrTyp(ConstantArgs.MBR_TYP_INDIVIDUAL);
		
		personMainInfo.setLoginName("");
		
		personMainInfo.setMbrNetName(fanliParamVo.getUsername());
		personMainInfo.setIsAgreeSendPromotion("0");
		personMainInfo.setLastName(ConstantArgs.SYMBOL);
		personMainInfo.setFirstName(ConstantArgs.SYMBOL);
		personMainInfo.setMiddleName(ConstantArgs.SYMBOL);
		personMainInfo.setLoginPwd(CommonUtil.getRandomPwd());//获取随机密码
		personMainInfo.setEmailAddr(fanliParamVo.getEmail());
		personMainInfo.setMobileNo("");
		
		// 如果是false的话，直接用返利过来的邮箱注册;如果是true的话，则用返利用户名+.com邮箱注册
		if ("false".equals(fanliParamVo.getEmailFlag())) {
			personMainInfo.setEmailAddr(fanliParamVo.getEmail());
		}else if("true".equals(fanliParamVo.getEmailFlag())){
			personMainInfo.setEmailAddr(fanliParamVo.getUsername()+".com");
		}
		
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
		
		// 默认会籍
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
			System.out.println("==调用EJB注册接口返回的信息personMainInfo.getReturnTyp()====>"+personMainInfo.getReturnTyp());
			return null;
		}
		return personMainInfo;
	}


	public IMbrshipCategoryService getMbrhisCateService() {
		return mbrhisCateService;
	}


	public void setMbrhisCateService(IMbrshipCategoryService mbrhisCateService) {
		this.mbrhisCateService = mbrhisCateService;
	}


	public ICrmRegisterService getCrmRegisterService() {
		return crmRegisterService;
	}


	public void setCrmRegisterService(ICrmRegisterService crmRegisterService) {
		this.crmRegisterService = crmRegisterService;
	}


	public IThirdPartyRegisterService getThirdPartyRegisterService() {
		return thirdPartyRegisterService;
	}


	public void setThirdPartyRegisterService(
			IThirdPartyRegisterService thirdPartyRegisterService) {
		this.thirdPartyRegisterService = thirdPartyRegisterService;
	}

}
