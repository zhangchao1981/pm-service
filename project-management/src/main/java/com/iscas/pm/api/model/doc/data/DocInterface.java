package com.iscas.pm.api.model.doc.data;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.iscas.pm.api.model.dev.DevInterface;
import com.iscas.pm.api.model.dev.InterfaceTypeEnum;
import com.iscas.pm.api.model.dev.PriorityEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.ArrayList;
import java.util.List;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;

/**
 * @Author： zhangchao
 * @Date： 2022/10/26
 * @Description：
 */
@Data
public class DocInterface {

    @ApiModelProperty(value = "接口编号，系统自动生成，添加接口无需传参")
    private Integer id;

    @ApiModelProperty(value = "接口名称", required = true)
    private String name;

    @ApiModelProperty(value = "接口类型", required = true)
    private InterfaceTypeEnum type;

    @ApiModelProperty(value = "维护人，即新建接口的用户")
    private String maintainer;

    @ApiModelProperty(value = "发送方", required = true)
    private String sender;

    @ApiModelProperty(value = "接收方", required = true)
    private String acceptor;

    @ApiModelProperty(value = "优先级", required = true)
    private PriorityEnum priority;

    @ApiModelProperty(value = "开发需求id", required = true)
    private Integer requireId;

    @ApiModelProperty(value = "接口种类", notes = "API接口", required = true)
    private String category;

    @ApiModelProperty(value = "接口描述", notes = "API接口", required = true)
    private String description;

    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    private List<DocInterfaceDataDescription> dataDescriptionList;

    private String projectId;

    private String projectName;

    public DocInterface(DevInterface devInterface, String projectId, String projectName) {
        this.id = devInterface.getId();
        this.name = devInterface.getName();
        this.type = devInterface.getType();
        this.maintainer = devInterface.getMaintainer();
        this.sender = devInterface.getSender();
        this.acceptor = devInterface.getAcceptor();
        this.priority = devInterface.getPriority();
        this.requireId = devInterface.getRequireId();
        if (devInterface.getDataDescription() != null && devInterface.getDataDescription().size() > 0) {
            List<DocInterfaceDataDescription> docInterfaceDataDescription = new ArrayList<>();
            devInterface.getDataDescription().forEach(dataDescription -> {
                docInterfaceDataDescription.add(new DocInterfaceDataDescription(dataDescription));
            });
            this.dataDescriptionList = docInterfaceDataDescription;
        }
        this.projectId = projectId;
        this.projectName = projectName;
        this.category = devInterface.getCategory();
        this.description = devInterface.getDescription();
    }
}
