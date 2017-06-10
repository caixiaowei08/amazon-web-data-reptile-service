package com.amazon.service.recharge.service.impl;

import com.amazon.service.recharge.service.UserRechargeFundService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/10.
 */
@Service("userRechargeFundService")
@Transactional
public class UserRechargeFundServiceImpl extends BaseServiceImpl implements UserRechargeFundService {
}
