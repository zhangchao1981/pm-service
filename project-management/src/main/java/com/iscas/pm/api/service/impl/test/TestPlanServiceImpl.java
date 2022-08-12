package com.iscas.pm.api.service.impl.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.test.TestExecuteLogMapper;
import com.iscas.pm.api.mapper.test.TestPlanMapper;
import com.iscas.pm.api.model.test.TestExecuteLog;
import com.iscas.pm.api.model.test.TestPlan;
import com.iscas.pm.api.model.test.TestPlanStatisticData;
import com.iscas.pm.api.service.TestPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author 66410
 * @description 针对表【test_plan(测试计划表)】的数据库操作Service实现
 * @createDate 2022-08-10 10:22:58
 */
@Service
public class TestPlanServiceImpl extends ServiceImpl<TestPlanMapper, TestPlan>
        implements TestPlanService {
    @Autowired
    TestPlanMapper testPlanMapper;
    @Autowired
    TestExecuteLogMapper testExecuteLogMapper;

    @Override
    public TestPlanStatisticData statisticData(Integer testPlanId) {
        TestPlanStatisticData data = new TestPlanStatisticData();
        Integer totalUseCase = testExecuteLogMapper.selectCount(new QueryWrapper<TestExecuteLog>().select("DISTINCT use_case_id").eq("plan_id", testPlanId));
        if (totalUseCase<1){
            return  data;
        }
        //已测用例：   执行记录中  用测试用例id分组，计算组数(总数)
        Integer testedUseCase= testExecuteLogMapper.selectCount(new QueryWrapper<TestExecuteLog>().select("DISTINCT use_case_id").eq("plan_id", testPlanId).isNotNull("pass"));
        //待测，判断条件用布尔还是int
        Integer passUseCase = testExecuteLogMapper.selectCount(new QueryWrapper<TestExecuteLog>().select("DISTINCT use_case_id").eq("plan_id", testPlanId).eq("pass", true));
        data.setTestedCase(testedUseCase.toString()+"/"+totalUseCase.toString());
        data.setPassRate((double) passUseCase/totalUseCase);
        //缺陷情况待完成
//        data.setBugStatistic()
        //执行进度待完成
        data.setExecuteProgress( (double) testedUseCase/totalUseCase);
        return data;
    }
}




