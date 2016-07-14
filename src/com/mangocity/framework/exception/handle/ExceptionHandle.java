package com.mangocity.framework.exception.handle;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.framework.exception.ActionException;
import com.mangocity.framework.exception.BaseException;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.framework.exception.DataException;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.framework.util.ConfigureUtils;
import com.mangocity.framework.util.ErrorCode;

/**
 * 异常处理类，负责抛出各类异常，获取异常信息。
 * 
 * @author chenjun
 * @since 1.0
 */
public class ExceptionHandle {

	private static Log log = LogFactory.getLog(ExceptionHandle.class);

	/**
	 * 根据抛出BaseException
	 * 
	 * @param e
	 *            捕获的异常
	 * @param errorCode
	 *            异常编码
	 * @param classAndMethod
	 *            捕获到异常的类名和方法名
	 * @throws BaseException
	 */
	public static void throwBaseException(Exception e, String errorCode,
			String classAndMethod) throws BaseException {
		log.error(errorCode + ":" + classAndMethod);
		if (e.getCause() != null) {
			log.error(e.getCause().getMessage());
		} else if (e.getMessage() != null) {
			log.error(e.getMessage());
		}
		// 打印异常堆栈
		e.printStackTrace();
		// 抛出新封装的异常
		if (errorCode != null) {
			throw new BaseException(errorCode);
		} else {
			throw new BaseException(ErrorCode.BASE_ERROR);
		}
	}

	/**
	 * 根据抛出DaoException
	 * 
	 * @param e
	 *            捕获的异常
	 * @param errorCode
	 *            异常编码
	 * @param classAndMethod
	 *            捕获到异常的类名和方法名
	 * @throws DaoException
	 */
	public static void throwDaoException(Exception e, String errorCode,
			String classAndMethod) throws DaoException {
		log.error(errorCode + ":" + classAndMethod);
		if (e.getCause() != null) {
			log.error(e.getCause().getMessage());
		} else if (e.getMessage() != null) {
			log.error(e.getMessage());
		}
		// 打印异常堆栈
		e.printStackTrace();
		// 抛出新封装的异常
		if (errorCode != null) {
			throw new DaoException(errorCode);
		} else {
			throw new DaoException(ErrorCode.DAO_ERROR);
		}
	}

	/**
	 * 根据抛出ServiceException
	 * 
	 * @param e
	 *            捕获的异常
	 * @param errorCode
	 *            异常编码
	 * @param classAndMethod
	 *            捕获到异常的类名和方法名
	 * @throws ServiceException
	 */
	public static void throwServiceException(Exception e, String errorCode,
			String classAndMethod) throws ServiceException {
		log.error(errorCode + ":" + classAndMethod);
		if (e.getCause() != null) {
			log.error(e.getCause().getMessage());
		} else if (e.getMessage() != null) {
			log.error(e.getMessage());
		}
		// 打印异常堆栈
		e.printStackTrace();
		// 抛出新封装的异常
		if (errorCode != null) {
			throw new ServiceException(errorCode);
		} else {
			throw new ServiceException(ErrorCode.SERVICE_ERROR);
		}
	}

	/**
	 * 根据抛出DataException
	 * 
	 * @param e
	 *            捕获的异常
	 * @param errorCode
	 *            异常编码
	 * @param classAndMethod
	 *            捕获到异常的类名和方法名
	 * @throws DataException
	 */
	public static void throwDataException(Exception e, String errorCode,
			String classAndMethod) throws DataException {
		log.error(errorCode + ":" + classAndMethod);
		if (e.getCause() != null) {
			log.error(e.getCause().getMessage());
		} else if (e.getMessage() != null) {
			log.error(e.getMessage());
		}
		// 打印异常堆栈
		e.printStackTrace();
		// 抛出新封装的异常
		if (errorCode != null) {
			throw new DataException(errorCode);
		} else {
			throw new DataException(ErrorCode.DATA_ERROR);
		}
	}

	/**
	 * 根据抛出ActionException
	 * 
	 * @param e
	 *            捕获的异常
	 * @param errorCode
	 *            异常编码
	 * @param classAndMethod
	 *            捕获到异常的类名和方法名
	 */
	public static ActionException getActionException(Exception e, String errorCode,
			String classAndMethod) {
		log.error(errorCode + ":" + classAndMethod);
		if (e.getCause() != null) {
			log.error(e.getCause().getMessage());
		} else if (e.getMessage() != null) {
			log.error(e.getMessage());
		}
		// 打印异常堆栈
		e.printStackTrace();
		// 抛出新封装的异常
		if (errorCode != null) {
			return new ActionException(errorCode);
		} else {
			return new ActionException(ErrorCode.ACTION_ERROR);
		}
	}

	/**
	 * 从error_zh_CN.properties属性文件中读取相应的错误消息
	 * 
	 * @param e 异常
	 * @return 错误消息 
	 */
	public static String getExceptionValue(Exception e) {
		return ConfigureUtils.getErrorConfig(e.getMessage(),Locale.getDefault());
	}

}
