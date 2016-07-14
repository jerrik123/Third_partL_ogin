package com.mangocity.thirdparty.login.helper.qmango;

import java.util.List;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdparty.login.utils.Constants;
import com.mangocity.thirdparty.login.utils.LoginUtils;

/**
 * 自动完成用户登录,首先看用记是否已经绑定,如果没则
 * 执行绑定.
 * @author shilin.pan
 *
 */
public class LoginHandler extends Handler {
	private IThirdPartyRegisterService thirdPartyRegisterService;
	private IRegisterService registerService;

	public void handleRequest(VerifyRequest request) {
		Long csn = null;
		try {
			TsIntUser oTsIntUser = thirdPartyRegisterService.queryThirdPartyUserByLoginCode(request.getQmangoParams().get(Constants.QMANGO.UID));
			if(null != oTsIntUser){
				csn = oTsIntUser.getCsn();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == csn){
			this.getSuccessHandler().handleRequest(request);
			try {
				TsIntUser oTsIntUser = thirdPartyRegisterService.queryThirdPartyUserByLoginCode(request.getQmangoParams().get(Constants.QMANGO.UID));
				if(null != oTsIntUser){
					csn = oTsIntUser.getCsn();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		MbrSession mbrSession =null;
		try {
			mbrSession = registerService.checkLogin(csn.toString(), null);
			List<Mbrship> mbrShipList = mbrSession.getSessionMbrShipList();
			//青芒果：0100100001
			for (Mbrship mbrship : mbrShipList) {
				if (Constants.QMANGO.MANGO_MEMBERSHIP.equals(mbrship.getMbrshipCategoryCd())) {
					mbrSession.setSessionDefaultMbrshipCd(mbrship.getOldMbrshipCd());
					LoginUtils.setCookies(mbrSession, request.getResponse());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setThirdPartyRegisterService(
			IThirdPartyRegisterService thirdPartyRegisterService) {
		this.thirdPartyRegisterService = thirdPartyRegisterService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}
	
	
	

}
