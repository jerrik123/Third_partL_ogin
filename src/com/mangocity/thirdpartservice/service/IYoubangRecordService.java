package com.mangocity.thirdpartservice.service;

import com.mangocity.framework.base.service.BaseService;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.thirdpartservice.domain.YoubangRecordInfo;

public interface IYoubangRecordService extends BaseService{
	
	/**
	 * 创建友邦记录信息
	 * @param youbangRecordInfo
	 * @throws ServiceException
	 */
	public void recordYoubangInfo(YoubangRecordInfo youbangRecordInfo) throws ServiceException;
	
	
	/**
	 * 根据手机号码查询友邦记录信息
	 * @param mobileNo
	 * @return
	 * @throws ServiceException
	 */
	public YoubangRecordInfo queryRecordInfoByMobileNo(String mobileNo) throws ServiceException;
	
}
