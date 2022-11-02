package com.iscas.pm.api.service.impl.dev;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.dev.DevInterfaceMapper;
import com.iscas.pm.api.model.dev.DevInterface;
import com.iscas.pm.api.service.DevInterfaceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lichang
 * @description 针对表【dev_interface(关联接口表)】的数据库操作Service实现
 * @createDate 2022-08-16 10:01:11
 */
@Service
public class DevInterfaceServiceImpl extends ServiceImpl<DevInterfaceMapper, DevInterface>
        implements DevInterfaceService {
    @Autowired
    DevInterfaceMapper devInterfaceMapper;

    @Override
    public List<DevInterface> devInterfaceListByType(String type) {
        return  devInterfaceMapper.selectList(new QueryWrapper<DevInterface>().eq(StringUtils.isNotBlank(type),"type",type));
    }
}




