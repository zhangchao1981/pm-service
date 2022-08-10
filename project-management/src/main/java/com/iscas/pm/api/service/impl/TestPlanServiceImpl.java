package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.test.TestPlanMapper;
import com.iscas.pm.api.model.test.TestPlan;
import com.iscas.pm.api.service.TestPlanService;
import org.springframework.stereotype.Service;


/**
* @author 66410
* @description 针对表【test_plan(测试计划表)】的数据库操作Service实现
* @createDate 2022-08-10 10:22:58
*/
@Service
public class TestPlanServiceImpl extends ServiceImpl<TestPlanMapper, TestPlan>
    implements TestPlanService {

}




