package com.iscas.pm.api.model.test;

import lombok.Data;

/**
 * @Author： zhangchao
 * @Date： 2022/12/7
 * @Description：
 */
@Data
public class BugStatistics {
    Integer bugCount = 0;

    Integer deadlyDesignCount = 0;
    Integer deadlyCodeCount = 0;
    Integer deadlyDocumentCount = 0;
    Integer deadlyOtherCount = 0;
    Integer deadlyCloseCount = 0;
    Integer deadlyDelayedCount = 0;

    Integer criticalDesignCount = 0;
    Integer criticalCodeCount = 0;
    Integer criticalDocumentCount = 0;
    Integer criticalOtherCount = 0;
    Integer criticalCloseCount = 0;
    Integer criticalDelayedCount = 0;

    Integer normalDesignCount = 0;
    Integer normalCodeCount = 0;
    Integer normalDocumentCount = 0;
    Integer normalOtherCount = 0;
    Integer normalCloseCount = 0;
    Integer normalDelayedCount = 0;

    Integer slightDesignCount = 0;
    Integer slightCodeCount = 0;
    Integer slightDocumentCount = 0;
    Integer slightOtherCount = 0;
    Integer slightCloseCount = 0;
    Integer slightDelayedCount = 0;
}
