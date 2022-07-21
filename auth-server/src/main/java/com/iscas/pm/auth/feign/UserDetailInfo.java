package com.iscas.pm.auth.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

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
    private boolean userEnabled;

    @ApiModelProperty("权限列表字符串")
    private String permissions;

    @ApiModelProperty("权限例表")
    private Collection<GrantedAuthority> authorities;

    public UserDetailInfo(Integer userId, String username, String password, String employeeName, boolean userEnabled, String permissions) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.employeeName = employeeName;
        this.userEnabled = userEnabled;
        this.permissions = permissions;
    }

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
        return this.userEnabled;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


}
