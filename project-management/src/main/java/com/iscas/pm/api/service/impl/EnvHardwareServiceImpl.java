package com.iscas.pm.api.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.environment.EnvHardwareMapper;
import com.iscas.pm.api.model.environment.EnvHardware;
import com.iscas.pm.api.service.EnvHardwareService;
import org.springframework.stereotype.Service;

/**
 * @author by  lichang
 * @date 2022/8/2.
 * @description 针对表【env_hardware(硬件环境需求表)】的数据库操作Service实现
 * @createDate 2022-08-02 14:25:03
 */
@Service
public class EnvHardwareServiceImpl extends ServiceImpl<EnvHardwareMapper, EnvHardware>
        implements EnvHardwareService {

}