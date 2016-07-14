package com.mangocity.framework.base.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.type.NullableType;

import com.mangocity.framework.base.domain.entity.Entity;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.framework.util.ConditionItem;

/**
 * hibernate基本的数据访问操作接口(DAO)
 * 
 * @author chenjun
 * @since 1.0
 * 
 */
public interface HibernateDao {

    // -------------------------------------------------------------------------
    // 以下为调用hql方法
    // -------------------------------------------------------------------------
    public Session getCurrentSession();

    /**
     * 保存或更新一个实体
     * 
     * @param entity
     *            待保存的实体
     * @throws DaoException
     *             数据库层异常
     */
    public void saveOrUpdate(final Entity entity) throws DaoException;

    /**
     * 保存或更新一组实体
     * 
     * @param entities
     *            待保存的实体集合
     * @throws DaoException
     *             数据库层异常异常
     */
    public void saveOrUpdateAll(final Collection<Entity> entities) throws DaoException;

    /**
     * 删除一个实体对象
     * 
     * @param entity
     *            待删除的实体对象
     * @throws DaoException
     *             数据库层异常异常
     */
    public void remove(final Entity entity) throws DaoException;

    /**
     * 删除一组实体对象，删除大量实体时性能低下
     * 
     * @param entities
     *            待删除的实体对象集合
     * @throws DaoException
     *             数据库层异常异常
     */
    public void removeAll(final Collection<Entity> entities) throws DaoException;

    /**
     * 查询一个实体类型的所有实体
     * 
     * @param entity
     *            实体类型
     * @return 实体集合
     * @throws DaoException
     *             数据库层异常异常
     */
    public List findAll(final Entity entity) throws DaoException;

    /**
     * 根据实体id查询实体
     * 
     * @param entity
     *            实体类型
     * @param id
     *            实体id
     * @return 实体对象
     * @throws DaoException
     *             数据库层异常异常
     */
    public Object find(final Entity entity, final Serializable id) throws DaoException;

    /**
     * 执行xml映射文件定义的sql
     * 
     * @param queryName
     *            配置文件中的sql名
     * @param values
     *            参数数组
     * @return 查询结果集合
     * @throws DaoException
     *             数据库层异常异常
     */
    public List findByNamedQuery(final String queryName, final Object[] values) throws DaoException;

    /**
     * 执行xml映射文件定义的sql(分页)
     * 
     * @param queryName
     *            配置文件中的sql名
     * @param values
     *            参数数组
     * @param startIndex
     *            查询起始位置
     * @param maxResults
     *            查询最大结果数
     * @return 查询结果集合
     * @throws DaoException
     *             数据库层异常异常
     */
    public List findByNamedQuery(final String queryName, final Object[] values,
        final int startIndex, final int maxResults) throws DaoException;

    /**
     * 条件hql查询
     * 
     * @param hqlKey
     *            资源文件中hql键值
     * @param conditList
     *            组合条件(hql中如果已经有where条件，排序规则应添加在conditList中),可为null
     * @param values
     *            参数值数组,可为null
     * @return 查询到的集合
     * @throws DaoException
     *             数据库层异常异常
     */
    public List hqlQuery(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException;

    /**
     * 根据hql 查询
     * @param hql
     * @return
     * @throws DaoException
     */
    public List hqlQuery(String hql) throws DaoException;

    /**
     * 条件hql查询(分页)
     * 
     * @param hqlKey
     *            资源文件中hql键值
     * @param conditList
     *            组合条件(hql中如果已经有where条件，排序规则应添加在conditList中),可为null
     * @param values
     *            参数值数组,可为null
     * @param startIndex
     *            开始序号
     * @param maxResults
     *            查询的最大纪录结果数
     * @return 查询到的集合
     * @throws DaoException
     *             数据库层异常异常
     */
    public List hqlQuery(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values, final int startIndex, final int maxResults) throws DaoException;

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
     * @throws DaoException
     *             数据库层异常异常
     */
    public int hqlQueryTotalNum(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException;

    /**
     * 执行hql语句
     * 
     * @param hqlKey
     *            资源文件中hql键值
     * @param conditList
     *            组合条件(hql中如果已经有where条件，排序规则应添加在conditList中),可为null
     * @param values
     *            参数数组,可为null
     * @return update或delete掉的实体数量
     * @throws DaoException
     *             数据库层异常异常
     */
    public int executeHqlWithoutReturn(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException;

    // -------------------------------------------------------------------------
    // 以下为调用本地sql方法
    // -------------------------------------------------------------------------

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
     * @throws DaoException
     *             数据库层异常异常
     */
    public int sqlQueryTotalNum(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, final Map<String, Class> classEntity,
        final Map<String, NullableType> scalars) throws DaoException;

    /**
     * 带组合条件和参数的本地sql查询
     * 
     * @param sqlKey
     *            资源文件中sql键值
     * @param conditList
     *            组合条件(sql中如果已经有where条件，排序规则应添加在conditList中),可为null
     * @param values
     *            参数值数组,可为null
     * @param classEntity
     *            entity的class所在的map集合,可为null
     * @param scalars
     *            标量类型的对应map表,可为null
     * @return 查询结果集合
     * @throws DaoException
     *             数据库层异常异常
     */
    public List sqlQuery(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, final Map<String, Class> classEntity,
        final Map<String, NullableType> scalars) throws DaoException;

    /**
     * 带组合条件和参数的本地sql查询(分页、行号递增)
     * 
     * @param sqlKey
     *            资源文件中sql键值
     * @param conditList
     *            组合条件(sql中如果已经有where条件，排序规则应添加在conditList中),可为null
     * @param values
     *            参数值数组,可为null
     * @param startIndex
     *            查询起始位置
     * @param maxResults
     *            查询最大结果数
     * @param classEntity
     *            entity的class所在的map集合,可为null
     * @param scalars
     *            标量类型的对应map表,可为null
     * @return 查询结果集合
     * @throws DaoException
     *             数据库层异常异常
     */
    public List sqlQuery(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, final int startIndex, final int maxResults,
        final Map<String, Class> classEntity, final Map<String, NullableType> scalars)
        throws DaoException;

    /**
     * 带参数的本地sql更新
     * 
     * @param sqlKey
     *            资源文件中sql键值
     * @param conditList
     *            组合条件(sql中如果已经有where条件，排序规则应添加在conditList中),可为null
     * @param values
     *            参数数组,可为null
     * @throws DaoException
     *             数据库层异常异常
     */
    public void updateSql(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException;

    /**
     * 带参数的存储过程执行
     * 
     * @param sqlKey
     *            资源文件中存储过程语句键值
     * @param values
     *            参数数组
     * @throws DaoException
     *             数据库层异常异常
     */
    public void executeProcedure(final String sqlKey, final Object[] values) throws DaoException;
    
        public List doQuery(String hql)throws DaoException;

    public List doQuery(String hql, Object o)throws DaoException;

    public List doQuery(String hql, Object[] o)throws DaoException;
}