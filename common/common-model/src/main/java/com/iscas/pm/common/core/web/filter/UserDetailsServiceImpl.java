package com.iscas.pm.common.core.web.filter;

import com.iscas.pm.common.core.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义UserDetailsService
 *
 * @author 郑智聪
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
//    @Autowired
//    AuthServerClient authServerClient;

    @Override
    public UserDetails loadUserByUsername(String userId, String projectId) throws UsernameNotFoundException {
        List<Permission> permissions = new ArrayList<>();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

//        //非超级管理员获取权限列表
//        if (!"1".equals(userId)) {
//            // 查询当前用户在该项目上的授权
//            DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
//            Authority authority = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId).and("projectId").is(projectId)), Authority.class);
//            if (authority == null)
//                throw new IllegalArgumentException(userId + "用户没有" + projectId + "项目权限");
//
//            //获取当前用户的角色id列表
//            List<Integer> roleIds = new ArrayList<>();
//            authority.getRoles().forEach(role -> {
//                roleIds.add(Integer.valueOf(role.getRoleId()));
//            });
//
//            //根据roleId获取权限列表
//            permissions = authServerClient.getPermissionsByRoleIds(DataSourceHolder.DEFAULT_USER, projectId, roleIds).getData();
//            for (Permission permission : permissions) {
//                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getUrl()));
//            }
//
//            //切换回分库
//            DataSourceHolder.setDB(projectId);
//        }

        grantedAuthorities.add(new SimpleGrantedAuthority("/project-management/projectList"));

        return new WebUserDetails(userId, permissions, true, grantedAuthorities);
    }

}
