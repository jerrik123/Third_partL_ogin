package com.mangocity.thirdparty.login.action.weixin;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.mangocity.model.mbr.Mbr;
import com.mangocity.model.person.Person;
import com.mangocity.services.mbr.IMbrService;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.services.person.IPersonService;
import com.mangocity.thirdpartservice.domain.WeixinBindingInfo;
import com.mangocity.thirdpartservice.service.IWeixinBindingService;
import com.mangocity.thirdparty.login.action.ApplicationAction;

/**
 * 微信登录绑定
 * @author suzhangsheng
 *
 */
public class WeixinBindingAction extends ApplicationAction {
	
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = -6310983850532201440L;
	
	private IWeixinBindingService weixinBindingService;
	
	private IMbrService mbrService;
	
	private IPersonService personService;
	
	private IRegisterService registerService;
	
	/**
	 * 检查是否绑定微信号
	 * @return
	 * @throws Exception 
	 */
	public String checkWeixinBinding() throws Exception {
		log.info("-----------------check weixin binding start-----------------");
		getResponse().setCharacterEncoding("UTF-8");
		PrintWriter pw = getResponse().getWriter();
			
		if(!isPost()) {
			pw.write(generateResponse(ResultCode.GET_ERROR, "Not_Allow_Get_Method", "", ""));
			pw.flush();
			pw.close();
			return null;
		}
		
		try {
			BufferedReader reader = getRequest().getReader();
			String json = reader.readLine();
			
			if(json == null || "".equals(json)) {
				pw.write(generateResponse(ResultCode.JSON_EMPTY, "Json_Is_Empty", "", ""));
				pw.flush();
				pw.close();
				return null;
			}
			
			log.info("json: " + json);
			
			WeixinUserInfo weixinUserInfo = JSONObject.parseObject(json, WeixinUserInfo.class);
			WeixinBindingInfo weixinBindingInfo = new WeixinBindingInfo();
			// 检查数据
			boolean mbrIdFlag = (weixinUserInfo.getMbrId() == null || "".equals(weixinUserInfo.getMbrId()));
			boolean mobileFlag = (weixinUserInfo.getMobile() == null || "".equals(weixinUserInfo.getMobile()));
			boolean openIdFlag = (weixinUserInfo.getOpenId() == null || "".equals(weixinUserInfo.getOpenId()));
			
			if(openIdFlag) {
				pw.write(generateResponse(ResultCode.PARAM_EMPTY, "Param_Is_Empty", "", ""));
				pw.flush();
				pw.close();
				return null;
			}
			
			// 根据openId查询绑定信息
			if(mbrIdFlag && mobileFlag) {
				weixinBindingInfo.setOpenId(weixinUserInfo.getOpenId());
				weixinBindingInfo = weixinBindingService.retrieveWeixinBindingInfoByOpenId(weixinBindingInfo);
				if(weixinBindingInfo == null) {
					pw.write(generateResponse(ResultCode.OPENID_NOT_EXIST, "OpenId_Not_Exist", "", ""));
					pw.flush();
					pw.close();
				} else {
					pw.write(generateResponse(ResultCode.SEARCH_SUCCESS, "Search_Success", 
							weixinBindingInfo.getMbrId() + "", weixinBindingInfo.getMobile()));
					pw.flush();
					pw.close();
				}
				log.info("-----------------check weixin binding end-----------------");
				return null;
			}
			
			int isMobileExist = 0;
			// 检验手机号是否在系统中已被注册
			if(!mobileFlag) {
				if(registerService.validateLoginNameUnique(weixinUserInfo.getMobile(), "M")) {
					// 手机号已被注册
					isMobileExist = 2;
				} else {
					// 手机号没有被注册
					isMobileExist = 1;
				}
			}
			log.info("isMobileExist: " + isMobileExist);
			
			if(!mbrIdFlag) {
				weixinBindingInfo.setMbrId(Long.valueOf(weixinUserInfo.getMbrId()));
			} else {
				weixinBindingInfo.setMbrId(-1L);
			}
			weixinBindingInfo.setMobile(weixinUserInfo.getMobile());
			weixinBindingInfo.setOpenId(weixinUserInfo.getOpenId());
			// 检验是否绑定
			weixinBindingInfo = weixinBindingService.retrieveWeixinBindingInfo(weixinBindingInfo);
			if(weixinBindingInfo == null) {
				
				switch(isMobileExist) {
				case 1:
					pw.write(generateResponse(ResultCode.IS_NOT_BINDING_AND_EXIST, "Is_Not_Banding_And_Exist", "", ""));
					pw.flush();
					pw.close();
					break;
				case 2:
					pw.write(generateResponse(ResultCode.IS_NOT_BINDING_AND_NOT_EXIST, "Is_Not_Banding_And_Not_Exist", "", ""));
					pw.flush();
					pw.close();
					break;
				default:
					pw.write(generateResponse(ResultCode.IS_NOT_BINDING, "Is_Not_Banding", "", ""));
					pw.flush();
					pw.close();
				}
				
			} else {
				
				switch(isMobileExist) {
				case 1:
					pw.write(generateResponse(ResultCode.IS_BINDING_AND_EXIST, "Is_Banding_And_Exist", 
							weixinBindingInfo.getMbrId() + "", weixinBindingInfo.getMobile()));
					pw.flush();
					pw.close();
					break;
				case 2:
					pw.write(generateResponse(ResultCode.IS_BINDING_AND_NOT_EXIST, "Is_Banding_And_Not_Exist", 
							weixinBindingInfo.getMbrId() + "", weixinBindingInfo.getMobile()));
					pw.flush();
					pw.close();
					break;
				default:
					pw.write(generateResponse(ResultCode.IS_BINDING, "Is_Banding", 
							weixinBindingInfo.getMbrId() + "", weixinBindingInfo.getMobile()));
					pw.flush();
					pw.close();
				}
				
			}
			
		} catch (Exception e) {
			log.error("检查是否绑定微信号出错：", e);
			pw.write(generateResponse(ResultCode.SYSTEM_ERROR, "System_Error", "", ""));
			pw.flush();
			pw.close();
		}
		
		log.info("-----------------check weixin binding end-----------------");
		return null;
	}
	
