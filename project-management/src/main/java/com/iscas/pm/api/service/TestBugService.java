package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.test.TestBug;
import com.iscas.pm.api.model.test.TestBugQueryParam;

/**
* @author 66410
* @description 针对表【test_bug(测试缺陷表)】的数据库操作Service
* @createDate 2022-08-10 10:25:53
*/
public interface TestBugService extends IService<TestBug> {

    void addBug(TestBug testBug);

    IPage<TestBug> bugList(TestBugQueryParam testBug);

    void editBug(TestBug testBug);
}
