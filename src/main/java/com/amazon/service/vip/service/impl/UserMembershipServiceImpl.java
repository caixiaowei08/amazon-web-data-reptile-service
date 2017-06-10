package com.amazon.service.vip.service.impl;

import com.amazon.service.vip.service.UserMembershipService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/10.
 */
@Service("userMembershipService")
@Transactional
public class UserMembershipServiceImpl extends BaseServiceImpl implements UserMembershipService {
}
