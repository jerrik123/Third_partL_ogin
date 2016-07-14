package com.mangocity.thirdparty.login.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 所有Action的基类，用来处理基础设置或者值的获取
 * @author shilin.pan
 *
 */
public abstract class ApplicationAction extends ActionSupport {
	
	public final static String QQ_LOGIN_SESSION="QQ_LOGIN_SESSION";
	
	/**
	 * 获取ActionContext
	 * @return
	 */
	public ActionContext getContext() {
		return ActionContext.getContext();  
	}
	
	/**
	 * 获取HttpServletRequest
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) getContext().get(ServletActionContext.HTTP_REQUEST);
	}
	
	/**
	 * 获取HttpServletResponse
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return (HttpServletResponse) getContext().get(ServletActionContext.HTTP_RESPONSE);
	}
	
	/**
	 * 获取HttpSession
	 * @return
	 */
	public HttpSession getSession() {
		return getRequest().getSession();
	}
	
	public String catchIdAdress(HttpServletRequest request) {
	        
	        String ip = request.getHeader("clientip");
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("x-forwarded-for");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("Proxy-Client-IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("WL-Proxy-Client-IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getRemoteAddr();
	        }
	        return ip;
	}
	
}
