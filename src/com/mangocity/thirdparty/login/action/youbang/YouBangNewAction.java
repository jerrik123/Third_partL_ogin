package com.mangocity.thirdparty.login.action.youbang;

import java.util.Date;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.thirdpartservice.domain.YoubangNewInfo;
import com.mangocity.thirdpartservice.service.IYoubangNewService;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.utils.CookieUtil;



/**
 * 友邦项目2期收集入口
 * @author panshilin
 *
 */
public class YouBangNewAction extends ApplicationAction{
	
	private static final long serialVersionUID = -5051514358286415748L;

	private IYoubangNewService youbangNewService;
	
	private String result;
	
	private String type;
	
	
	/**
	 * 初始化页面
	 * @return
	 */
	public String initYoubang(){
		return SUCCESS;
	}
	
	
	/**
	 * 记录友邦信息
	 * @return
	 */
	public String recordYoubang(){
		result = "1";
		String mbrId = CookieUtil.readCookie("mbrID", getRequest());
		String memberCd = CookieUtil.readCookie("membercd", getRequest());
		if(null == mbrId || "".equals(mbrId)){
			return SUCCESS;
		}
		YoubangNewInfo youbangNewInfo = new YoubangNewInfo();
		youbangNewInfo.setMbrId(Long.parseLong(mbrId));
		youbangNewInfo.setOldMbrshipCd(memberCd);
		//如果类型为空则默认为网站登录加入类型0
		if(null == type || "".equals(type)){
			youbangNewInfo.setType("0");
		}else{
			youbangNewInfo.setType(type);
		}
		youbangNewInfo.setCreateBy(this.catchIdAdress(getRequest()).equals("")?"no_ip":this.catchIdAdress(getRequest()));
		youbangNewInfo.setCreateTime(new Date());
		try {
			youbangNewService.recordYoubangNewInfo(youbangNewInfo);
		} catch (ServiceException e) {
			e.printStackTrace();
			result = "2";
			return SUCCESS;
		}
		result = "0";
		return SUCCESS;
	}
	
	

	public IYoubangNewService getYoubangNewService() {
		return youbangNewService;
	}

	public void setYoubangNewService(IYoubangNewService youbangNewService) {
		this.youbangNewService = youbangNewService;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
}
