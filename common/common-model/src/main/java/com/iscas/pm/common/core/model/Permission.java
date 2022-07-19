package com.iscas.pm.common.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 权限资源实体
 *
 * @author 郑智聪
 * @since 2019-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源Id,后台生成",position = 1)
    private Integer permissionId;

    @ApiModelProperty(value = "资源名称",required = true,position = 4)
    private String name;

    @ApiModelProperty(value = "资源描述",position = 6)
    private String description;

    @ApiModelProperty(value = "父节点id",required = true,position = 3)
    private Integer pid;

    @ApiModelProperty(value = "资源URL",required = true,position = 2)
    private String url;

    @ApiModelProperty(value = "gvp地址，菜单类资源有该属性",required = true,position = 2)
    private String path;

    @ApiModelProperty(value = "页面路由，模块类资源有该属性",required = true,position = 2)
    private String route;

    @ApiModelProperty(value = "资源创建时间，后台生成",position = 5)
    private String createTime;

    @ApiModelProperty(value = "资源logo，模块类资源有该属性",position = 7)
    private String icon;

    @ApiModelProperty(value = "资源类型（其中0,1,2为权限树上的节点类型,0:接口资源 1:模块 2:资源分类；4,5,0为项目定制树上的节点类型，4:项目；5:模块，0:菜单），具体含义；",position = 8)
    private Integer type;

    @ApiModelProperty(value = "资源是否允许编辑（0不允许编辑，1允许编辑）",position = 9)
    private String component;

    @ApiModelProperty(value = "资源所属模块",position = 10)
    private String module;

    @ApiModelProperty(value = "资源所属项目",position = 10)
    private String projectId;

    @ApiModelProperty(value = "路由模板，菜单类资源有该属性",position = 10)
    private String routeComponent;

    @ApiModelProperty(value = "路由模板编号，菜单类资源有该属性",position = 10)
    private String routeComponentId;

    @ApiModelProperty(value = "用于区分前后端资源，项目定制下所有节点都是1，权限树资源都是0")
    private Integer useRoute;

    @ApiModelProperty(value = "前端给菜单排序使用的字段")
    private Integer sort;
}
