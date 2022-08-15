package com.iscas.pm.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.auth.model.Department;

import java.util.List;

/**
* @author 66410
* @description 针对表【auth_department(部门表)】的数据库操作Service
* @createDate 2022-08-15 15:27:03
*/
public interface DepartmentService extends IService<Department> {

    List<Department> getDepartmentTree();

    Department addDepartment(Department department);

    Department editDepartment(Department department);
}
