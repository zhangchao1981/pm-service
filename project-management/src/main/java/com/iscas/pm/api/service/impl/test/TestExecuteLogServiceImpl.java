package com.iscas.pm.api.service.impl.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.test.TestExecuteLogMapper;
import com.iscas.pm.api.mapper.test.TestPlanMapper;
import com.iscas.pm.api.mapper.test.TestUseCaseMapper;
import com.iscas.pm.api.model.test.*;
import com.iscas.pm.api.model.test.param.EditBatchExecuteLogParam;
import com.iscas.pm.api.model.test.param.TestExecuteLogParam;
import com.iscas.pm.api.service.TestBugService;
import com.iscas.pm.api.service.TestExecuteLogService;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lichang
 * @description 针对表【test_execute_log(测试用例执行记录表)】的数据库操作Service实现
 * @createDate 2022-08-10 10:25:50
 */
@Service
public class TestExecuteLogServiceImpl extends ServiceImpl<TestExecuteLogMapper, TestExecuteLog> implements TestExecuteLogService {
    @Autowired
    private TestUseCaseMapper testUseCaseMapper;
    @Autowired
    private TestExecuteLogMapper testExecuteLogMapper;
    @Autowired
    private TestPlanMapper testPlanMapper;
    @Autowired
    private TestBugService testBugService;

    @Override
    public List<TestExecuteLog> addTestExecuteLog(List<Integer> idList, Integer planId) {
        if (testPlanMapper.selectById(planId) == null) {
            throw new IllegalArgumentException("planId不存在");
        }

        //查询该计划已经导入的用例
        List<TestExecuteLog> testExecuteLogs = testExecuteLogMapper.selectList(new QueryWrapper<TestExecuteLog>().eq("plan_id", planId));
        List<Integer> existing_ids = testExecuteLogs.stream().map(TestExecuteLog::getUseCaseId).collect(Collectors.toList());

        //去除已经导入的用例
        List<Integer> caseIdList = idList.stream().distinct().collect(Collectors.toList());
        caseIdList.removeAll(existing_ids);

        //没有新的用例要导入
        if (caseIdList.size() < 1) {
            return null;
        }
        //查询真正要导入的测试用例
        List<TestUseCase> useCaseList = testUseCaseMapper.selectBatchIds(caseIdList);

        //输入的用例id不正确
        if (useCaseList.size() < 1) {
            return null;
        }

        List<TestExecuteLog> executeLogList = useCaseList.stream().map(useCase -> new TestExecuteLog(useCase, planId)).collect(Collectors.toList());
        super.saveBatch(executeLogList);

        return executeLogList;
//        45	0	2	张超	错误用户名密码登录系统	THIRD	100001	FUNCTION	[{"stepNumber": 0, "inputExplain": "浏览器输入网址", "expectedResult": "系统显示登录页面"}, {"stepNumber": 1, "inputExplain": "输入错误的用户名密码", "expectedResult": "系统提示用户名密码错误，不进入系统"}]	9	100002			1041
    }

    @Override
    public Boolean updateBatchTestExecute(EditBatchExecuteLogParam editBatchExecuteLogParam) {
        Boolean pass = editBatchExecuteLogParam.getPass();
        String testPerson = editBatchExecuteLogParam.getTestPerson();
        Integer testPersonId = editBatchExecuteLogParam.getTestPersonId();

        if (pass == null && StringUtils.isEmpty(testPerson)) {
            throw new IllegalArgumentException("执行结果和替换测试人员不能同时为空！");
        }

        List<TestExecuteLog> localList = testExecuteLogMapper.selectList(new QueryWrapper<TestExecuteLog>().in("id", editBatchExecuteLogParam.getIdList()));
        if (localList.size() < 1) {
            throw new IllegalArgumentException("要修改的执行记录id不存在");
        }

        //填写执行记录
        if (pass != null) {
            localList.stream().forEach(executeLog -> {
                executeLog.setPass(pass);
                executeLog.setTestPerson(RequestHolder.getUserInfo().getEmployeeName());
                executeLog.setTestPersonId(RequestHolder.getUserInfo().getId());
            });
        }

        //替换测试人员
        else {
            localList.stream().forEach(e -> {
                e.setTestPerson(testPerson);
                e.setTestPersonId(testPersonId);
            });
        }

        super.updateBatchById(localList);
        return true;
    }

    @Override
    public IPage<TestExecuteLog> testExecuteLogPage(TestExecuteLogParam testExecuteLogParam) {
        IPage<TestExecuteLog> testExecuteLogIPage = testExecuteLogMapper.testExecuteLogPage(new Page<>(testExecuteLogParam.getPageNum(), testExecuteLogParam.getPageSize()), testExecuteLogParam);

        //全表刷新时，更新缺陷统计信息
        if (testExecuteLogIPage != null && testExecuteLogIPage.getRecords().size() > 0) {
            List<TestExecuteLog> records = testExecuteLogIPage.getRecords();
            List<Integer> executeIdList = records.stream().map(TestExecuteLog::getId).collect(Collectors.toList());
            HashMap<Integer, Integer> bugAmountList = (HashMap<Integer, Integer>) testBugService.countTestBugByExecute(executeIdList).stream().collect(Collectors.toMap(
                    TestExecuteLog::getId,
                    TestExecuteLog::getBugCount
            ));
            ;
            records.forEach(testExecuteLog -> {
                testExecuteLog.setBugCount(bugAmountList.get(testExecuteLog.getId()));
            });
            testExecuteLogIPage.setRecords(records);
        }
        return testExecuteLogIPage;
    }

    @Override
    public List<TestExecuteLog> testExecuteLogList(Integer modularId, Integer executeId, Integer testPlanId) {
        return testExecuteLogMapper.testExecuteLogList(modularId, executeId,testPlanId);
    }
}