package auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import auth.domain.AuthUser;
import auth.service.AuthUserService;
import auth.mapper.AuthUserMapper;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【auth_user】的数据库操作Service实现
* @createDate 2022-07-18 20:53:41
*/
@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser>
    implements AuthUserService{

}




