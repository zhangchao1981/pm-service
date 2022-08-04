package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.dev.DevModular;

import java.util.List;

/**
* @author 66410
* @description 针对表【dev_modular(项目模块表)】的数据库操作Service
* @createDate 2022-08-03 16:10:22
*/
public interface DevModularService extends IService<DevModular> {


    DevModular modularValidCheck(DevModular devModular);
}
