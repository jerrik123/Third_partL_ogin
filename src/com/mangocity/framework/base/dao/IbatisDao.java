package com.mangocity.framework.base.dao;

import java.util.List;

import com.mangocity.framework.exception.DaoException;

/**
 * ibatis基本的数据访问操作接口(DAO)
 * 
 * @author chenjun
 * @since 1.0
 *
 */
public interface IbatisDao {

	/**
	 * 查询多条记录
	 * 
	 * @param sqlMapId
	 *            sqlMap文件中的id
	 * @param obj
	 *            参数对象(例如：JavaBean,Map,XML...)
	 * @return 查询结果的List
	 * @throws DaoException
	 */
	public List queryForList(String sqlMapId, Object obj) throws DaoException;

	/**
	 * 查询符合条件的第一条记录
	 * 
	 * @param sqlMapId
	 *            sqlMap文件中的id
	 * @param obj
	 *            参数对象(例如：JavaBean,Map,XML...)
	 * @return 符合条件的第一条记录
	 * @throws DaoException
	 */
	public Object queryForObject(String sqlMapId, Object obj)
			throws DaoException;

	/**
	 * 保存新对象
	 * 
	 * @param sqlMapId
	 *            sqlMap文件中的id
	 * @param obj
	 *            参数对象(例如：JavaBean,Map,XML...)
	 * @return 新存入对象的主键
	 * @throws DaoException
	 */
	public Object save(String sqlMapId, Object obj) throws DaoException;

	/**
	 * 更新对象
	 * 
	 * @param sqlMapId
	 *            sqlMap文件中的id
	 * @param obj
	 *            参数对象(例如：JavaBean,Map,XML...)
	 * @return 更新的记录个数
	 * @throws DaoException
	 */
	public int update(String sqlMapId, Object obj) throws DaoException;

	/**
	 * 删除对象
	 * 
	 * @param sqlMapId
	 *            sqlMap文件中的id
	 * @param obj
	 *            参数对象(例如：JavaBean,Map,XML...)
	 * @return 删除的记录个数
	 * @throws DaoException
	 */
	public int delete(String sqlMapId, Object obj) throws DaoException;

}
