package com.mangocity.thirdpartservice.service;

import com.mangocity.framework.base.service.BaseService;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.thirdpartservice.domain.YoubangNewInfo;

public interface IYoubangNewService extends BaseService{
	
	/**
	 * 创建友邦记录信息
	 * @param youbangNewInfo
	 * @throws ServiceException
	 */
	public void recordYoubangNewInfo(YoubangNewInfo youbangNewInfo) throws ServiceException;
	
}
