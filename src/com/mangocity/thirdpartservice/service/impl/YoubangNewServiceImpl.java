package com.mangocity.thirdpartservice.service.impl;

import com.mangocity.framework.base.service.impl.BaseServiceImpl;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.thirdpartservice.dao.IYoubangNewDao;
import com.mangocity.thirdpartservice.domain.YoubangNewInfo;
import com.mangocity.thirdpartservice.service.IYoubangNewService;

public class YoubangNewServiceImpl extends BaseServiceImpl implements IYoubangNewService{
	
	private IYoubangNewDao youbangNewDao;
	
	/**
	 * 创建友邦记录信息
	 * @param youbangRecordInfo
	 * @throws ServiceException
	 */
	public void recordYoubangNewInfo(YoubangNewInfo youbangNewInfo)
		throws ServiceException {
		youbangNewDao.recordYoubangNewInfo(youbangNewInfo);
	}

	
	public void setYoubangNewDao(IYoubangNewDao youbangNewDao) {
		this.youbangNewDao = youbangNewDao;
	}

}
