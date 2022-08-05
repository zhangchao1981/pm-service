package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.env.EnvHardware;
import com.iscas.pm.api.model.env.EnvInformation;
import com.iscas.pm.api.model.env.EnvSoftware;
import com.iscas.pm.api.service.EnvHardwareService;
import com.iscas.pm.api.service.EnvInformationService;
import com.iscas.pm.api.service.EnvSoftwareService;
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

/**
 * @author by  lichang
 * @date 2022/8/2.
 */
@RestController
@Api(tags = {"项目环境"})
@ApiSort(4)
@RequestMapping("/projectEnv")
public class EnvController {
    @Autowired
    EnvInformationService envInformationService;
    @Autowired
    EnvSoftwareService envSoftwareService;
    @Autowired
    EnvHardwareService envHardwareService;

    @ApiOperationSupport(order = 1)
    @PostMapping("/addEnvInformation")
    @ApiOperation(value = "添加环境说明", notes = "")
    @PreAuthorize("hasAuthority('/projectEnv/addEnvInformation')")
    public Boolean addEnvInformation(@Valid @RequestBody EnvInformation envInformation) {
        return envInformationService.save(envInformation);
    }

    @ApiOperationSupport(order = 2)
    @PostMapping("/editEnvInformation")
    @ApiOperation(value = "修改环境说明", notes = "")
    @PreAuthorize("hasAuthority('/projectEnv/editEnvInformation')")
    public boolean editEnvInformation(@Valid @RequestBody EnvInformation envInformation) {
        return envInformationService.updateById(envInformation);
    }

    @ApiOperationSupport(order = 3)
    @PostMapping("/EnvInformationList")
    @ApiOperation(value = "查询环境说明", notes = "")
    @PreAuthorize("hasAuthority('/projectEnv/EnvInformationList')")
    public List<EnvInformation> envInformationList() {
        return envInformationService.list();
    }

    @ApiOperationSupport(order = 4)
    @PostMapping("/deleteEnvInformation")
    @ApiOperation(value = "删除环境说明", notes = "删除id对应信息")
    @PreAuthorize("hasAuthority('/projectEnv/deleteEnvInformation')")
    public boolean deleteEnvInformation(@NotNull(message = "id不能为空") @RequestParam  Integer id) {
        return envInformationService.removeById(id);
    }

    @ApiOperationSupport(order = 5)
    @PostMapping("/addEnvSoftware")
    @ApiOperation(value = "添加软件环境需求", notes = "")
    @PreAuthorize("hasAuthority('/projectEnv/addEnvSoftware')")
    public Boolean addEnvSoftware(@Valid @RequestBody EnvSoftware envSoftware) {
        return envSoftwareService.save(envSoftware);
    }

    @ApiOperationSupport(order = 6)
    @PostMapping("/editEnvSoftware")
    @ApiOperation(value = "修改软件环境需求", notes = "")
    @PreAuthorize("hasAuthority('/projectEnv/editEnvSoftware')")
    public boolean editEnvSoftware(@Valid @RequestBody EnvSoftware envSoftware) {
        return envSoftwareService.updateById(envSoftware);
    }

    @ApiOperationSupport(order = 7)
    @PostMapping("/envSoftwareList")
    @ApiOperation(value = "查询软件环境需求", notes = "")
    @PreAuthorize("hasAuthority('/projectEnv/EnvSoftwareList')")
    public List<EnvSoftware> envSoftwareList() {
        return envSoftwareService.list();
    }

    @ApiOperationSupport(order = 8)
    @PostMapping("/deleteEnvSoftware")
    @ApiOperation(value = "删除软件环境需求", notes = "删除id对应信息")
    @PreAuthorize("hasAuthority('/projectEnv/deleteEnvHardware')")
    public boolean deleteEnvSoftware(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        return   envSoftwareService.removeById(id);
    }


    @ApiOperationSupport(order = 9)
    @PostMapping("/addEnvHardware")
    @ApiOperation(value = "添加硬件环境需求", notes = "")
    @PreAuthorize("hasAuthority('/projectEnv/addEnvHardware')")
    public Boolean addEnvHardware(@Valid @RequestBody EnvHardware envHardware) {
        return envHardwareService.save(envHardware);
    }

    @ApiOperationSupport(order = 10)
    @PostMapping("/editEnvHardware")
    @ApiOperation(value = "修改硬件环境需求", notes = "")
    @PreAuthorize("hasAuthority('/projectEnv/editEnvHardware')")
    public boolean editEnvHardware(@Valid @RequestBody EnvHardware envHardware) {
        return envHardwareService.updateById(envHardware);
    }

    @ApiOperationSupport(order = 11)
    @PostMapping("/envHardwareList")
    @ApiOperation(value = "查询硬件环境需求", notes = "")
    @PreAuthorize("hasAuthority('/projectEnv/EnvHardwareList')")
    public List<EnvHardware> envHardwareList() {
        return envHardwareService.list();
    }

    @ApiOperationSupport(order = 12)
    @PostMapping("/deleteEnvHardware")
    @ApiOperation(value = "删除硬件环境需求", notes = "删除id对应信息")
    @PreAuthorize("hasAuthority('/projectEnv/deleteEnvHardware')")
    public boolean deleteEnvHardware(@NotNull(message = "id不能为空") @RequestParam Integer id) {
        return    envHardwareService.removeById(id);
    }
}
