CREATE TABLE `student` (
  `sid` int NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `faculty` VARCHAR(20) NOT NULL,
  `dormitory_name` VARCHAR(20) NOT NULL,
  `gender` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `dormitory` (
  `dormitory_name` VARCHAR(20) NOT NULL,
  `campus` VARCHAR(20) NOT NULL,
  `charge` DOUBLE NOT NULL,
  `phone` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`dormitory_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SELECT faculty FROM `student` s
WHERE s.dormitory_name IN (
  SELECT s1.dormitory_name FROM `dormitory` s1
  WHERE s1.name = '王小星');

UPDATE `dormitory` SET charge = 1200 WHERE dormitory_name = '陶园一舍';

SELECT dormitory_name FROM `student`
WHERE faculty = '软件学院' AND gender = '男';

SELECT dormitory_name FROM `student`
WHERE faculty = '软件学院' AND gender = '女';

UPDATE `student` SET dormitory_name = ''
WHERE faculty = '软件学院' AND gender = '男';