-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Φιλοξενητής: 127.0.0.1
-- Χρόνος δημιουργίας: 17 Δεκ 2015 στις 10:27:34
-- Έκδοση διακομιστή: 5.5.32
-- Έκδοση PHP: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Βάση: `ptuxiakh`
--

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `papers`
--

CREATE TABLE IF NOT EXISTS `papers` (
  `year` int(10) NOT NULL,
  `title` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `authors` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `ISSN` bigint(20) NOT NULL,
  `journal` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `volume` int(11) NOT NULL,
  `before` int(11) NOT NULL,
  `1998` int(11) NOT NULL,
  `1999` int(11) NOT NULL,
  `2000` int(11) NOT NULL,
  `2001` int(11) NOT NULL,
  `2002` int(11) NOT NULL,
  `2003` int(11) NOT NULL,
  `2004` int(11) NOT NULL,
  `2005` int(11) NOT NULL,
  `2006` int(11) NOT NULL,
  `2007` int(11) NOT NULL,
  `2008` int(11) NOT NULL,
  `2009` int(11) NOT NULL,
  `2010` int(11) NOT NULL,
  `2011` int(11) NOT NULL,
  `2012` int(11) NOT NULL,
  `2013` int(11) NOT NULL,
  `after` int(11) NOT NULL,
  FULLTEXT KEY `authors` (`authors`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci MAX_ROWS=4000;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
