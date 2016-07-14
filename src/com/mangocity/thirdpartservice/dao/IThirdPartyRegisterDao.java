package com.mangocity.thirdpartservice.dao;

import java.util.List;
import java.util.Map;

import com.mangocity.framework.base.dao.HibernateDao;
import com.mangocity.thirdpartservice.dao.exception.ThirdPartyRegisterDaoException;
import com.mangocity.thirdpartservice.domain.TsIntUser;

/**
 * 操作第三方注册表T_MBR_TS_INT_USER
 * @author chenjun
 * 提供接口有：
 * 添加
 * 查询
 */
public interface IThirdPartyRegisterDao extends HibernateDao{
	/**
	 * 操作第三方注册表添加会员接口
	 * @param tsIntUser
	 * @throws AddThirdPartyUserDaoException
	 */
	public void addThirdPartyUser(TsIntUser tsIntUser) throws ThirdPartyRegisterDaoException;
	
	/**
	 * 操作第三方注册表
	 * 通过编码查询会员接口
	 * @param loginCode
	 * @return
	 * @throws QueryThirdPartyUserException
	 */
	public TsIntUser queryThirdPartyUserByLoginCode(String loginCode) throws ThirdPartyRegisterDaoException;
	
	/**
	 * 操作第三方注册表
	 * 通过登录名称查询会员接口
	 * @param loginName
	 */
	public TsIntUser queryThirdPartyUserByLoginName(String loginName,Integer type) throws ThirdPartyRegisterDaoException;
	
	/**
	 * 通过会员ID查询loginCode
	 * @param csn(逗号相隔)
	 * @return UID
	 * @throws ThirdPartyRegisterDaoException
	 */
	public Map<Long,String> queryLoginNickByCsn(String csns) throws ThirdPartyRegisterDaoException;
}
