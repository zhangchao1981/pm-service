package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;


/**
 * @TableName pm_project_user_role
 * @Author：李昶
 * @Date： 2022/7/21
 * @Description： 表对应实体类
 */

@TableName(value ="pm_project_user_role")
@Data
public class ProjectUserRole implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 项目id
     */
    private Integer projectId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}