	/**
	 * 添加微信号绑定
	 * @return
	 * @throws Exception 
	 */
	public String addWeixinBinding() throws Exception {
		log.info("-----------------add weixin binding start-----------------");
		getResponse().setCharacterEncoding("UTF-8");
		PrintWriter pw = getResponse().getWriter();
		
		if(!isPost()) {
			pw.write(generateResponse(ResultCode.GET_ERROR, "Not_Allow_Get_Method"));
			pw.flush();
			pw.close();
			return null;
		}
		
		try {
			BufferedReader reader = getRequest().getReader();
			String json = reader.readLine();
			
			if(json == null || "".equals(json)) {
				pw.write(generateResponse(ResultCode.JSON_EMPTY, "Json_Is_Empty"));
				pw.flush();
				pw.close();
				return null;
			}
			
			log.info("json: " + json);
			
			WeixinUserInfo weixinUserInfo = JSONObject.parseObject(json, WeixinUserInfo.class);
			WeixinBindingInfo weixinBindingInfo = new WeixinBindingInfo();
			// 检查数据
			boolean mbrIdFlag = (weixinUserInfo.getMbrId() == null || "".equals(weixinUserInfo.getMbrId()));
			boolean mobileFlag = (weixinUserInfo.getMobile() == null || "".equals(weixinUserInfo.getMobile()));
			boolean openIdFlag = (weixinUserInfo.getOpenId() == null || "".equals(weixinUserInfo.getOpenId()));
			if(mbrIdFlag || mobileFlag || openIdFlag) {
				pw.write(generateResponse(ResultCode.PARAM_EMPTY, "Param_Is_Empty"));
				pw.flush();
				pw.close();
				return null;
			}
			
			// 检查会员ID是否存在
			Mbr mbr = mbrService.mbrById(Long.valueOf(weixinUserInfo.getMbrId()));
			if(mbr == null) {
				pw.write(generateResponse(ResultCode.MEMBER_NOT_EXIST, "Member_Not_Exist"));
				pw.flush();
				pw.close();
				return null;
			}
			
			// 检查会员手机号是否和系统中一致
			Person person = personService.queryPersonById(String.valueOf(mbr.getPersonId()));
			if(!weixinUserInfo.getMobile().equals(person.getMobileNo())) {
				pw.write(generateResponse(ResultCode.MOBILE_NOT_CONSISTENT, "Mobile_Not_Consistent"));
				pw.flush();
				pw.close();
				return null;
			}
			
			weixinBindingInfo.setMbrId(Long.valueOf(weixinUserInfo.getMbrId()));
			weixinBindingInfo.setMobile(weixinUserInfo.getMobile());
			weixinBindingInfo.setOpenId(weixinUserInfo.getOpenId());
			Date currentDate = new Date(System.currentTimeMillis());
			weixinBindingInfo.setCreateTime(currentDate);
			weixinBindingInfo.setUpdateTime(currentDate);
			weixinBindingInfo.setStatus(1);
			
			// 添加绑定记录
			weixinBindingService.insertWeixinBindingInfo(weixinBindingInfo);
			pw.write(generateResponse(ResultCode.BINDING_SUCCESS, "Banding_Success"));
			pw.flush();
			pw.close();
			
		} catch (Exception e) {
			log.error("添加微信号绑定出错：", e);
			pw.write(generateResponse(ResultCode.SYSTEM_ERROR, "System_Error"));
			pw.flush();
			pw.close();
		}
		
		log.info("-----------------add weixin binding end-----------------");
		return null;
	}
	
