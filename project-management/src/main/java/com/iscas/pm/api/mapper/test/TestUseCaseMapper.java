package com.iscas.pm.api.mapper.test;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.test.TestUseCase;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【test_use_case(测试用例表)】的数据库操作Mapper
* @createDate 2022-08-10 10:25:39
* @Entity test.TestUseCase
*/
@Mapper
public interface TestUseCaseMapper extends BaseMapper<TestUseCase> {

}




