package com.iscas.pm.api.model.test.param;

import com.iscas.pm.api.model.test.enums.BugTypeEnum;
import lombok.Data;

/**
 * @author by  lichang
 * @date 2022/11/3.
 */
@Data
public class PlanBugStatisticParam {

    private  Integer id;

    private  Integer bugAmount;
}
