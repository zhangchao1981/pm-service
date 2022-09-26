package com.iscas.pm.api.service.impl.dev;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.dev.DevRequirementMapper;
import com.iscas.pm.api.mapper.dev.DevTaskMapper;
import com.iscas.pm.api.model.dev.DevRequirement;
import com.iscas.pm.api.model.dev.DevTask;
import com.iscas.pm.api.service.DevRequirementService;
import com.iscas.pm.api.service.DevTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【dev_task(开发任务表)】的数据库操作Service实现
* @createDate 2022-08-03 16:12:07
*/
@Service
public class DevTaskServiceImpl extends ServiceImpl<DevTaskMapper, DevTask>
    implements DevTaskService {
    @Autowired
    DevTaskMapper devTaskMapper;
    @Autowired
    DevRequirementService devRequirementService;

    @Override
    public Boolean addDevTask(DevTask devTask) {

       if ( devRequirementService.list(new QueryWrapper<DevRequirement>().eq("id", devTask.getRequireId())).size() < 1) {
            throw new IllegalArgumentException("父模块Id不存在");
        }
        if (devTaskMapper.insert(devTask)<1)
            return false;
        return true;


    }
}




