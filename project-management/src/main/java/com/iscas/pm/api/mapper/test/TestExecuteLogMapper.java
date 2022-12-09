package com.iscas.pm.api.mapper.test;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iscas.pm.api.model.test.TestExecuteLog;
import com.iscas.pm.api.model.test.param.TestExecuteLogParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 66410
* @description 针对表【test_execute_log(测试用例执行记录表)】的数据库操作Mapper
* @createDate 2022-08-10 10:25:50
* @Entity test.TestExecuteLog
*/
@Mapper
public interface TestExecuteLogMapper extends BaseMapper<TestExecuteLog> {
    IPage<TestExecuteLog> testExecuteLogPage(Page<TestExecuteLog> page, @Param("testExecuteLogParam") TestExecuteLogParam testExecuteLogParam);

    List<TestExecuteLog> testExecuteLogList(@Param("modularId") Integer modularId, Integer executeId, Integer testPlanId);
}




