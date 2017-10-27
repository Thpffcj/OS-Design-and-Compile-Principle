-- MySQL dump 10.13  Distrib 5.6.19, for osx10.7 (i386)
--
-- Host: 127.0.0.1    Database: healthclub_homework
-- ------------------------------------------------------
-- Server version	5.6.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-21 23:18:28
-- MySQL dump 10.13  Distrib 5.6.19, for osx10.7 (i386)
--
-- Host: 127.0.0.1    Database: platform_homework
-- ------------------------------------------------------
-- Server version	5.6.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `platform_article`
--

DROP TABLE IF EXISTS `platform_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `platform_article` (
  `article_id` varchar(45) NOT NULL,
  `writer_id` varchar(45) NOT NULL,
  `article_title` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_article`
--

LOCK TABLES `platform_article` WRITE;
/*!40000 ALTER TABLE `platform_article` DISABLE KEYS */;
INSERT INTO `platform_article` VALUES ('49c6bddccebcc25cf01d85291d733823','UzAZJvW1','css常用代码大全','代码大全的代码并不全','2015-09-23 22:38:01'),('818a13b79a2088e77d9b50f9d68fffe4','iHnbvVHS','软件工程师的青春','青春，是一段突如其来的成长','2015-09-29 23:42:48'),('e763d6a962ea4624e2e48a0d8b70a137','UzAZJvW1','数据库开发技术作业','作业的内容并不是很多，会做就好','2015-09-11 20:56:16'),('eef401802b96c800fa0cb7b6d0e775a5','UzAZJvW1','柠檬水的故事','夏天到了，程序员多喝柠檬水','2015-09-21 22:56:10'),('f5a4e21fbae1c7980595f2c6858e33d0','gbWTxmTM','软件工程：DBA是则样炼成的','时光可知，秋已凉','2015-09-23 20:44:58');
/*!40000 ALTER TABLE `platform_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_deal`
--

DROP TABLE IF EXISTS `platform_deal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `platform_deal` (
  `deal_id` varchar(45) NOT NULL,
  `article_id` varchar(45) NOT NULL,
  `reader_id` varchar(45) NOT NULL,
  `deal_payment` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`deal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_deal`
--

LOCK TABLES `platform_deal` WRITE;
/*!40000 ALTER TABLE `platform_deal` DISABLE KEYS */;
INSERT INTO `platform_deal` VALUES ('1ed0f1c66563bff397a5e55fb38b2189','818a13b79a2088e77d9b50f9d68fffe4','NuLi2j5Rucec4D6rx4GMnPNI',10,'2015-10-06 21:43:50'),('2fcab16db74efd8345c51529449f1171','49c6bddccebcc25cf01d85291d733823','NuLi2j5Rucec4D6rx4GMnPNI',12,'2015-10-07 11:29:37'),('60bf28056194e961bcd7940feb5c2ab3','818a13b79a2088e77d9b50f9d68fffe4','LZnFO2YtW0ROgmie9GR-AU',8,'2015-10-06 21:57:46'),('9c74670fb8426fa4acc94f5add38b791','f5a4e21fbae1c7980595f2c6858e33d0','NuNsBWEeKKuG1QFFIZK576e4',8,'2015-10-07 18:34:25');
/*!40000 ALTER TABLE `platform_deal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_reader`
--

DROP TABLE IF EXISTS `platform_reader`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `platform_reader` (
  `reader_id` varchar(45) NOT NULL,
  `reader_name` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`reader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_reader`
--

LOCK TABLES `platform_reader` WRITE;
/*!40000 ALTER TABLE `platform_reader` DISABLE KEYS */;
INSERT INTO `platform_reader` VALUES ('LZnFO2YtW0ROgmie9GR-AU','Harlan','2015-10-06 21:56:57'),('NuA88jBl-MssJOquIz-49YNU','Carl','2015-09-24 15:46:38'),('NuFdeo1FO1VBtuKCg7-tmgoc','Aaron','2015-09-24 15:44:55'),('NuLi2j5Rucec4D6rx4GMnPNI','Fabian','2015-09-25 10:08:58'),('NuN9Kg3gXFKe5ZBeUZS1yNjc','Darren','2015-09-24 15:50:42'),('NuNsBWEeKKuG1QFFIZK576e4','Emmanuel','2015-09-25 10:08:58');
/*!40000 ALTER TABLE `platform_reader` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_writer`
--

DROP TABLE IF EXISTS `platform_writer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `platform_writer` (
  `writer_id` varchar(20) NOT NULL,
  `writer_name` varchar(45) NOT NULL,
  `writer_email` varchar(45) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`writer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_writer`
--

LOCK TABLES `platform_writer` WRITE;
/*!40000 ALTER TABLE `platform_writer` DISABLE KEYS */;
INSERT INTO `platform_writer` VALUES ('76if1crJ','Milley','wym@platform.com','2015-09-04 20:10:22'),('gbWTxmTM','Larua','mrlx@platform.com','2015-09-23 20:37:47'),('iHnbvVHS','Kimi','wzl@platform.com','2015-09-29 23:39:53'),('M1XKYowT','Soul','gxe@platform.com','2015-09-11 21:17:20'),('UzAZJvW1','Karen','jyt@platform.com','2015-09-04 09:30:27');
/*!40000 ALTER TABLE `platform_writer` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-21 23:18:28
