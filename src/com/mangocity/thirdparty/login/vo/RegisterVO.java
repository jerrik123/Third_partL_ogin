package com.mangocity.thirdparty.login.vo;

import java.util.Map;

/**
 * 公共注册Bean
 * @author yangxing
 *
 */
public class RegisterVO extends BaseVO {
	public static final String REG="1";
	public static final String BND="2";
	public static final String JUMP="3";
	
	public static final String REG_TYPE_QQ = "1";
	public static final String REG_TYPE_QMANGO = "2";
	public static final String REG_TYPE_CAIBEI = "3";
	public static final String REG_TYPE_51BI = "4";

	/**
	 * 手机号码
	 */
	private String phonenumber;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 注册类型 1-QQ 2 青芒果 3:QQ彩贝 4:51比购
	 */
	private String registertype;
	
	/**
	 * 注册方式 1-注册 2-绑定 3-跳过
	 */
	private String registermode;
	
	private Map<String,String> message;
	
	private Integer loginType;//0注册 1邮箱绑定 2手机绑定

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegistertype() {
		return registertype;
	}

	public void setRegistertype(String registertype) {
		this.registertype = registertype;
	}

	public String getRegistermode() {
		return registermode;
	}

	public void setRegistermode(String registermode) {
		this.registermode = registermode;
	}

	public Map<String, String> getMessage() {
		return message;
	}

	public void setMessage(Map<String, String> message) {
		this.message = message;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	
	
}
