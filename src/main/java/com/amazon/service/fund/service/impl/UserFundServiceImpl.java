package com.amazon.service.fund.service.impl;

import com.amazon.service.fund.service.UserFundService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/10.
 */
@Service("userFundService")
@Transactional
public class UserFundServiceImpl extends BaseServiceImpl implements UserFundService {
}
