
# Create Database
# ------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `foodie-shop-dev` DEFAULT CHARACTER SET = utf8mb4;

Use `foodie-shop-dev`;

# Dump of table broker_message
# ------------------------------------------------------------

DROP TABLE IF EXISTS `broker_message`;

CREATE TABLE `broker_message` (
  `message_id` varchar(128) NOT NULL COMMENT '消息主键',
  `message` varchar(4000) COMMENT '序列化后的消息',
  `try_count` int(4) DEFAULT 0 COMMENT '最大努力尝试次数',
  `status` varchar(10) DEFAULT '' COMMENT '消息状态',
  `next_retry` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '下次重试时间',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='broker message';