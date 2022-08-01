package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.DirectoryMapper;
import com.iscas.pm.api.model.project.Directory;
import com.iscas.pm.api.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
* @author 66410
* @description 针对表【doc_directory】的数据库操作Service实现
* @createDate 2022-07-28 18:21:01
*/
@Service
public class DirectoryServiceImpl extends ServiceImpl<DirectoryMapper, Directory>
    implements DirectoryService {
@Autowired
DirectoryMapper directoryMapper;
    @Override
    public List<Directory> getDirectoryTree(Integer id, String name) {

        //查找id为 id或者parentid=id的所有目录
        QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null!=id,"id",id).or()
                .eq(null!=id,"parent_id",id);
        queryWrapper.like(!StringUtils.isEmpty(name),"name",name);
        //如果只输目录名，子目录查不出来？
        return list2Tree(directoryMapper.selectList(queryWrapper));
    }

    @Override
    public Directory addDirectory(Directory directory) {
        Integer parentId = directory.getParentId();
        //判断parentId不存在的情况
        //父节点id 查出来为空则报错  or 名字查出来不为空则报错
        //重名问题--->mysql约束解决
        if(null==parentId){
            QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",parentId);
            if (1>directoryMapper.selectList(queryWrapper).size()){
                throw new IllegalArgumentException("父节点id不存在");
            }
        }
        directoryMapper.insert(directory);
        return directory;
    }

    @Override
    public List<Directory> deleteDirectory(Integer id, String name) {
        List<Directory> directoryTree = getDirectoryTree(id, name);
        QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
        //问题：删目录时 id(name)是否可以为空
        queryWrapper.eq("id",id).or().eq("parent_id",id);
        queryWrapper.eq(!StringUtils.isEmpty(name),"name",name);
        directoryMapper.delete(queryWrapper);
        return null;
    }

    @Override
    public Directory editDirectory(Directory directory) {

        Integer parentId = directory.getParentId();
        //验证是否有效：  判断父id是否存在
        String name = directory.getName();
        QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(parentId !=null,"id",parentId);
       if (1>directoryMapper.selectList(queryWrapper).size()){
           throw new IllegalArgumentException("父id不存在");
       }
        //id不存在返回0
        if (1>directoryMapper.update(directory,null)){
            throw new IllegalArgumentException("要修改目录id不存在");
        }
        return directory;
    }


    //sourceList 平铺的原始数据集合
    public List<Directory> list2Tree(List<Directory> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        List<Directory> resultList = new ArrayList<>();
        //将数据封装成树形结构
        Map<Integer, Directory> map = new HashMap<>(4);
        for (Directory data : sourceList) {
            map.put(data.getId(), data);
        }
        for (Directory data : sourceList) {
            Directory obj = map.get(data.getParentId());
            if (obj != null) {
                List<Directory> children = obj.getChildren();
                if (children == null || children.isEmpty()) {
                    children = new ArrayList<>();
                }
                children.add(data);
                obj.setChildren(children);

            } else {
                resultList.add(data);
            }

        }
        return resultList;
    }



}



