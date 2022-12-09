package com.iscas.pm.api.model.doc.param;

import com.iscas.pm.api.model.doc.DataBaseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author by  lichang
 * @date 2022/8/19.
 */
@Data
public class CreateDocumentParam {

    @ApiModelProperty(value = "目录id", required = true)
    private Integer directoryId;

    @ApiModelProperty(value = "生成文档名", required = true)
    private String documentName;

    @ApiModelProperty(value = "本文档版本号", required = true)
    private String version;

    @ApiModelProperty(value = "模板id", required = true)
    private Integer templateId;

    @ApiModelProperty(value = "模板存储路径", required = true)
    private String templatePath;

    @ApiModelProperty(value = "当前项目id，前端无需传参")
    private String currentProjectId;

    @ApiModelProperty(value = "数据库名",notes = "选择数据库设计类型模板时，输入该参数")
    private  String  dbName;

    @ApiModelProperty(value = "用户名",notes = "选择数据库设计类型模板时，输入该参数")
    private  String  userName;

    @ApiModelProperty(value = "密码",notes = "选择数据库设计类型模板时，输入该参数")
    private  String  password;

    @ApiModelProperty(value = "数据库地址",notes = "选择数据库设计类型模板时，输入该参数")
    private  String  dbPath;

    @ApiModelProperty(value = "数据库端口号",notes = "选择数据库设计类型模板时，输入该参数")
    private  String  port;

    @ApiModelProperty(value = "数据库驱动名",notes = "选择数据库设计类型模板时，输入该参数")
    private  String   driverClassName;

    @ApiModelProperty(value = "数据库类型",notes = "选择数据库设计类型模板时，输入该参数")
    private DataBaseTypeEnum dbType;

    @ApiModelProperty(value = "测试计划id",notes = "选择测试相关模板时（类型为：UTP/UTD/UTR/ITP/ITD/ITR/CTP/CTD/CTR），输入该参数")
    private Integer testPlanId;

}
