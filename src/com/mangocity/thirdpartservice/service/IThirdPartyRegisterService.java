package com.mangocity.thirdpartservice.service;

import java.util.List;
import java.util.Map;

import com.mangocity.framework.base.service.BaseService;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.exception.ThirdPartyRegisterServiceException;

/**
 * 操作第三方注册表T_MBR_TS_INT_USER
 * @author chenjun
 * 提供接口有：
 * 添加
 * 查询
 */
public interface IThirdPartyRegisterService extends BaseService{
	/**
	 * 操作第三方注册表添加会员接口
	 * @param tsIntUser
	 * @throws ThirdPartyRegisterServiceException
	 */
	public void addThirdPartyUser(TsIntUser tsIntUser) throws ThirdPartyRegisterServiceException;
	
	/**
	 * 操作第三方注册表
	 * 通过编码查询会员接口
	 * @param loginCode
	 * @return
	 * @throws ThirdPartyRegisterServiceException
	 */
	public TsIntUser queryThirdPartyUserByLoginCode(String loginCode) throws ThirdPartyRegisterServiceException;
	
	
	/**
	 * 操作第三方注册表
	 * 通过登录名称查询会员接口
	 * @param loginName
	 * @return
	 * @throws ThirdPartyRegisterServiceException
	 */
	public TsIntUser queryThirdPartyUserByLoginName(String loginName,Integer type) throws ThirdPartyRegisterServiceException;
	
	/**
	 * 通过csn查询loginNick（批量）
	 * @param csn（逗号相隔的字符串）
	 * @return
	 * @throws ThirdPartyRegisterServiceException
	 */
	public Map<Long,String> queryLoginNickByCsn(String csn) throws ThirdPartyRegisterServiceException;
}
