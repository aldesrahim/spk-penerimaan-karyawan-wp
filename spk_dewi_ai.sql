/*
 Navicat Premium Data Transfer

 Source Server         : LOCAL_DOCKER_MYSQL
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : mysql:3306
 Source Schema         : spk_dewi_ai

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 07/07/2024 17:40:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for applicants
-- ----------------------------
DROP TABLE IF EXISTS `applicants`;
CREATE TABLE `applicants` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `vacancy_id` bigint unsigned DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `religion` varchar(50) DEFAULT NULL,
  `gender` tinyint DEFAULT NULL COMMENT '1 male, 2 female, 3 others',
  `address` text,
  `dob` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `vacancy_id` (`vacancy_id`),
  CONSTRAINT `applicants_ibfk_1` FOREIGN KEY (`vacancy_id`) REFERENCES `vacancies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of applicants
-- ----------------------------
BEGIN;
INSERT INTO `applicants` (`id`, `vacancy_id`, `name`, `phone_number`, `religion`, `gender`, `address`, `dob`) VALUES (1, 1, 'John Doe', '083893962489', 'Other', 2, 'Depok City', '2001-12-27');
INSERT INTO `applicants` (`id`, `vacancy_id`, `name`, `phone_number`, `religion`, `gender`, `address`, `dob`) VALUES (2, 1, 'Jaka', '085156211645', 'Islam', 1, 'Ciracas', '1998-07-07');
COMMIT;

-- ----------------------------
-- Table structure for calculations
-- ----------------------------
DROP TABLE IF EXISTS `calculations`;
CREATE TABLE `calculations` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `evaluation_id` bigint unsigned DEFAULT NULL,
  `s` decimal(11,8) DEFAULT NULL,
  `v` decimal(11,8) DEFAULT NULL COMMENT 'will be updated',
  PRIMARY KEY (`id`),
  UNIQUE KEY `evaluation_id` (`evaluation_id`),
  CONSTRAINT `calculations_ibfk_1` FOREIGN KEY (`evaluation_id`) REFERENCES `evaluations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of calculations
-- ----------------------------
BEGIN;
INSERT INTO `calculations` (`id`, `evaluation_id`, `s`, `v`) VALUES (55, 2, 2.20036731, 0.52515094);
INSERT INTO `calculations` (`id`, `evaluation_id`, `s`, `v`) VALUES (56, 3, 1.98960391, 0.47484906);
COMMIT;

-- ----------------------------
-- Table structure for criterias
-- ----------------------------
DROP TABLE IF EXISTS `criterias`;
CREATE TABLE `criterias` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `weight_id` bigint unsigned DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `attribute` tinyint DEFAULT NULL COMMENT '1 benefit, -1 cost',
  `normalization` decimal(11,8) DEFAULT NULL COMMENT 'weight proportion',
  PRIMARY KEY (`id`),
  KEY `weight_id` (`weight_id`),
  CONSTRAINT `criterias_ibfk_1` FOREIGN KEY (`weight_id`) REFERENCES `weights` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of criterias
-- ----------------------------
BEGIN;
INSERT INTO `criterias` (`id`, `weight_id`, `name`, `attribute`, `normalization`) VALUES (1, 3, 'Pendidikan', 1, 0.15000000);
INSERT INTO `criterias` (`id`, `weight_id`, `name`, `attribute`, `normalization`) VALUES (2, 4, 'Jurusan', 1, 0.20000000);
INSERT INTO `criterias` (`id`, `weight_id`, `name`, `attribute`, `normalization`) VALUES (3, 2, 'Usia', -1, 0.10000000);
INSERT INTO `criterias` (`id`, `weight_id`, `name`, `attribute`, `normalization`) VALUES (4, 3, 'Pengalaman', 1, 0.15000000);
INSERT INTO `criterias` (`id`, `weight_id`, `name`, `attribute`, `normalization`) VALUES (5, 3, 'Wawancara', 1, 0.15000000);
INSERT INTO `criterias` (`id`, `weight_id`, `name`, `attribute`, `normalization`) VALUES (6, 5, 'Psikotes', 1, 0.25000000);
COMMIT;

-- ----------------------------
-- Table structure for evaluation_details
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_details`;
CREATE TABLE `evaluation_details` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `evaluation_id` bigint unsigned DEFAULT NULL,
  `criteria_id` bigint unsigned DEFAULT NULL,
  `sub_criteria_id` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `evaluation_id` (`evaluation_id`),
  CONSTRAINT `evaluation_details_ibfk_1` FOREIGN KEY (`evaluation_id`) REFERENCES `evaluations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of evaluation_details
-- ----------------------------
BEGIN;
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (25, 3, 1, 3);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (26, 3, 2, 8);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (27, 3, 3, 12);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (28, 3, 4, 17);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (29, 3, 5, 23);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (30, 3, 6, 27);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (37, 2, 1, 31);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (38, 2, 2, 9);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (39, 2, 3, 15);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (40, 2, 4, 18);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (41, 2, 5, 21);
INSERT INTO `evaluation_details` (`id`, `evaluation_id`, `criteria_id`, `sub_criteria_id`) VALUES (42, 2, 6, 29);
COMMIT;

-- ----------------------------
-- Table structure for evaluations
-- ----------------------------
DROP TABLE IF EXISTS `evaluations`;
CREATE TABLE `evaluations` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `applicant_id` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `applicant_id` (`applicant_id`),
  CONSTRAINT `evaluations_ibfk_1` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of evaluations
-- ----------------------------
BEGIN;
INSERT INTO `evaluations` (`id`, `applicant_id`) VALUES (2, 1);
INSERT INTO `evaluations` (`id`, `applicant_id`) VALUES (3, 2);
COMMIT;

-- ----------------------------
-- Table structure for sub_criterias
-- ----------------------------
DROP TABLE IF EXISTS `sub_criterias`;
CREATE TABLE `sub_criterias` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `criteria_id` bigint unsigned DEFAULT NULL,
  `weight_id` bigint unsigned DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `criteria_id` (`criteria_id`),
  KEY `weight_id` (`weight_id`),
  CONSTRAINT `sub_criterias_ibfk_1` FOREIGN KEY (`criteria_id`) REFERENCES `criterias` (`id`),
  CONSTRAINT `sub_criterias_ibfk_2` FOREIGN KEY (`weight_id`) REFERENCES `weights` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sub_criterias
-- ----------------------------
BEGIN;
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (2, 1, 2, 'D1');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (3, 1, 3, 'D3');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (4, 1, 4, 'S1');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (5, 1, 5, 'S2');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (6, 2, 1, 'Sangat Tidak Sesuai');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (7, 2, 2, 'Tidak Sesuai');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (8, 2, 3, 'Cukup Sesuai');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (9, 2, 4, 'Sesuai');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (10, 2, 5, 'Sangat Sesuai');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (11, 3, 5, '18-21');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (12, 3, 4, '22-25');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (13, 3, 3, '26-29');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (14, 3, 2, '30-33');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (15, 3, 1, '34-36');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (16, 4, 1, '0 tahun');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (17, 4, 2, '1 tahun');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (18, 4, 3, '2 tahun');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (19, 4, 4, '3 tahun');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (20, 4, 5, '>3 tahun');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (21, 5, 1, '<59');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (22, 5, 2, '60-69');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (23, 5, 3, '70-79');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (24, 5, 4, '80-89');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (25, 5, 5, '90-100');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (26, 6, 1, '<59');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (27, 6, 2, '60-69');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (28, 6, 3, '70-79');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (29, 6, 4, '80-89');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (30, 6, 5, '90-100');
INSERT INTO `sub_criterias` (`id`, `criteria_id`, `weight_id`, `name`) VALUES (31, 1, 1, 'SMA');
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`id`, `username`, `password`, `name`) VALUES (1, 'dewi', '5f4dcc3b5aa765d61d8327deb882cf99', 'Dewi Wulandari');
INSERT INTO `users` (`id`, `username`, `password`, `name`) VALUES (2, 'aldes', '5ebe2294ecd0e0f08eab7690d2a6ee69', 'Ahmad Al Desrahim');
COMMIT;

-- ----------------------------
-- Table structure for vacancies
-- ----------------------------
DROP TABLE IF EXISTS `vacancies`;
CREATE TABLE `vacancies` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `position` varchar(100) DEFAULT NULL,
  `quota` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of vacancies
-- ----------------------------
BEGIN;
INSERT INTO `vacancies` (`id`, `position`, `quota`) VALUES (1, 'IT', 10);
INSERT INTO `vacancies` (`id`, `position`, `quota`) VALUES (2, 'Sales', 20);
INSERT INTO `vacancies` (`id`, `position`, `quota`) VALUES (3, 'Manager', 5);
INSERT INTO `vacancies` (`id`, `position`, `quota`) VALUES (4, 'Admin', 100);
COMMIT;

-- ----------------------------
-- Table structure for weights
-- ----------------------------
DROP TABLE IF EXISTS `weights`;
CREATE TABLE `weights` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `weight` int unsigned DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of weights
-- ----------------------------
BEGIN;
INSERT INTO `weights` (`id`, `weight`, `description`) VALUES (1, 1, 'Tidak Penting');
INSERT INTO `weights` (`id`, `weight`, `description`) VALUES (2, 2, 'Kurang Penting');
INSERT INTO `weights` (`id`, `weight`, `description`) VALUES (3, 3, 'Cukup Penting');
INSERT INTO `weights` (`id`, `weight`, `description`) VALUES (4, 4, 'Penting');
INSERT INTO `weights` (`id`, `weight`, `description`) VALUES (5, 5, 'Sangat Penting');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
