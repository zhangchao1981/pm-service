package com.iscas.pm.api.mapper.test;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.test.TestUseCase;
import com.iscas.pm.api.model.test.param.UseCaseForPlanQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 66410
* @description 针对表【test_use_case(测试用例表)】的数据库操作Mapper
* @createDate 2022-08-10 10:25:39
* @Entity test.TestUseCase
*/
@Mapper
public interface TestUseCaseMapper extends BaseMapper<TestUseCase> {
//    @Select("SELECT * "
//            +"FROM dev_modular,test_use_case"+
//            "<if test=\"param.modularList !=null and param.modularList.size()>0 \">"+
//            " INNER JOIN  dev_modular "
//            +"ON dev_modular.id in param.modularList "+
//            "</if>"+
//            "<if test=\"param.creatorId !=null and param.creatorId!='' \" >"+
//            "WHERE  creator_id =#{param.creatorId} " +
//            "</if>"+
//            "<if test=\"param.getUseCaseType() !=null and param.getUseCaseType()!='' \" >"+
//            "WHERE  type =#{param.useCaseType} " +
//            "</if>"+
//            "<if test=\"param.getUseCaseType() !=null and param.getUseCaseType()!='' \" >"+
//            "WHERE  type =#{param.useCaseType} " +
//            "</if>"+
//            "")


        //        List<Integer> modularList = useCaseForPlanQueryParam.getModularList();
//        String titleOrId = useCaseForPlanQueryParam.getTitleOrId();
//        Integer creatorId = useCaseForPlanQueryParam.getCreatorId();
//        QueryWrapper<TestUseCase> wrapper = new QueryWrapper<TestUseCase>()
//                .in(modularList.size()>0, "modular_id", modularList)
//                .eq(creatorId !=null,"creator_id",creatorId)
//                .eq(StringUtils.isNotBlank(useCaseForPlanQueryParam.getUseCaseType()),"type",useCaseForPlanQueryParam.getUseCaseType())
//                .and(StringUtils.isNotBlank(titleOrId),q->q.eq(isNumeric(titleOrId), "id", org.springframework.util.StringUtils.isEmpty(titleOrId)||!isNumeric(titleOrId)?null:Integer.valueOf(titleOrId))
//                        .or().like("title", titleOrId));
//        return testUseCaseMapper.selectList(wrapper);

    List<TestUseCase> testUseCaseListForPlan(@Param("param") UseCaseForPlanQueryParam param);


}




