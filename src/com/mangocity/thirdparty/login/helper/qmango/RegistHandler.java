package com.mangocity.thirdparty.login.helper.qmango;

import com.mangocity.thirdparty.login.helper.IRegisterHelper;
import com.mangocity.thirdparty.login.utils.Constants;
import com.mangocity.thirdparty.login.vo.RegisterVO;

/**
 * 用户注册
 * @author shilin.pan
 *
 */
public class RegistHandler extends Handler {
	private IRegisterHelper registerHelper;
	@Override
	public void handleRequest(VerifyRequest request) {
		RegisterVO vo = new RegisterVO();
		//
		vo.setEmail(request.getQmangoParams().get(Constants.QMANGO.PAR_EMAIL));
		vo.setPhonenumber(request.getQmangoParams().get(Constants.QMANGO.PAR_MOBILE));
		vo.setRegistermode("1");
		vo.setRegistertype(Constants.QMANGO.REGISTE_TYPE);
		//vo.setPassword(MD5.encode(java.util.UUID.randomUUID().toString()));
		vo.setPassword(request.getQmangoParams().get("pwd"));
		vo.setMessage(request.getQmangoParams());
		try {
			registerHelper.registerMango(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void setRegisterHelper(IRegisterHelper registerHelper) {
		this.registerHelper = registerHelper;
	}
	
	

}
