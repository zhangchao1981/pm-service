/*
 Navicat Premium Data Transfer

 Source Server         : pm_mysql
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 192.168.100.77:3306
 Source Schema         : wdscgj

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 05/10/2022 21:01:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for project_plan
-- ----------------------------
DROP TABLE IF EXISTS `project_plan`;
CREATE TABLE `project_plan`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `wbs` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'WBS编号',
  `doc_path` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联文档路径',
  `is_milestone` tinyint(4) UNSIGNED NOT NULL COMMENT '是否为里程碑节点，\"1\":\"是\"；\"0\":\"否\"',
  `is_baseline` tinyint(4) UNSIGNED NOT NULL COMMENT '是否为基线节点，\"1\":\"是\"；\"0\":\"否\"',
  `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `worker_list` json COMMENT '责任人名字list',
  `person_count` tinyint(4) DEFAULT NULL COMMENT '人数',
  `working_days` tinyint(4) DEFAULT NULL COMMENT '工期(天)',
  `start_date` datetime(0) DEFAULT NULL COMMENT '开始日期',
  `end_date` datetime(0) DEFAULT NULL COMMENT '截止日期',
  `schedule_hour` float(8, 0) DEFAULT NULL COMMENT '计划工时',
  `status` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态',
  `progress_rate` float DEFAULT NULL COMMENT '项目完成进度',
  `actual_start_date` datetime(0) DEFAULT NULL COMMENT '实际开始日期',
  `actual_end_date` datetime(0) DEFAULT NULL COMMENT '实际结束日期',
  `position` tinyint(4) DEFAULT NULL COMMENT '任务排序编号',
  `parent_id` int(11) DEFAULT NULL COMMENT '父任务id',
  `happened_hour` double DEFAULT NULL COMMENT '发生工时',
  `worker_ids` json COMMENT '责任人id列表',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_plan
-- ----------------------------
INSERT INTO `project_plan` VALUES (24, '1', '', 0, 0, '项目策划', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, NULL, NULL);
INSERT INTO `project_plan` VALUES (26, '1.1', '', 0, 0, '编写软件开发计划', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 24, NULL, NULL);
INSERT INTO `project_plan` VALUES (27, '1.3', '', 0, 0, '编写软件质量保证计划', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 24, NULL, NULL);
INSERT INTO `project_plan` VALUES (28, '1.2', '', 0, 0, '编写软件配置管理计划', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 24, NULL, NULL);
INSERT INTO `project_plan` VALUES (29, '1.4', '', 1, 0, '评审项目计划', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 24, NULL, NULL);
INSERT INTO `project_plan` VALUES (30, '2', '', 0, 0, '需求分析与原型设计', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 0, NULL, NULL);
INSERT INTO `project_plan` VALUES (31, '2.2', '', 0, 0, '软件需求分析', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 30, NULL, NULL);
INSERT INTO `project_plan` VALUES (32, '2.1', '', 0, 0, '原型设计', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 30, NULL, NULL);
INSERT INTO `project_plan` VALUES (33, '2.3', '', 0, 0, '原型确认与完善', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 30, NULL, NULL);
INSERT INTO `project_plan` VALUES (34, '2.4', '', 0, 0, '分解分配需求，确定迭代版本和周期', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 30, NULL, NULL);
INSERT INTO `project_plan` VALUES (35, '3', '', 0, 0, '概要设计', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 0, NULL, NULL);
INSERT INTO `project_plan` VALUES (36, '3.1', '', 0, 0, '技术选型与试验', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 35, NULL, NULL);
INSERT INTO `project_plan` VALUES (37, '3.2', '', 0, 0, '开发框架搭建', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 35, NULL, NULL);
INSERT INTO `project_plan` VALUES (38, '3.3', '', 0, 0, '编写概要设计文档', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 35, NULL, NULL);
INSERT INTO `project_plan` VALUES (39, '3.4', '', 0, 0, '编写集成测试计划', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 35, NULL, NULL);
INSERT INTO `project_plan` VALUES (40, '3.5', '', 1, 0, '评审概要设计/集成测试计划', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 35, NULL, NULL);
INSERT INTO `project_plan` VALUES (41, '4', '', 0, 0, '开发与验证', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 0, NULL, NULL);
INSERT INTO `project_plan` VALUES (42, '4.1', '', 0, 0, '1.0版本开发与验证', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 41, NULL, NULL);
INSERT INTO `project_plan` VALUES (43, '4.1.1', '', 0, 0, '编写软件需求规格说明', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 42, NULL, NULL);
INSERT INTO `project_plan` VALUES (44, '4.1.2', '', 1, 1, '评审软件需求规格说明', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 42, NULL, NULL);
INSERT INTO `project_plan` VALUES (45, '4.1.6.1', '', 0, 0, '编码开发', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 54, NULL, NULL);
INSERT INTO `project_plan` VALUES (46, '4.1.7.1', '', 0, 0, '编写测试用例', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 55, NULL, NULL);
INSERT INTO `project_plan` VALUES (47, '4.1.7.2', '', 0, 0, '评审测试用例', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 55, NULL, NULL);
INSERT INTO `project_plan` VALUES (48, '4.1.6.2', '', 0, 0, '单元测试与bug修复', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 54, NULL, NULL);
INSERT INTO `project_plan` VALUES (49, '4.1.3', '', 0, 0, '软件需求跟踪', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 42, NULL, NULL);
INSERT INTO `project_plan` VALUES (50, '4.1.4', '', 0, 0, '编写详细设计', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 42, NULL, NULL);
INSERT INTO `project_plan` VALUES (51, '4.1.5', '', 0, 0, '评审详细设计', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 42, NULL, NULL);
INSERT INTO `project_plan` VALUES (52, '4.1.6.4', '', 0, 0, '编写单元测试报告', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 54, NULL, NULL);
INSERT INTO `project_plan` VALUES (54, '4.1.6', '', 0, 0, '编码开发与单元测试', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, 42, NULL, NULL);
INSERT INTO `project_plan` VALUES (55, '4.1.7', '', 0, 0, '集成测试', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, 42, NULL, NULL);
INSERT INTO `project_plan` VALUES (56, '4.1.7.3', '', 0, 0, '编写集成测试说明', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 55, NULL, NULL);
INSERT INTO `project_plan` VALUES (57, '4.1.7.5', '', 0, 0, '编写集成测试报告', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 55, NULL, NULL);
INSERT INTO `project_plan` VALUES (58, '4.1.7.4', '', 0, 0, '集成测试与bug修复', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 55, NULL, NULL);
INSERT INTO `project_plan` VALUES (59, '4.1.6.5', '', 0, 0, '编写单元测试计划', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 54, NULL, NULL);
INSERT INTO `project_plan` VALUES (60, '4.1.6.3', '', 0, 0, '编写单元测试说明', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 54, NULL, NULL);
INSERT INTO `project_plan` VALUES (61, '5', '', 0, 0, '交付与验收', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 0, NULL, NULL);
INSERT INTO `project_plan` VALUES (62, '5.1', '', 0, 0, '系统测试', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 61, NULL, NULL);
INSERT INTO `project_plan` VALUES (63, '5.1.1', '', 0, 0, '编写系统测试计划', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 62, NULL, NULL);
INSERT INTO `project_plan` VALUES (64, '5.1.2', '', 0, 0, '编写系统测试说明', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 62, NULL, NULL);
INSERT INTO `project_plan` VALUES (65, '5.1.3', '', 0, 0, '编写系统测试报告', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 62, NULL, NULL);
INSERT INTO `project_plan` VALUES (66, '5.2', '', 0, 0, '第三方验收测试', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 61, NULL, NULL);
INSERT INTO `project_plan` VALUES (67, '5.2.1', '', 0, 0, '测试环境准备', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 66, NULL, NULL);
INSERT INTO `project_plan` VALUES (68, '5.2.2', '', 0, 0, '验收相关文档准备', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 66, NULL, NULL);
INSERT INTO `project_plan` VALUES (69, '5.3', '', 0, 0, '项目复盘会议', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 61, NULL, NULL);
INSERT INTO `project_plan` VALUES (71, '2.1.1', '', 0, 0, 'XX模块原型设计', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 32, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
