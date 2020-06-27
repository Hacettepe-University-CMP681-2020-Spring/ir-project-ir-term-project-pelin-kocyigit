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
-- Table structure for table `sentences`
--

DROP TABLE IF EXISTS `sentences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sentences` (
  `sentenceId` int(11) NOT NULL,
  `orjSentence` varchar(400) NOT NULL DEFAULT '0',
  `processedSentence` varchar(400) NOT NULL DEFAULT '0',
  `wordCount` int(11) DEFAULT '0',
  `punctuation` tinyint(1) DEFAULT '0',
  `linkingWord` tinyint(1) DEFAULT '0',
  `bonusWord` tinyint(1) DEFAULT '0',
  `capitalLetter` tinyint(1) DEFAULT '0',
  `novelWord` tinyint(1) DEFAULT '0',
  `paragraphStart` tinyint(1) DEFAULT '0',
  `numericValue` tinyint(1) DEFAULT '0',
  `sentenceWeight1` float DEFAULT '0',
  `sentenceWeight2` float DEFAULT '0',
  `totalWeight` float DEFAULT '0',
  PRIMARY KEY (`sentenceId`),
  UNIQUE KEY `sentenceId_UNIQUE` (`sentenceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sentences`
--

LOCK TABLES `sentences` WRITE;
/*!40000 ALTER TABLE `sentences` DISABLE KEYS */;
INSERT INTO `sentences` VALUES (0,'Quarterly profits at US media giant TimeWarner jumped 76% to $1.13bn (£600m) for the three months to December, from $639m year earlier.','  quart profi media giant timew jump bn m three month decem m year earli',22,1,0,1,1,0,1,1,0.931,0.014,0.013034),(1,'\n\nThe firm, which is now one of the biggest investors in Google, benefited from sales of high speed internet connections and higher advert sales.','  firm bigge inves googl benef sale high speed inter conne highe adver sale',24,0,0,1,1,0,1,0,0.602,0.009,0.005418),(2,' TimeWarner said fourth quarter sales rose 2% to $11.1bn from $10.9bn.','  timew said fourt quart sale rose bn bn',11,0,0,1,1,0,0,1,0.729,0.017,0.012393),(3,' Its profits were buoyed by one off gains which offset a profit dip at Warner Bros, and less users for AOL.','  profi buoi gain offse profi dip warne bro less user aol',21,0,0,1,1,0,0,0,0.528,0.013,0.006864),(4,'\n\nTime Warner said on Friday that it now owns 8% of search engine Google.','  time warne said frida own searc engin googl',14,0,0,0,1,0,1,1,0.639,0.009,0.005751),(5,' But its own internet business, AOL, had has mixed fortunes.','  but inter busi aol mix fortu',10,0,1,0,1,0,0,0,0.437,0.012,0.005244),(6,' It lost 464,000 subscribers in the fourth quarter profits were lower than in the preceding three quarters.','  lost subsc fourt quart profi lower prece three quart',17,0,0,1,1,0,0,1,0.729,0.013,0.009477),(7,' However, the company said AOL\'s underlying profit before exceptional items rose 8% on the back of stronger internet advertising revenues.','  howev compa said aol under profi excep item rose back stron inter adver reven',20,1,1,1,1,0,0,1,0.93,0.014,0.01302),(8,' It hopes to increase subscribers by offering the online service free to TimeWarner internet customers and will try to sign up AOL\'s existing customers for high speed broadband.','  hope incre subsc offer onlin servi free timew inter custo try sign aol exist custo high speed broad',28,1,0,0,1,0,0,0,0.492,0.009,0.004428),(9,' TimeWarner also has to restate 2000 and 2003 results following a probe by the US Securities Exchange Commission (SEC), which is close to concluding.','  timew also resta resul probe secur excha commi sec close concl',24,1,0,0,1,0,0,1,0.693,0.009,0.006237),(10,'\n\nTime Warner\'s fourth quarter profits were slightly better than analysts\' expectations.','  time warne fourt quart profi bette analy expec',11,1,0,1,1,0,1,0,0.73,0.013,0.00949),(11,' But its film division saw profits slump 27% to $284m, helped by box office flops Alexander and Catwoman, a sharp contrast to year earlier, when the third and final film in the Lord of the Rings trilogy boosted results.','  but film divis saw profi slump m help box offic flop alexa catwo sharp contr year earli third final film lord ring trilo boost resul',39,0,1,1,1,0,0,1,0.803,0.008,0.006424),(12,' For the full year, TimeWarner posted a profit of $3.36bn, up 27% from its 2003 performance, while revenues grew 6.4% to $42.09bn.','  full year timew post profi bn perfo while reven grew bn',22,0,0,0,1,0,0,1,0.565,0.015,0.008475),(13,' \"Our financial performance was strong, meeting or exceeding all of our full year objectives and greatly enhancing our flexibility,\" chairman and chief executive Richard Parsons said.','  finan perfo stron meet excee full year objec great enhan flexi chair chief execu richa parso said',26,1,0,0,1,0,0,0,0.492,0.007,0.003444),(14,' For 2005, TimeWarner is projecting operating earnings growth of around 5%, and also expects higher revenue and wider profit margins.','  timew proje oper earn growt aroun also expec highe reven wider profi margi',20,0,0,1,1,0,0,1,0.729,0.011,0.008019),(15,'\n\nTimeWarner is to restate its accounts as part of efforts to resolve an inquiry into AOL by US market regulators.','  timew resta accou part effor resol inqui aol marke regul',20,0,0,0,1,0,1,0,0.437,0.011,0.004807),(16,' It has already offered to pay $300m to settle charges, in a deal that is under review by the SEC.','  offer pai m settl charg deal under revie sec',20,0,1,0,1,0,0,1,0.639,0.008,0.005112),(17,' The company said it was unable to estimate the amount it needed to set aside for legal reserves, which it previously set at $500m.','  compa said unabl estim amoun need set asid legal reser set m',24,0,0,0,1,0,0,1,0.565,0.008,0.00452),(18,' It intends to adjust the way it accounts for a deal with German music publisher Bertelsmann\'s purchase of a stake in AOL Europe, which it had reported as advertising revenue.','  inten adjus wai accou deal germa music publi berte purch stake aol europ repor adver reven',30,1,0,0,1,0,0,0,0.492,0.009,0.004428),(19,' It will now book the sale of its stake in AOL Europe as a loss on the value of that stake.','  book sale stake aol europ loss valu stake',21,0,0,0,1,0,0,0,0.364,0.011,0.004004);
/*!40000 ALTER TABLE `sentences` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


