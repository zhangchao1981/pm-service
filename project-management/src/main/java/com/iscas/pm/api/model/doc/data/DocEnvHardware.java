package com.iscas.pm.api.model.doc.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.iscas.pm.api.model.env.EnvHardware;
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
public class DocEnvHardware {
    @ApiModelProperty(value = "硬件表id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "硬件名称",required = true)
    @NotBlank(message = "硬件名称不能为空")
    @Size(max = 255,message = "硬件名称长度不能大于255")
    private String hardwareName;

    @ApiModelProperty(value = "最低配置",required = true)
    @NotBlank(message = "最低配置不能为空")
    @Size(max = 255,message = "最低配置长度不能大于255")
    private String minConfig;

    @ApiModelProperty(value = "数量",required = true)
    @NotNull(message = "数量不能为空")
    private Integer amount;

    @ApiModelProperty(value = "用途",required = true)
    @NotBlank(message = "用途不能为空")
    @Size(max = 100,message = "用途长度不能大于100")
    private String application;

    @ApiModelProperty(value = "使用时间",notes = "年月日组成的时间区间")
    @Size(max = 100,message = "使用时间长度不能大于100")
    private String usePeriod;

    @ApiModelProperty(value = "保密范围",required = true)
    @NotNull(message = "安全保密不能为空")
    private String security;

    public DocEnvHardware(EnvHardware hardware) {
        this.id = hardware.getId();
        this.hardwareName = hardware.getHardwareName();
        this.minConfig = hardware.getMinConfig();
        this.amount = hardware.getAmount();
        this.application = hardware.getApplication();
        this.usePeriod = hardware.getUsePeriod();
        this.security = hardware.getSecurity()!=null?hardware.getSecurity().getValue():null;
    }
}
