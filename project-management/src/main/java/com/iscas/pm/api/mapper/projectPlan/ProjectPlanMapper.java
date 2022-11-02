package com.iscas.pm.api.mapper.projectPlan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【plan】的数据库操作Mapper
* @createDate 2022-07-28 17:13:09
* @Entity /api.model/project.Plan
*/
@Mapper
public interface ProjectPlanMapper extends BaseMapper<PlanTask> {
}




