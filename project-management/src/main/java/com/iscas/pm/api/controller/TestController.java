package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iscas.pm.api.model.test.*;
import com.iscas.pm.api.service.*;

import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    public IPage<TestUseCase> testUseCaseList(@Valid @RequestBody UseCaseQueryParam useCaseQueryParam) {
        Integer modularId = useCaseQueryParam.getModularId();
        String useCaseId = useCaseQueryParam.getId();
        String useCaseTitle = useCaseQueryParam.getTitle();

        QueryWrapper<TestUseCase> wrapper = new QueryWrapper<TestUseCase>()
                .eq(modularId != null, "modular_id", modularId)
                .like(StringUtils.isNotBlank(useCaseId), "id", useCaseId)
                .like(StringUtils.isNotBlank(useCaseTitle), "title", useCaseTitle);

        return testUseCaseService.page(new Page<>(useCaseQueryParam.getPageNum(), useCaseQueryParam.getPageSize()), wrapper);
    }


    @ApiOperationSupport(order = 2)
    @PostMapping("/addTestUseCase")
    @ApiOperation(value = "添加测试用例", notes = "添加测试用例")
    @PreAuthorize("hasAuthority('/test/addTestUseCase')")
    public TestUseCase addTestUseCase(@Valid @RequestBody TestUseCase testUseCase) {
        testUseCase.setCreator(RequestHolder.getUserInfo().getEmployeeName());
        testUseCase.setCreateTime(new Date());
        //不需要setModularId
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

    @ApiOperationSupport(order = 4)
    @PostMapping("/deleteBatchUseCase")
    @ApiOperation(value = "批量删除测试用例", notes = "批量删除指定测试用例")
    @PreAuthorize("hasAuthority('/test/deleteBatchTestUseCase')")
    public Boolean deleteTestUseCase(@RequestBody List<Integer> ids) {
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
    @ApiOperation(value = "测试计划列表", notes = "查询符合条件的测试计划列表")
    @PreAuthorize("hasAuthority('/test/testPlanList')")
    public IPage<TestPlan> testPlanList(@Valid @RequestBody TestPlanQueryParam planQueryParam) {
        String titleOrWorker = planQueryParam.getTitleOrWorker();
        QueryWrapper<TestPlan> wrapper = new QueryWrapper<TestPlan>()
                .like(StringUtils.isNotBlank(titleOrWorker), "name", titleOrWorker).or()
                .like(StringUtils.isNotBlank(titleOrWorker), "worker", titleOrWorker);
        List<TestPlan> list = testPlanService.list(wrapper);
        list.stream().forEach(e -> {
            e.inputstatisticData(testPlanService.statisticData(e.getId()));   });
        return testPlanService.page(new Page<>(planQueryParam.getPageNum(), planQueryParam.getPageSize())).setRecords(list);
    }


    //未测，等执行记录开发
    @ApiOperationSupport(order = 6)
    @PostMapping("/testPlan")
    @ApiOperation(value = "测试计划详情", notes = "查看测试计划对应的测试用例及执行情况")
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
        if (!ObjectUtils.isEmpty(testPlanService.getOne(new QueryWrapper<TestPlan>().eq("name", testPlan.getName())))) {
            throw new IllegalArgumentException("计划名重复，请重新命名");
        }
        testPlan.setCreateTime(new Date());
        testPlan.setUpdateTime(new Date());

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
    @GetMapping("/deleteTestPlan")
    @ApiOperation(value = "删除测试计划", notes = "删除指定测试计划")
    @PreAuthorize("hasAuthority('/test/deletetTestPlan')")
    public Boolean deleteTestPlan(Integer planId) {
        //有问题
        QueryWrapper<TestExecuteLog> wrapper = new QueryWrapper<TestExecuteLog>()
                .eq("plan_id", planId)
                .isNotNull("pass");
        //有问题  boolean校验  不能校验null  只要加判断条件是null就查不出来结果

        if (testExecuteLogService.list(wrapper).size() > 0) {
            throw new IllegalArgumentException("该计划下已存在有效测试执行记录，不允许删除");
        }
        if (!testPlanService.removeById(planId)) {
            throw new IllegalArgumentException("要删除的测试计划id不存在");
        }
        return true;
    }


    @ApiOperationSupport(order = 10)
    @PostMapping("/testExecuteLogList")
    @ApiOperation(value = "查询用例执行记录", notes = "查询指定模块下符合条件的用例执行记录表")
    @PreAuthorize("hasAuthority('/test/testExecuteLogList')")
    public IPage<TestExecuteLog> testExecuteLogList(@Valid @RequestBody TestExecuteLogParam testExecuteLogParam) {
        Integer planId = testExecuteLogParam.getPlanId();
        return testExecuteLogService.page(new Page<>(testExecuteLogParam.getPageNum(), testExecuteLogParam.getPageSize()),
                new QueryWrapper<TestExecuteLog>().eq(planId != null, "plan_id", planId));
    }


    @ApiOperationSupport(order = 11)
    @PostMapping("/addTestExecuteLog")
    @ApiOperation(value = "导入用例", notes = "批量导入测试用例到目标测试计划的用例执行记录表里")
    @PreAuthorize("hasAuthority('/test/addTestExecuteLog')")
    public List<TestExecuteLog> addTestExecuteLog(@Valid @RequestBody AddTestExecultLogParam addTestExecultLogParam) {
        return testExecuteLogService.addTestExecuteLog(addTestExecultLogParam.getIdList(), addTestExecultLogParam.getPlanId());
    }

    //是否需要
    @ApiOperationSupport(order = 12)
    @PostMapping("/editTestExecuteLog")
    @ApiOperation(value = "修改用例执行记录", notes = "修改用例执行记录")
    @PreAuthorize("hasAuthority('/test/editTestExecuteLog')")
    public Boolean editTestExecuteLog(@Valid @RequestBody TestExecuteLog testExecuteLog) {
        if (!testExecuteLogService.updateById(testExecuteLog)) {
            throw new IllegalArgumentException("要修改的用例执行记录id不存在");
        }
        return true;
    }

    @ApiOperationSupport(order = 13)
    @PostMapping("/editBatchTestExecuteLog")
    @ApiOperation(value = "批量更改执行记录状态", notes = "批量完善执行记录是否通过及指定测试人员信息")
    @PreAuthorize("hasAuthority('/test/editBatchTestExecuteLog')")
    public Boolean editBatchTestExecuteLog(@Valid @RequestBody EditBatchExecuteLogParam editBatchExecuteLogParam) {
        return testExecuteLogService.updateBatchTestExecute(editBatchExecuteLogParam);
    }

    @ApiOperationSupport(order = 14)
    @PostMapping("/deleteBatchTestExecuteLog")
    @ApiOperation(value = "批量删除用例执行记录", notes = "批量删除指定用例执行记录")
    @PreAuthorize("hasAuthority('/test/deleteBatchTestExecuteLog')")
    public Boolean deleteTestExecuteLog(@RequestBody List<Integer> ids) {
        if (ids.size() < 1) {
            throw new IllegalArgumentException("要删除的执行记录id不能为空");
        }
        if (!testExecuteLogService.removeByIds(ids)) {
            throw new IllegalArgumentException("要删除的用例执行记录id不存在");
        }
        return true;
    }


    /**
     * 执行计划统计信息
     *
     * @return
     */

    @ApiOperationSupport(order = 6)
    @PostMapping("/testPlanStatistic")
    @ApiOperation(value = "测试计划统计数", notes = "查看测试计划对应统计数据")
    @PreAuthorize("hasAuthority('/test/useCaseAmountInPlan')")
    public TestPlanStatisticData useCaseAmountInPlan(@RequestParam Integer testPlanId) {
        return testPlanService.statisticData(testPlanId);
    }

}
