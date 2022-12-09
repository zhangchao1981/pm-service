package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "项目模块", description = "项目模块表，对应project_demo库dev_modular表")
@TableName(value = "dev_modular")
@Accessors(chain = true)
@Data
public class DevModular implements Serializable {

    public DevModular() {
        type = String.valueOf(DevTypeEnum.Modular);
    }

    @ApiModelProperty(value = "id", notes = "不需前端传入")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "模块名称", required = true)
    @Size(max = 25, message = "模块名称长度不能大于25")
    private String name;

    @NotNull(message = "父节点id不能为空")
    @ApiModelProperty(value = "父节点id", required = true)
    private Integer parentId;

    @TableField(exist = false)
    @ApiModelProperty(value = "子模板")
    private List<DevModular> modulars;

    @TableField(exist = false)
    @ApiModelProperty(value = "子需求")
    private List<DevRequirement> devRequirements;

    @TableField(exist = false)
    @ApiModelProperty(value = "返回值类型")
    @Size(max = 25, message = "返回值类型长度不能大于25")
    private String type;

    @ApiModelProperty(value = "模块描述", required = true)
    @Size(max = 100, message = "模块描述长度不能大于100")
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}