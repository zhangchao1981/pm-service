package com.iscas.pm.common.core.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class TreeUtil {





	/**
	 * 两层循环实现建树
	 * @param treeNodes 传入的树节点列表
	 */
	public <T extends TreeNode> List<T> buildByLoop(List<T> treeNodes, Object root) {

		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}
			for (T it : treeNodes) {
				if (treeNode.getId().equals(it.getParentId())) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.add(it);
				}
			}
		}
		return trees;
	}

	/**
	 * 使用递归方法建树
	 */
	public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(findChildrenToTree(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点(用来建树)
	 */
	private <T extends TreeNode> T findChildrenToTree(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId().equals(it.getParentId())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<>());
				}
				treeNode.add(findChildrenToTree(it, treeNodes));
			}
		}
		return treeNode;
	}


	/**
	 * 使用递归方法建List
	 */
	public <T extends TreeNode> List<T> buildList(List<T> treeNodes) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			trees.add(findChildrenToList(treeNode, treeNodes));
		}
		return trees;
	}

	/**
	 * 递归查找子节点(用来建List)
	 */
	private <T extends TreeNode> T findChildrenToList(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId().equals(it.getParentId())) {
				treeNode.add(findChildrenToList(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * 递归将树所有节点转为list
	 * @param treeNode
	 * @param <T>
	 * @return
	 */
	public static <T extends TreeNode> List<T> change2List(T treeNode) {
		List<T> list = new ArrayList<>();
		list.add(treeNode);
		change2List(treeNode, list);
		return list;
	}

	private static <T extends TreeNode> void change2List(T treeNode, List<T> list) {
		final List<TreeNode> children = treeNode.getChildren();
		if (!CollectionUtils.isEmpty(children)) {
			for (TreeNode child : children) {
				list.add((T) child);
				change2List((T)child, list);
			}
		}
	}

	/**
	 * 构建一颗以root为根节点的树
	 */
	public <T extends TreeNode> T buildTree(List<T> allNodes, T root) {
		root.setChildren(new ArrayList<>());
		List<T> subChildren = allNodes.stream().filter(node -> Objects.equals(node.getParentId(), root.getId())).collect(Collectors.toList());
		if (!subChildren.isEmpty()) {
			for (T node : subChildren) {
				root.add(node);
				buildTree(allNodes, node);
			}
		}
		return root;
	}

	public static TreeNode fixTree(TreeNode tree, Set<String> ids) {
		if (tree.children.isEmpty())  {
			return ids.contains(tree.getId()) ? tree : null;
		}

		List<TreeNode> newChildren = new ArrayList<>();
		for (TreeNode child : tree.children) {
			TreeNode newChild = fixTree(child, ids);
			if (newChild != null) {
				newChildren.add(newChild);
			}
		}
		if (!newChildren.isEmpty() || ids.contains(tree.getId())) {
			return tree.setChildren(newChildren);
		}
		return null;
	}
}
