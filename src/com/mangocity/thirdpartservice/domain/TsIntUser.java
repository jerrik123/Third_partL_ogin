package com.mangocity.thirdpartservice.domain;

import java.util.Date;

import com.mangocity.framework.base.domain.entity.Entity;

/**
 * 第三方合作会员对应关系表
 *
 */
@SuppressWarnings("serial")
public class TsIntUser extends Entity {

	/** 
	 * 会员ID
	 */
	private long csn;
	
	/**
	 * 用户类型；1：腾讯用户，2:青芒果，3：QQ彩贝，4：交行积分乐园，5：51返利 7:51比购 8:支付宝
	 */
	private Integer type;
	
	/**
	 * 登录编码，腾讯用户可以是QQ号
	 */
	private String loginCode;
	
	/**
	 * 登录名称
	 */
	private String loginName;
	
	/**
	 * 昵称
	 */
	private String nick;
	
	/**
	 * 性别
	 */
	private Integer gendar;
	
	/**
	 * 生日
	 */
	private Date birthday;
	
	/**
	 * 地区
	 */
	private String region;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 状态；1：有效，0：无效；默认1
	 */
	private Integer status = new Integer(1);
	
	/**
	 * 安全码(暂用于51返利，51比购)
	 */
	private String saftkey;

	
	public long getCsn() {
		return csn;
	}

	public void setCsn(long csn) {
		this.csn = csn;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getGendar() {
		return gendar;
	}

	public void setGendar(Integer gendar) {
		this.gendar = gendar;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSaftkey() {
		return saftkey;
	}

	public void setSaftkey(String saftkey) {
		this.saftkey = saftkey;
	}
}
