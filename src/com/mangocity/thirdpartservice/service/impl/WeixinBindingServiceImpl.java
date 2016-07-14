package com.mangocity.thirdpartservice.service.impl;

import com.mangocity.framework.base.service.impl.BaseServiceImpl;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.thirdpartservice.dao.IWeixinBindingDao;
import com.mangocity.thirdpartservice.domain.WeixinBindingInfo;
import com.mangocity.thirdpartservice.service.IWeixinBindingService;

public class WeixinBindingServiceImpl extends BaseServiceImpl implements IWeixinBindingService {
	
	private IWeixinBindingDao weixinBindingDao;

	public void insertWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws ServiceException {
		weixinBindingDao.insertWeixinBindingInfo(weixinBindingInfo);
	}

	public WeixinBindingInfo retrieveWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws ServiceException {
		return weixinBindingDao.retrieveWeixinBindingInfo(weixinBindingInfo);
	}
	
	public int updateWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws ServiceException {
		return weixinBindingDao.updateWeixinBindingInfo(weixinBindingInfo);
	}
	
	public WeixinBindingInfo retrieveWeixinBindingInfoByOpenId(WeixinBindingInfo weixinBindingInfo) throws ServiceException {
		return weixinBindingDao.retrieveWeixinBindingInfoByOpenId(weixinBindingInfo);
	}

	public void setWeixinBindingDao(IWeixinBindingDao weixinBindingDao) {
		this.weixinBindingDao = weixinBindingDao;
	}
	
}
