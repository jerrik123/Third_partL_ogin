package com.mangocity.thirdparty.login.utils;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mangocity.mbr.unionlogin.utils.StringUtil;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.thirdparty.login.vo.FanliParamVo;
import com.mangocity.util.CookieUtil;

/**
 * 此工具类主要完成用户登陆时设置cookie信息.
 * @author panshilin
 *
 */
public class LoginUtils {
	
	private final static Logger log = Logger.getLogger(LoginUtils.class);
	public final static String domain = ".mangocity.com";

	public final static int cookie_max_age = -1;
    
    public final static int cookie_max_age_year = 60*60*24*365;
    
    public final static int cookie_max_age_month = 60*60*24*30;
    
    /**
	 * 彩贝项目的编号
	 */
	private static final String CAIBEI_PROJECT_CODE = "0019009";

	
	    
	 private static String setloyaltyAccountCookie(MbrSession mbrSession,HttpServletResponse response) {
		 for(int i=0; i<mbrSession.getSessionMbrShipList().size(); i++){
			 Mbrship mbrship = mbrSession.getSessionMbrShipList().get(i);
			 if (StringUtils.isNotEmpty(mbrship.getOldMbrshipCd()) && StringUtils.isNotEmpty(mbrSession.getSessionDefaultMbrshipCd()) &&
						mbrship.getOldMbrshipCd().equals(mbrSession.getSessionDefaultMbrshipCd())){
				 CookieUtil.writeCookie("userid", mbrship.getMbrshipId().toString(), cookie_max_age, domain, "/", response);
				 CookieUtil.writeCookie("memberid", mbrship.getMbrshipId().toString(), cookie_max_age, domain, "/", response);
				 CookieUtil.writeCookie("membercd", mbrship.getOldMbrshipCd(), cookie_max_age,  domain, "/", response);
				 System.out.println("membercd:"+ mbrship.getOldMbrshipCd());
				 return mbrship.getMbrshipCategoryCd();
			 }	 
		 }
		 return null;
	    }
	 

	 //3.0版本
	 public static String setCookies(MbrSession mbrSession,HttpServletResponse response) {
		 try {
	            if (mbrSession != null) {// 登录成功！
	                // 设置 csn 
	                CookieUtil.writeCookie("csn", String.valueOf(mbrSession.getSessionMbrId()), cookie_max_age, domain, "/", response);
	                // 设置 mbrID
	                CookieUtil.writeCookie("mbrID", String.valueOf(mbrSession.getSessionMbrId()), cookie_max_age, domain, "/", response);
	              //start add by wangjia 2012-10-22
	    			// 在做权限校验时，增加mbrsign参数校验 规则如下：mbrsign = sub(MD5(MD5(mbrID) + "mbr_sign"), 0, 10)
	    			MD5Algorithm mD5Algorithm = new MD5Algorithm();
	    			String mbrsign = StringUtils.substring(mD5Algorithm.getMD5ofStr((mD5Algorithm.getMD5ofStr(String.valueOf(mbrSession.getSessionMbrId())) + "mbr_sign")), 0, 10);
	    			CookieUtil.writeCookie("mbrsign", mbrsign, -1,domain, "/", response);
	                //end
	    			
	    			// 添加标识，用cookie登录时，辨别是从cc还是web跳转过来的，防止cc获取到的mbrId可以直接登录网站 flagId = MD5(mbrId)
	    			// by SuZhangSheng 2014.11.25 start
	    			String flagId = mD5Algorithm.getMD5ofStr(String.valueOf(mbrSession.getSessionMbrId()));
	    			CookieUtil.writeCookie("flagId", flagId, cookie_max_age, domain, "/", response);
	    			// end
	    			
	                // 设置 webid
	                CookieUtil.writeCookie("webid", mbrSession.getSessionMbrNetName(), cookie_max_age, domain, "/", response);
	                // 设置 aliasname
	                CookieUtil.writeCookie("aliasname", mbrSession.getSessionMbrNetName(), cookie_max_age, domain,"/", response);
	                // 设置 qqaliasname
	                CookieUtil.writeCookie("qqaliasname", mbrSession.getSessionMbrNetName(), cookie_max_age, domain,"/", response);
	                // 设置 userName
	                CookieUtil.writeCookie("userName", mbrSession.getSessionMbrNetName(), cookie_max_age, domain,"/", response);
	                
	                System.out.println("LoginUtils setCookies mehtod: qqaliasname=" + mbrSession.getSessionMbrNetName());
	                
	                return setloyaltyAccountCookie(mbrSession, response);
	            }
	        } catch (Exception e) {
	            log.error("LoginUtils setCookies:",e);
	        }
	    return null;    
	 }
	 
	 
	 /**
	 * 添加彩贝所需要的cookie参数
	 * 根据电商人员需求设计
	 * @param openId
	 */
	 public static void setCaibeiCookies(String openId, String qqheadshow, String qqjifenurl, String qqopenkey, String CBvkey, String CBattach, HttpServletRequest request, HttpServletResponse response) {
		 String resultStr = "";
			
			// QQ彩贝联盟
			String projectcode = CAIBEI_PROJECT_CODE;
			String attach = "mangocitycom";
			String vkey = "mangocitycom";
			//如果彩贝没有传入VKEY和ATTACH的话则写入默认值mangocitycom,否则写入彩贝传入的参数
			if(null != CBvkey && !"".equals(CBvkey)){
				vkey = CBvkey;
			}
			if(null != CBattach && !"".equals(CBattach)){
				attach = CBattach;
			}
			
			if (StringUtils.isNotEmpty(openId) && StringUtils.isNotEmpty(attach) 
					&& StringUtils.isNotEmpty(vkey)) {
				openId = openId.trim().replace("=", "!e");
				attach = attach.trim().replace("=", "!e");
				vkey = vkey.trim().replace("=", "!e");
				resultStr = "OpenId#" + openId + "@Attach#" + attach + "@Vkey#" + vkey;
			}

			//从cookie中获取exprojectcode1值
			String exprojectcode1 = getCookieValueByName("exprojectcode1", request);
			CookieUtil.writeCookie("projectcode", projectcode, cookie_max_age, domain, "/", response);
			/**
			 * psl-2011/10/24新增2个cookie字段，qqheadshow和qqjifenurl，参数值为：HeadShow和JifenUrl
			 * psl-2011/10/24新增1个cookie字段，qqopenkey，参数值为：OpenKey
			 */
			if(null != qqheadshow && !"".equals(qqheadshow)){
				CookieUtil.writeCookie("qqheadshow", qqheadshow, cookie_max_age, domain, "/", response);
			}
			if(null != qqjifenurl && !"".equals(qqjifenurl)){
				CookieUtil.writeCookie("qqjifenurl", qqjifenurl, cookie_max_age, domain, "/", response);
			}
			if(null != qqopenkey && !"".equals(qqopenkey)){
				CookieUtil.writeCookie("qqopenkey", qqopenkey, cookie_max_age, domain, "/", response);
			}
			 
			String exprojectcode2 = resultStr;
			System.out.println("resultStr=" + resultStr);
			//当exprojectcode1有值时，证明已经有延时性渠道。
			if (!StringUtil.isStringNull(exprojectcode1) 
					&& !exprojectcode1.trim().equals(projectcode)) {
				
				//把exprojectcode1的值加到exprojectcode2后面，以“!code#”进行分割
				exprojectcode2 = exprojectcode2 + "@!code#" + exprojectcode1;
				CookieUtil.writeCookie("exprojectcode1", projectcode, cookie_max_age, domain, "/", response);
				CookieUtil.writeCookie("exprojectcode2", exprojectcode2, cookie_max_age, domain, "/", response);
			} else {
				CookieUtil.writeCookie("exprojectcode1", projectcode, cookie_max_age, domain, "/", response);
				CookieUtil.writeCookie("exprojectcode2", exprojectcode2, cookie_max_age, domain, "/", response);
				
			}
	 }
	 
