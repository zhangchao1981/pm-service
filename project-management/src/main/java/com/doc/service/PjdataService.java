package com.doc.service;

import com.doc.domain.Pjdata;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 66410
* @description 针对表【pjdata】的数据库操作Service
* @createDate 2022-07-04 13:44:25
*/
public interface PjdataService extends IService<Pjdata> {

    boolean createword(int id, String filename);
}
