package com.mangocity.thirdparty.login.action.qmango;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.mangocity.model.mbr.Mbr;
import com.mangocity.model.mbrship.MbrshipCategory;
import com.mangocity.model.person.Person;
import com.mangocity.services.mbr.IMbrService;
import com.mangocity.services.mbrship.IMbrshipCategoryService;
import com.mangocity.services.mbrsys.IWasconfigService;
import com.mangocity.services.person.IPersonService;
import com.mangocity.thirdparty.login.action.ApplicationAction;
import com.mangocity.thirdparty.login.vo.QmangoResultVO;
import com.mangocity.util.MD5Algorithm;

/**
 * This class is used mangocity member to qmango.If member is logined,give
 * mangocsn to qmango.
 * 
 * @author shilin.pan
 * 
 */
@SuppressWarnings("serial")
public class QmangoVerifyAction extends ApplicationAction {
	private String qmangoId;
	private String result;
	private String mangocsn;
	private String key;
	//加密串
	private static final String keyGen = "@B7coQt$2Lo8cI1W13g0$zx7R6p94pOX";
	/**
	 * 会员会籍渠道
	 */
	private String mbrshipcategorycd;
	/**
	 * 调用EJB会员查询接口
	 */
	private IMbrService mbrService;
	/**
	 * 调用EJB自然人信息接口
	 */
	private IPersonService personService;
	/**
	 * 调用EJB接口查询wasconfig表参数
	 */
	private IWasconfigService wasconfigService;
	/**
	 * 调用EJB查询会籍渠道接口
	 */
	private IMbrshipCategoryService mbrshipCategoryService;
	

	/**
	 * 
	 * Member from qmango to mangocity. 用户从Qmango到mangocity系统需做以下处理
	 * 1验证请求中是否带有qmangoId和key.如果没有则直接进入芒果网的首页.
	 * 2用qmangoId和key请求qmango的验证中心验证用户是否登录，如果已登录则返回
	 * 对应的uid和会员的其它信息.如果说没有登陆则直接进入芒果网首页。
	 * 3在qmango已登录的用户，系统先用uid查询用户是否已经绑定如果已经绑定，则直 接使用已绑定的用户登陆。并进入首页。
	 * 4如果用户没有绑定到芒果网，则系统需执行绑定。绑定需执行以下的处理。
	 * a、如果手机号或邮箱存在则直接使用该手机或该邮箱的用户绑定会员。绑定完成后用该 用户自动登录。
	 * b、手机号和邮箱不存在，则需使用从qmango得到的会员资料，利用得到的资进行会员注册， 其注册会籍为芒果会籍。密码为MD5(UUID)
	 * 
	 * 
	 * idea:上面的业务可以使用责任链设计设模式来实现。链的构造会放在spring的配置里。
	 * 
	 * 
	 * 
	 * @return
	 */
	public String inSite() {
//		if (qmangoId == null || key == null) {
//			return SUCCESS;
//		}
//		VerifyRequest request = new VerifyRequest();
//		request.setRequest(this.getRequest());
//		request.setResponse(this.getResponse());
//		request.setKey(key);
//		request.setUid(qmangoId);
//		try {
//			qmangoVerifyHandler.handleRequest(request);
//		} catch (Throwable tr) {
//			tr.printStackTrace();
//			return ERROR;
//		}

		return SUCCESS;
	}

