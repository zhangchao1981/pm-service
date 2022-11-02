package com.iscas.pm.api.service.impl.dev;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.dev.DevModularMapper;
import com.iscas.pm.api.model.dev.DevModular;
import com.iscas.pm.api.service.DevModularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lichang
 * @description 针对表【dev_modular(项目模块表)】的数据库操作Service实现
 * @createDate 2022-08-03 16:10:22
 */
@Service
public class DevModularServiceImpl extends ServiceImpl<DevModularMapper, DevModular>
        implements DevModularService {
    @Autowired
    DevModularMapper devModularMapper;

    @Override
    public DevModular modularValidCheck(DevModular devModular) {
        //同级重名校验+父id有效校验
        Integer parentId = devModular.getParentId();
        if (parentId != 0) {
            if (devModularMapper.selectList(new QueryWrapper<DevModular>().eq("id", parentId)).size() < 1) {
                throw new IllegalArgumentException("父节点id不存在");
            }
        }
        //同级不能重名  添加重名校验
        if (devModularMapper.selectList(new QueryWrapper<DevModular>().eq("parent_id", parentId).eq("name", devModular.getName()).ne("id",devModular.getId())).size() > 0) {
            throw new IllegalArgumentException("模块名重复");
        }
        return devModular;
    }

}




