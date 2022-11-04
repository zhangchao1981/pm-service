package com.iscas.pm.api.model.doc.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.iscas.pm.api.model.env.EnvSoftware;
import com.iscas.pm.api.model.project.SecretLevelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author by  lichang
 * @date 2022/11/4.
 */
@Data
public class DocEnvSoftware {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分类",required = true)
    @NotBlank(message = "分类不能为空")
    @Size(max = 11,message = "分类不能大于11")
    private String category;

    @ApiModelProperty(value = "软件名称",required = true)
    @NotBlank(message = "软件名称不能为空")
    @Size(max = 32,message = "软件名称不能大于32")
    private String softName;

    @ApiModelProperty(value = "版本标识")
    @Size(max = 32,message = "版本标识不能大于32")
    private String versionId;

    @ApiModelProperty(value = "来源",required = true)
    @NotBlank(message = "来源不能为空")
    @Size(max = 32,message = "来源不能大于32")
    private String source;

    @ApiModelProperty(value = "用途",required = true)
    @NotBlank(message = "用途不能为空")
    @Size(max = 255,message = "用途长度不能大于255")
    private String application;

    @ApiModelProperty(value = "介质",required = true)
    @NotBlank(message = "介质不能为空")
    @Size(max = 11,message = "介质长度不能大于11")
    private String medium;

    @ApiModelProperty(value = "安全保密",required = true)
    @NotNull(message = "安全保密不能为空")
    private String security;

    public DocEnvSoftware(EnvSoftware software) {
        this.id = software.getId();
        this.category = software.getCategory();
        this.softName = software.getSoftName();
        this.versionId = software.getVersionId();
        this.source = software.getSource();
        this.application = software.getApplication();
        this.medium = software.getMedium();
        this.security = software.getSecurity()!=null?software.getSecurity().getValue():null;
    }
}
