package com.iscas.pm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.auth.model.Department;
import org.apache.ibatis.annotations.Mapper;


/**
* @author 66410
* @description 针对表【auth_department(部门表)】的数据库操作Mapper
* @createDate 2022-08-15 15:27:03
* @Entity generate.AuthDepartment
*/
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

}




