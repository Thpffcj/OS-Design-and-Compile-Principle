CREATE TABLE `student` (
  `sid` VARCHAR(30) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `faculty` VARCHAR(50) NOT NULL,
  `dormitory_name` VARCHAR(50) NOT NULL,
  `gender` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `dormitory` (
  `dormitory_name` VARCHAR(50) NOT NULL,
  `campus` VARCHAR(50) NOT NULL,
  `charge` DOUBLE NOT NULL,
  `phone` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`dormitory_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SELECT faculty FROM `student` s
WHERE s.dormitory_name IN (
  SELECT s1.dormitory_name FROM `student` s1
  WHERE s1.name = '王小星');

UPDATE `dormitory` SET charge = 1200 WHERE dormitory_name = '陶园一舍';

SELECT dormitory_name FROM `student`
WHERE faculty = '软件学院' AND gender = '男';

SELECT dormitory_name FROM `student`
WHERE faculty = '软件学院' AND gender = '女';

UPDATE `student` SET dormitory_name = ''
WHERE faculty = '软件学院' AND gender = '男';