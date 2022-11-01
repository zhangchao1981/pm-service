
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for data_requirement
-- ----------------------------
DROP TABLE IF EXISTS `data_requirement`;
CREATE TABLE `data_requirement`  (
                                     `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `data_info` json NULL COMMENT '数据需求集合',
                                     `requirement_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据需求名称',
                                     `require_id` int(10) UNSIGNED NOT NULL COMMENT '开发需求id',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `fk_dev_require_data`(`require_id`) USING BTREE,
                                     CONSTRAINT `fk_dev_require_data` FOREIGN KEY (`require_id`) REFERENCES `dev_requirement` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据需求表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for dev_interface
-- ----------------------------
DROP TABLE IF EXISTS `dev_interface`;
CREATE TABLE `dev_interface`  (
                                  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '接口编号',
                                  `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口名称',
                                  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口类型(内部/外部)',
                                  `maintainer` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护人',
                                  `sender` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送方',
                                  `acceptor` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收方',
                                  `priority` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'MIN' COMMENT '优先级',
                                  `require_id` int(6) UNSIGNED ZEROFILL NOT NULL COMMENT '需求id',
                                  `data_description` json COMMENT '接口数据元素说明',
                                  `category` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口种类(API)',
                                  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口说明',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `fk_interface_requirement`(`require_id`) USING BTREE,
                                  CONSTRAINT `fk_interface_requirement` FOREIGN KEY (`require_id`) REFERENCES `dev_requirement` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1001 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '关联接口表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for dev_modular
-- ----------------------------
DROP TABLE IF EXISTS `dev_modular`;
CREATE TABLE `dev_modular`  (
                                `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                                `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块名称',
                                `parent_id` int(10) NOT NULL COMMENT '父节点id',
                                `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块描述',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目模块表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_requirement
-- ----------------------------
DROP TABLE IF EXISTS `dev_requirement`;
CREATE TABLE `dev_requirement`  (
                                     `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '需求编号  6位数',
                                     `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '需求用例名称',
                                     `worker` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '责任人',
                                     `priority` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '优先级',
                                     `requirement_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '需求类型',
                                     `is_change` tinyint(4) UNSIGNED DEFAULT NULL COMMENT '变更需求  0: 无变更 1:有变更',
                                     `status` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态',
                                     `dev_progress` float DEFAULT NULL COMMENT '开发进度',
                                     `schedule_hour` float(8, 0) DEFAULT NULL COMMENT '计划工时',
  `start_date` datetime(0) DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime(0) DEFAULT NULL COMMENT '结束时间',
  `happened_hour` double(8, 0) DEFAULT NULL COMMENT '发生工时',
  `source` varchar(17) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '需求来源',
  `use_case_explain` json COMMENT '用例说明',
  `modular_id` int(10) UNSIGNED NOT NULL COMMENT '模块id',
  `prototype` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原型设计图路径，多个用逗号隔开',
  `update_time` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `requirement_description` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '需求描述',
  `user_id` int(10) DEFAULT NULL COMMENT '责任人的userId',
  `actual_end_date` datetime(0) DEFAULT NULL COMMENT '实际结束时间',
  `actual_start_date` datetime(0) DEFAULT NULL COMMENT '实际开始时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_requirement_modular`(`modular_id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  CONSTRAINT `fk_requirement_modular` FOREIGN KEY (`modular_id`) REFERENCES `dev_modular` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 100001 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '开发需求表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_task
-- ----------------------------
DROP TABLE IF EXISTS `dev_task`;
CREATE TABLE `dev_task`  (
                             `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                             `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
                             `worker` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '责任人',
                             `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务状态',
                             `schedule_hour` float NOT NULL COMMENT '计划工时',
                             `happened_hour` float DEFAULT 0 COMMENT '发生工时',
                             `start_date` datetime(0) NOT NULL COMMENT '开始时间',
                             `end_date` datetime(0) NOT NULL COMMENT '结束时间',
                             `require_id` int(6) UNSIGNED ZEROFILL NOT NULL COMMENT '开发需求id',
                             `dev_progress` float DEFAULT 0 COMMENT '开发进度',
                             `worker_id` int(10) DEFAULT NULL COMMENT '责任人id',
                             `actual_start_date` datetime(0) DEFAULT NULL COMMENT '实际开始日期',
                             `actual_end_date` datetime(0) DEFAULT NULL COMMENT '实际结束日期',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `fk_task_require`(`require_id`) USING BTREE,
                             CONSTRAINT `fk_task_requirement` FOREIGN KEY (`require_id`) REFERENCES `dev_requirement` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 10001 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '开发任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for doc_directory
-- ----------------------------
DROP TABLE IF EXISTS `doc_directory`;
CREATE TABLE `doc_directory`  (
                                  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '目录id',
                                  `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目录名称',
                                  `parent_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '父节点id',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文档目录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for doc_document
-- ----------------------------
DROP TABLE IF EXISTS `doc_document`;
CREATE TABLE `doc_document`  (
                                 `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
                                 `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文档名称',
                                 `uploader` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传者姓名',
                                 `create_time` datetime(0) NOT NULL COMMENT '生成时间',
                                 `version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '版本号',
                                 `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文档类型',
                                 `directory_id` int(11) UNSIGNED NOT NULL COMMENT '所属的目录id',
                                 `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文档路径',
                                 `update_time` datetime(0) NOT NULL COMMENT '最后更新时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `fk_directory_document`(`directory_id`) USING BTREE,
                                 CONSTRAINT `fk_directory_document` FOREIGN KEY (`directory_id`) REFERENCES `doc_directory` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文档记录表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for doc_reference
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
                                  INDEX `fk_template`(`template_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模板对应的引用文档表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for doc_revise_record
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
                                      INDEX `fk_reference_template`(`template_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文档修订记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for env_hardware
-- ----------------------------
DROP TABLE IF EXISTS `env_hardware`;
CREATE TABLE `env_hardware`  (
                                 `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `hardware_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '硬件名称',
                                 `min_config` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最低配置',
                                 `amount` tinyint(4) NOT NULL COMMENT '数量',
                                 `application` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用途',
                                 `use_period` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '使用时间',
                                 `security` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '安全保密',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for env_information
-- ----------------------------
DROP TABLE IF EXISTS `env_information`;
CREATE TABLE `env_information`  (
                                    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                    `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类',
                                    `server_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务器或系统地址',
                                    `username` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
                                    `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
                                    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '说明',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for env_software
-- ----------------------------
DROP TABLE IF EXISTS `env_software`;
CREATE TABLE `env_software`  (
                                 `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                 `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类',
                                 `version_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '版本标识',
                                 `source` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '来源',
                                 `application` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用途',
                                 `medium` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '介质',
                                 `security` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '安全保密',
                                 `soft_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '软件名称',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
                                 `worker_list` json COMMENT '责任人名字List',
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
  `worker_ids` json COMMENT '责任人IdList',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_feedback
-- ----------------------------
DROP TABLE IF EXISTS `task_feedback`;
CREATE TABLE `task_feedback`  (
                                  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `person_name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '反馈人姓名',
                                  `date` datetime(0) NOT NULL COMMENT '反馈日期',
                                  `create_time` datetime(0) NOT NULL COMMENT '填写反馈时间',
                                  `progress` tinyint(4) NOT NULL COMMENT '当日进度（0-100）',
                                  `working_hour` float(4, 0) NOT NULL COMMENT '当日花费工时',
  `working_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作内容说明',
  `plan_task_id` int(10) NULL DEFAULT NULL COMMENT '关联的计划任务id',
  `user_id` int(10) NULL DEFAULT NULL COMMENT '用户id',
  `dev_task_id` int(11) NULL DEFAULT NULL COMMENT '关联的开发任务id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_bug
-- ----------------------------
DROP TABLE IF EXISTS `test_bug`;
CREATE TABLE `test_bug`  (
                             `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '缺陷编号',
                             `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '缺陷标题',
                             `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '缺陷状态',
                             `severity` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缺陷严重程度',
                             `priority` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '优先级',
                             `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '缺陷类型',
                             `source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缺陷来源',
                             `inject_stage` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缺陷注入阶段',
                             `probability` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缺陷出现概率',
                             `files` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '附件文件路径，多个用逗号隔开',
                             `detail` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '缺陷详细说明，富文本存储',
                             `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提出人姓名',
                             `creator_user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '提出人用户名',
                             `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                             `current_processor` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '当前处理人姓名',
                             `current_processor_user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前处理人用户名',
                             `owner` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缺陷归属人',
                             `solver` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缺陷解决人',
                             `solve_time` datetime(0) DEFAULT NULL COMMENT '缺陷解决时间',
                             `solve_result` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缺陷解决结果',
                             `solve_hours` tinyint(4) DEFAULT NULL COMMENT '解决时长',
                             `regression_hours` tinyint(4) DEFAULT NULL COMMENT '回归时长',
                             `plan_id` int(10) NOT NULL COMMENT '关联测试计划id',
                             `execute_log_id` int(10) NOT NULL COMMENT '关联用例执行记录id',
                             `module_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属项目模块id',
                             `requirement_id` int(10) UNSIGNED ZEROFILL NOT NULL COMMENT '关联开发需求id',
                             `current_processor_id` int(10) DEFAULT NULL COMMENT '当前处理人用户id',
                             `creator_id` int(10) DEFAULT NULL COMMENT '提出人用户id\r\n',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `fk_bug_module`(`module_id`) USING BTREE,
                             INDEX `fk_bug_plan`(`plan_id`) USING BTREE,
                             INDEX `fk_bug_requirement`(`requirement_id`) USING BTREE,
                             INDEX `fk_bug_case`(`execute_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100001 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '测试缺陷表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_bug_process_log
-- ----------------------------
DROP TABLE IF EXISTS `test_bug_process_log`;
CREATE TABLE `test_bug_process_log`  (
                                         `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                         `bug_id` int(10) NULL DEFAULT NULL COMMENT '缺陷id',
                                         `time` datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
                                         `action` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理动作',
                                         `processor` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理人',
                                         `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '处理描述',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_execute_log
-- ----------------------------
DROP TABLE IF EXISTS `test_execute_log`;
CREATE TABLE `test_execute_log`  (
                                     `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `pass` tinyint(4) DEFAULT NULL COMMENT '是否通过  :1 .通过  0 未通过  null  未执行 ',
                                     `plan_id` int(10) DEFAULT NULL COMMENT '测试计划id',
                                     `test_person` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '测试人员姓名',
                                     `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用例标题',
                                     `level` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用例等级',
                                     `requirement_id` int(10) DEFAULT NULL COMMENT '用例关联的需求id',
                                     `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用例类型',
                                     `process_step` json COMMENT '用例执行步骤',
                                     `modular_id` int(10) DEFAULT NULL COMMENT '用例所属模块id',
                                     `use_case_id` int(10) DEFAULT NULL COMMENT '导入的测试用例id',
                                     `default_count` int(10) DEFAULT NULL COMMENT '缺陷数量',
                                     `modular_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用例所属模块的模块名',
                                     `test_person_id` int(10) DEFAULT NULL COMMENT '测试人员的用户id',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '测试用例执行记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_plan
-- ----------------------------
DROP TABLE IF EXISTS `test_plan`;
CREATE TABLE `test_plan`  (
                              `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '测试计划名称\r\n',
                              `test_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '测试类型',
                              `worker_id` int(10) DEFAULT NULL COMMENT '负责人id',
                              `worker` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '负责人',
                              `bug_statistic` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缺陷统计',
                              `tested_case` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '已测用例统计',
                              `pass_rate` float DEFAULT NULL COMMENT '通过率',
                              `execute_progress` float DEFAULT NULL COMMENT '执行进度',
                              `start_time` date NOT NULL COMMENT '开始时间',
                              `end_time` date NOT NULL COMMENT '结束时间',
                              `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                              `update_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '测试计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_use_case
-- ----------------------------
DROP TABLE IF EXISTS `test_use_case`;
CREATE TABLE `test_use_case`  (
                                  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用例编号',
                                  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用例标题',
                                  `level` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '等级',
                                  `creator_id` int(10) DEFAULT NULL COMMENT '创建人id',
                                  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                  `requirement_id` int(10) UNSIGNED NOT NULL COMMENT '需求 id   外键',
                                  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用例类型',
                                  `process_step` json COMMENT '处理步骤(记录)',
                                  `modular_id` int(10) DEFAULT NULL COMMENT '所属模块id',
                                  `precondition` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '前置条件',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `fk_case_requirement`(`requirement_id`) USING BTREE,
                                  CONSTRAINT `fk_case_requirement` FOREIGN KEY (`requirement_id`) REFERENCES `dev_requirement` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 100001 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '测试用例表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
