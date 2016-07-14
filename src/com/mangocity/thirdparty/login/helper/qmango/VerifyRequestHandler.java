package com.mangocity.thirdparty.login.helper.qmango;

/**
 * 根据请求职参数中的qmangoId和key验证用户是否为空如果为空直接返回
 * 否
 * @author shilin.pan
 *
 */
public class VerifyRequestHandler extends Handler {

	@Override
	public void handleRequest(VerifyRequest request) {
		if(request.getKey()!=null&&request.getUid()!=null)
			this.getSuccessHandler().handleRequest(request);

	}

}
