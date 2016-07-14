package com.mangocity.thirdparty.login.helper;

import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.thirdparty.login.vo.FanliParamVo;


/**
 * 返利用户注册芒果会员接口
 * @author panshilin
 *
 */
public interface IFanliRegisterHelper {
	/**
	 * 注册芒果会员
	 * @param registerBean
	 * @return
	 * @throws Exception
	 */
	public PersonMainInfo registerMangoFromFanli(FanliParamVo fanliParamVo) throws Exception;
}
