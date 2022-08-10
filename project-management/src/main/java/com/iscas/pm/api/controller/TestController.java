package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iscas.pm.api.model.test.TestUseCase;
import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/8/10
 * @Description： 测试管理控制类
 */
@RestController
@Api(tags = {"测试管理"})
@RequestMapping("/test")
@ApiSort(7)
public class TestController {


    @ApiOperationSupport(order = 1)
    @PostMapping("/testUseCaseList")
    @ApiOperation(value = "测试用例列表", notes = "查询指定模块下符合条件的测试用例列表")
    @PreAuthorize("hasAuthority('/test/testUseCaseList')")
    public IPage<TestUseCase> testUseCaseList(String modularId,String query) {

        return null;
    }


    @ApiOperationSupport(order = 2)
    @PostMapping("/addTestUseCase")
    @ApiOperation(value = "添加测试用例", notes = "添加测试用例")
    @PreAuthorize("hasAuthority('/test/addTestUseCase')")
    public TestUseCase addTestUseCase(@Valid @RequestBody TestUseCase testUseCase) {

        return testUseCase;
    }

    @ApiOperationSupport(order = 3)
    @PostMapping("/editTestUseCase")
    @ApiOperation(value = "修改测试用例", notes = "修改测试用例")
    @PreAuthorize("hasAuthority('/test/editTestUseCase')")
    public Boolean editTestUseCase(@Valid @RequestBody TestUseCase testUseCase) {

        return true;
    }

    @ApiOperationSupport(order = 4)
    @GetMapping("/deleteTestUseCase")
    @ApiOperation(value = "批量删除测试用例", notes = "批量删除指定测试用例")
    @PreAuthorize("hasAuthority('/test/editTestUseCase')")
    public Boolean deleteTestUseCase(List<Integer> ids) {

        return true;
    }

    @ApiOperationSupport(order = 5)
    @GetMapping("/exportTestUseCase")
    @ApiOperation(value = "导出测试用例（暂缓实现）", notes = "导出测试用例到excel文件")
    @PreAuthorize("hasAuthority('/test/exportTestUseCase')")
    public Boolean exportTestUseCase() {

        return true;
    }
}
