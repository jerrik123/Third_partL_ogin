package com.mangocity.thirdparty.login.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 新的MbrServer接口请求工具类
 * @author James
 * @since 2015-3-10
 */
public class HTTPMbrServerHelper extends HttpConnHelper {
	private static String mbrServerIp;  //ip
	
	private static Integer mbrServerPort;  //端口
	
	private static String mbrServerContext;  //上下文
	
	public static String generateServerAddress(){
		return "http://"+mbrServerIp+":"+mbrServerPort+(StringUtils.isBlank(mbrServerContext)?"":"/"+mbrServerContext);
	}
	
	public static String requestMS(String url,Object postParams){		
		return doHttpRequest(generateServerAddress()+url, postParams);
	}

	public static String getMbrServerIp() {
		return mbrServerIp;
	}

	public void setMbrServerIp(String mbrServerIp) {
		HTTPMbrServerHelper.mbrServerIp = mbrServerIp;
	}

	public static Integer getMbrServerPort() {
		return mbrServerPort;
	}

	public void setMbrServerPort(Integer mbrServerPort) {
		HTTPMbrServerHelper.mbrServerPort = mbrServerPort;
	}

	public static String getMbrServerContext() {
		return mbrServerContext;
	}

	public void setMbrServerContext(String mbrServerContext) {
		HTTPMbrServerHelper.mbrServerContext = mbrServerContext;
	}
	
	
}
