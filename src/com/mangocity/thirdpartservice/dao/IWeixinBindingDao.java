package com.mangocity.thirdpartservice.dao;

import com.mangocity.framework.base.dao.HibernateDao;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.thirdpartservice.domain.WeixinBindingInfo;

public interface IWeixinBindingDao extends HibernateDao {
	
	/**
	 * 查找微信绑定信息
	 * @param weixinBindingInfo
	 * @return
	 * @throws DaoException
	 */
	public WeixinBindingInfo retrieveWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws DaoException;
	
	/**
	 * 根据openId查找绑定信息
	 * @param weixinBindingInfo
	 * @return
	 * @throws DaoException
	 */
	public WeixinBindingInfo retrieveWeixinBindingInfoByOpenId(WeixinBindingInfo weixinBindingInfo) throws DaoException;
	
	/**
	 * 插入微信绑定信息
	 * @param weixinBindingInfo
	 * @return
	 * @throws DaoException
	 */
	public void insertWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws DaoException;
	
	/**
	 * 更新微信绑定信息
	 * @param weixinBindingInfo
	 * @return
	 * @throws DaoException
	 */
	public int updateWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws DaoException;

}
