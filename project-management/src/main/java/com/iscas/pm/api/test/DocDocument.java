package test;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 * 文档记录表
 * @TableName doc_document
 */
@TableName(value ="doc_document")
@Data
public class DocDocument implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 上传者姓名
     */
    private String uploader;

    /**
     * 生成时间
     */
    private LocalDate createTime;

    /**
     * 版本号
     */
    private String version;

    /**
     * 文档类型
     */
    private String type;

    /**
     * 所属的目录id
     */
    private Integer directoryId;

    /**
     * 文档路径
     */
    private String path;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}