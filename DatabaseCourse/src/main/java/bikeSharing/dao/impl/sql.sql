CREATE TABLE `user` (
  `uid` int NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(50) NOT NULL,
  `address` DOUBLE DEFAULT NULL ,
  `charge` DOUBLE NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `bike` (
  `bid` int NOT NULL,
  `time` DOUBLE DEFAULT NULL,
  PRIMARY KEY (`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `maintain` (
  `bid` int NOT NULL,
  `end` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ride` (
  `uid` int NOT NULL,
  `bid` int NOT NULL,
  `begin` VARCHAR(50) NOT NULL,
  `begin_time` DATETIME NOT NULL,
  `end` VARCHAR(50) NOT NULL,
  `end_time` DATETIME NOT NULL,
  `money` DOUBLE DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# 查询出所有用户在晚上6点到12点之间，单车归还的最频繁的地点，作为家庭住址添加在用户表中
SELECT uid, end FROM `ride`
WHERE EXTRACT(HOUR FROM end_time) BETWEEN 18 AND 24
GROUP BY uid;

# 实现根据单车使用记录中的使用时间，自动补全费用字段，并在用户账户中，扣除相应的金额。半小时内1元，超过半小时不满一小时
# 的2元，超过一小时不满1.5小时的3元，超过1.5个小时的4元。金额不足为负时，则用户不能使用共享单车

# 每个月初，禁用上一个月内使用超200小时的单车，提醒工作人员维护（维护一个单车维修表，包括单车编号和单车最后使用地点）

