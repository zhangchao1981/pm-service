package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iscas.pm.api.model.dev.*;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.service.*;
import com.iscas.pm.common.core.util.TreeUtil;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/3.
 */
@RestController
@Api(tags = {"开发需求"})
@RequestMapping("/projectDev")
@ApiSort(6)
public class DevController {
    @Autowired
    DevRequirementService devRequirementService;
    @Autowired
    DevModularService devModularService;
    @Autowired
    DevTaskService devTaskService;
    @Autowired
    TaskFeedbackService taskFeedbackService;
    @Autowired
    DevInterfaceService devInterfaceService;

    @ApiOperationSupport(order = 1)
    @PostMapping("/addDevModular")
    @ApiOperation(value = "添加项目模块", notes = "添加项目模块，内含模块名重复校验")
    @PreAuthorize("hasAuthority('/projectDev/addDevModular')")
    public DevModular addDevModular(@Valid @RequestBody DevModular devModular) {
        devModularService.save(devModularService.modularValidCheck(devModular));
        return devModular;
    }

    @ApiOperationSupport(order = 2)
    @PostMapping("/editDevModular")
    @ApiOperation(value = "修改项目模块", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/editDevModular')")
    public DevModular editDevModular(@Valid @RequestBody DevModular devModular) {
        if (!devModularService.updateById(devModularService.modularValidCheck(devModular))) {
            throw new IllegalArgumentException("修改失败，目标id不存在");
        }
        return devModular;
    }

    @ApiOperationSupport(order = 3)
    @PostMapping("/DevModularList")
    @ApiOperation(value = "项目模块列表", notes = "返回全部项目模块(树型)", response = DevModular.class)
    @PreAuthorize("hasAuthority('/projectDev/DevModularList')")
    public List<DevModular> devModularList() {
        return TreeUtil.treeOut(devModularService.list(), DevModular::getId, DevModular::getParentId, DevModular::getModulars);
    }

    @ApiOperationSupport(order = 14)
    @PostMapping("/devChildModularList")
    @ApiOperation(value = "子项目模块列表", notes = "返回模块id下面的子模块集合(只递进一层),返回List<DevModular>", response = DevModular.class)
    public List<DevModular> devChildModularList(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        return devModularService.list(new QueryWrapper<DevModular>().eq("parent_id", id));
    }

    @ApiOperationSupport(order = 4)
    @PostMapping("/deleteDevModular")
    @ApiOperation(value = "删除项目模块", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/deleteDevModular')")
    public boolean deleteDevModular(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        if (devModularService.list(new QueryWrapper<DevModular>().eq("parent_id", id)).size() > 0) {
            throw new IllegalArgumentException("当前模块下有子模块，不许删除");
        }

        if (devRequirementService.list(new QueryWrapper<DevRequirement>().eq("modular_id", id)).size() > 0) {
            throw new IllegalArgumentException("当前模块下有需求，不许删除");
        }
        if (!devModularService.removeById(id)) {
            throw new IllegalArgumentException("要删除的项目模块id不存在");
        }
        return true;
    }

    @ApiOperationSupport(order = 5)
    @PostMapping("/addDevRequirement")
    @ApiOperation(value = "添加开发需求", notes = "开发需求允许重名")
    @PreAuthorize("hasAuthority('/projectDev/addDevRequirement')")
    public Boolean addDevRequirement(@Valid @RequestBody DevRequirement devRequirement) {
        if (devModularService.list(new QueryWrapper<DevModular>().eq("id", devRequirement.getModularId())).size() < 1) {
            throw new IllegalArgumentException("所属模块Id不存在");
        }
        devRequirement.setWorker(RequestHolder.getUserInfo().getEmployeeName());
        devRequirement.setUserId(RequestHolder.getUserInfo().getId());
        devRequirement.setChanged(false);
        devRequirement.setCreateTime(new Date());
        devRequirement.setUpdateTime(new Date());
        devRequirement.setStatus(RequireStatusEnum.DESIGN);
        devRequirementService.save(devRequirement);
        return true;
    }

    @ApiOperationSupport(order = 6)
    @PostMapping("/editDevRequirement")
    @ApiOperation(value = "修改开发需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/editDevRequirement')")
    public Boolean editDevRequirement(@Valid @RequestBody DevRequirement devRequirement) {
        DevRequirement db_requirement = devRequirementService.getById(devRequirement.getId());
        if (db_requirement == null) {
            throw new IllegalArgumentException("要修改的开发需求不存在");
        }

        if (db_requirement.getStatus() != RequireStatusEnum.DESIGN) {
            throw new IllegalArgumentException("已发布的需求不允许修改");
        }
        return devRequirementService.updateById(devRequirement);
    }

    @ApiOperationSupport(order = 6)
    @GetMapping("/deployDevRequirement")
    @ApiOperation(value = "发布开发需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/deployDevRequirement')")
    public Boolean deployDevRequirement(@RequestParam @NotNull(message = "requirementId不能为空") Integer requirementId) {
        DevRequirement db_requirement = devRequirementService.getById(requirementId);
        if (db_requirement == null) {
            throw new IllegalArgumentException("要修改的开发需求不存在");
        }

        if (db_requirement.getStatus() != RequireStatusEnum.DESIGN) {
            throw new IllegalArgumentException("该开发需求已经发布");
        }
        db_requirement.setStatus(RequireStatusEnum.DEPLOYED);

        return devRequirementService.updateById(db_requirement);
    }

    @ApiOperationSupport(order = 7)
    @GetMapping("/devRequirementList")
    @ApiOperation(value = "查询开发需求", notes = "返回开发需求页面的略缩信息,类型为")
    @PreAuthorize("hasAuthority('/projectDev/devRequirementList')")
    public List<DevRequirement> devRequirementList(@RequestParam @NotNull(message = "modularId不能为空") Integer modularId) {
        return devRequirementService.list(new QueryWrapper<DevRequirement>().eq("modular_id", modularId));

    }

    @ApiOperationSupport(order = 7)
    @PostMapping("/devRequirementListOrTaskList")
    @ApiOperation(value = "查询开发需求或开发任务", notes = "根据用户输入参数查询符合条件的开发需求或开发任务，")
    public List<DevModular> devRequirementListOrTaskList(@RequestBody DevRequirementQueryParam queryParam) {
        return devRequirementService.devRequirementListOrTaskList(queryParam);
    }

    @ApiOperationSupport(order = 8)
    @GetMapping("/devRequirement")
    @ApiOperation(value = "查询开发需求详情", notes = "基本信息及原型设计图在devRequirement里面,用例说明在useCase里", response = DevRequirement.class)
    @PreAuthorize("hasAuthority('/projectDev/devRequirement')")
    public DevRequirement devRequirement(@RequestParam @NotNull(message = "requirementId不能为空") Integer requirementId) {
        return devRequirementService.getById(requirementId);
    }

    @ApiOperationSupport(order = 9)
    @PostMapping("/deleteDevRequirement")
    @ApiOperation(value = "删除开发需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/deleteDevRequirement')")
    public boolean deleteDevRequirement(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        DevRequirement db_requirement = devRequirementService.getById(id);
        if (db_requirement == null) {
            throw new IllegalArgumentException("要删除的开发需求不存在");
        }

        if (db_requirement.getStatus() != RequireStatusEnum.DESIGN) {
            throw new IllegalArgumentException("已发布的需求不允许删除");
        }
        return devRequirementService.removeById(id);
    }

    @ApiOperationSupport(order = 10)
    @PostMapping("/addDevTask")
    @ApiOperation(value = "添加开发任务", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/addDevTask')")
    public Boolean addDevTask(@Valid @RequestBody DevTask devTask) {
        DevRequirement db_requirement = devRequirementService.getById(devTask.getRequireId());
        if (db_requirement == null) {
            throw new IllegalArgumentException("开发任务的所属需求不存在");
        }

        if (db_requirement.getStatus() == RequireStatusEnum.DESIGN) {
            throw new IllegalArgumentException("未发布的需求不能添加任务");
        }

        devTask.setStatus(getDevStatus(devTask.getStartDate(), devTask.getEndDate()));
        devTask.setHappenedHour(0.0);
        devTaskService.addDevTask(devTask);

        //重新计算需求的已发生工时、进度、实际开始时间、实际结束时间、需求状态
        devRequirementService.computeRequirementStatus(devTask.getRequireId());

        return true;
    }

    @ApiOperationSupport(order = 11)
    @PostMapping("/editDevTask")
    @ApiOperation(value = "修改开发任务")
    @PreAuthorize("hasAuthority('/projectDev/editDevTask')")
    public DevTask editDevTask(@Valid @RequestBody DevTask devTask) {
        DevTask db_task = devTaskService.getById(devTask.getId());
        if (db_task == null)
            throw new IllegalArgumentException("修改的任务不存在！");

        devTask.setStatus(getDevStatus(devTask.getStartDate(), devTask.getEndDate()));
        devTaskService.updateById(devTask);

        //重新计算任务的已发生工时、任务进度、任务实际开始时间、任务结束时间、任务状态
        List<TaskFeedback> all_feedbacks = taskFeedbackService.selectListByTaskId(new TaskFeedback().setDevTaskId(devTask.getId()));
        devTaskService.computeDevTask(devTask, all_feedbacks);
        return devTask;
    }

    @ApiOperationSupport(order = 12)
    @GetMapping("/devTaskList")
    @ApiOperation(value = "查询开发任务", notes = "返回需求id对应的全部任务 返回值类型为List<DevTask>,需求id为null返回所有任务", response = DevTask.class)
    @PreAuthorize("hasAuthority('/projectDev/devTaskList')")
    public List<DevTask> devTaskList(@RequestParam Integer requireId) {
        return devTaskService.list(new QueryWrapper<DevTask>().eq(requireId != null, "require_id", requireId));
    }

    @ApiOperationSupport(order = 13)
    @PostMapping("/deleteDevTask")
    @ApiOperation(value = "删除开发任务", notes = "删除id对应信息")
    @PreAuthorize("hasAuthority('/projectDev/deleteDevTask')")
    public boolean deleteDevTask(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        if (taskFeedbackService.selectListByTaskId(new TaskFeedback().setDevTaskId(id)).size() > 0)
            throw new IllegalArgumentException("该开发任务已填写反馈，不允许删除");

        DevTask db_task = devTaskService.getById(id);
        if (db_task == null) {
            throw new IllegalArgumentException("要删除的开发任务不存在");
        }

        devTaskService.removeById(id);

        //重新计算需求的已发生工时、进度、实际开始时间、实际结束时间、需求状态
        devRequirementService.computeRequirementStatus(db_task.getRequireId());
        return true;
    }

    @GetMapping("/getTaskFeedbacks")
    @ApiOperation(value = "查询任务反馈", notes = "查询指定任务的反馈列表")
    @ApiOperationSupport(order = 15)
    @PreAuthorize("hasAuthority('/projectDev/getTaskFeedbacks')")
    public List<TaskFeedback> getTaskFeedbacks(@NotNull(message = "id不能为空") @RequestParam Integer devTaskId) {
        return taskFeedbackService.selectListByTaskId(new TaskFeedback().setDevTaskId(devTaskId));
    }

    @ApiOperationSupport(order = 16)
    @PostMapping("/addDevInterface")
    @ApiOperation(value = "添加关联接口", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/addDevInterface')")
    public Boolean addDevInterface(@Valid @RequestBody DevInterface devInterface) {
        if (devInterfaceService.list(
                new QueryWrapper<DevInterface>()
                        .eq("name", devInterface.getName())
                        .eq("require_id", devInterface.getRequireId())).size() > 0) {
            throw new IllegalArgumentException("该需求下已有同名接口存在");
        }

        if (devRequirementService.getById(devInterface.getRequireId()) == null) {
            throw new IllegalArgumentException("接口所属需求不存在");
        }

        devInterface.setMaintainer(RequestHolder.getUserInfo().getUserName());
        devInterfaceService.save(devInterface);
        return true;
    }

    @ApiOperationSupport(order = 11)
    @PostMapping("/editDevInterface")
    @ApiOperation(value = "修改关联接口")
    @PreAuthorize("hasAuthority('/projectDev/editDevInterface')")
    public DevInterface editDevInterface(@Valid @RequestBody DevInterface devInterface) {
        if (!devInterfaceService.updateById(devInterface)) {
            throw new IllegalArgumentException("要修改的关联接口不存在！");
        }
        return devInterface;
    }

    @ApiOperationSupport(order = 12)
    @GetMapping("/DevInterfaceList")
    @ApiOperation(value = "查询关联接口", notes = "返回需求id对应的关联接口,参数为空则查询全部关联接口")
    @PreAuthorize("hasAuthority('/projectDev/DevInterfaceList')")
    public List<DevInterface> devInterfaceList(@RequestParam Integer requireId) {
        return devInterfaceService.list(new QueryWrapper<DevInterface>().eq(requireId != null, "require_id", requireId));
    }

    @ApiOperationSupport(order = 13)
    @PostMapping("/deleteDevInterface")
    @ApiOperation(value = "删除关联接口", notes = "删除id对应信息")
    @PreAuthorize("hasAuthority('/projectDev/deleteDevInterface')")
    public boolean deleteDevInterface(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        if (!devInterfaceService.removeById(id)) {
            throw new IllegalArgumentException("要删除的关联接口id不存在");
        }
        return true;
    }

    private TaskStatusEnum getDevStatus(Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            return null;
        if (new Date().before(startDate))
            return TaskStatusEnum.UN_START;
        else if (startDate.before(new Date()) && new Date().before(endDate))
            return TaskStatusEnum.RUNNING;
        else
            return TaskStatusEnum.DELAYED;
    }


}
