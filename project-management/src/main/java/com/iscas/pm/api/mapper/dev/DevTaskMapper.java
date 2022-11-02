package com.iscas.pm.api.mapper.dev;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.dev.DevTask;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lichang
* @description 针对表【dev_task(开发任务表)】的数据库操作Mapper
* @createDate 2022-08-03 16:12:06
* @Entity test.DevTask
*/
@Mapper
public interface DevTaskMapper extends BaseMapper<DevTask> {

}




