package com.iscas.pm.api.model.doc;

import com.iscas.pm.api.model.project.Project;
import lombok.Data;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目详细信息，生成文档时使用
 */
@Data
public class ProjectDetailInfo {

    private Project basicInfo;
}
