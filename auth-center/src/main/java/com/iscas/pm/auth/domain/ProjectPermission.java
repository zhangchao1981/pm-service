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

    private String projectId;

    private String permissionId;
}
