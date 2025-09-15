-- ==========================
-- Insert mặc định InspectionType
-- ==========================
INSERT INTO inspection_types (inspection_type_id, name, note, created_at, updated_at)
VALUES 
    ('01', 'GIÁM ĐỊNH MÁY MÓC, THIẾT BỊ VÀ DÂY CHUYỀN CÔNG NGHỆ ĐÃ QUA SỬ DỤNG', 'Kiểm tra an toàn', NOW(), NOW()),
    ('02', 'GIÁM ĐỊNH THƯƠNG MẠI', 'Kiểm tra chất lượng', NOW(), NOW()),
    ('03', 'GIÁM ĐỊNH MÔI TRƯỜNG', 'Kiểm tra môi trường', NOW(), NOW());

-- ==========================
-- Insert mặc định User (Admin)
-- ==========================
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

