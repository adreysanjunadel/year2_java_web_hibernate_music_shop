-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               8.0.36 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for golden_octaves_db
CREATE DATABASE IF NOT EXISTS `golden_octaves_db` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `golden_octaves_db`;

-- Dumping structure for table golden_octaves_db.account_status
CREATE TABLE IF NOT EXISTS `account_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.account_status: ~2 rows (approximately)
INSERT IGNORE INTO `account_status` (`id`, `name`) VALUES
	(1, 'Active'),
	(2, 'Inactive\r\n');

-- Dumping structure for table golden_octaves_db.address
CREATE TABLE IF NOT EXISTS `address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `line1` text NOT NULL,
  `line2` text NOT NULL,
  `postal_code` varchar(10) NOT NULL,
  `city_id` int NOT NULL,
  `mobile` varchar(15) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_address_city1_idx` (`city_id`),
  KEY `fk_address_user1_idx` (`user_id`),
  CONSTRAINT `fk_address_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_user_address_city1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.address: ~0 rows (approximately)
INSERT IGNORE INTO `address` (`id`, `first_name`, `last_name`, `line1`, `line2`, `postal_code`, `city_id`, `mobile`, `user_id`) VALUES
	(1, 'Adrien', 'Sanjuna', '8 North', 'Flyaway Street Rajagiriya', '10100', 1, '0771234567', 1);

-- Dumping structure for table golden_octaves_db.admin
CREATE TABLE IF NOT EXISTS `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  `verification` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.admin: ~0 rows (approximately)

-- Dumping structure for table golden_octaves_db.brand
CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.brand: ~27 rows (approximately)
INSERT IGNORE INTO `brand` (`id`, `name`) VALUES
	(1, 'Yamaha'),
	(2, 'Stentor'),
	(3, 'Eastman'),
	(4, 'Fender'),
	(5, 'Gibson'),
	(6, 'Cervini '),
	(7, 'Pernambuco '),
	(8, 'Hammond'),
	(9, 'Kawai'),
	(10, 'Korg'),
	(11, 'Pearl'),
	(12, 'Vic Firth'),
	(13, 'Remo'),
	(14, 'Latin Percussion'),
	(15, 'Bose'),
	(16, 'JBL'),
	(17, 'Shure '),
	(18, 'Sennheiser '),
	(19, 'AKG '),
	(20, 'Neumann '),
	(21, 'Allen & Heath'),
	(22, 'Behringer '),
	(23, 'Novation '),
	(24, 'Akai Professional'),
	(25, 'Native Instruments'),
	(26, 'Roland '),
	(27, 'Moog '),
	(28, 'Buffet Crampon'),
	(29, 'Jupiter '),
	(30, 'Bach (Vincent Bach)');

-- Dumping structure for table golden_octaves_db.cart
CREATE TABLE IF NOT EXISTS `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `qty` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_recent_product1_idx` (`product_id`),
  KEY `fk_cart_user1_idx` (`user_id`),
  CONSTRAINT `fk_cart_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_recent_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.cart: ~1 rows (approximately)

-- Dumping structure for table golden_octaves_db.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.category: ~7 rows (approximately)
INSERT IGNORE INTO `category` (`id`, `name`) VALUES
	(1, 'String Instruments'),
	(2, 'Keyboard'),
	(3, 'Percussion'),
	(4, 'Wind Instruments'),
	(5, 'Speakers'),
	(6, 'Microphones'),
	(7, 'Electronic Equipment');

-- Dumping structure for table golden_octaves_db.city
CREATE TABLE IF NOT EXISTS `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.city: ~4 rows (approximately)
INSERT IGNORE INTO `city` (`id`, `name`) VALUES
	(1, 'Colombo'),
	(2, 'Kandy'),
	(3, 'Nuwara Eliya'),
	(4, 'Galle'),
	(5, 'Matara');

-- Dumping structure for table golden_octaves_db.colour
CREATE TABLE IF NOT EXISTS `colour` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.colour: ~6 rows (approximately)
INSERT IGNORE INTO `colour` (`id`, `name`) VALUES
	(1, 'Black'),
	(2, 'White'),
	(3, 'Marble'),
	(4, 'Brown'),
	(5, 'Wood'),
	(6, 'Brass'),
	(7, 'Silver'),
	(8, 'Red');

-- Dumping structure for table golden_octaves_db.delivery_status
CREATE TABLE IF NOT EXISTS `delivery_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.delivery_status: ~4 rows (approximately)
INSERT IGNORE INTO `delivery_status` (`id`, `name`) VALUES
	(1, 'Paid'),
	(2, 'Processing'),
	(3, 'Delivered'),
	(4, 'Shipped'),
	(5, 'Order Placed');

