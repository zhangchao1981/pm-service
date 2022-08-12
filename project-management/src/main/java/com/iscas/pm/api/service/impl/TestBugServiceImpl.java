package com.iscas.pm.api.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author 66410
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
        TestBugProcessLog processLog = new TestBugProcessLog(testBug.getId(), BugProcessActionEnum.NEW, "");
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public IPage<TestBug> bugList(TestBugQueryParam param) {
        return testBugMapper.getTestBugList(new Page<>(param.getPageNum(), param.getPageSize()), param);
    }

    @Override
    public void editBug(TestBug testBug) {
        ///修改缺陷
        testBugMapper.updateById(testBug);

        //添加缺陷处理日志
        TestBugProcessLog processLog = new TestBugProcessLog(testBug.getId(), BugProcessActionEnum.NEW, "");
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public void startProcessBug(Integer bugId) {
        //更新缺陷信息
        UpdateWrapper<TestBug> wrapper = Wrappers.update();
        wrapper.lambda()
                .set(TestBug::getStatus, BugStatusEnum.RUNNING)
                .eq(TestBug::getId, bugId);
        if (!super.update(wrapper))
            throw new IllegalArgumentException("缺陷id不存在");

        //添加缺陷处理日志
        TestBugProcessLog processLog = new TestBugProcessLog(bugId, BugProcessActionEnum.START, "");
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public void transferBug(TransferBugParam param) {
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

        //补全信息，更新缺陷信息
        testBug.setSolver(RequestHolder.getUserInfo().getEmployeeName());
        testBug.setSolveTime(new Date());
        testBug.setSolveResult(param.getSolveResult());
        testBug.setSolveHours(DateUtil.getWorkHours(testBug.getCreateTime(),new Date()));
        testBug.setInjectStage(param.getInjectStage());
        testBug.setType(param.getType());

        super.updateById(testBug);

        //添加缺陷处理日志
        String description = "解决结果：" + param.getSolveResult() + ",解决说明：" + param.getExplain();
        TestBugProcessLog processLog = new TestBugProcessLog(param.getBugId(), BugProcessActionEnum.SOLVED, description);
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public void delayedSolveBug(Integer bugId, String explain) {
        //更新缺陷信息
        UpdateWrapper<TestBug> wrapper = Wrappers.update();
        wrapper.lambda()
                .set(TestBug::getStatus, BugStatusEnum.DELAYED_SOLVED)
                .eq(TestBug::getId, bugId);
        if (!super.update(wrapper))
            throw new IllegalArgumentException("缺陷id不存在");

        //添加缺陷处理日志
        String description = "延时原因：" + explain;
        TestBugProcessLog processLog = new TestBugProcessLog(bugId, BugProcessActionEnum.DELAYED_SOLVED, description);
        testBugProcessLogMapper.insert(processLog);
    }

    @Override
    public void reopenBug(Integer bugId, String explain) {

    }

    @Override
    public void closeBug(SolveBugParam param) {

    }

}




