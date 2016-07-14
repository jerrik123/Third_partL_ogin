package com.mangocity.thirdparty.login.helper.qmango;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 责任链的请求信息
 * @author shilin.pan
 *
 */
public class VerifyRequest {
	private String uid;
	private String key;
	private Map<String,String> qmangoParams;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Map<String, String> getQmangoParams() {
		return qmangoParams;
	}
	public void setQmangoParams(Map<String, String> qmangoParams) {
		this.qmangoParams = qmangoParams;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	
	
	
	

}
