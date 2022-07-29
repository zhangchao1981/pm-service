
package com.iscas.pm.common.core.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用树节点
 * @author zzc
 */
@Data
@Accessors(chain = true)
public class TreeNode {
	@Id
	@ApiModelProperty(value = "节点id，后台自动生成")
	private String id;

	@ApiModelProperty(value = "上级节点")
	protected String parentId;

	protected List<TreeNode> children = new ArrayList<>();

	public void add(TreeNode node) {
		children.add(node);
	}
}
