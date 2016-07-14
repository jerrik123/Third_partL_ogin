package com.mangocity.framework.base.dao.impl;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.NullableType;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.mangocity.framework.base.dao.HibernateDao;
import com.mangocity.framework.base.domain.entity.Entity;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.framework.exception.handle.ExceptionHandle;
import com.mangocity.framework.util.ConditionItem;
import com.mangocity.framework.util.ConditionItemHandler;
import com.mangocity.framework.util.ConfigureUtils;
import com.mangocity.framework.util.ErrorCode;

/**
 * hibernate基本的数据访问操作接口(DAO)的实现类
 * 
 * @author chenjun
 * @since 1.0
 * @see HibernateDao
 * 
 */
public class HibernateDaoImpl extends HibernateDaoSupport implements HibernateDao {

    // -------------------------------------------------------------------------
    // 以下为调用hql方法
    // -------------------------------------------------------------------------
    public Session getCurrentSession() {
        return getHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    public void saveOrUpdate(final Entity obj) throws DaoException {
        try {
            getHibernateTemplate().saveOrUpdate(obj);
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_002,
                "HibernateDaoImpl.saveOrUpdate()");
        }
    }

    public void saveOrUpdateAll(final Collection<Entity> entities) throws DaoException {
        try {
            getHibernateTemplate().saveOrUpdateAll(entities);
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_002,
                "HibernateDaoImpl.saveOrUpdateAll()");
        }
    }

    public void remove(final Entity obj) throws DaoException {
        try {
            getHibernateTemplate().delete(obj);
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_003,
                "HibernateDaoImpl.remove()");
        }
    }

    public void removeAll(final Collection<Entity> entities) throws DaoException {
        try {
            getHibernateTemplate().deleteAll(entities);
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_003,
                "HibernateDaoImpl.removeAll()");
        }
    }

    public List findAll(final Entity entity) throws DaoException {
        List list = null;
        try {
            list = getHibernateTemplate().find(" from " + entity.getName());
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.findAll()");
        }
        return list;
    }

    public Object find(final Entity entity, final Serializable id) throws DaoException {
        Object object = null;
        HibernateTemplate template = getHibernateTemplate();
        try {
            object = template.get(entity.getClass(), id);
        } catch (Exception e) {
            ExceptionHandle
                .throwDaoException(e, ErrorCode.DAO_ERROR_001, "HibernateDaoImpl.find()");
        }
        return object;
    }

    public List findByNamedQuery(final String queryName, final Object[] values) throws DaoException {
        List list = null;
        try {
            list = getHibernateTemplate().findByNamedQuery(queryName, values);
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.findByNamedQuery()");
        }
        return list;
    }

    public List findByNamedQuery(final String queryName, final Object[] values,
        final int startIndex, final int maxResults) throws DaoException {
        List list = null;
        try {
            list = getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Query query = session.getNamedQuery(queryName);

                    addValueObject(query, values);
                    query.setFirstResult(startIndex);
                    query.setMaxResults(maxResults);
                    return query.list();
                }
            });
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.findByNamedQuery()");
        }
        return list;
    }

    public List hqlQuery(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException {
        List list = null;
        try {
            list = getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Query query = session.createQuery(getParseHSqlByKey(hqlKey, conditList));
                    addValueObject(query, values);
                    return query.list();
                }
            });
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.hqlQuery()");
        }
        return list;
    }

    public List hqlQuery(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values, final int startIndex, final int maxResults) throws DaoException {
        List list = null;
        try {
            list = getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Query query = session.createQuery(getParseHSqlByKey(hqlKey, conditList));
                    addValueObject(query, values);
                    query.setFirstResult(startIndex);
                    query.setMaxResults(maxResults);
                    return query.list();
                }
            });
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.hqlQuery()");
        }
        return list;
    }

    public int hqlQueryTotalNum(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException {
        int num = 0;
        try {
            num = hqlQuery(hqlKey, conditList, values).size();
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.hqlQueryTotalNum()");
        }
        return num;
    }

    public int executeHqlWithoutReturn(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException {
        int num = 0;
        try {
            num = getHibernateTemplate().bulkUpdate(getParseHSqlByKey(hqlKey, conditList), values);
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.executeHqlWithoutReturn()");
        }
        return num;
    }

    // -------------------------------------------------------------------------
    // 以下为调用本地sql方法
    // -------------------------------------------------------------------------

    public int sqlQueryTotalNum(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, final Map<String, Class> classEntity,
        final Map<String, NullableType> scalars) throws DaoException {
        int num = 0;
        try {
            num = sqlQuery(sqlKey, conditList, values, classEntity, scalars).size();
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.sqlQueryTotalNum()");
        }
        return num;
    }

    @SuppressWarnings("unchecked")
    public List sqlQuery(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, final Map<String, Class> classEntity,
        final Map<String, NullableType> scalars) throws DaoException {
        List list = null;
        try {
            list = getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    SQLQuery query = session.createSQLQuery(getParseHSqlByKey(sqlKey, conditList));
                    queryPrepare(query, values, classEntity, scalars);
                    return query.list();
                }
            });
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.sqlQuery()");
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List sqlQuery(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, final int startIndex, final int maxResults,
        final Map<String, Class> classEntity, final Map<String, NullableType> scalars)
        throws DaoException {
        List list = null;
        try {
            list = getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    SQLQuery query = session.createSQLQuery(getParseHSqlByKey(sqlKey, conditList));
                    queryPrepare(query, values, classEntity, scalars);
                    query.setFirstResult(startIndex);
                    query.setMaxResults(maxResults);
                    return query.list();
                }
            });
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.sqlQuery()");
        }
        return list;
    }

    public void updateSql(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException {
        try {
            getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    SQLQuery query = session.createSQLQuery(getParseHSqlByKey(sqlKey, conditList));
                    addValueObject(query, values);
                    return query.executeUpdate();
                }
            });
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_002,
                "HibernateDaoImpl.updateSql()");
        }
    }

    @SuppressWarnings("deprecation")
    public void executeProcedure(final String sqlKey, final Object[] values) throws DaoException {
        try {
            getHibernateTemplate().execute(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                    Connection con = session.connection();
                    CallableStatement cstmt;
                    cstmt = con.prepareCall(ConfigureUtils.getHSqlConfig(sqlKey, Locale
                        .getDefault()));
                    addValueObject(cstmt, values);
                    return cstmt.executeUpdate();
                }
            });
        } catch (Exception e) {
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_004,
                "HibernateDaoImpl.executeProcedure()");
        }
    }

    // -------------------------------------------------------------------------
    // 以下内部方法
    // -------------------------------------------------------------------------

    /**
     * 获得经过解析的hsql语句 将conditList里面的where条件解析出来并绑定到对应的hsql中
     * 
     * @param hsqlKey
     *            资源文件中hsql键值
     * @param conditList
     *            where条件List
     * @return 经过解析后的hql或sql语句
     */
    private String getParseHSqlByKey(final String hsqlKey, final List<ConditionItem> conditList) {
        String sql;
        String sqlNotParse = ConfigureUtils.getHSqlConfig(hsqlKey, Locale.getDefault());
        if (conditList != null) {
            sql = ConditionItemHandler.parseWhereCondition(new StringBuffer(sqlNotParse),
                conditList).toString();
        } else {
            sql = sqlNotParse;
        }
        return sql;
    }

    private void queryPrepare(SQLQuery query, final Object[] values,
        final Map<String, Class> classEntity, final Map<String, NullableType> scalars) {
        addValueObject(query, values);
        addEntity(query, classEntity);
        addScalar(query, scalars);
    }

    /**
     * 在查询语句中绑定参数，如果参数数组为空，不做任何绑定
     * 
     * @param query
     *            SQLQuery语句
     * @param values
     *            参数数组
     */
    private void addValueObject(Query query, final Object[] values) {
        for (int i = 0; values != null && i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
    }

    /**
     * 在CallableStatement语句中绑定参数，如果参数数组为空，不做任何绑定(主要用于存储过程)
     * 
     * @param cstmt
     *            CallableStatement对象
     * @param values
     *            参数数组
     * @throws SQLException
     *             数据层异常
     */
    private void addValueObject(CallableStatement cstmt, final Object[] values) throws SQLException {
        for (int i = 0; values != null && i < values.length; i++) {
            cstmt.setObject(i, values[i]);
        }
    }

    /**
     * 在查询语句中添加需要实例化的实体类型
     * 
     * @param query
     *            SQLQuery语句
     * @param classEntity
     *            entity的class对应map表
     */
    @SuppressWarnings("unchecked")
    private void addEntity(SQLQuery query, final Map<String, Class> classEntity) {
        if (classEntity != null) {
            for (String s : classEntity.keySet()) {
                query.addEntity(s, classEntity.get(s));
            }
        }
    }

    /**
     * 在查询语句中添加标量类型
     * 
     * @param query
     *            SQLQuery语句
     * @param scalars
     *            标量类型的对应map表
     */
    private void addScalar(SQLQuery query, final Map<String, NullableType> scalars) {
        if (scalars != null) {
            for (String s : scalars.keySet()) {
                query.addScalar(s, scalars.get(s));
            }
        }
    }

    public List hqlQuery(String hql) throws DaoException {
        List list = null;
        try {

            Query query = getCurrentSession().createQuery(hql);
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandle.throwDaoException(e, ErrorCode.DAO_ERROR_001,
                "HibernateDaoImpl.hqlQuery(String hql)");
        }
        return list;
    }
    public List doQuery(String hql) {
        return getHibernateTemplate().find(hql);
    }

    public List doQuery(String hql, Object o) {
        return getHibernateTemplate().find(hql, o);
    }

    public List doQuery(String hql, Object[] o) {
        return getHibernateTemplate().find(hql, o);
    }
}
