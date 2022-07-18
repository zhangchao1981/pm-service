package auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import auth.domain.AuthRolePermission;
import auth.service.AuthRolePermissionService;
import auth.mapper.AuthRolePermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【auth_role_permission】的数据库操作Service实现
* @createDate 2022-07-18 20:53:25
*/
@Service
public class AuthRolePermissionServiceImpl extends ServiceImpl<AuthRolePermissionMapper, AuthRolePermission>
    implements AuthRolePermissionService{

}




