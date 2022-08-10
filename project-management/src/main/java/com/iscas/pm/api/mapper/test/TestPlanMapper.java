package com.iscas.pm.api.mapper.test;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.test.TestPlan;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【test_plan(测试计划表)】的数据库操作Mapper
* @createDate 2022-08-10 10:22:58
* @Entity test.TestPlan
*/
@Mapper
public interface TestPlanMapper extends BaseMapper<TestPlan> {

}




