package com.iscas.pm.api.model.doc;

import com.iscas.pm.api.model.env.EnvHardware;
import com.iscas.pm.api.model.env.EnvSoftware;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import lombok.Data;

import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目详细信息，生成文档时使用
 */
@Data
public class ProjectDetailInfo {
    private Project basicInfo;

    private List<PlanTask> plan;

    private List<EnvSoftware> softwareEnvs;

    private List<EnvHardware> hardwareEnvs;


}
