package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.test.TestBugMapper;
import com.iscas.pm.api.mapper.test.TestBugProcessLogMapper;
import com.iscas.pm.api.model.test.*;
import com.iscas.pm.api.service.TestBugProcessLogService;
import com.iscas.pm.api.service.TestBugService;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
        //添加缺陷
        testBug.setCreator(RequestHolder.getUserInfo().getEmployeeName());
        testBug.setCreateTime(new Date());
        testBug.setOwner(testBug.getCurrentProcessor());
        testBug.setStatus(BugStatusEnum.NEW);
        testBugMapper.insert(testBug);

        //添加缺陷处理日志
        TestBugProcessLog processLog = new TestBugProcessLog()
                .setBugId(testBug.getId())
                .setAction(BugProcessActionEnum.EDIT)
                .setDescription(getDescription(BugProcessActionEnum.EDIT, testBug))
                .setProcessor(RequestHolder.getUserInfo().getEmployeeName())
                .setTime(new Date());
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
        TestBugProcessLog processLog = new TestBugProcessLog()
                .setBugId(testBug.getId())
                .setAction(BugProcessActionEnum.NEW)
                .setDescription(getDescription(BugProcessActionEnum.NEW, testBug))
                .setProcessor(RequestHolder.getUserInfo().getEmployeeName())
                .setTime(new Date());
        testBugProcessLogMapper.insert(processLog);
    }

    private String getDescription(BugProcessActionEnum action, TestBug testBug) {
        switch (action) {
            case NEW:
                return "新建缺陷";
            case EDIT:
                return "修改缺陷";
            case TRANSFER:
                return "转办给" + testBug.getCurrentProcessor();
            case SOLVED:
                return "解决缺陷,反馈内容：";
            case START:
                return "开始处理缺陷";
            case DELAYED_SOLVED:
                return "延时解决该缺陷，延时原因：";
            case REOPEN:
                return "重新打开该缺陷";
            case FEEDBACK:
                return "填写反馈：";
            case CLOSE:
                return "关闭该缺陷";
            default:
                return "";
        }
    }
}




