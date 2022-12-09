package com.iscas.pm.api.model.doc.data;

import com.iscas.pm.api.model.doc.ReviseRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;

@Accessors(chain = true)
@ApiModel(value = "文档修订记录", description = "文档修订记录基本信息，对应project_demo库doc_revise_record表")
@Data
public class DocReviseRecord {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "外键：模板id",required = true)
    private Integer templateId;

    @ApiModelProperty(value = "修订记录版本编号",required = true)
    private String version;

    @ApiModelProperty(value = "简要说明(变更内容和范围)")
    private String notes;

    @ApiModelProperty(value = "修订日期",required = true)
    private String date;

    @NotBlank(message = "修改人不能为空")
    private String mender;

    @ApiModelProperty(value = "批准人")
    private String approver;

    public DocReviseRecord(ReviseRecord  reviseRecord) {
        this.id = reviseRecord.getId();
        this.templateId = reviseRecord.getTemplateId();
        this.version = reviseRecord.getVersion();
        this.notes = reviseRecord.getNotes();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.date = formatter.format(reviseRecord.getDate());
        this.mender = reviseRecord.getMender();
        this.approver = reviseRecord.getApprover();
    }
}