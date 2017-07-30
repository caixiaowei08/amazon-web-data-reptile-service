package com.amazon.author.account.service.impl;

import com.amazon.author.account.service.AuthorUserService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/7/30.
 */
@Service("authorUserService")
@Transactional
public class AuthorUserServiceImpl extends BaseServiceImpl implements AuthorUserService {
}
