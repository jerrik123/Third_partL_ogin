package com.mangocity.thirdpartservice.dao;

import com.mangocity.framework.base.dao.HibernateDao;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.thirdpartservice.domain.YoubangRecordInfo;

public interface IYoubangRecordDao extends HibernateDao{
	
	/**
	 * 创建友邦记录信息
	 * @throws DaoException
	 */
	public void recordYoubangInfo(YoubangRecordInfo youbangRecordInfo) throws DaoException;
	
	
	/**
	 * 根据手机号码查询友邦记录信息
	 * @return
	 * @throws DaoException
	 */
	public YoubangRecordInfo queryRecordInfoByMobileNo(String mobileNo) throws DaoException;
	
	
}
