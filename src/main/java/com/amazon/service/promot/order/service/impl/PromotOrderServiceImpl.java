package com.amazon.service.promot.order.service.impl;

import com.amazon.service.promot.order.service.PromotOrderService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/10.
 */
@Service("promotOrderService")
@Transactional
public class PromotOrderServiceImpl extends BaseServiceImpl implements PromotOrderService {
}
