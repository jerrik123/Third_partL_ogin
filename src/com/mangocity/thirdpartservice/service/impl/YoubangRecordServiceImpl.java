package com.mangocity.thirdpartservice.service.impl;

import com.mangocity.framework.base.service.impl.BaseServiceImpl;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.thirdpartservice.dao.IYoubangRecordDao;
import com.mangocity.thirdpartservice.domain.YoubangRecordInfo;
import com.mangocity.thirdpartservice.service.IYoubangRecordService;

public class YoubangRecordServiceImpl extends BaseServiceImpl implements IYoubangRecordService{
	
	private IYoubangRecordDao youbangRecordDao;
	
	/**
	 * 创建友邦记录信息
	 * @param youbangRecordInfo
	 * @throws ServiceException
	 */
	public void recordYoubangInfo(YoubangRecordInfo youbangRecordInfo)
		throws ServiceException {
		youbangRecordDao.recordYoubangInfo(youbangRecordInfo);
	}
	
	
	/**
	 * 根据手机号码查询友邦记录信息
	 * @param mobileNo
	 * @return
	 * @throws ServiceException
	 */
	public YoubangRecordInfo queryRecordInfoByMobileNo(String mobileNo)
			throws ServiceException {
		return youbangRecordDao.queryRecordInfoByMobileNo(mobileNo);
	}


	public void setYoubangRecordDao(IYoubangRecordDao youbangRecordDao) {
		this.youbangRecordDao = youbangRecordDao;
	}

}
