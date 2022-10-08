package com.iscas.pm.api.service.impl.dev;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.dev.DevRequirementMapper;
import com.iscas.pm.api.mapper.dev.DevTaskMapper;
import com.iscas.pm.api.model.dev.DevRequirement;
import com.iscas.pm.api.model.dev.DevTask;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.service.DevRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 66410
 * @description 针对表【dev_requirement(开发需求表)】的数据库操作Service实现
 * @createDate 2022-08-03 11:21:48
 */
@Service
public class DevRequirementServiceImpl extends ServiceImpl<DevRequirementMapper, DevRequirement> implements DevRequirementService {
}




