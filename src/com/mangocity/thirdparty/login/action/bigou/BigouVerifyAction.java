package com.mangocity.thirdparty.login.action.bigou;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.services.mbr.RegisterServiceException;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdpartservice.service.exception.ThirdPartyRegisterServiceException;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.helper.IRegisterHelper;
import com.mangocity.thirdparty.login.utils.Constants;
import com.mangocity.thirdparty.login.utils.LoginUtils;
import com.mangocity.thirdparty.login.vo.RegisterVO;

/**
 * 51比购网cgi
 * @author chenhao
 *
 */
public class BigouVerifyAction extends ApplicationAction {

	private static final Logger log = Logger.getLogger(BigouVerifyAction.class);
	
	private static final long serialVersionUID = 1L;
	private static final String BIGOU_CONNECT_KEY = "c5ae04c158600001871b33fb1b60149b"; 

	/**
	 * 51比购项目的编号
	 */
	private static final String BIGOU_PROJECT_CODE = "0135001";
	
	/**
	 * 服务器允许相差时间(15分钟)
	 */
	private static final long DIFF_TIME_QUARTER = 15*60;

	/**
	 * 第三方注册服务接口
	 * 目前包括：1:QQ;2:青芒果；3：彩贝 4:51比购
	 */
	private IThirdPartyRegisterService thirdPartyRegisterService;

	/**
	 * 第三方注册服务接口
	 * 目前包括：1:QQ;2:青芒果；3:51比购
	 */
	private IRegisterHelper registerHelper;

	private IRegisterService registerService;

	private String url;

