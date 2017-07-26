package org.framework.core.common.dao;

import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by User on 2017/6/5.
 */
public interface BaseDao {

    /**
     * 保存
     * @param entity
     * @param <T>
     * @return
     */
    public <T> Serializable save(T entity);

    /**
     * 保存修改
     * @param entity
     * @param <T>
     */
    public <T> void saveOrUpdate(T entity);

    /**
     * 删除实体
     *
     * @param <T>
     *
     * @param <T>
     *
     * @param <T>
     * @param entitie
     */
    public <T> void delete(T entitie);

    /**
     * 根据实体名称和主键获取实体
     *
     * @param <T>
     * @param entityName
     * @param id
     * @return
     */
    public <T> T find(Class<T> entityName, Serializable id);

    public <T> void deleteEntityById(Class entityName, Serializable id);

    /**
     * 条件查询 设置参数
     * @param cq
     * @return
     */
    public List getListByCriteriaQuery(DetachedCriteria cq);

    /**
     * 返回easyui datagrid模型
     *
     * @param criteriaQuery
     * @return
     */
    public DataGridReturn getDataGridReturn(final CriteriaQuery criteriaQuery);

    /**
     * 获取条数
     */
    public Integer getRowCount(DetachedCriteria detachedCriteria);

    /**
     * 获取某一项的和
     */
    public Integer getRowSum(DetachedCriteria detachedCriteria);

    /**
     * 获取某一项的和
     */
    public BigDecimal getRowBigDecimalSum(DetachedCriteria detachedCriteria);

    /**
     * 获取某一项的和
     */
    public Long getSum(DetachedCriteria detachedCriteria);


}
