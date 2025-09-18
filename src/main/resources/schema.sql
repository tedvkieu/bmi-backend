-- ========================================
-- FILE: 01_base_schema.sql
-- Base tables (existing system)
-- ========================================

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
    `files` VARCHAR(1000),
    `created_by_user_id` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`customer_submit_id`) REFERENCES `customers`(`customer_id`) ON DELETE CASCADE,
    FOREIGN KEY (`customer_related_id`) REFERENCES `customers`(`customer_id`) ON DELETE SET NULL,
    FOREIGN KEY (`inspection_type_id`) REFERENCES `inspection_types`(`inspection_type_id`) ON DELETE CASCADE,
    FOREIGN KEY (`created_by_user_id`) REFERENCES `users`(`user_id`) ON DELETE SET NULL
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

-- ========================================
-- FILE: 02_evaluation_extension_schema.sql
-- Extension tables for evaluation system
-- ========================================

-- Bảng lưu trữ vai trò khi thực hiện giám định
CREATE TABLE `inspection_execution_roles` (
    `role_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `role_code` VARCHAR(50) NOT NULL UNIQUE,
    `role_name` VARCHAR(255) NOT NULL,
    `role_description` TEXT,
    `is_leader` BOOLEAN NOT NULL DEFAULT FALSE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng quan hệ giữa user và vai trò thực hiện giám định cho từng dossier
CREATE TABLE `dossier_inspection_team` (
    `team_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `dossier_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `execution_role_id` BIGINT NOT NULL,
    `assigned_date` DATE,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`dossier_id`) REFERENCES `dossiers`(`dossier_id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`execution_role_id`) REFERENCES `inspection_execution_roles`(`role_id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_dossier_user` (`dossier_id`, `user_id`)
);

-- Bảng chính lưu trữ phiếu theo dõi đánh giá hoàn tất vụ giám định
CREATE TABLE `inspection_completion_evaluations` (
    `evaluation_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `dossier_id` BIGINT NOT NULL,
    `inspection_number` VARCHAR(100) NOT NULL,
    `evaluation_date` DATE,
    `evaluator_user_id` BIGINT,
    `supervisor_user_id` BIGINT,
    `status` VARCHAR(50) NOT NULL DEFAULT 'IN_PROGRESS',
    `notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`dossier_id`) REFERENCES `dossiers`(`dossier_id`) ON DELETE CASCADE,
    FOREIGN KEY (`evaluator_user_id`) REFERENCES `users`(`user_id`) ON DELETE SET NULL,
    FOREIGN KEY (`supervisor_user_id`) REFERENCES `users`(`user_id`) ON DELETE SET NULL,
    UNIQUE KEY `unique_dossier_evaluation` (`dossier_id`)
);

-- Bảng lưu trữ các tiêu chí đánh giá (categories)
CREATE TABLE `evaluation_categories` (
    `category_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `category_code` VARCHAR(50) NOT NULL UNIQUE,
    `category_name` VARCHAR(255) NOT NULL,
    `category_order` INT NOT NULL DEFAULT 0,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng lưu trữ các tiêu chí đánh giá chi tiết
CREATE TABLE `evaluation_criteria` (
    `criteria_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `category_id` BIGINT NOT NULL,
    `criteria_code` VARCHAR(50) NOT NULL,
    `criteria_order` INT NOT NULL DEFAULT 0,
    `criteria_text` TEXT NOT NULL,
    `input_type` ENUM('CHECKBOX', 'TEXT', 'SELECT', 'DATE', 'NUMBER') NOT NULL DEFAULT 'CHECKBOX',
    `is_required` BOOLEAN NOT NULL DEFAULT FALSE,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`category_id`) REFERENCES `evaluation_categories`(`category_id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_category_code` (`category_id`, `criteria_code`)
);

-- Bảng lưu trữ kết quả đánh giá cho từng tiêu chí
CREATE TABLE `evaluation_results` (
    `result_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `evaluation_id` BIGINT NOT NULL,
    `criteria_id` BIGINT NOT NULL,
    `checkbox_value` BOOLEAN NULL,
    `text_value` TEXT NULL,
    `number_value` DECIMAL(10,2) NULL,
    `date_value` DATE NULL,
    `select_value` VARCHAR(255) NULL,
    `notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`evaluation_id`) REFERENCES `inspection_completion_evaluations`(`evaluation_id`) ON DELETE CASCADE,
    FOREIGN KEY (`criteria_id`) REFERENCES `evaluation_criteria`(`criteria_id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_evaluation_criteria` (`evaluation_id`, `criteria_id`)
);

