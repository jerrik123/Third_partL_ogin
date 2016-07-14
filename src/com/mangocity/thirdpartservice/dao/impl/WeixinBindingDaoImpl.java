package com.mangocity.thirdpartservice.dao.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.framework.exception.DaoException;
import com.mangocity.thirdpartservice.dao.IWeixinBindingDao;
import com.mangocity.thirdpartservice.domain.WeixinBindingInfo;
import com.mangocity.framework.base.dao.impl.HibernateDaoImpl;

public class WeixinBindingDaoImpl extends HibernateDaoImpl implements IWeixinBindingDao {
	
	@SuppressWarnings("unchecked")
	public WeixinBindingInfo retrieveWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws DaoException {
		String mobile = weixinBindingInfo.getMobile();
		String mbrId = String.valueOf(weixinBindingInfo.getMbrId());
		String openId = weixinBindingInfo.getOpenId();
		String hql = null;
		if(mobile != null && !"".equals(mobile)) {
			hql = "select t from " + WeixinBindingInfo.class.getName() 
				+ " t where t.mobile='"+mobile+"' and t.openId='"+openId+"' and t.status=1";
		} else {
			hql = "select t from " + WeixinBindingInfo.class.getName() 
			+ " t where t.mbrId="+mbrId+" and t.openId='"+openId+"' and t.status=1";
		}
		List<WeixinBindingInfo> results = hqlQuery(hql);
		if(results != null && !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}
	
	public void insertWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws DaoException {
		this.saveOrUpdate(weixinBindingInfo);
	}
	
	@SuppressWarnings("unchecked")
	public int updateWeixinBindingInfo(WeixinBindingInfo weixinBindingInfo) throws DaoException {
		String hql = "select t from " + WeixinBindingInfo.class.getName() 
			+ " t where t.mbrId="+weixinBindingInfo.getMbrId() + " and t.mobile='" + weixinBindingInfo.getMobile()
			+ "' and t.openId='"+weixinBindingInfo.getOpenId()+"' and t.status=1";
		List<WeixinBindingInfo> results = hqlQuery(hql);
		WeixinBindingInfo temp = null;
		if(results != null && !results.isEmpty()) {
			for(int i = 0; i < results.size(); i++) {
				temp = results.get(i);
				temp.setUpdateTime(new Date());
				temp.setStatus(0);
				this.saveOrUpdate(temp);
			}
			return 1;
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public WeixinBindingInfo retrieveWeixinBindingInfoByOpenId(WeixinBindingInfo weixinBindingInfo) throws DaoException {
		String openId = weixinBindingInfo.getOpenId();
		String hql = "select t from " + WeixinBindingInfo.class.getName() 
			+ " t where t.openId='"+openId+"' and t.status=1";
		List<WeixinBindingInfo> results = hqlQuery(hql);
		if(results != null && !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

}
