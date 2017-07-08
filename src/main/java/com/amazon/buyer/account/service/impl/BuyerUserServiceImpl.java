package com.amazon.buyer.account.service.impl;

import com.amazon.buyer.account.service.BuyerUserService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/7/8.
 */
@Service("buyerUserService")
@Transactional
public class BuyerUserServiceImpl  extends BaseServiceImpl implements BuyerUserService {
}
