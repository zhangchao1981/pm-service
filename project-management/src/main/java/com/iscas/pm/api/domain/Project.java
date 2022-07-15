package com.iscas.pm.api.domain;

//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
//project
/**
 * 
 * @TableName pjdata
 */
@ApiModel(value = "Pjdata对象", description = "")
//@Data
@Data
public class Project implements Serializable {

    /**
     * 
     */
    //@TableId(type = IdType.AUTO)
    private String id;

    /**
     * 
     */
    private String name;

}