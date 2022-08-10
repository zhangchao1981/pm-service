package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iscas.pm.api.model.test.*;
import com.iscas.pm.api.service.*;

import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Date;
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
    @Autowired
    TestUseCaseService testUseCaseService;
    @Autowired
    TestPlanService testPlanService;
    @Autowired
    TestBugService testBugService;
    @Autowired
    TestBugProcessLogService testBugProcessLogService;
    @Autowired
    TestExecuteLogService testExecuteLogService;
    @Autowired
    DevRequirementService requirementService;


    @ApiOperationSupport(order = 1)
    @PostMapping("/testUseCaseList")
    @ApiOperation(value = "查询测试用例", notes = "查询指定模块下符合条件的测试用例表")
    @PreAuthorize("hasAuthority('/test/testUseCaseList')")
    public IPage<TestUseCase> testUseCaseList(@RequestBody UseCaseQueryParam useCaseQueryParam) {
        Integer modularId = useCaseQueryParam.getModularId();
        String useCaseId = useCaseQueryParam.getId();
        String useCaseTitle = useCaseQueryParam.getTitle();
        //有一个问题：modularId查询不到
        return testUseCaseService.page(new Page<>(useCaseQueryParam.getPageNum(), useCaseQueryParam.getPageSize()),
                new QueryWrapper<TestUseCase>().eq(modularId != null, "modular_id", modularId)
                        .eq(useCaseId != null, "id", useCaseId)
                        .like(useCaseTitle != null, "title", useCaseTitle));
    }


    @ApiOperationSupport(order = 2)
    @PostMapping("/addTestUseCase")
    @ApiOperation(value = "添加测试用例", notes = "添加测试用例")
    @PreAuthorize("hasAuthority('/test/addTestUseCase')")
    public TestUseCase addTestUseCase(@Valid @RequestBody TestUseCase testUseCase) {
        testUseCase.setCreator(RequestHolder.getUserInfo().getUserName());
        testUseCase.setCreateTime(new Date());
        testUseCase.setModularId(requirementService.getById(testUseCase.getRequirementId()).getModularId());
        testUseCaseService.save(testUseCase);
        return testUseCase;
    }

    @ApiOperationSupport(order = 3)
    @PostMapping("/editTestUseCase")
    @ApiOperation(value = "修改测试用例", notes = "修改测试用例")
    @PreAuthorize("hasAuthority('/test/editTestUseCase')")
    public Boolean editTestUseCase(@Valid @RequestBody TestUseCase testUseCase) {
        if (!testUseCaseService.updateById(testUseCase)) {
            throw new IllegalArgumentException("要修改的测试用例id不存在");
        }
        return true;
    }


    //有问题,已该，未测
    @ApiOperationSupport(order = 4)
    @GetMapping("/deleteBatchUseCase")
    @ApiOperation(value = "批量删除测试用例", notes = "批量删除指定测试用例")
    @PreAuthorize("hasAuthority('/test/deleteBatchTestUseCase')")
    public Boolean deleteTestUseCase(@RequestParam List<Integer> ids) {
        if (!testUseCaseService.removeByIds(ids)) {
            throw new IllegalArgumentException("要删除的测试用例id不存在");
        }
        return true;
    }

    @ApiOperationSupport(order = 5)
    @GetMapping("/exportTestUseCase")
    @ApiOperation(value = "导出测试用例（暂缓实现）", notes = "导出测试用例到excel文件")
    @PreAuthorize("hasAuthority('/test/exportTestUseCase')")
    public Boolean exportTestUseCase() {
        return true;
    }



    @ApiOperationSupport(order = 6)
    @PostMapping("/testPlanList")
    @ApiOperation(value = "查询测试计划列表", notes = "查询符合条件的测试计划列表")
    @PreAuthorize("hasAuthority('/test/testPlanList')")
    public IPage<TestPlan> testPlanList(@RequestBody TestPlanQueryParam planQueryParam) {
        String titleOrWorker = planQueryParam.getTitleOrWorker();
        return testPlanService.page(new Page<>(planQueryParam.getPageNum(), planQueryParam.getPageSize()),
                new QueryWrapper<TestPlan>().like(titleOrWorker != null, "name", titleOrWorker).or().like(titleOrWorker != null,"worker",titleOrWorker));
    }

    @ApiOperationSupport(order = 6) //未测，等执行记录开发
    @PostMapping("/testPlan")
    @ApiOperation(value = "查看测试计划详情", notes = "查看测试计划对应的测试用例及执行情况")
    @PreAuthorize("hasAuthority('/test/testPlan')")
    public IPage<TestExecuteLog> testPlan(@RequestBody TestExecuteQueryParam executeQueryParam) {
        return testExecuteLogService.page(new Page<>(executeQueryParam.getPageNum(), executeQueryParam.getPageSize()),
                new QueryWrapper<TestExecuteLog>().eq("plan_id", executeQueryParam.getPlanId()));
    }


    @ApiOperationSupport(order = 7)
    @PostMapping("/addTestPlan")
    @ApiOperation(value = "添加测试计划", notes = "添加测试计划")
    @PreAuthorize("hasAuthority('/test/addTestPlan')")
    public TestPlan addTestPlan(@Valid @RequestBody TestPlan testPlan) {
        testPlan.setId(1);
        testPlan.setPassRate(0.0);
        testPlan.setExecuteProgress(0.0);
        testPlan.setPassRate(0.0);
        testPlan.setCreateTime(new Date());
        testPlan.setUpdateTime(new Date());
        testPlan.setBugStatistic(null);
        testPlan.setTestedCase(null);
        if (!ObjectUtils.isEmpty(testPlanService.getOne(new QueryWrapper<TestPlan>().eq("name", testPlan.getName())))) {
            throw new IllegalArgumentException("计划名重复，请重新命名");
        }
        testPlanService.save(testPlan);
        return testPlan;
    }

    @ApiOperationSupport(order = 8)
    @PostMapping("/editTestPlan")
    @ApiOperation(value = "修改测试计划", notes = "修改测试计划")
    @PreAuthorize("hasAuthority('/test/editTestPlan')")
    public Boolean editTestPlan(@Valid @RequestBody TestPlan testPlan) {
        if (!testPlanService.updateById(testPlan)) {
            throw new IllegalArgumentException("要修改的测试用例id不存在");
        }
        return true;
    }

    @ApiOperationSupport(order = 9)
    @GetMapping("/deleteTestPlan")     //测一半,另一半要等执行记录开发
    @ApiOperation(value = "删除测试计划", notes = "删除指定测试计划")
    @PreAuthorize("hasAuthority('/test/deletetTestPlan')")
    public Boolean deleteTestPlan(Integer planId) {
        //若该计划下有执行记录，拒绝删除
        if (testExecuteLogService.list(new QueryWrapper<TestExecuteLog>().eq("plan_id",planId)).size()>0){
            throw new IllegalArgumentException("该计划下仍存有执行记录，拒绝删除");
        }
        if (!testPlanService.removeById(planId)) {
            throw new IllegalArgumentException("要删除的测试计划id不存在");
        }
        return true;
    }

}