	/**
	 * 功能：
	 * 步骤如下：
	 * 1：首先检验syncname字段，如果是true，则进入第2步。如果是false，则取消“联合登录”，直接跳转到本次访问的目标网址（url参数）
	 * 2：接着判断操作时间与当前时间 - 操作时间，注意由于我们网站服务器和你们网站服务时间有差异，这个时间开发时可以设定长一点，比如15分钟.
	 * 	  再通过code验证51比购网的合法访问，如果验证合法，则进行第3步。如果验证未通过，则取消“联合登录”，直接跳转到本次访问的目标网址（url参数）
	 * 3：判断username是否存在。(指的是在mango网站数据库中)
	 * 3.1、如果username不存在，则创建新用户，商城用户名为username参数，用户密码请随机指定（尽量复杂一点）， 还需要保存usersaekey，对应当前用户。创建用户后，进入第4步。
	 * 3.2、如果username已经存在，则验证usersafekey是否一致，如果一致，则进入第4步操作。如果不一致，则以未登陆状态跳转到本次访问的目标网址（url参数）
	 * 4：进行用户登录并跳转到本次访问的目标网址（url参数）
	 * @return
	 */
	public String bigoucgi() {
		
		log.debug("51比购网跳转参数:" + getRequest().getQueryString());
		/*
		 * 联合登录完成后要跳转的的Url，
		 * 若该值没有输入，则跳转到站点首页即可，
		 * 否则须跳转到该Url指定的页面。
		 * 注：该Url主要的目的是用于商户正常联合登录后商户需要跳转到的某个具体的页面，
		 * 如某个具体的活动页面或者某个商品详情页面，防止每次都跳转到首页，
		 * 需要用户自己二次跳转到某个指定页面的问题。
		 */
		url = getRequest().getParameter("url");
		
		if(!StringUtils.hasLength(url)){
			url = "http://www.mangocity.com";
		}else{
			try {
				url = URLDecoder.decode(url, "utf-8");
			} catch (UnsupportedEncodingException e2) {
				url = "http://www.mangocity.com";
				log.error("url ["+url+"] decode异常", e2);
			}
		}
		
		String channelId = getRequest().getParameter("channelid");
		String uId = getRequest().getParameter("u_id");
		LoginUtils.addBigouCookie(channelId, uId, this.getRequest(),this.getResponse());
		
		//1:校验syncname
		String syncname = getRequest().getParameter("syncname");
		if(!"true".equalsIgnoreCase(syncname)){
			return "redirectCB";
		}
		
		//2：验证参数合法性
		//该字段是商户作为识别帐户的唯一依据
		String username = getRequest().getParameter("username");
		//精确到毫秒的时间戳
		String action_time = getRequest().getParameter("action_time");
		//验证字符串
		String code = getRequest().getParameter("code");
		boolean bigouVerifyFlag = bigouVerifyFlag(username,action_time, code);
		if (!bigouVerifyFlag) {
			//如果time和code检测不通过，那么跳转到url
			return "redirectCB";
		}

		//3:查询数据库中是否已经绑定会员
		Long csn = null;
		TsIntUser tsIntUser = null;
		try {
			tsIntUser = thirdPartyRegisterService
					.queryThirdPartyUserByLoginCode(username);
		} catch (ThirdPartyRegisterServiceException e1) {
			log.error("根据username"+username+"查询是否已绑定会员异常",e1);
			return "redirectCB";
		}
		if (tsIntUser != null && (Long) tsIntUser.getCsn() != null) {
			csn = tsIntUser.getCsn();
		}
		//用户安全码
		String usersafekey = getRequest().getParameter("usersafekey");
		//若已注册，则验证usersafekey
		if(null != csn && ( tsIntUser.getSaftkey() == null || !tsIntUser.getSaftkey().equals(usersafekey))){
			log.error("51比购usersafekey:"+usersafekey+"不匹配");
			return "redirectCB";
		}else if (csn == null) {
			//3.1:若没有绑定，则注册会员并绑定
			RegisterVO registerBean = createRegisterVO(username,usersafekey);
			try {
				registerHelper.registerMango(registerBean);
			} catch (Exception e) {
				log.error("51比购会员注册异常[username:"+username+",usersafekey:"+usersafekey+"]",e);
				return "redirectCB";
			}
			// 注册完成后，需要再次查询该会员信息并登录
			try {
				tsIntUser = thirdPartyRegisterService
						.queryThirdPartyUserByLoginCode(username);
			} catch (ThirdPartyRegisterServiceException e1) {
				log.error("51比购会员查询异常,username:"+username,e1);
				return "redirectCB";
			}
			csn = tsIntUser.getCsn();
		}
		//登录
		MbrSession mbrSession = null;
		try {
			mbrSession = registerService.checkLogin(csn.toString(),null);
		} catch (RegisterServiceException e) {
			log.error("51比购会员登录异常,csn:"+csn,e);
			return "redirectCB";
		}

		List<Mbrship> mbrShipList = mbrSession.getSessionMbrShipList();

		for (Mbrship mbrship : mbrShipList) {
			if (Constants.CON_MEMBERSHIPCODE_BIGOU.equals(mbrship.getMbrshipCategoryCd())) {
				mbrSession.setSessionDefaultMbrshipCd(mbrship.getOldMbrshipCd());
				break;
			}
		}

		//4:信息参数存入cookie
		if (!mbrShipList.isEmpty()) {

			//4.1将会员信息参数存入cookie
			LoginUtils.setCookies(mbrSession, this.getResponse());

			//4.2将51比购信息参数存入cookie
			LoginUtils.addBigouProjectCookie(BIGOU_PROJECT_CODE, this.getRequest(),this.getResponse());
		}
		log.info("51比购会员:"+csn+"登陆成功");
		//4.3登录后跳转到指定页面
		return "redirectCB";
	}

	/**
	 * 批量根据csn查UID
	 * @return
	 */
	public String queryUIDByCSN(){
		String mbrIdStrs = getRequest().getParameter("mbrIds");
		if(null == mbrIdStrs || "".equals(mbrIdStrs)){
			return NONE;
		}
		log.debug("批量根据csn查询UID方法,csn串:["+mbrIdStrs+"]");
		Map<Long,String> csnUIDMap = new HashMap<Long,String>();
		try{
			csnUIDMap = thirdPartyRegisterService.queryLoginNickByCsn(mbrIdStrs);
		}catch(ThirdPartyRegisterServiceException e){
			log.error("批量根据csn查询LoginNick方法出现异常,csn串"+mbrIdStrs,e);
			return NONE;
		}
		dealQueryResult(csnUIDMap);
		return NONE;
	}
	
