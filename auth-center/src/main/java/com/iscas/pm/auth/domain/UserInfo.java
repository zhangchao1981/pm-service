package com.iscas.pm.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author： zhangchao
 * @Date： 2022/7/19
 * @Description： 登陆后返回的对象
 */
@Data
@ApiModel("用户登录返回信息")
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfo extends User implements Serializable {

    private static final long serialVersionUID = -8242940190960961504L;

    @ApiModelProperty("token令牌")
    private String accessToken;

    @ApiModelProperty("系统角色对应的权限列表")
    private List<String> systemPermissions;

    @ApiModelProperty("项目角色对应的权限列表")
    private Map<String,List<String> >projectPermissions;

}
