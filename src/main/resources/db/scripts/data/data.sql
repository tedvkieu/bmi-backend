
/*
========================================
FILE: 04_base_data_insert.sql  
Base data insertion
========================================
*/

/* Insert inspection types */
INSERT INTO inspection_types (inspection_type_id, name, note, created_at, updated_at)
VALUES 
    ('01', 'GIÁM ĐỊNH MÁY MÓC, THIẾT BỊ VÀ DÂY CHUYỀN CÔNG NGHỆ ĐÃ QUA SỬ DỤNG', 'Kiểm tra an toàn', NOW(), NOW()),
    ('02', 'GIÁM ĐỊNH THƯƠNG MẠI', 'Kiểm tra chất lượng', NOW(), NOW()),
    ('03', 'GIÁM ĐỊNH MÔI TRƯỜNG', 'Kiểm tra môi trường', NOW(), NOW());

/* Insert admin user */
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

/*
========================================
FILE: 05_evaluation_data_insert.sql
Evaluation system data insertion  
========================================
*/

/* Insert inspection execution roles */
INSERT INTO `inspection_execution_roles` (`role_code`, `role_name`, `role_description`, `is_leader`) VALUES
('TEAM_LEADER', 'Trưởng nhóm giám định GĐV', 'Người dẫn đầu nhóm giám định', TRUE),
('MEMBER', 'GĐV - thành viên', 'Thành viên trong nhóm giám định', FALSE),
('TRAINEE', 'GĐV Tập sự', 'Giám định viên đang trong thời gian tập sự', FALSE);

/* Insert evaluation categories */
INSERT INTO `evaluation_categories` (`category_code`, `category_name`, `category_order`) VALUES
('DOSSIER_REVIEW', 'A. Xem xét sự đầy đủ thành phần hồ sơ yêu cầu giám định', 1),
('PREPARATION', 'B. Công tác chuẩn bị giám định và giám định tại hiện trường', 2),
('PROCESSING', 'C. Xử lý/ Báo cáo kết quả giám định', 3),
('CERTIFICATION', 'D. Cấp chứng thư và lưu hồ sơ', 4);

/* Insert evaluation criteria */
INSERT INTO `evaluation_criteria` (`category_id`, `criteria_code`, `criteria_text`, `criteria_order`, `input_type`) VALUES
/* Category A: Xem xét sự đầy đủ thành phần hồ sơ */
(1, 'A1', 'Hồ sơ đăng ký ban đầu đầy đủ theo yêu cầu của quy trình giám định', 1, 'CHECKBOX'),
(1, 'A2', 'Thời gian hoàn tất việc đăng ký ban đầu có đảm bảo nhanh, thuận lợi cho khách hàng hay không?', 2, 'CHECKBOX'),

/* Category B: Công tác chuẩn bị giám định */
(2, 'B1', 'Có đảm bảo việc xem xét kỹ hồ sơ vụ giám định và tuân thủ kế hoạch/ phân công giám định hay không?', 1, 'CHECKBOX'),
(2, 'B2', 'Có đảm bảo việc chuẩn bị đầy đủ biểu mẫu và trang thiết bị giám định hay không?', 2, 'CHECKBOX'),
(2, 'B3', 'Tuân thủ trình tự trong quy trình giám định, ghi chép đầy đủ các biểu mẫu', 3, 'CHECKBOX'),
(2, 'B4', 'Có chụp ảnh đúng quy trình hay không?', 4, 'CHECKBOX'),
(2, 'B5', 'Có đảm bảo đúng thời gian như đã hẹn với khách hàng hay không?', 5, 'CHECKBOX'),

/* Category C: Xử lý/ Báo cáo kết quả giám định */
(3, 'C1', 'Tuân thủ trình tự trong quy trình giám định, ghi chép đầy đủ các biểu mẫu', 1, 'CHECKBOX'),
(3, 'C2', 'Có xem xét và đối chiếu giữa kết quả giám định với hồ sơ và tài liệu kỹ thuật liên quan hay không?', 2, 'CHECKBOX'),
(3, 'C3', 'Việc soạn thảo và và phê duyệt chứng thư có đảm bảo đúng trình tự hay không?', 3, 'CHECKBOX'),
(3, 'C4', 'Có đảm bảo đúng thời gian như kế hoạch đã phân công hay không?', 4, 'CHECKBOX'),

