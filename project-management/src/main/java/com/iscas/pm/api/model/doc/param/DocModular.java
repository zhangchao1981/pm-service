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
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/10/12.
 */
@Data
@Accessors(chain = true)
public class DocModular {
    @ApiModelProperty(value = "id", notes = "不需前端传入")
    private Integer id;

    @ApiModelProperty(value = "模块名称", required = true)
    private String name;

    @ApiModelProperty(value = "项目标识")
    private String projectId;

    @ApiModelProperty(value = "父节点id", required = true)
    private Integer parentId;

    @ApiModelProperty(value = "子模板")
    private List<DevModular> modulars;

    @ApiModelProperty(value = "子需求(输出文档用)")
    private List<DocRequirement> docRequirements;

    @ApiModelProperty(value = "模块描述")
    private String description;


    public DocModular(DevModular devModular) {
        this.id = devModular.getId();
        this.name = devModular.getName();
        this.description = devModular.getDescription();
        this.parentId = devModular.getParentId();
        this.modulars = devModular.getModulars();
        if (devModular.getDevRequirements() != null) {
            List<DocRequirement> docRequirements = new ArrayList<>();
            devModular.getDevRequirements().forEach(devRequirement -> docRequirements.add(new DocRequirement(devRequirement)));
            this.docRequirements = docRequirements;
        }
    }
}
