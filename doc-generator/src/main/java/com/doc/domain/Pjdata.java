package com.doc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
//project
/**
 * 
 * @TableName pjdata
 */
@ApiModel(value = "Pjdata对象", description = "")
@TableName(value ="pjdata")
public class Pjdata implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String pjname;

    /**
     * 
     */
    private Integer pjnumber;

    /**
     * 
     */
    private String pjstage;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     */
    public String getPjname() {
        return pjname;
    }

    /**
     * 
     */
    public void setPjname(String pjname) {
        this.pjname = pjname;
    }

    /**
     * 
     */
    public Integer getPjnumber() {
        return pjnumber;
    }

    /**
     * 
     */
    public void setPjnumber(Integer pjnumber) {
        this.pjnumber = pjnumber;
    }

    /**
     * 
     */
    public String getPjstage() {
        return pjstage;
    }

    /**
     * 
     */
    public void setPjstage(String pjstage) {
        this.pjstage = pjstage;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Pjdata other = (Pjdata) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPjname() == null ? other.getPjname() == null : this.getPjname().equals(other.getPjname()))
            && (this.getPjnumber() == null ? other.getPjnumber() == null : this.getPjnumber().equals(other.getPjnumber()))
            && (this.getPjstage() == null ? other.getPjstage() == null : this.getPjstage().equals(other.getPjstage()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPjname() == null) ? 0 : getPjname().hashCode());
        result = prime * result + ((getPjnumber() == null) ? 0 : getPjnumber().hashCode());
        result = prime * result + ((getPjstage() == null) ? 0 : getPjstage().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pjname=").append(pjname);
        sb.append(", pjnumber=").append(pjnumber);
        sb.append(", pjstage=").append(pjstage);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}