package com.mangocity.thirdparty.login._new.action.model;


import java.io.Serializable;


public class MemberCacheInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7690932436681632276L;
	
	private String mbrID;
	/**
	 * 手机号码
	 */
	private String mobileNo;
	
	/**
	 * 电子邮箱
	 */
	private String emailAddr;
	
	private String mbrshipCd;// 会籍编码串， 多个“,”号分割
	/**
	 * //会员级别
	 */
	private String mbrLevel;
	/**
	 * //会员级别代码
	 */
	private String mbrLevelCode;
	/**
	 * 中文名
	 */
	private String nameCn;
	public String getMbrID() {
		return mbrID;
	}

	public void setMbrID(String mbrID) {
		this.mbrID = mbrID;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public String getMbrshipCd() {
		return mbrshipCd;
	}

	public void setMbrshipCd(String mbrshipCd) {
		this.mbrshipCd = mbrshipCd;
	}

	public String getMbrLevel() {
		return mbrLevel;
	}

	public void setMbrLevel(String mbrLevel) {
		this.mbrLevel = mbrLevel;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getMbrLevelCode() {
		return mbrLevelCode;
	}

	public void setMbrLevelCode(String mbrLevelCode) {
		this.mbrLevelCode = mbrLevelCode;
	}

	@Override
	public String toString() {
		return "MemberCacheInfo [mbrID=" + mbrID + ", mobileNo=" + mobileNo
				+ ", emailAddr=" + emailAddr + ", mbrshipCd=" + mbrshipCd
				+ ", mbrLevel=" + mbrLevel + ", mbrLevelCode=" + mbrLevelCode
				+ ", nameCn=" + nameCn + "]";
	}
	
}
