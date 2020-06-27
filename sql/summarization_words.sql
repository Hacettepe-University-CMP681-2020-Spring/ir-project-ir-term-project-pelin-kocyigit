CREATE DATABASE  IF NOT EXISTS `summarization` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `summarization`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: summarization
-- ------------------------------------------------------
-- Server version	5.5.29

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
-- Table structure for table `words`
--

DROP TABLE IF EXISTS `words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `words` (
  `wordId` int(11) NOT NULL,
  `word` varchar(45) NOT NULL,
  `firstSentenceId` int(11) DEFAULT '-1',
  `totalCount` int(11) DEFAULT '0',
  `sentenceCount` int(11) DEFAULT '0',
  `weight` float DEFAULT '0',
  PRIMARY KEY (`wordId`),
  UNIQUE KEY `wordId_UNIQUE` (`wordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `words`
--

LOCK TABLES `words` WRITE;
/*!40000 ALTER TABLE `words` DISABLE KEYS */;
INSERT INTO `words` VALUES (0,'quart',0,5,4,0.017316),(1,'profi',0,9,8,0.034632),(2,'media',0,1,1,0.004329),(3,'giant',0,1,1,0.004329),(4,'timew',0,7,7,0.030303),(5,'jump',0,1,1,0.004329),(6,'bn',0,5,3,0.012987),(7,'m',0,5,4,0.017316),(8,'three',0,2,2,0.00865801),(9,'month',0,1,1,0.004329),(10,'decem',0,1,1,0.004329),(11,'year',0,4,4,0.017316),(12,'earli',0,2,2,0.00865801),(13,'firm',1,1,1,0.004329),(14,'bigge',1,1,1,0.004329),(15,'inves',1,1,1,0.004329),(16,'googl',1,2,2,0.00865801),(17,'benef',1,1,1,0.004329),(18,'sale',1,4,3,0.012987),(19,'high',1,2,2,0.00865801),(20,'speed',1,2,2,0.00865801),(21,'inter',1,4,4,0.017316),(22,'conne',1,1,1,0.004329),(23,'highe',1,2,2,0.00865801),(24,'adver',1,3,3,0.012987),(25,'said',2,5,5,0.021645),(26,'fourt',2,3,3,0.012987),(27,'rose',2,2,2,0.00865801),(28,'buoi',3,1,1,0.004329),(29,'gain',3,1,1,0.004329),(30,'offse',3,1,1,0.004329),(31,'dip',3,1,1,0.004329),(32,'warne',3,3,3,0.012987),(33,'bro',3,1,1,0.004329),(34,'less',3,1,1,0.004329),(35,'user',3,1,1,0.004329),(36,'aol',3,7,7,0.030303),(37,'time',0,2,2,0.00865801),(38,'frida',4,1,1,0.004329),(39,'own',4,1,1,0.004329),(40,'searc',4,1,1,0.004329),(41,'engin',4,1,1,0.004329),(42,'but',5,2,2,0.00865801),(43,'busi',5,1,1,0.004329),(44,'mix',5,1,1,0.004329),(45,'fortu',5,1,1,0.004329),(46,'lost',6,1,1,0.004329),(47,'subsc',6,2,2,0.00865801),(48,'lower',6,1,1,0.004329),(49,'prece',6,1,1,0.004329),(50,'howev',7,1,1,0.004329),(51,'compa',7,2,2,0.00865801),(52,'under',7,2,2,0.00865801),(53,'excep',7,1,1,0.004329),(54,'item',7,1,1,0.004329),(55,'back',7,1,1,0.004329),(56,'stron',7,2,2,0.00865801),(57,'reven',7,4,4,0.017316),(58,'hope',8,1,1,0.004329),(59,'incre',8,1,1,0.004329),(60,'offer',8,2,2,0.00865801),(61,'onlin',8,1,1,0.004329),(62,'servi',8,1,1,0.004329),(63,'free',8,1,1,0.004329),(64,'custo',8,2,1,0.004329),(65,'try',8,1,1,0.004329),(66,'sign',8,1,1,0.004329),(67,'exist',8,1,1,0.004329),(68,'broad',8,1,1,0.004329),(69,'also',9,2,2,0.00865801),(70,'resta',9,2,2,0.00865801),(71,'resul',9,2,2,0.00865801),(72,'probe',9,1,1,0.004329),(73,'secur',9,1,1,0.004329),(74,'excha',9,1,1,0.004329),(75,'commi',9,1,1,0.004329),(76,'sec',9,2,2,0.00865801),(77,'close',9,1,1,0.004329),(78,'concl',9,1,1,0.004329),(79,'bette',10,1,1,0.004329),(80,'analy',10,1,1,0.004329),(81,'expec',10,2,2,0.00865801),(82,'film',11,2,1,0.004329),(83,'divis',11,1,1,0.004329),(84,'saw',11,1,1,0.004329),(85,'slump',11,1,1,0.004329),(86,'help',11,1,1,0.004329),(87,'box',11,1,1,0.004329),(88,'offic',11,1,1,0.004329),(89,'flop',11,1,1,0.004329),(90,'alexa',11,1,1,0.004329),(91,'catwo',11,1,1,0.004329),(92,'sharp',11,1,1,0.004329),(93,'contr',11,1,1,0.004329),(94,'third',11,1,1,0.004329),(95,'final',11,1,1,0.004329),(96,'lord',11,1,1,0.004329),(97,'ring',11,1,1,0.004329),(98,'trilo',11,1,1,0.004329),(99,'boost',11,1,1,0.004329),(100,'full',12,2,2,0.00865801),(101,'post',12,1,1,0.004329),(102,'perfo',12,2,2,0.00865801),(103,'while',12,1,1,0.004329),(104,'grew',12,1,1,0.004329),(105,'finan',13,1,1,0.004329),(106,'meet',13,1,1,0.004329),(107,'excee',13,1,1,0.004329),(108,'objec',13,1,1,0.004329),(109,'great',13,1,1,0.004329),(110,'enhan',13,1,1,0.004329),(111,'flexi',13,1,1,0.004329),(112,'chair',13,1,1,0.004329),(113,'chief',13,1,1,0.004329),(114,'execu',13,1,1,0.004329),(115,'richa',13,1,1,0.004329),(116,'parso',13,1,1,0.004329),(117,'proje',14,1,1,0.004329),(118,'oper',14,1,1,0.004329),(119,'earn',14,1,1,0.004329),(120,'growt',14,1,1,0.004329),(121,'aroun',14,1,1,0.004329),(122,'wider',14,1,1,0.004329),(123,'margi',14,1,1,0.004329),(124,'accou',15,2,2,0.00865801),(125,'part',15,1,1,0.004329),(126,'effor',15,1,1,0.004329),(127,'resol',15,1,1,0.004329),(128,'inqui',15,1,1,0.004329),(129,'marke',15,1,1,0.004329),(130,'regul',15,1,1,0.004329),(131,'pai',16,1,1,0.004329),(132,'settl',16,1,1,0.004329),(133,'charg',16,1,1,0.004329),(134,'deal',16,2,2,0.00865801),(135,'revie',16,1,1,0.004329),(136,'unabl',17,1,1,0.004329),(137,'estim',17,1,1,0.004329),(138,'amoun',17,1,1,0.004329),(139,'need',17,1,1,0.004329),(140,'set',16,2,1,0.004329),(141,'asid',17,1,1,0.004329),(142,'legal',17,1,1,0.004329),(143,'reser',17,1,1,0.004329),(144,'inten',18,1,1,0.004329),(145,'adjus',18,1,1,0.004329),(146,'wai',18,1,1,0.004329),(147,'germa',18,1,1,0.004329),(148,'music',18,1,1,0.004329),(149,'publi',18,1,1,0.004329),(150,'berte',18,1,1,0.004329),(151,'purch',18,1,1,0.004329),(152,'stake',18,3,2,0.00865801),(153,'europ',18,2,2,0.00865801),(154,'repor',18,1,1,0.004329),(155,'book',19,1,1,0.004329),(156,'loss',19,1,1,0.004329),(157,'valu',19,1,1,0.004329);
/*!40000 ALTER TABLE `words` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-22  9:28:15
