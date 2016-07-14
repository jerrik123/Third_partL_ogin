package com.mangocity.thirdpartservice.dao.impl;

import java.util.List;

import com.mangocity.framework.base.dao.impl.HibernateDaoImpl;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.thirdpartservice.dao.IYoubangRecordDao;
import com.mangocity.thirdpartservice.domain.YoubangRecordInfo;

public class YoubangRecordDaoImpl extends HibernateDaoImpl implements IYoubangRecordDao {
	
	/**
	 * 创建友邦记录信息
	 * @throws DaoException
	 */
	public void recordYoubangInfo(YoubangRecordInfo youbangRecordInfo) throws DaoException {
		this.saveOrUpdate(youbangRecordInfo);
	}
	
	
	/**
	 * 根据手机号码查询友邦记录信息
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public YoubangRecordInfo queryRecordInfoByMobileNo(String mobileNo) throws DaoException {
		List<YoubangRecordInfo> results = hqlQuery("select t from " + YoubangRecordInfo.class.getName() + " t where t.mobileNo='"+mobileNo+"'");
		if(results != null && !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}
	
}
