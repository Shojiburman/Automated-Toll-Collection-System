-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 21, 2019 at 03:50 AM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_user=@@CHARACTER_SET_user */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `toll_sys`
--

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `fullname` varchar(30) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `dob` varchar(30) DEFAULT NULL,
  `vehiclenumber` varchar(20) DEFAULT NULL,
  `admin` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `password`, `fullname`, `phone`, `dob`, `vehiclenumber`, `admin`) VALUES
('ani', 'fgf', 'gfg', '01940010515', '18 September, 1996', NULL, 0),
('df', '1', 'dfdf', '01940010515', 'null null, null', NULL, 0),
('dfrrg', '44', 'fwrfrtrg', '01843633243', 'null null, null', NULL, 0),
('ds', '11', 'fgg', '01940010515', 'null null, null', NULL, 1),
('sdfsfgfg', '123456', 'fgfgfg', '01843633243', 'null null, null', NULL, 0),
('shojib', '1', 'shojibur Rahman', '01827056645', '30 June, 1998', NULL, 0),
('ty', 'yj', 'yj', '01940010515', 'null null, null', NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `toll`
--

CREATE TABLE `toll` (
  `place` varchar(50) NOT NULL,
  `price` int(20) NOT NULL,
  `id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `toll`
--

INSERT INTO `toll` (`place`, `price`, `id`) VALUES
('padma3', 450, 1),
('meghna2', 500, 4),
('high', 560, 10),
('test', 500, 11);

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `id` int(11) NOT NULL,
  `placeName` varchar(30) DEFAULT NULL,
  `amount` int(20) NOT NULL,
  `vehiclenumber` varchar(30) DEFAULT NULL,
  `date` varchar(30) NOT NULL,
  `username` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`id`, `placeName`, `amount`, `vehiclenumber`, `date`, `username`) VALUES
(1, 'padma3', 450, 'et', '2019/08/21 06:09:36', 'df'),
(2, 'test', 500, 'ertggg', '2019/08/21 06:09:36', 'df'),
(3, 'high', 560, 'khbkjnjklk', '2019/08/21 06:35:04', 'df'),
(4, 'meghna2', 500, 'ertggg', '2019/08/21 06:42:11', 'df');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle`
--

CREATE TABLE `vehicle` (
  `brandname` varchar(30) NOT NULL,
  `vehiclemodel` varchar(40) NOT NULL,
  `vehiclenumber` varchar(20) NOT NULL,
  `vehicleclass` varchar(40) NOT NULL,
  `vehicletype` varchar(30) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vehicle`
--

INSERT INTO `vehicle` (`brandname`, `vehiclemodel`, `vehiclenumber`, `vehicleclass`, `vehicletype`, `username`) VALUES
('bani', 'bo541', '1122', 'Private service', 'touring coach', 'ani'),
('audi', 'S5', '4789', 'Private service', 'touring coach', 'shojib'),
('bb', 'bb', 'bb', 'Private service', 'touring coach', 'ty'),
('fggfg', 'dfgg', 'dfgg', 'Public service', 'minibus', 'df'),
('sg', 'gg', 'dgh', 'Private service', 'minibus', 'ds');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `username_2` (`username`),
  ADD UNIQUE KEY `vehiclenumber` (`vehiclenumber`);

--
-- Indexes for table `toll`
--
ALTER TABLE `toll`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `vehicle`
--
ALTER TABLE `vehicle`
  ADD PRIMARY KEY (`vehiclenumber`),
  ADD UNIQUE KEY `vehiclenumber` (`vehiclenumber`,`username`),
  ADD KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `toll`
--
ALTER TABLE `toll`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`vehiclenumber`) REFERENCES `vehicle` (`vehiclenumber`);

--
-- Constraints for table `vehicle`
--
ALTER TABLE `vehicle`
  ADD CONSTRAINT `vehicle_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_user=@OLD_CHARACTER_SET_user */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
