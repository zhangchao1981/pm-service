package com.iscas.pm.api.mapper.test;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.test.TestUseCase;
import com.iscas.pm.api.model.test.param.UseCaseForPlanQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 66410
* @description 针对表【test_use_case(测试用例表)】的数据库操作Mapper
* @createDate 2022-08-10 10:25:39
* @Entity test.TestUseCase
*/
@Mapper
public interface TestUseCaseMapper extends BaseMapper<TestUseCase> {
    @Select("SELECT permission_id " +
            "FROM auth_role_permission INNER JOIN auth_user_role "+
            "ON auth_user_role.role_id=auth_role_permission.role_id " +
            "WHERE auth_user_role.user_id=#{userid}")
    List<TestUseCase> testUseCaseListForPlan(UseCaseForPlanQueryParam useCaseForPlanQueryParam);

}




