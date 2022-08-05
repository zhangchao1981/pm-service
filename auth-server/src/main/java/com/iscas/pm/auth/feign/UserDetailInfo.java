package com.iscas.pm.auth.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @Author： zhangchao
 * @Date： 2022/7/19
 * @Description： 用户详细信息，实现UserDetails接口，即用于security鉴权，也是前端获取用户详细信息的实体对象.
 */
@Data
@ApiModel("用户详细信息")
@NoArgsConstructor
public class UserDetailInfo implements UserDetails, Serializable {

    private static final long serialVersionUID = -8242940190960961504L;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("人员姓名")
    private String employeeName;

    @ApiModelProperty("账号是否可用")
    private boolean enabled;

    @ApiModelProperty("系统角色对应的权限列表")
    private List<String> systemPermissions;

    @ApiModelProperty("项目角色对应的权限列表,key为项目id，value为项目下的权限列表")
    private Map<String, List<String>> projectPermissions;

    @ApiModelProperty("权限例表")
    private Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


}
