-- phpMyAdmin SQL Dump
-- version 4.0.6deb1
-- http://www.phpmyadmin.net
--
-- Värd: localhost
-- Skapad: 19 mars 2014 kl 17:46
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
CREATE DATABASE IF NOT EXISTS `elis` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `elis`;

-- --------------------------------------------------------

--
-- Tabellstruktur `collections`
--

CREATE TABLE IF NOT EXISTS `collections` (
  `collecting_object` binary(16) NOT NULL,
  `collected_object` binary(16) NOT NULL,
  `collection_name` varchar(256) NOT NULL,
  PRIMARY KEY (`collecting_object`),
  KEY `collected_object` (`collected_object`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellstruktur `object_lookup_table`
--

CREATE TABLE IF NOT EXISTS `object_lookup_table` (
  `id` binary(16) NOT NULL,
  `stored_in` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellstruktur `se-mah-elis-services-users-PlatformUser`
--

CREATE TABLE IF NOT EXISTS `se-mah-elis-services-users-PlatformUser` (
  `uuid` binary(16) NOT NULL,
  `username` varchar(128) NOT NULL,
  `password` varchar(70) NOT NULL,
  `first_name` varchar(32) NOT NULL,
  `last_name` varchar(32) NOT NULL,
  `email` varchar(256) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellstruktur `user_bindings`
--

CREATE TABLE IF NOT EXISTS `user_bindings` (
  `platform_user` binary(16) NOT NULL,
  `user` binary(16) NOT NULL,
  UNIQUE KEY `platform_user` (`platform_user`,`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
