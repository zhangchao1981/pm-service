package com.iscas.pm.api.model.doc.data;

import com.iscas.pm.api.model.projectPlan.PlanTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * @Author： zhangchao
 * @Date： 2022/9/23
 * @Description：
 */
@Data
public class DocPlanTask {
//    @ApiModelProperty(value = "任务id")
//    private Integer id;

    @ApiModelProperty(value = "WBS编号,前端无需传参，后端自动生成")
    private String wbs;

    @ApiModelProperty(value = "关联文档路径")
    private String docPath;

    @ApiModelProperty(value = "任务名称",required = true)
    private String name;

    @ApiModelProperty(value = "责任人，人员姓名，多个人用逗号隔开")
    private String worker;

//    @ApiModelProperty(value = "人数,前端无需传参，后端自动根据责任人计算")
//    private Integer personCount;

    @ApiModelProperty(value = "工期(天),前端无需传参，后端自动根据开始日期和结束日期计算")
    private Integer workingDays;

    @ApiModelProperty(value = "开始日期")
    private String startDate;

    @ApiModelProperty(value = "截止日期")
    private String endDate;

    @ApiModelProperty(value = "发生工时")
    private Double happenedHour;

    @ApiModelProperty(value = "任务状态，前端无需传参")
    private String status;

//    @ApiModelProperty(value = "项目完成进度，前端无需传参")
//    private Integer progressRate;
////
//    @ApiModelProperty(value = "实际开始日期")
//    private Date actualStartDate;
//
//    @ApiModelProperty(value = "实际结束日期")
//    private Date actualEndDate;

    public DocPlanTask(PlanTask planTask) {
        this.wbs = planTask.getWbs();
        this.docPath = planTask.getDocPath();
        this.name = planTask.getName();
        if( planTask.getWorkerList()!=null){
            planTask.getWorkerList().forEach(workerE->this.worker+=','+workerE);
            this.workingDays = planTask.getWorkingDays();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.startDate = planTask.getStartDate()!=null?formatter.format(planTask.getStartDate()):null;
        this.endDate = planTask.getEndDate()!=null?formatter.format(planTask.getEndDate()):null;
        this.happenedHour = planTask.getHappenedHour();
        this.status = planTask.getStatus().getValue();
    }
}
