package com.iscas.pm.api.service.impl.test;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.test.TestUseCaseMapper;
import com.iscas.pm.api.model.test.TestUseCase;
import com.iscas.pm.api.model.test.param.UseCaseForPlanQueryParam;
import com.iscas.pm.api.service.TestUseCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lichang
 * @description 针对表【test_use_case(测试用例表)】的数据库操作Service实现
 * @createDate 2022-08-10 10:25:39
 */
@Service
public class TestUseCaseServiceImpl extends ServiceImpl<TestUseCaseMapper, TestUseCase>
        implements TestUseCaseService {
    @Autowired  TestUseCaseMapper testUseCaseMapper;

    @Override
    public List<TestUseCase> testUseCaseListForPlan(UseCaseForPlanQueryParam param) {
        return  testUseCaseMapper.testUseCaseListForPlan(param);
    }
}







