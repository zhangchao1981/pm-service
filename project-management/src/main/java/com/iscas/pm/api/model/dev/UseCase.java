package com.iscas.pm.api.model.dev;

import lombok.Data;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/4.
 */
@Data
public class UseCase {
    String  role;
    String  precondition;
    String successScene;
    List<String>  branchScene;
    List<String>  Constraint;
}
