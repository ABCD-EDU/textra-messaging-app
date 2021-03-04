-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 04, 2021 at 02:46 PM
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
  `is_fav` tinyint(1) NOT NULL,
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `group_msg`
--

INSERT INTO `group_msg` (`group_id`, `user_id`, `is_fav`) VALUES
(22, 713, 0),
(22, 720, 0),
(22, 715, 1),
(22, 712, 0),
(22, 711, 0),
(21, 711, 0),
(21, 710, 0),
(21, 709, 1),
(21, 708, 1),
(21, 707, 0),
(20, 709, 0),
(20, 718, 0),
(20, 716, 0),
(20, 715, 1),
(20, 714, 0),
(20, 713, 0),
(19, 712, 0),
(19, 711, 0),
(19, 710, 0),
(19, 708, 0),
(19, 707, 0),
(18, 716, 0),
(18, 715, 0),
(18, 714, 0),
(18, 713, 0),
(18, 712, 0),
(18, 711, 0),
(18, 710, 0),
(18, 709, 0),
(18, 708, 1),
(18, 707, 1);

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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `group_repo`
--

INSERT INTO `group_repo` (`group_id`, `is_admin`, `alias`, `uid_admin`) VALUES
(18, 0, 'Hello World', 707),
(19, 0, 'Foo Bar', 708),
(20, 0, 'Lorem Ipsum', 709),
(21, 0, 'Star Wars!', 710),
(22, 0, 'Friends', 711);

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
) ENGINE=InnoDB AUTO_INCREMENT=1172 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`message_id`, `from_user`, `group_id`, `message`, `time_sent`) VALUES
(1171, 707, 18, 'hello!', '2021-03-04 14:39:00');

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
) ENGINE=InnoDB AUTO_INCREMENT=3782 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `unread_msg`
--

