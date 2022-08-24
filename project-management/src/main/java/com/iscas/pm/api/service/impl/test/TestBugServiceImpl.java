package com.iscas.pm.api.service.impl.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.test.TestBugMapper;
import com.iscas.pm.api.mapper.test.TestBugProcessLogMapper;
import com.iscas.pm.api.model.test.*;
import com.iscas.pm.api.model.test.enums.BugProcessActionEnum;
import com.iscas.pm.api.model.test.enums.BugStatusEnum;
import com.iscas.pm.api.model.test.param.SolveBugParam;
import com.iscas.pm.api.model.test.param.TestBugQueryParam;
import com.iscas.pm.api.model.test.param.TransferBugParam;
import com.iscas.pm.api.service.TestBugService;
import com.iscas.pm.api.util.DateUtil;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author lichang
 * @description 针对表【test_bug(测试缺陷表)】的数据库操作Service实现
 * @createDate 2022-08-10 10:25:53
 */
@Service
public class TestBugServiceImpl extends ServiceImpl<TestBugMapper, TestBug> implements TestBugService {
    @Autowired
    private TestBugMapper testBugMapper;
    @Autowired
    private TestBugProcessLogMapper testBugProcessLogMapper;

    @Override
    public void addBug(TestBug testBug) {
        //补全信息，添加缺陷
        testBug.setCreator(RequestHolder.getUserInfo().getEmployeeName());
        testBug.setCreatorUserName(RequestHolder.getUserInfo().getUserName());
        testBug.setCreateTime(new Date());
        testBug.setOwner(testBug.getCurrentProcessor());
        testBug.setStatus(BugStatusEnum.NEW);
        testBugMapper.insert(testBug);

        //添加缺陷处理日志
        TestBugProcessLog processLog = new TestBugProcessLog(testBug.getId(), BugProcessActionEnum.NEW, JSON.toJSONString(testBug));
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public IPage<TestBug> bugList(TestBugQueryParam param) {
        return testBugMapper.getTestBugList(new Page<>(param.getPageNum(), param.getPageSize()), param);
    }

    @Override
    public void editBug(TestBug testBug) {
        TestBug db_testBug = super.getById(testBug.getId());
        if (db_testBug == null)
            throw new IllegalArgumentException("缺陷id不存在");
        if (db_testBug.getStatus() == BugStatusEnum.CLOSE)
            throw new IllegalArgumentException("缺陷已经关闭，不能修改");

        //获取变化字段
        TestBug bug = getChangeInfo(testBug, db_testBug);

        if (bug != null) {
            super.updateById(bug);

            //添加缺陷处理日志
            TestBugProcessLog processLog = new TestBugProcessLog(testBug.getId(), BugProcessActionEnum.EDIT, JSON.toJSONString(bug));
            testBugProcessLogMapper.insert(processLog);
        }
    }

    @Override
    public void startProcessBug(Integer bugId) {
        TestBug db_testBug = super.getById(bugId);
        if (db_testBug == null)
            throw new IllegalArgumentException("缺陷id不存在");

        if (db_testBug.getStatus() != BugStatusEnum.NEW && db_testBug.getStatus() != BugStatusEnum.REOPEN)
            throw new IllegalArgumentException("缺陷状态为新建或重新打开时，才可执行【开始】操作");

        if (!db_testBug.getCurrentProcessorUserName().equals(RequestHolder.getUserInfo().getUserName()))
            throw new IllegalArgumentException("您没有处理该缺陷的权限");

       super.updateById(db_testBug);

        //添加缺陷处理日志
        TestBugProcessLog processLog = new TestBugProcessLog(bugId, BugProcessActionEnum.START, "");
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public void transferBug(TransferBugParam param) {
        //当前处理人或技术经理可以转办缺陷

        //更新缺陷信息
        UpdateWrapper<TestBug> wrapper = Wrappers.update();
        wrapper.lambda()
                .set(TestBug::getOwner, param.getTransferName())
                .set(TestBug::getCurrentProcessor, param.getTransferName())
                .set(TestBug::getCurrentProcessorUserName, param.getTransferUserName())
                .eq(TestBug::getId, param.getBugId());
        if (!super.update(wrapper))
            throw new IllegalArgumentException("缺陷id不存在");

        //添加缺陷处理日志
        String description = "给" + param.getTransferName() + ",转办说明：" + param.getExplain();
        TestBugProcessLog processLog = new TestBugProcessLog(param.getBugId(), BugProcessActionEnum.TRANSFER, description);
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public void solveBug(SolveBugParam param) {
        TestBug testBug = super.getById(param.getBugId());
        if (testBug == null)
            throw new IllegalArgumentException("缺陷id不存在");
        if (testBug.getStatus()!=BugStatusEnum.RUNNING)
            throw new IllegalArgumentException("【进行中】状态的缺陷才能执行【已解决】操作");
        if (!testBug.getCurrentProcessorUserName().equals(RequestHolder.getUserInfo().getUserName()))
            throw new IllegalArgumentException("您没有处理该缺陷的权限");

        //补全信息，更新缺陷信息
        testBug.setSolver(RequestHolder.getUserInfo().getEmployeeName());
        testBug.setSolveTime(new Date());
        testBug.setSolveResult(param.getSolveResult());
        testBug.setSolveHours(DateUtil.getWorkHours(testBug.getCreateTime(), new Date()));

        testBug.setInjectStage(param.getInjectStage());
        testBug.setType(param.getType());
        testBug.setStatus(BugStatusEnum.SOLVED);

        testBug.setCurrentProcessor(testBug.getCreator());
        testBug.setCurrentProcessorUserName(testBug.getCreatorUserName());

        super.updateById(testBug);

        //添加缺陷处理日志
        String description = "解决结果：" + param.getSolveResult().getValue() + ",解决说明：" + param.getExplain();
        TestBugProcessLog processLog = new TestBugProcessLog(param.getBugId(), BugProcessActionEnum.SOLVED, description);
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public void delayedSolveBug(Integer bugId, String explain) {
        TestBug db_testBug = super.getById(bugId);
        if (db_testBug == null)
            throw new IllegalArgumentException("缺陷id不存在");
        if (db_testBug.getStatus() != BugStatusEnum.NEW && db_testBug.getStatus() != BugStatusEnum.RUNNING)
            throw new IllegalArgumentException("缺陷状态为新建或运行中时，才可执行【延迟解决】操作");
        if (!db_testBug.getCurrentProcessorUserName().equals(RequestHolder.getUserInfo().getUserName()))
            throw new IllegalArgumentException("您没有处理该缺陷的权限");

        db_testBug.setStatus(BugStatusEnum.DELAYED_SOLVED);

        //更新缺陷信息
        super.updateById(db_testBug);

        //添加缺陷处理日志
        String description = "延时原因：" + explain;
        TestBugProcessLog processLog = new TestBugProcessLog(bugId, BugProcessActionEnum.DELAYED_SOLVED, description);
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public void reopenBug(Integer bugId, String explain) {
        TestBug db_testBug = super.getById(bugId);
        if (db_testBug == null)
            throw new IllegalArgumentException("缺陷id不存在");
        if (db_testBug.getStatus() != BugStatusEnum.SOLVED)
            throw new IllegalArgumentException("缺陷状态为已解决时，才可执行【重新打开】操作");

        db_testBug.setStatus(BugStatusEnum.REOPEN);

        //更新缺陷信息
        super.updateById(db_testBug);

        //添加缺陷处理日志
        String description = StringUtils.isBlank(explain) ? "" : "说明：" + explain;
        TestBugProcessLog processLog = new TestBugProcessLog(bugId, BugProcessActionEnum.REOPEN, description);
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public void closeBug(SolveBugParam param) {
        TestBug testBug = super.getById(param.getBugId());
        if (testBug == null)
            throw new IllegalArgumentException("缺陷id不存在");
        if (testBug.getStatus() != BugStatusEnum.SOLVED)
            throw new IllegalArgumentException("缺陷状态为已解决时，才可执行【关闭】操作");

        //补全信息，更新缺陷信息
        testBug.setSolveResult(param.getSolveResult());
        testBug.setRegressionHours(DateUtil.getWorkHours(testBug.getSolveTime(), new Date()));
        testBug.setInjectStage(param.getInjectStage());
        testBug.setType(param.getType());
        testBug.setStatus(BugStatusEnum.CLOSE);

        super.updateById(testBug);

        //添加缺陷处理日志
        String description = StringUtils.isBlank(param.getExplain()) ? "" : "说明：" + param.getExplain();
        TestBugProcessLog processLog = new TestBugProcessLog(param.getBugId(), BugProcessActionEnum.CLOSE, description);
        testBugProcessLogMapper.insert(processLog);
    }

    private TestBug getChangeInfo(TestBug testBug, TestBug db_testBug) {
        boolean flag = false;

        //设置允许修改的信息
        TestBug bug = new TestBug();
        if (!db_testBug.getTitle().equals(testBug.getTitle())) {
            flag = true;
            bug.setTitle(testBug.getTitle());
        }
        if (db_testBug.getInjectStage() != testBug.getInjectStage()) {
            flag = true;
            bug.setInjectStage(testBug.getInjectStage());
        }
        if (!db_testBug.getDetail().equals(testBug.getDetail())) {
            flag = true;
            bug.setDetail(testBug.getDetail());
        }
        if (!db_testBug.getExecuteLogId().equals(testBug.getExecuteLogId())) {
            flag = true;
            bug.setExecuteLogId(testBug.getExecuteLogId());
        }
        if (!db_testBug.getFiles().equals(testBug.getFiles())) {
            flag = true;
            bug.setFiles(testBug.getFiles());
        }
        if (!db_testBug.getModuleId().equals(testBug.getModuleId())) {
            flag = true;
            bug.setModuleId(testBug.getModuleId());
        }
        if (!db_testBug.getPlanId().equals(testBug.getPlanId())) {
            flag = true;
            bug.setPlanId(testBug.getPlanId());
        }
        if (db_testBug.getPriority() != testBug.getPriority()) {
            flag = true;
            bug.setPriority(testBug.getPriority());
        }
        if (db_testBug.getProbability() != testBug.getProbability()) {
            flag = true;
            bug.setProbability(testBug.getProbability());
        }
        if (!db_testBug.getRequirementId().equals(testBug.getRequirementId())) {
            flag = true;
            bug.setRequirementId(testBug.getRequirementId());
        }
        if (db_testBug.getSeverity() != testBug.getSeverity()) {
            flag = true;
            bug.setSeverity(testBug.getSeverity());
        }
        if (db_testBug.getSource() != testBug.getSource()) {
            flag = true;
            bug.setSource(testBug.getSource());
        }
        if (db_testBug.getType() != testBug.getType()) {
            flag = true;
            bug.setType(testBug.getType());
        }
        if (!flag) {
            return null;
        }
        bug.setId(testBug.getId());
        return bug;
    }

}



