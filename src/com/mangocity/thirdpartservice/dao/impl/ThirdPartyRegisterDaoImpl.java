package com.mangocity.thirdpartservice.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.framework.base.dao.impl.HibernateDaoImpl;
import com.mangocity.thirdpartservice.dao.IThirdPartyRegisterDao;
import com.mangocity.thirdpartservice.dao.exception.ThirdPartyRegisterDaoException;
import com.mangocity.thirdpartservice.domain.TsIntUser;

/**
 * 操作第三方注册表T_MBR_TS_INT_USER
 * @author chenjun
 * 提供接口有：
 * 添加
 * 查询
 */
public class ThirdPartyRegisterDaoImpl extends HibernateDaoImpl implements IThirdPartyRegisterDao {

	/**
	 * 添加第三方会员信息
	 */
	public void addThirdPartyUser(TsIntUser tsIntUser)
			throws ThirdPartyRegisterDaoException {
		this.saveOrUpdate(tsIntUser);
		
	}

	/**
	 * 通过登录编码查询第三方会员信息
	 */
	public TsIntUser queryThirdPartyUserByLoginCode(String loginCode)
			throws ThirdPartyRegisterDaoException {
		@SuppressWarnings("unchecked")
		List<TsIntUser> results = hqlQuery("select t from " + TsIntUser.class.getName() + " t where t.loginCode='"+loginCode+"'");
		if(results != null && !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}
	
	/**
	 * 操作第三方注册表
	 * 通过登录名称查询会员接口
	 * @param loginName
	 */
	public TsIntUser queryThirdPartyUserByLoginName(String loginName,Integer type)
			throws ThirdPartyRegisterDaoException{
		List<TsIntUser> results = hqlQuery("select t from " + TsIntUser.class.getName() + " t where t.loginName='"+loginName+"' and t.type='"+type+"'");
		if(results != null && !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

	public Map<Long,String> queryLoginNickByCsn(String csns)
			throws ThirdPartyRegisterDaoException {
		Map maps = new HashMap<Long,String>();
		List<Map<Long,String>> results = hqlQuery("select new Map(t.csn as key, t.loginCode as value) from " + TsIntUser.class.getName() + " t where t.csn in ("+csns+")");
		if(results != null && !results.isEmpty()) {
			for(Map<Long,String> map : results){
				maps.put(map.get("key"), map.get("value"));
			}
		}
		return maps;
	}

}