-- Bảng lưu trữ danh mục hồ sơ giám định
CREATE TABLE `dossier_document_types` (
    `document_type_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `type_code` VARCHAR(50) NOT NULL UNIQUE,
    `type_name` VARCHAR(255) NOT NULL,
    `type_name_english` VARCHAR(255),
    `category` ENUM('CUSTOMER', 'BMI') NOT NULL,
    `is_required` BOOLEAN NOT NULL DEFAULT FALSE,
    `display_order` INT NOT NULL DEFAULT 0,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng theo dõi hồ sơ có trong dossier
CREATE TABLE `dossier_documents_checklist` (
    `checklist_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `evaluation_id` BIGINT NOT NULL,
    `document_type_id` BIGINT NOT NULL,
    `has_physical_copy` BOOLEAN NOT NULL DEFAULT FALSE,
    `has_electronic_copy` BOOLEAN NOT NULL DEFAULT FALSE,
    `electronic_file_path` VARCHAR(500),
    `notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`evaluation_id`) REFERENCES `inspection_completion_evaluations`(`evaluation_id`) ON DELETE CASCADE,
    FOREIGN KEY (`document_type_id`) REFERENCES `dossier_document_types`(`document_type_id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_evaluation_document` (`evaluation_id`, `document_type_id`)
);

-- Bảng lưu trữ thông tin ký duyệt
CREATE TABLE `evaluation_signatures` (
    `signature_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `evaluation_id` BIGINT NOT NULL,
    `signature_type` ENUM('EVALUATOR', 'SUPERVISOR') NOT NULL,
    `user_id` BIGINT NOT NULL,
    `signature_date` DATE,
    `full_name` VARCHAR(255),
    `position` VARCHAR(255),
    `digital_signature` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`evaluation_id`) REFERENCES `inspection_completion_evaluations`(`evaluation_id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_evaluation_signature_type` (`evaluation_id`, `signature_type`)
);

-- ========================================
-- FILE: 03_indexes_and_views.sql
-- Indexes and Views for performance
-- ========================================

-- Thêm indexes cho hiệu suất
CREATE INDEX `idx_inspection_completion_evaluations_dossier` ON `inspection_completion_evaluations`(`dossier_id`);
CREATE INDEX `idx_inspection_completion_evaluations_status` ON `inspection_completion_evaluations`(`status`);
CREATE INDEX `idx_evaluation_results_evaluation` ON `evaluation_results`(`evaluation_id`);
CREATE INDEX `idx_evaluation_results_criteria` ON `evaluation_results`(`criteria_id`);
CREATE INDEX `idx_dossier_inspection_team_dossier` ON `dossier_inspection_team`(`dossier_id`);
CREATE INDEX `idx_dossier_inspection_team_user` ON `dossier_inspection_team`(`user_id`);
CREATE INDEX `idx_dossier_documents_checklist_evaluation` ON `dossier_documents_checklist`(`evaluation_id`);

-- View để xem thông tin đầy đủ của phiếu đánh giá
CREATE VIEW `v_inspection_evaluations_full` AS
SELECT 
    ice.evaluation_id,
    ice.inspection_number,
    ice.evaluation_date,
    ice.status,
    d.dossier_id,
    d.registration_no,
    d.registration_date,
    c_submit.name AS customer_submit_name,
    c_related.name AS customer_related_name,
    it.name AS inspection_type_name,
    u_evaluator.full_name AS evaluator_name,
    u_supervisor.full_name AS supervisor_name,
    ice.created_at,
    ice.updated_at
FROM inspection_completion_evaluations ice
JOIN dossiers d ON ice.dossier_id = d.dossier_id
JOIN customers c_submit ON d.customer_submit_id = c_submit.customer_id
LEFT JOIN customers c_related ON d.customer_related_id = c_related.customer_id
JOIN inspection_types it ON d.inspection_type_id = it.inspection_type_id
LEFT JOIN users u_evaluator ON ice.evaluator_user_id = u_evaluator.user_id
LEFT JOIN users u_supervisor ON ice.supervisor_user_id = u_supervisor.user_id;

-- View để xem kết quả đánh giá chi tiết
CREATE VIEW `v_evaluation_results_detail` AS
SELECT 
    er.result_id,
    er.evaluation_id,
    ec.category_name,
    ecr.criteria_code,
    ecr.criteria_text,
    ecr.input_type,
    er.checkbox_value,
    er.text_value,
    er.number_value,
    er.date_value,
    er.select_value,
    er.notes,
    er.created_at
FROM evaluation_results er
JOIN evaluation_criteria ecr ON er.criteria_id = ecr.criteria_id
JOIN evaluation_categories ec ON ecr.category_id = ec.category_id
ORDER BY ec.category_order, ecr.criteria_order;

-- View để xem team inspection cho dossier
CREATE VIEW `v_dossier_inspection_teams` AS
SELECT 
    dit.team_id,
    dit.dossier_id,
    d.registration_no,
    u.full_name,
    u.email,
    u.phone,
    ier.role_name,
    ier.is_leader,
    dit.assigned_date,
    dit.is_active
FROM dossier_inspection_team dit
JOIN users u ON dit.user_id = u.user_id
JOIN inspection_execution_roles ier ON dit.execution_role_id = ier.role_id
JOIN dossiers d ON dit.dossier_id = d.dossier_id
WHERE dit.is_active = TRUE
ORDER BY dit.dossier_id, ier.is_leader DESC, u.full_name;