/* Category D: Cấp chứng thư và lưu hồ sơ */
(4, 'D1', 'Tuân thủ trình tự trong quy trình giám định, ghi chép đầy đủ các biểu mẫu', 1, 'CHECKBOX'),
(4, 'D2', 'Việc lưu giữ hồ sơ có đảm bảo đúng như quy định của quy trình hay không?', 2, 'CHECKBOX'),
(4, 'D3', 'Có đảm bảo đúng thời gian đúng như kế hoạch giám định hay không?', 3, 'CHECKBOX');

/* Insert document types */
INSERT INTO `dossier_document_types` (`type_code`, `type_name`, `type_name_english`, `category`, `display_order`) VALUES
/* Khách hàng cung cấp */
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

/* ------------------------------------------------------------------------------------------------
-- ========================================
-- SAMPLE DATA - Dữ liệu mẫu đầy đủ cho form đánh giá
-- ========================================

-- Thêm users mẫu (nhân viên thực hiện) */
INSERT INTO users (full_name, username, password_hash, email, role, phone, note, dob, is_active, created_at, updated_at)
VALUES 
    ('Nguyễn Văn An', 'nguyen.van.an', '$2a$10$zkKynXsI4WzSDCjoD0jcs.IKLtIBoMnwKz6pBUYBFa2PDFjhbxuve', 'an.nguyen@bmi.vn', 'STAFF', '0901234567', 'Chuyên viên theo dõi', '1985-05-15', true, NOW(), NOW()),
    ('Trần Minh Đức', 'tran.minh.duc', '$2a$10$zkKynXsI4WzSDCjoD0jcs.IKLtIBoMnwKz6pBUYBFa2PDFjhbxuve', 'duc.tran@bmi.vn', 'INSPECTOR', '0907654321', 'Trưởng nhóm giám định GĐV', '1980-08-20', true, NOW(), NOW()),
    ('Lê Thị Hồng', 'le.thi.hong', '$2a$10$zkKynXsI4WzSDCjoD0jcs.IKLtIBoMnwKz6pBUYBFa2PDFjhbxuve', 'hong.le@bmi.vn', 'INSPECTOR', '0912345678', 'GĐV - thành viên', '1988-12-10', true, NOW(), NOW()),
    ('Phạm Văn Nam', 'pham.van.nam', '$2a$10$zkKynXsI4WzSDCjoD0jcs.IKLtIBoMnwKz6pBUYBFa2PDFjhbxuve', 'nam.pham@bmi.vn', 'INSPECTOR', '0923456789', 'GĐV Tập sự', '1992-03-25', true, NOW(), NOW()),
    ('Hoàng Thị Lan', 'hoang.thi.lan', '$2a$10$zkKynXsI4WzSDCjoD0jcs.IKLtIBoMnwKz6pBUYBFa2PDFjhbxuve', 'lan.hoang@bmi.vn', 'INSPECTOR', '0934567890', 'GĐV Tập sự', '1993-07-08', true, NOW(), NOW()),
    ('Trần Văn Bình', 'tran.van.binh', '$2a$10$zkKynXsI4WzSDCjoD0jcs.IKLtIBoMnwKz6pBUYBFa2PDFjhbxuve', 'binh.tran@bmi.vn', 'MANAGER', '0945678901', 'Lãnh đạo phê duyệt', '1975-11-30', true, NOW(), NOW());

-- Thêm khách hàng mẫu
INSERT INTO customers (name, address, email, phone, tax_code, note, customer_type, created_at, updated_at)
VALUES 
    ('Công ty TNHH ABC Manufacturing', '123 Đường Nguyễn Văn Cừ, Quận 5, TP.HCM', 'contact@abc-mfg.com', '0283456789', '0123456789', 'Khách hàng nhập khẩu máy móc', 'COMPANY', NOW(), NOW()),
    ('Công ty XYZ Trading Co., Ltd', '456 Đường Lê Văn Việt, Quận 9, TP.HCM', 'info@xyz-trading.com', '0287654321', '0987654321', 'Công ty xuất khẩu', 'COMPANY', NOW(), NOW());

