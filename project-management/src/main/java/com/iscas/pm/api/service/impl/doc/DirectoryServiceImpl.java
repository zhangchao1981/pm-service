package com.iscas.pm.api.service.impl.doc;

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

        if (directoryMapper.selectList(new QueryWrapper<Directory>().eq("name", directory.getName()).eq("parent_id",directory.getParentId())).size() > 0) {
            throw new IllegalArgumentException("同级目录下，目录名称不能重复");
        }

        Integer parentId = directory.getParentId();
        if (0 != parentId && 1 > directoryMapper.selectList(new QueryWrapper<Directory>().eq("id", parentId)).size()) {
            throw new IllegalArgumentException("父节点id不存在");
        }

        directoryMapper.insert(directory);
        return directory;
    }



    @Override
    public Directory editDirectory(Directory directory) {
        Integer parentId = directory.getParentId();
        //验证是否有效：  判断父id是否存在
        if (parentId != 0) {
            if (1 > directoryMapper.selectList( new QueryWrapper<Directory>().eq("id", parentId)).size()) {
                throw new IllegalArgumentException("父id对应目录不存在");
            }
        }
        //判断父id是否和自身id相同
        if (parentId.equals(directory.getParentId())){
            throw new IllegalArgumentException("父id与自身id相同，死循环");
        }



        //进行更新，更新失败(id不存在)抛出异常
        if (1 > directoryMapper.updateById(directory)) {
            throw new IllegalArgumentException("要修改目录id不存在");
        }
        return directory;
    }

}




