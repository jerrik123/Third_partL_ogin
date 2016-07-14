package com.mangocity.framework.exception;

/**
 * 业务服务模块异常类
 * 
 * @author chenjun
 * @since 1.0
 * @see BaseException
 */
public class ServiceException extends BaseException {

	private static final long serialVersionUID = -7086855509188011754L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);
	}
}
