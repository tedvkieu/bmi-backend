-- Drop existing tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS `inspection_results`;
DROP TABLE IF EXISTS `machines`;
DROP TABLE IF EXISTS `dossiers`;
DROP TABLE IF EXISTS `inspection_types`;
DROP TABLE IF EXISTS `customers`;
DROP TABLE IF EXISTS `users`;

-- Create users table first (no dependencies)
CREATE TABLE `users` (
    `user_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `full_name` VARCHAR(255) NOT NULL,
    `dob` DATE,
    `role` VARCHAR(50) NOT NULL,
    `username` VARCHAR(50) UNIQUE NOT NULL,
    `password_hash` VARCHAR(255) NOT NULL,
    `email` VARCHAR(100),
    `phone` VARCHAR(20),
    `note` TEXT,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create customers table (no dependencies)
CREATE TABLE `customers` (
    `customer_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `address` VARCHAR(500),
    `email` VARCHAR(100),
    `password` VARCHAR(255), 
    `dob` DATE,
    `phone` VARCHAR(20),
    `tax_code` VARCHAR(50),
    `note` TEXT,
    `customer_type` VARCHAR(50) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create inspection_types table (no dependencies)
CREATE TABLE `inspection_types` (
    `inspection_type_id` VARCHAR(255) PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `note` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create dossiers table (depends on customers, inspection_types)
CREATE TABLE `dossiers` (
    `dossier_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `registration_no` VARCHAR(100),
    `registration_date` DATE,
    `customer_submit_id` BIGINT NOT NULL,
    `customer_related_id` BIGINT,
    `inspection_type_id` VARCHAR(255) NOT NULL,
    `declaration_no` VARCHAR(100),
    `declaration_date` DATE,
    `bill_of_lading` VARCHAR(100),
    `bill_of_lading_date` DATE,
    `ship_name` VARCHAR(255),
    `cout10` INT,
    `cout20` INT,
    `bulk_ship` BOOLEAN NOT NULL DEFAULT FALSE,
    `declaration_doc` VARCHAR(500),
    `declaration_place` VARCHAR(255),
    `inspection_date` DATE,
    `certificate_date` DATE,
    `inspection_location` VARCHAR(500),
    `certificate_status` VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `files` VARCHAR(1000),
    FOREIGN KEY (`customer_submit_id`) REFERENCES `customers`(`customer_id`) ON DELETE CASCADE,
    FOREIGN KEY (`customer_related_id`) REFERENCES `customers`(`customer_id`) ON DELETE SET NULL,
    FOREIGN KEY (`inspection_type_id`) REFERENCES `inspection_types`(`inspection_type_id`) ON DELETE CASCADE
);

-- Create machines table (depends on dossiers)
CREATE TABLE `machines` (
    `machine_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `dossier_id` BIGINT NOT NULL,
    `registration_no` VARCHAR(100),
    `item_name` VARCHAR(255),
    `brand` VARCHAR(100),
    `model` VARCHAR(100),
    `serial_number` VARCHAR(100),
    `manufacture_country` VARCHAR(100),
    `manufacturer_name` VARCHAR(255),
    `manufacture_year` INT,
    `quantity` INT,
    `usage_description` TEXT,
    `note` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`dossier_id`) REFERENCES `dossiers`(`dossier_id`) ON DELETE CASCADE
);

-- Create inspection_results table (depends on machines, dossiers)
CREATE TABLE `inspection_results` (
    `inspection_result_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `machine_id` BIGINT NOT NULL,
    `dossier_id` BIGINT NOT NULL,
    `snapshot_registration_no` VARCHAR(100),
    `snapshot_item_name` VARCHAR(255),
    `snapshot_brand` VARCHAR(100),
    `snapshot_model` VARCHAR(100),
    `snapshot_serial_number` VARCHAR(100),
    `snapshot_manufacture_country` VARCHAR(100),
    `snapshot_manufacturer_name` VARCHAR(255),
    `snapshot_manufacture_year` INT,
    `snapshot_quantity` INT,
    `snapshot_usage` TEXT,
    `evaluation` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`machine_id`) REFERENCES `machines`(`machine_id`) ON DELETE CASCADE,
    FOREIGN KEY (`dossier_id`) REFERENCES `dossiers`(`dossier_id`) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX `idx_dossiers_registration_no` ON `dossiers`(`registration_no`);
CREATE INDEX `idx_dossiers_registration_date` ON `dossiers`(`registration_date`);
CREATE INDEX `idx_dossiers_certificate_status` ON `dossiers`(`certificate_status`);
CREATE INDEX `idx_customers_tax_code` ON `customers`(`tax_code`);
CREATE INDEX `idx_machines_serial_number` ON `machines`(`serial_number`);

-- Thêm cột created_by_user_id vào bảng dossiers
ALTER TABLE `dossiers` 
ADD COLUMN `created_by_user_id` BIGINT AFTER `updated_at`;

-- Thêm foreign key constraint
ALTER TABLE `dossiers` 
ADD CONSTRAINT `fk_dossiers_created_by_user` 
FOREIGN KEY (`created_by_user_id`) REFERENCES `users`(`user_id`) 
ON DELETE SET NULL;