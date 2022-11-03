package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.test.TestBug;
import com.iscas.pm.api.model.test.TestExecuteLog;
import com.iscas.pm.api.model.test.param.SolveBugParam;
import com.iscas.pm.api.model.test.param.TestBugQueryParam;
import com.iscas.pm.api.model.test.param.TransferBugParam;

import java.util.List;

/**
* @author 66410
* @description 针对表【test_bug(测试缺陷表)】的数据库操作Service
* @createDate 2022-08-10 10:25:53
*/
public interface TestBugService extends IService<TestBug> {

    void addBug(TestBug testBug);

    IPage<TestBug> bugList(TestBugQueryParam testBug);

    void editBug(TestBug testBug);

    void startProcessBug(Integer bugId);

    void transferBug(TransferBugParam param);

    void dispatchBug(TransferBugParam param);

    void solveBug(SolveBugParam param);

    void delayedSolveBug(Integer bugId, String explain);

    void reopenBug(Integer bugId, String explain);

    void closeBug(SolveBugParam param);

    List<TestExecuteLog> countTestBugByExecute(List<Integer> executeIdList);
}
