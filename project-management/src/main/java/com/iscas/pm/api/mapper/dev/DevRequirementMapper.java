package com.iscas.pm.api.mapper.dev;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.dev.DevRequirement;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lichang
* @description 针对表【dev_requirement(开发需求表)】的数据库操作Mapper
* @createDate 2022-08-03 11:21:48
* @Entity test.DevRequirement
*/
@Mapper
public interface DevRequirementMapper extends BaseMapper<DevRequirement> {

}




