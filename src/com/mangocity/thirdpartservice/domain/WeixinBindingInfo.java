package com.mangocity.thirdpartservice.domain;

import java.util.Date;

import com.mangocity.framework.base.domain.entity.Entity;

/**
 * 微信登录绑定
 * @author suzhangsheng
 *
 */
public class WeixinBindingInfo extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4376287230359057545L;
	
	/**
	 * 会员ID
	 */
	private Long mbrId;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 微信openId
	 */
	private String openId;
	
	/**
	 * 创建时间
	 */
	private Date createTime; 
	
	/**
	 * 变更时间
	 */
	private Date updateTime;
	
	/**
	 * 状态（0：解绑；1：绑定）
	 */
	private Integer status;

	public Long getMbrId() {
		return mbrId;
	}

	public void setMbrId(Long mbrId) {
		this.mbrId = mbrId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
