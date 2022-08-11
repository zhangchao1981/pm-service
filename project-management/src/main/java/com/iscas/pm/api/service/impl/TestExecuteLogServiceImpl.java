package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.test.TestExecuteLogMapper;
import com.iscas.pm.api.mapper.test.TestUseCaseMapper;
import com.iscas.pm.api.model.test.*;
import com.iscas.pm.api.service.TestExecuteLogService;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;


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

    @Override
    public List<TestExecuteLog> addTestExecuteLog(List<Integer> idList, Integer planId) {
        //前端维护一个测试人员表，内容调接口查询user-role表，查到角色是测试员的就加到这个表里，然后导入用例时选中表中的人员name 传过来
        //查询useCaseList
        List<TestUseCase> useCaseList = testUseCaseMapper.selectBatchIds(idList);
        if (useCaseList.size() < 1) {
            throw new IllegalArgumentException("未查询到指定测试用例");
        }
        return useCaseList.stream().map(e -> new TestExecuteLog(e, planId)).collect(Collectors.toList());
    }

    @Override
    public Boolean updateBatchTestExecute(EditBatchExecuteLogParam editBatchExecuteLogParam) {
        Boolean pass = editBatchExecuteLogParam.getPass();
        String testPerson = editBatchExecuteLogParam.getTestPerson();
        List<TestExecuteLog> localList = testExecuteLogMapper.selectList(new QueryWrapper<TestExecuteLog>().in("id", editBatchExecuteLogParam.getIdList()));
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
}




