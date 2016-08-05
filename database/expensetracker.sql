-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 02, 2015 at 05:04 AM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `expensetracker`
--

-- --------------------------------------------------------

--
-- Table structure for table `expensedetails`
--

CREATE TABLE IF NOT EXISTS `expensedetails` (
  `Category` varchar(100) NOT NULL,
  `Amount` double NOT NULL,
  `Date` date NOT NULL,
  PRIMARY KEY (`Category`,`Date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `expensedetails`
--

INSERT INTO `expensedetails` (`Category`, `Amount`, `Date`) VALUES
('Dining', 198, '2015-11-28'),
('Dining', 610, '2015-11-29'),
('Dining', 20, '2015-11-30'),
('Dining', 200, '2015-12-01'),
('Entertainment', 0, '2015-11-28'),
('Entertainment', 500, '2015-11-29'),
('Entertainment', 70, '2015-11-30'),
('Entertainment', 100, '2015-12-01'),
('Gasoline', 0, '2015-11-28'),
('Gasoline', 333, '2015-11-29'),
('Gasoline', 35, '2015-11-30'),
('Gasoline', 900, '2015-12-01'),
('Grocery', 543, '2015-11-28'),
('Grocery', 800, '2015-11-29'),
('Grocery', 50, '2015-11-30'),
('Grocery', 300, '2015-12-01'),
('Miscellaneous', 0, '2015-11-28'),
('Miscellaneous', 34, '2015-11-29'),
('Miscellaneous', 120, '2015-11-30'),
('Miscellaneous', 1001, '2015-12-01');

-- --------------------------------------------------------

--
-- Table structure for table `limitdetails`
--

CREATE TABLE IF NOT EXISTS `limitdetails` (
  `startdate` date NOT NULL DEFAULT '0000-00-00',
  `enddate` date NOT NULL,
  `limit` int(11) NOT NULL,
  PRIMARY KEY (`startdate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `limitdetails`
--

INSERT INTO `limitdetails` (`startdate`, `enddate`, `limit`) VALUES
('2015-12-01', '2016-01-01', 6000);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
