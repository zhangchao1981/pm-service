SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for doc_directory
-- ----------------------------
DROP TABLE IF EXISTS `doc_directory`;
CREATE TABLE `doc_directory`  (
                                  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '目录id',
                                  `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目录名称',
                                  `parent_id` int(11) UNSIGNED DEFAULT NULL COMMENT '父节点id',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for document
-- ----------------------------
DROP TABLE IF EXISTS `doc_document`;
CREATE TABLE `doc_document`  (
                             `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
                             `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文档名称',
                             `uploader` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '上传者姓名',
                             `create_time` date NOT NULL COMMENT '生成时间',
                             `version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '版本号',
                             `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文档类型',
                             `directory_id` int(11) UNSIGNED NOT NULL COMMENT '所属的目录id',
                             `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文档路径',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for planTask
-- ----------------------------
DROP TABLE IF EXISTS `project_plan`;
CREATE TABLE `project_plan`  (
                         `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `wbs` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'WBS编号',
                         `doc_path` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联文档路径',
                         `is_milestone` tinyint(4) NOT NULL COMMENT '是否为里程碑节点，\"1\":\"是\"；\"0\":\"否\"',
                         `is_baseline` tinyint(4) NOT NULL COMMENT '是否为基线节点，\"1\":\"是\"；\"0\":\"否\"',
                         `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
                         `woker` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '责任人',
                         `person_count` tinyint(4) DEFAULT NULL COMMENT '人数',
                         `working_days` tinyint(4) DEFAULT NULL COMMENT '工期(天)',
                         `start_date` datetime(0) DEFAULT NULL COMMENT '开始日期',
                         `end_date` datetime(0) DEFAULT NULL COMMENT '截止日期',
                         `schedule_hour` mediumint(8) DEFAULT NULL COMMENT '计划工时',
                         `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '项目状态',
                         `progress_rate` float DEFAULT NULL COMMENT '项目完成进度',
                         `actual_start_date` datetime(0) DEFAULT NULL COMMENT '实际开始日期',
                         `actual_end_date` datetime(0) DEFAULT NULL COMMENT '实际结束日期',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for reference_doc
-- ----------------------------
DROP TABLE IF EXISTS `doc_reference`;
CREATE TABLE `doc_reference`  (
                                  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `template_id` int(11) NOT NULL COMMENT '外键：模板id',
                                  `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文档名称',
                                  `mark` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
                                  `version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '版本号',
                                  `source` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '来源',
                                  `notes` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '备注',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `fk_template`(`template_id`) USING BTREE,
                                  CONSTRAINT `fk_template` FOREIGN KEY (`template_id`) REFERENCES `template` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for revise_record
-- ----------------------------
DROP TABLE IF EXISTS `doc_revise_record`;
CREATE TABLE `doc_revise_record`  (
                                  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `template_id` int(11) NOT NULL COMMENT '外键：模板id',
                                  `version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '版本编号',
                                  `notes` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '简要说明(变更内容和范围)',
                                  `date` datetime(0) NOT NULL COMMENT '日期',
                                  `mender` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
                                  `approver` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批准人',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `fk_reference`(`template_id`) USING BTREE,
                                  CONSTRAINT `fk_reference` FOREIGN KEY (`template_id`) REFERENCES `template` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `doc_template`;
CREATE TABLE `doc_template`  (
                             `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
                             `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',
                             `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模板描述',
                             `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板文件路径',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;