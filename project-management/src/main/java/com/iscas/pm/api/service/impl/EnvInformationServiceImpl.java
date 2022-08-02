package com.iscas.pm.api.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.model.env.EnvInformation;
import com.iscas.pm.api.mapper.env.EnvInformationMapper;
import com.iscas.pm.api.service.EnvInformationService;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【env_information(环境资源表)】的数据库操作Service实现
* @createDate 2022-08-02 14:15:08
*/
@Service
public class EnvInformationServiceImpl extends ServiceImpl<EnvInformationMapper, EnvInformation>
    implements EnvInformationService {
}




