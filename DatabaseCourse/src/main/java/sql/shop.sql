# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.16)
# Database: sql
# Generation Time: 2017-10-19 05:22:45 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table clothes
# ------------------------------------------------------------

DROP TABLE IF EXISTS `clothes`;

CREATE TABLE `clothes` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `price` double(11,0) NOT NULL,
  `brand` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `launchYear` year(4) NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `clothes` WRITE;
/*!40000 ALTER TABLE `clothes` DISABLE KEYS */;

INSERT INTO `clothes` (`cid`, `name`, `price`, `brand`, `type`, `launchYear`)
VALUES
	(1,'秋季高端衬衫',229,'adidas','衬衫','2014'),
	(2,'夏季清凉短袖',139,'adidas','短袖','2015'),
	(3,'高端好衬衫',148,'nike','衬衫','2014'),
	(4,'绝世好衬衫',128,'converse','衬衫','2014'),
	(5,'nike新款短袖',158,'nike','短袖','2015'),
	(6,'cc新款蓝色短袖',138,'converse','短袖','2015'),
	(7,'cc新款红色短袖',148,'converse','短袖','2015'),
	(8,'cc新款白色短袖',148,'converse','短袖','2015'),
	(9,'冬季新品外套',468,'adidas','外套','2015'),
	(10,'男士9分运动裤',288,'nike','裤子','2015'),
	(11,'男士休闲外套',498,'nike','外套','2015'),
	(12,'男士7分运动裤',268,'nike','裤子','2015'),
	(13,'男士运动外套',468,'nike','外套','2015'),
	(14,'爆款棉外套',368,'nike','外套','2014'),
	(15,'爆款七分裤',168,'nike','裤子','2014'),
	(16,'运动短袖',108,'converse','短袖','2014'),
	(17,'运动九分裤',148,'converse','裤子','2014'),
	(18,'新品运动短袖',128,'adidas','短袖','2014'),
	(19,'新品冬季外套',369,'adidas','外套','2014'),
	(20,'cc新款黑色短袖',148,'converse','短袖','2015');

/*!40000 ALTER TABLE `clothes` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table customer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `cuid` int(11) NOT NULL,
  `cname` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`cuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;

INSERT INTO `customer` (`cuid`, `cname`, `phone`)
VALUES
	(1,'lucy','13888899999'),
	(2,'kelven','15588844444'),
	(3,'jacky','17788334444'),
	(4,'rino','13355566774'),
	(5,'fei','16644433333'),
	(6,'leo','13344466666');

/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `oid` int(11) NOT NULL AUTO_INCREMENT,
  `cuid` int(11) NOT NULL,
  `cid` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `totalprice` double NOT NULL,
  `ordertime` datetime NOT NULL,
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;

INSERT INTO `order` (`oid`, `cuid`, `cid`, `quantity`, `totalprice`, `ordertime`)
VALUES
	(1,1,6,4,552,'2015-09-21 20:33:12'),
	(2,1,7,1,148,'2015-09-08 19:29:32'),
	(3,2,8,1,148,'2015-09-17 19:29:57'),
	(4,3,7,1,148,'2015-09-14 13:30:15'),
	(5,3,1,1,229,'2015-08-12 16:33:34'),
	(6,4,9,1,468,'2015-08-19 17:34:30'),
	(7,3,2,1,139,'2015-08-11 15:36:03'),
	(8,1,10,1,288,'2015-08-13 19:37:59'),
	(9,1,11,1,498,'2015-09-16 09:38:29'),
	(10,2,10,1,288,'2015-09-03 19:39:03'),
	(11,2,13,1,468,'2014-10-07 09:41:50'),
	(12,1,14,3,1104,'2014-11-11 01:50:38'),
	(13,2,15,2,336,'2014-11-11 14:51:33'),
	(14,4,16,1,108,'2014-11-11 11:52:05'),
	(15,3,17,5,740,'2014-11-11 19:52:50'),
	(16,4,15,4,672,'2014-11-11 19:53:39'),
	(17,3,18,2,256,'2014-12-12 19:54:49'),
	(18,3,19,3,1107,'2014-12-12 19:55:25'),
	(19,2,14,3,1104,'2014-12-12 19:56:03'),
	(20,2,15,6,1008,'2014-12-12 19:56:56'),
	(21,4,19,3,1107,'2014-12-12 19:57:29'),
	(22,6,16,4,432,'2014-11-11 21:49:49'),
	(23,6,15,4,672,'2014-11-11 14:52:36'),
	(24,3,9,1,468,'2015-08-26 21:58:54'),
	(25,4,1,1,229,'2015-08-05 22:00:03');

/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
