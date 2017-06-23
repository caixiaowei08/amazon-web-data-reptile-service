package com.amazon.admin.promot.service.impl;

import com.amazon.admin.promot.service.AdminPromotService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/23.
 */
@Service("adminPromotService")
@Transactional
public class AdminPromotServiceImpl extends BaseServiceImpl implements AdminPromotService {
}
