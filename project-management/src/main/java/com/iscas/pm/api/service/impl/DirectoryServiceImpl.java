package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.doc.DirectoryMapper;
import com.iscas.pm.api.model.doc.Directory;
import com.iscas.pm.api.service.DirectoryService;
import com.iscas.pm.common.core.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 66410
 * @description 针对表【doc_directory】的数据库操作Service实现
 * @createDate 2022-07-28 18:21:01
 */
@Service
public class DirectoryServiceImpl extends ServiceImpl<DirectoryMapper, Directory> implements DirectoryService {
    @Autowired
    DirectoryMapper directoryMapper;

    @Override
    public List<Directory> getDirectoryTree() {
        QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
        List<Directory> directoryList = directoryMapper.selectList(queryWrapper);
        return TreeUtil.treeOut(directoryList, Directory::getId, Directory::getParentId, Directory::getChildren);
    }

    @Override
    public Directory addDirectory(Directory directory) {
        Integer parentId = directory.getParentId();
        Integer id = directory.getId();
        String name = directory.getName();

        //名字、id重复的校验
        QueryWrapper<Directory> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name).or().eq("id", id);
        if (directoryMapper.selectList(wrapper).size() > 0) {
            throw new IllegalArgumentException("名称或id重复");
        }

        //判断parentId不存在的情况
        if (null != parentId && 0 != parentId) {
            QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", parentId);
            if (1 > directoryMapper.selectList(queryWrapper).size()) {
                throw new IllegalArgumentException("父节点id不存在");
            }
        } else {
            //父节点默认置0
            parentId = 0;
        }
        directoryMapper.insert(directory);
        return directory;
    }

    @Override
    public boolean deleteDirectory(Integer id) {
        QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).or().eq("parent_id", id);
        if (directoryMapper.selectList(queryWrapper).size() > 1) {
            //有子节点，不能删
            throw new IllegalArgumentException("有子节点存在，拒绝删除");
        }
        if (directoryMapper.delete(queryWrapper) == 0) {
            throw new IllegalArgumentException("该目录已被删除");
        }
        return true;
    }

    @Override
    public Directory editDirectory(Directory directory) {

        Integer parentId = directory.getParentId();
        //验证是否有效：  判断父id是否存在
        if (parentId == null) {
            directory.setParentId(0);
        } else if (parentId != 0) {
            String name = directory.getName();
            QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", parentId);
            if (1 > directoryMapper.selectList(queryWrapper).size()) {
                throw new IllegalArgumentException("父id不存在");
            }
        }

        //进行更新，更新失败(id不存在)抛出异常
        if (1 > directoryMapper.updateById(directory)) {
            throw new IllegalArgumentException("要修改目录id不存在");
        }
        return directory;
    }

}




