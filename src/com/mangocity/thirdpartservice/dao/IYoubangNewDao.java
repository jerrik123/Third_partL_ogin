package com.mangocity.thirdpartservice.dao;

import com.mangocity.framework.base.dao.HibernateDao;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.thirdpartservice.domain.YoubangNewInfo;

public interface IYoubangNewDao extends HibernateDao{
	
	/**
	 * 创建友邦记录信息
	 * @throws DaoException
	 */
	public void recordYoubangNewInfo(YoubangNewInfo youbangNewInfo) throws DaoException;
		
	
}