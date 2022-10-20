package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iscas.pm.api.model.test.*;
import com.iscas.pm.api.model.test.param.*;
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
        testUseCase.setCreatorId(RequestHolder.getUserInfo().getId());
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
    @ApiOperation(value = "测试计划列表", notes = "查询符合条件的测试计划列表(分页)")
    @PreAuthorize("hasAuthority('/test/testPlanList')")
    public IPage<TestPlan> testPlanList(@Valid @RequestBody TestPlanQueryParam planQueryParam) {
        String titleOrWorker = planQueryParam.getTitleOrWorker();
        QueryWrapper<TestPlan> wrapper = new QueryWrapper<TestPlan>()
                .like(StringUtils.isNotBlank(titleOrWorker), "name", titleOrWorker).or()
                .like(StringUtils.isNotBlank(titleOrWorker), "worker", titleOrWorker);
        IPage<TestPlan> planIPage = testPlanService.page(new Page<>(planQueryParam.getPageNum(), planQueryParam.getPageSize()), wrapper);
        //添加测试计划的统计计算结果
        planIPage.getRecords().stream().forEach(plan -> {
            plan.inputStatisticData(testPlanService.statisticData(plan.getId())); });
        return   planIPage;
    }


    @ApiOperationSupport(order = 7)
    @GetMapping("/allTestPlanList")
    @ApiOperation(value = "查询全部测试计划", notes = "查询当前项目下全部的测试计划列表(不分页)")
    @PreAuthorize("hasAuthority('/test/allTestPlanList')")
    public List<TestPlan> allTestPlanList() {
            return  testPlanService.list();
    }


    @ApiOperationSupport(order = 8)
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

    @ApiOperationSupport(order = 9)
    @PostMapping("/editTestPlan")
    @ApiOperation(value = "修改测试计划", notes = "修改测试计划")
    @PreAuthorize("hasAuthority('/test/editTestPlan')")
    public Boolean editTestPlan(@Valid @RequestBody TestPlan testPlan) {
        if (!testPlanService.updateById(testPlan)) {
            throw new IllegalArgumentException("要修改的测试用例id不存在");
        }
        return true;
    }

    @ApiOperationSupport(order = 10)
    @GetMapping("/deleteTestPlan")
    @ApiOperation(value = "删除测试计划", notes = "删除指定测试计划")
    @PreAuthorize("hasAuthority('/test/deletetTestPlan')")
    public Boolean deleteTestPlan(Integer planId) {

        QueryWrapper<TestExecuteLog> wrapper = new QueryWrapper<TestExecuteLog>()
                .eq("plan_id", planId)
                .ne("pass",null);
        if (testExecuteLogService.list(wrapper).size() > 0) {
            throw new IllegalArgumentException("该计划下已存在有效测试执行记录，不允许删除");
        }

        if (!testPlanService.removeById(planId)) {
            throw new IllegalArgumentException("要删除的测试计划id不存在");
        }
        return true;
    }

    @ApiOperationSupport(order = 11)
    @PostMapping("/addTestExecuteLog")
    @ApiOperation(value = "导入用例", notes = "批量导入测试用例到目标测试计划的用例执行记录表里")
    @PreAuthorize("hasAuthority('/test/addTestExecuteLog')")
    public List<TestExecuteLog> addTestExecuteLog(@Valid @RequestBody AddTestExecultLogParam addTestExecultLogParam) {
        return testExecuteLogService.addTestExecuteLog(addTestExecultLogParam.getIdList(), addTestExecultLogParam.getPlanId());
    }
    //和查询测试计划详情接口合并，增加查询条件
    @ApiOperationSupport(order = 12)
    @PostMapping("/testExecuteLogList")
    @ApiOperation(value = "查询用例执行记录", notes = "查询指定模块下符合条件的用例执行记录表,对应测试计划详情")
    @PreAuthorize("hasAuthority('/test/testExecuteLogList')")
    public IPage<TestExecuteLog> testExecuteLogList(@Valid @RequestBody TestExecuteLogParam testExecuteLogParam) {
        return  testExecuteLogService.testExecuteLogList(testExecuteLogParam);
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

//    @ApiOperationSupport(order = 15)
//    @PostMapping("/testPlanStatistic")
//    @ApiOperation(value = "测试计划统计数", notes = "查看测试计划对应统计数据")
//    @PreAuthorize("hasAuthority('/test/useCaseAmountInPlan')")
//    public TestPlanStatisticData useCaseAmountInPlan(@RequestParam Integer testPlanId) {
//        return testPlanService.statisticData(testPlanId);
//    }


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
    @ApiOperation(value = "编辑缺陷", notes = "编辑缺陷，只能编辑部分信息")
    @PreAuthorize("hasAuthority('/test/editBug')")
    public Boolean editBug(@Valid @RequestBody TestBug testBug) {
        testBugService.editBug(testBug);
        return true;
    }

    @ApiOperationSupport(order = 23)
    @PostMapping("/transferBug")
    @ApiOperation(value = "转办缺陷", notes = "将缺陷转给其他人处理,缺陷归属人也一并转移")
    @PreAuthorize("hasAuthority('/test/transferBug')")
    public Boolean transferBug(@Valid @RequestBody TransferBugParam param) {
        testBugService.transferBug(param);
        return true;
    }

    @ApiOperationSupport(order = 24)
    @PostMapping("/dispatchBug")
    @ApiOperation(value = "指派缺陷", notes = "将缺陷指派给其他人处理,缺陷归属人不变")
    @PreAuthorize("hasAuthority('/test/dispatchBug')")
    public Boolean dispatchBug(@Valid @RequestBody TransferBugParam param) {
        testBugService.dispatchBug(param);
        return true;
    }

    @ApiOperationSupport(order = 25)
    @GetMapping("/startProcessBug")
    @ApiOperation(value = "开始处理缺陷", notes = "开始处理缺陷，更改缺陷状态为进行中")
    @PreAuthorize("hasAuthority('/test/startProcessBug')")
    public Boolean startProcessBug(Integer bugId) {
        testBugService.startProcessBug(bugId);
        return true;
    }

    @ApiOperationSupport(order = 26)
    @PostMapping("/solveBug")
    @ApiOperation(value = "已解决缺陷", notes = "成功解决缺陷，填写解决反馈")
    @PreAuthorize("hasAuthority('/test/solveBug')")
    public Boolean solveBug(@Valid @RequestBody SolveBugParam param) {
        testBugService.solveBug(param);
        return true;
    }

    @ApiOperationSupport(order = 27)
    @GetMapping("/delayedSolveBug")
    @ApiOperation(value = "延迟解决缺陷", notes = "暂不解决，后面版本在解决")
    @PreAuthorize("hasAuthority('/test/delayedSolveBug')")
    public Boolean delayedSolveBug(Integer bugId,String explain) {
        testBugService.delayedSolveBug(bugId,explain);
        return true;
    }

    @ApiOperationSupport(order = 28)
    @GetMapping("/reopenBug")
    @ApiOperation(value = "重新打开缺陷", notes = "重新打开缺陷")
    @PreAuthorize("hasAuthority('/test/reopenBug')")
    public Boolean reopenBug(Integer bugId,String explain) {
        testBugService.reopenBug(bugId,explain);
        return true;
    }

    @ApiOperationSupport(order = 29)
    @PostMapping("/closeBug")
    @ApiOperation(value = "关闭缺陷", notes = "关闭缺陷")
    @PreAuthorize("hasAuthority('/test/closeBug')")
    public Boolean closeBug(@Valid @RequestBody SolveBugParam param) {
        testBugService.closeBug(param);
        return true;
    }

    @ApiOperationSupport(order = 30)
    @GetMapping("/bugProgressLog")
    @ApiOperation(value = "缺陷处理日志", notes = "查询指定缺陷的处理日志")
    public List<TestBugProcessLog> bugProgressLog(Integer bugId) {
        return testBugProcessLogService.list(new QueryWrapper<TestBugProcessLog>().eq("bug_id", bugId));
    }




}
