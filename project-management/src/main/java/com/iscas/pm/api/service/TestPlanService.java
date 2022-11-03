package com.iscas.pm.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.test.TestPlan;
import com.iscas.pm.api.model.test.TestPlanStatisticData;

import java.util.List;

/**
* @author 66410
* @description 针对表【test_plan(测试计划表)】的数据库操作Service
* @createDate 2022-08-10 10:22:58
*/
public interface TestPlanService extends IService<TestPlan> {

    TestPlanStatisticData statisticData(Integer testPlanId);

    List<TestPlan> updateBugStatistic(IPage<TestPlan> planIdList);
}
