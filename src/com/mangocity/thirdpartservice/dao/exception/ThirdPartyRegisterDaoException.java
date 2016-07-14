package com.mangocity.thirdpartservice.dao.exception;

import com.mangocity.framework.exception.DaoException;

/**
 * 数据访问模块异常类
 * @author chenjun
 *
 */
public class ThirdPartyRegisterDaoException extends DaoException {
	private static final long serialVersionUID = 2357594864159475349L;

	public ThirdPartyRegisterDaoException(String message) {
		super(message);
	}
}
