package com.amazon.service.promot.price.service.impl;

import com.amazon.service.promot.price.service.PromotPriceService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/10.
 */
@Service("promotPriceService")
@Transactional
public class PromotPriceServiceImpl extends BaseServiceImpl implements PromotPriceService {
}
