package com.mangocity.thirdparty.login.action.weixin;

public class ResultCode {
	
	// 已绑定
	public static final String IS_BINDING = "000";
	
	// 未绑定
	public static final String IS_NOT_BINDING = "001";
	
	// 绑定成功
	public static final String BINDING_SUCCESS = "002";
	
	// 已绑定，已注册
	public static final String IS_BINDING_AND_EXIST = "003";
	
	// 已绑定，未注册
	public static final String IS_BINDING_AND_NOT_EXIST = "004";
	
	// 未绑定，已注册
	public static final String IS_NOT_BINDING_AND_EXIST = "005";
	
	// 未绑定，未注册
	public static final String IS_NOT_BINDING_AND_NOT_EXIST = "006";
	
	// 解绑成功
	public static final String UNBIND_SUCCESS = "007";
	
	// 解绑失败
	public static final String UNBIND_FAIL = "008";
	
	// 查找成功
	public static final String SEARCH_SUCCESS = "009";
	
	// OpenId不存在
	public static final String OPENID_NOT_EXIST = "010";
	
	// 不允许get方法
	public static final String GET_ERROR = "102";
	
	// json为空
	public static final String JSON_EMPTY = "103";
	
	// 系统错误
	public static final String SYSTEM_ERROR = "104";
	
	// 必要参数为空
	public static final String PARAM_EMPTY = "105";
	
	// 会员ID不存在
	public static final String MEMBER_NOT_EXIST = "106";
	
	// 手机号码与会员系统中的不对应
	public static final String MOBILE_NOT_CONSISTENT = "107";

}
