package com.mangocity.thirdparty.login.action.fanli;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import com.mangocity.model.mbrsys.MbrSession;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.services.mbr.IRegisterService;
import com.mangocity.thirdpartservice.domain.TsIntUser;
import com.mangocity.thirdpartservice.service.IThirdPartyRegisterService;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.helper.IFanliRegisterHelper;
import com.mangocity.thirdparty.login.helper.ItravelBusinessHelper;
import com.mangocity.thirdparty.login.utils.CookieUtil;
import com.mangocity.thirdparty.login.utils.LoginUtils;
import com.mangocity.thirdparty.login.vo.FanliParamVo;
import com.mangocity.util.MD5Algorithm;
/**
 * 51返利登录
 * @author panshilin
 *
 */
@SuppressWarnings("serial")
public class FanliVerifyAction extends ApplicationAction{
	/**
	 * 51返利网安全key
	 */
	private final String fanlikey="a2ae59a32e6d8ce8";
	/**
	 * 51返利在关系表的类型
	 */
	private final Integer fanliType = 5;
	/**
	 * 51返利在旧会员配送地址表中的类型标识USEFOR
	 */
	//private final String useFor = "51FANLI";
	/**
	 * 查询绑定关系接口
	 */
	private IThirdPartyRegisterService thirdPartyRegisterService;
	/**
	 * 调用EJB会员登录注册接口
	 */
	private IRegisterService registerService;
	
	private IFanliRegisterHelper fanliRegisterHelper;
	
	/**
	 * 查询是否商旅用户接口
	 */
	private ItravelBusinessHelper travelBusinessHelper;
	
