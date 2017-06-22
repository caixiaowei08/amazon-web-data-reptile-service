package com.amazon.service.promot.order.service;

import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.spider.pojo.AmazonPageInfoPojo;
import com.amazon.service.user.entity.UserEntity;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.BaseService;

/**
 * Created by User on 2017/6/10.
 */
public interface PromotOrderService extends BaseService {

    public AjaxJson doAddNewPromot(UserEntity userEntity, AmazonPageInfoPojo amazonPageInfoPojo, PromotOrderEntity promotOrderEntity);
}
