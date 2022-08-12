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
import org.springframework.boot.web.server.LocalServerPort;
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
        return testPlanService.page(new Page<>(planQueryParam.getPageNum(), planQueryParam.getPageSize()), wrapper);
    }

    @ApiOperationSupport(order = 6) //未测，等执行记录开发
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

        QueryWrapper<TestExecuteLog> wrapper = new QueryWrapper<TestExecuteLog>()
                .eq("plan_id", planId)
                .eq("pass",null);
        if (testExecuteLogService.list(wrapper).size() > 0) {
            throw new IllegalArgumentException("该计划下已存在测试执行记录，不允许删除");
        }

        if (!testPlanService.removeById(planId)) {
            throw new IllegalArgumentException("要删除的测试计划id不存在");
        }
        return true;
    }



    @ApiOperationSupport(order = 21)
    @PostMapping("/bugList")
    @ApiOperation(value = "缺陷列表", notes = "返回符合条件的缺陷列表")
    public IPage<TestBug> bugList(@Valid @RequestBody TestBugQueryParam param) {
        return testBugService.bugList(param);
    }

    @ApiOperationSupport(order = 22)
    @PostMapping("/addBug")
    @ApiOperation(value = "新建缺陷", notes = "添加新缺陷")
    @PreAuthorize("hasAuthority('/test/addBug')")
    public TestBug addBug(@Valid @RequestBody TestBug testBug) {
        testBugService.addBug(testBug);
        return testBug;
    }

    @ApiOperationSupport(order = 22)
    @PostMapping("/editBug")
    @ApiOperation(value = "修改缺陷", notes = "修改缺陷")
    @PreAuthorize("hasAuthority('/test/editBug')")
    public Boolean editBug(@Valid @RequestBody TestBug testBug) {
        testBugService.editBug(testBug);
        return true;
    }

    @ApiOperationSupport(order = 22)
    @PostMapping("/transferBug")
    @ApiOperation(value = "转办缺陷", notes = "将缺陷转给其他人处理")
    @PreAuthorize("hasAuthority('/test/transferBug')")
    public Boolean transferBug(@Valid @RequestBody TestBug testBug) {

        return null;
    }

    @ApiOperationSupport(order = 22)
    @GetMapping("/startProcessBug")
    @ApiOperation(value = "开始处理缺陷", notes = "开始处理缺陷")
    @PreAuthorize("hasAuthority('/test/startProcessBug')")
    public Boolean startProcessBug(Integer bugId) {

        return null;
    }


    @ApiOperationSupport(order = 22)
    @GetMapping("/solveBug")
    @ApiOperation(value = "已解决缺陷", notes = "成功解决缺陷，填写解决反馈")
    @PreAuthorize("hasAuthority('/test/solveBug')")
    public Boolean solveBug(@Valid @RequestBody TestBug testBug) {

        return null;
    }

    @ApiOperationSupport(order = 22)
    @GetMapping("/delayedSolveBug")
    @ApiOperation(value = "延迟解决缺陷", notes = "暂不解决，后面版本在解决")
    @PreAuthorize("hasAuthority('/test/delayedSolveBug')")
    public Boolean delayedSolveBug(@Valid @RequestBody TestBug testBug) {

        return null;
    }

    @ApiOperationSupport(order = 22)
    @GetMapping("/closeBug")
    @ApiOperation(value = "关闭缺陷", notes = "关闭缺陷")
    @PreAuthorize("hasAuthority('/test/closeBug')")
    public Boolean closeBug(String bugId) {

        return true;
    }

    @ApiOperationSupport(order = 22)
    @GetMapping("/bugProgressLog")
    @ApiOperation(value = "缺陷处理日志", notes = "查询指定缺陷的处理日志")
    public List<TestBugProcessLog> bugProgressLog(String bugId) {
        return testBugProcessLogService.list(new QueryWrapper<TestBugProcessLog>().eq("bug_id", bugId));
    }




}
