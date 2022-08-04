package com.iscas.pm.api.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iscas.pm.api.model.dev.DevModular;
import com.iscas.pm.api.model.dev.DevRequirement;
import com.iscas.pm.api.model.dev.DevTask;
import com.iscas.pm.api.model.dev.UseCase;
import com.iscas.pm.api.service.DevModularService;
import com.iscas.pm.api.service.DevRequirementService;
import com.iscas.pm.api.service.DevTaskService;
import com.iscas.pm.common.core.util.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author by  lichang
 * @date 2022/8/3.
 */
@RestController
@Api(tags = {"开发需求"})
@RequestMapping("/projectDev")
@ApiSort(1)
public class DevController {
    @Autowired
    DevRequirementService devRequirementService;
    @Autowired
    DevModularService devModularService;
    @Autowired
    DevTaskService devTaskService;

    @ApiOperationSupport(order = 1)
    @PostMapping("/addEnvInformation")
    @ApiOperation(value = "添加项目模块", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/addDevModular')")
    public Boolean addDevModular(@Valid @RequestBody DevModular devModular) {
        return devModularService.save(devModularService.modularValidCheck(devModular));
    }

    @ApiOperationSupport(order = 2)
    @PostMapping("/editDevModular")
    @ApiOperation(value = "修改项目模块", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/editDevModular')")
    public boolean editDevModular(@Valid @RequestBody DevModular devModular) {
        return devModularService.updateById(devModularService.modularValidCheck(devModular));
    }


    @ApiOperationSupport(order = 3)
    @PostMapping("/DevModularList")
    @ApiOperation(value = "查询项目模块列表", notes = "返回全部项目模块(树型)")
    @PreAuthorize("hasAuthority('/projectDoc/DevModularList')")
    public List<DevModular> devModularList() {
        return TreeUtil.treeOut(devModularService.list(), DevModular::getId, DevModular::getParentId, DevModular::getChildren);
    }

    @ApiOperationSupport(order = 4)
    @PostMapping("/deleteDevModular")
    @ApiOperation(value = "删除项目模块", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteDevModular')")
    public boolean deleteDevModular(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        //如果当前模块下有需求，则不许删除
        QueryWrapper<DevRequirement> devRequireWrapper = new QueryWrapper<>();
        devRequireWrapper.eq("modular_id", id);
        if (devRequirementService.list(devRequireWrapper).size() > 1) {
            throw new IllegalArgumentException("当前模块下有需求，不许删除");
        }
        return devModularService.removeById(id);
    }


    @ApiOperationSupport(order = 5)
    @PostMapping("/addEnvInformation")
    @ApiOperation(value = "添加开发需求", notes = "请求参数:DevRequirement  UseCase ")
    @PreAuthorize("hasAuthority('/projectDoc/addDevRequirement')")
    public DevRequirement addDevRequirement(@Valid @RequestBody Map<String, Object> requestmap) {
        //需测试：  boolearn转tinyint映射   JSON字符串存储格式
        DevRequirement devRequirement = (DevRequirement) requestmap.get("DevRequirement");
        UseCase useCase = (UseCase) requestmap.get("UseCase");
        //进行父模块Id不存在校验  (有外键了，是否还需要校验[控制返回的异常])
        QueryWrapper<DevModular> devModularQueryWrapper = new QueryWrapper<>();
        devModularQueryWrapper.eq("id",devRequirement.getModularId());
        if (devModularService.list(devModularQueryWrapper).size()<1) {
            throw new IllegalArgumentException("父模块Id不存在");
        }
        //是否加入重名校验
        devRequirement.setUseCase(JSON.toJSONString(useCase));
        devRequirementService.save(devRequirement);
        return devRequirement;
    }

    @ApiOperationSupport(order = 6)
    @PostMapping("/editDevRequirement")
    @ApiOperation(value = "修改开发需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/editDevRequirement')")
    public boolean editDevRequirement(@Valid @RequestBody Map<String, Object> requestmap) {
        DevRequirement devRequirement = (DevRequirement) requestmap.get("DevRequirement");
        UseCase useCase = (UseCase) requestmap.get("UseCase");
        devRequirement.setUseCase(JSON.toJSONString(useCase));
        return devRequirementService.updateById(devRequirement);
    }

    @ApiOperationSupport(order = 7)
    @PostMapping("/DevRequirementList")
    @ApiOperation(value = "查询开发需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/DevRequirementList')")
    public List<DevRequirement> devRequirementList(@RequestParam Integer modularId) {
        QueryWrapper<DevRequirement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("modular_id", modularId);
        return devRequirementService.list(queryWrapper);
    }

    @ApiOperationSupport(order = 8)
    @PostMapping("/deleteDevRequirement")
    @ApiOperation(value = "删除开发需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteDevRequirement')")
    public boolean deleteDevRequirement(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        //如果当前开发需求下有任务，则不许删除
        QueryWrapper<DevTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("require_id", id);
        if (devTaskService.list(queryWrapper).size() > 1) {
            throw new IllegalArgumentException("当前开发需求下有任务，不许删除");
        }
        return devRequirementService.removeById(id);
    }

    @ApiOperationSupport(order = 9)
    @PostMapping("/addDevTask")
    @ApiOperation(value = "添加开发任务", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/addDevTask')")
    public Boolean addDevTask(@Valid @RequestBody DevTask devTask) {
        QueryWrapper<DevRequirement> wrapper = new QueryWrapper<>();
        wrapper.eq("id",devTask.getRequireId());
        if (devRequirementService.list(wrapper).size()<1) {
            throw new IllegalArgumentException("父模块Id不存在");
        }
        return devTaskService.save(devTask);
    }

    @ApiOperationSupport(order = 6)
    @PostMapping("/editDevTask")
    @ApiOperation(value = "修改开发任务", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/editDevTask')")
    public boolean editDevTask(@Valid @RequestBody DevTask devTask) {

        return devTaskService.updateById(devTask);
    }

    @ApiOperationSupport(order = 7)
    @PostMapping("/DevTaskList")
    @ApiOperation(value = "查询开发任务", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/DevTaskList')")
    public List<DevTask> DevTaskList(@RequestParam Integer requireId) {
        QueryWrapper<DevTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("require_id", requireId);
        return devTaskService.list(queryWrapper);
    }

    @ApiOperationSupport(order = 8)
    @PostMapping("/deleteDevTask")
    @ApiOperation(value = "删除开发任务", notes = "删除id对应信息")
    @PreAuthorize("hasAuthority('/projectDoc/deleteEnvHardware')")
    public boolean deleteDevTask(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        return devTaskService.removeById(id);
    }


}