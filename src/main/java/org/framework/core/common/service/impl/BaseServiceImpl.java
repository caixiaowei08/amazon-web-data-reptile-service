package org.framework.core.common.service.impl;

import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import org.framework.core.common.dao.BaseDao;
import org.framework.core.common.service.BaseService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by User on 2017/6/5.
 */
@Service("baseService")
@Transactional
public class BaseServiceImpl implements BaseService{

    @Resource
    private BaseDao baseDao;

    public <T> Serializable save(T entity) {
        return baseDao.save(entity);
    }

    public <T> T find(Class<T> class1, Serializable id) {
        return baseDao.find(class1,id);
    }

    public <T> void saveOrUpdate(T entity) {
        baseDao.saveOrUpdate(entity);
    }

    public <T> void delete(T entity) {
        baseDao.delete(entity);
    }

    public <T> void deleteEntityById(Class entityName, Serializable id) {
        baseDao.deleteEntityById(entityName,id);
    }

    public List getListByCriteriaQuery(DetachedCriteria cq) {
        return baseDao.getListByCriteriaQuery(cq);
    }

    public DataGridReturn getDataGridReturn(CriteriaQuery criteriaQuery) {
        return baseDao.getDataGridReturn(criteriaQuery);
    }

    public Integer getRowCount(DetachedCriteria detachedCriteria) {
        return baseDao.getRowCount(detachedCriteria);
    }

    public Integer getRowSum(DetachedCriteria detachedCriteria){
        return baseDao.getRowSum(detachedCriteria);
    }

    public BigDecimal getRowBigDecimalSum(DetachedCriteria detachedCriteria){
        return baseDao.getRowBigDecimalSum(detachedCriteria);
    }

    public Integer getRowSumPlanBuyerNum(DetachedCriteria detachedCriteria) {
        return baseDao.getSum(detachedCriteria).intValue();
    }
}