-- Tạo dossier mẫu
INSERT INTO dossiers (registration_no, registration_date, customer_submit_id, customer_related_id, inspection_type_id, declaration_no, declaration_date, bill_of_lading, bill_of_lading_date, ship_name, cout10, cout20, bulk_ship, declaration_doc, declaration_place, inspection_date, certificate_date, inspection_location, certificate_status, files, created_by_user_id, created_at, updated_at)
VALUES 
    ('BMI/2024/GD-001/KT', '2024-03-01', 1, 2, '01', 'TK123456789', '2024-02-28', 'BL-ABC-2024-001', '2024-02-25', 'MV Ocean Star', 0, 5, false, 'TK123456789.pdf', 'Cảng Cát Lái, TP.HCM', '2024-03-10', '2024-03-15', 'Kho A1, Cảng Cát Lái, TP.HCM', 'PENDING', '/files/BMI-2024-GD-001-KT/', 2, NOW(), NOW());

-- Thêm máy móc trong dossier
INSERT INTO machines (dossier_id, registration_no, item_name, brand, model, serial_number, manufacture_country, manufacturer_name, manufacture_year, quantity, usage_description, note, created_at, updated_at)
VALUES 
    (1, 'BMI/2024/GD-001/KT', 'Máy dập thủy lực', 'AMADA', 'TP-110', 'TP110-2020-5678', 'Nhật Bản', 'AMADA Co., Ltd', 2020, 1, 'Dập kim loại tấm trong sản xuất ô tô', 'Máy đã qua sử dụng 3 năm', NOW(), NOW()),
    (1, 'BMI/2024/GD-001/KT', 'Máy cắt laser', 'TRUMPF', 'TruLaser 3030', 'TL3030-2019-1234', 'Đức', 'TRUMPF GmbH', 2019, 1, 'Cắt kim loại tấm chính xác cao', 'Tình trạng tốt', NOW(), NOW());

-- Tạo phiếu đánh giá hoàn tất
INSERT INTO inspection_completion_evaluations (dossier_id, inspection_number, evaluation_date, evaluator_user_id, supervisor_user_id, status, notes, created_at, updated_at)
VALUES 
    (1, 'BMI/2024/GD-001/KT', '2024-03-15', 2, 6, 'COMPLETED', 'Hoàn thành đầy đủ các bước giám định theo quy trình', NOW(), NOW());

-- Gán team inspection cho dossier
INSERT INTO dossier_inspection_team (dossier_id, user_id, execution_role_id, assigned_date, is_active, created_at, updated_at)
VALUES 
    (1, 3, 1, '2024-03-08', true, NOW(), NOW()), -- Trần Minh Đức - Trưởng nhóm
    (1, 4, 2, '2024-03-08', true, NOW(), NOW()), -- Lê Thị Hồng - Thành viên 
    (1, 5, 3, '2024-03-08', true, NOW(), NOW()), -- Phạm Văn Nam - Tập sự
    (1, 6, 3, '2024-03-08', true, NOW(), NOW()); -- Hoàng Thị Lan - Tập sự

-- Nhập kết quả đánh giá cho từng tiêu chí
-- Category A: Xem xét sự đầy đủ thành phần hồ sơ
INSERT INTO evaluation_results (evaluation_id, criteria_id, checkbox_value, text_value, notes, created_at, updated_at)
VALUES 
    (1, 1, true, 'Nguyễn Văn An - Chuyên viên', 'Hồ sơ đầy đủ theo checklist', NOW(), NOW()),
    (1, 2, true, NULL, 'Hoàn tất đăng ký trong 2 ngày làm việc', NOW(), NOW());

-- Category B: Công tác chuẩn bị giám định và giám định tại hiện trường
INSERT INTO evaluation_results (evaluation_id, criteria_id, checkbox_value, text_value, notes, created_at, updated_at)
VALUES 
    (1, 3, true, NULL, 'Xem xét kỹ hồ sơ và tuân thủ kế hoạch phân công', NOW(), NOW()),
    (1, 4, true, NULL, 'Chuẩn bị đầy đủ biểu mẫu và thiết bị đo lường', NOW(), NOW()),
    (1, 5, true, NULL, 'Tuân thủ đúng trình tự quy trình', NOW(), NOW()),
    (1, 6, true, NULL, 'Chụp ảnh đầy đủ theo quy định', NOW(), NOW()),
    (1, 7, true, NULL, 'Đúng giờ hẹn với khách hàng', NOW(), NOW());