	/**
	 * Member from mangocity to qmango.
	 * 
	 * @return
	 */
	public String outSite() {
		try {
			Mbr mbr = alreadyLogin(getRequest());// new
			mangocsn="0";
			key="";
			//会籍渠道号
			mbrshipcategorycd="";
			if (null != mbr) {
				this.mangocsn = String.valueOf(mbr.getMbrId());
				//会员点击青芒时的当前会籍渠道
				mbrshipcategorycd = getRequest().getParameter("mbrshipcategorycd");
				//key = java.util.UUID.randomUUID().toString();
				//封装加密串
				MD5Algorithm oMD5Algorithm = new MD5Algorithm();
				key = oMD5Algorithm.getMD5ofStr((oMD5Algorithm.getMD5ofStr(mangocsn) + keyGen + mbrshipcategorycd));
				System.out.println("封装加密串KEY:"+key);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	

	public String getMemberInfo() {
		try {
			String ip=this.catchIdAdress(getRequest());
			System.out.println("调用者IP："+ip);
			//查询wasconfig表中paraname为mbr.third.update.password.ips的值
			String sParaValue = wasconfigService.getConfValueByName("mbr.third.update.password.ips");
			Set<String> ipSet=new HashSet<String>();
			if(null != sParaValue && !"".equals(sParaValue)){
				String[] ips = sParaValue.split(";");
				for(String ipStr:ips){
					ipSet.add(ipStr);
				}
			}
			for(String tip:ipSet){
				System.out.println("====tip====:"+tip);
			}
			if(!(ipSet.contains(ip))){
				return ERROR;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Mbr mbr = null;
		Person person = null;
		MbrshipCategory mbrshipCategory = null;
		try {
			//青芒传来的芒果网会员CSN
			String vmangocsn = getRequest().getParameter("mangocsn");
			//青芒传来的芒果网会员点击青芒时的当前会籍渠道
			String vscd = getRequest().getParameter("scd");
			//青芒传来的key验证码
			String vkey = getRequest().getParameter("key");
			//验证是否参数合法
			MD5Algorithm mD5Algorithm = new MD5Algorithm();
			String validateKey = mD5Algorithm.getMD5ofStr((mD5Algorithm.getMD5ofStr(vmangocsn) + keyGen + vscd));
			System.out.println("获取加密串KEY:"+key);
			if(!validateKey.equals(vkey)){
				System.out.println("返回参数调用不合法，返回主页!!");
				return ERROR;
			}
			//通过调用EJB接口查询会员信息
			mbr = mbrService.mbrById(Long.parseLong(vmangocsn));
			person = personService.queryPersonById(mbr.getPersonId().toString());
			mbrshipCategory = mbrshipCategoryService.mbrshipCategoryByCategoryCd(vscd);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		QmangoResultVO resultVO = new QmangoResultVO();
		//封装Qmango对象
		resultVO.setCsn(mbr.getMbrId());
		resultVO.setEmail(person.getEmailAddr());
		resultVO.setMobile(person.getMobileNo());
		resultVO.setGender((person.getGender() == null ? "" : person.getGender().toString()));
		resultVO.setName(person.getNameCn());
		resultVO.setCategoryCd(mbrshipCategory.getCategoryCd());
		resultVO.setCategoryName(mbrshipCategory.getCategoryName());
		resultVO.setAliasCardTyp(mbrshipCategory.getAliasCardTyp());
		JSONObject jo = JSONObject.fromObject(resultVO);
		result = jo.toString();
		System.out.println("返回给青芒果的result信息："+result);
		return "vjson";
	}
	
	
	/**
	 * 用于青芒果
	 * @param request
	 * @return
	 * @throws Exception
	 */
    private Mbr alreadyLogin(HttpServletRequest request) throws Exception {
        Long id = null;
        Mbr oMbr = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                if ("mbrID".equals(cookies[i].getName())) {
                    if ((cookies[i].getValue() != null) && (!"".equals(cookies[i].getValue()))) {
                        id = Long.parseLong(cookies[i].getValue());
                        oMbr = mbrService.mbrById(id);
                        break;
                    }
                }
            }
        }
        return oMbr;
    }
	

	public String getQmangoId() {
		return qmangoId;
	}

	public void setQmangoId(String qmangoId) {
		this.qmangoId = qmangoId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMangocsn() {
		return mangocsn;
	}

	public void setMangocsn(String mangocsn) {
		this.mangocsn = mangocsn;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setMbrService(IMbrService mbrService) {
		this.mbrService = mbrService;
	}

	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	public void setWasconfigService(IWasconfigService wasconfigService) {
		this.wasconfigService = wasconfigService;
	}

	public void setMbrshipCategoryService(
			IMbrshipCategoryService mbrshipCategoryService) {
		this.mbrshipCategoryService = mbrshipCategoryService;
	}

	public String getMbrshipcategorycd() {
		return mbrshipcategorycd;
	}

	public void setMbrshipcategorycd(String mbrshipcategorycd) {
		this.mbrshipcategorycd = mbrshipcategorycd;
	}

}
