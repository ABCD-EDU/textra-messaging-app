-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Feb 17, 2021 at 08:24 AM
-- Server version: 5.7.31
-- PHP Version: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `messenger`
--

-- --------------------------------------------------------

--
-- Table structure for table `group_msg`
--

DROP TABLE IF EXISTS `group_msg`;
CREATE TABLE IF NOT EXISTS `group_msg` (
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `is_fav` tinyint(1) DEFAULT NULL,
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `group_repo`
--

DROP TABLE IF EXISTS `group_repo`;
CREATE TABLE IF NOT EXISTS `group_repo` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `is_admin` tinyint(1) DEFAULT NULL,
  `alias` varchar(30) DEFAULT NULL,
  `uid_admin` int(11) NOT NULL,
  PRIMARY KEY (`group_id`),
  KEY `uid_admin` (`uid_admin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE IF NOT EXISTS `message` (
  `from_user` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `message` text NOT NULL,
  `time_sent` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `from_user` (`from_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user_acc`
--

DROP TABLE IF EXISTS `user_acc`;
CREATE TABLE IF NOT EXISTS `user_acc` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `pwd_hash` varchar(100) NOT NULL,
  `salt` varchar(100) DEFAULT NULL,
  `user_fname` tinytext NOT NULL,
  `user_lname` tinytext NOT NULL,
  `verified` tinyint(1) NOT NULL,
  `is_admin` tinyint(1) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_acc`
--

INSERT INTO `user_acc` (`user_id`, `email`, `pwd_hash`, `salt`, `user_fname`, `user_lname`, `verified`, `is_admin`) VALUES
(3, 'johndoe@gmail.com', '2Cfw+dzScMy/5neZ+xUP2qC+vnPk0MTWkiqzhWau52ISLKfvjyM1YsJkVVqMhGrfK9gdlURbm+381WI/uRP8SQ==', '93J8LO5rmpShXDVHZxgFTTCerxTpmQqWrNHhJdM5laBtTyi1zcpIUMrn3mocf/pKIAaMLToNWCIgjxnBJB4Nfw==', 'john', 'doe', 1, 1),
(4, 'testemail@gmail.com', 'FIWBIcpL0vStQxgUlbltYPV6VEdsqP0GWTCydCcCDZcwbGnNSHNvEWZbwt83bHzvzzbZkFDUpc4tatlWpWh+DQ==', 'XYL4I5WYHbqu4iMd+aTXgjwI9xNEPmAqEfbGRPGr3V6NdQUeUkEb96u/Lzs9m++qJBODe6EomKvZmygmuKabSQ==', 'test', 'user', 0, 0);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `message_ibfk_1` FOREIGN KEY (`from_user`) REFERENCES `user_acc` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
