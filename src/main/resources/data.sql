
-- ========================================
-- FILE: 04_base_data_insert.sql  
-- Base data insertion
-- ========================================

-- Insert inspection types
INSERT INTO inspection_types (inspection_type_id, name, note, created_at, updated_at)
VALUES 
    ('01', 'GIÁM ĐỊNH MÁY MÓC, THIẾT BỊ VÀ DÂY CHUYỀN CÔNG NGHỆ ĐÃ QUA SỬ DỤNG', 'Kiểm tra an toàn', NOW(), NOW()),
    ('02', 'GIÁM ĐỊNH THƯƠNG MẠI', 'Kiểm tra chất lượng', NOW(), NOW()),
    ('03', 'GIÁM ĐỊNH MÔI TRƯỜNG', 'Kiểm tra môi trường', NOW(), NOW());

-- Insert admin user
INSERT INTO users (full_name, username, password_hash, email, role, phone, note, dob, is_active, created_at, updated_at)
VALUES (
    'Admin',
    'admin',
    '$2a$10$zkKynXsI4WzSDCjoD0jcs.IKLtIBoMnwKz6pBUYBFa2PDFjhbxuve',
    'admin@gmail.com',
    'ADMIN',
    '0123456789',
    'Test user for login',
    '2000-01-01',
    true,
    NOW(),
    NOW()
);

-- ========================================
-- FILE: 05_evaluation_data_insert.sql
-- Evaluation system data insertion  
-- ========================================

-- Insert inspection execution roles
INSERT INTO `inspection_execution_roles` (`role_code`, `role_name`, `role_description`, `is_leader`) VALUES
('TEAM_LEADER', 'Trưởng nhóm giám định GĐV', 'Người dẫn đầu nhóm giám định', TRUE),
('MEMBER', 'GĐV - thành viên', 'Thành viên trong nhóm giám định', FALSE),
('TRAINEE', 'GĐV Tập sự', 'Giám định viên đang trong thời gian tập sự', FALSE);

-- Insert evaluation categories
INSERT INTO `evaluation_categories` (`category_code`, `category_name`, `category_order`) VALUES
('DOSSIER_REVIEW', 'A. Xem xét sự đầy đủ thành phần hồ sơ yêu cầu giám định', 1),
('PREPARATION', 'B. Công tác chuẩn bị giám định và giám định tại hiện trường', 2),
('PROCESSING', 'C. Xử lý/ Báo cáo kết quả giám định', 3),
('CERTIFICATION', 'D. Cấp chứng thư và lưu hồ sơ', 4);

-- Insert evaluation criteria
INSERT INTO `evaluation_criteria` (`category_id`, `criteria_code`, `criteria_text`, `criteria_order`, `input_type`) VALUES
-- Category A: Xem xét sự đầy đủ thành phần hồ sơ
(1, 'A1', 'Hồ sơ đăng ký ban đầu đầy đủ theo yêu cầu của quy trình giám định', 1, 'CHECKBOX'),
(1, 'A2', 'Thời gian hoàn tất việc đăng ký ban đầu có đảm bảo nhanh, thuận lợi cho khách hàng hay không?', 2, 'CHECKBOX'),

-- Category B: Công tác chuẩn bị giám định
(2, 'B1', 'Có đảm bảo việc xem xét kỹ hồ sơ vụ giám định và tuân thủ kế hoạch/ phân công giám định hay không?', 1, 'CHECKBOX'),
(2, 'B2', 'Có đảm bảo việc chuẩn bị đầy đủ biểu mẫu và trang thiết bị giám định hay không?', 2, 'CHECKBOX'),
(2, 'B3', 'Tuân thủ trình tự trong quy trình giám định, ghi chép đầy đủ các biểu mẫu', 3, 'CHECKBOX'),
(2, 'B4', 'Có chụp ảnh đúng quy trình hay không?', 4, 'CHECKBOX'),
(2, 'B5', 'Có đảm bảo đúng thời gian như đã hẹn với khách hàng hay không?', 5, 'CHECKBOX'),

