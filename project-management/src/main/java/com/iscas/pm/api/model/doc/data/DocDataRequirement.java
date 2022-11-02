package com.iscas.pm.api.model.doc.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.iscas.pm.api.model.dev.DataRequirement;
import com.iscas.pm.api.model.dev.DataRequirementInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;

/**
 * @author by  lichang
 * @date 2022/11/1.
 */
@Data
public class DocDataRequirement {
    @ApiModelProperty(value = "id", notes = "不需前端传入")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty(value = "数据需求集合", required = true)
    @Valid
    private List<DocDataRequirementInfo> dataInfo;

    @ApiModelProperty(value = "数据需求名称",required = true)
    @NotBlank(message = "数据需求名称不能为空")
    @Size(max = 50,message = "数据需求名称长度不能大于50")
    private String requirementName;

    @ApiModelProperty(value = "开发需求id",required = true)
    @NotNull(message = "开发需求id不能为空")
    private Integer requireId;

    public DocDataRequirement(DataRequirement dataRequirement) {
        this.id =dataRequirement.getId();
        this.dataInfo=new ArrayList<>();
        if (dataRequirement.getDataInfo()!=null){
            List<DataRequirementInfo> info = JSONArray.parseArray(dataRequirement.getDataInfo().toString()).toJavaList(DataRequirementInfo.class);
            for (int i = 0; i < info.size(); i++) {
                this.dataInfo.add(new DocDataRequirementInfo(i+1, info.get(i)));
            }
        }

        this.requirementName = dataRequirement.getRequirementName();
        this.requireId = dataRequirement.getRequireId();
    }
}
