package com.iscas.pm.common.core.model;

import lombok.Data;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： token校验返回值对象
 */
@Data
public class User {
    //用户id
    private String userId;
    //用户名
    private String userName;
    //人员姓名
    private String personalName;
    //当前所在项目
    private String currentProjectId;
}
