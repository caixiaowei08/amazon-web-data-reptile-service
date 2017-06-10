package com.amazon.service.user.service.impl;

import com.amazon.service.user.service.UserService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/6.
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl implements UserService {
}
