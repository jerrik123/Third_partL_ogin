package com.mangocity.thirdpartservice.service.impl;

import java.util.Map;

import com.mangocity.framework.base.service.impl.BaseServiceImpl;
import com.mangocity.thirdpartservice.dao.IThirdPartyRegisterDao;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdpartservice.service.exception.ThirdPartyRegisterServiceException;

/**
 * 操作第三方注册表T_MBR_TS_INT_USER
 * @author chenjun
 * 提供接口有：
 * 添加
 * 查询
 */
public class ThirdPartyRegisterServiceImpl extends BaseServiceImpl implements IThirdPartyRegisterService {
	private IThirdPartyRegisterDao thirdPartyRegisterDao;
	
	/**
	 * 添加第三方会员信息
	 */
	public void addThirdPartyUser(TsIntUser tsIntUser)
			throws ThirdPartyRegisterServiceException {
		thirdPartyRegisterDao.addThirdPartyUser(tsIntUser);
	}
	
	/**
	 * 通过登录编码查询第三方会员信息
	 */
	public TsIntUser queryThirdPartyUserByLoginCode(String loginCode)
			throws ThirdPartyRegisterServiceException {
		return thirdPartyRegisterDao.queryThirdPartyUserByLoginCode(loginCode);
	}
	
	/**
	 * 操作第三方注册表
	 * 通过登录名称查询会员接口
	 * @param loginName
	 * @return
	 * @throws ThirdPartyRegisterServiceException
	 */
	public TsIntUser queryThirdPartyUserByLoginName(String loginName,Integer type) 
			throws ThirdPartyRegisterServiceException{
		return thirdPartyRegisterDao.queryThirdPartyUserByLoginName(loginName,type);
	}
	
	public IThirdPartyRegisterDao getThirdPartyRegisterDao() {
		return thirdPartyRegisterDao;
	}

	public void setThirdPartyRegisterDao(
			IThirdPartyRegisterDao thirdPartyRegisterDao) {
		this.thirdPartyRegisterDao = thirdPartyRegisterDao;
	}

	public Map<Long,String> queryLoginNickByCsn(String csns)
			throws ThirdPartyRegisterServiceException {
		return thirdPartyRegisterDao.queryLoginNickByCsn(csns);
	}
}
