package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.project.Directory;

import java.util.List;

/**
* @author 66410
* @description 针对表【doc_directory】的数据库操作Service
* @createDate 2022-07-28 18:21:01
*/
public interface DirectoryService extends IService<Directory> {

    List<Directory> getDirectoryTree(Integer id, String name);

    Directory addDirectory(Directory directory);

    List<Directory> deleteDirectory(Integer id, String name);

    Directory editDirectory(Directory directory);
}
