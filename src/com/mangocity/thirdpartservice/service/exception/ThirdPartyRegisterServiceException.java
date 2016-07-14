package com.mangocity.thirdpartservice.service.exception;

import com.mangocity.framework.exception.ServiceException;

/**
 * 业务服务模块异常类
 * 查询第三方会员注册信息异常
 * @author chenjun
 *
 */
@SuppressWarnings("serial")
public class ThirdPartyRegisterServiceException extends ServiceException {
	public ThirdPartyRegisterServiceException(String message) {
		super(message);
	}
}
