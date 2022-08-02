package com.iscas.pm.api.mapper.environment;


import com.iscas.pm.api.model.environment.EnvInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【env_information(环境资源表)】的数据库操作Mapper
* @createDate 2022-08-02 14:15:08
* @Entity mapper/environment.model/project.EnvInformation
*/
@Mapper
public interface EnvInformationMapper extends BaseMapper<EnvInformation> {

}




