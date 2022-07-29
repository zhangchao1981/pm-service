package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.DirectoryMapper;
import com.iscas.pm.api.model.project.Directory;
import com.iscas.pm.api.service.DirectoryService;
import io.micrometer.shaded.org.pcollections.Empty;
import io.netty.util.internal.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
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
        //问题：  目录重名
        QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null!=id,"id",id);
        queryWrapper.like(!StringUtils.isEmpty(name),"name",name);

        return list2Tree(directoryMapper.selectList(queryWrapper));
    }

    @Override
    public Directory addDirectory(Directory directory) {
        Integer id = directory.getId();
        String name = directory.getName();
        Integer parentId = directory.getParentId();
        //判断是否有重名、parentId不存在的情况
        QueryWrapper<Directory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        queryWrapper.eq(parentId==null,"parent_id",parentId);
        if (null!=directoryMapper.selectOne(queryWrapper)){
            throw new IllegalArgumentException("所需添加目录名重复或父节点错误");
        }
        directoryMapper.insert(directory);
        return directory;
    }

    @Override
    public List<Directory> deleteDirectory(Integer id, String name) {


        return null;
    }















    //sourceList 平铺的原始数据集合
    public List<Directory> list2Tree(List<Directory> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        List<Directory> resultList = new ArrayList<>();
        //将数据封装成树形结构
        Map<Integer, Directory> map = new HashMap<>();
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




