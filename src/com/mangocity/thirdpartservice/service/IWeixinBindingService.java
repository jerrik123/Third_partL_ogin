package com.mangocity.thirdpartservice.service;

import com.mangocity.framework.base.service.BaseService;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.thirdpartservice.domain.WeixinBindingInfo;

public interface IWeixinBindingService extends BaseService{
	
	/**
	 * 查找微信绑定信息
	 * @param weixinBindingInfo
	 * @return
	 * @throws DaoException
	 */
	public WeixinBindingInfo retrieveWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws ServiceException;
	
	/**
	 * 根据openId查找绑定信息
	 * @param weixinBindingInfo
	 * @return
	 * @throws ServiceException
	 */
	public WeixinBindingInfo retrieveWeixinBindingInfoByOpenId(WeixinBindingInfo weixinBindingInfo) throws ServiceException;
	
	/**
	 * 插入微信绑定信息
	 * @param weixinBindingInfo
	 * @throws DaoException
	 */
	public void insertWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws ServiceException;
	
	/**
	 * 更新微信绑定信息
	 * @param weixinBindingInfo
	 * @throws ServiceException
	 */
	public int updateWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws ServiceException;

}
