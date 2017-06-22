package com.amazon.service.user.service;

import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.user.vo.UserBaseInfoVo;
import org.framework.core.common.service.BaseService;

/**
 * Created by User on 2017/6/6.
 */
public interface UserService extends BaseService{

    public UserEntity doRegister(UserEntity userEntity);

    public UserBaseInfoVo doGetBaseUserInfo(UserEntity userEntity);


}
