package com.mangocity.thirdpartservice.domain;

import java.util.Date;

import com.mangocity.framework.base.domain.entity.Entity;

/**
 * 友邦记录对象
 * @author panshilin
 *
 */
public class YoubangRecordInfo extends Entity{
	
	private static final long serialVersionUID = 1826156278229570590L;

	/**
	 * 手机号
	 */
	private String mobileNo;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 地区
	 */
	private String area;
	
	/**
	 * 生日
	 */
	private String birthday;
	
	/**
	 * 创建人
	 */
	private String createBy;
	
	/**
	 * 修改人
	 */
	private String updateBy;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 修改时间
	 */
	private Date updateTime;

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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
	
}
