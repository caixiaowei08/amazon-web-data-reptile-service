package org.framework.core.common.service;

import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
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


}
