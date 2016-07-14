package com.mangocity.framework.base.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.type.NullableType;

import com.mangocity.framework.base.domain.entity.Entity;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.framework.util.ConditionItem;
import com.mangocity.framework.util.Page;

/**
 * 用于DB操作的service接口
 * 
 * @author chenjun
 * @since 1.0
 */
public interface BaseService {

    /*----------------------Hibernate方法------------------------------*/

    /**
     * 保存或更新实体对象，entity的id为空就是增加，entity的id不为null就是修改
     * 
     * @param entity
     *            待保存的实体
     * @return 实体的id
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public Long saveOrUpdate(final Entity entity) throws DaoException, ServiceException;

    /**
     * 保存或更新一组实体对象，entity的id为空就是增加，entity的id不为null就是修改
     * 
     * @param entities
     *            待保存的实体集合
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public void saveOrUpdateAll(final List<Entity> entities) throws DaoException, ServiceException;

    /**
     * 删除实体对象
     * 
     * @param entity
     *            删除对象
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public void remove(final Entity entity) throws DaoException, ServiceException;

    /**
     * 删除一组实体对象
     * 
     * @param entities
     *            待删除对象集合
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public void remove(final List<Entity> entities) throws DaoException, ServiceException;

    /**
     * 执行一条hql非查询语句
     * 
     * @param hqlKey
     *            资源文件中hql或sql键值
     * @param conditList
     *            查询条件集合,可为null
     * @param values
     *            参数数组,可为null
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public void executeHqlUpdate(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException, ServiceException;

    /**
     * 根据实体类的类型进行查询
     * 
     * @param entity
     *            实体类的类型
     * @return 查询结果集合
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public List hqlQuery(final Entity entity) throws DaoException, ServiceException;

    /**
     * 根据hql进行查询
     * 
     * @param hql
     *            hql
     * @return 查询结果集合
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public List hqlQuery(String hql) throws DaoException, ServiceException;

    /**
     * 根据实体类的类型和id查询
     * 
     * @param entity
     *            实体类的类型
     * @param id
     *            实体的id
     * @return 查询结果实体
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public Entity hqlQuery(final Entity entity, Serializable id) throws DaoException,
        ServiceException;

    /**
     * hql查询
     * 
     * @param hqlKey
     *            配置文件中的hql语句键值
     * @param conditList
     *            查询条件集合,可为null
     * @param values
     *            参数数组,可为null
     * @return 查询结果集合
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public List hqlQuery(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException, ServiceException;

    /**
     * hql查询
     * 
     * @param hqlKey
     *            配置文件中的hql语句键值
     * @param conditList
     *            查询条件集合,可为null
     * @param values
     *            参数数组,可为null
     * @param page
     *            分页对象
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public void hqlQuery(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values, Page page) throws DaoException, ServiceException;

    /**
     * 根据一个带参数数组和查询条件的hql来查询总共的纪录数
     * 
     * @param hqlKey
     *            资源文件中hql键值
     * @param conditList
     *            组合条件(hql中如果已经有where条件，排序规则应添加在conditList中),可为null
     * @param values
     *            参数值数组,可为null
     * @return 总共的纪录数
     * @throws DaoException,
     *             ServiceException 数据层异常
     */
    public int hqlQueryTotalNum(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException, ServiceException;

    /**
     * 根据一个带参数数组和查询条件的hql来查询总共的纪录数
     * 
     * @param sqlKey
     *            资源文件中sql键值
     * @param conditList
     *            组合条件(sql中如果已经有where条件，排序规则应添加在conditList中),可为null
     * @param values
     *            参数值数组,可为null
     * @return 总共的纪录数
     * @throws DaoException,
     *             ServiceException 数据层异常
     */
    public int sqlQueryTotalNum(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, final Map<String, Class> classEntity,
        final Map<String, NullableType> scalars) throws DaoException, ServiceException;

    /**
     * sql查询
     * 
     * @param sqlKey
     *            配置文件中的sql语句键值
     * @param conditList
     *            查询条件集合,可为null
     * @param values
     *            参数数组,可为null
     * @param classEntity
     *            entity的class所在的map集合,可为null
     * @param scalars
     *            标量类型的所在的map集合,可为null
     * @return 查询结果集合
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    @SuppressWarnings("unchecked")
    public List sqlQuery(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, final Map<String, Class> classEntity,
        final Map<String, NullableType> scalars) throws DaoException, ServiceException;

    /**
     * sql查询
     * 
     * @param sqlKey
     *            配置文件中的sql语句键值
     * @param conditList
     *            查询条件集合
     * @param values
     *            参数数组
     * @param classEntity
     *            entity的class所在的map集合
     * @param scalars
     *            标量类型的所在的map集合
     * @param page
     *            分页对象
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public void sqlQuery(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, final Map<String, Class> classEntity,
        final Map<String, NullableType> scalars, Page page) throws DaoException, ServiceException;

    /**
     * 执行存储过程
     * 
     * @param procKey
     *            配置文件中的存储过程键值
     * @param values
     *            参数数组,可为null
     * @throws DaoException,
     *             ServiceException 服务层异常
     */
    public void executeProcedure(final String procKey, final Object[] values) throws DaoException,
        ServiceException;

    /**
     * 带参数的本地sql更新
     * 
     * @param sqlKey
     *            资源文件中sql键值
     * @param conditList
     *            组合条件(sql中如果已经有where条件，排序规则应添加在conditList中),可为null
     * @param values
     *            参数数组,可为null
     * @throws DaoException,
     *             ServiceException 数据层异常
     */
    public void executeSqlUpdate(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException, ServiceException;

    /*-----------------------iBATIS方法--------------------------------*/

    /**
     * 查询多条记录
     * 
     * @param sqlMapId
     *            sqlMap文件中的id
     * @param obj
     *            参数对象(例如：JavaBean,Map,XML...)
     * @return 查询结果的List
     * @throws DaoException,
     *             ServiceException
     */
    public List queryForList(String sqlMapId, Object obj) throws DaoException, ServiceException;

    /**
     * 查询符合条件的第一条记录
     * 
     * @param sqlMapId
     *            sqlMap文件中的id
     * @param obj
     *            参数对象(例如：JavaBean,Map,XML...)
     * @return 符合条件的第一条记录
     * @throws DaoException,
     *             ServiceException
     */
    public Object queryForObject(String sqlMapId, Object obj) throws DaoException, ServiceException;

    /*---------------------禁止使用iBATIS的增删改方法--------------------------------*/
    /**
     * 保存新对象
     * 
     * @param sqlMapId
     *            sqlMap文件中的id
     * @param obj
     *            参数对象(例如：JavaBean,Map,XML...)
     * @return 新存入对象的主键
     * @throws DaoException,
     *             ServiceException
     */
    // public Object save(String sqlMapId, Object obj) throws
    // ServiceException;
    /**
     * 更新对象
     * 
     * @param sqlMapId
     *            sqlMap文件中的id
     * @param obj
     *            参数对象(例如：JavaBean,Map,XML...)
     * @return 更新的记录个数
     * @throws DaoException,
     *             ServiceException
     */
    // public int update(String sqlMapId, Object obj) throws
    // ServiceException;
    /**
     * 删除对象
     * 
     * @param sqlMapId
     *            sqlMap文件中的id
     * @param obj
     *            参数对象(例如：JavaBean,Map,XML...)
     * @return 删除的记录个数
     * @throws DaoException,
     *             ServiceException
     */
    // public int delete(String sqlMapId, Object obj) throws
    // ServiceException;
}
