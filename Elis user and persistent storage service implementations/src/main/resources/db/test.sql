-- phpMyAdmin SQL Dump
-- version 4.0.6deb1
-- http://www.phpmyadmin.net
--
-- Värd: localhost
-- Skapad: 13 mars 2014 kl 17:34
-- Serverversion: 5.5.35-0ubuntu0.13.10.2
-- PHP-version: 5.5.3-1ubuntu2.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Databas: `elis`
--
CREATE DATABASE IF NOT EXISTS `elis_test` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `elis_test`;

-- --------------------------------------------------------

--
-- Tabellstruktur `object_lookup_table`
--

CREATE TABLE IF NOT EXISTS `object_lookup_table` (
  `id` binary(16) NOT NULL,
  `stored_in` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellstruktur `se-mah-elis-impl-service-storage-test-mock-MockUser3`
--

CREATE TABLE IF NOT EXISTS `se-mah-elis-impl-service-storage-test-mock-MockUser3` (
  `uuid` binary(16) NOT NULL,
  `service_name` varchar(9) DEFAULT NULL,
  `id_number` int(11) DEFAULT NULL,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `id` int(11) DEFAULT NULL,
  `stuff` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellstruktur `se-mah-elis-services-users-PlatformUser`
--

CREATE TABLE IF NOT EXISTS `se-mah-elis-services-users-PlatformUser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `first_name` varchar(32) NOT NULL,
  `last_name` varchar(32) NOT NULL,
  `email` varchar(256) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Tabellstruktur `user_bindings`
--

CREATE TABLE IF NOT EXISTS `user_bindings` (
  `platform_user` int(11) NOT NULL,
  `user` binary(16) NOT NULL,
  UNIQUE KEY `platform_user` (`platform_user`,`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
