package com.mangocity.thirdpartservice.domain;

import java.util.Date;

import com.mangocity.framework.base.domain.entity.Entity;

/**
 * 友邦记录对象2期
 * @author panshilin
 *
 */
public class YoubangNewInfo extends Entity{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6578300357131042261L;

	/**
	 * 会员ID
	 */
	private Long mbrId;
	
	/**
	 * 会籍编号
	 */
	private String oldMbrshipCd;
	
	/**
	 * 记录类型
	 */
	private String type;
	
	/**
	 * 备注
	 */
	private String remark;
	
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

	
	public Long getMbrId() {
		return mbrId;
	}

	public void setMbrId(Long mbrId) {
		this.mbrId = mbrId;
	}

	public String getOldMbrshipCd() {
		return oldMbrshipCd;
	}

	public void setOldMbrshipCd(String oldMbrshipCd) {
		this.oldMbrshipCd = oldMbrshipCd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	
}
