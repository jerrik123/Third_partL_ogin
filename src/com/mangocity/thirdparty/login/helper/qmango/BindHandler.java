package com.mangocity.thirdparty.login.helper.qmango;

import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.thirdparty.login.helper.IRegisterHelper;
import com.mangocity.thirdparty.login.utils.Constants;
import com.mangocity.thirdparty.login.vo.RegisterVO;

/**
 * 用户绑定,如果在利用会员的手机号或邮箱可以查出.则绑定,如果不能查出则 执行注册流程.
 * 
 * @author shilin.pan
 * 
 */
public class BindHandler extends Handler {
	private IRegisterHelper registerHelper;
	private IRegisterService registerService; 

	@Override
	public void handleRequest(VerifyRequest request) {
		try {
			if(!registerService.validateLoginNameUnique(request.getQmangoParams().get(Constants.QMANGO.PAR_EMAIL), "E") 
					|| !registerService.validateLoginNameUnique(request.getQmangoParams().get(Constants.QMANGO.PAR_MOBILE), "M")){
				RegisterVO vo = new RegisterVO();
				//
				vo.setEmail(request.getQmangoParams().get(
						Constants.QMANGO.PAR_EMAIL));
				vo.setPhonenumber(request.getQmangoParams().get(
						Constants.QMANGO.PAR_MOBILE));
				vo.setRegistermode("2");
				vo.setRegistertype(Constants.QMANGO.REGISTE_TYPE);
				vo.setPassword(request.getQmangoParams().get("pwd"));
				vo.setMessage(request.getQmangoParams());
				try {
					registerHelper.bindMango(vo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// 执行注册
				this.getSuccessHandler().handleRequest(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}// bind

	}


	public void setRegisterHelper(IRegisterHelper registerHelper) {
		this.registerHelper = registerHelper;
	}

	public IRegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}

}
