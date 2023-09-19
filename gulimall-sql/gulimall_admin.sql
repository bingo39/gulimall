/*
 Navicat Premium Data Transfer

 Source Server         : Ubuntu_mysql
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : 192.168.31.106:3306
 Source Schema         : gulimall_admin

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 19/09/2023 11:59:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', '0 0/30 * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', NULL, 'io.renren.modules.job.utils.ScheduleJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002E696F2E72656E72656E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000185CA5C49E07874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000000000174000672656E72656E74000CE58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000007800);

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
INSERT INTO `QRTZ_LOCKS` VALUES ('RenrenScheduler', 'STATE_ACCESS');
INSERT INTO `QRTZ_LOCKS` VALUES ('RenrenScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('RenrenScheduler', 'DESKTOP-PJ8IFB61694460990739', 1694463144306, 15000);

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_TRIGGERS` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', 'TASK_1', 'DEFAULT', NULL, 1694464200000, 1694462400000, 5, 'WAITING', 'CRON', 1674166635000, 0, NULL, 2, 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002E696F2E72656E72656E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000185CA5C49E07874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000000000174000672656E72656E74000CE58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000007800);

-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '任务状态  0：正常  1：暂停',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_job
-- ----------------------------
INSERT INTO `schedule_job` VALUES (1, 'testTask', 'renren', '0 0/30 * * * ?', 0, '参数测试', '2023-01-19 22:08:44');

-- ----------------------------
-- Table structure for schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job_log`;
CREATE TABLE `schedule_job_log`  (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `job_id` bigint(20) NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `status` tinyint(4) NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `error` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败信息',
  `times` int(11) NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `job_id`(`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 922 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_job_log
-- ----------------------------
INSERT INTO `schedule_job_log` VALUES (1, 1, 'testTask', 'renren', 0, NULL, 1, '2023-01-20 06:30:00');
INSERT INTO `schedule_job_log` VALUES (2, 1, 'testTask', 'renren', 0, NULL, 1, '2023-01-20 07:00:00');
INSERT INTO `schedule_job_log` VALUES (3, 1, 'testTask', 'renren', 0, NULL, 0, '2023-01-20 07:30:00');
INSERT INTO `schedule_job_log` VALUES (4, 1, 'testTask', 'renren', 0, NULL, 0, '2023-01-20 08:00:00');
INSERT INTO `schedule_job_log` VALUES (5, 1, 'testTask', 'renren', 0, NULL, 1, '2023-01-20 08:30:00');
INSERT INTO `schedule_job_log` VALUES (6, 1, 'testTask', 'renren', 0, NULL, 1, '2023-01-20 09:00:00');
INSERT INTO `schedule_job_log` VALUES (7, 1, 'testTask', 'renren', 0, NULL, 1, '2023-01-20 09:30:01');
INSERT INTO `schedule_job_log` VALUES (8, 1, 'testTask', 'renren', 0, NULL, 1, '2023-01-20 10:00:00');
INSERT INTO `schedule_job_log` VALUES (9, 1, 'testTask', 'renren', 0, NULL, 0, '2023-01-20 10:30:00');
INSERT INTO `schedule_job_log` VALUES (10, 1, 'testTask', 'renren', 0, NULL, 1, '2023-01-24 01:00:00');
INSERT INTO `schedule_job_log` VALUES (11, 1, 'testTask', 'renren', 0, NULL, 1, '2023-01-24 01:30:00');
INSERT INTO `schedule_job_log` VALUES (12, 1, 'testTask', 'renren', 0, NULL, 1, '2023-02-08 21:30:00');
INSERT INTO `schedule_job_log` VALUES (13, 1, 'testTask', 'renren', 0, NULL, 0, '2023-02-08 22:00:00');
INSERT INTO `schedule_job_log` VALUES (14, 1, 'testTask', 'renren', 0, NULL, 0, '2023-02-10 07:00:00');
INSERT INTO `schedule_job_log` VALUES (15, 1, 'testTask', 'renren', 0, NULL, 0, '2023-02-10 07:30:00');
INSERT INTO `schedule_job_log` VALUES (16, 1, 'testTask', 'renren', 0, NULL, 0, '2023-02-10 19:00:00');
INSERT INTO `schedule_job_log` VALUES (17, 1, 'testTask', 'renren', 0, NULL, 0, '2023-02-10 19:30:00');
INSERT INTO `schedule_job_log` VALUES (18, 1, 'testTask', 'renren', 0, NULL, 0, '2023-02-10 20:00:00');
INSERT INTO `schedule_job_log` VALUES (19, 1, 'testTask', 'renren', 0, NULL, 1, '2023-02-10 20:30:00');
INSERT INTO `schedule_job_log` VALUES (20, 1, 'testTask', 'renren', 0, NULL, 1, '2023-02-10 21:00:00');
INSERT INTO `schedule_job_log` VALUES (21, 1, 'testTask', 'renren', 0, NULL, 1, '2023-02-10 21:30:00');
INSERT INTO `schedule_job_log` VALUES (22, 1, 'testTask', 'renren', 0, NULL, 1, '2023-02-10 22:00:00');
INSERT INTO `schedule_job_log` VALUES (23, 1, 'testTask', 'renren', 0, NULL, 1, '2023-02-10 22:30:00');
INSERT INTO `schedule_job_log` VALUES (24, 1, 'testTask', 'renren', 0, NULL, 0, '2023-02-10 23:00:00');
INSERT INTO `schedule_job_log` VALUES (25, 1, 'testTask', 'renren', 0, NULL, 0, '2023-02-10 23:30:00');
INSERT INTO `schedule_job_log` VALUES (26, 1, 'testTask', 'renren', 0, NULL, 4, '2023-02-11 00:00:00');
INSERT INTO `schedule_job_log` VALUES (27, 1, 'testTask', 'renren', 0, NULL, 0, '2023-04-19 23:30:00');
INSERT INTO `schedule_job_log` VALUES (28, 1, 'testTask', 'renren', 0, NULL, 7, '2023-04-20 00:00:00');
INSERT INTO `schedule_job_log` VALUES (29, 1, 'testTask', 'renren', 0, NULL, 1, '2023-04-20 00:30:00');
INSERT INTO `schedule_job_log` VALUES (30, 1, 'testTask', 'renren', 0, NULL, 1, '2023-04-20 01:00:00');
INSERT INTO `schedule_job_log` VALUES (31, 1, 'testTask', 'renren', 0, NULL, 0, '2023-04-20 13:30:00');
INSERT INTO `schedule_job_log` VALUES (32, 1, 'testTask', 'renren', 0, NULL, 0, '2023-04-20 14:00:00');
INSERT INTO `schedule_job_log` VALUES (33, 1, 'testTask', 'renren', 0, NULL, 1, '2023-04-20 14:30:00');
INSERT INTO `schedule_job_log` VALUES (34, 1, 'testTask', 'renren', 0, NULL, 1, '2023-04-20 15:00:00');
INSERT INTO `schedule_job_log` VALUES (35, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-01 16:30:01');
INSERT INTO `schedule_job_log` VALUES (36, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-01 17:00:00');
INSERT INTO `schedule_job_log` VALUES (37, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-01 17:30:00');
INSERT INTO `schedule_job_log` VALUES (38, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-01 18:00:00');
INSERT INTO `schedule_job_log` VALUES (39, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-01 18:30:00');
INSERT INTO `schedule_job_log` VALUES (40, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-01 19:00:00');
INSERT INTO `schedule_job_log` VALUES (41, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-01 19:30:00');
INSERT INTO `schedule_job_log` VALUES (42, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-01 20:00:00');
INSERT INTO `schedule_job_log` VALUES (43, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-01 20:30:00');
INSERT INTO `schedule_job_log` VALUES (44, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-01 21:00:12');
INSERT INTO `schedule_job_log` VALUES (45, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-01 21:30:00');
INSERT INTO `schedule_job_log` VALUES (46, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 02:30:00');
INSERT INTO `schedule_job_log` VALUES (47, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 14:30:01');
INSERT INTO `schedule_job_log` VALUES (48, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 15:00:00');
INSERT INTO `schedule_job_log` VALUES (49, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 15:30:00');
INSERT INTO `schedule_job_log` VALUES (50, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 16:00:00');
INSERT INTO `schedule_job_log` VALUES (51, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 16:30:00');
INSERT INTO `schedule_job_log` VALUES (52, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-03 17:00:00');
INSERT INTO `schedule_job_log` VALUES (53, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 17:30:00');
INSERT INTO `schedule_job_log` VALUES (54, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-03 18:00:00');
INSERT INTO `schedule_job_log` VALUES (55, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-03 18:30:00');
INSERT INTO `schedule_job_log` VALUES (56, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 19:00:00');
INSERT INTO `schedule_job_log` VALUES (57, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 19:30:00');
INSERT INTO `schedule_job_log` VALUES (58, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-03 20:00:00');
INSERT INTO `schedule_job_log` VALUES (59, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 20:30:00');
INSERT INTO `schedule_job_log` VALUES (60, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 21:00:00');
INSERT INTO `schedule_job_log` VALUES (61, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 21:30:00');
INSERT INTO `schedule_job_log` VALUES (62, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-03 22:00:00');
INSERT INTO `schedule_job_log` VALUES (63, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-03 22:30:00');
INSERT INTO `schedule_job_log` VALUES (64, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-03 23:00:00');
INSERT INTO `schedule_job_log` VALUES (65, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-03 23:30:00');
INSERT INTO `schedule_job_log` VALUES (66, 1, 'testTask', 'renren', 0, NULL, 4, '2023-05-04 00:00:00');
INSERT INTO `schedule_job_log` VALUES (67, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-04 00:30:00');
INSERT INTO `schedule_job_log` VALUES (68, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-04 19:30:00');
INSERT INTO `schedule_job_log` VALUES (69, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-04 20:00:00');
INSERT INTO `schedule_job_log` VALUES (70, 1, 'testTask', 'renren', 0, NULL, 2, '2023-05-04 20:30:00');
INSERT INTO `schedule_job_log` VALUES (71, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-04 21:00:00');
INSERT INTO `schedule_job_log` VALUES (72, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-04 21:30:00');
INSERT INTO `schedule_job_log` VALUES (73, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-04 22:00:00');
INSERT INTO `schedule_job_log` VALUES (74, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-04 22:30:00');
INSERT INTO `schedule_job_log` VALUES (75, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-04 23:00:00');
INSERT INTO `schedule_job_log` VALUES (76, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-04 23:30:00');
INSERT INTO `schedule_job_log` VALUES (77, 1, 'testTask', 'renren', 0, NULL, 4, '2023-05-05 00:00:00');
INSERT INTO `schedule_job_log` VALUES (78, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 00:30:00');
INSERT INTO `schedule_job_log` VALUES (79, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 01:00:02');
INSERT INTO `schedule_job_log` VALUES (80, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 01:30:00');
INSERT INTO `schedule_job_log` VALUES (81, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 02:00:00');
INSERT INTO `schedule_job_log` VALUES (82, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 02:30:00');
INSERT INTO `schedule_job_log` VALUES (83, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 03:00:00');
INSERT INTO `schedule_job_log` VALUES (84, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 12:30:00');
INSERT INTO `schedule_job_log` VALUES (85, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 13:00:00');
INSERT INTO `schedule_job_log` VALUES (86, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 13:30:00');
INSERT INTO `schedule_job_log` VALUES (87, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 14:00:00');
INSERT INTO `schedule_job_log` VALUES (88, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 14:30:00');
INSERT INTO `schedule_job_log` VALUES (89, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 15:00:00');
INSERT INTO `schedule_job_log` VALUES (90, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 15:30:00');
INSERT INTO `schedule_job_log` VALUES (91, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 16:00:00');
INSERT INTO `schedule_job_log` VALUES (92, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 16:30:00');
INSERT INTO `schedule_job_log` VALUES (93, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 17:00:00');
INSERT INTO `schedule_job_log` VALUES (94, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 17:30:00');
INSERT INTO `schedule_job_log` VALUES (95, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 19:30:00');
INSERT INTO `schedule_job_log` VALUES (96, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 20:00:00');
INSERT INTO `schedule_job_log` VALUES (97, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 20:30:00');
INSERT INTO `schedule_job_log` VALUES (98, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 21:00:00');
INSERT INTO `schedule_job_log` VALUES (99, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-05 21:30:00');
INSERT INTO `schedule_job_log` VALUES (100, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 22:00:00');
INSERT INTO `schedule_job_log` VALUES (101, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 22:30:00');
INSERT INTO `schedule_job_log` VALUES (102, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 23:00:00');
INSERT INTO `schedule_job_log` VALUES (103, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-05 23:30:00');
INSERT INTO `schedule_job_log` VALUES (104, 1, 'testTask', 'renren', 0, NULL, 4, '2023-05-06 00:00:00');
INSERT INTO `schedule_job_log` VALUES (105, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-06 00:30:00');
INSERT INTO `schedule_job_log` VALUES (106, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 01:00:00');
INSERT INTO `schedule_job_log` VALUES (107, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-06 01:30:00');
INSERT INTO `schedule_job_log` VALUES (108, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 02:00:00');
INSERT INTO `schedule_job_log` VALUES (109, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 02:30:00');
INSERT INTO `schedule_job_log` VALUES (110, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 03:00:00');
INSERT INTO `schedule_job_log` VALUES (111, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 03:30:00');
INSERT INTO `schedule_job_log` VALUES (112, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-06 12:30:00');
INSERT INTO `schedule_job_log` VALUES (113, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 13:00:00');
INSERT INTO `schedule_job_log` VALUES (114, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 13:30:00');
INSERT INTO `schedule_job_log` VALUES (115, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-06 18:00:00');
INSERT INTO `schedule_job_log` VALUES (116, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 18:30:00');
INSERT INTO `schedule_job_log` VALUES (117, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-06 19:00:00');
INSERT INTO `schedule_job_log` VALUES (118, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 19:30:00');
INSERT INTO `schedule_job_log` VALUES (119, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 20:00:00');
INSERT INTO `schedule_job_log` VALUES (120, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 20:30:00');
INSERT INTO `schedule_job_log` VALUES (121, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 21:00:00');
INSERT INTO `schedule_job_log` VALUES (122, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 21:30:00');
INSERT INTO `schedule_job_log` VALUES (123, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-06 22:00:00');
INSERT INTO `schedule_job_log` VALUES (124, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 22:30:00');
INSERT INTO `schedule_job_log` VALUES (125, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 23:00:00');
INSERT INTO `schedule_job_log` VALUES (126, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-06 23:30:00');
INSERT INTO `schedule_job_log` VALUES (127, 1, 'testTask', 'renren', 0, NULL, 4, '2023-05-07 00:00:00');
INSERT INTO `schedule_job_log` VALUES (128, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 00:30:00');
INSERT INTO `schedule_job_log` VALUES (129, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 01:00:01');
INSERT INTO `schedule_job_log` VALUES (130, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 01:30:00');
INSERT INTO `schedule_job_log` VALUES (131, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 02:00:00');
INSERT INTO `schedule_job_log` VALUES (132, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 02:30:00');
INSERT INTO `schedule_job_log` VALUES (133, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 03:00:00');
INSERT INTO `schedule_job_log` VALUES (134, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 03:30:00');
INSERT INTO `schedule_job_log` VALUES (135, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 04:00:00');
INSERT INTO `schedule_job_log` VALUES (136, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 15:00:00');
INSERT INTO `schedule_job_log` VALUES (137, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-07 15:30:00');
INSERT INTO `schedule_job_log` VALUES (138, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-07 16:00:00');
INSERT INTO `schedule_job_log` VALUES (139, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-07 20:00:00');
INSERT INTO `schedule_job_log` VALUES (140, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-07 20:30:00');
INSERT INTO `schedule_job_log` VALUES (141, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 21:00:00');
INSERT INTO `schedule_job_log` VALUES (142, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 21:30:00');
INSERT INTO `schedule_job_log` VALUES (143, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-07 22:00:00');
INSERT INTO `schedule_job_log` VALUES (144, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 22:30:00');
INSERT INTO `schedule_job_log` VALUES (145, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-07 23:00:00');
INSERT INTO `schedule_job_log` VALUES (146, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-07 23:30:00');
INSERT INTO `schedule_job_log` VALUES (147, 1, 'testTask', 'renren', 0, NULL, 3, '2023-05-08 00:00:00');
INSERT INTO `schedule_job_log` VALUES (148, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-08 00:30:00');
INSERT INTO `schedule_job_log` VALUES (149, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-08 01:00:00');
INSERT INTO `schedule_job_log` VALUES (150, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-08 01:30:00');
INSERT INTO `schedule_job_log` VALUES (151, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-08 02:00:00');
INSERT INTO `schedule_job_log` VALUES (152, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-08 02:30:00');
INSERT INTO `schedule_job_log` VALUES (153, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-08 03:00:00');
INSERT INTO `schedule_job_log` VALUES (154, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-08 03:30:00');
INSERT INTO `schedule_job_log` VALUES (155, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-08 19:00:00');
INSERT INTO `schedule_job_log` VALUES (156, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-08 19:30:00');
INSERT INTO `schedule_job_log` VALUES (157, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-08 20:00:00');
INSERT INTO `schedule_job_log` VALUES (158, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-08 20:30:00');
INSERT INTO `schedule_job_log` VALUES (159, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-08 21:00:00');
INSERT INTO `schedule_job_log` VALUES (160, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-08 21:30:00');
INSERT INTO `schedule_job_log` VALUES (161, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-08 22:00:00');
INSERT INTO `schedule_job_log` VALUES (162, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-09 02:30:00');
INSERT INTO `schedule_job_log` VALUES (163, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-09 03:00:00');
INSERT INTO `schedule_job_log` VALUES (164, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-09 03:30:00');
INSERT INTO `schedule_job_log` VALUES (165, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-14 23:30:01');
INSERT INTO `schedule_job_log` VALUES (166, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 00:00:00');
INSERT INTO `schedule_job_log` VALUES (167, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 00:30:00');
INSERT INTO `schedule_job_log` VALUES (168, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-15 12:30:00');
INSERT INTO `schedule_job_log` VALUES (169, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 13:00:01');
INSERT INTO `schedule_job_log` VALUES (170, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 13:30:01');
INSERT INTO `schedule_job_log` VALUES (171, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 14:00:01');
INSERT INTO `schedule_job_log` VALUES (172, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-15 14:30:00');
INSERT INTO `schedule_job_log` VALUES (173, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 15:00:00');
INSERT INTO `schedule_job_log` VALUES (174, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 15:30:01');
INSERT INTO `schedule_job_log` VALUES (175, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 16:00:00');
INSERT INTO `schedule_job_log` VALUES (176, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 16:30:00');
INSERT INTO `schedule_job_log` VALUES (177, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-15 17:00:00');
INSERT INTO `schedule_job_log` VALUES (178, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 17:30:00');
INSERT INTO `schedule_job_log` VALUES (179, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 18:00:00');
INSERT INTO `schedule_job_log` VALUES (180, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 18:30:00');
INSERT INTO `schedule_job_log` VALUES (181, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-15 19:00:01');
INSERT INTO `schedule_job_log` VALUES (182, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-15 19:30:00');
INSERT INTO `schedule_job_log` VALUES (183, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-18 03:00:00');
INSERT INTO `schedule_job_log` VALUES (184, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-18 03:30:00');
INSERT INTO `schedule_job_log` VALUES (185, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-18 04:00:01');
INSERT INTO `schedule_job_log` VALUES (186, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-18 04:30:00');
INSERT INTO `schedule_job_log` VALUES (187, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-18 16:30:00');
INSERT INTO `schedule_job_log` VALUES (188, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-18 17:00:01');
INSERT INTO `schedule_job_log` VALUES (189, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-18 17:30:00');
INSERT INTO `schedule_job_log` VALUES (190, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-18 18:00:00');
INSERT INTO `schedule_job_log` VALUES (191, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-18 18:30:00');
INSERT INTO `schedule_job_log` VALUES (192, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-18 19:00:00');
INSERT INTO `schedule_job_log` VALUES (193, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-18 19:30:00');
INSERT INTO `schedule_job_log` VALUES (194, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-18 20:00:00');
INSERT INTO `schedule_job_log` VALUES (195, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-18 20:30:00');
INSERT INTO `schedule_job_log` VALUES (196, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-18 21:00:00');
INSERT INTO `schedule_job_log` VALUES (197, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-18 21:30:00');
INSERT INTO `schedule_job_log` VALUES (198, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-19 01:30:00');
INSERT INTO `schedule_job_log` VALUES (199, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-19 02:00:00');
INSERT INTO `schedule_job_log` VALUES (200, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-19 02:30:00');
INSERT INTO `schedule_job_log` VALUES (201, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-19 03:00:00');
INSERT INTO `schedule_job_log` VALUES (202, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-19 20:30:00');
INSERT INTO `schedule_job_log` VALUES (203, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-19 21:00:00');
INSERT INTO `schedule_job_log` VALUES (204, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-19 21:30:00');
INSERT INTO `schedule_job_log` VALUES (205, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-19 22:00:00');
INSERT INTO `schedule_job_log` VALUES (206, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-19 22:30:01');
INSERT INTO `schedule_job_log` VALUES (207, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-19 23:00:00');
INSERT INTO `schedule_job_log` VALUES (208, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-19 23:30:01');
INSERT INTO `schedule_job_log` VALUES (209, 1, 'testTask', 'renren', 0, NULL, 2, '2023-05-20 00:00:00');
INSERT INTO `schedule_job_log` VALUES (210, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-20 00:30:00');
INSERT INTO `schedule_job_log` VALUES (211, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-20 01:00:00');
INSERT INTO `schedule_job_log` VALUES (212, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-20 01:30:00');
INSERT INTO `schedule_job_log` VALUES (213, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-20 02:00:01');
INSERT INTO `schedule_job_log` VALUES (214, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-20 02:30:00');
INSERT INTO `schedule_job_log` VALUES (215, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-20 03:00:00');
INSERT INTO `schedule_job_log` VALUES (216, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-21 20:30:01');
INSERT INTO `schedule_job_log` VALUES (217, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-21 21:00:00');
INSERT INTO `schedule_job_log` VALUES (218, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-21 21:30:00');
INSERT INTO `schedule_job_log` VALUES (219, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-21 22:00:00');
INSERT INTO `schedule_job_log` VALUES (220, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-21 22:30:00');
INSERT INTO `schedule_job_log` VALUES (221, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-21 23:00:00');
INSERT INTO `schedule_job_log` VALUES (222, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-21 23:30:00');
INSERT INTO `schedule_job_log` VALUES (223, 1, 'testTask', 'renren', 0, NULL, 2, '2023-05-22 00:00:00');
INSERT INTO `schedule_job_log` VALUES (224, 1, 'testTask', 'renren', 0, NULL, 2, '2023-05-22 00:30:00');
INSERT INTO `schedule_job_log` VALUES (225, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 01:00:00');
INSERT INTO `schedule_job_log` VALUES (226, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-22 01:30:00');
INSERT INTO `schedule_job_log` VALUES (227, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-22 02:00:00');
INSERT INTO `schedule_job_log` VALUES (228, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 02:30:00');
INSERT INTO `schedule_job_log` VALUES (229, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 03:00:00');
INSERT INTO `schedule_job_log` VALUES (230, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 16:30:01');
INSERT INTO `schedule_job_log` VALUES (231, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 17:00:00');
INSERT INTO `schedule_job_log` VALUES (232, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 17:30:00');
INSERT INTO `schedule_job_log` VALUES (233, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 18:00:00');
INSERT INTO `schedule_job_log` VALUES (234, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 21:30:00');
INSERT INTO `schedule_job_log` VALUES (235, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-22 22:00:00');
INSERT INTO `schedule_job_log` VALUES (236, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-22 22:30:00');
INSERT INTO `schedule_job_log` VALUES (237, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 23:00:00');
INSERT INTO `schedule_job_log` VALUES (238, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-22 23:30:00');
INSERT INTO `schedule_job_log` VALUES (239, 1, 'testTask', 'renren', 0, NULL, 4, '2023-05-23 00:00:00');
INSERT INTO `schedule_job_log` VALUES (240, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-23 00:30:00');
INSERT INTO `schedule_job_log` VALUES (241, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-23 01:00:00');
INSERT INTO `schedule_job_log` VALUES (242, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-23 01:30:00');
INSERT INTO `schedule_job_log` VALUES (243, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-23 02:00:00');
INSERT INTO `schedule_job_log` VALUES (244, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-23 02:30:00');
INSERT INTO `schedule_job_log` VALUES (245, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-23 03:00:00');
INSERT INTO `schedule_job_log` VALUES (246, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-23 03:30:00');
INSERT INTO `schedule_job_log` VALUES (247, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-23 04:00:00');
INSERT INTO `schedule_job_log` VALUES (248, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-23 04:30:00');
INSERT INTO `schedule_job_log` VALUES (249, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-26 17:30:00');
INSERT INTO `schedule_job_log` VALUES (250, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-26 18:00:00');
INSERT INTO `schedule_job_log` VALUES (251, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-26 18:30:00');
INSERT INTO `schedule_job_log` VALUES (252, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-26 19:00:06');
INSERT INTO `schedule_job_log` VALUES (253, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-26 19:30:01');
INSERT INTO `schedule_job_log` VALUES (254, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-26 20:00:00');
INSERT INTO `schedule_job_log` VALUES (255, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-26 20:30:00');
INSERT INTO `schedule_job_log` VALUES (256, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-26 21:00:00');
INSERT INTO `schedule_job_log` VALUES (257, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-26 21:30:00');
INSERT INTO `schedule_job_log` VALUES (258, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-26 22:00:00');
INSERT INTO `schedule_job_log` VALUES (259, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-26 22:30:00');
INSERT INTO `schedule_job_log` VALUES (260, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-26 23:00:00');
INSERT INTO `schedule_job_log` VALUES (261, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-26 23:30:00');
INSERT INTO `schedule_job_log` VALUES (262, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 03:30:00');
INSERT INTO `schedule_job_log` VALUES (263, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 13:30:00');
INSERT INTO `schedule_job_log` VALUES (264, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 14:00:00');
INSERT INTO `schedule_job_log` VALUES (265, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-27 14:30:00');
INSERT INTO `schedule_job_log` VALUES (266, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 15:00:00');
INSERT INTO `schedule_job_log` VALUES (267, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 15:30:00');
INSERT INTO `schedule_job_log` VALUES (268, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 16:00:00');
INSERT INTO `schedule_job_log` VALUES (269, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-27 16:30:00');
INSERT INTO `schedule_job_log` VALUES (270, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-27 17:00:01');
INSERT INTO `schedule_job_log` VALUES (271, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 17:30:00');
INSERT INTO `schedule_job_log` VALUES (272, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 20:00:01');
INSERT INTO `schedule_job_log` VALUES (273, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-27 20:30:00');
INSERT INTO `schedule_job_log` VALUES (274, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-27 21:00:00');
INSERT INTO `schedule_job_log` VALUES (275, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 21:30:00');
INSERT INTO `schedule_job_log` VALUES (276, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 22:00:01');
INSERT INTO `schedule_job_log` VALUES (277, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 22:30:00');
INSERT INTO `schedule_job_log` VALUES (278, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-27 23:00:00');
INSERT INTO `schedule_job_log` VALUES (279, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-27 23:30:01');
INSERT INTO `schedule_job_log` VALUES (280, 1, 'testTask', 'renren', 0, NULL, 4, '2023-05-28 00:00:01');
INSERT INTO `schedule_job_log` VALUES (281, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-28 00:30:00');
INSERT INTO `schedule_job_log` VALUES (282, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 01:00:00');
INSERT INTO `schedule_job_log` VALUES (283, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 01:30:00');
INSERT INTO `schedule_job_log` VALUES (284, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 02:00:00');
INSERT INTO `schedule_job_log` VALUES (285, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-28 02:30:00');
INSERT INTO `schedule_job_log` VALUES (286, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 17:00:00');
INSERT INTO `schedule_job_log` VALUES (287, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 17:30:00');
INSERT INTO `schedule_job_log` VALUES (288, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-28 18:00:00');
INSERT INTO `schedule_job_log` VALUES (289, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-28 18:30:01');
INSERT INTO `schedule_job_log` VALUES (290, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 19:00:00');
INSERT INTO `schedule_job_log` VALUES (291, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 19:30:00');
INSERT INTO `schedule_job_log` VALUES (292, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 20:00:00');
INSERT INTO `schedule_job_log` VALUES (293, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-28 20:30:00');
INSERT INTO `schedule_job_log` VALUES (294, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 21:00:00');
INSERT INTO `schedule_job_log` VALUES (295, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 21:30:00');
INSERT INTO `schedule_job_log` VALUES (296, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 22:00:00');
INSERT INTO `schedule_job_log` VALUES (297, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-28 22:30:00');
INSERT INTO `schedule_job_log` VALUES (298, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-28 23:00:00');
INSERT INTO `schedule_job_log` VALUES (299, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-28 23:30:00');
INSERT INTO `schedule_job_log` VALUES (300, 1, 'testTask', 'renren', 0, NULL, 3, '2023-05-29 00:00:00');
INSERT INTO `schedule_job_log` VALUES (301, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 00:30:00');
INSERT INTO `schedule_job_log` VALUES (302, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 01:00:00');
INSERT INTO `schedule_job_log` VALUES (303, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 01:30:00');
INSERT INTO `schedule_job_log` VALUES (304, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 02:00:00');
INSERT INTO `schedule_job_log` VALUES (305, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 02:30:00');
INSERT INTO `schedule_job_log` VALUES (306, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 10:30:17');
INSERT INTO `schedule_job_log` VALUES (307, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 11:00:01');
INSERT INTO `schedule_job_log` VALUES (308, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 11:30:01');
INSERT INTO `schedule_job_log` VALUES (309, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 12:00:01');
INSERT INTO `schedule_job_log` VALUES (310, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 12:30:00');
INSERT INTO `schedule_job_log` VALUES (311, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 13:00:00');
INSERT INTO `schedule_job_log` VALUES (312, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 13:30:00');
INSERT INTO `schedule_job_log` VALUES (313, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 14:00:00');
INSERT INTO `schedule_job_log` VALUES (314, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 14:30:00');
INSERT INTO `schedule_job_log` VALUES (315, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 15:00:01');
INSERT INTO `schedule_job_log` VALUES (316, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 17:30:00');
INSERT INTO `schedule_job_log` VALUES (317, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 18:00:00');
INSERT INTO `schedule_job_log` VALUES (318, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 18:30:00');
INSERT INTO `schedule_job_log` VALUES (319, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 19:00:00');
INSERT INTO `schedule_job_log` VALUES (320, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 19:30:00');
INSERT INTO `schedule_job_log` VALUES (321, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 20:00:00');
INSERT INTO `schedule_job_log` VALUES (322, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 20:30:00');
INSERT INTO `schedule_job_log` VALUES (323, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 21:00:00');
INSERT INTO `schedule_job_log` VALUES (324, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 21:30:00');
INSERT INTO `schedule_job_log` VALUES (325, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 22:00:00');
INSERT INTO `schedule_job_log` VALUES (326, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 22:30:00');
INSERT INTO `schedule_job_log` VALUES (327, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-29 23:00:00');
INSERT INTO `schedule_job_log` VALUES (328, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-29 23:30:00');
INSERT INTO `schedule_job_log` VALUES (329, 1, 'testTask', 'renren', 0, NULL, 5, '2023-05-30 00:00:01');
INSERT INTO `schedule_job_log` VALUES (330, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 00:30:00');
INSERT INTO `schedule_job_log` VALUES (331, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 01:00:00');
INSERT INTO `schedule_job_log` VALUES (332, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 01:30:00');
INSERT INTO `schedule_job_log` VALUES (333, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-30 02:00:00');
INSERT INTO `schedule_job_log` VALUES (334, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-30 02:30:00');
INSERT INTO `schedule_job_log` VALUES (335, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 03:00:00');
INSERT INTO `schedule_job_log` VALUES (336, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-30 03:30:00');
INSERT INTO `schedule_job_log` VALUES (337, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 04:00:00');
INSERT INTO `schedule_job_log` VALUES (338, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-30 04:30:00');
INSERT INTO `schedule_job_log` VALUES (339, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 05:00:00');
INSERT INTO `schedule_job_log` VALUES (340, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 05:30:00');
INSERT INTO `schedule_job_log` VALUES (341, 1, 'testTask', 'renren', 0, NULL, 0, '2023-05-30 11:30:02');
INSERT INTO `schedule_job_log` VALUES (342, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 12:00:02');
INSERT INTO `schedule_job_log` VALUES (343, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 12:30:00');
INSERT INTO `schedule_job_log` VALUES (344, 1, 'testTask', 'renren', 0, NULL, 1, '2023-05-30 13:00:00');
INSERT INTO `schedule_job_log` VALUES (345, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-08 18:30:00');
INSERT INTO `schedule_job_log` VALUES (346, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 13:30:00');
INSERT INTO `schedule_job_log` VALUES (347, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 14:00:00');
INSERT INTO `schedule_job_log` VALUES (348, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 14:30:00');
INSERT INTO `schedule_job_log` VALUES (349, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-09 15:00:00');
INSERT INTO `schedule_job_log` VALUES (350, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-09 15:30:00');
INSERT INTO `schedule_job_log` VALUES (351, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-09 16:00:01');
INSERT INTO `schedule_job_log` VALUES (352, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 16:30:00');
INSERT INTO `schedule_job_log` VALUES (353, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 17:00:00');
INSERT INTO `schedule_job_log` VALUES (354, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-09 17:30:00');
INSERT INTO `schedule_job_log` VALUES (355, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-09 18:00:00');
INSERT INTO `schedule_job_log` VALUES (356, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-09 18:30:00');
INSERT INTO `schedule_job_log` VALUES (357, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 19:00:00');
INSERT INTO `schedule_job_log` VALUES (358, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 19:30:00');
INSERT INTO `schedule_job_log` VALUES (359, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 20:00:00');
INSERT INTO `schedule_job_log` VALUES (360, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 20:30:00');
INSERT INTO `schedule_job_log` VALUES (361, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 21:00:00');
INSERT INTO `schedule_job_log` VALUES (362, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-09 21:30:00');
INSERT INTO `schedule_job_log` VALUES (363, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 22:00:00');
INSERT INTO `schedule_job_log` VALUES (364, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 22:30:00');
INSERT INTO `schedule_job_log` VALUES (365, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-09 23:00:00');
INSERT INTO `schedule_job_log` VALUES (366, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-09 23:30:00');
INSERT INTO `schedule_job_log` VALUES (367, 1, 'testTask', 'renren', 0, NULL, 3, '2023-06-10 00:00:00');
INSERT INTO `schedule_job_log` VALUES (368, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 00:30:00');
INSERT INTO `schedule_job_log` VALUES (369, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 01:00:00');
INSERT INTO `schedule_job_log` VALUES (370, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 01:30:00');
INSERT INTO `schedule_job_log` VALUES (371, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 02:00:00');
INSERT INTO `schedule_job_log` VALUES (372, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 02:30:00');
INSERT INTO `schedule_job_log` VALUES (373, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 03:00:00');
INSERT INTO `schedule_job_log` VALUES (374, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 03:30:01');
INSERT INTO `schedule_job_log` VALUES (375, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 04:00:00');
INSERT INTO `schedule_job_log` VALUES (376, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 04:30:00');
INSERT INTO `schedule_job_log` VALUES (377, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 05:00:00');
INSERT INTO `schedule_job_log` VALUES (378, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 05:30:00');
INSERT INTO `schedule_job_log` VALUES (379, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 06:00:00');
INSERT INTO `schedule_job_log` VALUES (380, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 06:30:00');
INSERT INTO `schedule_job_log` VALUES (381, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 07:00:00');
INSERT INTO `schedule_job_log` VALUES (382, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 07:30:00');
INSERT INTO `schedule_job_log` VALUES (383, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 08:00:00');
INSERT INTO `schedule_job_log` VALUES (384, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 08:30:00');
INSERT INTO `schedule_job_log` VALUES (385, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 09:00:00');
INSERT INTO `schedule_job_log` VALUES (386, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 09:30:00');
INSERT INTO `schedule_job_log` VALUES (387, 1, 'testTask', 'renren', 0, NULL, 2, '2023-06-10 10:00:00');
INSERT INTO `schedule_job_log` VALUES (388, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 10:30:00');
INSERT INTO `schedule_job_log` VALUES (389, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 11:00:18');
INSERT INTO `schedule_job_log` VALUES (390, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 11:30:00');
INSERT INTO `schedule_job_log` VALUES (391, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 12:00:00');
INSERT INTO `schedule_job_log` VALUES (392, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 12:30:00');
INSERT INTO `schedule_job_log` VALUES (393, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 13:00:01');
INSERT INTO `schedule_job_log` VALUES (394, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 13:30:00');
INSERT INTO `schedule_job_log` VALUES (395, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 14:00:00');
INSERT INTO `schedule_job_log` VALUES (396, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 14:30:00');
INSERT INTO `schedule_job_log` VALUES (397, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 15:00:00');
INSERT INTO `schedule_job_log` VALUES (398, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 15:30:00');
INSERT INTO `schedule_job_log` VALUES (399, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 16:00:00');
INSERT INTO `schedule_job_log` VALUES (400, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 16:30:00');
INSERT INTO `schedule_job_log` VALUES (401, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 17:00:00');
INSERT INTO `schedule_job_log` VALUES (402, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 17:30:00');
INSERT INTO `schedule_job_log` VALUES (403, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 18:30:00');
INSERT INTO `schedule_job_log` VALUES (404, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 19:00:00');
INSERT INTO `schedule_job_log` VALUES (405, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 19:30:00');
INSERT INTO `schedule_job_log` VALUES (406, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 20:00:00');
INSERT INTO `schedule_job_log` VALUES (407, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 20:30:00');
INSERT INTO `schedule_job_log` VALUES (408, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 21:00:00');
INSERT INTO `schedule_job_log` VALUES (409, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 21:30:00');
INSERT INTO `schedule_job_log` VALUES (410, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 22:00:00');
INSERT INTO `schedule_job_log` VALUES (411, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 22:30:00');
INSERT INTO `schedule_job_log` VALUES (412, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-10 23:00:00');
INSERT INTO `schedule_job_log` VALUES (413, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-10 23:30:00');
INSERT INTO `schedule_job_log` VALUES (414, 1, 'testTask', 'renren', 0, NULL, 5, '2023-06-11 00:00:00');
INSERT INTO `schedule_job_log` VALUES (415, 1, 'testTask', 'renren', 0, NULL, 2, '2023-06-11 00:30:00');
INSERT INTO `schedule_job_log` VALUES (416, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-11 01:00:00');
INSERT INTO `schedule_job_log` VALUES (417, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-11 01:30:00');
INSERT INTO `schedule_job_log` VALUES (418, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-11 02:00:00');
INSERT INTO `schedule_job_log` VALUES (419, 1, 'testTask', 'renren', 0, NULL, 1, '2023-06-11 02:30:00');
INSERT INTO `schedule_job_log` VALUES (420, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-11 03:00:00');
INSERT INTO `schedule_job_log` VALUES (421, 1, 'testTask', 'renren', 0, NULL, 0, '2023-06-11 03:30:00');
INSERT INTO `schedule_job_log` VALUES (422, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-10 00:00:00');
INSERT INTO `schedule_job_log` VALUES (423, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-10 01:00:00');
INSERT INTO `schedule_job_log` VALUES (424, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 13:30:00');
INSERT INTO `schedule_job_log` VALUES (425, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 14:00:00');
INSERT INTO `schedule_job_log` VALUES (426, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-13 14:30:00');
INSERT INTO `schedule_job_log` VALUES (427, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 15:00:00');
INSERT INTO `schedule_job_log` VALUES (428, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-13 15:30:00');
INSERT INTO `schedule_job_log` VALUES (429, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-13 16:00:00');
INSERT INTO `schedule_job_log` VALUES (430, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 16:30:00');
INSERT INTO `schedule_job_log` VALUES (431, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 17:00:00');
INSERT INTO `schedule_job_log` VALUES (432, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-13 17:30:01');
INSERT INTO `schedule_job_log` VALUES (433, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-13 18:00:00');
INSERT INTO `schedule_job_log` VALUES (434, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-13 18:30:00');
INSERT INTO `schedule_job_log` VALUES (435, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 19:00:01');
INSERT INTO `schedule_job_log` VALUES (436, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 19:30:00');
INSERT INTO `schedule_job_log` VALUES (437, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-13 20:00:00');
INSERT INTO `schedule_job_log` VALUES (438, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-13 20:30:00');
INSERT INTO `schedule_job_log` VALUES (439, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-13 21:00:00');
INSERT INTO `schedule_job_log` VALUES (440, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 21:30:00');
INSERT INTO `schedule_job_log` VALUES (441, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-13 22:00:00');
INSERT INTO `schedule_job_log` VALUES (442, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 22:30:00');
INSERT INTO `schedule_job_log` VALUES (443, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 23:00:00');
INSERT INTO `schedule_job_log` VALUES (444, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-13 23:30:00');
INSERT INTO `schedule_job_log` VALUES (445, 1, 'testTask', 'renren', 0, NULL, 6, '2023-07-14 00:00:00');
INSERT INTO `schedule_job_log` VALUES (446, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 00:30:00');
INSERT INTO `schedule_job_log` VALUES (447, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 01:00:00');
INSERT INTO `schedule_job_log` VALUES (448, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 01:30:00');
INSERT INTO `schedule_job_log` VALUES (449, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 02:00:00');
INSERT INTO `schedule_job_log` VALUES (450, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 02:30:00');
INSERT INTO `schedule_job_log` VALUES (451, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 03:00:00');
INSERT INTO `schedule_job_log` VALUES (452, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 03:30:00');
INSERT INTO `schedule_job_log` VALUES (453, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 04:00:00');
INSERT INTO `schedule_job_log` VALUES (454, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 04:30:00');
INSERT INTO `schedule_job_log` VALUES (455, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 05:00:01');
INSERT INTO `schedule_job_log` VALUES (456, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 05:30:02');
INSERT INTO `schedule_job_log` VALUES (457, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 06:00:00');
INSERT INTO `schedule_job_log` VALUES (458, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 06:30:00');
INSERT INTO `schedule_job_log` VALUES (459, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 07:00:00');
INSERT INTO `schedule_job_log` VALUES (460, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 07:30:00');
INSERT INTO `schedule_job_log` VALUES (461, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 08:00:00');
INSERT INTO `schedule_job_log` VALUES (462, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 08:30:00');
INSERT INTO `schedule_job_log` VALUES (463, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 09:00:00');
INSERT INTO `schedule_job_log` VALUES (464, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 09:30:00');
INSERT INTO `schedule_job_log` VALUES (465, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 10:00:00');
INSERT INTO `schedule_job_log` VALUES (466, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 10:30:00');
INSERT INTO `schedule_job_log` VALUES (467, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 11:00:00');
INSERT INTO `schedule_job_log` VALUES (468, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 11:30:00');
INSERT INTO `schedule_job_log` VALUES (469, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 12:00:00');
INSERT INTO `schedule_job_log` VALUES (470, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 12:30:00');
INSERT INTO `schedule_job_log` VALUES (471, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 13:00:00');
INSERT INTO `schedule_job_log` VALUES (472, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 13:30:00');
INSERT INTO `schedule_job_log` VALUES (473, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 14:00:00');
INSERT INTO `schedule_job_log` VALUES (474, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 14:30:00');
INSERT INTO `schedule_job_log` VALUES (475, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 15:00:00');
INSERT INTO `schedule_job_log` VALUES (476, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 15:30:00');
INSERT INTO `schedule_job_log` VALUES (477, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 16:00:00');
INSERT INTO `schedule_job_log` VALUES (478, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 16:30:00');
INSERT INTO `schedule_job_log` VALUES (479, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 17:00:00');
INSERT INTO `schedule_job_log` VALUES (480, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 17:30:00');
INSERT INTO `schedule_job_log` VALUES (481, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 18:00:00');
INSERT INTO `schedule_job_log` VALUES (482, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 18:30:00');
INSERT INTO `schedule_job_log` VALUES (483, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 19:00:00');
INSERT INTO `schedule_job_log` VALUES (484, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 19:30:00');
INSERT INTO `schedule_job_log` VALUES (485, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 20:00:00');
INSERT INTO `schedule_job_log` VALUES (486, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 20:30:00');
INSERT INTO `schedule_job_log` VALUES (487, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 21:00:00');
INSERT INTO `schedule_job_log` VALUES (488, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-14 21:30:00');
INSERT INTO `schedule_job_log` VALUES (489, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 22:00:00');
INSERT INTO `schedule_job_log` VALUES (490, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 22:30:00');
INSERT INTO `schedule_job_log` VALUES (491, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 23:00:00');
INSERT INTO `schedule_job_log` VALUES (492, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-14 23:30:00');
INSERT INTO `schedule_job_log` VALUES (493, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-15 00:00:00');
INSERT INTO `schedule_job_log` VALUES (494, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 00:30:00');
INSERT INTO `schedule_job_log` VALUES (495, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 01:00:00');
INSERT INTO `schedule_job_log` VALUES (496, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 01:30:00');
INSERT INTO `schedule_job_log` VALUES (497, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 02:00:00');
INSERT INTO `schedule_job_log` VALUES (498, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 02:30:00');
INSERT INTO `schedule_job_log` VALUES (499, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 03:00:00');
INSERT INTO `schedule_job_log` VALUES (500, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 03:30:00');
INSERT INTO `schedule_job_log` VALUES (501, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 04:00:00');
INSERT INTO `schedule_job_log` VALUES (502, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 04:30:00');
INSERT INTO `schedule_job_log` VALUES (503, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-15 05:00:00');
INSERT INTO `schedule_job_log` VALUES (504, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 05:30:00');
INSERT INTO `schedule_job_log` VALUES (505, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 06:00:00');
INSERT INTO `schedule_job_log` VALUES (506, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 06:30:00');
INSERT INTO `schedule_job_log` VALUES (507, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 07:00:00');
INSERT INTO `schedule_job_log` VALUES (508, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 07:30:00');
INSERT INTO `schedule_job_log` VALUES (509, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 08:00:01');
INSERT INTO `schedule_job_log` VALUES (510, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 08:30:00');
INSERT INTO `schedule_job_log` VALUES (511, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 09:00:00');
INSERT INTO `schedule_job_log` VALUES (512, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 09:30:00');
INSERT INTO `schedule_job_log` VALUES (513, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 10:00:00');
INSERT INTO `schedule_job_log` VALUES (514, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 10:30:00');
INSERT INTO `schedule_job_log` VALUES (515, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 11:00:00');
INSERT INTO `schedule_job_log` VALUES (516, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 11:30:00');
INSERT INTO `schedule_job_log` VALUES (517, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 12:00:00');
INSERT INTO `schedule_job_log` VALUES (518, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 12:30:00');
INSERT INTO `schedule_job_log` VALUES (519, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 13:00:00');
INSERT INTO `schedule_job_log` VALUES (520, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 13:30:00');
INSERT INTO `schedule_job_log` VALUES (521, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 14:00:00');
INSERT INTO `schedule_job_log` VALUES (522, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 14:30:00');
INSERT INTO `schedule_job_log` VALUES (523, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 15:00:00');
INSERT INTO `schedule_job_log` VALUES (524, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 15:30:00');
INSERT INTO `schedule_job_log` VALUES (525, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 16:00:00');
INSERT INTO `schedule_job_log` VALUES (526, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 16:30:00');
INSERT INTO `schedule_job_log` VALUES (527, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 17:00:00');
INSERT INTO `schedule_job_log` VALUES (528, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 17:30:00');
INSERT INTO `schedule_job_log` VALUES (529, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 18:00:00');
INSERT INTO `schedule_job_log` VALUES (530, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 18:30:00');
INSERT INTO `schedule_job_log` VALUES (531, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 19:00:00');
INSERT INTO `schedule_job_log` VALUES (532, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 19:30:00');
INSERT INTO `schedule_job_log` VALUES (533, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 20:00:00');
INSERT INTO `schedule_job_log` VALUES (534, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-15 20:30:00');
INSERT INTO `schedule_job_log` VALUES (535, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 21:00:00');
INSERT INTO `schedule_job_log` VALUES (536, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 21:30:00');
INSERT INTO `schedule_job_log` VALUES (537, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 22:00:00');
INSERT INTO `schedule_job_log` VALUES (538, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 22:30:00');
INSERT INTO `schedule_job_log` VALUES (539, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 23:00:00');
INSERT INTO `schedule_job_log` VALUES (540, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-15 23:30:00');
INSERT INTO `schedule_job_log` VALUES (541, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-16 00:00:01');
INSERT INTO `schedule_job_log` VALUES (542, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-16 00:30:00');
INSERT INTO `schedule_job_log` VALUES (543, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 01:00:00');
INSERT INTO `schedule_job_log` VALUES (544, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 01:30:00');
INSERT INTO `schedule_job_log` VALUES (545, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 02:00:00');
INSERT INTO `schedule_job_log` VALUES (546, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 02:30:00');
INSERT INTO `schedule_job_log` VALUES (547, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 03:00:00');
INSERT INTO `schedule_job_log` VALUES (548, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 03:30:00');
INSERT INTO `schedule_job_log` VALUES (549, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 04:00:00');
INSERT INTO `schedule_job_log` VALUES (550, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 04:30:00');
INSERT INTO `schedule_job_log` VALUES (551, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 05:00:00');
INSERT INTO `schedule_job_log` VALUES (552, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 05:30:00');
INSERT INTO `schedule_job_log` VALUES (553, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 06:00:00');
INSERT INTO `schedule_job_log` VALUES (554, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 06:30:00');
INSERT INTO `schedule_job_log` VALUES (555, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 07:00:00');
INSERT INTO `schedule_job_log` VALUES (556, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 07:30:00');
INSERT INTO `schedule_job_log` VALUES (557, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 08:00:00');
INSERT INTO `schedule_job_log` VALUES (558, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 08:30:00');
INSERT INTO `schedule_job_log` VALUES (559, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 09:00:00');
INSERT INTO `schedule_job_log` VALUES (560, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 09:30:00');
INSERT INTO `schedule_job_log` VALUES (561, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 10:00:00');
INSERT INTO `schedule_job_log` VALUES (562, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 10:30:00');
INSERT INTO `schedule_job_log` VALUES (563, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 11:00:00');
INSERT INTO `schedule_job_log` VALUES (564, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 11:30:00');
INSERT INTO `schedule_job_log` VALUES (565, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 12:00:00');
INSERT INTO `schedule_job_log` VALUES (566, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 12:30:00');
INSERT INTO `schedule_job_log` VALUES (567, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 13:00:00');
INSERT INTO `schedule_job_log` VALUES (568, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 13:30:00');
INSERT INTO `schedule_job_log` VALUES (569, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 14:00:00');
INSERT INTO `schedule_job_log` VALUES (570, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 14:30:00');
INSERT INTO `schedule_job_log` VALUES (571, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 15:00:00');
INSERT INTO `schedule_job_log` VALUES (572, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 15:30:00');
INSERT INTO `schedule_job_log` VALUES (573, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 16:00:00');
INSERT INTO `schedule_job_log` VALUES (574, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 16:30:00');
INSERT INTO `schedule_job_log` VALUES (575, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 17:00:00');
INSERT INTO `schedule_job_log` VALUES (576, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-16 17:30:00');
INSERT INTO `schedule_job_log` VALUES (577, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 18:00:00');
INSERT INTO `schedule_job_log` VALUES (578, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 18:30:00');
INSERT INTO `schedule_job_log` VALUES (579, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 19:00:00');
INSERT INTO `schedule_job_log` VALUES (580, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-16 19:30:00');
INSERT INTO `schedule_job_log` VALUES (581, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 20:00:00');
INSERT INTO `schedule_job_log` VALUES (582, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 20:30:00');
INSERT INTO `schedule_job_log` VALUES (583, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 21:00:00');
INSERT INTO `schedule_job_log` VALUES (584, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 21:30:00');
INSERT INTO `schedule_job_log` VALUES (585, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 22:00:00');
INSERT INTO `schedule_job_log` VALUES (586, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-16 22:30:00');
INSERT INTO `schedule_job_log` VALUES (587, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 23:00:00');
INSERT INTO `schedule_job_log` VALUES (588, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-16 23:30:00');
INSERT INTO `schedule_job_log` VALUES (589, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-17 00:00:00');
INSERT INTO `schedule_job_log` VALUES (590, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 00:30:00');
INSERT INTO `schedule_job_log` VALUES (591, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 01:00:01');
INSERT INTO `schedule_job_log` VALUES (592, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 01:30:00');
INSERT INTO `schedule_job_log` VALUES (593, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 02:00:00');
INSERT INTO `schedule_job_log` VALUES (594, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 02:30:00');
INSERT INTO `schedule_job_log` VALUES (595, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 03:00:00');
INSERT INTO `schedule_job_log` VALUES (596, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-17 03:30:00');
INSERT INTO `schedule_job_log` VALUES (597, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 04:00:00');
INSERT INTO `schedule_job_log` VALUES (598, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 04:30:00');
INSERT INTO `schedule_job_log` VALUES (599, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-17 05:00:00');
INSERT INTO `schedule_job_log` VALUES (600, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 05:30:00');
INSERT INTO `schedule_job_log` VALUES (601, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 06:00:00');
INSERT INTO `schedule_job_log` VALUES (602, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-17 06:30:00');
INSERT INTO `schedule_job_log` VALUES (603, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-17 07:00:00');
INSERT INTO `schedule_job_log` VALUES (604, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 07:30:00');
INSERT INTO `schedule_job_log` VALUES (605, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 08:00:00');
INSERT INTO `schedule_job_log` VALUES (606, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 08:30:00');
INSERT INTO `schedule_job_log` VALUES (607, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 09:00:00');
INSERT INTO `schedule_job_log` VALUES (608, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 09:30:00');
INSERT INTO `schedule_job_log` VALUES (609, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-17 10:00:00');
INSERT INTO `schedule_job_log` VALUES (610, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 10:30:00');
INSERT INTO `schedule_job_log` VALUES (611, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 11:00:00');
INSERT INTO `schedule_job_log` VALUES (612, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 11:30:00');
INSERT INTO `schedule_job_log` VALUES (613, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 12:00:00');
INSERT INTO `schedule_job_log` VALUES (614, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 12:30:00');
INSERT INTO `schedule_job_log` VALUES (615, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 13:00:00');
INSERT INTO `schedule_job_log` VALUES (616, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-17 13:30:00');
INSERT INTO `schedule_job_log` VALUES (617, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 14:00:00');
INSERT INTO `schedule_job_log` VALUES (618, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 14:30:00');
INSERT INTO `schedule_job_log` VALUES (619, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-17 15:00:00');
INSERT INTO `schedule_job_log` VALUES (620, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 15:30:01');
INSERT INTO `schedule_job_log` VALUES (621, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 16:00:00');
INSERT INTO `schedule_job_log` VALUES (622, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 16:30:00');
INSERT INTO `schedule_job_log` VALUES (623, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 17:00:00');
INSERT INTO `schedule_job_log` VALUES (624, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 17:30:00');
INSERT INTO `schedule_job_log` VALUES (625, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-17 18:00:00');
INSERT INTO `schedule_job_log` VALUES (626, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 18:30:00');
INSERT INTO `schedule_job_log` VALUES (627, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-17 19:00:00');
INSERT INTO `schedule_job_log` VALUES (628, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 19:30:00');
INSERT INTO `schedule_job_log` VALUES (629, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 20:00:00');
INSERT INTO `schedule_job_log` VALUES (630, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 20:30:00');
INSERT INTO `schedule_job_log` VALUES (631, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-17 21:00:00');
INSERT INTO `schedule_job_log` VALUES (632, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 21:30:00');
INSERT INTO `schedule_job_log` VALUES (633, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 22:00:00');
INSERT INTO `schedule_job_log` VALUES (634, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 22:30:00');
INSERT INTO `schedule_job_log` VALUES (635, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-17 23:00:00');
INSERT INTO `schedule_job_log` VALUES (636, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-17 23:30:00');
INSERT INTO `schedule_job_log` VALUES (637, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-18 00:00:00');
INSERT INTO `schedule_job_log` VALUES (638, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 00:30:00');
INSERT INTO `schedule_job_log` VALUES (639, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 01:00:00');
INSERT INTO `schedule_job_log` VALUES (640, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 01:30:00');
INSERT INTO `schedule_job_log` VALUES (641, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-18 16:00:00');
INSERT INTO `schedule_job_log` VALUES (642, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-18 16:30:00');
INSERT INTO `schedule_job_log` VALUES (643, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-18 17:00:00');
INSERT INTO `schedule_job_log` VALUES (644, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 17:30:00');
INSERT INTO `schedule_job_log` VALUES (645, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 18:00:00');
INSERT INTO `schedule_job_log` VALUES (646, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 18:30:00');
INSERT INTO `schedule_job_log` VALUES (647, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 19:00:00');
INSERT INTO `schedule_job_log` VALUES (648, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 19:30:00');
INSERT INTO `schedule_job_log` VALUES (649, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 20:00:00');
INSERT INTO `schedule_job_log` VALUES (650, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 20:30:00');
INSERT INTO `schedule_job_log` VALUES (651, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 21:00:00');
INSERT INTO `schedule_job_log` VALUES (652, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-18 21:30:00');
INSERT INTO `schedule_job_log` VALUES (653, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 22:00:00');
INSERT INTO `schedule_job_log` VALUES (654, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 22:30:00');
INSERT INTO `schedule_job_log` VALUES (655, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 23:00:00');
INSERT INTO `schedule_job_log` VALUES (656, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-18 23:30:01');
INSERT INTO `schedule_job_log` VALUES (657, 1, 'testTask', 'renren', 0, NULL, 3, '2023-07-19 00:00:00');
INSERT INTO `schedule_job_log` VALUES (658, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 00:30:00');
INSERT INTO `schedule_job_log` VALUES (659, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 01:00:00');
INSERT INTO `schedule_job_log` VALUES (660, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 01:30:00');
INSERT INTO `schedule_job_log` VALUES (661, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 02:00:00');
INSERT INTO `schedule_job_log` VALUES (662, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 02:30:00');
INSERT INTO `schedule_job_log` VALUES (663, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 03:00:00');
INSERT INTO `schedule_job_log` VALUES (664, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 03:30:00');
INSERT INTO `schedule_job_log` VALUES (665, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 04:00:00');
INSERT INTO `schedule_job_log` VALUES (666, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 04:30:00');
INSERT INTO `schedule_job_log` VALUES (667, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 05:00:00');
INSERT INTO `schedule_job_log` VALUES (668, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 05:30:00');
INSERT INTO `schedule_job_log` VALUES (669, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 06:00:00');
INSERT INTO `schedule_job_log` VALUES (670, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 06:30:01');
INSERT INTO `schedule_job_log` VALUES (671, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 07:00:00');
INSERT INTO `schedule_job_log` VALUES (672, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 07:30:00');
INSERT INTO `schedule_job_log` VALUES (673, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 08:00:00');
INSERT INTO `schedule_job_log` VALUES (674, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 08:30:00');
INSERT INTO `schedule_job_log` VALUES (675, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 09:00:00');
INSERT INTO `schedule_job_log` VALUES (676, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 09:30:00');
INSERT INTO `schedule_job_log` VALUES (677, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 10:00:00');
INSERT INTO `schedule_job_log` VALUES (678, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 10:30:00');
INSERT INTO `schedule_job_log` VALUES (679, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 11:00:00');
INSERT INTO `schedule_job_log` VALUES (680, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 11:30:00');
INSERT INTO `schedule_job_log` VALUES (681, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 12:00:00');
INSERT INTO `schedule_job_log` VALUES (682, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 12:30:00');
INSERT INTO `schedule_job_log` VALUES (683, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 13:00:00');
INSERT INTO `schedule_job_log` VALUES (684, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 13:30:00');
INSERT INTO `schedule_job_log` VALUES (685, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 14:00:00');
INSERT INTO `schedule_job_log` VALUES (686, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 14:30:00');
INSERT INTO `schedule_job_log` VALUES (687, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 15:00:00');
INSERT INTO `schedule_job_log` VALUES (688, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 15:30:00');
INSERT INTO `schedule_job_log` VALUES (689, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 16:00:00');
INSERT INTO `schedule_job_log` VALUES (690, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 16:30:00');
INSERT INTO `schedule_job_log` VALUES (691, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 17:00:00');
INSERT INTO `schedule_job_log` VALUES (692, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 17:30:00');
INSERT INTO `schedule_job_log` VALUES (693, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 18:00:00');
INSERT INTO `schedule_job_log` VALUES (694, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 18:30:00');
INSERT INTO `schedule_job_log` VALUES (695, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 19:00:00');
INSERT INTO `schedule_job_log` VALUES (696, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 19:30:00');
INSERT INTO `schedule_job_log` VALUES (697, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 20:00:00');
INSERT INTO `schedule_job_log` VALUES (698, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 20:30:00');
INSERT INTO `schedule_job_log` VALUES (699, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 21:00:00');
INSERT INTO `schedule_job_log` VALUES (700, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 21:30:00');
INSERT INTO `schedule_job_log` VALUES (701, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 22:00:00');
INSERT INTO `schedule_job_log` VALUES (702, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 22:30:00');
INSERT INTO `schedule_job_log` VALUES (703, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-19 23:00:00');
INSERT INTO `schedule_job_log` VALUES (704, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-19 23:30:00');
INSERT INTO `schedule_job_log` VALUES (705, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-20 00:00:00');
INSERT INTO `schedule_job_log` VALUES (706, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 00:30:00');
INSERT INTO `schedule_job_log` VALUES (707, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 01:00:00');
INSERT INTO `schedule_job_log` VALUES (708, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 01:30:00');
INSERT INTO `schedule_job_log` VALUES (709, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 02:00:00');
INSERT INTO `schedule_job_log` VALUES (710, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 02:30:00');
INSERT INTO `schedule_job_log` VALUES (711, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 03:00:00');
INSERT INTO `schedule_job_log` VALUES (712, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 03:30:00');
INSERT INTO `schedule_job_log` VALUES (713, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 04:00:00');
INSERT INTO `schedule_job_log` VALUES (714, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 04:30:00');
INSERT INTO `schedule_job_log` VALUES (715, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 05:00:00');
INSERT INTO `schedule_job_log` VALUES (716, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 05:30:00');
INSERT INTO `schedule_job_log` VALUES (717, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 06:00:00');
INSERT INTO `schedule_job_log` VALUES (718, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 06:30:00');
INSERT INTO `schedule_job_log` VALUES (719, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 07:00:00');
INSERT INTO `schedule_job_log` VALUES (720, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 07:30:00');
INSERT INTO `schedule_job_log` VALUES (721, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 08:00:00');
INSERT INTO `schedule_job_log` VALUES (722, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 08:30:00');
INSERT INTO `schedule_job_log` VALUES (723, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 09:00:00');
INSERT INTO `schedule_job_log` VALUES (724, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 09:30:00');
INSERT INTO `schedule_job_log` VALUES (725, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 10:00:00');
INSERT INTO `schedule_job_log` VALUES (726, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 10:30:00');
INSERT INTO `schedule_job_log` VALUES (727, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 11:00:00');
INSERT INTO `schedule_job_log` VALUES (728, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 11:30:00');
INSERT INTO `schedule_job_log` VALUES (729, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 12:00:00');
INSERT INTO `schedule_job_log` VALUES (730, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 12:30:00');
INSERT INTO `schedule_job_log` VALUES (731, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 13:00:00');
INSERT INTO `schedule_job_log` VALUES (732, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 13:30:00');
INSERT INTO `schedule_job_log` VALUES (733, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 14:00:00');
INSERT INTO `schedule_job_log` VALUES (734, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 14:30:00');
INSERT INTO `schedule_job_log` VALUES (735, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 15:00:00');
INSERT INTO `schedule_job_log` VALUES (736, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 15:30:00');
INSERT INTO `schedule_job_log` VALUES (737, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 16:00:00');
INSERT INTO `schedule_job_log` VALUES (738, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 16:30:00');
INSERT INTO `schedule_job_log` VALUES (739, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 17:00:00');
INSERT INTO `schedule_job_log` VALUES (740, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 17:30:00');
INSERT INTO `schedule_job_log` VALUES (741, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 18:00:00');
INSERT INTO `schedule_job_log` VALUES (742, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 18:30:00');
INSERT INTO `schedule_job_log` VALUES (743, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 19:00:00');
INSERT INTO `schedule_job_log` VALUES (744, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 19:30:00');
INSERT INTO `schedule_job_log` VALUES (745, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 21:30:00');
INSERT INTO `schedule_job_log` VALUES (746, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 22:00:00');
INSERT INTO `schedule_job_log` VALUES (747, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 22:30:00');
INSERT INTO `schedule_job_log` VALUES (748, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-20 23:00:00');
INSERT INTO `schedule_job_log` VALUES (749, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-20 23:30:00');
INSERT INTO `schedule_job_log` VALUES (750, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-21 00:00:00');
INSERT INTO `schedule_job_log` VALUES (751, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 00:30:00');
INSERT INTO `schedule_job_log` VALUES (752, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 01:00:00');
INSERT INTO `schedule_job_log` VALUES (753, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 01:30:00');
INSERT INTO `schedule_job_log` VALUES (754, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 02:00:00');
INSERT INTO `schedule_job_log` VALUES (755, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 02:30:00');
INSERT INTO `schedule_job_log` VALUES (756, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 03:00:00');
INSERT INTO `schedule_job_log` VALUES (757, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 03:30:00');
INSERT INTO `schedule_job_log` VALUES (758, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 04:00:00');
INSERT INTO `schedule_job_log` VALUES (759, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 04:30:00');
INSERT INTO `schedule_job_log` VALUES (760, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 05:00:00');
INSERT INTO `schedule_job_log` VALUES (761, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 05:30:00');
INSERT INTO `schedule_job_log` VALUES (762, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 06:00:00');
INSERT INTO `schedule_job_log` VALUES (763, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 06:30:00');
INSERT INTO `schedule_job_log` VALUES (764, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 07:00:00');
INSERT INTO `schedule_job_log` VALUES (765, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 07:30:00');
INSERT INTO `schedule_job_log` VALUES (766, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 08:00:00');
INSERT INTO `schedule_job_log` VALUES (767, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 08:30:00');
INSERT INTO `schedule_job_log` VALUES (768, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 09:00:00');
INSERT INTO `schedule_job_log` VALUES (769, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 09:30:00');
INSERT INTO `schedule_job_log` VALUES (770, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 10:00:00');
INSERT INTO `schedule_job_log` VALUES (771, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 10:30:00');
INSERT INTO `schedule_job_log` VALUES (772, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 11:00:00');
INSERT INTO `schedule_job_log` VALUES (773, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 11:30:00');
INSERT INTO `schedule_job_log` VALUES (774, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 12:00:03');
INSERT INTO `schedule_job_log` VALUES (775, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 12:30:00');
INSERT INTO `schedule_job_log` VALUES (776, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 13:00:00');
INSERT INTO `schedule_job_log` VALUES (777, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 13:30:00');
INSERT INTO `schedule_job_log` VALUES (778, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 14:00:00');
INSERT INTO `schedule_job_log` VALUES (779, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 14:30:00');
INSERT INTO `schedule_job_log` VALUES (780, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 15:00:00');
INSERT INTO `schedule_job_log` VALUES (781, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 15:30:00');
INSERT INTO `schedule_job_log` VALUES (782, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 16:00:00');
INSERT INTO `schedule_job_log` VALUES (783, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 16:30:00');
INSERT INTO `schedule_job_log` VALUES (784, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 17:00:04');
INSERT INTO `schedule_job_log` VALUES (785, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 17:30:00');
INSERT INTO `schedule_job_log` VALUES (786, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 18:00:00');
INSERT INTO `schedule_job_log` VALUES (787, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 18:30:00');
INSERT INTO `schedule_job_log` VALUES (788, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 19:00:00');
INSERT INTO `schedule_job_log` VALUES (789, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 19:30:00');
INSERT INTO `schedule_job_log` VALUES (790, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 20:00:00');
INSERT INTO `schedule_job_log` VALUES (791, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 20:30:01');
INSERT INTO `schedule_job_log` VALUES (792, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-21 21:00:00');
INSERT INTO `schedule_job_log` VALUES (793, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-21 21:30:00');
INSERT INTO `schedule_job_log` VALUES (794, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-22 00:30:00');
INSERT INTO `schedule_job_log` VALUES (795, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-22 01:00:00');
INSERT INTO `schedule_job_log` VALUES (796, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-22 01:30:00');
INSERT INTO `schedule_job_log` VALUES (797, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-22 02:00:00');
INSERT INTO `schedule_job_log` VALUES (798, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-22 02:30:00');
INSERT INTO `schedule_job_log` VALUES (799, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 13:00:00');
INSERT INTO `schedule_job_log` VALUES (800, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 13:30:00');
INSERT INTO `schedule_job_log` VALUES (801, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 14:00:00');
INSERT INTO `schedule_job_log` VALUES (802, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-26 14:30:00');
INSERT INTO `schedule_job_log` VALUES (803, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 15:00:00');
INSERT INTO `schedule_job_log` VALUES (804, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 15:30:00');
INSERT INTO `schedule_job_log` VALUES (805, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-26 16:00:00');
INSERT INTO `schedule_job_log` VALUES (806, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 16:30:00');
INSERT INTO `schedule_job_log` VALUES (807, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 17:00:00');
INSERT INTO `schedule_job_log` VALUES (808, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 17:30:00');
INSERT INTO `schedule_job_log` VALUES (809, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 18:00:00');
INSERT INTO `schedule_job_log` VALUES (810, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-26 18:30:00');
INSERT INTO `schedule_job_log` VALUES (811, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-26 19:00:00');
INSERT INTO `schedule_job_log` VALUES (812, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 19:30:00');
INSERT INTO `schedule_job_log` VALUES (813, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-26 20:00:00');
INSERT INTO `schedule_job_log` VALUES (814, 1, 'testTask', 'renren', 0, NULL, 2, '2023-07-26 20:30:00');
INSERT INTO `schedule_job_log` VALUES (815, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-26 21:00:00');
INSERT INTO `schedule_job_log` VALUES (816, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-26 21:30:00');
INSERT INTO `schedule_job_log` VALUES (817, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-26 22:00:00');
INSERT INTO `schedule_job_log` VALUES (818, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 01:00:01');
INSERT INTO `schedule_job_log` VALUES (819, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 09:00:00');
INSERT INTO `schedule_job_log` VALUES (820, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 11:30:00');
INSERT INTO `schedule_job_log` VALUES (821, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-27 12:00:01');
INSERT INTO `schedule_job_log` VALUES (822, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 12:30:00');
INSERT INTO `schedule_job_log` VALUES (823, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-27 13:00:00');
INSERT INTO `schedule_job_log` VALUES (824, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-27 13:30:00');
INSERT INTO `schedule_job_log` VALUES (825, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 14:00:00');
INSERT INTO `schedule_job_log` VALUES (826, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-27 14:30:00');
INSERT INTO `schedule_job_log` VALUES (827, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 15:00:00');
INSERT INTO `schedule_job_log` VALUES (828, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 15:30:00');
INSERT INTO `schedule_job_log` VALUES (829, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 16:00:00');
INSERT INTO `schedule_job_log` VALUES (830, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-27 16:30:00');
INSERT INTO `schedule_job_log` VALUES (831, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-27 17:00:00');
INSERT INTO `schedule_job_log` VALUES (832, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 17:30:00');
INSERT INTO `schedule_job_log` VALUES (833, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 18:00:00');
INSERT INTO `schedule_job_log` VALUES (834, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 18:30:00');
INSERT INTO `schedule_job_log` VALUES (835, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 19:00:00');
INSERT INTO `schedule_job_log` VALUES (836, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-27 19:30:00');
INSERT INTO `schedule_job_log` VALUES (837, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 20:00:02');
INSERT INTO `schedule_job_log` VALUES (838, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 20:30:00');
INSERT INTO `schedule_job_log` VALUES (839, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 21:00:00');
INSERT INTO `schedule_job_log` VALUES (840, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 21:30:00');
INSERT INTO `schedule_job_log` VALUES (841, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-27 22:00:00');
INSERT INTO `schedule_job_log` VALUES (842, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-27 22:30:00');
INSERT INTO `schedule_job_log` VALUES (843, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 23:00:00');
INSERT INTO `schedule_job_log` VALUES (844, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-27 23:30:00');
INSERT INTO `schedule_job_log` VALUES (845, 1, 'testTask', 'renren', 0, NULL, 5, '2023-07-28 00:00:00');
INSERT INTO `schedule_job_log` VALUES (846, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 00:30:00');
INSERT INTO `schedule_job_log` VALUES (847, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 01:00:00');
INSERT INTO `schedule_job_log` VALUES (848, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 01:30:00');
INSERT INTO `schedule_job_log` VALUES (849, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 02:00:00');
INSERT INTO `schedule_job_log` VALUES (850, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 02:30:00');
INSERT INTO `schedule_job_log` VALUES (851, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 03:00:00');
INSERT INTO `schedule_job_log` VALUES (852, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 03:30:00');
INSERT INTO `schedule_job_log` VALUES (853, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 04:00:00');
INSERT INTO `schedule_job_log` VALUES (854, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 04:30:00');
INSERT INTO `schedule_job_log` VALUES (855, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 05:00:00');
INSERT INTO `schedule_job_log` VALUES (856, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 05:30:00');
INSERT INTO `schedule_job_log` VALUES (857, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 06:00:00');
INSERT INTO `schedule_job_log` VALUES (858, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 06:30:00');
INSERT INTO `schedule_job_log` VALUES (859, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 07:00:00');
INSERT INTO `schedule_job_log` VALUES (860, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 07:30:00');
INSERT INTO `schedule_job_log` VALUES (861, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 08:00:01');
INSERT INTO `schedule_job_log` VALUES (862, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 08:30:00');
INSERT INTO `schedule_job_log` VALUES (863, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 09:00:00');
INSERT INTO `schedule_job_log` VALUES (864, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 09:30:00');
INSERT INTO `schedule_job_log` VALUES (865, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 10:00:00');
INSERT INTO `schedule_job_log` VALUES (866, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 10:30:00');
INSERT INTO `schedule_job_log` VALUES (867, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 11:00:00');
INSERT INTO `schedule_job_log` VALUES (868, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 11:30:00');
INSERT INTO `schedule_job_log` VALUES (869, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 12:00:00');
INSERT INTO `schedule_job_log` VALUES (870, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 12:30:00');
INSERT INTO `schedule_job_log` VALUES (871, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 13:00:00');
INSERT INTO `schedule_job_log` VALUES (872, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 13:30:00');
INSERT INTO `schedule_job_log` VALUES (873, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 14:00:00');
INSERT INTO `schedule_job_log` VALUES (874, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 14:30:00');
INSERT INTO `schedule_job_log` VALUES (875, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 15:00:00');
INSERT INTO `schedule_job_log` VALUES (876, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 15:30:00');
INSERT INTO `schedule_job_log` VALUES (877, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 16:00:00');
INSERT INTO `schedule_job_log` VALUES (878, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 16:30:00');
INSERT INTO `schedule_job_log` VALUES (879, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 17:00:00');
INSERT INTO `schedule_job_log` VALUES (880, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 17:30:00');
INSERT INTO `schedule_job_log` VALUES (881, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 18:00:00');
INSERT INTO `schedule_job_log` VALUES (882, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 18:30:00');
INSERT INTO `schedule_job_log` VALUES (883, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 19:00:00');
INSERT INTO `schedule_job_log` VALUES (884, 1, 'testTask', 'renren', 0, NULL, 0, '2023-07-28 19:30:00');
INSERT INTO `schedule_job_log` VALUES (885, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 20:00:01');
INSERT INTO `schedule_job_log` VALUES (886, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-28 20:30:01');
INSERT INTO `schedule_job_log` VALUES (887, 1, 'testTask', 'renren', 0, NULL, 1, '2023-07-31 00:30:00');
INSERT INTO `schedule_job_log` VALUES (888, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 15:30:01');
INSERT INTO `schedule_job_log` VALUES (889, 1, 'testTask', 'renren', 0, NULL, 0, '2023-08-01 16:00:00');
INSERT INTO `schedule_job_log` VALUES (890, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 16:30:00');
INSERT INTO `schedule_job_log` VALUES (891, 1, 'testTask', 'renren', 0, NULL, 0, '2023-08-01 17:00:00');
INSERT INTO `schedule_job_log` VALUES (892, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 17:30:01');
INSERT INTO `schedule_job_log` VALUES (893, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 18:00:00');
INSERT INTO `schedule_job_log` VALUES (894, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 18:30:00');
INSERT INTO `schedule_job_log` VALUES (895, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 19:00:00');
INSERT INTO `schedule_job_log` VALUES (896, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 19:30:00');
INSERT INTO `schedule_job_log` VALUES (897, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 20:00:00');
INSERT INTO `schedule_job_log` VALUES (898, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 20:30:00');
INSERT INTO `schedule_job_log` VALUES (899, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 21:00:00');
INSERT INTO `schedule_job_log` VALUES (900, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 21:30:00');
INSERT INTO `schedule_job_log` VALUES (901, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 22:00:00');
INSERT INTO `schedule_job_log` VALUES (902, 1, 'testTask', 'renren', 0, NULL, 0, '2023-08-01 22:30:00');
INSERT INTO `schedule_job_log` VALUES (903, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 23:00:00');
INSERT INTO `schedule_job_log` VALUES (904, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-01 23:30:00');
INSERT INTO `schedule_job_log` VALUES (905, 1, 'testTask', 'renren', 0, NULL, 3, '2023-08-02 00:00:00');
INSERT INTO `schedule_job_log` VALUES (906, 1, 'testTask', 'renren', 0, NULL, 0, '2023-08-02 00:30:00');
INSERT INTO `schedule_job_log` VALUES (907, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-02 01:00:01');
INSERT INTO `schedule_job_log` VALUES (908, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-02 01:30:00');
INSERT INTO `schedule_job_log` VALUES (909, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-02 02:00:00');
INSERT INTO `schedule_job_log` VALUES (910, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-02 02:30:00');
INSERT INTO `schedule_job_log` VALUES (911, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-02 03:00:00');
INSERT INTO `schedule_job_log` VALUES (912, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-02 03:30:00');
INSERT INTO `schedule_job_log` VALUES (913, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-02 23:00:00');
INSERT INTO `schedule_job_log` VALUES (914, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-03 02:00:00');
INSERT INTO `schedule_job_log` VALUES (915, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-03 02:30:00');
INSERT INTO `schedule_job_log` VALUES (916, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-03 03:00:01');
INSERT INTO `schedule_job_log` VALUES (917, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-03 03:30:00');
INSERT INTO `schedule_job_log` VALUES (918, 1, 'testTask', 'renren', 0, NULL, 1, '2023-08-03 04:00:00');
INSERT INTO `schedule_job_log` VALUES (919, 1, 'testTask', 'renren', 0, NULL, 1, '2023-09-12 03:00:00');
INSERT INTO `schedule_job_log` VALUES (920, 1, 'testTask', 'renren', 0, NULL, 0, '2023-09-12 03:30:00');
INSERT INTO `schedule_job_log` VALUES (921, 1, 'testTask', 'renren', 0, NULL, 0, '2023-09-12 04:00:00');

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统验证码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------
INSERT INTO `sys_captcha` VALUES ('0428c59e-b8fc-4f7b-86df-8798331f2e8f', 'wn5w2', '2023-05-03 14:39:29');
INSERT INTO `sys_captcha` VALUES ('119587a7-d7c7-48a8-845c-55e6af95b0a3', 'd32n2', '2023-04-19 23:31:37');
INSERT INTO `sys_captcha` VALUES ('376dea4c-f983-4f99-80de-092ef2721dbe', '52fbn', '2023-05-03 14:33:48');
INSERT INTO `sys_captcha` VALUES ('38bb4aa3-84fd-4a95-82a8-92c9b2309ef7', 'bc7bc', '2023-07-26 12:47:38');
INSERT INTO `sys_captcha` VALUES ('54f21078-009f-44aa-8f44-ae043b7a10b7', 'ywdxg', '2023-05-18 20:34:21');
INSERT INTO `sys_captcha` VALUES ('5ca3a851-f5c7-4d17-8904-d5d292d6233a', '535my', '2023-04-19 23:31:45');
INSERT INTO `sys_captcha` VALUES ('616092a3-27df-4578-8d98-4e3daa4132ea', '8y2yc', '2023-05-07 15:45:16');
INSERT INTO `sys_captcha` VALUES ('61745264-4ada-479e-8029-f40554f62f59', 'x7cx7', '2023-02-10 19:29:20');
INSERT INTO `sys_captcha` VALUES ('7a1c57d2-1483-4bd1-8a52-015798f23f12', 'b2ew8', '2023-07-26 12:48:12');
INSERT INTO `sys_captcha` VALUES ('8fa2fc4f-2a64-4203-86ea-1d050fe8ba91', 'wweda', '2023-05-07 15:54:08');
INSERT INTO `sys_captcha` VALUES ('a56a28eb-9b4c-4954-811b-6c1f5b0d2879', 'y7bb6', '2023-05-07 16:02:29');
INSERT INTO `sys_captcha` VALUES ('aee2dd98-a1a7-41ca-8a7b-7c169949562e', 'fg3nw', '2023-05-07 16:01:06');
INSERT INTO `sys_captcha` VALUES ('ba978e11-70b8-4b86-86b1-52f3b2bbcd59', '8dy6y', '2023-05-07 16:00:53');
INSERT INTO `sys_captcha` VALUES ('c2ab4552-def3-4e91-8f82-949090259b81', '2x3n2', '2023-07-26 12:48:15');
INSERT INTO `sys_captcha` VALUES ('ce50e5d0-81ed-423d-8369-d2818745dbc6', 'bw5eb', '2023-04-20 13:39:15');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY', '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuBucketName\":\"ios-app\",\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"type\":1}', 0, '云存储配置信息');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":31,\"parentId\":0,\"name\":\"商品系统\",\"url\":\"\",\"perms\":\"\",\"type\":0,\"icon\":\"menu\",\"orderNum\":0,\"list\":[]}]', 130, '0:0:0:0:0:0:0:1', '2023-05-01 16:30:47');
INSERT INTO `sys_log` VALUES (2, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":32,\"parentId\":31,\"name\":\"分类维护\",\"url\":\"product/category\",\"perms\":\"\",\"type\":1,\"icon\":\"menu\",\"orderNum\":0,\"list\":[]}]', 71, '0:0:0:0:0:0:0:1', '2023-05-01 16:32:13');
INSERT INTO `sys_log` VALUES (3, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":33,\"parentId\":31,\"name\":\"品牌管理\",\"url\":\"product/brand\",\"perms\":\"\",\"type\":1,\"icon\":\"role\",\"orderNum\":0,\"list\":[]}]', 53, '0:0:0:0:0:0:0:1', '2023-05-08 02:08:15');
INSERT INTO `sys_log` VALUES (4, 'admin', '保存用户', 'io.renren.modules.sys.controller.SysUserController.save()', '[{\"userId\":2,\"username\":\"bingo\",\"password\":\"149964a6e87d246c2a6bb521150585eca09065c1ccb410747cb017ee95ca0cfd\",\"salt\":\"j5uJRKWYpcq61H4XfIWI\",\"email\":\"bbbb@163.com\",\"mobile\":\"12345678912\",\"status\":1,\"roleIdList\":[],\"createUserId\":1,\"createTime\":\"May 29, 2023 3:04:52 PM\"}]', 415, '0:0:0:0:0:0:0:1', '2023-05-29 15:04:53');
INSERT INTO `sys_log` VALUES (5, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":76,\"parentId\":41,\"name\":\"规格维护\",\"url\":\"/product-attrupdate\",\"perms\":\"\",\"type\":1,\"icon\":\"config\",\"orderNum\":4,\"list\":[]}]', 68, '0:0:0:0:0:0:0:1', '2023-05-30 02:26:21');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, NULL, 0, 'system', 0);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员列表', 'sys/user', NULL, 1, 'admin', 1);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'sys/role', NULL, 1, 'role', 2);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'sys/menu', NULL, 1, 'menu', 3);
INSERT INTO `sys_menu` VALUES (5, 1, 'SQL监控', 'http://localhost:8080/renren-fast/druid/sql.html', NULL, 1, 'sql', 4);
INSERT INTO `sys_menu` VALUES (6, 1, '定时任务', 'job/schedule', NULL, 1, 'job', 5);
INSERT INTO `sys_menu` VALUES (7, 6, '查看', NULL, 'sys:schedule:list,sys:schedule:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (8, 6, '新增', NULL, 'sys:schedule:save', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (9, 6, '修改', NULL, 'sys:schedule:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (10, 6, '删除', NULL, 'sys:schedule:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (11, 6, '暂停', NULL, 'sys:schedule:pause', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (12, 6, '恢复', NULL, 'sys:schedule:resume', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (13, 6, '立即执行', NULL, 'sys:schedule:run', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 6, '日志列表', NULL, 'sys:schedule:log', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (27, 1, '参数管理', 'sys/config', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'config', 6);
INSERT INTO `sys_menu` VALUES (29, 1, '系统日志', 'sys/log', 'sys:log:list', 1, 'log', 7);
INSERT INTO `sys_menu` VALUES (30, 1, '文件上传', 'oss/oss', 'sys:oss:all', 1, 'oss', 6);
INSERT INTO `sys_menu` VALUES (31, 0, '商品系统', '', '', 0, 'editor', 0);
INSERT INTO `sys_menu` VALUES (32, 31, '分类维护', 'product/category', '', 1, 'menu', 0);
INSERT INTO `sys_menu` VALUES (34, 31, '品牌管理', 'product/brand', '', 1, 'editor', 0);
INSERT INTO `sys_menu` VALUES (37, 31, '平台属性', '', '', 0, 'system', 0);
INSERT INTO `sys_menu` VALUES (38, 37, '属性分组', 'product/attrgroup', '', 1, 'tubiao', 0);
INSERT INTO `sys_menu` VALUES (39, 37, '规格参数', 'product/baseattr', '', 1, 'log', 0);
INSERT INTO `sys_menu` VALUES (40, 37, '销售属性', 'product/saleattr', '', 1, 'zonghe', 0);
INSERT INTO `sys_menu` VALUES (41, 31, '商品维护', 'product/spu', '', 0, 'zonghe', 0);
INSERT INTO `sys_menu` VALUES (42, 0, '优惠营销', '', '', 0, 'mudedi', 0);
INSERT INTO `sys_menu` VALUES (43, 0, '库存系统', '', '', 0, 'shouye', 0);
INSERT INTO `sys_menu` VALUES (44, 0, '订单系统', '', '', 0, 'config', 0);
INSERT INTO `sys_menu` VALUES (45, 0, '用户系统', '', '', 0, 'admin', 0);
INSERT INTO `sys_menu` VALUES (46, 0, '内容管理', '', '', 0, 'sousuo', 0);
INSERT INTO `sys_menu` VALUES (47, 42, '优惠券管理', 'coupon/coupon', '', 1, 'zhedie', 0);
INSERT INTO `sys_menu` VALUES (48, 42, '发放记录', 'coupon/history', '', 1, 'sql', 0);
INSERT INTO `sys_menu` VALUES (49, 42, '专题活动', 'coupon/subject', '', 1, 'tixing', 0);
INSERT INTO `sys_menu` VALUES (50, 42, '秒杀活动', 'coupon/seckill', '', 1, 'daohang', 0);
INSERT INTO `sys_menu` VALUES (51, 42, '积分维护', 'coupon/bounds', '', 1, 'geren', 0);
INSERT INTO `sys_menu` VALUES (52, 42, '满减折扣', 'coupon/full', '', 1, 'shoucang', 0);
INSERT INTO `sys_menu` VALUES (53, 43, '仓库维护', 'ware/wareinfo', '', 1, 'shouye', 0);
INSERT INTO `sys_menu` VALUES (54, 43, '库存工作单', 'ware/task', '', 1, 'log', 0);
INSERT INTO `sys_menu` VALUES (55, 43, '商品库存', 'ware/sku', '', 1, 'jiesuo', 0);
INSERT INTO `sys_menu` VALUES (56, 44, '订单查询', 'order/order', '', 1, 'zhedie', 0);
INSERT INTO `sys_menu` VALUES (57, 44, '退货单处理', 'order/return', '', 1, 'shanchu', 0);
INSERT INTO `sys_menu` VALUES (58, 44, '等级规则', 'order/settings', '', 1, 'system', 0);
INSERT INTO `sys_menu` VALUES (59, 44, '支付流水查询', 'order/payment', '', 1, 'job', 0);
INSERT INTO `sys_menu` VALUES (60, 44, '退款流水查询', 'order/refund', '', 1, 'mudedi', 0);
INSERT INTO `sys_menu` VALUES (61, 45, '会员列表', 'member/member', '', 1, 'geren', 0);
INSERT INTO `sys_menu` VALUES (62, 45, '会员等级', 'member/level', '', 1, 'tubiao', 0);
INSERT INTO `sys_menu` VALUES (63, 45, '积分变化', 'member/growth', '', 1, 'bianji', 0);
INSERT INTO `sys_menu` VALUES (64, 45, '统计信息', 'member/statistics', '', 1, 'sql', 0);
INSERT INTO `sys_menu` VALUES (65, 46, '首页推荐', 'content/index', '', 1, 'shouye', 0);
INSERT INTO `sys_menu` VALUES (66, 46, '分类热门', 'content/category', '', 1, 'zhedie', 0);
INSERT INTO `sys_menu` VALUES (67, 46, '评论管理', 'content/comments', '', 1, 'pinglun', 0);
INSERT INTO `sys_menu` VALUES (68, 41, 'spu管理', 'product/spu', '', 1, 'config', 0);
INSERT INTO `sys_menu` VALUES (69, 41, '发布商品', 'product/spuadd', '', 1, 'bianji', 0);
INSERT INTO `sys_menu` VALUES (70, 43, '采购单维护', '', '', 0, 'tubiao', 0);
INSERT INTO `sys_menu` VALUES (71, 70, '采购需求', 'ware/purchaseitem', '', 1, 'editor', 0);
INSERT INTO `sys_menu` VALUES (72, 70, '采购单', 'ware/purchase', '', 1, 'menu', 0);
INSERT INTO `sys_menu` VALUES (73, 41, '商品管理', 'product/manager', '', 1, 'zonghe', 0);
INSERT INTO `sys_menu` VALUES (74, 42, '会员价格', 'coupon/memberprice', '', 1, 'admin', 0);
INSERT INTO `sys_menu` VALUES (75, 42, '每日秒杀', 'coupon/seckillsession', '', 1, 'job', 0);
INSERT INTO `sys_menu` VALUES (77, NULL, '规格维护', 'product/attrupdate', NULL, 1, 'config', 4);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e', 'root@renren.io', '13612345678', 1, 1, '2016-11-11 11:11:11');
INSERT INTO `sys_user` VALUES (2, 'bingo', '149964a6e87d246c2a6bb521150585eca09065c1ccb410747cb017ee95ca0cfd', 'j5uJRKWYpcq61H4XfIWI', 'bbbb@163.com', '12345678912', 1, 1, '2023-05-29 15:04:53');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
INSERT INTO `sys_user_token` VALUES (1, 'c4ec0d35f326a3e232a8bd29732eea50', '2023-08-03 10:48:36', '2023-08-02 22:48:36');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'mark', '13612345678', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '2017-03-23 22:37:41');

SET FOREIGN_KEY_CHECKS = 1;
