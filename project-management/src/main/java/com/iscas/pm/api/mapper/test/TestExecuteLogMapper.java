package com.iscas.pm.api.mapper.test;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.test.TestExecuteLog;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【test_execute_log(测试用例执行记录表)】的数据库操作Mapper
* @createDate 2022-08-10 10:25:50
* @Entity test.TestExecuteLog
*/
@Mapper
public interface TestExecuteLogMapper extends BaseMapper<TestExecuteLog> {

}