-- Category C: Xử lý/ Báo cáo kết quả giám định  
INSERT INTO evaluation_results (evaluation_id, criteria_id, checkbox_value, text_value, notes, created_at, updated_at)
VALUES 
    (1, 8, true, 'Trần Minh Đức - Trưởng nhóm GĐV', 'Tuân thủ trình tự quy trình', NOW(), NOW()),
    (1, 9, true, NULL, 'Đối chiếu kỹ với tài liệu kỹ thuật', NOW(), NOW()),
    (1, 10, true, NULL, 'Soạn thảo và phê duyệt đúng trình tự', NOW(), NOW()),
    (1, 11, true, NULL, 'Hoàn thành đúng thời gian kế hoạch', NOW(), NOW());

-- Category D: Cấp chứng thư và lưu hồ sơ
INSERT INTO evaluation_results (evaluation_id, criteria_id, checkbox_value, text_value, notes, created_at, updated_at)
VALUES 
    (1, 12, true, 'Nguyễn Văn An - Chuyên viên', 'Tuân thủ trình tự quy trình', NOW(), NOW()),
    (1, 13, true, NULL, 'Lưu giữ hồ sơ đúng quy định', NOW(), NOW()),
    (1, 14, true, NULL, 'Đúng thời gian kế hoạch giám định', NOW(), NOW());

-- Nhập danh mục hồ sơ giám định (checklist documents)
-- Hồ sơ khách hàng cung cấp
INSERT INTO dossier_documents_checklist (evaluation_id, document_type_id, has_physical_copy, has_electronic_copy, electronic_file_path, notes, created_at, updated_at)
VALUES 
    (1, 1, true, true, '/server/documents/BMI-2024-GD-001-KT/01-giay-yeu-cau.pdf', NULL, NOW(), NOW()),
    (1, 2, true, true, '/server/documents/BMI-2024-GD-001-KT/02-phieu-chung-cau.pdf', NULL, NOW(), NOW()),
    (1, 3, true, true, '/server/documents/BMI-2024-GD-001-KT/03-packing-list.pdf', NULL, NOW(), NOW()),
    (1, 4, true, true, '/server/documents/BMI-2024-GD-001-KT/04-invoice.pdf', NULL, NOW(), NOW()),
    (1, 5, true, false, NULL, 'Chỉ có bản giấy', NOW(), NOW()),
    (1, 6, true, true, '/server/documents/BMI-2024-GD-001-KT/06-bill-of-loading.pdf', NULL, NOW(), NOW()),
    (1, 7, true, true, '/server/documents/BMI-2024-GD-001-KT/07-to-khai-hai-quan.pdf', NULL, NOW(), NOW()),
    (1, 8, true, false, NULL, 'Chỉ có bản giấy', NOW(), NOW()),
    (1, 9, true, true, '/server/documents/BMI-2024-GD-001-KT/09-tai-lieu-ky-thuat.pdf', NULL, NOW(), NOW()),
    (1, 10, true, false, NULL, 'Hồ sơ bảo dưỡng bản giấy', NOW(), NOW()),
    (1, 11, true, true, '/server/documents/BMI-2024-GD-001-KT/11-so-do-lap-dat.pdf', NULL, NOW(), NOW()),
    (1, 12, true, false, NULL, 'Xác nhận tiêu chuẩn bản giấy', NOW(), NOW()),
    (1, 13, true, true, '/server/documents/BMI-2024-GD-001-KT/13-tai-lieu-bo-sung.pdf', NULL, NOW(), NOW());

