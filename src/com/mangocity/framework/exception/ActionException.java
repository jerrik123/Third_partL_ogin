package com.mangocity.framework.exception;

/**
 * 页面控制模块异常类
 * 
 * @author chenmubing
 * @since 1.0
 * @see BaseException
 */
public class ActionException extends BaseException {

	private static final long serialVersionUID = -7553293136071625348L;

	public ActionException() {
	}
	
	public ActionException(String message) {
		super(message);
	}

}