INSERT INTO `unread_msg` (`unread_id`, `message_id`, `user_id`, `group_id`) VALUES
(3773, 1171, 716, 18),
(3774, 1171, 715, 18),
(3775, 1171, 714, 18),
(3776, 1171, 713, 18),
(3777, 1171, 712, 18),
(3778, 1171, 711, 18),
(3779, 1171, 710, 18),
(3780, 1171, 709, 18);

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
) ENGINE=InnoDB AUTO_INCREMENT=808 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_acc`
--

INSERT INTO `user_acc` (`user_id`, `email`, `pwd_hash`, `salt`, `user_fname`, `user_lname`, `verified`, `is_admin`, `user_color`) VALUES
(707, 'test1@mail.com', 'Eb6fkLZrATunRiYaSLFuRvsZrV/Ihmk9IN/22DOlJQ3lYB7KtH28VjiKJeNcPeO8jNzcYOXXfkfV3qGEjCU9gQ==', 'jM4lqQnMtElVGYBQaq0xytiCZMKO8t/2RILEWKWFc6YnhNNkJan0AjdJ9iUyh/sxpkI6MClm/rApAmncS/kHlA==', 'test', '1', 1, 0, '#16A085'),
(708, 'test2@mail.com', '5xKQyuD1skuTAsPZRt22onWe9C5vowIuoCa9f/4aH8AjxTb+ir8QBsu7zGuZtGxo1Tj86YKqfzH3V9rhQb9aRg==', '68UFDSWfojwmLhCts71+GU7GmQbaVWdUpHGMC5F09eGoS4gC6pLN1WLDp0QevlCd5X+kWf9NKJrvYYq6pwkXWA==', 'test', '2', 1, 0, '#F17EA6'),
(709, 'test3@mail.com', 'gjLTJAt2EV4nAq/9FfOzP69TzY2eLYRCw3iKIxNcCnEGF7M8cG9wbKsukoM+lBwhcVdc6X1dhK6EXMZe2fQBVQ==', '5F9mE3XNBhnlJiDduMGRZ9vXeRyCO1IaGa1CRqS7Q6HjJcsHJt1tbu2jDei2gSHaFzMFE/Q7sXClN41z76Dlbw==', 'test', '3', 1, 0, '#F17EA6'),
(710, 'test4@mail.com', 'F02lTfGe0GYwuXNRHmNm+WT6ij/rqriucfpOnFDiWsO6PNaiXrNQluuPejLEUzjp1je1O3KB6pZGFMBCi5UEbg==', 'GwyYyyf+F/RGTO+TbS6WhPfgXPNuY3Www23XRtOGVClwlcTQNyqtMCfJ0I7cFameRPeVsxFVtzxgv2mGXUCFsw==', 'test', '4', 1, 0, '#596275'),
(711, 'test5@mail.com', 'f1K6fg7fUOer7HNgJXWm8fPkV1Hw3vIlXxDcTCOpK3trJ/2zPmk2Kmhz2XVkon+eezp+pHgU+31FCz8d7vPG1g==', '0OrVnxXP1XeM+dXqxOw8UNK7kcL4h1Qf3lYCAjJ/3b8VRKN64cxYHqTEubgJEl2plAgKMmrM30cgMqswT2AR2g==', 'test', '5', 1, 0, '#3498DB'),
(712, 'test6@mail.com', '+SyyAw9mLRr3Du2aUHXp1/sWlCfhUOmUFkDaEF1pMd21NnSEhY/Rns1Hn0+4TstA4+JJJKXvOEa3taJaneOPKw==', 'y6k5tK7GH493uVC7d2Vvcp29+/H2MBhOljTfO0U520fy+L8eVBPzwjxQn1x4T71W2bd4SfL27V7Vf2dDDXm1/A==', 'test', '6', 1, 0, '#596275'),
(713, 'test7@mail.com', 'ne3SYoRz8L5lBE3UnGA2F1NNvVjUfH7V/1186LUjQyYiQd+yb79XkioQEvI1T1Hhmjt9m3i/Bz5AK8BuVYq4jA==', 'I69wssJJlTAQXjnkpxU4Unl4cB0Hc34EmjGEXgCamOR0fQc6Jnn0f8NNXpFE4aGn1lsaY7iBIptjlwf9y45n0g==', 'test', '7', 1, 0, '#16A085'),
(714, 'test8@mail.com', 'zLcnSb1ecG2BJ2ZbiSl9RDhDZEQqdDF1B2v4qEuBoR6RWDo5xfaCPUz8EOYr9Gw82NpTUbeyUzTaYNBNvKAMBg==', '1XtcvRomF5gxHyBsB9EhGBtR7uHx/KgQhqnRp5kgItinFDyygBEe4S6t10glxbLn0Os6Eh54Kzb2rXLZR2U0ng==', 'test', '8', 1, 0, '#F17EA6'),
(715, 'test9@mail.com', 'YgZMjBZLL5FI9u8txgIA6F+s9IsKaI9GDUPwud8Vh0IFbOKAc8TG9rwkp7p8LsA0QZ5xmLWm46O6APBlqHLYbA==', 'QhJy7j9ZGcK9aDdgBLWkqmp0Xq6rqeJkCMl3rqeIKb8B6/2G4+BiM/rxzySJVPPSbNz5Ctb1KnOmMxlg/X3hXw==', 'test', '9', 1, 0, '#3498DB'),
(716, 'test10@mail.com', 'yHAgr7oKn/7Ariv328QjHG9N5Geb6CGzMGraE12ny8y/8hL9E6IUiF+5EidodtoHO/mvhOHdCXY4hNg0XW2Ueg==', 'n2VmxNbYRqXLTPMYNW9Tr7fIk0NTdJ+u/DQqRk6Y2jGBP2i2aj+u2YRCrbMEy8ZZxeOEzvEi2GMZWcNAk3b0dg==', 'test', '10', 1, 0, '#7F8C8D'),
(717, 'test11@mail.com', 'kVfPrCrXKjbhDwTYKY95ia4zJa4hMMGA5aw9Cgr2lJfHuFtl9OQwKe0IyrnOltaeKZunL0R3AfnoLnR/XDA7hA==', 'I/OWF7Y0eI/QG4SQ46L9SgYMIfFOay5UoWaoM6BygxjRxK4mhaSZMVpgAFxnG2nYN8oVu3wTbuxQRjgd5OHdag==', 'test', '11', 1, 0, '#9B59B6'),
(718, 'test12@mail.com', 'mAF7WiYalN9iHBsWKOFVA9CHycyRKGYVzRXrxnjhrEL+rqDOEZ+Cftx5leP+tVhcERyVF+Qw0SprYrvi1KfmUg==', 'sK55y0iZJQkZ0NkOCqojcgbAxgr6SzsIa5mSlUVroI4s1OxncCgn0Jh3H11hwo0Pv0mEjawwz7qD5BZPJ5hJwQ==', 'test', '12', 1, 0, '#16A085'),
(719, 'test13@mail.com', 'btmKp2RO+anGhYxEWEusz4mK/KlEWDLaazOu+Ol2qvXTnjLdWPmz24JDJviWXoep4GNY8sy/pS9gddd0KBtN3w==', 'ATHbEXd46Vxg1g7yx3uhZTf3eVTh6s6jedMzulCwYFYvCrvxRtu+v45wKigucDS23kRnLLUJ9KlOVCz6LlUkYw==', 'test', '13', 1, 0, '#596275'),
(720, 'test14@mail.com', 'd1pru6r1qqaH6sd1rUH4N+M1rh+N6/EU/C6SjJg0r1ETfFcnXVhC825m/GahbzPnvCkorMuVS7Z62+OxOo/lFg==', 'b3ZBADEvQvvkMqr7EgpWU1Zv8hfx8rp7NYImALzoP4MwZeZBvL1Z5L/cwbtBCHCWPZn4yZc5RsxfpLSGVRa56g==', 'test', '14', 1, 0, '#16A085'),
(721, 'test15@mail.com', 'IxleQxoZgsWWiR+pqCNRaxYtIbQK1fr/0N9kBaI8ItbBvcJdF/EJ+D+QqKtDgb0yHtObXwaPyM/6dO/KHUzmWA==', 'ShqwPweTk6gCq4h8+mEBewucCUDy0aknpOOGSlPz/g3F7RoHNByS2R4WVW0HnV9zCoC6Bd9HMvzLk5eaVYpBYw==', 'test', '15', 1, 0, '#7F8C8D'),
(722, 'test16@mail.com', 'SVQc2O0u0AlAMyF55URVasnswZKK9W2xxuOpTUYxvIux3BEigcJS/YZHdXmTDtziYyWc2PjK67nj4FRSu6XlCw==', '4JD6mIX9Ux4dvgqUPo3X+hdYT8azwvUpYoS1XF7zezc8D+FdeMQl+l4ztp/X0Pr9ozril3IlaRyYMiqymmyPvQ==', 'test', '16', 1, 0, '#7F8C8D'),
(723, 'test17@mail.com', 'VmppZ6fIPpv1t1LkSR4Oaz8Xu69iVALYVtCFY/GMNB7arVP6JlAtwOxGApNUBQgsMHEibAatEjqVxgspvUJCmw==', 'mqJbZylF25Zyd4CFv8fiN7btRD9MLWkR41c04ryPJEjAAeTki6DCsfQ6AYIfVvKbCPTUO0+KdgPflHQCyVgwrQ==', 'test', '17', 1, 0, '#596275'),
(724, 'test18@mail.com', '3Qjj83ANFntV43ljVCLXc9ndqTzBnsJIzdxK6OMrefGuYWhCbiankSwdtwZjgi80eh5NWxj/8F7+SKt7IbjHjA==', 'rYVcfSuvfk5tiOvHWw+ZdyBsNmmMjN4ZVfHUQHZvRRkiouS8bedBuNcsYovh7qBjhuMlrJ3sIoXyiks78q02Bw==', 'test', '18', 1, 0, '#2ECC71'),
(725, 'test19@mail.com', 'YYbSI/PqdVgHZboxGoN04VYlj4Rdu9unAX2gF5QNS6pL9db6fGh1DLfR5QOZ0+fKMSIluWY6fu2UV89DNzJgBw==', 'oJuL622LYwkAUoENPM0RHu5aItj9IQGgclLvP0QsYF3AR4GvOvrDLqw0in5Vli5BepdnABQYw2e+1eVWeNuBMQ==', 'test', '19', 1, 0, '#9B59B6'),
(726, 'test20@mail.com', 'ImCq/VTjbnjZ99OoN7Ox48OeWaWTk+I9fmZUb8Oy5h71UBnLZizK7gAwyQCOzOqtYQDQ/1Bh8hSmc8OGL5Z8Og==', 'Uy2ivh1JOjos/dddQassWY1zvhyWVeLu4XmqjXmEmdfYdUQ2T4/RkoYgCYR0+YlVNhzIbKtEc1IDTVhuTvfeKA==', 'test', '20', 1, 0, '#F17EA6'),
(727, 'test21@mail.com', 'opfLy0gdMqTq4LC1kZtmdlEEKe2NtmXhVUc7AKUYQ/hnaua6qPLyPocMeF4ZjacCWgWRiIPwdGiwqeRWJs3p7Q==', 'E63bmNNFWSSXe1fstPOCAdNXmTxZpa3RB3UMo4W7TUvjP92FUn3sVHmSOJyskmCKVcJ/PpJByHNEJFqn7bVyxA==', 'test', '21', 1, 0, '#E67E22'),
(728, 'test22@mail.com', 'yidWlFNYACwVrC0MMRfzfePj4vJtnYILgoaHfLtu0qfJYc14DBJfYFqWbxWVIZWIYrmlYiBzfqxsfITqpO+vnQ==', 'WRK7lbOqD5ccEUQG+TcVuKJafj+kw2b/ck+1pCFCgHN0YRv6Xc4iHW7W9CzcfdtJ4EMtW4bkc32K0DFAiaoe4A==', 'test', '22', 1, 0, '#E67E22'),
(729, 'test23@mail.com', 'a/rAmU4RlPqj606dOM2EcM1T5w8sRrK/f3ZYOaLFA5sxNUdsFUQfDoSUfEN6OPuUGBZQgJjp8T/47fmzgzQtjg==', 'O0JX0wq3dbZFLxYvyRtxSde0q4ymhZH4CGofCVGpWi1vxTNY49XPR6Qz6YzP9ZETM3K6X1R3QYN2q51yjSDp1A==', 'test', '23', 1, 0, '#9B59B6'),
(730, 'test24@mail.com', 'ATSYeCjHrYD9X1rug8gsPTnjKtOHu264IAVzBE3tBGyi0iXMjxqGK/iIx3uJagpYv2WEhsmwlleBkw3Zlk3yqA==', 'RJIKBQ5z0Z5cxQjuIHVMZ9MqSKq+X4tjwNUkvsuHL+4ps7nI87XPIts9wgujgqaNzlz5AEH2YqSrRDQ2uoRiDA==', 'test', '24', 1, 0, '#F17EA6'),
(731, 'test25@mail.com', '4D+dZhrMA0szK0QnBtfChtNhMfoHBQQxIaaQQIWtUuaAMIZB33K3spRquaQ6HQPtApp/nAn4MSYABA4j2CLQFA==', 'Pnv+ZocvY0jkZ3b64lHEd7PoitBivub4d0WdREJwsLdSCWmH4IuKrqLch4gKi/AyNfOwvQzV+d7daqE9JBFMUQ==', 'test', '25', 1, 0, '#7F8C8D'),
(732, 'test26@mail.com', 'DEzBQXDKs9/q7nKvUtLHsabVt1cnn0VEnAwc41LWKRfqo4CWAGXUzGuw93LR4/VemqcdCYBU0gCMplRIabVueg==', 'Orm3pL8Sb7czu5jgNwIYU4XiyZoHEK3OhTiaZZB+KI85eqUjuKWdxxvAogpGqeCuPuL7cmbRxlLhJnKhIfvXJQ==', 'test', '26', 1, 0, '#9B59B6'),
(733, 'test27@mail.com', 'At9E5HXFimquR1woGiHjImOAH+rGC7tcM1Xzw95Mp2LdJiGTCWifEiUR+P091ANGM5c1j3h/c1Xj1eDBu64bJg==', 'NKQICJKA+fQvVR22yBUOn36XndXIHcbBCMKHbW/0kBGC4fRLgJ4j18Kq80lSjh7EO5+ebAOK6uokHxTiEVccyA==', 'test', '27', 1, 0, '#7F8C8D'),
(734, 'test28@mail.com', 'aynVNf0zpoJDEk9opj+kFXHiMOpEcVC9KTHwCS56mfQ74726n7Q5ZgGY8MpXTcavlIEeSq2hRJ6MpfSJ37PDJg==', 'Zt4iXJQrlFZVols7/BWU8v2jju6aySZ3HrVrRV/WCRzX+dzUgGvMKAbp2ElkyyOYSEFugbFDM25uCyY0RLem1A==', 'test', '28', 1, 0, '#2ECC71'),
(735, 'test29@mail.com', 'XaAm48nVNj/5qAyxBdP6BGCik1qUBpDicP6gjvTuOALqCkJ1iM5rmhJKhfNNWyK9WS1SjUEEeeFE2/3w09SM/A==', 'S2EBZ2W20XICsIitaSEbUDA7XK/4nURkCr2PufJkXV3qHVlJsNQ23IVzOeozfh0wH71Ieo+PrIUG+Mp2ObZaeQ==', 'test', '29', 1, 0, '#E74C3C'),
(736, 'test30@mail.com', 'cWjHMH+b3aJCN5MgeNmqT79s8KCG1GI7Yp8QKcsFX+bbN7Wxr6RWv8h8JZvHsi2a2CiEE8HuFEmOi0ewfeoPAQ==', '6nTKCClercd38O9IFvF5BEixipg9tY/URfcLFX7nqaD02akAxFhzd5AwcZ7Bm/scUraSLOZdKtCuRDyUaul5qQ==', 'test', '30', 1, 0, '#F17EA6'),
(737, 'test31@mail.com', 'XVPxikJvFyQDiQrg1T5A62XzRdeeNewGPRRdz6ofW2E1K4U5SI0emBDzhRenxGZ1LZ0o0HxfbExOkf6pHYBuJA==', 'u7sIC1/x7cwqc/VsYndq3/eTyZ/YXw5k5R0X/Q6eY0J+v83XTY5VDzJZuv8IjBttWNVdpsGIK7zwpbgm+w+BkA==', 'test', '31', 1, 0, '#F1C40F'),
(738, 'test32@mail.com', '4ko8X0B+1hG0A6mvivIH8UfC+JlkL10F8Hi39XeHrgWojY3Ps/IxX6n1tVADtghKYng87zB4clSLdi0ibkAHmQ==', 'dqArvRO8gLUZTY9ej3NTQSJYKPOLotspelPdCCf4XmgEe5D4KjW8GVCP6WSlAWmSsLfAyyFs2EZyPe4Zu2f2bg==', 'test', '32', 1, 0, '#F1C40F'),
(739, 'test33@mail.com', 'cDRzTJPdn/WQ5jfp8GLUjBjimZBzYd/DGdbvd+TrBRDMQ0vq8YynYtKcADFaEZdKB8Q+d3lwHr2T4FT7LJIBww==', 'C9c//kaNhm9l3suKhPeeOScNPeWans7QaGytxIU1jXFBBY/CYu28HY/ABhUadQOLQ0z4qN7vU8lGwtoaHs4AVA==', 'test', '33', 1, 0, '#F1C40F'),
(740, 'test34@mail.com', '/oWymZPHmEkSTejP3nqYFO504AnnYGRTnO5mcuomUpneetH7RqnEZva40vf+46t7/bcptI9KQvq6XWB3Bvfspw==', 'xhIYuLu2UfggLgmHLyNCust5tcDHpONzgYw1/0nNSF3NLt37CI9KzORYr7OXIX4lfTFA2zIJ33TnfiyV8N5cPA==', 'test', '34', 1, 0, '#596275'),
(741, 'test35@mail.com', 'CU0znZlEzHre+FENmdymsE3Vbq4182hAWjZjbn3P9R/+mGQEe0iLAfKaCB5WfBfpsr6u+zFkURiIHoUx6mobMg==', 'zXE1Z1SE3cl+jiE3wODe9bXlLYe+lqeVjJxy0YHirslmjRC4FnU+W4zxZ35QqNUMIqRB3Dhp+g9jEsF7nYNRTg==', 'test', '35', 1, 0, '#E74C3C'),
(742, 'test36@mail.com', '5HItrl/6caK/Jq+6w30xv7b7LNabdHPs7xa0h22NXcxXe+aFvsnRGfCgu/lzks4ZctZiqNLfbi3oqn9/vTh97g==', '87ISp4vSBGk4BirToDVWsqKVqev1zQiZnDQIyXPdDsFZWqfADwcetxuF22ThOL5WOLg/EvZ4uvZVKxDClXN0DA==', 'test', '36', 1, 0, '#E74C3C'),
(743, 'test37@mail.com', 'Mm3W9Ye5xTiyXkxaEpCvCa/r0AS/5A8A9+BdK92GukEZlCp708ySQ4V4AiYANPjLRILP/7xJapyoAh3H2CUgtQ==', '38Ege23IjLIpcxMMcKPi60xOUVhNvxytSUw2WCxVH7Yli9yzwSBIZ+FMy6LhEW3nl+6ASRHlHd0T6X89d49g2Q==', 'test', '37', 1, 0, '#3498DB'),
(744, 'test38@mail.com', '1psk+1oqMzX04tUX7gz1xqzAttju2YPw8TPpP6MVMOxhuNumyI2H7gzRSpMYbY6AzzXR24PBnT1S12Zh2soSHw==', 'b4JJoZamGHfXbqOeQhnjm/bhU+B0OLSUsRnAEZMlKZ6eWjHFtwwmsbWXKsyGy2LddXwMhhdUhp/lB7yGDjalRg==', 'test', '38', 1, 0, '#3498DB'),
(745, 'test39@mail.com', '0LXfPtAXejQqoT8RTBnXaqgJVXNYpQkAJPdy/PT7t5BXaSAmWFAwpnYPWDVZR9FcSRFout+zncQAzTRhlSYAqw==', 'm/sucQXzI8dQzuA0PHzMxZTXS2R+Ad96y2DIAb1KP1m964FRV4LAMVYJNQQmRPDReIiUZxwnd52gtqZxLGfnsA==', 'test', '39', 1, 0, '#F17EA6'),
(746, 'test40@mail.com', '9rItIv9DtW5BTZH5Lp5Dy16boX/v/d2lUGHnf1l0rT9wnB/tmggoOKk91GB+CvN3PngYj6Ure0JTjbMr4uHuHQ==', 'Q3Ao9MKitBNeUDrOSwRUHNAatnmNTuCmL6ErOL00BZHxL0f9vlI2wND9rTI5zpqmBwSA/34BZWyCPvp8xa+iTg==', 'test', '40', 1, 0, '#2ECC71'),
(747, 'test41@mail.com', 'zr7wjwF6moCcMxnEOXYme3ZwWWd6OFQ1fwwXITfw8GQvWASwuGq+XHpM0/VdLGF74F5spDqCL89e1vO2zte3Kg==', 'pNxoACFPxNOXbAjHLCzpYs2dAtp1Q1GLGqeGNnDFVHDx79IScr2UwzTdbgtPrzDhcVJPYN9gD5LLUHFgGO0rNA==', 'test', '41', 1, 0, '#E67E22'),
(748, 'test42@mail.com', 'sTbZgX1Vwt4fBWnfivXir/MIIx7XGpwsYcgbOPtHmy9D/dMFbQU9kBeJivu4EmYawrMwAX0XqfGYZ/gKjwDm7A==', 'OiKlTNmcsjCmv1Myj5yUxBArAca9Q1Hw3oi0GiGSm/giuPhGAKBUSH8vOVbDgr2yO4wd0DhTFF/F69I+N7y1fQ==', 'test', '42', 1, 0, '#16A085'),
(749, 'test43@mail.com', 'nOdH8O+kdC14wJmm+cih1/pr2h5Sw62YSO/txoWGQIGA9M1Ngtz30iZ07gWlzppc6WIsqvXOeQxcJJ1L00Be1A==', 'fvgMjKQd8NM/hlia2Mrv4F3VNRssa3JxqEVry8MDysHO0yE/m82GqeCl23CPOTY4ZXySi4/JyYWiBTtI/5hKLA==', 'test', '43', 1, 0, '#16A085'),
(750, 'test44@mail.com', '60ih2ZwRyrVEazzY/MIl7ehVT2INs5tJTQZRuebVWYERjfH+Ez1j7RWNZmtgmb8YOZxtNpHOv2d0mGkBsZwkDQ==', 'V4ke6vbGeLF/zn3xpswXGdOIkWzy2lG0LmRAI/qPxl5AmcLhGfd6BmOvg19rVyNLGyMHqcK1skUrpOp8nA2+qw==', 'test', '44', 1, 0, '#16A085'),
(751, 'test45@mail.com', '3qf5awAP+E/Encz1Fp+JTjLsA5ng/+SRIcUylWrF/Bcs+NuVZCBgbwiNsG7/InioL1kEh1+BjfuH3UG/lZuCIA==', 'CTvfplJlYipXyrIm5LWkwnX0ACjAy1Er5mBN1OB+/eruq/Pj+jmDtvwkcpTiY+z5aNT08zR6qEPZv15N5mcZXQ==', 'test', '45', 1, 0, '#2ECC71'),
(752, 'test46@mail.com', 'baMkKSQpDaujyCQ67CDHklItUI0ErRGC65QbTE5WgT6K4zyDFq8erBu4eh6IJdpwHArfcZPPFASmhl5qE02U2g==', '0nO6xM7iNXSf0tNs5T9fSF5FB1VxY7ebtj7uoKZJ7mBbG0cu4WTJagpQBQrcs5KvTQovWIZfWJCOswsICUPQng==', 'test', '46', 1, 0, '#9B59B6'),
(753, 'test47@mail.com', 'M8ApB8E6C9zns/JahfMzwzfIZTF6HiJF1emTRYxCp1X/WqoMLvpI1MVQUkN42gMGsHoxS3NimVXusV/9+O5RfQ==', '8xDhsXZV9+xJAgIcghhs7PZNl83GHDelJdgfs+5LitvPJxMRyr+5L4vn1Ti4bQJPBfJB0E9leTpkQH/bCIeDmA==', 'test', '47', 1, 0, '#F17EA6'),
(754, 'test48@mail.com', '415KEe46Yqy4Z7ZZe+9M89mhWwO0Aahz1rDPSxMiQMvOHYCFFLAswspMhywk9krQ8/Gj7CYsCyW9WCcdqh1TRA==', 'DjzHB8yqYTKQJUV80Pveq0dT4BjAhnLblMDoeTYexpVVnbVf0ECvA7PEYhRQAR6KHnLU0ffoc5eb38B4Zrsi4A==', 'test', '48', 1, 0, '#F17EA6'),
(755, 'test49@mail.com', 'OM63RD8aTrFILnd1hKTT5qkpy3kUg8bP0u9s3w0FqKc3y3oEJPxzoSeO/+U6o/PYlQnx9Ek302klOi7FVfUmqg==', 'oyIhXb9hEZuJPzzfbjJrVMXnrBIbgAYcItf+zKY/nthJpoI75PHNxZkyO97WauCkUvQDlAiJFiqByI2u6019QA==', 'test', '49', 1, 0, '#E67E22'),
(756, 'test50@mail.com', 'Y1l3IAz6E7r2K03L5kozlHD87zRE3ILAtfLlmff++kniQlFOnzqPgGVoDpCCqYhdw7pQ4uBaCaH5vHCYdV3bUQ==', 'HjX31d321ThGEbDQhyKzChy/8iVxSumYh0evkYZ7lQR/rrM4PFabHNuHxLvmPIQO5SRvEYNOEO1x+9G0TVqIUA==', 'test', '50', 1, 0, '#3498DB'),
(757, 'test51@mail.com', 'L9dKzQZmiQnCEe2SwJZiJWpWQEgdzVlD2GrrIlAa80i8iftEVuofV7WWl1ft9rba0unKtHZrEX64FYLfshcAOQ==', '0TPUXEVVoaLr9NwNEweYCeRqroXmPkpisOTTBZOBXQuwXxdQRLeB6CiYLtKx30P7U8fqFi+giwMuQXsBG/2lOA==', 'test', '51', 1, 0, '#9B59B6'),
(758, 'test52@mail.com', 'BaNw9z7rkryRAyiNl+vy1alRBRzai9BGcTgqvX+JW/ydaMKnNlcijaLwouPPzfQ5CeXhztjprCsFktKQehzFcg==', '4w0FPAHOvoO8rlV889JfkRt/HY2YVEylB/+juHXvULeCDyDfdzFDIlQEeFnneSa22VqSkmk3tRiDlysQHQwpwA==', 'test', '52', 1, 0, '#E74C3C'),
(759, 'test53@mail.com', '8HnZwy8LIvPJRcXBFeL5u5AnaPoUdRfvoI73Qy3SWyrpUb4DWvflf/mG5kVmYR0Ba6/tuCLhmkxk01b66QdIUw==', 'WYswany/dzzHvzptkvXr2VCCleUjFcqxd56Du98KS7H1ljrMMPzC6d1ue5sS8KMUnc8o8ajkN5V8LM7kHixjCw==', 'test', '53', 1, 0, '#E67E22'),
(760, 'test54@mail.com', 'pt3SQjDnFPO2uc0G+daGtpnvLITdSgPj2NLgaUInU4p1aGBDudc6wCJKW5TZ66/+gjhqOSiBZeVbdsH2yy8H5A==', 'wzCqgLQGsOwly68HQVELP6c5tBk2rmOAHOzFYVjKolClwhLlwVrdgkxGiiJr+3/5N/6pOFsOXN8WLSMEjOTWKg==', 'test', '54', 1, 0, '#596275'),
(761, 'test55@mail.com', 'IH0Ut6O8wFus0cBLe6Ah7og3wVBVRumN4VsRWBwhyyIS8+cXx48FouyNmN4hnHs/Wnk6M4q0/xkyjmOH6mWrfQ==', 't9YiGA+eR1A54PcHQ4aV1BKwESHQoGpFuUIINegMoVswnKeIAdkMym88lH7XkTUSxFJ+NoAZG14tx1Jr/M/7rw==', 'test', '55', 1, 0, '#7F8C8D'),
(762, 'test56@mail.com', 'RA0QqsKPicgm+AT/ssY3ddBcODQNKo+SCIUO8GkaUlvsRh5AZNfClj7mtMUkxfQGTxY//CAYJjOjLEKO1mbwrw==', '5QghLSfHs2zufSCxXW2rqGH1F41FVL00cTKBrdEa5cYUnPXOxw7sHag9szd4s7Svf17zsPMW4rxpc7dO63PyfQ==', 'test', '56', 1, 0, '#9B59B6'),
(763, 'test57@mail.com', 'ckk7SUcihVsVJlypMCIMWZLkpATk7xqdLgbmngvaKUugW48E8XGrHp9C40iJyZ+qOHdhZ2XbMJXSZsvkhbSLkw==', 'P4suJ0zc0KDxpF6LjsmU8w4BAzVuOeifUDQTgfDgGQ5IW/4fF9kxKK41BKvLkcxzUvaAS6zcsGDGtdWQTg4gsg==', 'test', '57', 1, 0, '#9B59B6'),
(764, 'test58@mail.com', 'Ec7yXZOPirSfRs6s8kO6kbTIeMOODXSqN/lKyQMXHWYN+937XxT5xPutinyyxSdRlifGar1jeVljwGfgIY6lyQ==', 'R6YwObOgF+M4fJ8gTMcFYC8iCHHF8LOvtXrsaSO53ivjx+iRpYp2lj0Mu6hmW+bBD6/GvO9SfIoNZni/y08OAw==', 'test', '58', 1, 0, '#596275'),
(765, 'test59@mail.com', 'EeHX5aejuJ3sdjMris4vgTwhr/Yq+KgTBJN2qtpom4MDVo/aZBtAX6MbsRAJzJL0pYXPCiHVwdX4I0Jefa24Aw==', '06ZHQVqypKyKJ+z9pgGzeiv6HP5h/xEfB8vsr1LUS9bN2kcArzotVuwe+tQXugHYPwxdmdr9akLfYj4EUMu3nQ==', 'test', '59', 1, 0, '#16A085'),
(766, 'test60@mail.com', 't7WkAIXsCTsWUHjwfkPjBsmhsZTwDwkS82aVWUKRRhVKz/taxAJ11Vy1zQPPaam9FrRmESwZQmT8DABhv2WzWQ==', '2QoR/Vxc97jRt2WeYtxHCodRRMTFbgXI/WPYtp4peBuaVa8rxNXx9iiEx+OL1FTC738L1i49dBgxQIEgr8r5iQ==', 'test', '60', 1, 0, '#16A085'),
(767, 'test61@mail.com', '6YNXBnL0zwhCrsBIYWIlt5lJoDoKqq2LbRONbNxHA6BwhbNi9F5XRVKORruwNJg80E2pWIx78Fmigq2lHPpSVQ==', 'Z+IHgK/SNP/9G4gNQin7DS1Hn3mRZ7K9hpDHHBIAM6ofgvTiEwcwCIYwLXF7uyvMC1z+j1dmAfAd7vBhQXT8iw==', 'test', '61', 1, 0, '#596275'),
(768, 'test62@mail.com', 'EO/9SrVLGTUD4kqbD2YC2fbHJ4UFty6Wzop06/G9rA1gU2qssGcW3rd/HhMmmowAthE59DsDWffRIZgFD1xemg==', 'B2gERLBgoYG0BFkbmsn4Ifdw/f+dJGmhwqlCMHzSLY2dRXED68la3IcBzuxvcMPfVaULbsizjG4oEx7iwdTFoQ==', 'test', '62', 1, 0, '#16A085'),
(769, 'test63@mail.com', 'YzU3bZxyRSpH5wU9FMz3XM89oZChfE4Cw4kr8IisweaKY51+X6qyPTYb5ZhBFAmVuXuZwHpGV+3T46f36kiPEA==', 'ABUO45b8XjCNnRhV27C4BDjTMdB5DbCaUrB1Tmpdv2cbRL5ZdAdz6qmWbaeKw5h3n8cX5iiwKRaiNSsQELLT4A==', 'test', '63', 1, 0, '#F17EA6'),
(770, 'test64@mail.com', '11kMsVM1WzfprQMdQhjgpbIj2cyO5CAnUqrxC0kuHVvlZDemq3yCUIHx5iLfyUeeBUZRNYbGwmWnooNbYrbJkQ==', 'u7TwnsVmSyqqMReEmhAKVc9GPDTTzVtLcG7pCoYayn17PnaCTnKdGtKxcWwIR3HhBbw2IRzKmJsR/WrGprMg+A==', 'test', '64', 1, 0, '#3498DB'),
(771, 'test65@mail.com', 'xzhzj3o+/ropBw+qv5RU8DLqDo8gBCu96rjK84JQJYaidYsiWcMtU1pdWBhvZz+niQVNLiVk4CBl7j6VBYGVNA==', 'BPL6ZG2KatDmkOyT7zfGWGG90odDQMJuaW75D8ObCA/R4VK9PS3DNtcS31D089/5Sx7rG7fZAaII4zQpaDEBpQ==', 'test', '65', 1, 0, '#F1C40F'),
(772, 'test66@mail.com', 'QxwQGYN2QtI8ma4r4oPtLtMOv0Uc/VPaXbxfzEcgpkdASBSPjaeBThtTrYosWIOqBpXVVwYppORDjkZ8EW+L/g==', 'RBhATomfsKONTCa9G8VdK99beRNwQNMFIP176ReZckYgiyRiRWUIGtCDcY0ww7bn7t2qOYnO+eCos1J5YKsX0Q==', 'test', '66', 1, 0, '#2ECC71'),
(773, 'test67@mail.com', 'SiQf/sVYs1S3SYhCX8GjhinDIdU9jSzcsX1EcMFfkm/c7UYtjeEnm8VJFwcrwNRXTAiU65rt1OTkiQJ+OjrkWA==', '+vRmViwHDSgizcOiL8AjThl+cwo3DhX79QqeQYGz95eB5mI802laeDSvWy5uUnU/nS2l4JipYobGCpWjqL4WEA==', 'test', '67', 1, 0, '#E67E22'),
(774, 'test68@mail.com', '9ZWG8bv8LHjfZpQmT2C5p1AtO725cJWgh7AXpYPeP0xhTRtgPw0j5eFvLdf2f2OajU0sxXaKpQn1Pzki/R9RyQ==', 'cDCmlQLBMwlxYqc5skzMCPRCdpPA9rRB5ayIyXEik2bC5wCS0RWmn76ZfJvXn0cgFdncoK+7eBxATMciIcBNjA==', 'test', '68', 1, 0, '#F17EA6'),
(775, 'test69@mail.com', '69JBmo4v01TzUElllAWGL1lYXYG3NZUQLk7BUXCKlkefu+pqMuULgup7A4D27E+04ET3n6WJGmTEnNhRPKHaQA==', 'NKLAYJZcYuwNbiFHGCZI2uYSGGzwFXhOIXiFvVQpHi2R9yyhxvsBbACwsPrA090Ci+664Ck1GNRil9L6Q0F9hA==', 'test', '69', 1, 0, '#3498DB'),
(776, 'test70@mail.com', 'tGDTUTN/DxzN3zx4DKCAf5vqQ2MImeo7P3LgR00dZClWcrvB7hpY76mMQLOWPxEwcyEyV0Q40XTgxzGWY8qMjA==', 'LzR4t9k2qV8UeTJOrCTmVKXoQDG1nl/0RnIpTn2mGMBJ9wxbTm6brycN8Xj2L3h4ysEuEhvPQ9a7U1QEpNcaWw==', 'test', '70', 1, 0, '#9B59B6'),
(777, 'test71@mail.com', 'Qu9mD6eoZoTDhPFEL8QPXieZLoqNBonksFsUFDsVwun2jyUCW7EhpOUlzYRe1M9RCcgkYQIhs32llvdOcmoA8Q==', 'yB9glc36lHwIQAQvB8FpfjjmZTb7E4O2Dc8ICXsry7O/pCUZsbeg4agvj2K4vgC3GjfY5LJJCfX1FmdmzWyvsA==', 'test', '71', 1, 0, '#E67E22'),
(778, 'test72@mail.com', 'adU2gHvSIgdTTupOgnuqcehsOtMrmY1y7QwVNP+4IgyLP/GEMbi5+c7sm6DgOJeG2bdcXVdKNo2ar4+Z4xtsmA==', 'Vah5laIEn+Vg03OZ7pH8EK3dTzUkOodsB7B22wbx+pAl5ILhXAvjQFUMKg6r3mSHlemzUKqpy8RMlq9P9M7ylA==', 'test', '72', 1, 0, '#E67E22'),
(779, 'test73@mail.com', 'mUTU1OpfvInqNXk1/KxDY+dckPiqtSxsBOLlYJSknQO6Vy92x1aYtL3Ecgy9eRpzQg9AzzR8h6MzK4hrc8JNgw==', '/m9aMEwQTUHZs1WNfJ7GB73QjmV87pNkzYd10KfQjNe0ebdYiqE+xpwwUmUvIfX4goJW8/GiZBB7FTwwuUChlA==', 'test', '73', 1, 0, '#596275'),
(780, 'test74@mail.com', 'oinln1SLpdjqedhBrX7NquRDiD25WqUVSvim6HUdWxoutRxHBQKwqM7VzTs6JclFwKDUTrIgXUQmKgcc4i/gUA==', 'RO7uRL19oypPiVdmJC6DyNZu/3FD2FKcc/YdmGF5NtIiKuCuUgABxqwhj60+/+yU/XejyHeadIHrGKyOaofa2w==', 'test', '74', 1, 0, '#9B59B6'),
(781, 'test75@mail.com', 's+tI8UEGlmYm0Nlv76LQqNQAeG3Mqr6LNd7EYq+STq8qiV838kdZ9DCv6e6TbPwHQ7EO4HkH/V9e5b7o0hva4A==', 'Z8E3swWSdD4hzntjogyz+Y/G2qJJxQIIEjFPD5II1CO7FeyK04IybFqfW6P0f7gEamOEHT2gLbZ2TGaTA0EJbg==', 'test', '75', 1, 0, '#F1C40F'),
(782, 'test76@mail.com', '+vmOM43KRFUEt9YdXzQHvhull6YRjYbDqCr2jzimB0Bx3LAfbglf/OYqt+bD4+6PBHPLaPJhtyt40qCKUw/LrQ==', '9Cd25XOnqFRxiNZv0PFq0gCc7jBXGwa5truoEZH0WZcC65D/POz4idqNVDRBa67XcLCu8+ZGCqOcjKGY4yshVQ==', 'test', '76', 1, 0, '#596275'),
(783, 'test77@mail.com', '+nqk6aBxgarjvSUg4Wb92hSnqNBYgKy1PEERYR7BfP8BdW3Na8/WrtH7t28cTn4ayZlVtjm5oZAO8wEQ0V4oTA==', 'bOxaoDts+MgRlbZAWmncpDngH16UzvzalPyfbSreq+/uPBfeU19+C76P8D7vg93Qxh6dgZS248/GsZuaGZKjcQ==', 'test', '77', 1, 0, '#2ECC71'),
(784, 'test78@mail.com', 'KeSzzeRS/bMVCVSJWzLzcc/VhwvE5bY62olNcc0KK6Ou6Q0Uef8tvSQ2lFsfWqSuhukNdT53eRQa2fqwF7+2fw==', 'uG9SVVkXPzmiKNw5Byk2lbT9acvjXmSfZmrPLjjix/GSNsG9I7xMQXuHEQWO/CR53Y05QjzdvcbP5Dy1wm1rMA==', 'test', '78', 1, 0, '#3498DB'),
(785, 'test79@mail.com', 'xoSh/1sf2P1FyhIvzStFUD96Vb4G3gRCzGXEN9AorX2CZ8Hq0AIA5eFInm98stnxl7NWcjlWvCU415jze9NXng==', 'R0rH725s0FTxEXmEhHDq57HhSNoeHZtpxZE8iM3x1qtQGraJoHGVMYOEmg5tjpKe+nlk2NF7os6IY7AmXWMscw==', 'test', '79', 1, 0, '#E67E22'),
(786, 'test80@mail.com', 'qufhZB8k7tdFitDIcxPN+74mHC+SSwjL01329UU9OuKNOyMYzxyd7XpUnCq3/cn6lK576UW/3IxrNUTBEEx4ZA==', 'M4EB+q9Bux9i1DIkv5s5IjxI30F1EPnIZg29RPuXV9H1xa6Ql4YwdMrSAw6L1PS8eDCQooStAmAaTs1nNLDsjg==', 'test', '80', 0, 0, '#2ECC71'),
(787, 'test81@mail.com', '0Az4KWOkZqjLxRAhjxcENUqPT7MAGzMAiwm4ldW/iWJvVPcLnbkXIBiThZafOnt6h4zONHiUvgXp4Th3g5sMvg==', 'gZDQdgPjKoytPNVpJ8Sg2sXC9CuDrJmfxLj4Eenedka+XNue6OxNbzukF8ymj4aeUjxOKit2o9iWHBhjxTRozQ==', 'test', '81', 0, 0, '#F17EA6'),
(788, 'test82@mail.com', 'U3TUgSKwEBffw5c1UPzivflW0FL7nWNz2clGsc3F+3vCOr8npoWdVuqzqdJid4hc01diDrDdKoMJCkmyrDcdQQ==', 'Puyou8Zk0BoAbB86ZpXTG6rRRcQZTQ4tupwxv04BTKc4MqzZYh5vui+xv1/c/ylKlty2l13tL1OSaaSPpExuFQ==', 'test', '82', 0, 0, '#16A085'),
(789, 'test83@mail.com', 'tw75EKcmUpjcrE1BaeXlhIfv62Xc4jzchCKKSyfx2RofiX+x5tEqhsL3tZvwOGxOnRNDucLP1Xx2I+OqZm0Q3w==', 'b6qlhWGenlZ+LRN0shk+81DDY0NEg6HFMGgt6weXxFraP3fvI/4yHlSRA9lkl94c0O1xvNB3ZccZGfgW6FQagw==', 'test', '83', 0, 0, '#596275'),
(790, 'test84@mail.com', 'TNppdG6CveR9VE1XbZMPwUDcTQeq72f0/xLy0/203QWANdJuoKomhvUjgnepVZiaGKAPwfWan3Mz7FIZ7TXaTg==', 'BDUS/hwbSfDNnrUQcNA4dYoumBr2eCWHlvZ08AgjVR0Cz38ZLF/AhvvjVzkEcNEn87ODZapvIypZ8pSlA5k38A==', 'test', '84', 0, 0, '#3498DB'),
(791, 'test85@mail.com', 'EgyYMrrqIid3cKISUhRhzKJisMMulqaQzQlRPMVGwyNgfYKI7kNOH8h/IUzIPVshVdHnfJbvF3Fsr5dAeFGiFg==', '1cNJd4jdSikkzMBxogSRB9BCBevXBPbFfmfAcl7CwghM/hgJP4rLumY35yuHJglAOTPKoOyj8bMZxw5h+ezOTQ==', 'test', '85', 0, 0, '#596275'),
(792, 'test86@mail.com', 'xie8lId7uvepcMadU0mtAZxFcEAUUYerfjmf8yFalmC84ZPnhIa5acIfAN7gnfqa62VrXboL4UayjswTrG/XtA==', '2EGky7nlaRL0jZvkZeH95m3PJidmOGnOmFLA/VHUesMhm/kwB4Z38GHwymAX/Z5TfD0PxJ+jMDjCTvK128wrFQ==', 'test', '86', 0, 0, '#7F8C8D'),
(793, 'test87@mail.com', 'nw1lgIH/BtR2g3NCmPhA3QyXQIHM2fE9tHXKbd2ScmjTjFcKiXuix3UVBk7cP7iTBAPF+5t0baJpKZBC8znTaw==', 'qWbcsRN9Ys+3bu2KoID3J03OnE7/3iS8DuedRzqTO7APamVs3FxqaJtGF0fBMc+5ibHWraEoQ9pHqDVGlvzmfw==', 'test', '87', 0, 0, '#E74C3C'),
(794, 'test88@mail.com', 'TwThCd8j6Ff3suOEPOU26V9rPPR4VwHbbGXeUgrG5kayW5kHuo+UcZs9CBC+4k3OXE5KCy0NWfHhEIa5nm2nXA==', 'bQONfDD8uive5EsOrc7c1ZGragEgRBtUlgNcDwnDEZNhQ5NlmC0fTAyzWEILwVisMtEVQVbho+XdqogH8IR1Kg==', 'test', '88', 0, 0, '#F17EA6'),
(795, 'test89@mail.com', 'hx2viiqBW/v8GxnwRuTG37IzUlhz2X0N3t+KY0Et3tKwyoGROwJSz0iPEJ0lmOg4Ok5EKSKKlIVCti9B6jL+2A==', 'OGpEqd8AMvANVPXwtR1bdKJYR9R+IvE9lLaAN0DI9xmxlKl1enjNZ5hnd0HKFeb31LO758J4aIolqPC6WjzfdQ==', 'test', '89', 0, 0, '#3498DB'),
(796, 'test90@mail.com', 'lqYE7Z2ckgPFRBvMnHyTwhr3BsI5JwrVoSk2POZ52YeYdBM9l6+nbv7JVpBCIXRIBp/fD2IhOAFqBT+D5oq7BA==', 'yNXHP94zUg4eljrV6mqtkeiJULonn6t1WrTvgD0mm0bdd9d/qepywda7/zSOzE5AvKC2mrsfkWFYtIDh3oDAcg==', 'test', '90', 0, 0, '#F1C40F'),
(797, 'test91@mail.com', 'ZV8sBqLl/gDxxKYcn3RFo383fjiJn0ZHCHZN5OEV3zeKESfc07bzm/lHZhyFjlXJ4pKHE7zwyvUUjV5rzYolKA==', 'MUqj/EgjMhY7qTsgZjX0Rd9KpXOKACer+XVSwxJOYMf7bBOwxYGt2b0uyRxOi/lRHccupt37ThZAWSRpp0Vl6Q==', 'test', '91', 0, 0, '#2ECC71'),
(798, 'test92@mail.com', 'cd9pJ8p87NFKDWIpDikKR3Y3Xatzrl9/ZDlfkdU33P3zY6b+AWB3HbVMJY7pQHAUIn04UWYrWY23e0TJJFL3wg==', '+vJOaJCHJjonfuU9w2iZ+HsuYWDKidGevtKkyA2NBPqzz7YPtomgB6gElkUDcb8BDk+/yfCt4kcpM7ulysqgkA==', 'test', '92', 0, 0, '#F17EA6'),
(799, 'test93@mail.com', 'bOOsA77SukHEsAdmGZJD2DZn7kCsKMFnXUn5e/2ppM9XrwykxFJqrv49nPwsPdwLyQNEI/G70PBGl2GTvBiEjw==', 'yCSxfb+SpHc42EsE1tTprJU7AcwbveRSn0Oo9qH6r/vMAnGIxmWzQw49ukbHC9o+py/prPmK3YbkYz7doerZTg==', 'test', '93', 0, 0, '#596275'),
(800, 'test94@mail.com', 'q88CrqHE5Jv/nGjR0bajx58qeKW3XZSEVJuWWGRpRQtBl4vuizUDyHItiWzRSOfnufVdQJE1C7sAjM0HfRnBFQ==', 'dL9lp2k2J+tDYmyWGtz4u6/2bCdNqihqRwzngoWlJ9wVI/w1H4jSPYofKIDxzJy9ekU7UKpUUZWR9j5RvdzA1g==', 'test', '94', 0, 0, '#F17EA6'),
(801, 'test95@mail.com', 'VE/B7FTsFzs8EmqYU65xOB7tl77tz0ulRG9ZWwAhgGBAftMuDi4ZfDYmQTuV9WrwpsuKEn47As2UXpIBq9NQpA==', '2lYLuPLKYgtBY5VBHNxN9mN5hbmMjcXptyFKy8v9bTNzrXvrPwgMvRS3Ie5xZhpw5JrSYRXMJCm4DUpPpqg1kg==', 'test', '95', 0, 0, '#F1C40F'),
(802, 'test96@mail.com', 'Um4eY2sI6axoyhEDfpBEgIQ3Ubj7OU2DBUNCH1mBk/5yomN0b/bLVEbGMldlpSJ6HeEl+XEox2aPlE6VUr2KDA==', 'Q6JQj7QlL7eOd7BnBM92PpKT5wL3Zb1+2q+BKgMnmMyHf09ETNCcEi9vPijclTjzpt6ZXLPJ52eMDDiv4CYYCQ==', 'test', '96', 0, 0, '#7F8C8D'),
(803, 'test97@mail.com', 'jcXj6Zsi4Gj/QcrP5aLKm+TXtAPQ+34j+CyMCTENQpuq5kX7P3x6QC0JDMUh2u3YQege7xMf7qNeyTJjp8q/5w==', 'PVixqn2/+NxDrpdo3Wuwv3984pu4lTdbyuTeEWlHhpTrmJifS8tCUOFBcvMky3xjq/yy+qqkTLGeVHH5d4Sd6Q==', 'test', '97', 0, 0, '#596275'),
(804, 'test98@mail.com', 'P4jnJNt5uC1CuOoSUdQd5JuZnDVMyy7WkIevgYdkdqzdZ/QfbXxYn3JteBkgAds5WPdemyQtj1qIFigyqRJnZA==', 'S9etNuSN0xfi9RDjwHP4YTjMFOGSwnhsA0GtmZd59JhUsXVmSyGm4TIjebz8dEUjIonxYkKRqEJuzZTzx3CL9Q==', 'test', '98', 0, 0, '#596275'),
(805, 'test99@mail.com', 'Hqqj1cFTVIj6DDnq43IOPxE+1mhfOZGHQH2299dNukWjQ75I/bD3zploHD+ZV4fERr5RO4AjajEmYMyIoCgVAg==', 'CfyygxefvN2arwopJYtpGLA2yVpmXF1GthkHZDinEiNO4Zp3Iu19j/s7oXo/fl+5mT+3t+11b9fj/QjbJYbhRw==', 'test', '99', 0, 0, '#E74C3C'),
(806, 'test100@mail.com', 'ktl8FpzKPEF8rMdHl6jHXDoZqNZygF1iQMHkkqrx0+xUW9IpzEE6EH1tvSVfR7QnourqHj4i+R07z4sQ6sJ34A==', 'dQZL6tV1+MQDt+/GAYBpnLiN5wFZkKmg+VBYltZr/PSZQce/BpxSpwVs5lk/Z43nvgW5N08ffDYgrjbjH8TwSw==', 'test', '100', 0, 0, '#E74C3C'),
(807, 'admin@mail.com', 'XWLa441j+ZRNVgDnNYyw3/RgY3VIxiKhwrBnRrWHexfAliAU3NO6XFUSUtALrqpa2E4kafMks+YUxc5LQUQr8g==', '3rS+D1QmRkYulyj201qEiYRgruC5VhITRtJGO/9b7EvgAdNOTwDuJwLQaABdGGvfhC45I6/OwnyGzp5LS/DlKg==', 'admin', 'admin', 1, 1, '#3498DB');

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
