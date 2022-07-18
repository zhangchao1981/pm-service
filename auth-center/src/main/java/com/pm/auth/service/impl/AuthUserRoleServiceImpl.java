package auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import auth.domain.AuthUserRole;
import auth.service.AuthUserRoleService;
import auth.mapper.AuthUserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【auth_user_role】的数据库操作Service实现
* @createDate 2022-07-18 20:53:49
*/
@Service
public class AuthUserRoleServiceImpl extends ServiceImpl<AuthUserRoleMapper, AuthUserRole>
    implements AuthUserRoleService{

}




