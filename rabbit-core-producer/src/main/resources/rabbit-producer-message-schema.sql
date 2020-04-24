
-- 表 broker_message.broker_message 结构
CREATE TABLE IF NOT EXISTS `broker_message` (
  `message_id` VARCHAR(128) NOT NULL,
  `message` VARCHAR(4000),
  `try_count` INT(4) DEFAULT 0,
  `status` VARCHAR(10) DEFAULT '',
  `next_retry` TIMESTAMP NOT NULL DEFAULT '1970-1-1 08:00:01',
  `create_time` TIMESTAMP NOT NULL DEFAULT '1970-1-1 08:00:01',
  `update_time` TIMESTAMP NOT NULL DEFAULT '1970-1-1 08:00:01',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
