package com.mangocity.framework.exception;

/**
 * 基础异常类
 * 
 * @author chenjun
 * @since 1.0
 * @see Exception
 */
public class BaseException extends Exception {

	private static final long serialVersionUID = 3358888911029354719L;
	private String errorCode;
	private String errorMessage;
	public BaseException() {
	}

	public BaseException(String message) {
		super(message);
	}
	public BaseException(String errorCode,String message) {
		super("["+errorCode+"]:"+message);
		this.errorMessage=message;
		this.errorCode=errorCode;
	}
	
	public BaseException(String errorCode,String message,Throwable tr){
		super("["+errorCode+"]:"+message,tr);
		this.errorMessage=message;
		this.errorCode=errorCode;

	}
	
	public BaseException(String errorCode,Throwable tr){
		super("["+errorCode+"]:",tr);
		this.errorMessage="";
		this.errorCode=errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
