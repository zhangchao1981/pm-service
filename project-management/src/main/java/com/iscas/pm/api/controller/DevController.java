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
    @ApiOperation(value = "添加项目模块", notes = "内含模块名重复校验")
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
    @ApiOperation(value = "项目模块列表", notes = "返回全部项目模块(树型),返回List<DevModular>",response=DevModular.class)
    @PreAuthorize("hasAuthority('/projectDev/DevModularList')")
    public List<DevModular> devModularList() {
        return TreeUtil.treeOut(devModularService.list(), DevModular::getId, DevModular::getParentId, DevModular::getChildren);
    }

    @ApiOperationSupport(order = 14)
    @PostMapping("/devChildModularList")
    @ApiOperation(value = "子项目模块列表", notes = "返回模块id下面的子模块集合(只递进一层),返回List<DevModular>",response=DevModular.class)
    @PreAuthorize("hasAuthority('/projectDev/devChildModularList')")
    public List<DevModular> devChildModularList(@NotNull(message = "id不能为空")@RequestParam Integer id) {
        return devModularService.list(new QueryWrapper<DevModular>().eq("parent_id",id));
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
    @PostMapping("/addEnvInformation")
    @ApiOperation(value = "添加开发需求", notes = "开发需求允许重名")
    @PreAuthorize("hasAuthority('/projectDev/addDevRequirement')")
    public Boolean addDevRequirement(@Valid @RequestBody DevRequirement devRequirement) {
        //进行父模块Id有效校验
        if (devModularService.list(new QueryWrapper<DevModular>().eq("id", devRequirement.getModularId())).size() < 1) {
            throw new IllegalArgumentException("父模块Id不存在");
        }

        devRequirement.setCreateTime(new Date());
        devRequirement.setUpdateTime(new Date());
        devRequirement.setStatus(getStatus(devRequirement.getStartDate(), devRequirement.getEndDate()));
        devRequirementService.save(devRequirement);
        return true;
    }

    @ApiOperationSupport(order = 6)
    @PostMapping("/editDevRequirement")
    @ApiOperation(value = "修改开发需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/editDevRequirement')")
    public Boolean editDevRequirement(@Valid @RequestBody DevRequirement devRequirement) {
        //进行父模块Id有效校验
        if (devModularService.list(new QueryWrapper<DevModular>().eq("id", devRequirement.getModularId())).size() < 1) {
            throw new IllegalArgumentException("父模块Id不存在");
        }
        if (!devRequirementService.updateById(devRequirement)) {
            throw new IllegalArgumentException("要修改的开发需求id不存在");
        }
        return true;
    }
    @ApiOperationSupport(order = 7)
    @GetMapping("/devRequirementList")
    @ApiOperation(value = "查询开发需求", notes = "返回开发需求页面的略缩信息,类型为")
    @PreAuthorize("hasAuthority('/projectDev/devRequirementList')")
    public  List<DevRequirement> devRequirementList(@RequestParam @NotNull(message = "modularId不能为空") Integer modularId) {
        return devRequirementService.list(new QueryWrapper<DevRequirement>().eq("modular_id", modularId));
    }

    @ApiOperationSupport(order = 8)
    @GetMapping("/devRequirement")
    @ApiOperation(value = "查询开发需求详情", notes = "基本信息及原型设计图在devRequirement里面,用例说明在useCase里",response=DevRequirement.class)
    @PreAuthorize("hasAuthority('/projectDev/devRequirement')")
    public DevRequirement devRequirement(@RequestParam @NotNull(message = "requirementId不能为空") Integer requirementId) {
        return devRequirementService.getById(requirementId);
    }


    @ApiOperationSupport(order = 9)
    @PostMapping("/deleteDevRequirement")
    @ApiOperation(value = "删除开发需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/deleteDevRequirement')")
    public boolean deleteDevRequirement(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        //如果当前开发需求下有任务，则不许删除
        if (devTaskService.list(new QueryWrapper<DevTask>().eq("require_id", id)).size() > 0) {
            throw new IllegalArgumentException("当前开发需求下有任务，不许删除");
        }
        //如果当前开发需求下有关联接口  ，则不许删除
        if (devTaskService.list(new QueryWrapper<DevTask>().eq("require_id", id)).size() > 0) {
            throw new IllegalArgumentException("当前开发需求下有任务，不许删除");
        }

        if (!devRequirementService.removeById(id)) {
            throw new IllegalArgumentException("要删除的开发需求id不存在");
        }
        return true;
    }

    @ApiOperationSupport(order = 10)
    @PostMapping("/addDevTask")
    @ApiOperation(value = "添加开发任务", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/addDevTask')")
    public Boolean addDevTask(@Valid @RequestBody DevTask devTask) {
        if (devRequirementService.list(new QueryWrapper<DevRequirement>().eq("id", devTask.getRequireId())).size() < 1) {
            throw new IllegalArgumentException("父模块Id不存在");
        }

        devTask.setStatus(getTaskStatus(devTask.getStartDate(), devTask.getEndDate(), devTask.getDevProgress()));
        devTaskService.save(devTask);
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

        devTask.setStatus(getTaskStatus(devTask.getStartDate(), devTask.getEndDate(), db_task.getDevProgress()));
        devTaskService.updateById(devTask);
        return devTask;
    }

    @ApiOperationSupport(order = 12)
    @GetMapping("/devTaskList")
    @ApiOperation(value = "查询开发任务", notes = "返回需求id对应的全部任务 返回值类型为List<DevTask>,需求id为null返回所有任务",response=DevTask.class)
    @PreAuthorize("hasAuthority('/projectDev/devTaskList')")
    public List<DevTask> devTaskList(@RequestParam Integer requireId) {
        return devTaskService.list(new QueryWrapper<DevTask>().eq(requireId != null, "require_id", requireId));
    }

    @ApiOperationSupport(order = 13)
    @PostMapping("/deleteDevTask")
    @ApiOperation(value = "删除开发任务", notes = "删除id对应信息")
    @PreAuthorize("hasAuthority('/projectDev/deleteDevTask')")
    public boolean deleteDevTask(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        if (getTaskFeedbacks(id).size() > 0)
            throw new IllegalArgumentException("该开发任务已填写反馈，不允许删除");

        if (!devTaskService.removeById(id)) {
            throw new IllegalArgumentException("要删除的开发任务id不存在");
        }

        return true;
    }

    @PostMapping("/addTaskFeedback")
    @ApiOperation(value = "添加或修改任务反馈", notes = "添加或修改开发任务完成情况的反馈信息")
    @ApiOperationSupport(order = 14)
    @PreAuthorize("hasAuthority('/projectDev/saveTaskFeedback')")
    public TaskFeedback saveTaskFeedback(@Valid @RequestBody TaskFeedback taskFeedback) {
        taskFeedbackService.saveTaskFeedback(taskFeedback);
        return taskFeedback;
    }

    @GetMapping("/getTaskFeedbacks")
    @ApiOperation(value = "查询任务反馈", notes = "查询指定任务的反馈列表")
    @ApiOperationSupport(order = 15)
    @PreAuthorize("hasAuthority('/projectDev/getTaskFeedbacks')")
    public List<TaskFeedback> getTaskFeedbacks(@NotNull Integer taskId) {
        return taskFeedbackService.selectListByPlanTaskId(new TaskFeedback().setDevTaskId(taskId));
    }

    @ApiOperationSupport(order = 16)
    @PostMapping("/addDevInterface")
    @ApiOperation(value = "添加关联接口", notes = "")
    @PreAuthorize("hasAuthority('/projectDev/addDevInterface')")
    public Boolean addDevInterface(@Valid @RequestBody DevInterface devInterface) {
        //重名校验  同一个需求下的关联接口不允许同名
        if (devInterfaceService.list(
                new QueryWrapper<DevInterface>().eq("name", devInterface.getName()).eq("require_id", devInterface.getRequireId())).size() > 0) {
            throw new IllegalArgumentException("该需求下已有同名接口存在");
        }

        if (devRequirementService.list(new QueryWrapper<DevRequirement>().eq("id", devInterface.getRequireId())).size() < 1) {
            throw new IllegalArgumentException("需求Id对应需求不存在");
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
    public List<DevInterface> DevInterfaceList(@RequestParam Integer requireId) {
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

    private RequireStatusEnum getStatus(Date start, Date end) {
        if (new Date().before(start))
            return RequireStatusEnum.DESIGN;
        else if (start.before(new Date()) && new Date().before(end))
            return RequireStatusEnum.DEVELOPING;
        else
            return RequireStatusEnum.DELAYED;
    }

    private TaskStatusEnum getTaskStatus(Date start, Date end, float progress) {
        //开始时间晚于当前时间
        if (new Date().before(start)) {
            if (progress == 0)
                return TaskStatusEnum.UN_START;
            if (progress > 0 && progress < 100)
                return TaskStatusEnum.RUNNING;
            else
                return TaskStatusEnum.FINISHED;
        }

        //当前时间介于开始时间和结束时间
        else if (start.before(new Date()) && new Date().before(end)) {
            if (progress < 100)
                return TaskStatusEnum.RUNNING;
            else
                return TaskStatusEnum.FINISHED;
        }

        //当前时间晚于结束时间
        else {
            if (progress < 100) {
                return TaskStatusEnum.DELAYED;
            } else {
                return TaskStatusEnum.DELAYED_FINISH;
            }
        }

    }


}
