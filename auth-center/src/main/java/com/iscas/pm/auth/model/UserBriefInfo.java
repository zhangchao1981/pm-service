package com.iscas.pm.auth.model;

import com.iscas.pm.common.core.model.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author by  lichang
 * @date 2022/9/9.
 */
@Data
@ApiModel("添加用户时候选名单上显示的信息")
@Accessors(chain = true)
public class UserBriefInfo {

    private  String employName;

    private  String departmentName;  //换成  部门名

    private  Integer userId;

    private  String  userName;

}