/* Hồ sơ BMI lập */
INSERT INTO dossier_documents_checklist (evaluation_id, document_type_id, has_physical_copy, has_electronic_copy, electronic_file_path, notes, created_at, updated_at)
VALUES 
    (1, 14, true, true, '/server/documents/BMI-2024-GD-001-KT/BMI-01-phieu-theo-doi.pdf', NULL, NOW(), NOW()),
    (1, 15, true, true, '/server/documents/BMI-2024-GD-001-KT/BMI-02-bao-cao-ket-qua.pdf', NULL, NOW(), NOW()),
    (1, 16, true, true, '/server/documents/BMI-2024-GD-001-KT/BMI-03-bien-ban-hien-truong.pdf', NULL, NOW(), NOW()),
    (1, 17, true, true, '/server/documents/BMI-2024-GD-001-KT/BMI-04-phieu-thu-thap-thong-tin.pdf', NULL, NOW(), NOW()),
    (1, 18, false, false, NULL, 'Không áp dụng - không có từ chối', NOW(), NOW()),
    (1, 19, false, true, '/server/documents/BMI-2024-GD-001-KT/BMI-06-hinh-anh/', 'Thư mục hình ảnh', NOW(), NOW()),
    (1, 20, false, true, '/server/documents/BMI-2024-GD-001-KT/BMI-07-tai-lieu-ngoai.pdf', NULL, NOW(), NOW()),
    (1, 21, true, true, '/server/documents/BMI-2024-GD-001-KT/BMI-08-chung-thu-giam-dinh.pdf', NULL, NOW(), NOW());

/* Thêm chữ ký điện tử */
INSERT INTO evaluation_signatures (evaluation_id, signature_type, user_id, signature_date, full_name, position, digital_signature, created_at, updated_at)
VALUES 
    (1, 'EVALUATOR', 2, '2024-03-15', 'Nguyễn Văn An', 'Chuyên viên', 'signature_hash_evaluator_123', NOW(), NOW()),
    (1, 'SUPERVISOR', 6, '2024-03-15', 'Trần Văn Bình', 'Phó Giám đốc', 'signature_hash_supervisor_456', NOW(), NOW());

/* Thêm kết quả inspection cho máy móc */
INSERT INTO inspection_results (machine_id, dossier_id, snapshot_registration_no, snapshot_item_name, snapshot_brand, snapshot_model, snapshot_serial_number, snapshot_manufacture_country, snapshot_manufacturer_name, snapshot_manufacture_year, snapshot_quantity, snapshot_usage, evaluation, created_at, updated_at)
VALUES 
    (1, 1, 'BMI/2024/GD-001/KT', 'Máy dập thủy lực', 'AMADA', 'TP-110', 'TP110-2020-5678', 'Nhật Bản', 'AMADA Co., Ltd', 2020, 1, 'Dập kim loại tấm trong sản xuất ô tô', 'Máy móc hoạt động bình thường, đáp ứng yêu cầu kỹ thuật. Tình trạng 85% so với máy mới.', NOW(), NOW()),
    (2, 1, 'BMI/2024/GD-001/KT', 'Máy cắt laser', 'TRUMPF', 'TruLaser 3030', 'TL3030-2019-1234', 'Đức', 'TRUMPF GmbH', 2019, 1, 'Cắt kim loại tấm chính xác cao', 'Máy hoạt động tốt, độ chính xác cao. Tình trạng 90% so với máy mới.', NOW(), NOW());

/*
========================================
QUERY MẪU ĐỂ XEM KẾT QUẢ
========================================
*/

-- Xem thông tin đầy đủ của phiếu đánh giá
/*
SELECT * FROM v_inspection_evaluations_full WHERE evaluation_id = 1;

-- Xem kết quả đánh giá chi tiết
SELECT * FROM v_evaluation_results_detail WHERE evaluation_id = 1 ORDER BY category_name, criteria_code;

-- Xem team inspection
SELECT * FROM v_dossier_inspection_teams WHERE dossier_id = 1;

-- Xem danh mục hồ sơ
SELECT 
    ddt.type_name,
    ddt.type_name_english,
    ddt.category,
    ddc.has_physical_copy,
    ddc.has_electronic_copy,
    ddc.electronic_file_path,
    ddc.notes
FROM dossier_documents_checklist ddc
JOIN dossier_document_types ddt ON ddc.document_type_id = ddt.document_type_id
WHERE ddc.evaluation_id = 1
ORDER BY ddt.category, ddt.display_order;

-- Xem chữ ký
SELECT 
    es.signature_type,
    es.full_name,
    es.position,
    es.signature_date,
    u.email
FROM evaluation_signatures es
JOIN users u ON es.user_id = u.user_id
WHERE es.evaluation_id = 1;
*/
