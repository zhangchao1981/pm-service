package com.iscas.pm.api.model.doc.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iscas.pm.api.model.dev.DevModular;
import com.iscas.pm.api.model.dev.DevRequirement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/10/12.
 */
@ApiModel(value = "项目模块", description = "项目模块表，对应project_demo库dev_modular表")
@TableName(value = "dev_modular")
@Data
public class DocModular {
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
    @ApiModelProperty(value = "子需求(输出文档用)")
    private List<DocRequirement> docRequirements;


    public DocModular(DevModular devModular) {
        this.id = devModular.getId();
        this.name = devModular.getName();
        this.parentId = devModular.getParentId();
        this.modulars = devModular.getModulars();
        List<DocRequirement> docRequirements = new ArrayList<>();
        devModular.getDevRequirements().forEach(devRequirement -> docRequirements.add(new DocRequirement(devRequirement)));
        this.docRequirements = docRequirements;
    }
}
