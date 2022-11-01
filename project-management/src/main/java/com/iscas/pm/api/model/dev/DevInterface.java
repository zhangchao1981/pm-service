package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;


@TableName(value ="dev_interface",autoResultMap = true)
@Data
@Accessors(chain = true)
@ApiModel(value = "关联接口表", description = "关联接口信息，对应项目库dev_interface表")
public class DevInterface implements Serializable {

    @ApiModelProperty(value = "接口编号，系统自动生成，添加接口无需传参")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "接口名称",required = true)
    @NotBlank(message = "接口名称不能为空")
    @Size(max = 25,message = "接口名称长度不能大于25")
    private String name;


    @ApiModelProperty(value = "接口类型",required = true,notes = "内部或外部接口")
    @NotNull(message = "接口类型不能为空")
    private InterfaceTypeEnum type;

    @ApiModelProperty(value = "接口种类",notes = "API接口",required = true)
    @Size(max = 25,message = "接口种类长度不能大于25")
    private String category;


    @ApiModelProperty(value = "接口描述",required = true)
    @Size(max = 255,message = "接口描述长度不能大于255")
    private String description;


    @ApiModelProperty(value = "维护人，即新建接口的用户")
    @Size(max = 12,message = "维护人长度不能大于12")
    private String maintainer;


    @ApiModelProperty(value = "发送方",required = true)
    @NotBlank(message = "发送方不能为空")
    @Size(max = 25,message = "发送方长度不能大于25")
    private String sender;


    @ApiModelProperty(value = "接收方",required = true)
    @NotBlank(message = "接收方不能为空")
    @Size(max = 25,message = "接收方长度不能大于25")
    private String acceptor;

    @ApiModelProperty(value = "优先级",required = true)
    @NotNull(message = "优先级不能为空")
    private PriorityEnum priority;


    @ApiModelProperty(value = "开发需求id",required = true)
    @NotNull(message = "开发需求id不能为空")
    private Integer requireId;

//
//    @ApiModelProperty(value = "接口数据元素说明",required = true)
//    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)

    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty(value = "接口数据元素说明", required = true)
    @Valid
    private List<InterfaceDataDescription> dataDescription;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}