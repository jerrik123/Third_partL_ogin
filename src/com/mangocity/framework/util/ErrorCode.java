package com.mangocity.framework.util;

/**
 * 错误代码定义
 * 
 * @author chenmubing
 * @since 1.0
 */

public interface ErrorCode {

	/**
	 * 基础异常
	 */
	public String BASE_ERROR = "ERROR.BASE"; 

	/**
	 * 数据访问模块异常
	 */
	public String DAO_ERROR = "ERROR.DAO";

	/**
	 * 数据访问模块查询异常
	 */
	public String DAO_ERROR_001 = "ERROR.DAO001";

	/**
	 * 数据访问模块更新异常
	 */
	public String DAO_ERROR_002 = "ERROR.DAO002";

	/**
	 * 数据访问模块删除异常
	 */
	public String DAO_ERROR_003 = "ERROR.DAO003";

	/**
	 * 数据访问模块执行存储过程异常
	 */
	public String DAO_ERROR_004 = "ERROR.DAO004";

	/**
	 * 业务服务模块异常
	 */
	public String SERVICE_ERROR = "ERROR.SERVICE"; 

	/**
	 * 业务服务模块查询异常
	 */
	public String SERVICE_ERROR_001 = "ERROR.SERVICE001";

	/**
	 * 业务服务模块更新异常
	 */
	public String SERVICE_ERROR_002 = "ERROR.SERVICE002";

	/**
	 * 业务服务模块删除异常
	 */
	public String SERVICE_ERROR_003 = "ERROR.SERVICE003";

	/**
	 * 业务服务模块执行存储过程异常
	 */
	public String SERVICE_ERROR_004 = "ERROR.SERVICE004";

	/**
	 * 查询用户列表异常
	 */
	public String SERVICE_ERROR_005 = "ERROR.SERVICE005"; 

	/**
	 * 保存用户异常
	 */
	public String SERVICE_ERROR_006 = "ERROR.SERVICE006"; 

	/**
	 * 数据异常
	 */
	public String DATA_ERROR = "ERROR.DATA"; 

	/**
	 * 页面控制模块异常
	 */
	public String ACTION_ERROR = "ERROR.ACTION";  

}
