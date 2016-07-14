package com.mangocity.thirdparty.login.helper.qmango;

/**
 * 责任链的节点
 * @author shilin.pan
 *
 */
public abstract class Handler {
	private Handler successHandler;
	public abstract void handleRequest(VerifyRequest request);
	public Handler getSuccessHandler() {
		return successHandler;
	}
	public void setSuccessHandler(Handler successHandler) {
		this.successHandler = successHandler;
	}
	
	

}
