package com.iscas.pm.auth.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @Author： zhangchao
 * @Date： 2022/7/25
 * @Description：
 */
@Data
public class ProjectPermission {

    @TableField("project_id")
    private String projectId;


    @TableField("permission_id")
    private String permissionId;
}
