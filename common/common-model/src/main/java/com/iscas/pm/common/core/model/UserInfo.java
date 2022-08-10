package com.iscas.pm.common.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
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
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -8242940190960961504L;

    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "人员姓名")
    @NotBlank(message = "人员姓名不能为空")
    private String employeeName;

    @ApiModelProperty("token令牌")
    private String accessToken;

    @ApiModelProperty("系统角色对应的权限列表")
    private List<String> systemPermissions;

    @ApiModelProperty("项目角色对应的权限列表")
    private Map<String,List<String> >projectPermissions;

    @ApiModelProperty("当前项目id,前端忽略")
    private String currentProjectId;

}
