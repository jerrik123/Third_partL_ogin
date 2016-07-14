package com.mangocity.framework.exception;

import org.springframework.dao.DataAccessException;

/**
 * 数据访问模块异常类
 * 
 * @author chenjun
 * @since 1.0
 * @see BaseException
 */
public class DaoException extends DataAccessException {

	private static final long serialVersionUID = 2357594864159475349L;

	
	public DaoException(String message) {
		super(message);
	}

}