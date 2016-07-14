package com.mangocity.thirdparty.login.utils;

public class Constants {

	public final static String CON_MEMBERSHIPCODE_QQ = "9303400001";
	public final static String CON_MEMBERSHIPCODE_BIGOU = "9313400001";
	
	/** 判断腾讯账户是否已经绑定*/
	public final static String IS_QQ_DUPLICATE_BINDING = "/isQQDuplicateBinding.do";
	/**根据token加载用户信息*/
	public final static String LOGIN_URL = "/loadLoginMemberInfo.do";
	/**删除重复用信息*/
	public final static String DELETE_DUPLICATE = "/deleteDuplicateData.do";
	/**根据登陆账户获得MbrId*/
	public final static String GET_MBRID_BY_LOGIN_NAME = "/getMbrIdByLoginName.do";
	/**解绑账户*/
    public static String UNBINDING_ACCOUNT = "/unBindingAccount.do";
	
	public final class QMANGO{
		public final static String UID="uid";
		public final static String KEY="key";
		public final static String REGISTE_TYPE="2";
		public final static String PAR_EMAIL="mobile";
		public final static String PAR_MOBILE="email";
		public final static String PAR_GENDER="gender";
		public final static String PAR_NAME="name";
		public final static String MANGO_MEMBERSHIP="0100100001";
		
		
	}

}
