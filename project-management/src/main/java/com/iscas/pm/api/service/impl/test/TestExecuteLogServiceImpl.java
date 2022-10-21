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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.isNumeric;


/**
 * @author 66410
 * @description 针对表【test_execute_log(测试用例执行记录表)】的数据库操作Service实现
 * @createDate 2022-08-10 10:25:50
 */
@Service
public class TestExecuteLogServiceImpl extends ServiceImpl<TestExecuteLogMapper, TestExecuteLog>
        implements TestExecuteLogService {
    @Autowired
    TestUseCaseMapper testUseCaseMapper;
    @Autowired
    TestExecuteLogMapper testExecuteLogMapper;
    @Autowired
    TestPlanMapper testPlanMapper;

    @Override
    public List<TestExecuteLog> addTestExecuteLog(List<Integer> idList, Integer planId) {
        //前端维护一个测试人员表，内容调接口查询user-role表，查到角色是测试员的就加到这个表里，然后导入用例时选中表中的人员name 传过来
        //是否需要校验 planId+测试用例id对应的执行记录重复？

        //校验planId是否有效
        if (testPlanMapper.selectById(planId) == null) {
            throw new IllegalArgumentException("planId不存在");
        }
        //去重，查询useCaseList
        List<Integer> caseIdList = idList.stream().distinct().collect(Collectors.toList());
        List<TestUseCase> useCaseList = testUseCaseMapper.selectBatchIds(idList);


        //若传入的id部分有效  是否需要返回异常？
        if (useCaseList.size() < 1) {
            throw new IllegalArgumentException("未查询到指定测试用例");
        }
        List<TestExecuteLog> executeLogList = useCaseList.stream().map(e -> new TestExecuteLog(e, planId)).collect(Collectors.toList());
        executeLogList.forEach(e -> testExecuteLogMapper.insert(e));
        return executeLogList;
    }

    @Override
    public Boolean updateBatchTestExecute(EditBatchExecuteLogParam editBatchExecuteLogParam) {
        Boolean pass = editBatchExecuteLogParam.getPass();
        String testPerson = editBatchExecuteLogParam.getTestPerson();
        List<TestExecuteLog> localList = testExecuteLogMapper.selectList(new QueryWrapper<TestExecuteLog>().in("id", editBatchExecuteLogParam.getIdList()));
        if (localList.size() < 1) {
            throw new IllegalArgumentException("要修改的执行记录id不存在");
        }
        if (pass == null && StringUtils.isEmpty(testPerson)) {
            throw new IllegalArgumentException("无需要修改为的目标值，请求无效");
        }
        if (pass != null) {
            localList.stream().forEach(e -> e.setPass(pass));
        } else {
            localList.stream().forEach(e -> e.setTestPerson(testPerson));
        }
        //更新对应记录：
        localList.forEach(e -> {
            if (testExecuteLogMapper.updateById(e) < 1) {
                throw new IllegalArgumentException("要修改的执行记录id不存在");
            }
        });
        return true;
    }

    @Override
    public IPage<TestExecuteLog> testExecuteLogList(TestExecuteLogParam testExecuteLogParam) {
        String logIdOrTitle = testExecuteLogParam.getLogIdOrTitle();

        Page<TestExecuteLog> page = new Page<>(testExecuteLogParam.getPageNum(), testExecuteLogParam.getPageSize());
        QueryWrapper<TestExecuteLog> executeLogQueryWrapper = new QueryWrapper<TestExecuteLog>()
                .eq(testExecuteLogParam.getPlanId() != null, "plan_id", testExecuteLogParam.getPlanId())
                .eq(testExecuteLogParam.getModularId()!=null,"modular_id",testExecuteLogParam.getModularId())
                .eq(isNumeric(logIdOrTitle), "use_case_id",StringUtils.isEmpty(logIdOrTitle)?null:Integer.valueOf(logIdOrTitle))
                .or().like(logIdOrTitle != null, "title", logIdOrTitle);
        return testExecuteLogMapper.selectPage(page, executeLogQueryWrapper);
    }
}




