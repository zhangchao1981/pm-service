package com.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mapper.PlanMapper;
import com.demo.model.project.Plan;
import com.demo.service.PlanService;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【plan】的数据库操作Service实现
* @createDate 2022-07-28 17:13:09
*/
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan>
    implements PlanService {

}




