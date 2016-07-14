package com.mangocity.thirdpartservice.dao.impl;

import com.mangocity.framework.base.dao.impl.HibernateDaoImpl;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.thirdpartservice.dao.IYoubangNewDao;
import com.mangocity.thirdpartservice.domain.YoubangNewInfo;

public class YoubangNewDaoImpl extends HibernateDaoImpl implements IYoubangNewDao {
	
	/**
	 * 创建友邦记录信息
	 * @throws DaoException
	 */
	public void recordYoubangNewInfo(YoubangNewInfo youbangNewInfo)
			throws DaoException {
		this.saveOrUpdate(youbangNewInfo);
	}
	
}