	/**
	 * 验证参数合法性
	 * 1.验证服务器时间相差是否不超过15分钟
	 * 2.验证code
	 * 若合法，返回true;否则，返回false
	 * @return
	 */
	private boolean bigouVerifyFlag(String username, String action_time, String code) {
		long diff_time = (Long.valueOf(action_time)-(System.currentTimeMillis()))/1000;
		if(diff_time < - DIFF_TIME_QUARTER || diff_time > DIFF_TIME_QUARTER){
			log.error("51比购用户username:"+username+"访问时间与服务器时间相差较大,验证失败");
			return false;
		}
		String md5Str = (MD5(username + BIGOU_CONNECT_KEY + action_time)).toLowerCase();

		if(code != null && code.equals(md5Str)){
			return true;
		}else{
			log.error("51比购用户username:"+username+"验证code失败,传入的code:"+code+",计算的code:"+md5Str);
			return false;
		}
	}

	private RegisterVO createRegisterVO(String username,String usersafekey){
		RegisterVO registerBean = new RegisterVO();

		//构造默认注册的email,用户在51比购网标识ID加“@51bi.com”
		String eamil = username;
		registerBean.setEmail(eamil);

		//默认注册的用户设置随机密码
		registerBean.setPassword(java.util.UUID.randomUUID().toString());

		//注册类型:4：51比购
		registerBean.setRegistertype(RegisterVO.REG_TYPE_51BI);

		//注册方式 1-注册 2-绑定 3-跳过
		registerBean.setRegistermode(RegisterVO.JUMP);
		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("username", username);
		messageMap.put("usersafekey", usersafekey);
		registerBean.setMessage(messageMap);
		return registerBean;
	}
	
	/**
	 * 结果Map处理
	 * @param csnUIDMap
	 */
	private void dealQueryResult(Map<Long,String> csnUIDMap){
		if( null != csnUIDMap && csnUIDMap.size() > 0){
			String csnUIDMapStr = getCsnUIDMapStr(csnUIDMap);
			outputResult(csnUIDMapStr);
		}
	}
	
	/**
	 * 将CSN和UID Map转换为格式为 csn1:uid1,csn2:uid2字符串
	 * @return
	 */
	private String getCsnUIDMapStr(Map<Long,String> csnUIDMap){
		String csnUIDMapStr = "";
		StringBuilder csnUIDMapSb = new StringBuilder();
		for(Map.Entry entry : csnUIDMap.entrySet()){
			String value = (String)entry.getValue();
			//51bigou loginNick=UID+"@51bigou.com"
			if(value.indexOf("@") != -1){
				csnUIDMapSb.append(entry.getKey()).append(":")
					.append(value.substring(0, value.indexOf("@"))).append(",");
			}
		}
		csnUIDMapStr = csnUIDMapSb.toString();
		if(csnUIDMapStr.endsWith(","))
			csnUIDMapStr = csnUIDMapStr.substring(0, csnUIDMapStr.length()-1);
		return csnUIDMapStr;
	}
	
	/**
	 * 输出结果串到输出流
	 * @param csnUIDMapStr
	 */
	private void outputResult(String csnUIDMapStr){
		PrintWriter out = null;
		try{
			out = getResponse().getWriter();
			out.write(csnUIDMapStr);
			log.debug("批量根据csn查询UID方法,返回结果串["+csnUIDMapStr+"]");
		}catch(IOException e){
			log.error("批量根据csn查询UID方法出现异常",e);
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	/**
	 * 根据彩贝提供的MD5算法设计
	 * @param s
	 * @return
	 */
	private String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();

			//获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			//使用指定的字节更新摘要
			mdInst.update(btInput);

			//获得密文
			byte[] md = mdInst.digest();

			//把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public IThirdPartyRegisterService getThirdPartyRegisterService() {
		return thirdPartyRegisterService;
	}

	public void setThirdPartyRegisterService(
			IThirdPartyRegisterService thirdPartyRegisterService) {
		this.thirdPartyRegisterService = thirdPartyRegisterService;
	}

	public IRegisterHelper getRegisterHelper() {
		return registerHelper;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	public static void main(String[] args){
		long action_time = System.currentTimeMillis();
		System.out.println(action_time);
		action_time += 880000;
		System.out.println(action_time);
		System.out.println(new BigouVerifyAction().MD5("2592834@51bi.com"+BIGOU_CONNECT_KEY+action_time).toLowerCase());
	}
}
