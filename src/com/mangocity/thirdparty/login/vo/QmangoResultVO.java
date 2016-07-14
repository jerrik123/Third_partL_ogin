package com.mangocity.thirdparty.login.vo;

public class QmangoResultVO {
	/**
	 * 会员CSN，即MBRID
	 */
	private Long csn;
	/**
	 * 会员注册email
	 */
	private String email;
	/**
	 * 会员注册手机号码
	 */
	private String mobile;
	/**
	 * 会员姓别 ，11为男士，12为女士
	 */
	private String gender;
	/**
	 * 会员名称
	 */
	private String name;
	/**
	 * 会籍类型名称
	 */
	private String categoryName;
	/**
	 * 会籍类型编号
	 */
	private String categoryCd;
	/**
	 * 联名卡卡类型：1：联名卡；2：银行卡；3：无卡4:推广卡；
	 */
	private Long aliasCardTyp;
	
	
	public Long getCsn() {
		return csn;
	}
	public void setCsn(Long csn) {
		this.csn = csn;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getGender() {
		return gender;
	}	
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryCd() {
		return categoryCd;
	}
	public void setCategoryCd(String categoryCd) {
		this.categoryCd = categoryCd;
	}
	public Long getAliasCardTyp() {
		return aliasCardTyp;
	}
	public void setAliasCardTyp(Long aliasCardTyp) {
		this.aliasCardTyp = aliasCardTyp;
	}
}
