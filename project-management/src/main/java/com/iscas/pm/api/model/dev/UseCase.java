package com.iscas.pm.api.model.dev;

import lombok.Data;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/4.
 */
@Data
public class UseCase {

    private String role;


    private List<String> precondition;

    private List<String> successScene;

    private List<String> branchScene;

    private List<String> constraint;

    public UseCase() {
    }

    public UseCase(DevRequirement devRequirement) {
        this.role = devRequirement.getRole();
        this.precondition = devRequirement.getPrecondition();
        this.successScene = devRequirement.getSuccessScene();
        this.branchScene = devRequirement.getBranchScene();
        this.constraint = devRequirement.getConstraint();
    }

}