	 /**
	  * 添加51返利所需要的cookie参数
	  * 根据电商人员需求设计
	  */
	 public static void addFanliProjectCookie(FanliParamVo fanliParamVo, HttpServletRequest request, HttpServletResponse response){
		 System.out.println("51返利的cookie值写入start");
		 String resultStr = "";
		 //写入projectcode和exprojectcode1字段
		 if(null != fanliParamVo.getChannelid() && !"".equals(fanliParamVo.getChannelid())){
			 System.out.println("projectcode："+fanliParamVo.getChannelid());
			 CookieUtil.writeCookie("projectcode", fanliParamVo.getChannelid(), cookie_max_age, domain, "/", response);
			 CookieUtil.writeCookie("exprojectcode1", fanliParamVo.getChannelid(), cookie_max_age, domain, "/", response);
		 }
		 //写入exprojectcode2字段
		 if(null != fanliParamVo.getU_id() && !"".equals(fanliParamVo.getU_id())){
			 if(null != fanliParamVo.getUsername() && !"".equals(fanliParamVo.getUsername())){
				 resultStr = "u_id#"+fanliParamVo.getU_id()+"@username#"+fanliParamVo.getUsername();
			 }else{
				 resultStr = "u_id#"+fanliParamVo.getU_id()+"@username#";
			 }
			 System.out.println("resultStr:" + resultStr);
			 CookieUtil.writeCookie("exprojectcode2", resultStr, cookie_max_age, domain, "/", response);
		 }
		 System.out.println("51返利的cookie值写入end");
	 }
	 
	 /**
	  * 添加51比购所需要的cookie参数
	  * 根据市场人员需求设计
	  */
	 public static void addBigouCookie(String channelId, String u_id, HttpServletRequest request, HttpServletResponse response){
		 log.info("51比购的cookie值写入start");
		 //写入channelId字段
		 if(null != channelId && !"".equals(channelId)){
			 log.info("channelId："+channelId);
			 CookieUtil.writeCookie("channelid", channelId, cookie_max_age, domain, "/", response);
		 }
		 //写入u_id字段
		 if(null != u_id && !"".equals(u_id)){
			 log.info("u_id："+u_id);
			 CookieUtil.writeCookie("u_id", u_id, cookie_max_age_month, domain, "/", response);
		 }
		 log.info("51比购的cookie值写入end");
	 }
	 
	 /**
	  * 添加51比购所需要的cookie参数
	  * 根据市场人员需求设计
	  */
	 public static void addBigouProjectCookie(String projectCode, HttpServletRequest request, HttpServletResponse response){
		 log.info("51比购的cookie值写入start");
		 //写入projectcode字段
		 if(null != projectCode && !"".equals(projectCode)){
			 log.info("projectcode："+projectCode);
			 CookieUtil.writeCookie("projectcode", projectCode, cookie_max_age, domain, "/", response);
		 }
		 System.out.println("51比购的cookie值写入end");
	 }
	 
	 
	 
	 /**
	 * 在cookie中根据参数名称获取参数值
	 * @param name
	 * @param request
	 * @return 指定参数名称的参数值
	 */
	public static String getCookieValueByName(String name, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String verifyCode = "";
		if (cookies != null && cookies.length > 0) {
			if (cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					String tempName = cookie.getName();
					if (tempName.equals(name)) {
						verifyCode = cookie.getValue();
						break;
					}//end if
				}//end for
			}
		}//end if
		return verifyCode;
	}

}
