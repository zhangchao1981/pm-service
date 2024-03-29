package com.iscas.pm.common.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value ="auth_user")
@ApiModel(value = "人员信息", description = "用户信息、人员信息都存储在一张表中了")
@Accessors(chain = true)
public class User implements Serializable {
    @ApiModelProperty(value = "用户id，添加时无需传参，修改传参")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名,根据人员姓名自动生成，前端无需传值，默认为姓名全拼，重复时后面加数字区分")
    private String userName;

    @ApiModelProperty(value = "密码，系统自动生成初始密码，前端无需传值")
    private String password;

    @ApiModelProperty(value = "人员姓名，不允许修改",required = true)
    @NotBlank(message = "人员姓名不能为空")
    @Size(max = 32, message = "人员姓名长度不能大于32")
    private String employeeName;

    @ApiModelProperty(value = "人员所在部门id",required = true)
    private Integer departmentId;

    @ApiModelProperty(value = "手机号")
    @Size(max = 20, message = "手机号长度不能大于32")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Size(max = 32, message = "邮箱长度不能大于32")
    private String email;

    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "账号状态,添加修改时无需传参")
    private UserStatusEnum status;
}