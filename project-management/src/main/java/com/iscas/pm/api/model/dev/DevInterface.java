package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@TableName(value ="dev_interface")
@Data
@Accessors(chain = true)
@ApiModel(value = "关联接口表", description = "关联接口信息，对应项目库dev_interface表")
public class DevInterface implements Serializable {

    @ApiModelProperty(value = "接口编号，系统自动生成，添加接口无需传参")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "接口名称",required = true)
    @NotBlank(message = "接口名称不能为空")
    private String name;


    @ApiModelProperty(value = "接口类型",required = true)
    @NotNull(message = "接口类型不能为空")
    private InterfaceTypeEnum type;


    @ApiModelProperty(value = "维护人，即新建接口的用户")
    private String maintainer;


    @ApiModelProperty(value = "发送方",required = true)
    @NotBlank(message = "发送方不能为空")
    private String sender;


    @ApiModelProperty(value = "接收方",required = true)
    @NotBlank(message = "接收方不能为空")
    private String acceptor;

    @ApiModelProperty(value = "优先级",required = true)
    @NotNull(message = "优先级不能为空")
    private PriorityEnum priority;


    @ApiModelProperty(value = "开发需求id",required = true)
    @NotNull(message = "开发需求id不能为空")
    private Integer requireId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}