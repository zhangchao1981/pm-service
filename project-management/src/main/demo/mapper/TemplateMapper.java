package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.model.project.Template;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 李昶
* @description 针对表【template】的数据库操作Mapper
* @createDate 2022-07-28 18:18:33
* @Entity .model/project.Template
*/
@Mapper
public interface TemplateMapper extends BaseMapper<Template> {

}




