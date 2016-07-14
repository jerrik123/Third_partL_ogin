package com.mangocity.thirdparty.login.helper.qmango;

import java.util.HashMap;
import java.util.Map;

import com.mangocity.mbr.unionlogin.UnionLogin;
import com.mangocity.mbr.unionlogin.exception.LoginException;
import com.mangocity.thirdparty.login.utils.Constants;

/**
 * 从远程验证远程用户是否有效,如果有效则执行本地登录.
 * 
 * @author shilin.pan
 * 
 */
public class RemoteHandler extends Handler {

	private UnionLogin unionLogin;

	@Override
	public void handleRequest(VerifyRequest request) {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put(Constants.QMANGO.UID, request.getUid());
		requestParams.put(Constants.QMANGO.KEY, request.getKey());
		

		try {
			Object resultTmp = unionLogin.syncUserInfo(requestParams);
			if (resultTmp != null) {
				Map<String, String> tmpMap = (Map<String, String>) resultTmp;
				if (tmpMap.get(Constants.QMANGO.UID) != null
						&& !"".equals(tmpMap.get(Constants.QMANGO.UID))) {
					request.setQmangoParams(tmpMap);
					this.getSuccessHandler().handleRequest(request);
				}
			}
		} catch (LoginException e) {
			e.printStackTrace();
		}

	}

	public void setUnionLogin(UnionLogin unionLogin) {
		this.unionLogin = unionLogin;
	}

}
