package generate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 测试用例表
 * @TableName test_use_case
 */
@TableName(value ="test_use_case")
@Data
public class TestUseCase implements Serializable {
    /**
     * 用例编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用例标题
     */
    private String title;

    /**
     * 等级
     */
    private String level;

    /**
     * 创建人id
     */
    private Integer creatorId;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 需求 id   外键
     */
    private Integer requirementId;

    /**
     * 用例类型
     */
    private String type;

    /**
     * 处理步骤(记录)
     */
    private Object processStep;

    /**
     * 所属模块id
     */
    private Integer modularId;

    /**
     * 前置条件
     */
    private String precondition;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}