	/**
	 * 返利跳转URL
	 */
	private String url;
	
	
	/**
	 * 51返利初始化
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String init(){
		System.out.println("51返利初始化start!!!");
		//a.调用封装方法封转VO
		FanliParamVo oFanliParamVo = null;
		oFanliParamVo = encapsulateParam(getRequest());
		if(null == oFanliParamVo)
		return ERROR;
		//b.调用合法性验证方法,验证合法性
		//修改：校验syncname是否接收true为接收，false为不接收 by psl 2012/1/5
		if(!"true".equals(oFanliParamVo.getSyncname())){
			//添加projectcode信息cookie
	    	LoginUtils.addFanliProjectCookie(oFanliParamVo,this.getRequest(),this.getResponse());
	    	if(null != oFanliParamVo.getUrl() && !"".equals(oFanliParamVo.getUrl())){
				url = URLDecoder.decode(oFanliParamVo.getUrl());
				return "page";
			}
	    	return ERROR;
		}else{
			boolean flag = false;
			flag = fanliValidate(oFanliParamVo);
			if(flag == false)
			return ERROR;
		}
		//c.调用用户验证方法，验证用户是否存在,验证通过则写入COOKIE，登录
		boolean regFlag = false;
		regFlag = userValidate(oFanliParamVo);
		if(regFlag == false)
		return ERROR;
		if(null != oFanliParamVo.getUrl() && !"".equals(oFanliParamVo.getUrl())){
			url = URLDecoder.decode(oFanliParamVo.getUrl());
			return "page";
		}
		//d.调用配送地址验证方法
		//boolean addrFlag = false;
		//addrFlag = addressValidate(oFanliParamVo);
		//if(addrFlag == false){
		//	return ERROR;
		//}
		return SUCCESS;
	}
	
	
	/**
	 * 封装返利网传入参数，存入VO
	 * @param request
	 * @return FanliParamVo
	 */
	private FanliParamVo encapsulateParam(HttpServletRequest request){
		System.out.println("封装返利网参数start!!!");
	    //封装对象，如果参数不为null，怎封装入VO
		FanliParamVo fanliParamVo = new FanliParamVo();	
		if(null != request.getParameter("channelid")){
			fanliParamVo.setChannelid(request.getParameter("channelid").trim());
			System.out.println("channelid:"+request.getParameter("channelid").trim());
		}if(null != request.getParameter("u_id")){
			fanliParamVo.setU_id(request.getParameter("u_id").trim());
			System.out.println("u_id:"+request.getParameter("u_id").trim());
		}if(null != request.getParameter("url")){
			fanliParamVo.setUrl(request.getParameter("url").trim());
			System.out.println("url:"+request.getParameter("url").trim());
		}if(null != request.getParameter("code")){
			fanliParamVo.setCode(request.getParameter("code").trim());
			System.out.println("code:"+request.getParameter("code").trim());
		}if(null != request.getParameter("syncname")){
			fanliParamVo.setSyncname(request.getParameter("syncname").trim());
			System.out.println("syncname:"+request.getParameter("syncname").trim());
		}if(null != request.getParameter("username")){
			fanliParamVo.setUsername(request.getParameter("username").trim());
			System.out.println("username:"+request.getParameter("username").trim());
		}if(null != request.getParameter("usersafekey")){
			fanliParamVo.setUsersafekey(request.getParameter("usersafekey").trim());
			System.out.println("usersafekey:"+request.getParameter("usersafekey").trim());
		}if(null != request.getParameter("action_time")){
			fanliParamVo.setAction_time(request.getParameter("action_time").trim());
			System.out.println("action_time:"+request.getParameter("action_time").trim());
		}if(null != request.getParameter("email")){
			fanliParamVo.setEmail(request.getParameter("email").trim());
			System.out.println("email:"+request.getParameter("email").trim());
		}if(null != request.getParameter("syncaddress")){
			fanliParamVo.setSyncaddress(request.getParameter("syncaddress").trim());
			System.out.println("syncaddress:"+request.getParameter("syncaddress").trim());
		}if(null != request.getParameter("name")){
			String name = "";
			try {
				//联系人名称进行转码GB2312
				name = new String(URLDecoder.decode(request.getParameter("name").trim(), "GB2312").getBytes("ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			fanliParamVo.setName(name);
			System.out.println("name:"+name);
		}if(null != request.getParameter("province")){
			String province = "";
			try {
				//省份进行转码GB2312
				province = new String(URLDecoder.decode(request.getParameter("province").trim(), "GB2312").getBytes("ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			fanliParamVo.setProvince(province);
			System.out.println("province:"+province);
		}if(null != request.getParameter("city")){
			String city = "";
			try {
				//城市进行转码GB2312
				city = new String(URLDecoder.decode(request.getParameter("city").trim(), "GB2312").getBytes("ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			fanliParamVo.setCity(city);
			System.out.println("city:"+city);
		}if(null != request.getParameter("area")){
			String area = "";
			try {
				//区进行转码GB2312
				area = new String(URLDecoder.decode(request.getParameter("area").trim(), "GB2312").getBytes("ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			fanliParamVo.setArea(area);
			System.out.println("area:"+area);
		}if(null != request.getParameter("address")){
			String address = "";
			try {
				//地址进行转码GB2312
				address = new String(URLDecoder.decode(request.getParameter("address").trim(), "GB2312").getBytes("ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			fanliParamVo.setAddress(address);
			System.out.println("address:"+address);
		}if(null != request.getParameter("zip")){
			fanliParamVo.setZip(request.getParameter("zip").trim());
			System.out.println("zip:"+request.getParameter("zip").trim());
		}if(null != request.getParameter("phone")){
			fanliParamVo.setPhone(request.getParameter("phone").trim());
			System.out.println("phone:"+request.getParameter("phone").trim());
		}if(null != request.getParameter("mobile")){
			fanliParamVo.setMobile(request.getParameter("mobile").trim());
			System.out.println("mobile:"+request.getParameter("mobile").trim());
		}if(null != request.getParameter("pwd")){
			fanliParamVo.setPwd(request.getParameter("pwd").trim());
			System.out.println("pwd:"+request.getParameter("pwd").trim());
		}
		System.out.println("封装返利网参数end!!!");
		return fanliParamVo;
	}
	
	
	/**
	 * 返利网合法性验证方法
	 * @param fanliParamVo
	 * @return boolean
	 */
	private boolean fanliValidate(FanliParamVo fanliParamVo){
		System.out.println("返利网参数合法性校验start!!!");
		//a.校验syncname是否接收true为接收，false为不接收
		//if(!"true".equals(fanliParamVo.getSyncname())){
		//	return false;
		//}
		//b.校验操作时间，如果与传来时间相差超过5分钟,计算UNIX时间戳
		if(null != fanliParamVo.getAction_time() && !"".equals(fanliParamVo.getAction_time())){
			Long currTime = System.currentTimeMillis()/1000;
			Long UnixTemp = Long.parseLong(fanliParamVo.getAction_time());
			System.out.println("返利调用时间："+UnixTemp+"----转化后时间："+new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(UnixTemp*1000)));
			System.out.println("系统当前时间："+currTime+"----转化后时间："+new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(currTime*1000)));
			if(UnixTemp - currTime>300 || currTime - UnixTemp>300){
				return false;
			}
		}else{
			return false;
		}
		//c.校验code码是否合法
		if(null != fanliParamVo.getUsername() && !"".equals(fanliParamVo.getUsername())
				&& null != fanliParamVo.getCode() && !"".equals(fanliParamVo.getCode())){
			String md5Code = new MD5Algorithm().getMD5ofStr(fanliParamVo.getUsername() + fanlikey +fanliParamVo.getAction_time());
			System.out.println("系统加密后得到的code："+md5Code);
			System.out.println("返利网传送来的code："+fanliParamVo.getCode());
			if(!md5Code.equalsIgnoreCase(fanliParamVo.getCode())){
				return false;
			}
		}else{
			return false;
		}
		System.out.println("返利网参数合法性校验end!!!");
		return true;
	}
	
	/**
	 * 校验用户名,邮箱是否已存在
	 * @param fanliParamVo
	 * @return boolean
	 */
	private boolean userValidate(FanliParamVo fanliParamVo){
		//判断是否传入的51返利网用户名为空
		if(null != fanliParamVo.getUsername() && !"".equals(fanliParamVo.getUsername())){
			try {
				TsIntUser oTsIntUser = thirdPartyRegisterService.queryThirdPartyUserByLoginName(fanliParamVo.getUsername(),fanliType);
				System.out.println("返回的TsIntUser对象:"+oTsIntUser);
				/*
				 * 判断该用户名是否存在于关系表中，不存在则注册用户（还需判断邮箱密码是否存在于会员库中）
				 * 存在则判断用户安全码是否与关系表中的一致，一致则返回true,不一致则返回false
				 */
				if(null == oTsIntUser){
					//判断用户名在会员库中是否存在,返回结果true为不存在，false为存在
				    if(!registerService.validateLoginNameUnique(fanliParamVo.getUsername(), "N"))
				    return false;
					//判断51返利邮箱是否为空，空则返回false
				    if(null != fanliParamVo.getEmail() && !"".equals(fanliParamVo.getEmail())){
				    	//注册的邮箱标识,如果是false的话，直接用返利过来的邮箱注册;如果是true的话，则用返利用户名+.com邮箱注册
				    	boolean mailFlag = false;
				    	//判断邮箱在会员库中是否存在,返回结果true为不存在，false为存在
					    if(!registerService.validateLoginNameUnique(fanliParamVo.getEmail(), "E")){
					    	//如果51返利的邮箱已在芒果网存在，则将51返利的用户名+.com组成新邮箱存入数据库
					    	if(!registerService.validateLoginNameUnique(fanliParamVo.getUsername()+".com", "E")){
					    		return false;
					    	}else{
					    		mailFlag = true;
					    	}
					    }
					    //判断邮箱标识mailFlag
					    System.out.println("注册邮箱标识mailFlag:"+mailFlag);
					    if(mailFlag == false){
					    	fanliParamVo.setEmailFlag("false");
					    }else{
					    	fanliParamVo.setEmailFlag("true");
					    }
					    //注册用户...返回注册对象personMainInfo
					    PersonMainInfo personMainInfo = fanliRegisterHelper.registerMangoFromFanli(fanliParamVo);
					    if(null != personMainInfo){
					    	//添加会员基本信息cookie
					    	addFanliCookie(personMainInfo.getMbrId().toString());
					    	//添加projectcode信息cookie
					    	LoginUtils.addFanliProjectCookie(fanliParamVo,this.getRequest(),this.getResponse());
					    	return true;
					    }
				    }
				}else if(null != fanliParamVo.getUsersafekey() && !"".equals(fanliParamVo.getUsersafekey())){
					System.out.println("51返利安全码:"+fanliParamVo.getUsersafekey()+"----芒果数据库中安全码："+oTsIntUser.getSaftkey());
					if(oTsIntUser.getSaftkey().equals(fanliParamVo.getUsersafekey())){
						//安全码相同则判定为合法登录，登录用户写入COOKIE
						addFanliCookie(String.valueOf(oTsIntUser.getCsn()));
						//添加projectcode信息cookie
						LoginUtils.addFanliProjectCookie(fanliParamVo,this.getRequest(),this.getResponse());
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
	/**
	 * 校验配送信息
	 * @param fanliParamVo
	 * @return boolean
	 */
	private boolean addressValidate(FanliParamVo fanliParamVo){
		System.out.println("判断是否同步51返利配送信息");
		try{
			if("true".equals(fanliParamVo.getSyncaddress()) && !"".equals(fanliParamVo.getU_id()) && null != fanliParamVo.getU_id()){
				return false;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 51返利写用户COOKIE
	 * @param thirdPartyRegisterService
	 */
	private void addFanliCookie(String mbrId){
		MbrSession mbrSession = null;
		try {
			mbrSession = registerService.checkLogin(mbrId, null);
	    	//写入用户基本信息COOKIE
	    	String categoryCd = LoginUtils.setCookies(mbrSession, this.getResponse());
	    	String isTravelBusiness = travelBusinessHelper.getIsTravelBusiness(categoryCd);
	    	if(null != isTravelBusiness && !"".equals(isTravelBusiness))
	    	CookieUtil.writeCookie("member.isTravelbusiness", isTravelBusiness, LoginUtils.cookie_max_age, LoginUtils.domain, "/", this.getResponse());
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


	public void setFanliRegisterHelper(IFanliRegisterHelper fanliRegisterHelper) {
		this.fanliRegisterHelper = fanliRegisterHelper;
	}


	public void setTravelBusinessHelper(ItravelBusinessHelper travelBusinessHelper) {
		this.travelBusinessHelper = travelBusinessHelper;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

}