-- Category C: Xử lý/ Báo cáo kết quả giám định
(3, 'C1', 'Tuân thủ trình tự trong quy trình giám định, ghi chép đầy đủ các biểu mẫu', 1, 'CHECKBOX'),
(3, 'C2', 'Có xem xét và đối chiếu giữa kết quả giám định với hồ sơ và tài liệu kỹ thuật liên quan hay không?', 2, 'CHECKBOX'),
(3, 'C3', 'Việc soạn thảo và và phê duyệt chứng thư có đảm bảo đúng trình tự hay không?', 3, 'CHECKBOX'),
(3, 'C4', 'Có đảm bảo đúng thời gian như kế hoạch đã phân công hay không?', 4, 'CHECKBOX'),

-- Category D: Cấp chứng thư và lưu hồ sơ
(4, 'D1', 'Tuân thủ trình tự trong quy trình giám định, ghi chép đầy đủ các biểu mẫu', 1, 'CHECKBOX'),
(4, 'D2', 'Việc lưu giữ hồ sơ có đảm bảo đúng như quy định của quy trình hay không?', 2, 'CHECKBOX'),
(4, 'D3', 'Có đảm bảo đúng thời gian đúng như kế hoạch giám định hay không?', 3, 'CHECKBOX');

-- Insert document types
INSERT INTO `dossier_document_types` (`type_code`, `type_name`, `type_name_english`, `category`, `display_order`) VALUES
-- Khách hàng cung cấp
('REQUEST_LETTER', 'Giấy yêu cầu giám định', '', 'CUSTOMER', 1),
('INSPECTION_CERT', 'Phiếu chưng cầu giám định', '', 'CUSTOMER', 2),
('PACKING_LIST', 'Phiếu đóng gói', 'Packing list', 'CUSTOMER', 3),
('INVOICE', 'Hóa đơn', 'Invoice', 'CUSTOMER', 4),
('CONTRACT_LC', 'Hợp đồng, LC', '', 'CUSTOMER', 5),
('BILL_OF_LOADING', 'Vận đơn', 'Bill of loading', 'CUSTOMER', 6),
('CUSTOMS_DECLARATION', 'Tờ khai hải quan', '', 'CUSTOMER', 7),
('CO_CERTIFICATE', 'C/O', '', 'CUSTOMER', 8),
('TECHNICAL_DOCS', 'Tài liệu kỹ thuật', '', 'CUSTOMER', 9),
('MAINTENANCE_DOCS', 'Hồ sơ bảo dưỡng, bảo trì', '', 'CUSTOMER', 10),
('INSTALLATION_DIAGRAM', 'Sơ đồ lắp đặt', '', 'CUSTOMER', 11),
('PRODUCTION_STANDARD', 'Xác nhận tiêu chuẩn sản xuất', '', 'CUSTOMER', 12),
('ADDITIONAL_DOCS', 'Hồ sơ, tài liệu theo yêu cầu trên giấy yêu cầu giám định', '', 'CUSTOMER', 13),

-- BMI lập
('COMPLETION_TRACKING', 'Phiếu theo dõi hoàn tất vụ giám định', '', 'BMI', 1),
('INSPECTION_REPORT', 'Báo cáo kết quả giám định', '', 'BMI', 2),
('WORK_MINUTES', 'Biên bản làm việc hoặc Biên bản hiện trường giám định', '', 'BMI', 3),
('FIELD_INFO_FORM', 'Phiếu thu thập thông tin giám định tại hiện trường', '', 'BMI', 4),
('REJECTION_NOTICE', 'Thông báo từ chối giám định', '', 'BMI', 5),
('IMAGES', 'Hình ảnh', '', 'BMI', 6),
('EXTERNAL_DOCS', 'Tài liệu (ngoài)', '', 'BMI', 7),
('INSPECTION_CERTIFICATE', 'Chứng thư giám định', '', 'BMI', 8);