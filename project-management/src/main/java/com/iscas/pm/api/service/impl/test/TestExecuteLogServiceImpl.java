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
import com.iscas.pm.api.service.TestExecuteLogService;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.isNumeric;

/**
 * @author lichang
 * @description 针对表【test_execute_log(测试用例执行记录表)】的数据库操作Service实现
 * @createDate 2022-08-10 10:25:50
 */
@Service
public class TestExecuteLogServiceImpl extends ServiceImpl<TestExecuteLogMapper, TestExecuteLog> implements TestExecuteLogService {
    @Autowired
    TestUseCaseMapper testUseCaseMapper;
    @Autowired
    TestExecuteLogMapper testExecuteLogMapper;
    @Autowired
    TestPlanMapper testPlanMapper;

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

        //查询真正要导入的测试用例
        List<TestUseCase> useCaseList = testUseCaseMapper.selectBatchIds(caseIdList);

        if (useCaseList.size() < 1) {
            throw new IllegalArgumentException("要导入的用例已经全部存在，或用例已经被删除");
        }

        List<TestExecuteLog> executeLogList = useCaseList.stream().map(useCase -> new TestExecuteLog(useCase, planId)).collect(Collectors.toList());
        super.saveBatch(executeLogList);

        return executeLogList;
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
    public IPage<TestExecuteLog> testExecuteLogList(TestExecuteLogParam testExecuteLogParam) {
        String logIdOrTitle = testExecuteLogParam.getLogIdOrTitle();
        Page<TestExecuteLog> page = new Page<>(testExecuteLogParam.getPageNum(), testExecuteLogParam.getPageSize());
        QueryWrapper<TestExecuteLog> executeLogQueryWrapper = new QueryWrapper<TestExecuteLog>()
                .eq(testExecuteLogParam.getPlanId() != null, "plan_id", testExecuteLogParam.getPlanId())
                .eq(testExecuteLogParam.getModularId() != null, "modular_id", testExecuteLogParam.getModularId())
                .and(logIdOrTitle != null, a -> a.eq(isNumeric(logIdOrTitle), "use_case_id", StringUtils.isEmpty(logIdOrTitle) || !isNumeric(logIdOrTitle) ? null : Integer.valueOf(logIdOrTitle))
                        .or().like("title", logIdOrTitle));
        return testExecuteLogMapper.selectPage(page, executeLogQueryWrapper);
    }
}