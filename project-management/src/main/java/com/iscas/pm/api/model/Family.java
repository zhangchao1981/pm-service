package com.iscas.pm.api.model;

import lombok.Data;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/10/11.
 */
@Data
public class Family {
    private List<TestChild> childList;
}
