package com.amazon.admin.evaluate.service;

import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.BaseService;

/**
 * Created by User on 2017/6/23.
 */
public interface EvaluateService extends BaseService{

    public AjaxJson doAddEvaluateWithNoReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity);


}
