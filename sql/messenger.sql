-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 03, 2021 at 08:53 AM
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

--
-- Dumping data for table `group_msg`
--

INSERT INTO `group_msg` (`group_id`, `user_id`, `is_fav`) VALUES
(1, 506, 0),
(1, 507, 1),
(2, 508, 0),
(2, 506, 0),
(2, 507, 1),
(3, 507, 1),
(3, 506, 0),
(3, 507, 1),
(4, 507, 1),
(4, 508, 0),
(4, 509, 0);

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `group_repo`
--

INSERT INTO `group_repo` (`group_id`, `is_admin`, `alias`, `uid_admin`) VALUES
(1, 0, 'test', 506),
(2, 0, 'hello', 508),
(3, 0, 'tester', 507),
(4, 0, 'test12', 507);

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE IF NOT EXISTS `message` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `message` text NOT NULL,
  `time_sent` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`),
  KEY `from_user` (`from_user`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`message_id`, `from_user`, `group_id`, `message`, `time_sent`) VALUES
(1, 506, 1, 'hello', '2021-02-20 05:21:04'),
(2, 507, 1, 'test123', '2021-02-20 05:22:31'),
(3, 506, 1, 'hello there!', '2021-03-03 04:03:28'),
(4, 506, 1, 'lmaoooo', '2021-03-03 04:03:31'),
(5, 506, 1, 'nice', '2021-03-03 04:03:33'),
(6, 506, 1, 'wek wok', '2021-03-03 04:03:36'),
(7, 506, 1, 'afdsfads', '2021-03-03 04:03:41'),
(8, 506, 1, 'fdsafsda', '2021-03-03 04:03:46'),
(9, 508, 2, 'thello', '2021-03-03 04:05:06'),
(10, 508, 2, 'fdsafasdfad', '2021-03-03 04:06:23'),
(11, 507, 1, 'fdsafasd', '2021-03-03 05:30:18'),
(12, 507, 2, 'fdsafdsa', '2021-03-03 05:30:20'),
(13, 507, 4, 'fdsafdsa', '2021-03-03 05:30:23'),
(14, 507, 1, 'hello there', '2021-03-03 07:39:08'),
(15, 507, 1, 'dsfdasfadsfa!', '2021-03-03 07:42:42'),
(16, 507, 1, 'hello woworewo', '2021-03-03 07:43:40'),
(17, 507, 2, 'hello', '2021-03-03 07:44:55'),
(18, 507, 4, 'helo', '2021-03-03 07:45:20'),
(19, 507, 1, 'lmao', '2021-03-03 07:46:43'),
(20, 507, 1, 'lmao', '2021-03-03 07:48:55'),
(21, 507, 1, 'afsdafdsafs', '2021-03-03 07:49:32'),
(22, 507, 1, 'hello there', '2021-03-03 07:49:35'),
(23, 507, 1, 'fdsafsadfasdfads', '2021-03-03 07:55:37'),
(24, 507, 4, 'gaddsadas tel', '2021-03-03 08:04:22'),
(25, 507, 1, 'fdsafdsfsda', '2021-03-03 08:06:29'),
(26, 507, 1, 'fdsafdsfopawjfpowekfpokweapo', '2021-03-03 08:22:29'),
(27, 507, 2, 'halkmfsakdmfoaiwefiowea', '2021-03-03 08:23:17'),
(28, 507, 1, 'HELLO THERE!!', '2021-03-03 08:33:34'),
(29, 507, 1, 'hello', '2021-03-03 08:34:19'),
(30, 507, 1, 'lmfao', '2021-03-03 08:42:03'),
(31, 507, 4, 'lmao', '2021-03-03 08:42:29'),
(32, 507, 2, 'fds', '2021-03-03 08:42:40'),
(33, 507, 3, 'fdsafwef	2ek3', '2021-03-03 08:42:44'),
(34, 507, 1, 'latest', '2021-03-03 08:46:01'),
(35, 507, 1, 'hey', '2021-03-03 08:47:49'),
(36, 507, 1, 'helo', '2021-03-03 08:49:17'),
(37, 507, 4, 'heloo', '2021-03-03 08:49:30'),
(38, 507, 4, 'test', '2021-03-03 08:50:10'),
(39, 507, 1, 'test1', '2021-03-03 08:50:15'),
(40, 507, 3, 'tester', '2021-03-03 08:50:26');

-- --------------------------------------------------------

--
-- Table structure for table `unread_msg`
--

DROP TABLE IF EXISTS `unread_msg`;
CREATE TABLE IF NOT EXISTS `unread_msg` (
  `unread_id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`unread_id`),
  KEY `message_id` (`message_id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=416 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `unread_msg`
--

INSERT INTO `unread_msg` (`unread_id`, `message_id`, `user_id`, `group_id`) VALUES
(412, 508, 38, 4),
(413, 509, 38, 4),
(414, 506, 39, 1),
(415, 506, 40, 3);

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
  `user_color` varchar(7) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=605 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_acc`
--

INSERT INTO `user_acc` (`user_id`, `email`, `pwd_hash`, `salt`, `user_fname`, `user_lname`, `verified`, `is_admin`, `user_color`) VALUES
(505, 'test0@gmail.com', 'OngbpdbpBhm6nyICW4UYWfDVZGtwhUGkB0Lr9yxc3nohLdDqZTcp6wzNETHdJG9CffAdAUTU5dCZ4xoXOnqsBw==', 'i+J3dcx9BF/oS3tv5Hh3wHGCn/13m58/rCbA9c2YmGWF4bjlHhSYWCN9GJNQ9+bjcD8C93ADYm/2bfKX1uWsag==', 'test', '0', 1, 1, ''),
(506, 'test1@gmail.com', '72sL+fdjrJz8arhtTEOBsV9ZlHCoBtozhw3W7Qi5L5bhXipC82P3NwrbeW4pO3+kZ9pJeLC2qoEJ1zZTSkX2IA==', 'qGNHLkbjSP7CCcK92GvreTIHdrcUiIDhladwmyVmXD5JHrQAUUz+64QQTBiWA2+RoqXM1rLWhxKXMK9ckAy9MQ==', 'test', '1', 1, 0, ''),
(507, 'test2@gmail.com', '1fFmMHWnl27csfLtlf2Vduar+k2swVTM8osMlD14QcKGxcg8wwR6VOqVVM2BGGLeKypaKODrKvurLJAaDMevkg==', '4AtMUzvSsg7z5NijigpN37mVPw9/fPFXAKnOrBGDuyTJjyqKSh87KHquTZCTusV95LSrwMtSQtvabOd4l0LIug==', 'test', '2', 1, 0, ''),
(508, 'test3@gmail.com', 'sBRSD1Byx8pCP0miTp2c/l6sGgWYjZsqsOvpplHCB8+rWVnHz2ynC0MNVSN4bchUFWqOHnf0xPdk/ZcqrIPhHA==', 'E8N34NJ+yBHLwcqX9VNMqDEyabQxW7S9EKEWxC0g8T/+AbnpucOFfkF4quT5tIkvGaFvdKygqbavdyuujpVZYA==', 'test', '3', 1, 0, ''),
(509, 'test4@gmail.com', 'OMQ7ptbZAVXoLTl5+BOfHHBDc1gseN69TMzkq5bSk46FPE1YlrSiORih0kZ127LMqLeZw7mldp8vI32jVB/XNw==', 'KAVpN7gM4b7xp0sDKHhlSol/DvI4mK1JwRkMtAE6Ialtnc5hb/2OBhQGk+kAwgsdJTWjL4RiYWVfdy/svdlcvQ==', 'test', '4', 1, 0, ''),
(510, 'test5@gmail.com', 'VC/29Tm7Q4y2mF2vxgJOZ+FFD/zJgJVGMTXZm/DQ2xCXPA8ciTVOqlt/i3kRwrTESJgKVnzvQGWl/cEbbl3mNA==', '8OnWO9kcCj+u+iVHFw1sTq9kZx/Cloz79eaGIP2gXy09OK01cb/PEw7HFSM0NVpzEZfB1pkZucBV3UJF/gtKlA==', 'test', '5', 1, 0, ''),
(511, 'test6@gmail.com', 'pJdqURhIbd5vWbp1yQXgsWxbHDTY1z6ZCC/5YBwFxazXsIYgk+CABWIXTpQNy2zPGN5IdjNG+KAAoOPzXPfDRA==', '+ocN7szV0A7kGC8j3zyizcyiWTeqlGkRUxkYiLN9hoDobwZ08GYKuxj94B7NS0oHIyaFSGLc+iu3cmxuGJT+vA==', 'test', '6', 1, 0, ''),
(512, 'test7@gmail.com', 'nZSB7XccRqZ7xGqap11v+j6rvLZ14vwQrSbAE9kNziTRRwGmQ8hRho3bpAV50oglJYGzxo8Gpjj/kuo+g6FlSA==', 'octP+5UmhwW+deibaBU/OYBbwJ9BVp87wQMe/zlV3Ezvm9pspgxL3WO/pdhqXPi6nQ8NW7eMrsMlxM12GnHOhA==', 'test', '7', 1, 0, ''),
(513, 'test8@gmail.com', '77ulUKJKDhoJ4xL7PUhFePAM/CrtC3/7/ittXDEe3VxQ04DzwNPFR+7CUjTHRvwECMfUhrI/YkahwTjAyxMJwQ==', 'FAlWmW138Pk8dowEbz7v80/2VbT5UxDx5l0fS9SlbCERyxjGKA3BgMVeI4HVZ+3X3PTy7Iem4duxrZlXW3QhpA==', 'test', '8', 1, 0, ''),
(514, 'test9@gmail.com', 'b6RNCrs/dP+DlLpGp/k8DfT+1m3VTIkY7pJfbkJFJTTQ1U91Q/Omb8b87BQLxlpOBgTTm86xEkfunbw6uyT3sQ==', 'NAQGXRVb5R3DoPPV0yjB04M4kRU2q0ARp7zIqtv8J07hmYH8FiYyjNuEukXEBYV8/OS9eStI759rnH3A3HE3hA==', 'test', '9', 1, 0, ''),
(515, 'test10@gmail.com', 'UhYizjCuDs9UUPGWTsGO8lONHFAGCox4Y4NqV3d+leo4M1YoLHQELEcZsQWDnPy59oPC+HyY0NxanR/wEg/ByA==', 'nUQR8NeqFx7/eW+FbFot6M7PeY6JGQjOwzlvjSqc7tT5C1qOVqs6MgGhLeF+bks2G7VpM1ESG7m75D0jMsoU5w==', 'test', '10', 1, 0, ''),
(516, 'test11@gmail.com', 'hi/yDqcgqo7z6lOWCHAIFtAsfdBeJmNW4ZoJfEFPsi8FiEIoum4SlIgwS6y+grrp9P/YQLUbqYce+Nn99TRB7A==', '45tADEuc33Em2xt7YWYEpqv5J732mk0hbLGmcbgXuDwFrXF9vnX8sgMNf0N82aq5BdFNB8cZEvUbB3Ma3teTJA==', 'test', '11', 1, 0, ''),
(517, 'test12@gmail.com', '6axX1KuH/NI9/piC1051broIQH6dZAWwL91Zf6Jk8qifxGgpBE75VnNTabPUBw4zKU4t7tvSDY1rF6KlHsVONw==', 'rNzTmrBU/kNdhEzHACBGGWlzJGbmqOVopLKsXvO/YxmJ0DuyJ9TyrYAheaaVbdMA1CVB6btpftaK55cUGwX9qw==', 'test', '12', 0, 0, ''),
(518, 'test13@gmail.com', 'jGY16SKOqT8zIwJ8DHfBGCJLnzOF1f6Q0WoePC2kI/QYHaDaOKQWFqZZp59shO660dz/eLsCnUrrHPq4WJMJ7g==', 'Y06YZ9vUuOdZdd/dmdlnoWEtvPVYHRPj27PDWL9Q3MprwNFiXcd2EIBbgM40wuKiIUfuPmO+eB7Q2LWTvOHimg==', 'test', '13', 0, 0, ''),
(519, 'test14@gmail.com', 'Ybk++ylXH1HeL3Jf3TaSgNA/4VrnojGtGmFiXAY09EtQ0vuFtdUgnncQWs5qH43uvafA9EU1MUE14rDp9p3Srw==', '/0yz3oXrKSEEG/TZZSQN12QaKNKc84ZxJFnSR3Rw+MmKLkxlbaSQ9tHNqftC5yWCq6kIJW/fm5FbKCYESxYMAQ==', 'test', '14', 0, 0, ''),
(520, 'test15@gmail.com', 'li1VIjOZtonUNBUTWJrb5dQbs9NZg5dCfXJU1PQoSU+OsW2uq0ulffsyFvIv72dgZyMseJSeFpLmsmPhCJnI/g==', 'V3/EqAA1ALM/aDP0vZ3y6dkHjyrJjsC4HLeHmzne277IDCJOZ8nKxzKPrH+YUG+v7lvGqA7DLDkWjc7q4kf72A==', 'test', '15', 0, 0, ''),
(521, 'test16@gmail.com', 'krehQu2VoCyRC8KyFLvKocwQYZdUqWYXVG/5eXPTf7o7vYUWcu8QTNGh6OJiKGCySjdb7IE5WBOZc91qHExJmQ==', 'rvsLrsaF182SiXTc/H0J8AdE2IUHFDz+cZUTUDrnc4OMo3z4KCoGrtuSAvY68HNQzwRc4CI2oxZpgo/WswuknQ==', 'test', '16', 0, 0, ''),
(522, 'test17@gmail.com', 'KDcrD4gYjkJ73UnFrsemDvkrsnYeMq8NpYPaMfokVB3Nu8ZltzXwljiv4FVsDGg6GRpxWr6hYimMqrTRwTpsKA==', '2ds+/s2l+KMp7U54anXSYXRmEPsB/whDTB6C2F4kmdNX0We3luI81JBkUw9JisNBfyzEeDDOeB4v3btFzNMfUQ==', 'test', '17', 0, 0, ''),
(523, 'test18@gmail.com', 'YHl5gm14TY5ezOy6F3LXqYg5H5/2cwl3HUemmCUutrSNGbQsFGUeJlhJLa8PCgOYcPN92tWqjx8C9d82IwWuLQ==', '7tHNOmuBEeLqhEcDATvDlw/q4jGJB1qhEpYSiL9Ghr68UKd2RAy04yH96p314waOSCfTkqQmGpff/u29JCBnmg==', 'test', '18', 0, 0, ''),
(524, 'test19@gmail.com', 'M08T8iX66OjjE0Fpj6lIYrwz5Hyt1XRxHCh0+mG8bpOvh0nqrKMPZt3NpwT19b3y0DsIgcA2EekYFP7/mQkrpQ==', 'GFPqma5eQo3JZ9VntDoD/UgaEYcN6z3lUQ0Wv2evbonAfcn1j0Q/1J+cmryrJNWeVyzzPs5BPQtU+xmwLb8XSg==', 'test', '19', 0, 0, ''),
(525, 'test20@gmail.com', '9w001DAxjDfWib0iP0fZd3OCKZS8dRYoh52pPg0JdYPRi1bC1vcluq8jCMYVC+C3Bkbx5YrhiconEIxhYzpviw==', 'gcJQOyYJAdK4wS+zHDEA8DndYQAsDxfUylIOh6dg2jlfDBJye8a9lwb3YK19OFaEO97fcKFLbAOmENrnw+Ek1g==', 'test', '20', 0, 0, ''),
(526, 'test21@gmail.com', 'SOlFcT2QUFtH/zklxi8d0Dy+BoIl0Cy5y2mLAOgmoJPO6OYIe8p68WqxrlYMzAar8a/XrDOXmXCrLqICaItzwA==', 'mNg9IXg2fbsWR3ptfzeNzcHE2GRWoZTE+wddi0r7Q8HDFPc1fDlH1BcB4ek64fUGX8qwj70a2Ki6zlMTH9uBfA==', 'test', '21', 0, 0, ''),
(527, 'test22@gmail.com', 'bPR7/hiJhBoRdcQW0jyfYlPovZQOcSf/EJH45B6W0w+uPKsm3RM6SKClO0yEreeuL4ISpTGFPEMJ8RjLkmBIhg==', 'ZYn4wcVftXu805U5TQQKblNkePejeT2NShSi6Ly6C9djqOVGBmzsO9kA+iwDTfGdX9I5pyFqYjC9pHw4UMMdtA==', 'test', '22', 0, 0, ''),
(528, 'test23@gmail.com', 'RubLOwOc/RRWfzW6gQq0IszgvC6A0ml+JE+IiCblWGZAs105W4vzZzIsibvzAWm1aJY+eOyUGUvmO70uzpBgVA==', 'QFZtR/dFGgzbAzmqZ5YMHSTKlyvR5Akc3ZP/U8mltMFkOHnMl6jtpgo+FcdGBAKfANIIUXVyCuD53WTKT3R7zQ==', 'test', '23', 0, 0, ''),
(529, 'test24@gmail.com', 'cf2LxS12K8iQwghxNOTQtp3qnqYK2f0puxJ2eXOmxC9N5EuNcmuuv0L//uUVqO4oOwILWhVp3uCr4Q6uU8GA8w==', 'eJqyXbedH9GcklDEwhXCESP099CyKR4mH4Ojzzt9cj5Hm/APLpe96D/vfaE6WHRH1AjtThFSk+f7LLdFqeeKWw==', 'test', '24', 0, 0, ''),
(530, 'test25@gmail.com', 'BuGsVe8oSW9e3fp1Lit2TKKIEIj1qRfa1P4REs8Kckw9jyXtveq85HnUW1uu6HJhj4gzuYotI3OjhS9T+pz2Rw==', '6xNLHjZSDfsPbpOyoa3HqYAnPy0cn7xXz4qOvCkdCJGlRTsVbE388jTq2Z5AzuoDA9wjjm2+MHqSeNTFM/5lUw==', 'test', '25', 0, 0, ''),
(531, 'test26@gmail.com', 'wCRB6qv+duQIMTTshl+TbMSK3qxzwj//qXb5PtoThKhPh9lcXVaZo6QLxbV8g8etLhfvBakoE1Sn9gh1AAkwAQ==', 'sG42aWcYXRFTDTpRVnAXCAc5ofp3x1Qm6lxr/j4yvw9sLmt0Luz5Dw8FjzmZnOGrOYycWRuQnJ44crGfpxJ5IQ==', 'test', '26', 0, 0, ''),
(532, 'test27@gmail.com', 'jGtaLnoXyOHe+8wdb9RUYL992W97sbimdF+WStRFWfIt/53gTNATqiPSiVXIsSyMFkKtRcNN4n2BI7c3W28gaw==', 'G6ahFzhw9DRJc7VMHQmawj9XyZH5lFAS9WZzBwlkc6XKOQh5Yc+rgnmnMmHrQA2mUuX5RUPQQS5eVR+lZiTFYQ==', 'test', '27', 0, 0, ''),
(533, 'test28@gmail.com', 'HVs4lT9XG6CUi6yoHrtB8K3DEzBIg5AaNI8wElyYjsFu8viZj5vb/wQ95rsCk2hHS1aOhsZyiVVZJZUvd6KcYA==', '0+CoprB86dBmEmvErII+2e9WjE+o7lOYs+1Z9ltK8uNaTw32H05RjYP8+ZZRZjCaqFeQAzy1awgBqhV7swkQqQ==', 'test', '28', 0, 0, ''),
(534, 'test29@gmail.com', 'WZv7kZ0NtuXDv1FLXXH+4WVbH2a5J2tY3OKh3mueKGRf03VuMNXfU+1PHG+VPUJJIzyjXjcsvq9mPg9iLkNCFA==', 'k4igFPXs04xSiZmyyr6ccTdptb7b8B1xAkuaRX37IIVT/uv1tke71GSkhPBs/KlndpicUp21QnJUGsr8Mad9kw==', 'test', '29', 0, 0, ''),
(535, 'test30@gmail.com', 'fYVgPb07UHIP/rOmHnlgjo5BIHD1aeuNB5Pb8KjKjtQpp2Ni7aZ4Zm8PqpK59COB5yiNOVIgF7pMN+Oa+TKW/Q==', '/Zl5KAIa28OE16vtJ1avlLw1eR6mylul55V+y1uj3/l2h6RmgE2tYZCBQLsJi2GjTQlpJ17Xfl1pYTopf3UWrw==', 'test', '30', 0, 0, ''),
(536, 'test31@gmail.com', 'v5lm+8S+bUlIKIUq+0332jJNmTgdy+T6QEyGJ9tMeaGeMuwP8FbG7heN1J686mu/NFvjIWvwWx8ULOmdJHdfwQ==', 'HtW6ZVMKVaDGJky4/7HWWzmvO03tQUausZGodzLeJ9rCsjDye4lB6NUlLB1R4qrJZd7Sha9rdGtmQRqlPldl9g==', 'test', '31', 0, 0, ''),
(537, 'test32@gmail.com', '1Rlv8uWHPzCIM0H6kywoSDsp17pl3iIL2yIyXDMjWvY6wM1+i5ouJ1/RmzeCfj4+cw7AqygHHozJdohaQAVYiQ==', 'JzVKbVU98/mUBl3aIYp+V8CIeARSRzVUo5QcYkD2je4+J6UlBjeQs4pRpUh2+PVEIBxZW8ZDK4uXANTZ+kIFkQ==', 'test', '32', 0, 0, ''),
(538, 'test33@gmail.com', '+dclmLU2f1GD1L1HTvsPeNkyTkYIraHnlVNGo3q0hrDDwj7zuDDzSr5fXNs5eecuu6ptaiEW5A3WVN6q/zvq+w==', 'PofLukub1f4YZ21IArrheJMF+jzJmVsB28qDTrMnt7zSomaGzdgvTLICReNMOTwo4fKYlHq3nh1M2hDu8lZ7wA==', 'test', '33', 0, 0, ''),
(539, 'test34@gmail.com', 'E3I2+w1GhsdkYUNzpt0PWKAtxHj3a1WoLFnoGUSTE2iinUmdD6k263O+ngamMQEufyHlrs6WK6jRvqyGmuV5Eg==', 'qQNgN7jr4oRAv4LDYQApOuGoeVtMlNBTbH09fLjSeEItGaRDh5Z4s25OpP8guPO1neBaFhxd88QcgSAUUI3VAw==', 'test', '34', 0, 0, ''),
(540, 'test35@gmail.com', 'oLxHzwq88O72pckp8jPPzsTN1rxaPojNexDhEb3JDNcah2Yw+Se/WKexIaYVLHsWhjm37mRU+dp6ZUyGNerZbg==', 'S61D9K1HkM7224AXh0uRpEWI8lUpgDPHJf8RiL8YeXg9iYRfmhd5Eh0zRVJ/+AbV0Rii/797An2zSVP7P0lxyQ==', 'test', '35', 0, 0, ''),
(541, 'test36@gmail.com', '8IyDYMZ1xMJo+dD7c/Tl2fHQPgt0OXreQrpoZWZd6HNX03oMlVvjdn3GR1eAC25vYz42Oh2P//0lecwXI1nW5g==', 'a4N/uJVUSzV18aDjeJM7LOCrHmTyr+exgnfifZRicgNmU/eaoyD8LGmEKaFvlM3RRQ6P1//3QYBUEGuUurZxQg==', 'test', '36', 0, 0, ''),
(542, 'test37@gmail.com', 'AXaj0UX9EGThv7ilrcndi+EyHy1BNcMm/GY2qaDgMq0PTK5Z+fhtFA+9NmQLXfB7aOz/lgEhkaLqrqV9k8dzLg==', 'otriryXxAtHcGTEqQnWAGPPrOyDaQvUlWrR5/gSvaj+1o4p3dQaZSEQ1AIwK4TqxI4iedv3FHdD+7nJ1AnL4ZA==', 'test', '37', 0, 0, ''),
(543, 'test38@gmail.com', 'xq/1WcM9OO4Od/vc1lXtKQ4tjAM4gwirpRVyaGCaooQpSJkyw5brq2cWLCrZP/6TGscP473Sgfyn7dB2ABXdbw==', '5H6g9bRffvd9NUiLGrK5C8IfTdqqkLFnB4UEkkF9J4HHlPNX0o2BIq0Bq5bLY8PrNhRtn2OaSlpqLbPPy7dyfg==', 'test', '38', 0, 0, ''),
(544, 'test39@gmail.com', 'BH27JVBPcNhu1p/aplM8vNdxkPPK7KdAiURAbk4cn+IMOiICp2Lv1rVI2tLtLT/A5Id+B8zGLNUm75H7V1Ixlw==', 'oJZrvnX5A3QCsW/NXuwF8ymGBvd25FHHOg4uHBJ9ayK1yqhJGoDIi3QuvACaMt2hKCPn3LeIBab9fIdjy7hncw==', 'test', '39', 0, 0, ''),
(545, 'test40@gmail.com', 'ZSDa9waRST+vvI+sU4EtQXwtst5xEXFLCT+bBOQS+NgytA7QVE0cQpysFgEZ1MnZOINxiMmz/Gh+9WeER7so/A==', '0HM1VIUcxXvKU8FjETRE6SIT1VEFDjV8qrOaF65l/esNqjCiw43+saYdf1n4D4pAb4yVADUK0l4uRPovI7ziyg==', 'test', '40', 0, 0, ''),
(546, 'test41@gmail.com', 'KJG0qmiZxQ25HLefuW1rC89lrFW+Rmu7W0lmiNrvhayPp2z0mjg49Iaup72oYeNxEv0uCXfZ3ctfhtcl56cgQA==', '8fjsQW47faRL9i+Vul7s5WfhRLBm6uwS2Db8QLwhM0vmEVt9TZLPWVNoPLsbitrXdtQMtOkaoWvtGGKWQ619Bg==', 'test', '41', 0, 0, ''),
(547, 'test42@gmail.com', 'evnGlcQZSHUkp9igKH7d0gZEPZKd24KKqdaOFErn5D4YoytNEw6aPReQiZX8BD6kd09v5TOKGGfsRauIjrcijQ==', 'AZ2Z/sM+oMyzzaTM7l6e3Z1Yjuq3+kfskFtm64zcegAFfkdVO7aGYbcC4zbU5xNipq6zGCGA+By4/K4aDqR+iQ==', 'test', '42', 0, 0, ''),
(548, 'test43@gmail.com', 'oeGxN+hG4FnWhbHpYWqjr2DeWH5IRHwAzkfopfLSsrP91fkDt+xB8mLBHsmeyqTPhczd8avWJzr/3WuXab64+g==', 'AihiUvD2QAEVX7r/PjZYBzb00PwOxD3JNWIEg6mLpQbXS3tYyc1y+n0cFuaXG76Lk6PrpSnYw/h6atZ2kEoKvA==', 'test', '43', 0, 0, ''),
(549, 'test44@gmail.com', 'j7QyX73CTp5TktWbUpuzlNMYSHpxwTdU0ZjMD0EOApQShkSCDqSsK5Sb/qJYxIaHB7I5S79K7h6WJ047EADYDg==', 'xQhh2sf+C2hKgQAmzU/yC7JOYjbfHZfAxj3i/5osRxVemw3hsHwpM9GWHPKvWw6MJPJ1bxFeblel2YlmAedF9w==', 'test', '44', 0, 0, ''),
(550, 'test45@gmail.com', 'Q6SGZZoZZ8VBlBg2SEA/ls3ScpLltlVGw+2G6WXXEnsCHwSoGqhDLQ2UfWMJoKhqfpvXTL19aYuo0oaN6j2Utw==', 'MNXdX1YNixPObrU026sTszG3XyH0KorNf5MHeAXCZn5afsSbYLgg7vDNtWqU1RmFwLy3V3goyvkTxFlafPAjAQ==', 'test', '45', 0, 0, ''),
(551, 'test46@gmail.com', 'sPl7uB0sVthVuM6giZPG0s0eTHBZyuO7a0NnXzTtEKnYoQjZ+tfWTUgFvzDYVu+lm6xamX8C5VctoPuP71ZTPQ==', '6Y6qjMQOqk8V/jMdIeeJnP71mAjR7Bsi9yU9zTM+9HdDRLcx3eSTNpfUzG/a8wJpe3dejeCWSWEX1MIIijZaeQ==', 'test', '46', 0, 0, ''),
(552, 'test47@gmail.com', '1McvFmx5w5fCxjmrGw0yclHGXXfySXJ1kJzcpXf0YI8OzAxSrUbKne7fXi96SxtrzZ+izQPCyT/IkgOjyuTWxw==', '75lNSU10vjvlt7pM2xk04p4Sgl8bsFwmCqk0Qg0L2U/kdukmofPfj/4V0KODOxQirBMYTkzWQtSwIjgv2U3WHg==', 'test', '47', 0, 0, ''),
(553, 'test48@gmail.com', 'mmPWT3B1uerS+Bd/UQd0hPqBCimpWkxyMnzdwjp3/BUw8zBgfhizQTI6TzXgYFLaAvojSwa+3UN5WYGvtrQsyA==', 'ls6qXbYZZxxIj3osMlzSzoQ0oltjOQun4eCyub0uvT5yWH+ghs5MW0YQPaKxjKUOTbEfDiTUa8Hi6H0rtovrsA==', 'test', '48', 0, 0, ''),
(554, 'test49@gmail.com', 'U3eGYap22ndndf1/oeLkjnR69t6ekqqTVEww4cg2nfvG6kAq5d3KvYclgarkjJ/fIBDb42oSX68CWcOvHg9Gew==', 'mM6zzQ8VDs8POhQRTfD3hUUkwwJdViUu7wA9FKgfMwSHgevO1qpWNGgeL89YxdSvPd6jOUU1jf8CyNe4lPuAfA==', 'test', '49', 0, 0, ''),
(555, 'test50@gmail.com', 'LtHky22Dv70nzV2I5kWGkGVzJe9vpFef/WiPLNIugLswbA5PAN1VeEsAJVSCB0rh7kApT5tWJy3SgqmgY6FVuA==', 'TSL/Dg3+GMbXwroff2Re45IfdAZev3B4yX1ovqOLJ5A57J9ss54ZtfkP0e5pGpvnGIK70ImBNKLCSq4O3DTfXQ==', 'test', '50', 0, 0, ''),
(556, 'test51@gmail.com', 'LcM0o0Mvl/B+LzX1mf5h5MX5Flt9pDvjvCDJfQ78Z6I2i9wDXxJWLhTOe2ZxfxEb/W1SFSWRoPaFYqW1CajTpw==', 'WFO7MjYvSYLvR3g2FyThAq5TPqKInoz8pCPpcQO/skSscWuxyTCnzWOI96f1f/J7pOsW6bgwyuM5+RLxf74izQ==', 'test', '51', 0, 0, ''),
(557, 'test52@gmail.com', 'LkpVB0ibkXcyY6kvSULATs+WvAXKw9a22wYd7jT2QsKV7D/MRgZx827kmUb98RclpeK14xgnrzfkXP4zGnvMSA==', 'y5MnVdAREKzXaxIGt/IhqhrbTHpcOOrGGU55RS1pdwkIQ8WBPmpFjI7VXS6Dy/XNJg4SS71kjrbTSVjFmkCj5Q==', 'test', '52', 0, 0, ''),
(558, 'test53@gmail.com', '+QxogvrHBLaHqxpo7owEoy4HNxf7wmVeBM8DuJlpctZfYdxapAvfer3MAOJSHhG/IPi88jdVQAZVE+DpHyONSQ==', '3l9CrvQrVRUWs582fhxhfoxyJH9lDxhQKZqmP7BiE8mHdvuSYB9oREJwNRCo+T7CjsVFU6SZGYDWU70RDUVavg==', 'test', '53', 0, 0, ''),
(559, 'test54@gmail.com', 'okVojTejzaet5YUnqn9nyIYpCJ2px92Fzdcpe84vHVFZBH7mmXmksLP9fp0J4T4ur5/ujZQVd+XmFClCtqrMsw==', 'dm8sWiRxufAN7MmRgaSy4ZJlZ2yZuHjlaMaa9MGszsHNSQhYRUVM8mZbM9CPlKCAutFnunqDJdR/69i3K6B/2A==', 'test', '54', 0, 0, ''),
(560, 'test55@gmail.com', '7ItsEEdFM45EcHFr6FoB+hzt+riKGji5AUyi/m91WJmvREV1tc7k46gg30+0Y7WoetynWJ4/KBn8/C1+5SuE2g==', 'RntjxCDQ4ZT/qncwYa9dUbk2nrbYwm/C9IgeNSolFFIa5aR1Cm3EByp2Cee8Z2kap8F6hzJOSagbIjq7hIIkAQ==', 'test', '55', 0, 0, ''),
(561, 'test56@gmail.com', 'V07d2BR1BkmQjTyiGKb+wpfgxxF9bv2SQyvvqVq93boYkehodUuhBnAkugLjiZXytEOebgBiW7NWSIllhnQURw==', '3vRxNoD4P+eD0TPAEvJ1lYsIXA1+8rrZ8TfOFiDcvV8W+Fk7IYDvsAI/26bCk+ovwZgf3RmI+G9BvJptTWcuIg==', 'test', '56', 0, 0, ''),
(562, 'test57@gmail.com', 'ln9wlofw3vEK0z2Ypebr469/NdMmRqCdQviIqJIwl/XUc1+Z0E2DFsRCf5fQY+8R9HZsPKVj/Dkt+B+bKa8TbA==', '+7V1nnfiPa39S6Lt1kVU0A8GmlDQNwbQy6sZVGlFqREP+ukxCpNS6YZhV6YvA221074eSJyki8DBLlgzBvQ6vw==', 'test', '57', 0, 0, ''),
(563, 'test58@gmail.com', '/WlzA+xxhhaFBZhuLfAA1/C28KBaHzy1xRPORp0llyI8XiHuFpBc11KA5ZqcyXcuz9Cuogx9fqVt+7n50vZuKQ==', '9GvgrUM6Nw4p+yqe/qldqS4uAq02N286p08p7m5Ts1CHJ91xv5ikwJpu3cEj0tPOygWnKY/uJ9Jv1lJ3fQSS+g==', 'test', '58', 0, 0, ''),
(564, 'test59@gmail.com', 'ZPkPvcpa9GSgKZTHaLrLCJfmaUZWn27+j5krT8Tph2FMkHP6frgGiX6SvoTMK1JObIfZmoQ5br5SFQE+JNj8fA==', 'yTsrKxtZ9S43DjHz0YqMPPw92k2jzu8FGlPDOwSEvOZI8gcSEwwMbHWCX+T7tnyhrgvj+M2bBWSxJnDsDuCRDA==', 'test', '59', 0, 0, ''),
(565, 'test60@gmail.com', 'EFBgDb+Uw7GcU/MtZW3ua5bFxmjXHNY3PiFvMzf8t7fEEeUsMLKziom7LM2yGilAOVbML3Hg/dG4R8Sjs3O4Fg==', 'hapu0DApt8yhQaedui0eyHDeh3LNRoi1FUvFElaK4Vu+VX0h3ATwvdU0rNxfvX58UpNdwTQ0HM/7sBOIwgrMrQ==', 'test', '60', 0, 0, ''),
(566, 'test61@gmail.com', 'HpFwoIPXUWVGKCx+5td5Yc+r+63yPdMeuZXio5se0t/SvFu1ZumU+8otUeHEOni/iVaKXC304kZsRC6IbK1v9A==', 'cfQ+4WsfH+x7WN7J8IDXVJBqyTVlmcXeaumB51ZS6VjFHd/zSjCVIVOjddXcyARev1bFQb+F2PGkNKiZD3j9zA==', 'test', '61', 0, 0, ''),
(567, 'test62@gmail.com', 'c0T3oKbOMnQ1CsUit/k90E/s51I1EC7I848nwxtOV+wdPHhRfa9o32L23CsWSekd4yU13AOdd7SqGvOR2MNcAA==', 'OTAw0UbxonQ1Rd/q8xi1AR0mI+61dpMNcAf8VW6DPZEnPCB3Fr8Lvstv4j2h9aLOpJ1ilFsKPCnXEvA9SUNLzg==', 'test', '62', 0, 0, ''),
(568, 'test63@gmail.com', 'DbrqukfyJuEWMa/+82t6/g2rgO3CEb3pN3Ywpdm9Z4HK7JQq9wUL74d0ggsbPsu+TNSwixzBYIeh4+6wdB2WWg==', 'E9xYiKrr8I1kyzsfaFdbKwONODxHlW+iYfCaIs27lQiy87CL8XED87a2haTVDNi2ptIyzt6ldbyc0Kn8E0FjLQ==', 'test', '63', 0, 0, ''),
(569, 'test64@gmail.com', 'aU6X21jjnrBIIrdJQqkQnmLiL7BvNq7/D1FfG4Q8FD2XKk0bta198QMnEmw+GhyazHmb9hjVa0Ph2wmJ1M7Cjg==', 'Lz2cERCIoTyjLWmU8JWHN4nQvvRt/ihJ0LHkZcwx4bJ9bJeOuJEc+SkUIXtU9iHY85xEB4exvJzYw98sjSJoSA==', 'test', '64', 0, 0, ''),
(570, 'test65@gmail.com', 'S9AjPjm7GrGBHL11qBUt9SIXFpTrpIrgUhZ6LAXE5+HjnFz9O93Cq95Gt09IRDUa1kOnDa7LaUwIWOqPZpr1Mw==', '8Y9JMBbqMHbCDrdRszML/BeR9W5CBr98sRow6qh2J5UVEw+PbTG3+ySthDRETq/Zo7rLwSoEeysuwOh/oiD9iA==', 'test', '65', 0, 0, ''),
(571, 'test66@gmail.com', 'UzgauUICnwSOnqZmt88WAy/5M+lJisA+4UG07YIChHHYYZiww5sx1ZsLihKKJxfr2cHGkAW6LF734BDg1g2uzA==', 'x6RcSjyM/egnjxZN3pTnwlHMpjRvSFTlU9wLkXDrYr1ecCl2M1Py/rRi1JJJhfjHzGmsTZPXj+dvY4A4JQkXig==', 'test', '66', 0, 0, ''),
(572, 'test67@gmail.com', 'QgOGcRfJ35kN70wNopfgP6JAt3t8y825ebJFGmOJFMXQH65PP/ua2BWrseZqwYKhP/ptprtJKPAIJepxLXZ3Ng==', 'wzhTLaRNlzQvoBk0hEpGXBFOoXOYh/j7codo4b7FHgeD/z26Qq5DgsQ2Gic0qcu/dU6S/0RYw67SiGSZnnRnSg==', 'test', '67', 0, 0, ''),
(573, 'test68@gmail.com', 'rZfcJoxtE5egE7LzGHBE/xhRfi/RKi0EgQELJ57e1uYzFXmUaksLNcdB4+SA7YGtdA8F/Fu3MpWJyd6KoBpezg==', 'KJHJPPSaDveeANrqSKbtAglWfho0txJmPKdlGilWSLIG/Ny1PY42TpdAtNGHDNXTFrzcwJdjqcVYMn4WVp0Eew==', 'test', '68', 0, 0, ''),
(574, 'test69@gmail.com', 'ScKW1aTeIYPPCq/RoYJ5NIUo7+/gMrBavxeJvyGYQavZGfwDt3Isi6IDyNYV+bDhSw1T3iCubLdVyiFJwiHjKw==', 'e+oCb49ElPSgS+L48wp2AkrGU6Vl9SBctJ9YiF74AI9o6Tl5+fm+e7RBGA9PGDd9lfrX/MSE8QbCGDd5ywLS9w==', 'test', '69', 0, 0, ''),
(575, 'test70@gmail.com', '7h5WgUB/pYjMmXQcXmZMwExRKy/a+6TNs0mCmGgBtkEVne1DAyCVA98YQ6nx0Mf6zyLs4uRtDnCJLmQcEesoRQ==', 'hFEqogddumKluybsmUUQbZwP/URY0jsd9zmBRWqKllkoIJruTn6oa2ryU4cTAp5lrY0Xj27EctLYxvFZ+4j4/Q==', 'test', '70', 0, 0, ''),
(576, 'test71@gmail.com', 'UWGh03oJkB20eykW/m1F5F87Lq7G5GM5pw8Dd0fA973aliqI0flUv5qNJG1qT5pnw8P6HhXk11aMMUBuzuVojA==', 'rklpVCNr2rzHEAdizF7w713sV7qifSCzqCLDkLfUDRxBTmCCURw/A8n0celjZCFj76K0xYZMqqXG3VyHqdCFDA==', 'test', '71', 0, 0, ''),
(577, 'test72@gmail.com', 'yEJVBJTsvpZNajcGTILrO86v/AG/JM0OvK7k4jmXeGW7eka6DXoPthJ/fByBA2+xwu2ZrWDUHwL7uirF7La9pQ==', 'gUtWO/fSaSmDH9G55zbLQ8oXEm/OBvnSXdqTAq/3LbkgZ3Km54GJyUmGAn/OKHanGR4lQK/FLLpGhy7kjN8MhQ==', 'test', '72', 0, 0, ''),
(578, 'test73@gmail.com', '456hwCYYTlOoIIumh3R4aTgNXoU73FEskORQ9JPUXxXkZM9NCrR5tFL7a5yExIWK3Hgmvtfxtu2CK5IC1jdwWA==', '1d9pTNceT4zPyc9pYJIr26QCywbum5+srCUvRIT9s8xJORZHbe518D9ITDDMEIZQHs21YvuJ2fBMsedTI5KYaA==', 'test', '73', 0, 0, ''),
(579, 'test74@gmail.com', 'i9PMD6PSazedjKiN5UHl26LZ1EwR8D/+++cQmhsaB2ajmNtHAhBE8XW4XIijOuEoLJSx8L0TeBfYOtBKiXPVhw==', '+tFN+qaf2uyZ+/NGbCOboNbggRiq4hS+TTdFFUg54Bm8kLTKrqK5nqdWitSglhgkU5c5O4CMfOQSPcDjSDXLOw==', 'test', '74', 0, 0, ''),
(580, 'test75@gmail.com', 'PrFaMWvqEYr1YGxzFHsQtaCtNvHWla8KWzctvPkt+bFilAGsdASTYLdpqlgAF/joRS7wlfzVq4tOAv0ACgD+gA==', 'HqKYIBEcqAjw+JvAbM7XVQcccUw+TjLkXYi2hrH+cX69OE5SlKFqS2UrqRhiOeXAxF4CAajNsJHNJ80dLePzJw==', 'test', '75', 0, 0, ''),
(581, 'test76@gmail.com', 'pJadeU1P9lEKbtRK5edbzIlGTdnt+m+iyj8ddrOx13uYenU86cEhwuWHBmYiA0twQzY95GAZrB322A5lVmGMrg==', 'JCiSebpePXL6vkNpx52hlvhIbfC/ogzJ5L9udJEoc1cMDVecVuDz9FVbSTGk2C5/KBBfOJlGvo27v9jgy15jsA==', 'test', '76', 0, 0, ''),
(582, 'test77@gmail.com', '0YIWOZZz93pzSgbLgpjF8RWqIfa2uCoTUNG0hHD9lHkPUXpMmve2O4hQZOaf3PhJ9OV/q8KriFw16+rjkzt+2w==', 'yP0uYo3q9wbJ5vCBT/GB55Z8c/sJ4gYdlyZFHqRq7pdts8yoE9+3rmUQmEGNaYpgm2PKbWBR1C2j+4Z/wPm6aA==', 'test', '77', 0, 0, ''),
(583, 'test78@gmail.com', 'tzv3QUtiMhjXUEhvMSEmf4Fk2mamgJ9MMEnDwJI6tIus15e8B0tDIGcarVPm+Bt+VTbaLDar2GGunw+03RdLgA==', 'SEYbett5qvrEz667/+JilFYBzYLZ382f++s5EvLSn3C2SmzvWAsD8olAM78VCfnNuy825/svGNRA1/zkYEUGVg==', 'test', '78', 0, 0, ''),
(584, 'test79@gmail.com', 'LyTRRPUGf0dciGbfhfm+zFojKDni+vYbsTaJNHjGacXZxrwcHMIzjab1g+fOPJu1rw92i9hMGiqpMpHDaiii5g==', 'YLNiFjkWsKAgSw87SaB3rSAWmdoZQ6xNfx0QfTEHfmYWXEDbq92vg2oAjcB8+91TV5C1UlDJjqcODnio/5HJfw==', 'test', '79', 0, 0, ''),
(585, 'test80@gmail.com', '57ivPaI9WYq8WBE6brRRNasYTkwjZ4Qh1KU9VjmqwwJrvp0bFThRJiz/tODZtYCwpfM1fKZCRZVtCBirY5qKPQ==', '83LsQWE7Fc56R754sTP43bQMzH2nAUb7biegMUOBZ2MsUKVh6j9v1eAJ562jecrFpZCbchueynDsLdtHUs+Zug==', 'test', '80', 0, 0, ''),
(586, 'test81@gmail.com', 'r20PU02OZC+eyOwoTtbVmvKZxS5kcLkufp9DltYyKIZOjlSHpwlIjjZpiE1dknwoClAvVjH9NgFF17rlCNq0GQ==', 'bvYJW1+qD/2U3E12YAfbg/MIymmWAehKEm0BBXyWLp2xF7pPE131hVKmQvYdQ6+Fyn+MewJcoYfcvUb937idPg==', 'test', '81', 0, 0, ''),
(587, 'test82@gmail.com', 'EYseyiHX1pHtYcFl7uvVXsPfZvlAdtd4IdL3Q4Q+BujVysUHm4htrdDVbIV5r8sghGS4gd9oU8OdJs2iDNA0LA==', 'ZT2kZhB+RrQp/2zszZwTsuobFWTEbVSZgSNBBG4KChBZbRfDgswpg2UIhWaLWni9SIgGcMWb2T/0qkhdgp9e2A==', 'test', '82', 0, 0, ''),
(588, 'test83@gmail.com', 'GvgJ0KEXPU+4/7VRzTV8LBGAbMuDZKdrMNuVZL4zHSz93+nNhqGS00B+vaixnHe7iUKhT5nOkRp0aU4rh2T9lg==', 'gA2TdqCR6Fo3K1yfOkoaIzCE6Z6/vJdiDyqakeFZzHFODcutLx6P0S0Hfp0Mm6A+uJUz7U70IZ4f3HOffa8Sxw==', 'test', '83', 0, 0, ''),
(589, 'test84@gmail.com', 'E7rFZ7RLQXuEyg+3RyxvYsWmTSg4UEOCai7D0m7xIjUjJ8P8uXbLfOiHg14ydoImSz8XQ1DRhzvvRQLf+K8RrA==', 'aEBS88J+gPe/bGO0c55OJ/As/pgUgm/+rzbvak46iQbXVG4+Lqp6O4ncCv+Zpj4ffCYM4EIAZQNBtySd5sKSjg==', 'test', '84', 0, 0, ''),
(590, 'test85@gmail.com', 'ixsPQFBIFoWXpd73dgcJRLK5gnqp99G5qmbL+2zjOMkIBBy1mbH6B36PXS5x7K+HSpFMjNJBkRhu4TIL3B7t4w==', 'xBaUZ1mlUd9ExvL9XagJWWdfBZOh5wS/OHv0HFFK41uq/hLl4sHQRb1Tv24lvMNc35k1O+UQ5NCq9ttrFFTNow==', 'test', '85', 0, 0, ''),
(591, 'test86@gmail.com', 'a3J+zoGqZSqYKwmSgRG5zMseb5X105L9Hrwi9Go+z0JKOTIp58V8SzJtI5OIybtkZ8ffTOx1Zce3O6sj/me47A==', 'A2XpJ0pxXCTBkUNwrOtZNuJ4ufCUVhGOwDQtLx78r6qcOy1tJHc7sE88W/nTeoGE7dlKeOfURbFeWxPYlFXW1g==', 'test', '86', 0, 0, ''),
(592, 'test87@gmail.com', 'XICpQyuKumk2HZg+oMFU9Ve7oZFGyaZRlUhk7iWp4WpLJKzSPDMEgdJA/Uw4mZdZNE2CbGsAEUaxcSYaxudtSA==', '4kbED1+v7DfjP6qXjuDiOsogpWKgFfC2LJ3rsp+ERHJvXn1R5DYDxqbPEJkmrTxhAJ5n+G0WCWlJKJ+3xGoGLg==', 'test', '87', 0, 0, ''),
(593, 'test88@gmail.com', 'vYACaCGbECDOBOLmgIket1nsEs/ESrmF6xx4v6TuhFXSjGE1S5yfeAy1K/uPGtTtpQd5I2o89cYrgI8Q7aR72g==', 'a6A6kXL/djtsgTZpUaSaqRlBUXyOBw/LLxr2Sjg5BlDI6S/lWM4m3l3Wduv1o+a1wfZJ1iSGocu9Vt8DoOqvuw==', 'test', '88', 0, 0, ''),
(594, 'test89@gmail.com', 'nRFVUO7dRp7oJN9r4IqqpHlH5PWtnrbt1a8VI3qP2341mnrMbWihWRO97mY1xu+3/hfVc4GIJVgBYhfNo8QNPw==', 'kmkfDHJ67b9I/ja2WffUq6NzKfK2CfwZSFwLzegdSaE5LWjw30IgfhPjo2h72ObTe+BUXZihDitcxXUmpvuiog==', 'test', '89', 0, 0, ''),
(595, 'test90@gmail.com', 'yaO/BXCf7E7R41M+eU+8R/50nayliKk82r+JQYFdHaiTsPkgWXP2kdcnWbo6ZDzAKvOS5imzAhF1CI+EJCr/xw==', 'NrC419NJJYXmJtcUGjI1PStT+fVA5t5lS5wWAJIWfLnhgQKZNibySjSX12EnPf9KjI08kXI3oYr43cOVyqvNfQ==', 'test', '90', 0, 0, ''),
(596, 'test91@gmail.com', 'pR/8TYKYPgVv0+YenXh06JDkP8ROVm/A/w2xVgMu2cRcEPiJxhIJturXJV1NUpgA7HX/5p0hESxybK6OlTq5cw==', 'z3rgo+TyfkvjvA8zEJANMsZ9gSyG3+9HK4PKjwd4lXKwDH10TFoy/EaE7LTPOnF1dxvH1jrFm8xC9Eao0Q/zng==', 'test', '91', 0, 0, ''),
(597, 'test92@gmail.com', 'HhaOI10pA9NIHUIsPUQvrVSfnUzOxatj3aeTwYNkRoxpesTLRlvDB4GXGdjc0+gO2sCh4G95TJjQx+J+BpAmCw==', 'Ua2rKtbNXx2QB4kWtNmSDynuyiQ0S9pa/IXaFIj+G0dERVKoIFX+aqdQX+ZM8FVkexVVT3v03aIjguzKroXt5w==', 'test', '92', 0, 0, ''),
(598, 'test93@gmail.com', 'MEVB0W8Zsy8DdyiHCm8oa2fN7r1sqCCzSYb5Oo4yWof7vxWPycRAz3nX8abjL/Ywa3IRPIpq6gTfFXCOnEyZLg==', '9VpEQhShXHHfTYmWMm8ud4McdXj7RRBfpffkv+mGYDLr9skgPYuVakc2dZF+t8b+lzViIu0zrZFg20IDBsnxEA==', 'test', '93', 0, 0, ''),
(599, 'test94@gmail.com', 'GRtMdUSxQ0mEE8MHmWePGwm/2Iyq9Y+dQ5no3UeZI40Xzp961RbAr4Xldo+9oOpSMTqt3WJLqneDB+TFXzUvhw==', 'TcUL7jnJ/cWetE99KxERtx+f7a9H+rL0clgu5IbRtV96oeYhoEFc1pqrAOY2snyuJ3Dz/sJB2Y9MR0mJl/Zg6w==', 'test', '94', 0, 0, ''),
(600, 'test95@gmail.com', 'jTaNh0rCQtDOCY14mz1R2GsaA75pZwg6zMOfjcdobrwTpn2qjd7q0p14qIWPhi5i/0xaZ02BsHP55fqwtGqPrg==', 'PKNHd9ZDUBC15ZAHDGxQ9k0evhS5nQSzcIOMoQWIjp5tGcimumPJHJcK0G3N0eO/25Bcdrngk+zYcjm2T1kiqQ==', 'test', '95', 0, 0, ''),
(601, 'test96@gmail.com', 'M+YywsR56xx17vrffS5BRdOD2Jia3VlQSO9qMEITIU2sZkEI4yIOo7VeDUv4igFdFPpBxAVCg3qV7DxTTavt5Q==', 'yHSHsr9BXXGUTujkJ6qn95P5a5z/kafRBZp+ZXb3L+YNCEilJUOjoL2KJUJ4kazBwY3deqoRMVQO1DJK1S6AbA==', 'test', '96', 0, 0, ''),
(602, 'test97@gmail.com', 'fE7RWhAAdZWKXlzZEWTtxEqm+qOSgkDqfolknPZcW3hOr0rPO6ccC59yq2x/Pcqkl2ljXRd+Lxr6Xo6Z0SkuFA==', 'ydgW+EnOvd6mvSMJfEm/KZlK9/64Im4xiCGJQ5UTyEzGDO4E12uGDl7oUcR3iA3r28Ecf/NF192w32eZ3YpFug==', 'test', '97', 0, 0, ''),
(603, 'test98@gmail.com', 'IEWl2Lt98vspB0fd2a8pNNXaZP4I/tVZzkTc7lmQmjP5TS0TzjkHAyTPSgC9rLEsx938UrG7SaPzPo6QiRbZxg==', 'd/aEWdOziWMrHhGc2vN5z59008XXIpMnu5vnce3AnBO5Y4ro+fdZSaAQ47KiWRZqzE24QZtvmidpWUnLBJ6G6A==', 'test', '98', 0, 0, ''),
(604, 'test99@gmail.com', 'q+C8Uhkj7oGoJY9yHEWhpak00J6kmPAOUQXlRFQB/Na6qSHy93ARujtyP4pd42UIlXVt5LBSR6gSBw+Bx9G3cg==', 'njYavGHKTFW9/RHjkWcGjiG9I0eg+9pGSt3Ykp7LctxGto4r3AvygIz+VmGhjczpZ7aOJi3wWyaB8O1e/MqZug==', 'test', '99', 0, 0, '');

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
