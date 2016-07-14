package com.mangocity.thirdparty.login.action.youbang;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.thirdpartservice.domain.YoubangRecordInfo;
import com.mangocity.thirdpartservice.service.IYoubangRecordService;
import com.mangocity.thirdparty.login.action.ApplicationAction;


/**
 * 友邦推广页面收集记录抽奖用户信息
 * @author panshilin
 *
 */
public class YoubangRecordAction extends ApplicationAction{
	
	private static final long serialVersionUID = -3339972031700602738L;

	private IYoubangRecordService youbangRecordService;
	
	private String mobileNo;
	
	private String userName;
	
	private String area;
	
	private String birthday;
	
	private String result;
	
	/**
	 * 初始化页面
	 * @return
	 */
	public String initYoubang(){
		return SUCCESS;
	}
	
	
	/**
	 * 记录友邦信息Action
	 * @return
	 * 0代表记录成功，1代表参数校验失败，2代表手机号重复,3代表异常
	 */
	public String recordYoubang(){
		result = "1";
		//友邦入参校验,校验是否为空
		if(null==mobileNo || StringUtils.isBlank(mobileNo) || null==userName 
				|| StringUtils.isBlank(userName) || null==area || StringUtils.isBlank(area))
			return SUCCESS;
		//校验合法性
		if(!mobileNo.matches("^(0|[1-9][0-9]*)$") || mobileNo.length()>11 
				|| mobileNo.length()<8 || userName.length()>12 || area.length()>8)
			return SUCCESS;
		if(null!=birthday && !"".equals(birthday) && !birthday.matches("^\\d{4}-\\d{2}-\\d{2}$"))
			return SUCCESS;
		try {
			//校验手机号是否重复
			YoubangRecordInfo youbangRecordInfoValid = youbangRecordService.queryRecordInfoByMobileNo(mobileNo);
			if(null != youbangRecordInfoValid){
				result = "2";
				return SUCCESS;
			}
			YoubangRecordInfo youbangRecordInfo = new YoubangRecordInfo();
			youbangRecordInfo.setMobileNo(mobileNo);
			youbangRecordInfo.setUserName(userName.trim());
			youbangRecordInfo.setArea(area.trim());
			youbangRecordInfo.setBirthday(birthday);			
			youbangRecordInfo.setCreateBy(this.catchIdAdress(getRequest()).equals("")?"no_ip":this.catchIdAdress(getRequest()));
			youbangRecordInfo.setCreateTime(new Date());
			youbangRecordService.recordYoubangInfo(youbangRecordInfo);
		} catch (ServiceException e) {
			e.printStackTrace();
			result = "3";
			return SUCCESS;
		}
		result = "0";
		return SUCCESS;
	}
	

	public void setYoubangRecordService(IYoubangRecordService youbangRecordService) {
		this.youbangRecordService = youbangRecordService;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getBirthday() {
		return birthday;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}
	
}
