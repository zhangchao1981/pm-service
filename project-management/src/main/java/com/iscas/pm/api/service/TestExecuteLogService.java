package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.test.param.EditBatchExecuteLogParam;
import com.iscas.pm.api.model.test.TestExecuteLog;
import com.iscas.pm.api.model.test.param.TestExecuteLogParam;

import java.util.List;

/**
* @author 66410
* @description 针对表【test_execute_log(测试用例执行记录表)】的数据库操作Service
* @createDate 2022-08-10 10:25:50
*/
public interface TestExecuteLogService extends IService<TestExecuteLog> {

    List<TestExecuteLog> addTestExecuteLog(List<Integer> idList, Integer planId);

    Boolean updateBatchTestExecute(EditBatchExecuteLogParam editBatchExecuteLogParam);

    IPage<TestExecuteLog> testExecuteLogPage(TestExecuteLogParam testExecuteLogParam);


    List<TestExecuteLog> testExecuteLogList(Integer modularId, Integer executeId);
}
