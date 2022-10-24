package com.iscas.pm.api.service.impl.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.test.TestUseCaseMapper;
import com.iscas.pm.api.model.test.TestUseCase;
import com.iscas.pm.api.model.test.param.UseCaseForPlanQueryParam;
import com.iscas.pm.api.service.TestUseCaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

import static org.apache.commons.lang.StringUtils.isNumeric;


/**
 * @author 66410
 * @description 针对表【test_use_case(测试用例表)】的数据库操作Service实现
 * @createDate 2022-08-10 10:25:39
 */
@Service
public class TestUseCaseServiceImpl extends ServiceImpl<TestUseCaseMapper, TestUseCase>
        implements TestUseCaseService {
    @Autowired  TestUseCaseMapper testUseCaseMapper;


    @Override
    public List<TestUseCase> testUseCaseListForPlan(UseCaseForPlanQueryParam useCaseForPlanQueryParam) {
        return  testUseCaseMapper.testUseCaseListForPlan(useCaseForPlanQueryParam);
//        List<Integer> modularList = useCaseForPlanQueryParam.getModularList();
//        String titleOrId = useCaseForPlanQueryParam.getTitleOrId();
//        Integer creatorId = useCaseForPlanQueryParam.getCreatorId();
//        QueryWrapper<TestUseCase> wrapper = new QueryWrapper<TestUseCase>()
//                .in(modularList.size()>0, "modular_id", modularList)
//                .eq(creatorId !=null,"creator_id",creatorId)
//                .eq(StringUtils.isNotBlank(useCaseForPlanQueryParam.getUseCaseType()),"type",useCaseForPlanQueryParam.getUseCaseType())
//                .and(StringUtils.isNotBlank(titleOrId),q->q.eq(isNumeric(titleOrId), "id", org.springframework.util.StringUtils.isEmpty(titleOrId)||!isNumeric(titleOrId)?null:Integer.valueOf(titleOrId))
//                        .or().like("title", titleOrId));
//        return testUseCaseMapper.selectList(wrapper);
    }
}







