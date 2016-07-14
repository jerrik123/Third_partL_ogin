package com.mangocity.framework.base.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.type.NullableType;

import com.mangocity.framework.base.dao.HibernateDao;
import com.mangocity.framework.base.dao.IbatisDao;
import com.mangocity.framework.base.domain.entity.Entity;
import com.mangocity.framework.base.service.BaseService;
import com.mangocity.framework.exception.DaoException;
import com.mangocity.framework.exception.ServiceException;
import com.mangocity.framework.exception.handle.ExceptionHandle;
import com.mangocity.framework.util.ConditionItem;
import com.mangocity.framework.util.ErrorCode;
import com.mangocity.framework.util.Page;

/**
 * 用于DB操作的service接口的实现类，注入了HibernateDao和IbatisDao，封装了两种ORM的数据库操作方法
 * 
 * @author chenmubing,sibaofeng
 * @since 1.0
 * @see BaseService
 */
public class BaseServiceImpl implements BaseService {

    /**
     * 注入HibernateDaoImpl
     */
    private HibernateDao hibernateDao;

    /**
     * 注入ibatisDaoImpl
     */
    private IbatisDao ibatisDao;

    /*----------------------Hibernate方法------------------------------*/

    public Long saveOrUpdate(final Entity entity) throws DaoException, ServiceException {
        try {
            getHibernateDao().saveOrUpdate(entity);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_002,
                "BaseServiceImpl.saveOrUpdate()");
        }
        return entity.getId();
    }

    public void saveOrUpdateAll(final List<Entity> entities) throws DaoException, ServiceException {
        try {
            getHibernateDao().saveOrUpdateAll(entities);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_002,
                "BaseServiceImpl.saveOrUpdateAll()");
        }
    }

    public void remove(final Entity entity) throws DaoException, ServiceException {
        try {
            getHibernateDao().remove(entity);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_003,
                "BaseServiceImpl.remove()");
        }
    }

    public void remove(final List<Entity> entities) throws DaoException, ServiceException {
        try {
            getHibernateDao().removeAll(entities);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_003,
                "BaseServiceImpl.remove()");
        }
    }

    public void executeHqlUpdate(final String hqlKey, final List<ConditionItem> conditList,
        Object[] values) throws DaoException, ServiceException {
        try {
            getHibernateDao().executeHqlWithoutReturn(hqlKey, conditList, values);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_002,
                "BaseServiceImpl.executeHqlUpdate()");
        }
    }

    public List hqlQuery(final Entity entity) throws DaoException, ServiceException {
        List list = null;
        try {
            list = getHibernateDao().findAll(entity);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.hqlQuery()");
        }
        return list;
    }

    public Entity hqlQuery(final Entity entity, Serializable id) throws DaoException,
        ServiceException {
        Entity et = null;
        try {
            et = (Entity) getHibernateDao().find(entity, id);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.hqlQuery()");
        }
        return et;
    }

    public List hqlQuery(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException, ServiceException {
        List list = null;
        try {
            list = getHibernateDao().hqlQuery(hqlKey, conditList, values);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.hqlQuery()");
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public void hqlQuery(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values, Page page) throws DaoException, ServiceException {
        try {
            int totalNum = hqlQueryTotalNum(hqlKey, conditList, values);
            List list = null;
            list = getHibernateDao().hqlQuery(hqlKey, conditList, values, page.getStartIndex(),
                page.getPageSize());
            if (null != list) {
                page.setTotalNum(totalNum);
                page.setPageList(list);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.hqlQuery()");
        }
    }

    public int hqlQueryTotalNum(final String hqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException, ServiceException {
        int num = 0;
        try {
            num = getHibernateDao().hqlQueryTotalNum(hqlKey, conditList, values);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.hqlQueryTotalNum()");
        }
        return num;
    }

    public int sqlQueryTotalNum(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values, Map<String, Class> classEntity, Map<String, NullableType> scalars)
        throws DaoException, ServiceException {
        int num = 0;
        try {
            num = getHibernateDao().sqlQueryTotalNum(sqlKey, conditList, values, classEntity,
                scalars);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.sqlQueryTotalNum()");
        }
        return num;
    }

    public List sqlQuery(String sqlKey, List<ConditionItem> conditList, Object[] values,
        Map<String, Class> classEntity, Map<String, NullableType> scalars) throws DaoException,
        ServiceException {
        List list = null;
        try {
            list = getHibernateDao().sqlQuery(sqlKey, conditList, values, classEntity, scalars);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.sqlQuery()");
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public void sqlQuery(String sqlKey, List<ConditionItem> conditList, Object[] values,
        Map<String, Class> classEntity, Map<String, NullableType> scalars, Page page)
        throws DaoException, ServiceException {
        try {
            int totalNum = sqlQueryTotalNum(sqlKey, conditList, values, classEntity, scalars);
            List list = getHibernateDao().sqlQuery(sqlKey, conditList, values,
                page.getStartIndex(), page.getPageSize(), classEntity, scalars);
            if (null != list) {
                page.setTotalNum(totalNum);
                page.setPageList(list);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.sqlQuery()");
        }
    }

    public void executeProcedure(final String procKey, final Object[] values) throws DaoException,
        ServiceException {
        try {
            getHibernateDao().executeProcedure(procKey, values);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.sqlQuery()");
        }
    }

    public void executeSqlUpdate(final String sqlKey, final List<ConditionItem> conditList,
        final Object[] values) throws DaoException, ServiceException {
        try {
            getHibernateDao().updateSql(sqlKey, conditList, values);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.sqlQuery()");
        }
    }

    /**
     * 获取HibernateDao，不推荐继承此类的子类直接使用
     * 
     * @return HibernateDao
     */
    public HibernateDao getHibernateDao() {
        return hibernateDao;
    }

    /**
     * 设置HibernateDao，主要用于spring注入
     * 
     * @param hibernateDao
     */
    public void setHibernateDao(HibernateDao hibernateDao) {
        this.hibernateDao = hibernateDao;
    }

    /**
     * 获取IbatisDao，不推荐继承此类的子类直接使用
     * 
     * @return IbatisDao
     */
    public IbatisDao getIbatisDao() {
        return ibatisDao;
    }

    /**
     * 设置IbatisDao，主要用于spring注入
     * 
     * @param ibatisDao
     */
    public void setIbatisDao(IbatisDao ibatisDao) {
        this.ibatisDao = ibatisDao;
    }

    /*-----------------------iBATIS方法--------------------------------*/

    public List queryForList(String sqlMapId, Object obj) throws DaoException, ServiceException {
        List list = null;
        try {
            list = getIbatisDao().queryForList(sqlMapId, obj);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.queryForList()");
        }
        return list;
    }

    public Object queryForObject(String sqlMapId, Object obj) throws DaoException, ServiceException {
        Object object = null;
        try {
            object = getIbatisDao().queryForList(sqlMapId, obj);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.queryForObject()");
        }
        return object;
    }

    public List hqlQuery(String hql) throws DaoException, ServiceException {
        List list = null;
        try {
            list = getHibernateDao().hqlQuery(hql);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandle.throwServiceException(e, ErrorCode.SERVICE_ERROR_001,
                "BaseServiceImpl.hqlQuery()");
        }
        return list;
    }

    /*---------------------禁止使用iBATIS的增删改方法--------------------------------*/
    // public Object save(String sqlMapId, Object obj) {
    // return getIbatisDao().save(sqlMapId, obj);
    // }
    //
    // public int update(String sqlMapId, Object obj) {
    // return getIbatisDao().update(sqlMapId, obj);
    // }
    //
    // public int delete(String sqlMapId, Object obj) {
    // return getIbatisDao().delete(sqlMapId, obj);
    // }
}