-- Dumping structure for table golden_octaves_db.description
CREATE TABLE IF NOT EXISTS `description` (
  `id` int NOT NULL AUTO_INCREMENT,
  `short_description` text NOT NULL,
  `warranty` varchar(45) NOT NULL,
  `origin_country_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_description_origin_country1_idx` (`origin_country_id`),
  CONSTRAINT `fk_description_origin_country1` FOREIGN KEY (`origin_country_id`) REFERENCES `origin_country` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.description: ~39 rows (approximately)
INSERT IGNORE INTO `description` (`id`, `short_description`, `warranty`, `origin_country_id`) VALUES
	(1, 'The Roland Jupiter-X synthesizer offers a blend of vintage and modern sounds, drawing inspiration from legendary Roland instruments like the Jupiter-8, Juno-106, and SH-101. It features a 61-note keyboard with velocity and channel aftertouch, intuitive controls, and deep sound design capabilities. The Jupiter-X also includes ZEN-Core Synthesis System technology, which allows for expansive polyphony, rich analog sounds, and detailed layering options, making it suitable for live performances and studio production.', '2yr', 4),
	(2, 'The Yamaha V3SK is a student violin designed for beginners and intermediate players. It typically features a solid spruce top, solid maple back and sides, and an ebony fingerboard. It comes with a lightweight case and bow, making it a suitable choice for young learners and beginners.', '2yr', 3),
	(3, 'Brand new Bach TB20 Trombone with a rich, warm tone and superior craftsmanship.', '2yr', 5),
	(4, 'The Bach Stradivarius 42BO Trombone is a professional-grade instrument renowned for its rich, warm tone and exceptional craftsmanship. Featuring a dual bore design and an open wrap F attachment, this trombone provides superior playability and a smooth, resonant sound. Ideal for advanced players and professional musicians, it is built to deliver consistent performance across a wide range of musical genres.', '2yr', 5),
	(5, 'The L1Â® Compact system combines conventional PA and monitors into one sleek unit. Positioned behind or to the side of a performer, DJ or presenter, it delivers wide, even sound coverage throughout the stage area and audienceâeven off to the extreme sides. It\'s our smallest, lightest, easiest-to-set-up L1Â® system, weighing just 13.15 kg.', '2Yr', 2),
	(6, 'The Sennheiser MKH 416 is a compact pressure-gradient microphone with a short interference tube, providing excellent directivity and clarity. It is widely used in professional audio environments for its ability to isolate sound in challenging conditions.', '2yr', 2),
	(7, 'The Behringer X-Air XR18 is an 18-input digital mixer that combines a powerful, compact form factor with versatile wireless control. It features 16 studio-grade Midas-designed mic preamps, 6 aux sends, and built-in Wi-Fi, allowing for remote operation using your tablet or smartphone. Ideal for live sound, it provides high-quality sound processing and a range of digital effects to enhance performances.', '2yr', 3),
	(8, 'The Yamaha V5SC is a high-quality acoustic violin designed for young students.   It features a spruce top and maple back and neck made from select materials. Each instrument is handcrafted utilizing the same traditional methods as used on high-end violins. The V5SC offers a Wittner "Ultra" tailpiece with 4 fine tuners. It comes with a case, bow, and rosin and is available in 1/16, 1/10, 1/8, 1/4, 1/2, 3/4, and 4/4 sizes.', '2 years', 3),
	(9, 'The Stentor Student II Violin is a popular choice for beginner and intermediate violinists due to its affordability and good quality. It\'s designed to provide a solid foundation for learning the violin while offering a decent sound and playability.', '2 years', 4),
	(10, 'The Stentor Conservatoire Violin is a higher-end model from Stentor Violins, designed to offer a professional-grade sound and playability. It\'s a popular choice for advanced students and professional musicians who demand a high-quality instrument.', '2 years', 4),
	(11, 'Quality Construction: Stentor Conservatoire Violins are typically made from high-quality materials, ensuring durability and a great sound. Rich Tone: Known for their warm and resonant tone, these violins are suitable for various playing styles. Student-Friendly: Often considered a good choice for students due to their balance of quality and affordability.', '2yr', 3),
	(12, 'The Fender Stratocaster is one of the most iconic electric guitars in history, renowned for its versatility, playability, and distinctive sound. It has been used by countless musicians across various genres, from rock and blues to country and pop.', '2 years', 5),
	(13, 'The Gibson Les Paul Standard is another iconic electric guitar, renowned for its powerful, rich tone and classic design. It has been played by countless rock and blues legends, including Jimmy Page, Eric Clapton, and Slash.', '2 years', 5),
	(14, 'The Stentor Conservatoire Viola is a high-quality viola designed for advanced students and professional musicians. It offers a rich, powerful sound and excellent playability, making it a popular choice among violists seeking a top-tier instrument.', '2 years', 4),
	(15, 'The Eastman VA200 Viola is another popular choice among intermediate and advanced violists due to its excellent sound quality and affordability. It\'s known for its rich, full-bodied tone and comfortable playability.', '2 years', 3),
	(16, 'The Eastman DB505 is a popular choice among double bass players due to its excellent sound quality, affordability, and reliability. It\'s designed to offer a professional-grade sound without breaking the bank.', '2 years', 3),
	(17, 'The Hammond SK1-73 is a portable, digital organ keyboard that emulates the classic sound of a Hammond B-3 organ.   It\'s a popular choice for musicians who want the iconic tone of a traditional Hammond organ in a more compact and portable package.', '2 years', 5),
	(18, 'The Hammond SK2 is another portable, digital organ keyboard that emulates the classic sound of a Hammond B-3 organ. It offers many of the same features as the SK1 but with some additional enhancements.', '2 years', 5),
	(19, 'The Kawai CA79 is a high-quality digital piano that offers a realistic piano playing experience and a wide range of features. It\'s a popular choice among pianists who are looking for a digital piano that can closely replicate the sound and feel of a grand piano.', '2 years', 7),
	(20, 'The Korg CX3 is a digital piano that offers a good balance of affordability and features. It\'s a popular choice among beginner and intermediate pianists who are looking for a quality instrument without breaking the bank.', '2 years', 3),
	(21, 'The Korg B1 is a digital piano that offers a compact and affordable option for beginners and intermediate pianists. It\'s designed to provide a realistic piano playing experience in a small footprint.', '2 years', 3),
	(22, 'The Pearl Reference Series drumset is a high-end professional drumset known for its exceptional quality, sound, and versatility. It\'s a popular choice among drummers who demand the best in terms of performance and durability.', '2 years', 7),
	(23, 'The Remo Powerstroke Drum is a high-end professional drumset known for its exceptional quality, sound, and versatility. It\'s a popular choice among drummers who demand the best in terms of performance and durability.', '2 years', 3),
	(24, 'The Latin Percussion LP861 Snare Drum is a classic 14"x5" snare drum that offers a versatile and powerful sound. It\'s a popular choice among drummers of all levels, from beginners to professionals.', '2 years', 5),
	(25, 'The Latin Percussion LP861 Snare Drum is a classic 14"x5" snare drum that offers a versatile and powerful sound. It\'s a popular choice among drummers of all levels, from beginners to professionals.', '2 years', 5),
	(26, 'The Latin Percussion LP1025 Tambourine is a classic tambourine that offers a versatile and percussive sound. It\'s a popular choice among musicians of all levels, from beginners to professionals.', '2 years', 5),
	(27, 'The Vic Firth 7A drum sticks are a classic choice for drummers of all levels. They offer a balanced feel and a versatile sound that is suitable for a wide range of musical styles.', '-', 5),
	(28, 'The Vic Firth American Classic 5A Jazz drum sticks are a popular choice for drummers who prefer a thinner stick with a longer taper. They are made of hickory wood and have a tear-drop tip, which provides a good balance of power and control.', '-', 5),
	(29, 'The Bose Portable Smart Speaker is a versatile and powerful Bluetooth speaker that can also function as a smart speaker when connected to Wi-Fi. It offers a spacious 360-degree sound, powerful bass, and a durable, water-resistant design.', '1 year', 5),
	(30, 'The Bose L1 Compact is a portable PA system designed for musicians, DJs, and other performers. It\'s known for its unique vertical design, which provides a wide, even sound dispersion.', '1 year', 5),
	(31, 'The Bose L1 Model 812 is a powerful and versatile portable PA system designed for musicians, DJs, and other performers. It offers a wide, even sound dispersion, powerful bass, and a compact, portable design.', '1 year', 5),
	(32, 'The JBL Charge 5 is a powerful and versatile portable Bluetooth speaker that offers a rich, full-bodied sound with deep bass. It\'s designed to be durable and weather-resistant, making it perfect for outdoor use.', '1 year', 3),
	(33, 'The JBL PartyBox 310 is a powerful and portable party speaker designed to deliver a high-energy sound experience. It\'s equipped with two 7.6-inch woofers and two tweeters to deliver a powerful, immersive sound.', '1 year', 3),
	(34, 'The Shure VP88 Dynamic Vocal Microphone is a popular choice for live performance and recording due to its rugged construction, clear sound, and affordability. It\'s designed to provide a warm, natural sound with excellent feedback rejection.', '6 months', 3),
	(35, 'The Shure SM58 Dynamic Vocal Microphone is one of the most iconic microphones in the world, renowned for its rugged construction, clear sound, and versatility. It\'s used by countless musicians, singers, and podcasters around the globe.', '6 months', 3),
	(36, 'The Sennheiser MKE 418 S is a compact, directional microphone designed for use with camcorders and DSLR cameras. It offers a wide frequency response and a cardioid pickup pattern, making it ideal for capturing clear audio in a variety of situations.', '6 months', 3),
	(37, 'The Sennheiser MKE 416 is a compact, directional microphone designed for use with camcorders and DSLR cameras. It offers a wide frequency response and a cardioid pickup pattern, making it ideal for capturing clear audio in a variety of situations.', '1 year', 3),
	(38, 'The AKG C414 XLII is a large-diaphragm condenser microphone known for its exceptional audio quality and versatility. It\'s a popular choice among studio engineers, musicians, and podcasters who demand the best in terms of sound and performance.', '6 months', 3),
	(39, 'The JBL EON610 is a powerful and versatile portable PA speaker designed for live sound reinforcement. It\'s equipped with a 15-inch woofer and a 1-inch tweeter, delivering a powerful, immersive sound.', '1 year', 3),
	(40, 'The Jupiter JTS700 Tenor Saxophone is a quality instrument designed for beginner to intermediate players, offering features suitable for both learning and more advanced techniques. It is built with a lacquered brass body and keys, providing a robust and attractive design, and includes advanced features such as a high F# key, adjustable palm keys, and a contoured left-hand table for easier playing.', '2yr', 8);

-- Dumping structure for table golden_octaves_db.invoice
CREATE TABLE IF NOT EXISTS `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `date_time` datetime NOT NULL,
  `address_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_user1_idx` (`user_id`),
  KEY `fk_invoice_address1_idx` (`address_id`),
  CONSTRAINT `fk_invoice_address1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `fk_invoice_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.invoice: ~6 rows (approximately)
INSERT IGNORE INTO `invoice` (`id`, `user_id`, `date_time`, `address_id`) VALUES
	(18, 1, '2024-11-08 12:01:25', 1),
	(19, 1, '2024-11-08 12:02:38', 1),
	(20, 1, '2024-11-08 12:02:45', 1),
	(21, 1, '2024-11-08 12:04:17', 1),
	(22, 1, '2024-11-08 12:04:37', 1),
	(23, 1, '2024-11-08 12:05:49', 1);

-- Dumping structure for table golden_octaves_db.invoice_item
CREATE TABLE IF NOT EXISTS `invoice_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_id` int NOT NULL,
  `product_id` int NOT NULL,
  `qty` int NOT NULL,
  `delivery_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_has_product_product1_idx` (`product_id`),
  KEY `fk_invoice_has_product_invoice1_idx` (`invoice_id`),
  KEY `fk_invoice_has_product_delivery_status1_idx` (`delivery_status_id`),
  CONSTRAINT `fk_invoice_has_product_delivery_status1` FOREIGN KEY (`delivery_status_id`) REFERENCES `delivery_status` (`id`),
  CONSTRAINT `fk_invoice_has_product_invoice1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `fk_invoice_has_product_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.invoice_item: ~4 rows (approximately)
INSERT IGNORE INTO `invoice_item` (`id`, `invoice_id`, `product_id`, `qty`, `delivery_status_id`) VALUES
	(18, 18, 37, 1, 5),
	(19, 19, 37, 1, 5),
	(20, 21, 39, 1, 5),
	(21, 22, 37, 1, 5),
	(22, 23, 39, 1, 5);

-- Dumping structure for table golden_octaves_db.model
CREATE TABLE IF NOT EXISTS `model` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `brand_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_model_brand1_idx` (`brand_id`),
  CONSTRAINT `fk_model_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.model: ~59 rows (approximately)
INSERT IGNORE INTO `model` (`id`, `name`, `brand_id`) VALUES
	(1, 'V3SK Violin', 1),
	(3, 'V5SC Violin', 1),
	(6, 'Student II Violin', 2),
	(7, 'Conservatoire Violin', 2),
	(9, 'VL200 Violin', 3),
	(11, 'Stratocaster Guitar', 4),
	(13, 'Les Paul Standard Guitar', 5),
	(14, 'SG Standard', 5),
	(17, 'Conservatoire Viola', 2),
	(19, 'VA200 Viola', 3),
	(22, 'Conservatoire Cello', 2),
	(24, 'VC505 Cello', 3),
	(28, 'DB505 Double Bass', 3),
	(31, 'Cervini VC-300 Violin Cello', 6),
	(34, 'Pernambuco DB-300', 7),
	(35, 'Hammond SK1-73 Piano', 8),
	(36, 'Hammond SK2 Piano', 8),
	(39, 'Kawai CA79 Piano', 9),
	(42, 'Yamaha CFX Piano', 1),
	(43, 'Korg B1 Organ', 10),
	(45, 'Korg CX-3', 10),
	(48, 'Pearl Reference Series Drum Set', 11),
	(50, 'Remo Powerstroke Drum Set', 13),
	(53, 'Latin Percussion LP 792 Bass Drum', 14),
	(54, 'Latin Percussion LP 861 Snare Drum', 14),
	(56, 'Latin Percussion LP 1205 Tambourine', 14),
	(60, 'Vic Firth 7A Drum Sticks', 12),
	(63, 'Vic Firth American Classic® 5A Jazz Drum Sticks', 12),
	(65, 'Bose Portable Smart Speaker (Handheld/Portable)', 15),
	(66, 'Bose L1 Compact (Medium Speaker)', 15),
	(68, 'Bose F1 Model 812 (Large Party Speaker)', 15),
	(69, 'JBL Charge 5 (Handheld/Portable)', 16),
	(70, 'JBL EON610 (Medium Speaker)', 16),
	(71, 'JBL PartyBox 310 (Portable Party Speaker)', 16),
	(73, 'Shure VP88 (Stereo Mic)', 17),
	(75, 'Shure SM58 Wireless System (Wireless Mic)', 17),
	(77, 'Sennheiser MKH 418-S (Stereo Mic)', 18),
	(78, 'Sennheiser MKH 416 (Condenser Mic)', 18),
	(81, 'AKG C414 XLII (Stereo Mic)', 19),
	(82, 'AKG C214 (Condenser Mic)', 19),
	(85, 'Neumann KU 100 (Stereo Mic)', 20),
	(89, 'Allen & Heath SQ-5 - Digital Mixer', 21),
	(92, 'Behringer X-Air XR18 - Digital Mixer', 22),
	(93, 'Novation Launchpad Pro MK3 - MIDI Controller', 23),
	(95, 'Akai MPK Mini MK3 - MIDI Controller', 24),
	(96, 'Akai MPC Live II - Drum Machine and Sampler', 24),
	(98, 'Native Instruments Komplete Kontrol S61 MK2 - MIDI Controller', 25),
	(99, 'Roland Jupiter-X - Synthesizer', 26),
	(102, 'Moog Grandmother - Analog Synthesizer', 27),
	(107, 'Jupiter JCL-1100 Clarinet', 29),
	(108, 'Jupiter JAS-769 Alto Saxophone', 29),
	(109, 'Jupiter JFL-511 Flute', 29),
	(110, 'Jupiter JTB700 Tenor Saxophone', 29),
	(112, 'Jupiter JTB700 Tenor Trombone', 29),
	(113, 'Jupiter JFH1100 Flugelhorn', 29),
	(114, 'Jupiter JTP700 Piccolo Trumpet', 29),
	(116, 'Bach TR300H Trumpet', 30),
	(117, 'Bach Stradivarius 42BO Trombone', 30),
	(118, 'Bach TB20 Trombone', 30);

-- Dumping structure for table golden_octaves_db.origin_country
CREATE TABLE IF NOT EXISTS `origin_country` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.origin_country: ~6 rows (approximately)
INSERT IGNORE INTO `origin_country` (`id`, `name`) VALUES
	(1, 'Australia'),
	(2, 'Germany'),
	(3, 'China'),
	(4, 'UK'),
	(5, 'USA'),
	(6, 'India'),
	(7, 'Japan'),
	(8, 'Taiwan');

-- Dumping structure for table golden_octaves_db.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `model_id` int NOT NULL,
  `title` text NOT NULL,
  `description_id` int NOT NULL,
  `price` double NOT NULL,
  `qty` int NOT NULL,
  `product_condition_id` int NOT NULL,
  `colour_id` int NOT NULL,
  `datetime_updated` datetime NOT NULL,
  `product_status_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_description1_idx` (`description_id`),
  KEY `fk_product_colour1_idx` (`colour_id`),
  KEY `fk_product_category1_idx` (`category_id`),
  KEY `fk_product_model1_idx` (`model_id`),
  KEY `fk_product_product_condition1_idx` (`product_condition_id`),
  KEY `fk_product_product_status1_idx` (`product_status_id`),
  KEY `fk_product_user1_idx` (`user_id`),
  CONSTRAINT `fk_product_category1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_product_colour1` FOREIGN KEY (`colour_id`) REFERENCES `colour` (`id`),
  CONSTRAINT `fk_product_description1` FOREIGN KEY (`description_id`) REFERENCES `description` (`id`),
  CONSTRAINT `fk_product_model1` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`),
  CONSTRAINT `fk_product_product_condition1` FOREIGN KEY (`product_condition_id`) REFERENCES `product_condition` (`id`),
  CONSTRAINT `fk_product_product_status1` FOREIGN KEY (`product_status_id`) REFERENCES `product_status` (`id`),
  CONSTRAINT `fk_product_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.product: ~39 rows (approximately)
INSERT IGNORE INTO `product` (`id`, `category_id`, `model_id`, `title`, `description_id`, `price`, `qty`, `product_condition_id`, `colour_id`, `datetime_updated`, `product_status_id`, `user_id`) VALUES
	(1, 7, 99, 'Roland Jupiter-X - Synthesizer', 1, 7500, 99, 1, 1, '2024-09-13 21:07:22', 1, 1),
	(2, 1, 1, 'Yamaha V3SK Violin Brand New', 2, 600, 98, 1, 5, '2024-09-13 15:50:35', 1, 1),
	(3, 4, 118, 'Bach TB20 Trombone', 3, 850, 98, 1, 6, '2024-09-13 16:07:45', 1, 1),
	(4, 4, 117, 'Bach Stradivarius 42BO Trombone', 4, 4500, 98, 1, 6, '2024-09-13 16:13:03', 1, 1),
	(5, 5, 66, 'Bose L1 Compact speaker', 5, 800, 99, 1, 1, '2024-09-13 20:46:33', 1, 5),
	(6, 6, 78, 'Sennheiser MKH 416 (Condenser Mic) Brand New', 6, 3000, 99, 1, 1, '2024-09-13 20:57:54', 1, 1),
	(7, 7, 92, 'Behringer X-Air XR18 - Digital Mixer', 7, 1300, 98, 1, 1, '2024-09-13 22:52:41', 1, 1),
	(8, 1, 3, 'Yamaha V5SC Violin', 8, 850, 98, 1, 5, '2024-09-13 23:11:23', 1, 6),
	(9, 1, 6, 'Stentor Student II Violin', 9, 550, 100, 1, 5, '2024-09-13 23:23:05', 1, 6),
	(10, 1, 7, 'Stentor Conservatoire Violin', 10, 2000, 97, 1, 5, '2024-09-13 23:29:26', 1, 6),
	(11, 1, 7, 'Stentor Conservatoire Violin Brand New', 11, 800, 100, 1, 5, '2024-09-13 23:38:33', 1, 1),
	(12, 1, 11, 'Fender Stratocaster Guitar', 12, 10000, 99, 1, 1, '2024-09-14 00:06:02', 1, 6),
	(13, 1, 13, 'Gibson Les Paul Standard Guitar', 13, 9000, 100, 1, 4, '2024-09-14 00:12:37', 1, 6),
	(14, 1, 17, 'Stentor Conservatoire Viola', 14, 3000, 98, 1, 5, '2024-09-14 00:18:13', 1, 6),
	(15, 1, 19, 'Eastman VA200 Viola', 15, 1500, 100, 1, 5, '2024-09-14 00:24:06', 1, 6),
	(16, 1, 28, 'Eastman DB505 Double Bass', 16, 4000, 99, 1, 5, '2024-09-14 00:33:47', 1, 6),
	(17, 2, 35, 'Hammond SK1-73 P', 17, 3000, 98, 1, 1, '2024-09-14 00:39:55', 1, 6),
	(18, 2, 36, 'Hammond SK2 ', 18, 4000, 100, 1, 1, '2024-09-14 00:43:49', 1, 6),
	(19, 2, 39, 'Kawai CA79', 19, 4000, 99, 1, 1, '2024-09-14 00:47:44', 1, 6),
	(20, 2, 45, 'Korg CX3', 20, 2000, 100, 1, 4, '2024-09-14 00:52:12', 1, 6),
	(21, 2, 43, 'Korg B1', 21, 1500, 100, 1, 1, '2024-09-14 00:56:57', 1, 6),
	(22, 3, 48, 'Pearl Reference Series drumset', 22, 10000, 100, 1, 5, '2024-09-14 01:02:55', 1, 6),
	(23, 3, 50, 'Remo Powerstroke Drum', 23, 10000, 100, 1, 2, '2024-09-14 01:36:26', 1, 6),
	(24, 3, 54, 'Latin Percussion LP 861 Snare Drum', 24, 700, 100, 1, 5, '2024-09-14 01:43:23', 1, 6),
	(25, 3, 54, 'Latin Percussion LP 861 Snare Drum', 25, 700, 99, 1, 5, '2024-09-14 01:43:23', 1, 6),
	(26, 3, 56, 'Latin Percussion LP1025 Tambourine', 26, 150, 100, 1, 1, '2024-09-14 01:46:59', 1, 6),
	(27, 3, 60, 'Vic Firth 7A Drum Sticks', 27, 80, 95, 1, 5, '2024-09-14 01:53:27', 1, 6),
	(28, 3, 63, 'Vic Firth American Classic 5A Jazz drum sticks', 28, 90, 99, 1, 5, '2024-09-14 01:57:00', 1, 6),
	(29, 5, 65, ' Bose Portable Smart Speaker', 29, 500, 98, 1, 2, '2024-09-14 02:04:46', 1, 6),
	(30, 5, 66, 'Bose L1 Compact', 30, 5000, 99, 1, 1, '2024-09-14 02:08:40', 1, 6),
	(31, 5, 68, 'Bose L1 Model 812 ', 31, 4000, 100, 1, 1, '2024-09-14 02:12:55', 1, 6),
	(32, 5, 69, 'JBL Charge 5', 32, 400, 92, 1, 8, '2024-09-14 02:19:49', 1, 6),
	(33, 5, 71, 'JBL PartyBox 310 (Portable Party Speaker)', 33, 1700, 89, 1, 1, '2024-09-14 02:23:43', 1, 6),
	(34, 6, 73, 'Shure VP88 Dynamic Vocal Microphone', 34, 200, 98, 1, 1, '2024-09-14 02:30:36', 1, 6),
	(35, 6, 75, 'Shure SM58', 35, 250, 95, 1, 1, '2024-09-14 02:34:59', 1, 6),
	(36, 6, 77, 'Sennheiser MKH 418-S', 36, 250, 96, 1, 1, '2024-09-14 02:40:12', 1, 6),
	(37, 6, 78, 'Sennheiser MKH 416', 37, 250, 96, 1, 1, '2024-09-14 02:42:20', 1, 6),
	(38, 6, 81, 'AKG C414 XLII', 38, 3000, 85, 1, 1, '2024-09-14 02:45:50', 1, 6),
	(39, 5, 70, 'JBL EON610', 39, 1500, 65, 1, 1, '2024-09-14 02:53:15', 1, 6),
	(40, 4, 110, 'Jupiter JTB700 Tenor Saxophone', 40, 5500, 62, 1, 6, '2024-09-17 12:01:05', 1, 1);

-- Dumping structure for table golden_octaves_db.product_condition
CREATE TABLE IF NOT EXISTS `product_condition` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.product_condition: ~2 rows (approximately)
INSERT IGNORE INTO `product_condition` (`id`, `name`) VALUES
	(1, 'New'),
	(2, 'Refurbished');

-- Dumping structure for table golden_octaves_db.product_status
CREATE TABLE IF NOT EXISTS `product_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.product_status: ~2 rows (approximately)
INSERT IGNORE INTO `product_status` (`id`, `name`) VALUES
	(1, 'Active'),
	(2, 'Inactive');

-- Dumping structure for table golden_octaves_db.reviews
CREATE TABLE IF NOT EXISTS `reviews` (
  `id` int NOT NULL AUTO_INCREMENT,
  `review_type_id` int NOT NULL,
  `feedback` text NOT NULL,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reviews_product1_idx` (`product_id`),
  KEY `fk_reviews_user1_idx` (`user_id`),
  KEY `fk_reviews_review_type1_idx` (`review_type_id`),
  CONSTRAINT `fk_reviews_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_reviews_review_type1` FOREIGN KEY (`review_type_id`) REFERENCES `review_type` (`id`),
  CONSTRAINT `fk_reviews_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.reviews: ~0 rows (approximately)

-- Dumping structure for table golden_octaves_db.review_type
CREATE TABLE IF NOT EXISTS `review_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.review_type: ~0 rows (approximately)

-- Dumping structure for table golden_octaves_db.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `joined_datetime` datetime NOT NULL,
  `verification` varchar(10) NOT NULL,
  `account_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_account_status1_idx` (`account_status_id`),
  CONSTRAINT `fk_user_account_status1` FOREIGN KEY (`account_status_id`) REFERENCES `account_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.user: ~7 rows (approximately)
INSERT IGNORE INTO `user` (`id`, `first_name`, `last_name`, `email`, `password`, `joined_datetime`, `verification`, `account_status_id`) VALUES
	(1, 'Adrien Sanjuna', 'Delpitiya', 'sanjunadelpitiya1@gmail.com', '[1nTh3Night&2a', '2024-09-11 12:17:29', 'Verified', 1),
	(2, 'Ashvini', 'Delpitiya ', 'ashvinidelpitiya@gmail.com', 'Ash@231004', '2024-09-11 15:19:10', 'Verified', 1),
	(4, 'Devin', 'Silva', 'sdevinsilva@gmail.com', 'gOldenoctaves@37', '2024-09-13 07:21:24', 'Verified', 1),
	(5, 'Shevaan', 'Delpitiya', 'shevaandelpitiya01@gmail.com', 'Aminda@3005', '2024-09-13 20:36:33', 'Verified', 1),
	(6, 'Javana', 'Benul', 'javanabenul@gmail.com', 'Sanjuna@1', '2024-09-13 22:57:45', 'Verified', 1),
	(7, 'Ashan', 'Imantha', 'panzerb993@gmail.com', 'Ashan@123', '2024-09-14 11:17:07', 'Verified', 1),
	(8, 'Adrien Sanjuna', 'Delpitiya', 'sanjunadelpitiya@gmail.com', 'Sanjuna@2002', '2024-09-17 11:59:07', 'Verified', 1);

-- Dumping structure for table golden_octaves_db.watchlist
CREATE TABLE IF NOT EXISTS `watchlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_watchlist_product1_idx` (`product_id`),
  KEY `fk_watchlist_user1_idx` (`user_id`),
  CONSTRAINT `fk_watchlist_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_watchlist_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table golden_octaves_db.watchlist: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
