package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.AuthUser;
import com.iscas.pm.auth.mapper.AuthUserMapper;
import com.iscas.pm.auth.service.AuthUserService;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【auth_user】的数据库操作Service实现
* @createDate 2022-07-18 20:53:41
*/
@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser>
    implements AuthUserService {

}




