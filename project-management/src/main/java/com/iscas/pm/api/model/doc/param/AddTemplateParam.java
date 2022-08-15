package com.iscas.pm.api.model.doc.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author by  lichang
 * @date 2022/8/12.
 * @Describe 添加文档模板条件封装
 */
@Data
public class AddTemplateParam {

    @NotBlank(message ="维护人不能为空" )
    @ApiModelProperty(value = "维护人",required = true)
    private String maintainer;

    @NotBlank(message = "模板名称不能为空")
    @ApiModelProperty(value = "模板名称",required = true)
    private String name;

    @ApiModelProperty(value = "模板描述",required = true)
    private String description;

    @NotBlank(message = "模板文件路径不能为空")
    @ApiModelProperty(value = "模板文件路径",required = true)
    private String path;
}
