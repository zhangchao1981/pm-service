package com.iscas.pm.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.mapper.DepartmentMapper;
import com.iscas.pm.auth.model.Department;
import com.iscas.pm.auth.service.DepartmentService;

import com.iscas.pm.common.core.util.TreeUtil;
import org.apache.tomcat.jni.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 66410
* @description 针对表【auth_department(部门表)】的数据库操作Service实现
* @createDate 2022-08-15 15:27:03
*/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
    implements DepartmentService {
@Autowired
DepartmentMapper departmentMapper;


    @Override
    public List<Department> getDepartmentTree() {
            QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
            List<Department> departmentList = departmentMapper.selectList(queryWrapper);
            return TreeUtil.treeOut(departmentList, Department::getId, Department::getParentId, Department::getChildren);
    }

    @Override
    public Department addDepartment(Department department) {

        if (departmentMapper.selectList(new QueryWrapper<Department>().eq("name", department.getName()).eq("parent_id",department.getParentId())).size() > 0) {
            throw new IllegalArgumentException("同级目录下，目录名称不能重复");
        }
        Integer parentId = department.getParentId();
        if (0 != parentId && 1 > departmentMapper.selectList(new QueryWrapper<Department>().eq("id", parentId)).size()) {
            throw new IllegalArgumentException("父节点id不存在");
        }

        departmentMapper.insert(department);
        return department;
    }

    @Override
    public Department editDepartment(Department department) {
        Integer parentId = department.getParentId();
        //验证是否有效：  判断父id是否存在
        if (parentId != 0) {
            if (1 > departmentMapper.selectList( new QueryWrapper<Department>().eq("id", parentId)).size()) {
                throw new IllegalArgumentException("父id对应目录不存在");
            }
        }
        //判断父id是否和自身id相同
        if (parentId.equals(department.getParentId())){
            throw new IllegalArgumentException("不允许父id与自身id相同");
        }


        //进行更新，更新失败(id不存在)抛出异常
        if (1 > departmentMapper.updateById(department)) {
            throw new IllegalArgumentException("要修改目录id不存在");
        }
        return department;
    }
}




