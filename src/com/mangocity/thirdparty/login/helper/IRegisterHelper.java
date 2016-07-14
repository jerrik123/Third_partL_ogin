package com.mangocity.thirdparty.login.helper;


import com.mangocity.thirdparty.login.vo.RegisterVO;

public interface IRegisterHelper {
	/**
	 * 注册芒果会员
	 * @param registerBean
	 * @return
	 * @throws Exception
	 */
	public void registerMango(RegisterVO registerBean) throws Exception;
	
	/**
	 * 绑定芒果会员
	 * @param registerBean
	 * @return
	 * @throws Exception
	 */
	public void bindMango(RegisterVO registerBean) throws Exception;
	



}
