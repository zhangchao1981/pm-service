package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.test.TestBugMapper;
import com.iscas.pm.api.model.test.TestBug;
import com.iscas.pm.api.service.TestBugService;
import org.springframework.stereotype.Service;


/**
* @author 66410
* @description 针对表【test_bug(测试缺陷表)】的数据库操作Service实现
* @createDate 2022-08-10 10:25:53
*/
@Service
public class TestBugServiceImpl extends ServiceImpl<TestBugMapper, TestBug>
    implements TestBugService {

}




