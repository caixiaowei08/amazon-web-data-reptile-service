package org.framework.core.common.service;

import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by User on 2017/6/5.
 */
public interface BaseService {

    /**
     * 保存对象
     * @param entity
     * @param <T>
     * @return
     */
    public <T> Serializable save(T entity);

    /**
     * 根据实体名称和主键获取实体
     *
     * @param <T>
     * @param
     * @param id
     * @return
     */
    public <T> T find(Class<T> class1, Serializable id);

    /**
     * 保存和修改
     * @param entity
     * @param <T>
     */
    public <T> void saveOrUpdate(T entity);

    /**
     *删除
     * @param entity
     * @param <T>
     */
    public <T> void delete(T entity);

    /**
     * 利用主键删除
     * @param entityName
     * @param id
     * @param <T>
     */
    public <T> void deleteEntityById(Class entityName, Serializable id);

    /**
     * 自定义条件查询
     * @param cq
     * @return
     */
    public List getListByCriteriaQuery(DetachedCriteria cq);

    /**
     * 返回easyui datagrid模型
     * @param criteriaQuery
     * @return
     */
    public DataGridReturn getDataGridReturn(final CriteriaQuery criteriaQuery);

    /**
     * 根据条件获取条数
     * @param detachedCriteria
     * @return
     */
    public Integer getRowCount(DetachedCriteria detachedCriteria);

    /**
     * 求和
     * @param detachedCriteria
     * @return
     */
    public Integer getRowSum(DetachedCriteria detachedCriteria);

    public BigDecimal getRowBigDecimalSum(DetachedCriteria detachedCriteria);

    public Integer getRowSumPlanBuyerNum(DetachedCriteria detachedCriteria);


}