	/**
	 * 解除微信号绑定
	 * @return
	 * @throws Exception
	 */
	public String cancelWeixinBinding() throws Exception {
		log.info("-----------------cancel weixin binding start-----------------");
		getResponse().setCharacterEncoding("UTF-8");
		PrintWriter pw = getResponse().getWriter();
		
		if(!isPost()) {
			pw.write(generateResponse(ResultCode.GET_ERROR, "Not_Allow_Get_Method"));
			pw.flush();
			pw.close();
			return null;
		}
		
		try {
			BufferedReader reader = getRequest().getReader();
			String json = reader.readLine();
			
			if(json == null || "".equals(json)) {
				pw.write(generateResponse(ResultCode.JSON_EMPTY, "Json_Is_Empty"));
				pw.flush();
				pw.close();
				return null;
			}
			
			log.info("json: " + json);
			
			WeixinUserInfo weixinUserInfo = JSONObject.parseObject(json, WeixinUserInfo.class);
			WeixinBindingInfo weixinBindingInfo = new WeixinBindingInfo();
			
			// 检查数据
			boolean mbrIdFlag = (weixinUserInfo.getMbrId() == null || "".equals(weixinUserInfo.getMbrId()));
			boolean mobileFlag = (weixinUserInfo.getMobile() == null || "".equals(weixinUserInfo.getMobile()));
			boolean openIdFlag = (weixinUserInfo.getOpenId() == null || "".equals(weixinUserInfo.getOpenId()));
			if(mbrIdFlag || mobileFlag || openIdFlag) {
				pw.write(generateResponse(ResultCode.PARAM_EMPTY, "Param_Is_Empty"));
				pw.flush();
				pw.close();
				return null;
			}
			
			weixinBindingInfo.setMbrId(Long.valueOf(weixinUserInfo.getMbrId()));
			weixinBindingInfo.setMobile(weixinUserInfo.getMobile());
			weixinBindingInfo.setOpenId(weixinUserInfo.getOpenId());
			Date currentDate = new Date(System.currentTimeMillis());
			weixinBindingInfo.setCreateTime(currentDate);
			weixinBindingInfo.setUpdateTime(currentDate);
			weixinBindingInfo.setStatus(1);
			
			// 解除绑定
			int result = weixinBindingService.updateWeixinBindingInfo(weixinBindingInfo);
			if(result == 1) {
				pw.write(generateResponse(ResultCode.UNBIND_SUCCESS, "Unband_Success"));
				pw.flush();
				pw.close();
			} else {
				pw.write(generateResponse(ResultCode.UNBIND_FAIL, "Unband_Fail"));
				pw.flush();
				pw.close();
			}
			
		} catch (Exception e) {
			log.error("解除绑定出错：", e);
			pw.write(generateResponse(ResultCode.SYSTEM_ERROR, "System_Error"));
			pw.flush();
			pw.close();
		}
		
		log.info("-----------------cancel weixin binding end-----------------");
		return null;
	}

	public IWeixinBindingService getWeixinBindingService() {
		return weixinBindingService;
	}

	public void setWeixinBindingService(IWeixinBindingService weixinBindingService) {
		this.weixinBindingService = weixinBindingService;
	}
	
	public IMbrService getMbrService() {
		return mbrService;
	}

	public void setMbrService(IMbrService mbrService) {
		this.mbrService = mbrService;
	}

	public IPersonService getPersonService() {
		return personService;
	}

	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	private boolean isPost() {
		return "POST".equals(getRequest().getMethod());
	}
	
	public IRegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}

	private String generateResponse(String code, String msg) {
		JSONObject result = new JSONObject();
		result.put("code", code);
		result.put("msg", msg);
		log.info(result.toJSONString());
		return result.toJSONString();
	}
	
	private String generateResponse(String code, String msg, String mbrId, String mobile) {
		JSONObject result = new JSONObject();
		result.put("code", code);
		result.put("msg", msg);
		result.put("mbrId", mbrId);
		result.put("mobile", mobile);
		log.info(result.toJSONString());
		return result.toJSONString();
	}

}
