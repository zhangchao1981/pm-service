package com.iscas.pm.api.model.doc.param;

import com.iscas.pm.api.model.doc.DateBaseType;
import com.iscas.pm.api.model.doc.ReferenceDoc;
import com.iscas.pm.api.model.doc.ReviseRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/19.
 */
@Data
public class CreateDocumentParam {

    //软件开发计划需要的参数

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


    @NotBlank(message = "当前项目id不能为空")
    @ApiModelProperty(value = "当前项目id", required = true)
    private String currentProjectId;

//    @ApiModelProperty(value = "选中的引用文档", required = true)
//    private List<ReferenceDoc> referenceDocList;
//
//    @ApiModelProperty(value = "选中的文档修订记录", required = true)
//    private List<ReviseRecord> reviseRecordList;

    @ApiModelProperty(value = "数据库名",notes = "选择数据库文档，即模板id=2时传入")
    private  String  dbName;

    @ApiModelProperty(value = "用户名",notes = "选择数据库文档，即模板id=2时传入")
    private  String  userName;

    @ApiModelProperty(value = "密码",notes = "选择数据库文档，即模板id=2时传入")
    private  String  password;

    @ApiModelProperty(value = "数据库地址",notes = "选择数据库文档，即模板id=2时传入")
    private  String  dbPath;

    @ApiModelProperty(value = "数据库端口号",notes = "选择数据库文档，即模板id=2时传入")
    private  String  port;

    @ApiModelProperty(value = "数据库驱动名",notes = "选择数据库文档，即模板id=2时传入")
    private  String   driverClassName;

    @ApiModelProperty(value = "数据库类型",notes = "选择数据库文档，即模板id=2时传入")
    private DateBaseType dbType;




}
