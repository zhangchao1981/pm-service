package com.iscas.pm.api.mapper.test;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.test.TestBug;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【test_bug(测试缺陷表)】的数据库操作Mapper
* @createDate 2022-08-10 10:25:53
* @Entity test.TestBug
*/
@Mapper
public interface TestBugMapper extends BaseMapper<TestBug> {

}




