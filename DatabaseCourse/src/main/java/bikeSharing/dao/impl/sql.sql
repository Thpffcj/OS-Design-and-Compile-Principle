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
SELECT uid, bid, begin_time, EXTRACT(HOUR_MINUTE FROM end_time) - EXTRACT(HOUR_MINUTE FROM begin_time) FROM `ride`;

UPDATE `user` SET charge = charge - ? WHERE uid = ?;
UPDATE `bike` SET time = time + ? WHERE bid = ?;
REPLACE INTO `bike`(bid, time) VALUES (?, ?);
REPLACE INTO `ride`(uid, bid, begin, begin_time, end, end_time, money) VALUES (?, ?, ?, ?, ?, ?, ?);
UPDATE `ride` SET money = ? WHERE uid = ? AND bid = ? AND begin_time = ?;

# 每个月初，禁用上一个月内使用超200小时的单车，提醒工作人员维护（维护一个单车维修表，包括单车编号和单车最后使用地点）
SELECT bid FROM bike WHERE time > 12000;
SELECT bid, end FROM ride WHERE bid = ? ORDER BY end_time Desc LIMIT 0,1;
UPDATE `maintain` SET end = ? WHERE bid = ?;

SELECT A.uid, A.end, Max(A.time), name, phone, charge FROM
  (SELECT r.uid, r.end, COUNT(r.end) AS time FROM ride r
  WHERE DATE_FORMAT(r.end_time , "%H") BETWEEN '18' AND '24'
  GROUP BY r.uid,r.end
   ORDER BY r.uid, time DESC) AS A, `user` WHERE A.uid = user.uid
GROUP BY A.uid;