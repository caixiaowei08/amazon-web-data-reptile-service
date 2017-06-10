package org.framework.core.global.service.impl;

import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.global.service.GlobalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/10.
 */
@Service("globalService")
@Transactional
public class GlobalServiceImpl extends BaseServiceImpl implements GlobalService {
}
