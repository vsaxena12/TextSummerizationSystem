-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 19, 2015 at 01:53 PM
-- Server version: 5.5.8
-- PHP Version: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `textsummarization`
--

-- --------------------------------------------------------

--
-- Table structure for table `filedetail`
--

CREATE TABLE IF NOT EXISTS `filedetail` (
  `uid` int(10) DEFAULT NULL,
  `fileid` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(40) DEFAULT NULL,
  `filepath` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `filedetail`
--


-- --------------------------------------------------------

--
-- Table structure for table `keyword_data`
--

CREATE TABLE IF NOT EXISTS `keyword_data` (
  `fileid` int(10) DEFAULT NULL,
  `keywords` varchar(20) DEFAULT NULL,
  `occoutInSentence` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `keyword_data`
--


-- --------------------------------------------------------

--
-- Table structure for table `sentence_data`
--

CREATE TABLE IF NOT EXISTS `sentence_data` (
  `fileid` int(10) DEFAULT NULL,
  `sentenceid` int(20) DEFAULT NULL,
  `sentence` varchar(500) DEFAULT NULL,
  `length` int(10) DEFAULT NULL,
  `Titlekeyoccurence` int(11) DEFAULT NULL,
  `numeric_count` int(10) DEFAULT NULL,
  `propernoun` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sentence_data`
--


-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, 'admin', 'a');
