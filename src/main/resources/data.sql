
# use electricStore;
-- Thêm role với chỉ 2 role
INSERT INTO role (role_name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_EMPLOYEE');

-- Thêm dữ liệu vào User (đã gộp với thông tin Employee)
INSERT INTO user (username, encryted_password, employee_email, created_at, updated_at,
                  employee_name, employee_birthday, employee_address, employee_phone, employee_code)
VALUES
    ('admin123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'admin@gmail.com', NOW(), NOW(),
     'Admin', '1990-01-01', 'Số 25 Phố Lý Thường Kiệt, Quận Hoàn Kiếm, Hà Nội', '0933000000', 'NV0001'),

    ('hoang123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'hoang12@gmail.com', NOW(), NOW(),
     'Nguyễn Hoàng', '2000-12-12', 'Số 78 Phố Thái Hà, Quận Đống Đa, Hà Nội', '0933371781', 'NV0002'),

    ('vanhau123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'vanhau12@gmail.com', NOW(), NOW(),
     'Trương Văn Hậu', '2001-09-12', 'Số 45 Phố Trung Kính, Quận Cầu Giấy, Hà Nội', '0955571781', 'NV0003'),

    ('tuantai345', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'tuantai12@gmail.com', NOW(), NOW(),
     'Nguyễn Tuấn Tài', '1998-12-12', 'Số 112 Phố Láng Hạ, Quận Ba Đình, Hà Nội', '0944441781', 'NV0004'),

    ('thitrang05', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'trang12@gmail.com', NOW(), NOW(),
     'Ngô Thị Trang', '2002-02-12', 'Số 63 Phố Nguyễn Chí Thanh, Quận Đống Đa, Hà Nội', '0955555781', 'NV0005'),

    ('hoaian678', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'hoaian123@gmail.com', NOW(), NOW(),
     'Lê Hoài An', '1995-05-15', 'Số 127 Đường Nguyễn Đình Chiểu, Quận 3, Thành phố Hồ Chí Minh', '0912345678', 'NV0006'),

    ('khiem980', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'khiem12@gmail.com', NOW(), NOW(),
     'Trần Minh Khiêm', '1999-08-23', 'Số 92 Đường Trần Phú, Quận Hải Châu, Đà Nẵng', '0987654321', 'NV0007'),

    ('nguyenduc123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'duc12@gmail.com', NOW(), NOW(),
     'Nguyễn Đức', '1997-11-30', 'Số 54 Đường Lạch Tray, Quận Ngô Quyền, Hải Phòng', '0977123456', 'NV0008'),

    ('phuongnha123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'phuong12@gmail.com', NOW(), NOW(),
     'Lê Phương Nha', '2000-04-17', 'Số 38 Đường 30/4, Quận Ninh Kiều, Cần Thơ', '0966789012', 'NV0009'),

    ('vantuan', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'tuan56@gmail.com', NOW(), NOW(),
     'Nguyễn Văn Tuấn', '1996-07-22', 'Số 15 Đường Lê Lợi, Phường Phú Nhuận, Thành phố Huế', '0944567890', 'NV0010'),

    ('thihuong456', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'huong456@gmail.com', NOW(), NOW(),
     'Phạm Thị Hương', '1993-10-05', 'Số 73 Đường Trần Phú, Phường Lộc Thọ, Nha Trang', '0922123456', 'NV0011'),

    ('quangminh789', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'minh789@gmail.com', NOW(), NOW(),
     'Đỗ Quang Minh', '1998-01-20', 'Số 28 Đường Lê Hồng Phong, Phường 4, Thành phố Vũng Tàu', '0911234567', 'NV0012'),

    ('thimai234', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'mai234@gmail.com', NOW(), NOW(),
     'Hoàng Thị Mai', '1994-06-28', 'Số 41 Đường Trần Hưng Đạo, Phường Hạ Long, Quảng Ninh', '0933456789', 'NV0013'),

    ('dinhtrung567', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'trung567@gmail.com', NOW(), NOW(),
     'Vũ Đình Trung', '1992-03-14', 'Số 84 Phố Trần Duy Hưng, Quận Cầu Giấy, Hà Nội', '0955678901', 'NV0014'),

    ('thithanh890', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'thanh890@gmail.com', NOW(), NOW(),
     'Trần Thị Thanh', '1997-09-08', 'Số 56 Đường Nguyễn Văn Linh, Quận Thanh Khê, Đà Nẵng', '0966890123', 'NV0015'),

    ('vanhung123', '$2a$10$y/odQPEQIU.RifEy1Steu.5ZuCmTRGQUB3ntp6.bWM/kw/29huOZK', 'hung123@gmail.com', NOW(), NOW(),
     'Nguyễn Văn Hùng', '1991-12-25', 'Số 189 Đường Điện Biên Phủ, Quận Bình Thạnh, Thành phố Hồ Chí Minh', '0988901234', 'NV0016');
-- Gán Roles cho Users
INSERT INTO user_role (user_id, role_id)
VALUES (1, 1),  -- admin123 có role ADMIN
       (2, 2),  -- hoang123 có role EMPLOYEE
       (3, 2),  -- vanhau123 có role EMPLOYEE
       (4, 2),  -- tuantai345 có role EMPLOYEE
       (5, 2),  -- thitrang05 có role EMPLOYEE
       (6, 2),  -- hoaian678 có role EMPLOYEE
       (7, 2),  -- khiem980 có role EMPLOYEE
       (8, 2),  -- nguyenduc123 có role EMPLOYEE
       (9, 2),  -- phuongnha123 có role EMPLOYEE
       (10, 2), -- vantuan có role EMPLOYEE
       (11, 2), -- thihuong456 có role EMPLOYEE
       (12, 2), -- quangminh789 có role EMPLOYEE
       (13, 2), -- thimai234 có role EMPLOYEE
       (14, 2), -- dinhtrung567 có role EMPLOYEE
       (15, 2), -- thithanh890 có role EMPLOYEE
       (16, 2); -- vanhung123 có role EMPLOYEE
# Thêm dữ liệu vào customer
INSERT INTO customers (customer_code, customer_name, phone_number, address, email, birth_date)
VALUES
    ('KH0001', 'Trần Hoài An', '0981828128', '15 Nguyễn Trãi, Thanh Xuân, Hà Nội', 'hoaian@gmail.com', '2002-06-12'),
    ('KH0002', 'Tuấn Khiêm', '0971218291', '28 Lê Duẩn, Hải Châu, Đà Nẵng', 'tuankhiem@gmail.com', '2002-06-12'),
    ('KH0003', 'Nguyễn Văn Đức', '0912118128', '56 Hùng Vương, Phú Nhuận, Huế', 'vanduc@gmail.com', '2002-06-14'),
    ('KH0004', 'Phương Nha', '0989129112', '103 Láng Hạ, Đống Đa, Hà Nội', 'phuongnha@gmail.com', '2001-06-12'),
    ('KH0005', 'Trần Văn Tuấn', '0912991991', '78 Lạch Tray, Ngô Quyền, Hải Phòng', 'vantuan@gmail.com', '2002-12-12'),
    ('KH0006', 'Nguyễn Thị Hoa', '0912345678', '42 Nguyễn Huệ, Quận 1, Hồ Chí Minh', 'hoa.nguyen@gmail.com', '1995-04-15'),
    ('KH0007', 'Lê Văn Minh', '0923456789', '211 Nguyễn Văn Cừ, Ninh Kiều, Cần Thơ', 'minhle@gmail.com', '1990-08-21'),
    ('KH0008', 'Trần Thị Lan', '0934567890', '17 Phan Đình Phùng, Phường 2, Đà Lạt', 'lantran@gmail.com', '1988-11-30'),
    ('KH0009', 'Phạm Quốc Bảo', '0945678901', '24 Trần Phú, Lộc Thọ, Nha Trang', 'baopham@gmail.com', '1993-02-25'),
    ('KH0010', 'Vũ Thị Thu', '0956789012', '89 Trương Công Định, Phường 3, Vũng Tàu', 'thuvu@gmail.com', '1997-07-10'),
    ('KH0011', 'Đỗ Văn Hùng', '0967890123', '125 Trần Hưng Đạo, Hạ Long, Quảng Ninh', 'hungdo@gmail.com', '1985-09-18'),
    ('KH0012', 'Nguyễn Thành Nam', '0978901234', '56 Nguyễn An Ninh, Dĩ An, Bình Dương', 'namnguyen@gmail.com', '1992-12-05'),
    ('KH0013', 'Mai Thị Linh', '0989012345', '38 Trương Định, Hai Bà Trưng, Hà Nội', 'linhmai@gmail.com', '1998-03-22'),
    ('KH0014', 'Hoàng Văn Thắng', '0990123456', '72 Bạch Đằng, Phường Trần Phú, Hải Dương', 'thanghoang@gmail.com', '1994-05-17'),
    ('KH0015', 'Trần Thị Thảo', '0901234567', '19 Lê Lợi, Phường Điện Biên, Thanh Hóa', 'thaotran@gmail.com', '1996-10-08');
# Thêm dữ liệu vào categories (chỉ giữ 3 danh mục)
INSERT INTO categories (category_code, category_name, description, create_at, update_at)
VALUES ('DM0001', 'Điện thoại', 'Các loại điện thoại di động thông minh', NOW(), NOW()),
       ('DM0002', 'Laptop', 'Máy tính xách tay các loại', NOW(), NOW()),
       ('DM0003', 'Máy tính bảng', 'Các loại máy tính bảng', NOW(), NOW()),
       ('DM0004', 'Đồng hồ thông minh', 'Smartwatch và các thiết bị đeo', NOW(), NOW()),
       ('DM0005', 'Tai nghe', 'Tai nghe có dây và không dây', NOW(), NOW()),
       ('DM0006', 'Loa', 'Loa bluetooth và loa thông minh', NOW(), NOW()),
       ('DM0007', 'Sạc dự phòng', 'Pin dự phòng và các thiết bị sạc', NOW(), NOW()),
       ('DM0008', 'Phụ kiện', 'Các phụ kiện điện tử khác', NOW(), NOW()),
       ('DM0009', 'Máy tính để bàn', 'PC và các linh kiện máy tính', NOW(), NOW()),
       ('DM0010', 'Thiết bị mạng', 'Router, modem và thiết bị kết nối mạng', NOW(), NOW());

# Thêm dữ liệu vào brand (giữ nguyên)
INSERT INTO brands (brand_code, brand_name, country, create_at, update_at)
VALUES ('TH0001', 'Apple', 'Mỹ', NOW(), NOW()),
       ('TH0002', 'Samsung', 'Hàn Quốc', NOW(), NOW()),
       ('TH0003', 'Sony', 'Nhật Bản', NOW(), NOW()),
       ('TH0004', 'Asus', 'Đài Loan', NOW(), NOW()),
       ('TH0005', 'Dell', 'Mỹ', NOW(), NOW()),
       ('TH0006', 'Xiaomi', 'Trung Quốc', NOW(), NOW()),
       ('TH0007', 'Huawei', 'Trung Quốc', NOW(), NOW()),
       ('TH0008', 'LG', 'Hàn Quốc', NOW(), NOW()),
       ('TH0009', 'Oppo', 'Trung Quốc', NOW(), NOW()),
       ('TH0010', 'Lenovo', 'Trung Quốc', NOW(), NOW());

# Thêm dữ liệu vào supplier (giữ nguyên)
INSERT INTO suppliers (supplier_code, supplier_name, address, phone, email, create_at, update_at)
VALUES ('NCC0001', 'Apple Store', 'California, USA', '0981921280', 'apple@gmail.com', NOW(), NOW()),
       ('NCC0002', 'Samsung Store', 'Seoul, Korea', '0911921281', 'samsung@gmail.com', NOW(), NOW()),
       ('NCC0003', 'Oppo Store', 'Guangdong, China', '0981921182', 'oppo@gmail.com', NOW(), NOW()),
       ('NCC0004', 'Xiaomi Store', 'Beijing, China', '0001921283', 'xiaomi@gmail.com', NOW(), NOW()),
       ('NCC0005', 'Sony Distributor', 'Tokyo, Japan', '0981921284', 'sony@gmail.com', NOW(), NOW()),
       ('NCC0006', 'Asus Vietnam', 'Hanoi, Vietnam', '0981921285', 'asus@gmail.com', NOW(), NOW()),
       ('NCC0007', 'Dell Vietnam', 'HCMC, Vietnam', '0981921286', 'dell@gmail.com', NOW(), NOW()),
       ('NCC0008', 'Huawei Store', 'Shenzhen, China', '0981921287', 'huawei@gmail.com', NOW(), NOW()),
       ('NCC0009', 'LG Electronics', 'Seoul, Korea', '0981921288', 'lg@gmail.com', NOW(), NOW()),
       ('NCC0010', 'Lenovo Shop', 'Shanghai, China', '0981921289', 'lenovo@gmail.com', NOW(), NOW());
# Thêm dữ liệu vào products (sửa lại mã sản phẩm theo SP0001, SP0002, ...)
INSERT INTO products (product_id, product_name, main_image_url, description, price, stock, create_at, update_at, brand_id, category_id, supplier_id, product_code)
VALUES
-- Smartphones (Danh mục 1: Điện thoại)
(1, 'Xiaomi Redmi Note 12 Pro', 'https://byvn.net/2qP6', 'Xiaomi Redmi Note 12 Pro - Hiệu năng ổn định', 8990000.0, 100, NOW(), NOW(), 6, 1, 4, 'SP0001'),
(2, 'Realme GT Neo 5', 'https://byvn.net/kvZe', 'Realme GT Neo 5 - Sạc siêu nhanh', 10990000.0, 80, NOW(), NOW(), 9, 1, 9, 'SP0002'),
(3, 'Oppo Find X5 Pro', 'https://byvn.net/XHnW', 'Oppo Find X5 Pro - Thiết kế sang trọng', 19990000.0, 50, NOW(), NOW(), 9, 1, 3, 'SP0003'),
(4, 'Vivo X90 Pro', 'https://byvn.net/3hgh', 'Vivo X90 Pro - Camera chuyên nghiệp', 22990000.0, 60, NOW(), NOW(), 1, 1, 1, 'SP0004'),
(5, 'Honor Magic5 Pro', 'https://byvn.net/rhiq', 'Honor Magic5 Pro - Công nghệ cao cấp', 20990000.0, 45, NOW(), NOW(), 1, 1, 1, 'SP0005'),
(6, 'Samsung Galaxy S23 Ultra', 'https://byvn.net/s23u', 'Samsung Galaxy S23 Ultra - Flagship hàng đầu', 29990000.0, 40, NOW(), NOW(), 2, 1, 2, 'SP0006'),
(7, 'iPhone 15 Pro Max', 'https://byvn.net/ip15pm', 'iPhone 15 Pro Max - Đỉnh cao công nghệ', 33990000.0, 55, NOW(), NOW(), 1, 1, 1, 'SP0007'),
(8, 'Xiaomi 14 Ultra', 'https://byvn.net/xm14u', 'Xiaomi 14 Ultra - Camera đỉnh cao', 25990000.0, 35, NOW(), NOW(), 6, 1, 4, 'SP0008'),

-- Tablets (Danh mục 2: Máy tính bảng)
(9, 'Huawei MatePad Pro 11', 'https://byvn.net/U9pt', 'Huawei MatePad Pro 11 - Màn hình sắc nét', 16990000.0, 70, NOW(), NOW(), 7, 2, 8, 'SP0009'),
(10, 'Xiaomi Pad 6 Pro', 'https://byvn.net/RX3m', 'Xiaomi Pad 6 Pro - Hiệu năng mạnh mẽ', 14990000.0, 65, NOW(), NOW(), 6, 2, 4, 'SP0010'),
(11, 'Realme Pad 2', 'https://byvn.net/E4HL', 'Realme Pad 2 - Giải trí đa năng', 8990000.0, 90, NOW(), NOW(), 9, 2, 3, 'SP0011'),
(12, 'iPad Pro M2 12.9', 'https://byvn.net/ipadm2', 'iPad Pro M2 12.9 - Hiệu suất vượt trội', 31990000.0, 40, NOW(), NOW(), 1, 2, 1, 'SP0012'),
(13, 'Samsung Galaxy Tab S9 Ultra', 'https://byvn.net/tabs9u', 'Samsung Galaxy Tab S9 Ultra - Màn hình lớn', 26990000.0, 30, NOW(), NOW(), 2, 2, 2, 'SP0013'),
(14, 'Lenovo Tab P12 Pro', 'https://byvn.net/lenovop12', 'Lenovo Tab P12 Pro - Giải trí đỉnh cao', 17990000.0, 25, NOW(), NOW(), 10, 2, 10, 'SP0014'),

-- Laptops (Danh mục 3: Laptop)
(15, 'HP Spectre x360', 'https://byvn.net/KNjw', 'HP Spectre x360 - Laptop 2 trong 1', 39990000.0, 40, NOW(), NOW(), 1, 3, 1, 'SP0015'),
(16, 'Acer Predator Helios 300', 'https://byvn.net/9cmk', 'Acer Predator Helios 300 - Gaming', 35990000.0, 50, NOW(), NOW(), 1, 3, 1, 'SP0016'),
(17, 'Lenovo ThinkPad X1 Carbon', 'https://byvn.net/6Mva', 'Lenovo ThinkPad X1 Carbon - Doanh nhân', 45990000.0, 35, NOW(), NOW(), 10, 3, 10, 'SP0017'),
(18, 'Microsoft Surface Laptop 5', 'https://byvn.net/Ga38', 'Microsoft Surface Laptop 5 - Sang trọng', 37990000.0, 45, NOW(), NOW(), 1, 3, 1, 'SP0018'),
(19, 'MacBook Pro 16 M3 Max', 'https://byvn.net/mbpm3', 'MacBook Pro 16 M3 Max - Mạnh mẽ', 89990000.0, 20, NOW(), NOW(), 1, 3, 1, 'SP0019'),
(20, 'Dell XPS 15', 'https://byvn.net/dellxps15', 'Dell XPS 15 - Thiết kế cao cấp', 55990000.0, 30, NOW(), NOW(), 5, 3, 7, 'SP0020'),
(21, 'Asus ROG Zephyrus G16', 'https://byvn.net/asusrog', 'Asus ROG Zephyrus G16 - Gaming cao cấp', 49990000.0, 25, NOW(), NOW(), 4, 3, 6, 'SP0021'),
(22, 'MSI Creator Z16', 'https://byvn.net/msicre', 'MSI Creator Z16 - Dành cho sáng tạo', 51990000.0, 15, NOW(), NOW(), 1, 3, 1, 'SP0022');
# Thêm dữ liệu vào ProductDetail với cấu trúc mới
INSERT INTO product_details (screen_size, camera, front_camera, color, description, cpu, gpu, ram, rom, os, os_version, battery, screen_type, screen_resolution, ports, weight, create_at, update_at, product_id)
VALUES
-- Điện thoại
(6.67, 108, 16, 'Xanh', 'Màn hình AMOLED 120Hz, camera AI', 'MediaTek Dimensity 1080', 'Mali-G68 MC4', '8GB', '256GB', 'Android', '12 (MIUI 14)', '5000mAh', 'AMOLED', '2400x1080', 'USB Type-C, 3.5mm jack', 187, NOW(), NOW(), 1),
(6.74, 50, 16, 'Đen', 'Sạc siêu nhanh 240W, màn hình 144Hz', 'Snapdragon 8+ Gen 1', 'Adreno 730', '12GB', '256GB', 'Android', '13 (Realme UI 4.0)', '4600mAh', 'AMOLED', '2772x1240', 'USB Type-C', 199, NOW(), NOW(), 2),
(6.7, 50, 32, 'Trắng', 'Camera Hasselblad, màn hình LTPO', 'Snapdragon 8 Gen 1', 'Adreno 730', '12GB', '256GB', 'Android', '12 (ColorOS 12.1)', '5000mAh', 'AMOLED', '3216x1440', 'USB Type-C', 218, NOW(), NOW(), 3),
(6.78, 50, 32, 'Xám', 'Camera Zeiss, cảm biến 1-inch', 'MediaTek Dimensity 9200', 'Immortalis-G715 MC11', '12GB', '256GB', 'Android', '13 (Funtouch OS 13)', '4870mAh', 'AMOLED', '2800x1260', 'USB Type-C', 215, NOW(), NOW(), 4),
(6.81, 50, 12, 'Xanh dương', 'Camera 200MP, màn hình LTPO', 'Snapdragon 8 Gen 2', 'Adreno 740', '12GB', '512GB', 'Android', '13 (Magic UI 7.0)', '5100mAh', 'OLED', '2848x1312', 'USB Type-C', 219, NOW(), NOW(), 5),
(6.8, 200, 12, 'Đen Phantom', 'Bút S-Pen, zoom quang 10x', 'Snapdragon 8 Gen 2', 'Adreno 740', '12GB', '512GB', 'Android', '13 (One UI 5.1)', '5000mAh', 'Dynamic AMOLED 2X', '3088x1440', 'USB Type-C', 234, NOW(), NOW(), 6),
(6.7, 48, 12, 'Titan Tự nhiên', 'Khung titan, Dynamic Island', 'A17 Pro', 'Apple GPU (6-core)', '8GB', '512GB', 'iOS', '17', '4422mAh', 'Super Retina XDR OLED', '2796x1290', 'USB Type-C', 221, NOW(), NOW(), 7),
(6.73, 50, 32, 'Đen', 'Camera Leica, chụp đêm tốt', 'Snapdragon 8 Gen 2', 'Adreno 740', '16GB', '512GB', 'Android', '14 (MIUI 15)', '5000mAh', 'AMOLED', '3200x1440', 'USB Type-C', 227, NOW(), NOW(), 8),

-- Máy tính bảng
(11.0, 13, 8, 'Xám', 'Hỗ trợ bút M-Pencil', 'Snapdragon 888', 'Adreno 660', '8GB', '256GB', 'HarmonyOS', '3.0', '8300mAh', 'OLED', '2560x1600', 'USB Type-C', 449, NOW(), NOW(), 9),
(11.2, 50, 20, 'Bạc', 'Hỗ trợ bút, tần số quét 144Hz', 'Snapdragon 8+ Gen 1', 'Adreno 730', '8GB', '256GB', 'Android', '13 (MIUI 14)', '8600mAh', 'IPS LCD', '2880x1800', 'USB Type-C', 490, NOW(), NOW(), 10),
(11.5, 8, 5, 'Xanh lá', 'Màn hình lớn, pin khỏe', 'MediaTek Helio G99', 'Mali-G57 MC2', '8GB', '256GB', 'Android', '13 (Realme UI 4.0)', '8360mAh', 'IPS LCD', '2000x1200', 'USB Type-C, 3.5mm jack', 538, NOW(), NOW(), 11),
(12.9, 12, 12, 'Space Gray', 'Chip M2, màn hình mini-LED', 'Apple M2', 'Apple M2 GPU (10-core)', '16GB', '1TB', 'iPadOS', '17', '10758mAh', 'Liquid Retina XDR', '2732x2048', 'USB Type-C', 682, NOW(), NOW(), 12),
(14.6, 13, 12, 'Graphite', 'Màn hình lớn nhất, S-Pen', 'Snapdragon 8 Gen 2', 'Adreno 740', '12GB', '512GB', 'Android', '13 (One UI 5.1)', '11200mAh', 'Dynamic AMOLED 2X', '2960x1848', 'USB Type-C', 732, NOW(), NOW(), 13),
(12.6, 13, 8, 'Storm Gray', 'Màn hình AMOLED rộng', 'Snapdragon 8 Gen 1', 'Adreno 730', '8GB', '256GB', 'Android', '13', '10200mAh', 'AMOLED', '2560x1600', 'USB Type-C, 3.5mm jack', 565, NOW(), NOW(), 14),

-- Laptop
(13.5, NULL, NULL, 'Xanh Navy', 'Laptop xoay gập 360 độ', 'Intel Core i7-1355U', 'Intel Iris Xe', '16GB', '1TB SSD', 'Windows', '11 Home', '66Wh', 'OLED Touch', '3000x2000', '2x Thunderbolt 4, USB-A, 3.5mm jack', 1360, NOW(), NOW(), 15),
(15.6, NULL, NULL, 'Đen', 'Laptop gaming hiệu năng cao', 'Intel Core i7-12700H', 'NVIDIA RTX 3070 Ti 8GB', '16GB', '1TB SSD', 'Windows', '11 Home', '90Wh', 'IPS, 165Hz', '2560x1440', '3x USB-A, USB-C, HDMI, Ethernet, 3.5mm jack', 2400, NOW(), NOW(), 16),
(14.0, NULL, NULL, 'Đen', 'Laptop doanh nhân cao cấp', 'Intel Core i7-1360P', 'Intel Iris Xe', '16GB', '1TB SSD', 'Windows', '11 Pro', '57Wh', 'IPS', '2880x1800', '2x Thunderbolt 4, USB-A, HDMI, 3.5mm jack', 1120, NOW(), NOW(), 17),
(13.5, NULL, NULL, 'Bạch kim', 'Thiết kế sang trọng, pin dài', 'Intel Core i7-1255U', 'Intel Iris Xe', '16GB', '512GB SSD', 'Windows', '11 Home', '47.4Wh', 'PixelSense Touch', '2256x1504', 'USB-A, Thunderbolt 4, 3.5mm jack', 1270, NOW(), NOW(), 18),
(16.2, NULL, NULL, 'Space Black', 'Hiệu năng mạnh mẽ, màn hình mini-LED', 'Apple M3 Max (16-core)', 'Apple M3 Max (40-core)', '64GB', '2TB SSD', 'macOS', 'Sonoma', '100Wh', 'Liquid Retina XDR, 120Hz', '3456x2234', '3x Thunderbolt 4, HDMI, SD card, 3.5mm jack', 2170, NOW(), NOW(), 19),
(15.6, NULL, NULL, 'Platinum', 'Thiết kế mỏng nhẹ, màn hình OLED', 'Intel Core i9-13900H', 'NVIDIA RTX 4070 8GB', '32GB', '1TB SSD', 'Windows', '11 Pro', '86Wh', 'OLED', '3456x2160', '2x Thunderbolt 4, USB-A, HDMI, SD card, 3.5mm jack', 1920, NOW(), NOW(), 20),
(16.0, NULL, NULL, 'Off Black', 'Laptop gaming cao cấp', 'Intel Core i9-13900H', 'NVIDIA RTX 4080 12GB', '32GB', '2TB SSD', 'Windows', '11 Pro', '90Wh', 'ROG Nebula Display, 240Hz', '2560x1600', '2x USB-C, 2x USB-A, HDMI, LAN, 3.5mm jack', 2100, NOW(), NOW(), 21),
(16.0, NULL, NULL, 'Xám Core Black', 'Laptop cho nhà sáng tạo', 'Intel Core i7-12700H', 'NVIDIA RTX 3070 8GB', '32GB', '1TB SSD', 'Windows', '11 Pro', '90Wh', 'Touch', '2560x1600', '2x Thunderbolt 4, USB-A, HDMI, SD card, 3.5mm jack', 2350, NOW(), NOW(), 22);

# Thêm dữ liệu vào ware_house với giá nhập phù hợp (thấp hơn giá bán khoảng 20-30%)
INSERT INTO ware_house (import_date, price, product_id) VALUES
('2023-11-15', 6990000, 1),  -- Xiaomi Redmi Note 12 Pro (giá bán: 8990000)
('2023-11-20', 8490000, 2),  -- Realme GT Neo 5 (giá bán: 10990000)
('2023-12-05', 15990000, 3), -- Oppo Find X5 Pro (giá bán: 19990000)
('2023-12-10', 17990000, 4), -- Vivo X90 Pro (giá bán: 22990000)
('2023-12-15', 16990000, 5), -- Honor Magic5 Pro (giá bán: 20990000)
('2024-01-05', 23990000, 6), -- Samsung Galaxy S23 Ultra (giá bán: 29990000)
('2024-01-10', 27990000, 7), -- iPhone 15 Pro Max (giá bán: 33990000)
('2024-01-20', 20990000, 8), -- Xiaomi 14 Ultra (giá bán: 25990000)
('2024-02-01', 13490000, 9), -- Huawei MatePad Pro 11 (giá bán: 16990000)
('2024-02-10', 11990000, 10), -- Xiaomi Pad 6 Pro (giá bán: 14990000)
('2024-02-15', 6990000, 11),  -- Realme Pad 2 (giá bán: 8990000)
('2024-02-25', 26990000, 12), -- iPad Pro M2 12.9 (giá bán: 31990000)
('2024-03-01', 21990000, 13), -- Samsung Galaxy Tab S9 Ultra (giá bán: 26990000)
('2024-03-05', 14490000, 14), -- Lenovo Tab P12 Pro (giá bán: 17990000)
('2024-03-10', 32990000, 15), -- HP Spectre x360 (giá bán: 39990000)
('2024-03-15', 29990000, 16), -- Acer Predator Helios 300 (giá bán: 35990000)
('2024-03-20', 38990000, 17), -- Lenovo ThinkPad X1 Carbon (giá bán: 45990000)
('2024-03-25', 31990000, 18), -- Microsoft Surface Laptop 5 (giá bán: 37990000)
('2024-04-01', 75990000, 19), -- MacBook Pro 16 M3 Max (giá bán: 89990000)
('2024-04-05', 46990000, 20), -- Dell XPS 15 (giá bán: 55990000)
('2024-04-10', 41990000, 21), -- Asus ROG Zephyrus G16 (giá bán: 49990000)
('2024-04-15', 43990000, 22); -- MSI Creator Z16 (giá bán: 51990000)

-- Thêm dữ liệu cho bảng order_products
INSERT INTO order_products (order_code, total_price, discount, discount_percent, status, create_at, update_at, customer_id)
VALUES
-- Quý 1/2023
('HD00001', 8990000, 500000, 5.56, 'COMPLETE', '2023-01-05 08:30:00', '2023-01-05 15:45:00', 1),
('HD00002', 10990000, 0, 0, 'COMPLETE', '2023-01-12 10:15:00', '2023-01-12 16:20:00', 3),
('HD00003', 19990000, 1000000, 5.00, 'COMPLETE', '2023-01-18 14:20:00', '2023-01-18 18:10:00', 5),
('HD00004', 35980000, 2000000, 5.56, 'COMPLETE', '2023-01-25 09:45:00', '2023-01-25 14:30:00', 2),
('HD00005', 22990000, 1500000, 6.52, 'COMPLETE', '2023-02-03 11:30:00', '2023-02-03 17:00:00', 7),
('HD00006', 58980000, 5000000, 8.48, 'COMPLETE', '2023-02-10 15:45:00', '2023-02-10 20:15:00', 4),
('HD00007', 16990000, 800000, 4.71, 'COMPLETE', '2023-02-17 13:20:00', '2023-02-17 16:50:00', 9),
('HD00008', 33990000, 2500000, 7.35, 'COMPLETE', '2023-02-24 10:10:00', '2023-02-24 15:30:00', 6),
('HD00009', 8990000, 0, 0, 'COMPLETE', '2023-03-02 16:40:00', '2023-03-02 19:15:00', 8),
('HD00010', 45980000, 4000000, 8.70, 'COMPLETE', '2023-03-09 09:25:00', '2023-03-09 14:40:00', 10),

-- Quý 2/2023
('HD00011', 20990000, 1000000, 4.76, 'COMPLETE', '2023-04-05 14:15:00', '2023-04-05 18:30:00', 11),
('HD00012', 49980000, 3500000, 7.00, 'COMPLETE', '2023-04-12 11:30:00', '2023-04-12 16:45:00', 13),
('HD00013', 26990000, 1200000, 4.45, 'COMPLETE', '2023-04-19 10:20:00', '2023-04-19 14:10:00', 15),
('HD00014', 63980000, 5000000, 7.81, 'COMPLETE', '2023-04-26 15:45:00', '2023-04-26 19:20:00', 12),
('HD00015', 14990000, 500000, 3.34, 'COMPLETE', '2023-05-03 09:10:00', '2023-05-03 13:30:00', 14),
('HD00016', 31990000, 1500000, 4.69, 'CANCELLED', '2023-05-10 16:20:00', '2023-05-10 17:45:00', 1),
('HD00017', 34980000, 2000000, 5.72, 'COMPLETE', '2023-05-17 13:45:00', '2023-05-17 18:10:00', 3),
('HD00018', 25990000, 1200000, 4.62, 'COMPLETE', '2023-05-24 10:30:00', '2023-05-24 15:20:00', 5),
('HD00019', 76980000, 6000000, 7.80, 'COMPLETE', '2023-06-01 11:15:00', '2023-06-01 16:40:00', 7),
('HD00020', 39990000, 2000000, 5.00, 'COMPLETE', '2023-06-08 15:30:00', '2023-06-08 19:45:00', 9),

-- Quý 3/2023
('HD00021', 45990000, 2500000, 5.44, 'COMPLETE', '2023-07-05 10:10:00', '2023-07-05 14:30:00', 2),
('HD00022', 51990000, 3000000, 5.77, 'COMPLETE', '2023-07-12 13:45:00', '2023-07-12 18:15:00', 4),
('HD00023', 17990000, 800000, 4.45, 'COMPLETE', '2023-07-19 16:20:00', '2023-07-19 19:50:00', 6),
('HD00024', 89990000, 7000000, 7.78, 'COMPLETE', '2023-07-26 09:30:00', '2023-07-26 14:45:00', 8),
('HD00025', 55990000, 4000000, 7.14, 'COMPLETE', '2023-08-02 14:15:00', '2023-08-02 18:40:00', 10),
('HD00026', 29990000, 1500000, 5.00, 'COMPLETE', '2023-08-09 11:30:00', '2023-08-09 16:20:00', 12),
('HD00027', 22990000, 1000000, 4.35, 'COMPLETE', '2023-08-16 15:45:00', '2023-08-16 19:10:00', 14),
('HD00028', 63980000, 4500000, 7.03, 'CANCELLED', '2023-08-23 10:20:00', '2023-08-23 11:30:00', 1),
('HD00029', 37990000, 2000000, 5.26, 'COMPLETE', '2023-08-30 13:10:00', '2023-08-30 17:30:00', 3),
('HD00030', 43990000, 2500000, 5.68, 'COMPLETE', '2023-09-06 16:45:00', '2023-09-06 20:15:00', 5),

-- Quý 4/2023
('HD00031', 49990000, 3000000, 6.00, 'COMPLETE', '2023-10-04 09:30:00', '2023-10-04 14:45:00', 7),
('HD00032', 10990000, 500000, 4.55, 'COMPLETE', '2023-10-11 14:15:00', '2023-10-11 18:30:00', 9),
('HD00033', 147980000, 10000000, 6.76, 'COMPLETE', '2023-10-18 11:30:00', '2023-10-18 16:45:00', 11),
('HD00034', 57980000, 4000000, 6.90, 'COMPLETE', '2023-10-25 15:20:00', '2023-10-25 19:40:00', 13),
('HD00035', 87990000, 6000000, 6.82, 'COMPLETE', '2023-11-01 10:10:00', '2023-11-01 15:30:00', 15),
('HD00036', 31990000, 1500000, 4.69, 'COMPLETE', '2023-11-08 13:45:00', '2023-11-08 18:10:00', 2),
('HD00037', 28980000, 1200000, 4.14, 'COMPLETE', '2023-11-15 16:20:00', '2023-11-15 20:00:00', 4),
('HD00038', 67980000, 4500000, 6.62, 'COMPLETE', '2023-11-22 09:30:00', '2023-11-22 14:45:00', 6),
('HD00039', 37990000, 2000000, 5.26, 'COMPLETE', '2023-11-29 14:15:00', '2023-11-29 18:40:00', 8),
('HD00040', 33990000, 1800000, 5.30, 'COMPLETE', '2023-12-06 11:30:00', '2023-12-06 16:20:00', 10),
('HD00041', 143980000, 12000000, 8.33, 'COMPLETE', '2023-12-13 15:45:00', '2023-12-13 20:10:00', 12),
('HD00042', 49990000, 3000000, 6.00, 'COMPLETE', '2023-12-20 10:20:00', '2023-12-20 15:30:00', 14),
('HD00043', 104970000, 8000000, 7.62, 'CANCELLED', '2023-12-27 13:10:00', '2023-12-27 14:20:00', 1),

-- Quý 1/2024
('HD00044', 19990000, 1000000, 5.00, 'COMPLETE', '2024-01-03 16:45:00', '2024-01-03 20:15:00', 3),
('HD00045', 89990000, 6000000, 6.67, 'COMPLETE', '2024-01-10 09:30:00', '2024-01-10 14:50:00', 5),
('HD00046', 39990000, 2000000, 5.00, 'COMPLETE', '2024-01-17 14:15:00', '2024-01-17 18:40:00', 7),
('HD00047', 55990000, 3500000, 6.25, 'COMPLETE', '2024-01-24 11:30:00', '2024-01-24 16:20:00', 9),
('HD00048', 16990000, 800000, 4.71, 'COMPLETE', '2024-01-31 15:45:00', '2024-01-31 19:10:00', 11),
('HD00049', 143970000, 10000000, 6.95, 'COMPLETE', '2024-02-07 10:20:00', '2024-02-07 15:30:00', 13),
('HD00050', 47980000, 2500000, 5.21, 'COMPLETE', '2024-02-14 13:10:00', '2024-02-14 17:30:00', 15),
('HD00051', 35990000, 1800000, 5.00, 'COMPLETE', '2024-02-21 16:45:00', '2024-02-21 20:10:00', 2),
('HD00052', 52980000, 3000000, 5.66, 'COMPLETE', '2024-02-28 09:30:00', '2024-02-28 14:45:00', 4),
('HD00053', 29990000, 1500000, 5.00, 'COMPLETE', '2024-03-06 14:15:00', '2024-03-06 18:40:00', 6),
('HD00054', 67980000, 4000000, 5.88, 'COMPLETE', '2024-03-13 11:30:00', '2024-03-13 16:15:00', 8),
('HD00055', 22990000, 1000000, 4.35, 'COMPLETE', '2024-03-20 15:40:00', '2024-03-20 19:10:00', 10),
('HD00056', 10990000, 500000, 4.55, 'COMPLETE', '2024-03-27 10:20:00', '2024-03-27 14:50:00', 12),

-- Quý 2/2024
('HD00057', 37990000, 2000000, 5.26, 'COMPLETE', '2024-04-03 13:10:00', '2024-04-03 17:30:00', 14),
('HD00058', 49990000, 2500000, 5.00, 'COMPLETE', '2024-04-10 16:45:00', '2024-04-10 20:15:00', 1),
('HD00059', 126970000, 9000000, 7.09, 'COMPLETE', '2024-04-17 09:30:00', '2024-04-17 14:45:00', 3),
('HD00060', 51990000, 3000000, 5.77, 'COMPLETE', '2024-04-24 14:20:00', '2024-04-24 18:40:00', 5),
('HD00061', 45990000, 2500000, 5.44, 'COMPLETE', '2024-05-01 11:30:00', '2024-05-01 16:20:00', 7),
('HD00062', 16990000, 800000, 4.71, 'COMPLETE', '2024-05-08 15:45:00', '2024-05-08 19:10:00', 9),
('HD00063', 20990000, 1000000, 4.76, 'COMPLETE', '2024-05-15 10:20:00', '2024-05-15 14:50:00', 11),
('HD00064', 82980000, 5000000, 6.03, 'COMPLETE', '2024-05-22 13:10:00', '2024-05-22 17:30:00', 13),
('HD00065', 25990000, 1200000, 4.62, 'COMPLETE', '2024-05-29 16:45:00', '2024-05-29 20:10:00', 15),
('HD00066', 101970000, 7000000, 6.86, 'CANCELLED', '2024-06-05 09:30:00', '2024-06-05 10:45:00', 2),
('HD00067', 143980000, 10000000, 6.94, 'COMPLETE', '2024-06-12 14:15:00', '2024-06-12 18:40:00', 4),
('HD00068', 63980000, 4000000, 6.25, 'COMPLETE', '2024-06-19 11:30:00', '2024-06-19 16:15:00', 6),
('HD00069', 34980000, 1800000, 5.15, 'COMPLETE', '2024-06-26 15:40:00', '2024-06-26 19:10:00', 8),

-- Quý 3/2024
('HD00070', 17990000, 900000, 5.00, 'COMPLETE', '2024-07-03 10:20:00', '2024-07-03 14:50:00', 10),
('HD00071', 125970000, 8000000, 6.35, 'COMPLETE', '2024-07-10 13:10:00', '2024-07-10 17:30:00', 12),
('HD00072', 37990000, 2000000, 5.26, 'COMPLETE', '2024-07-17 16:45:00', '2024-07-17 20:15:00', 14),
('HD00073', 28980000, 1500000, 5.18, 'COMPLETE', '2024-07-24 09:30:00', '2024-07-24 14:45:00', 1),
('HD00074', 46980000, 2500000, 5.32, 'COMPLETE', '2024-07-31 14:20:00', '2024-07-31 18:40:00', 3),
('HD00075', 33990000, 1500000, 4.41, 'COMPLETE', '2024-08-07 11:30:00', '2024-08-07 16:15:00', 5),
('HD00076', 136970000, 9000000, 6.57, 'COMPLETE', '2024-08-14 15:40:00', '2024-08-14 19:10:00', 7),
('HD00077', 49990000, 2500000, 5.00, 'COMPLETE', '2024-08-21 10:20:00', '2024-08-21 14:50:00', 9),
('HD00078', 55990000, 3000000, 5.36, 'COMPLETE', '2024-08-28 13:10:00', '2024-08-28 17:30:00', 11),
('HD00079', 26990000, 1300000, 4.82, 'COMPLETE', '2024-09-04 16:45:00', '2024-09-04 20:10:00', 13),
('HD00080', 86980000, 5500000, 6.32, 'CANCELLED', '2024-09-11 09:30:00', '2024-09-11 10:45:00', 15),
('HD00081', 43990000, 2200000, 5.00, 'COMPLETE', '2024-09-18 14:15:00', '2024-09-18 18:40:00', 2),
('HD00082', 31990000, 1600000, 5.00, 'COMPLETE', '2024-09-25 11:30:00', '2024-09-25 16:15:00', 4),

-- Quý 4/2024 (hiện tại)
('HD00083', 56980000, 3000000, 5.26, 'COMPLETE', '2024-10-02 15:40:00', '2024-10-02 19:10:00', 6),
('HD00084', 20990000, 1000000, 4.76, 'COMPLETE', '2024-10-09 10:20:00', '2024-10-09 14:50:00', 8),
('HD00085', 146980000, 10000000, 6.80, 'COMPLETE', '2024-10-16 13:10:00', '2024-10-16 17:30:00', 10),
('HD00086', 35990000, 1800000, 5.00, 'COMPLETE', '2024-10-23 16:45:00', '2024-10-23 20:15:00', 12),
('HD00087', 51990000, 2600000, 5.00, 'COMPLETE', '2024-10-30 09:30:00', NULL, 14),
('HD00088', 96980000, 6000000, 6.19, 'COMPLETE', '2024-11-06 14:20:00', NULL, 1),
('HD00089', 29990000, 1500000, 5.00, 'COMPLETE', '2024-11-13 11:30:00', NULL, 3),
('HD00090', 34980000, 1700000, 4.86, 'COMPLETE', '2024-11-20 15:40:00', NULL, 5),
('HD00091', 19990000, 1000000, 5.00, 'COMPLETE', '2024-11-27 10:20:00', NULL, 7),
('HD00092', 63980000, 3200000, 5.00, 'COMPLETE', '2024-12-04 13:10:00', NULL, 9),
('HD00093', 33990000, 1700000, 5.00, 'COMPLETE', '2024-12-11 16:45:00', NULL, 11),
('HD00094', 37990000, 1900000, 5.00, 'COMPLETE', '2024-12-18 09:30:00', NULL, 13),
('HD00095', 89990000, 4500000, 5.00, 'COMPLETE', '2024-12-25 14:15:00', NULL, 15),

-- Quý 1/2025 (hiện tại)
('HD00096', 55990000, 2800000, 5.00, 'COMPLETE', '2025-01-01 11:30:00', NULL, 2),
('HD00097', 18990000, 950000, 5.00, 'COMPLETE', '2025-01-08 15:40:00', NULL, 4),
('HD00098', 163970000, 8200000, 5.00, 'COMPLETE', '2025-01-15 10:20:00', NULL, 6),
('HD00099', 49990000, 2500000, 5.00, 'COMPLETE', '2025-01-22 13:10:00', NULL, 8),
('HD00100', 104980000, 5250000, 5.00, 'COMPLETE', '2025-01-29 16:45:00', NULL, 10),

('HD00101', 37990000, 1900000, 5.00, 'COMPLETE', '2025-02-05 08:30:00', '2025-02-05 15:45:00', 11),
('HD00102', 53980000, 2700000, 5.00, 'COMPLETE', '2025-02-07 10:15:00', '2025-02-07 16:20:00', 13),
('HD00103', 29990000, 1500000, 5.00, 'COMPLETE', '2025-02-09 14:20:00', '2025-02-09 18:10:00', 15),
('HD00104', 125970000, 6300000, 5.00, 'COMPLETE', '2025-02-11 09:45:00', '2025-02-11 14:30:00', 1),
('HD00105', 45990000, 2300000, 5.00, 'COMPLETE', '2025-02-13 11:30:00', '2025-02-13 17:00:00', 3),
('HD00106', 83980000, 4200000, 5.00, 'COMPLETE', '2025-02-15 15:45:00', '2025-02-15 20:15:00', 5),
('HD00107', 22990000, 1150000, 5.00, 'COMPLETE', '2025-02-17 13:20:00', '2025-02-17 16:50:00', 7),
('HD00108', 19990000, 1000000, 5.00, 'COMPLETE', '2025-02-19 10:10:00', '2025-02-19 15:30:00', 9),
('HD00109', 55990000, 2800000, 5.00, 'COMPLETE', '2025-02-21 16:40:00', '2025-02-21 19:15:00', 11),
('HD00110', 67980000, 3400000, 5.00, 'COMPLETE', '2025-02-23 09:25:00', '2025-02-23 14:40:00', 13),
('HD00111', 31990000, 1600000, 5.00, 'COMPLETE', '2025-02-25 14:15:00', '2025-02-25 18:30:00', 15),
('HD00112', 89990000, 4500000, 5.00, 'CANCELLED', '2025-02-27 11:30:00', '2025-02-27 12:45:00', 2),

-- Tháng 3/2025
('HD00113', 43990000, 2200000, 5.00, 'COMPLETE', '2025-03-01 10:20:00', '2025-03-01 14:10:00', 4),
('HD00114', 79980000, 4000000, 5.00, 'COMPLETE', '2025-03-03 15:45:00', '2025-03-03 19:20:00', 6),
('HD00115', 28980000, 1450000, 5.00, 'COMPLETE', '2025-03-05 09:10:00', '2025-03-05 13:30:00', 8),
('HD00116', 61980000, 3100000, 5.00, 'COMPLETE', '2025-03-07 16:20:00', '2025-03-07 20:45:00', 10),
('HD00117', 41980000, 2100000, 5.00, 'COMPLETE', '2025-03-09 13:45:00', '2025-03-09 18:10:00', 12),
('HD00118', 35990000, 1800000, 5.00, 'COMPLETE', '2025-03-11 10:30:00', '2025-03-11 15:20:00', 14),
('HD00119', 96980000, 4850000, 5.00, 'COMPLETE', '2025-03-13 11:15:00', '2025-03-13 16:40:00', 1),
('HD00120', 49990000, 2500000, 5.00, 'COMPLETE', '2025-03-15 15:30:00', '2025-03-15 19:45:00', 3),
('HD00121', 51990000, 2600000, 5.00, 'COMPLETE', '2025-03-17 10:10:00', '2025-03-17 14:30:00', 5),
('HD00122', 25990000, 1300000, 5.00, 'COMPLETE', '2025-03-19 13:45:00', '2025-03-19 18:15:00', 7),
('HD00123', 143970000, 7200000, 5.00, 'COMPLETE', '2025-03-21 16:20:00', '2025-03-21 19:50:00', 9),
('HD00124', 59980000, 3000000, 5.00, 'COMPLETE', '2025-03-23 09:30:00', '2025-03-23 14:45:00', 11),
('HD00125', 37990000, 1900000, 5.00, 'COMPLETE', '2025-03-25 14:15:00', '2025-03-25 18:40:00', 13),
('HD00126', 17990000, 900000, 5.00, 'CANCELLED', '2025-03-27 11:30:00', '2025-03-27 12:15:00', 15),
('HD00127', 8990000, 450000, 5.00, 'COMPLETE', '2025-03-29 15:45:00', '2025-03-29 19:10:00', 2),
('HD00128', 33990000, 1700000, 5.00, 'COMPLETE', '2025-03-31 10:20:00', '2025-03-31 15:30:00', 4),

-- Tháng 4/2025
('HD00129', 65980000, 3300000, 5.00, 'COMPLETE', '2025-04-02 13:10:00', '2025-04-02 17:30:00', 6),
('HD00130', 87990000, 4400000, 5.00, 'COMPLETE', '2025-04-04 16:45:00', '2025-04-04 20:15:00', 8),
('HD00131', 26990000, 1350000, 5.00, 'COMPLETE', '2025-04-06 09:30:00', '2025-04-06 14:45:00', 10),
('HD00132', 20990000, 1050000, 5.00, 'COMPLETE', '2025-04-08 14:20:00', '2025-04-08 18:40:00', 12),
('HD00133', 102980000, 5150000, 5.00, 'COMPLETE', '2025-04-10 11:30:00', '2025-04-10 16:20:00', 14),
('HD00134', 45990000, 2300000, 5.00, 'CANCELLED', '2025-04-12 15:45:00', '2025-04-12 16:30:00', 1),
('HD00135', 19990000, 1000000, 5.00, 'COMPLETE', '2025-04-14 10:20:00', '2025-04-14 14:50:00', 3),
('HD00136', 47980000, 2400000, 5.00, 'COMPLETE', '2025-04-16 13:10:00', '2025-04-16 17:30:00', 5),
('HD00137', 73980000, 3700000, 5.00, 'COMPLETE', '2025-04-18 16:45:00', '2025-04-18 20:10:00', 7),
('HD00138', 55990000, 2800000, 5.00, 'COMPLETE', '2025-04-20 09:30:00', '2025-04-20 14:45:00', 9),
('HD00139', 107980000, 5400000, 5.00, 'COMPLETE', '2025-04-22 14:15:00', '2025-04-22 18:40:00', 11),
('HD00140', 39990000, 2000000, 5.00, 'COMPLETE', '2025-04-24 11:30:00', '2025-04-24 16:15:00', 13),
('HD00141', 43990000, 2200000, 5.00, 'CANCELLED', '2025-04-26 15:40:00', '2025-04-26 16:25:00', 15),
('HD00142', 29990000, 1500000, 5.00, 'COMPLETE', '2025-04-28 10:20:00', '2025-04-28 14:50:00', 2),
('HD00143', 89990000, 4500000, 5.00, 'COMPLETE', '2025-04-30 13:10:00', '2025-04-30 17:30:00', 4),

-- Quý 2/2025
('HD00144', 16990000, 850000, 5.00, 'COMPLETE', '2025-05-02 09:30:00', '2025-05-02 14:45:00', 6),
('HD00145', 51990000, 2600000, 5.00, 'COMPLETE', '2025-05-03 14:15:00', '2025-05-03 18:40:00', 8),
('HD00146', 35990000, 1800000, 5.00, 'COMPLETE', '2025-05-04 11:30:00', '2025-05-04 16:20:00', 10),
('HD00147', 22990000, 1150000, 5.00, 'COMPLETE', '2025-05-05 15:45:00', '2025-05-05 19:10:00', 12),
('HD00148', 37990000, 1900000, 5.00, 'COMPLETE', '2025-05-06 10:20:00', '2025-05-06 14:50:00', 14),
('HD00149', 161970000, 8100000, 5.00, 'COMPLETE', '2025-05-07 13:10:00', '2025-05-07 17:30:00', 1),
('HD00150', 25990000, 1300000, 5.00, 'COMPLETE', '2025-05-08 16:45:00', '2025-05-08 20:10:00', 3),
('HD00151', 15990000, 800000, 5.00, 'COMPLETE', '2025-05-09 09:30:00', '2025-05-09 14:45:00', 5),
('HD00152', 43990000, 2200000, 5.00, 'COMPLETE', '2025-05-10 14:15:00', '2025-05-10 18:40:00', 7),
('HD00153', 31990000, 1600000, 5.00, 'COMPLETE', '2025-05-11 11:30:00', '2025-05-11 16:15:00', 9),
('HD00154', 55990000, 2800000, 5.00, 'COMPLETE', '2025-05-12 15:40:00', '2025-05-12 19:10:00', 11),
('HD00155', 10990000, 550000, 5.00, 'COMPLETE', '2025-05-13 10:20:00', '2025-05-13 14:50:00', 13),
('HD00156', 19990000, 1000000, 5.00, 'COMPLETE', '2025-05-14 13:10:00', '2025-05-14 17:30:00', 15),
('HD00157', 33990000, 1700000, 5.00, 'COMPLETE', '2025-05-15 16:45:00', '2025-05-15 20:10:00', 2),
('HD00158', 89990000, 4500000, 5.00, 'COMPLETE', '2025-05-16 09:30:00', '2025-05-16 14:45:00', 4),
('HD00159', 29990000, 1500000, 5.00, 'COMPLETE', '2025-05-17 14:15:00', '2025-05-17 18:40:00', 6),
('HD00160', 73980000, 3700000, 5.00, 'CANCELLED', '2025-05-18 11:30:00', '2025-05-18 12:15:00', 8),
('HD00161', 49990000, 2500000, 5.00, 'COMPLETE', '2025-05-19 15:40:00', '2025-05-19 19:10:00', 10),
('HD00162', 22990000, 1150000, 5.00, 'COMPLETE', '2025-05-20 10:20:00', '2025-05-20 14:50:00', 12),
('HD00163', 143970000, 7200000, 5.00, 'COMPLETE', '2025-05-21 13:10:00', '2025-05-21 17:30:00', 14),
('HD00164', 37990000, 1900000, 5.00, 'COMPLETE', '2025-05-22 16:45:00', '2025-05-22 20:10:00', 1),
('HD00165', 83980000, 4200000, 5.00, 'COMPLETE', '2025-05-23 09:30:00', '2025-05-23 14:45:00', 3),
('HD00166', 55990000, 2800000, 5.00, 'COMPLETE', '2025-05-24 14:15:00', '2025-05-24 18:40:00', 5),
('HD00167', 20990000, 1050000, 5.00, 'COMPLETE', '2025-05-25 11:30:00', '2025-05-25 16:15:00', 7),
('HD00168', 22990000, 1150000, 5.00, 'COMPLETE', '2025-05-26 15:40:00', '2025-05-26 19:10:00', 9),
('HD00169', 78980000, 3950000, 5.00, 'COMPLETE', '2025-05-27 10:20:00', '2025-05-27 14:50:00', 11),
('HD00170', 26990000, 1350000, 5.00, 'COMPLETE', '2025-05-28 13:10:00', '2025-05-28 17:30:00', 13),
('HD00171', 149970000, 7500000, 5.00, 'COMPLETE', '2025-05-29 16:45:00', '2025-05-29 20:10:00', 15),
('HD00172', 31990000, 1600000, 5.00, 'COMPLETE', '2025-05-30 09:30:00', '2025-05-30 14:45:00', 2),

('HD00173', 49990000, 2500000, 5.00, 'COMPLETE', '2025-06-01 14:15:00', '2025-06-01 18:40:00', 4),
('HD00174', 63980000, 3200000, 5.00, 'COMPLETE', '2025-06-02 11:30:00', '2025-06-02 16:15:00', 6),
('HD00175', 16990000, 850000, 5.00, 'COMPLETE', '2025-06-03 15:40:00', '2025-06-03 19:10:00', 8),
('HD00176', 69980000, 3500000, 5.00, 'COMPLETE', '2025-06-04 10:20:00', '2025-06-04 14:50:00', 10),
('HD00177', 39990000, 2000000, 5.00, 'COMPLETE', '2025-06-05 13:10:00', '2025-06-05 17:30:00', 12),
('HD00178', 127970000, 6400000, 5.00, 'CANCELLED', '2025-06-06 16:45:00', '2025-06-06 17:30:00', 14),
('HD00179', 35990000, 1800000, 5.00, 'COMPLETE', '2025-06-07 09:30:00', '2025-06-07 14:45:00', 1),
('HD00180', 43990000, 2200000, 5.00, 'COMPLETE', '2025-06-08 14:15:00', '2025-06-08 18:40:00', 3),
('HD00181', 55990000, 2800000, 5.00, 'COMPLETE', '2025-06-09 11:30:00', '2025-06-09 16:15:00', 5),
('HD00182', 22990000, 1150000, 5.00, 'COMPLETE', '2025-06-10 15:40:00', '2025-06-10 19:10:00', 7),
('HD00183', 19990000, 1000000, 5.00, 'COMPLETE', '2025-06-11 10:20:00', '2025-06-11 14:50:00', 9),
('HD00184', 87990000, 4400000, 5.00, 'COMPLETE', '2025-06-12 13:10:00', '2025-06-12 17:30:00', 11),
('HD00185', 89990000, 4500000, 5.00, 'COMPLETE', '2025-06-13 16:45:00', '2025-06-13 20:10:00', 13),
('HD00186', 22990000, 1150000, 5.00, 'COMPLETE', '2025-06-14 09:30:00', '2025-06-14 14:45:00', 15),
('HD00187', 131970000, 6600000, 5.00, 'COMPLETE', '2025-06-15 14:15:00', '2025-06-15 18:40:00', 2),
('HD00188', 37990000, 1900000, 5.00, 'COMPLETE', '2025-06-16 11:30:00', '2025-06-16 16:15:00', 4),
('HD00189', 8990000, 450000, 5.00, 'COMPLETE', '2025-06-17 15:40:00', '2025-06-17 19:10:00', 6),
('HD00190', 49990000, 2500000, 5.00, 'COMPLETE', '2025-06-18 10:20:00', '2025-06-18 14:50:00', 8),
('HD00191', 25990000, 1300000, 5.00, 'COMPLETE', '2025-06-19 13:10:00', '2025-06-19 17:30:00', 10),
('HD00192', 31990000, 1600000, 5.00, 'COMPLETE', '2025-06-20 16:45:00', '2025-06-20 20:10:00', 12),
('HD00193', 55990000, 2800000, 5.00, 'COMPLETE', '2025-06-21 09:30:00', '2025-06-21 14:45:00', 14),
('HD00194', 22990000, 1150000, 5.00, 'COMPLETE', '2025-06-22 14:15:00', '2025-06-22 18:40:00', 1),
('HD00195', 35990000, 1800000, 5.00, 'COMPLETE', '2025-06-23 11:30:00', '2025-06-23 16:15:00', 3),
('HD00196', 89990000, 4500000, 5.00, 'COMPLETE', '2025-06-24 15:40:00', '2025-06-24 19:10:00', 5),
('HD00197', 43990000, 2200000, 5.00, 'CANCELLED', '2025-06-25 10:20:00', '2025-06-25 11:05:00', 7),
('HD00198', 29990000, 1500000, 5.00, 'COMPLETE', '2025-06-26 13:10:00', '2025-06-26 17:30:00', 9),
('HD00199', 16990000, 850000, 5.00, 'COMPLETE', '2025-06-28 16:45:00', '2025-06-28 20:10:00', 11),
('HD00200', 145970000, 7300000, 5.00, 'COMPLETE', '2025-06-30 09:30:00', '2025-06-30 14:45:00', 13);

-- Thêm dữ liệu cho bảng order_details
INSERT INTO order_details (quantity, price, product_id, order_id)
VALUES
-- Chi tiết cho đơn hàng 1-10
(1, 8990000, 1, 1),
(1, 10990000, 2, 2),
(1, 19990000, 3, 3),
(1, 22990000, 4, 4),
(1, 12990000, 5, 4),
(1, 22990000, 4, 5),
(2, 29990000, 6, 6),
(1, 16990000, 9, 7),
(1, 33990000, 7, 8),
(1, 8990000, 1, 9),
(1, 19990000, 3, 10),
(1, 25990000, 8, 10),

-- Chi tiết cho đơn hàng 11-20
(1, 20990000, 5, 11),
(1, 31990000, 12, 12),
(1, 17990000, 10, 12),
(1, 26990000, 13, 13),
(1, 33990000, 7, 14),
(1, 29990000, 6, 14),
(1, 14990000, 10, 15),
(1, 31990000, 12, 16),
(1, 19990000, 3, 17),
(1, 14990000, 10, 17),
(1, 25990000, 8, 18),
(1, 22990000, 4, 19),
(1, 19990000, 3, 19),
(1, 33990000, 7, 19),
(1, 39990000, 15, 20),

-- Chi tiết cho đơn hàng 21-30
(1, 45990000, 17, 21),
(1, 51990000, 22, 22),
(1, 17990000, 10, 23),
(1, 89990000, 19, 24),
(1, 55990000, 20, 25),
(1, 29990000, 6, 26),
(1, 22990000, 4, 27),
(1, 33990000, 7, 28),
(1, 29990000, 6, 28),
(1, 37990000, 18, 29),
(1, 43990000, 21, 30),

-- Chi tiết cho đơn hàng 31-40
(1, 49990000, 21, 31),
(1, 10990000, 2, 32),
(1, 55990000, 20, 33),
(1, 37990000, 18, 33),
(1, 19990000, 3, 33),
(1, 33990000, 7, 33),
(1, 33990000, 7, 34),
(1, 23990000, 6, 34),
(1, 87990000, 19, 35),
(1, 31990000, 12, 36),
(1, 14990000, 10, 37),
(1, 13990000, 11, 37),
(1, 33990000, 7, 38),
(1, 33990000, 7, 38),
(1, 37990000, 18, 39),
(1, 33990000, 7, 40),
(1, 31990000, 12, 41),
(1, 33990000, 7, 41),
(1, 37990000, 18, 41),
(1, 39990000, 15, 41),
(1, 49990000, 21, 42),
(1, 25990000, 8, 43),
(1, 25990000, 8, 43),
(1, 26990000, 13, 43),
(1, 25990000, 8, 43),

-- Chi tiết cho đơn hàng 44-53
(1, 19990000, 3, 44),
(1, 89990000, 19, 45),
(1, 39990000, 15, 46),
(1, 55990000, 20, 47),
(1, 16990000, 9, 48),
(1, 55990000, 20, 49),
(1, 31990000, 12, 49),
(1, 19990000, 3, 49),
(1, 35990000, 16, 49),
(1, 33990000, 7, 50),
(1, 13990000, 11, 50),
(1, 35990000, 16, 51),
(1, 22990000, 4, 52),
(1, 29990000, 6, 52),
(1, 29990000, 6, 53),
(1, 33990000, 7, 54),
(1, 33990000, 7, 54),
(1, 22990000, 4, 55),
(1, 10990000, 2, 56),

-- Chi tiết cho đơn hàng 57-66
(1, 37990000, 18, 57),
(1, 49990000, 21, 58),
(1, 31990000, 12, 59),
(1, 31990000, 12, 59),
(1, 19990000, 3, 59),
(1, 43990000, 21, 59),
(1, 51990000, 22, 60),
(1, 45990000, 17, 61),
(1, 16990000, 9, 62),
(1, 20990000, 5, 63),
(1, 25990000, 8, 64),
(1, 19990000, 3, 64),
(1, 36990000, 16, 64),
(1, 25990000, 8, 65),
(1, 25990000, 8, 66),
(1, 25990000, 8, 66),
(1, 49990000, 21, 66),
(1, 33990000, 7, 67),
(1, 37990000, 18, 67),
(1, 31990000, 12, 67),
(1, 39990000, 15, 67),
(1, 33990000, 7, 68),
(1, 29990000, 6, 68),
(1, 19990000, 3, 69),
(1, 14990000, 10, 69),

-- Chi tiết cho đơn hàng 70-79
(1, 17990000, 10, 70),
(1, 37990000, 18, 71),
(1, 31990000, 12, 71),
(1, 19990000, 3, 71),
(1, 35990000, 16, 71),
(1, 37990000, 18, 72),
(1, 14990000, 10, 73),
(1, 13990000, 11, 73),
(1, 10990000, 2, 74),
(1, 35990000, 16, 74),
(1, 33990000, 7, 75),
(1, 89990000, 19, 76),
(1, 25990000, 8, 76),
(1, 20990000, 5, 76),
(1, 49990000, 21, 77),
(1, 55990000, 20, 78),
(1, 26990000, 13, 79),

-- Chi tiết cho đơn hàng 80-89
(1, 43990000, 21, 80),
(1, 43990000, 21, 80),
(1, 43990000, 21, 81),
(1, 31990000, 12, 82),
(1, 33990000, 7, 83),
(1, 22990000, 4, 83),
(1, 20990000, 5, 84),
(1, 31990000, 12, 85),
(1, 37990000, 18, 85),
(1, 37990000, 18, 85),
(1, 39990000, 15, 85),
(1, 35990000, 16, 86),
(1, 51990000, 22, 87),
(1, 51990000, 22, 88),
(1, 44990000, 21, 88),
(1, 29990000, 6, 89),

-- Chi tiết cho đơn hàng 90-100
(1, 19990000, 3, 90),
(1, 14990000, 10, 90),
(1, 19990000, 3, 91),
(1, 33990000, 7, 92),
(1, 29990000, 6, 92),
(1, 33990000, 7, 93),
(1, 37990000, 18, 94),
(1, 89990000, 19, 95),
(1, 55990000, 20, 96),
(1, 18990000, 11, 97),
(1, 37990000, 18, 98),
(1, 31990000, 12, 98),
(1, 25990000, 8, 98),
(1, 31990000, 12, 98),
(1, 35990000, 16, 98),
(1, 49990000, 21, 99),
(1, 51990000, 22, 100),
(1, 19990000, 3, 100),
(1, 33990000, 7, 100),
(1, 37990000, 18, 101),
(1, 22990000, 4, 102),
(1, 30990000, 6, 102),
(1, 29990000, 6, 103),
(1, 37990000, 18, 104),
(1, 55990000, 20, 104),
(1, 31990000, 12, 104),
(1, 45990000, 17, 105),
(1, 33990000, 7, 106),
(1, 49990000, 21, 106),
(1, 22990000, 4, 107),
(1, 19990000, 3, 108),
(1, 55990000, 20, 109),
(1, 33990000, 7, 110),
(1, 33990000, 7, 110),

-- Chi tiết cho đơn hàng 111-120
(1, 31990000, 12, 111),
(1, 89990000, 19, 112),
(1, 43990000, 21, 113),
(1, 49990000, 21, 114),
(1, 29990000, 6, 114),
(1, 14990000, 10, 115),
(1, 13990000, 11, 115),
(1, 31990000, 12, 116),
(1, 29990000, 6, 116),
(1, 22990000, 4, 117),
(1, 18990000, 11, 117),
(1, 35990000, 16, 118),
(1, 25990000, 8, 119),
(1, 19990000, 3, 119),
(1, 51000000, 22, 119),
(1, 49990000, 21, 120),

-- Chi tiết cho đơn hàng 121-130
(1, 51990000, 22, 121),
(1, 25990000, 8, 122),
(1, 37990000, 18, 123),
(1, 35990000, 16, 123),
(1, 69990000, 19, 123),
(1, 29990000, 6, 124),
(1, 29990000, 6, 124),
(1, 37990000, 18, 125),
(1, 17990000, 10, 126),
(1, 8990000, 1, 127),
(1, 33990000, 7, 128),
(1, 35990000, 16, 129),
(1, 29990000, 6, 129),
(1, 87990000, 19, 130),

-- Chi tiết cho đơn hàng 131-140
(1, 26990000, 13, 131),
(1, 20990000, 5, 132),
(1, 51990000, 22, 133),
(1, 50990000, 21, 133),
(1, 45990000, 17, 134),
(1, 19990000, 3, 135),
(1, 33990000, 7, 136),
(1, 13990000, 11, 136),
(1, 39990000, 15, 137),
(1, 33990000, 7, 137),
(1, 55990000, 20, 138),
(1, 51990000, 22, 139),
(1, 55990000, 20, 139),
(1, 39990000, 15, 140),

-- Chi tiết cho đơn hàng 141-150
(1, 43990000, 21, 141),
(1, 29990000, 6, 142),
(1, 89990000, 19, 143),
(1, 16990000, 9, 144),
(1, 51990000, 22, 145),
(1, 35990000, 16, 146),
(1, 22990000, 4, 147),
(1, 37990000, 18, 148),
(1, 55990000, 20, 149),
(1, 39990000, 15, 149),
(1, 65990000, 19, 149),
(1, 25990000, 8, 150),

-- Chi tiết cho đơn hàng 151-160
(1, 15990000, 9, 151),
(1, 43990000, 21, 152),
(1, 31990000, 12, 153),
(1, 55990000, 20, 154),
(1, 10990000, 2, 155),
(1, 19990000, 3, 156),
(1, 33990000, 7, 157),
(1, 89990000, 19, 158),
(1, 29990000, 6, 159),
(1, 43990000, 21, 160),
(1, 29990000, 6, 160),

-- Chi tiết cho đơn hàng 161-170
(1, 49990000, 21, 161),
(1, 22990000, 4, 162),
(1, 55990000, 20, 163),
(1, 51990000, 22, 163),
(1, 35990000, 16, 163),
(1, 37990000, 18, 164),
(1, 33990000, 7, 165),
(1, 49990000, 21, 165),
(1, 55990000, 20, 166),
(1, 20990000, 5, 167),
(1, 22990000, 4, 168),
(1, 25990000, 8, 169),
(1, 52990000, 22, 169),
(1, 26990000, 13, 170),

-- Chi tiết cho đơn hàng 171-180
(1, 89990000, 19, 171),
(1, 59980000, 22, 171),
(1, 31990000, 12, 172),
(1, 49990000, 21, 173),
(1, 33990000, 7, 174),
(1, 29990000, 6, 174),
(1, 16990000, 9, 175),
(1, 39990000, 15, 176),
(1, 29990000, 6, 176),
(1, 39990000, 15, 177),
(1, 51990000, 22, 178),
(1, 75980000, 19, 178),
(1, 35990000, 16, 179),
(1, 43990000, 21, 180),

-- Chi tiết cho đơn hàng 181-190
(1, 55990000, 20, 181),
(1, 22990000, 4, 182),
(1, 19990000, 3, 183),
(1, 87990000, 19, 184),
(1, 89990000, 19, 185),
(1, 22990000, 4, 186),
(1, 55990000, 20, 187),
(1, 75980000, 19, 187),
(1, 37990000, 18, 188),
(1, 8990000, 1, 189),
(1, 49990000, 21, 190),

-- Chi tiết cho đơn hàng 191-200
(1, 25990000, 8, 191),
(1, 31990000, 12, 192),
(1, 55990000, 20, 193),
(1, 22990000, 4, 194),
(1, 35990000, 16, 195),
(1, 89990000, 19, 196),
(1, 43990000, 21, 197),
(1, 29990000, 6, 198),
(1, 16990000, 9, 199),
(1, 89990000, 19, 200),
(1, 55980000, 20, 200);

-- Thêm dữ liệu thanh toán cho 200 đơn hàng
INSERT INTO payments (amount, payment_method, create_at, order_id) VALUES
(8490000, 'CASH', '2023-01-05 08:30:00', 1),
(10990000, 'ONLINE_BANKING', '2023-01-12 10:15:00', 2),
(18990000, 'CASH', '2023-01-18 14:20:00', 3),
 (33980000, 'ONLINE_BANKING', '2023-01-25 09:45:00', 4),
 (21490000, 'CASH', '2023-02-03 11:30:00', 5),
 (53980000, 'ONLINE_BANKING', '2023-02-10 15:45:00', 6),
 (16190000, 'CASH', '2023-02-17 13:20:00', 7),
 (31490000, 'ONLINE_BANKING', '2023-02-24 10:10:00', 8),
 (8990000, 'CASH', '2023-03-02 16:40:00', 9),
 (41980000, 'ONLINE_BANKING', '2023-03-09 09:25:00', 10),
 (19990000, 'CASH', '2023-04-05 14:15:00', 11),
 (46480000, 'ONLINE_BANKING', '2023-04-12 11:30:00', 12),
 (25790000, 'CASH', '2023-04-19 10:20:00', 13),
 (58980000, 'ONLINE_BANKING', '2023-04-26 15:45:00', 14),
 (14490000, 'CASH', '2023-05-03 09:10:00', 15),
 (30490000, 'ONLINE_BANKING', '2023-05-10 16:20:00', 16),
 (32980000, 'CASH', '2023-05-17 13:45:00', 17),
 (24790000, 'ONLINE_BANKING', '2023-05-24 10:30:00', 18),
 (70980000, 'CASH', '2023-06-01 11:15:00', 19),
 (37990000, 'ONLINE_BANKING', '2023-06-08 15:30:00', 20),
 (43490000, 'CASH', '2023-07-05 10:10:00', 21),
 (48990000, 'ONLINE_BANKING', '2023-07-12 13:45:00', 22),
 (17190000, 'CASH', '2023-07-19 16:20:00', 23),
 (82990000, 'ONLINE_BANKING', '2023-07-26 09:30:00', 24),
 (51990000, 'CASH', '2023-08-02 14:15:00', 25),
 (28490000, 'ONLINE_BANKING', '2023-08-09 11:30:00', 26),
 (21990000, 'CASH', '2023-08-16 15:45:00', 27),
 (59480000, 'ONLINE_BANKING', '2023-08-23 10:20:00', 28),
 (35990000, 'CASH', '2023-08-30 13:10:00', 29),
 (41490000, 'ONLINE_BANKING', '2023-09-06 16:45:00', 30),
(46990000, 'CASH', '2023-10-04 09:30:00', 31),
(10490000, 'ONLINE_BANKING', '2023-10-11 14:15:00', 32),
(137980000, 'CASH', '2023-10-18 11:30:00', 33),
(53980000, 'ONLINE_BANKING', '2023-10-25 15:20:00', 34),
(81990000, 'CASH', '2023-11-01 10:10:00', 35),
(30490000, 'ONLINE_BANKING', '2023-11-08 13:45:00', 36),
(27780000, 'CASH', '2023-11-15 16:20:00', 37),
(63480000, 'ONLINE_BANKING', '2023-11-22 09:30:00', 38),
(35990000, 'CASH', '2023-11-29 14:15:00', 39),
(32190000, 'ONLINE_BANKING', '2023-12-06 11:30:00', 40),
(131980000, 'CASH', '2023-12-13 15:45:00', 41),
(46990000, 'ONLINE_BANKING', '2023-12-20 10:20:00', 42),
(96970000, 'CASH', '2023-12-27 13:10:00', 43),
(18990000, 'ONLINE_BANKING', '2024-01-03 16:45:00', 44),
(83990000, 'CASH', '2024-01-10 09:30:00', 45),
(37990000, 'ONLINE_BANKING', '2024-01-17 14:15:00', 46),
(52490000, 'CASH', '2024-01-24 11:30:00', 47),
(16190000, 'ONLINE_BANKING', '2024-01-31 15:45:00', 48),
(133970000, 'CASH', '2024-02-07 10:20:00', 49),
(45480000, 'ONLINE_BANKING', '2024-02-14 13:10:00', 50),
(34190000, 'CASH', '2024-02-21 16:45:00', 51),
(49980000, 'ONLINE_BANKING', '2024-02-28 09:30:00', 52),
(28490000, 'CASH', '2024-03-06 14:15:00', 53),
(63980000, 'ONLINE_BANKING', '2024-03-13 11:30:00', 54),
(21990000, 'CASH', '2024-03-20 15:40:00', 55),
(10490000, 'ONLINE_BANKING', '2024-03-27 10:20:00', 56),
(35990000, 'CASH', '2024-04-03 13:10:00', 57),
(47490000, 'ONLINE_BANKING', '2024-04-10 16:45:00', 58),
(117970000, 'CASH', '2024-04-17 09:30:00', 59),
(48990000, 'ONLINE_BANKING', '2024-04-24 14:20:00', 60),
(43490000, 'CASH', '2024-05-01 11:30:00', 61),
(16190000, 'ONLINE_BANKING', '2024-05-08 15:45:00', 62),
(19990000, 'CASH', '2024-05-15 10:20:00', 63),
(77980000, 'ONLINE_BANKING', '2024-05-22 13:10:00', 64),
(24790000, 'CASH', '2024-05-29 16:45:00', 65),
(94970000, 'ONLINE_BANKING', '2024-06-05 09:30:00', 66),
(133980000, 'CASH', '2024-06-12 14:15:00', 67),
(59980000, 'ONLINE_BANKING', '2024-06-19 11:30:00', 68),
(33180000, 'CASH', '2024-06-26 15:40:00', 69),
(17090000, 'ONLINE_BANKING', '2024-07-03 10:20:00', 70),
(117970000, 'CASH', '2024-07-10 13:10:00', 71),
(35990000, 'ONLINE_BANKING', '2024-07-17 16:45:00', 72),
(27480000, 'CASH', '2024-07-24 09:30:00', 73),
(44480000, 'ONLINE_BANKING', '2024-07-31 14:20:00', 74),
(32490000, 'CASH', '2024-08-07 11:30:00', 75),
(127970000, 'ONLINE_BANKING', '2024-08-14 15:40:00', 76),
(47490000, 'CASH', '2024-08-21 10:20:00', 77),
(52990000, 'ONLINE_BANKING', '2024-08-28 13:10:00', 78),
(25690000, 'CASH', '2024-09-04 16:45:00', 79),
(81480000, 'ONLINE_BANKING', '2024-09-11 09:30:00', 80),
(41790000, 'CASH', '2024-09-18 14:15:00', 81),
(30390000, 'ONLINE_BANKING', '2024-09-25 11:30:00', 82),
(53980000, 'CASH', '2024-10-02 15:40:00', 83),
(19990000, 'ONLINE_BANKING', '2024-10-09 10:20:00', 84),
(136980000, 'CASH', '2024-10-16 13:10:00', 85),
(34190000, 'ONLINE_BANKING', '2024-10-23 16:45:00', 86),
(49390000, 'CASH', '2024-10-30 09:30:00', 87),
(90980000, 'ONLINE_BANKING', '2024-11-06 14:20:00', 88),
(28490000, 'CASH', '2024-11-13 11:30:00', 89),
(33280000, 'ONLINE_BANKING', '2024-11-20 15:40:00', 90),
(18990000, 'CASH', '2024-11-27 10:20:00', 91),
(60780000, 'ONLINE_BANKING', '2024-12-04 13:10:00', 92),
(32290000, 'CASH', '2024-12-11 16:45:00', 93),
(36090000, 'ONLINE_BANKING', '2024-12-18 09:30:00', 94),
(85490000, 'CASH', '2024-12-25 14:15:00', 95),
(53190000, 'ONLINE_BANKING', '2025-01-01 11:30:00', 96),
(18040000, 'CASH', '2025-01-08 15:40:00', 97),
(155770000, 'ONLINE_BANKING', '2025-01-15 10:20:00', 98),
(47490000, 'CASH', '2025-01-22 13:10:00', 99),
(99730000, 'ONLINE_BANKING', '2025-01-29 16:45:00', 100),
(36090000, 'CASH', '2025-02-05 08:30:00', 101),
(51280000, 'ONLINE_BANKING', '2025-02-07 10:15:00', 102),
(28490000, 'CASH', '2025-02-09 14:20:00', 103),
(119670000, 'ONLINE_BANKING', '2025-02-11 09:45:00', 104),
(43690000, 'CASH', '2025-02-13 11:30:00', 105),
(79780000, 'ONLINE_BANKING', '2025-02-15 15:45:00', 106),
(21840000, 'CASH', '2025-02-17 13:20:00', 107),
(18990000, 'ONLINE_BANKING', '2025-02-19 10:10:00', 108),
(53190000, 'CASH', '2025-02-21 16:40:00', 109),
(64580000, 'ONLINE_BANKING', '2025-02-23 09:25:00', 110),
(30390000, 'CASH', '2025-02-25 14:15:00', 111),
(85490000, 'ONLINE_BANKING', '2025-02-27 11:30:00', 112),
(41790000, 'CASH', '2025-03-01 10:20:00', 113),
(75980000, 'ONLINE_BANKING', '2025-03-03 15:45:00', 114),
(27530000, 'CASH', '2025-03-05 09:10:00', 115),
(58880000, 'ONLINE_BANKING', '2025-03-07 16:20:00', 116),
(39880000, 'CASH', '2025-03-09 13:45:00', 117),
(34190000, 'ONLINE_BANKING', '2025-03-11 10:30:00', 118),
(92130000, 'CASH', '2025-03-13 11:15:00', 119),
(47490000, 'ONLINE_BANKING', '2025-03-15 15:30:00', 120),
(49390000, 'CASH', '2025-03-17 10:10:00', 121),
(24690000, 'ONLINE_BANKING', '2025-03-19 13:45:00', 122),
(136770000, 'CASH', '2025-03-21 16:20:00', 123),
(56980000, 'ONLINE_BANKING', '2025-03-23 09:30:00', 124),
(36090000, 'CASH', '2025-03-25 14:15:00', 125),
(17090000, 'ONLINE_BANKING', '2025-03-27 11:30:00', 126),
(8540000, 'CASH', '2025-03-29 15:45:00', 127),
(32290000, 'ONLINE_BANKING', '2025-03-31 10:20:00', 128),
(62680000, 'CASH', '2025-04-02 13:10:00', 129),
(83590000, 'ONLINE_BANKING', '2025-04-04 16:45:00', 130),
(25640000, 'CASH', '2025-04-06 09:30:00', 131),
(19940000, 'ONLINE_BANKING', '2025-04-08 14:20:00', 132),
(97830000, 'CASH', '2025-04-10 11:30:00', 133),
(43690000, 'ONLINE_BANKING', '2025-04-12 15:45:00', 134),
(18990000, 'CASH', '2025-04-14 10:20:00', 135),
(45580000, 'ONLINE_BANKING', '2025-04-16 13:10:00', 136),
(70280000, 'CASH', '2025-04-18 16:45:00', 137),
(53190000, 'ONLINE_BANKING', '2025-04-20 09:30:00', 138),
(102580000, 'CASH', '2025-04-22 14:15:00', 139),
(37990000, 'ONLINE_BANKING', '2025-04-24 11:30:00', 140),
(41790000, 'CASH', '2025-04-26 15:40:00', 141),
(28490000, 'ONLINE_BANKING', '2025-04-28 10:20:00', 142),
(85490000, 'CASH', '2025-04-30 13:10:00', 143),
(16140000, 'ONLINE_BANKING', '2025-05-02 09:30:00', 144),
(49390000, 'CASH', '2025-05-03 14:15:00', 145),
(34190000, 'ONLINE_BANKING', '2025-05-04 11:30:00', 146),
(21840000, 'CASH', '2025-05-05 15:45:00', 147),
(36090000, 'ONLINE_BANKING', '2025-05-06 10:20:00', 148),
(153870000, 'CASH', '2025-05-07 13:10:00', 149),
(24690000, 'ONLINE_BANKING', '2025-05-08 16:45:00', 150),
(15190000, 'CASH', '2025-05-09 09:30:00', 151),
(41790000, 'ONLINE_BANKING', '2025-05-10 14:15:00', 152),
(30390000, 'CASH', '2025-05-11 11:30:00', 153),
(53190000, 'ONLINE_BANKING', '2025-05-12 15:40:00', 154),
(10440000, 'CASH', '2025-05-13 10:20:00', 155),
(18990000, 'ONLINE_BANKING', '2025-05-14 13:10:00', 156),
(32290000, 'CASH', '2025-05-15 16:45:00', 157),
(85490000, 'ONLINE_BANKING', '2025-05-16 09:30:00', 158),
(28490000, 'CASH', '2025-05-17 14:15:00', 159),
(70280000, 'ONLINE_BANKING', '2025-05-18 11:30:00', 160),
(47490000, 'CASH', '2025-05-19 15:40:00', 161),
(21840000, 'ONLINE_BANKING', '2025-05-20 10:20:00', 162),
(136770000, 'CASH', '2025-05-21 13:10:00', 163),
(36090000, 'ONLINE_BANKING', '2025-05-22 16:45:00', 164),
(79780000, 'CASH', '2025-05-23 09:30:00', 165),
(53190000, 'ONLINE_BANKING', '2025-05-24 14:15:00', 166),
(19940000, 'CASH', '2025-05-25 11:30:00', 167),
(21840000, 'ONLINE_BANKING', '2025-05-26 15:40:00', 168),
(75030000, 'CASH', '2025-05-27 10:20:00', 169),
(25640000, 'ONLINE_BANKING', '2025-05-28 13:10:00', 170),
(142470000, 'CASH', '2025-05-29 16:45:00', 171),
(30390000, 'ONLINE_BANKING', '2025-05-30 09:30:00', 172),
(47490000, 'CASH', '2025-06-01 14:15:00', 173),
(60780000, 'ONLINE_BANKING', '2025-06-02 11:30:00', 174),
(16140000, 'CASH', '2025-06-03 15:40:00', 175),
(66480000, 'ONLINE_BANKING', '2025-06-04 10:20:00', 176),
(37990000, 'CASH', '2025-06-05 13:10:00', 177),
(121570000, 'ONLINE_BANKING', '2025-06-06 16:45:00', 178),
(34190000, 'CASH', '2025-06-07 09:30:00', 179),
(41790000, 'ONLINE_BANKING', '2025-06-08 14:15:00', 180),
(53190000, 'CASH', '2025-06-09 11:30:00', 181),
(21840000, 'ONLINE_BANKING', '2025-06-10 15:40:00', 182),
(18990000, 'CASH', '2025-06-11 10:20:00', 183),
(83590000, 'ONLINE_BANKING', '2025-06-12 13:10:00', 184),
(85490000, 'CASH', '2025-06-13 16:45:00', 185),
(21840000, 'ONLINE_BANKING', '2025-06-14 09:30:00', 186),
(125370000, 'CASH', '2025-06-15 14:15:00', 187),
(36090000, 'ONLINE_BANKING', '2025-06-16 11:30:00', 188),
(8540000, 'CASH', '2025-06-17 15:40:00', 189),
(47490000, 'ONLINE_BANKING', '2025-06-18 10:20:00', 190),
(24690000, 'CASH', '2025-06-19 13:10:00', 191),
(30390000, 'ONLINE_BANKING', '2025-06-20 16:45:00', 192),
(53190000, 'CASH', '2025-06-21 09:30:00', 193),
(21840000, 'ONLINE_BANKING', '2025-06-22 14:15:00', 194),
(34190000, 'CASH', '2025-06-23 11:30:00', 195),
(85490000, 'ONLINE_BANKING', '2025-06-24 15:40:00', 196),
(41790000, 'CASH', '2025-06-25 10:20:00', 197),
(28490000, 'ONLINE_BANKING', '2025-06-26 13:10:00', 198),
(16140000, 'CASH', '2025-06-28 16:45:00', 199),
(138670000, 'ONLINE_BANKING', '2025-06-30 09:30:00', 200);

-- Thêm dữ liệu vào bảng invoices (phiếu nhập kho)
INSERT INTO invoices (invoice_code, import_date, supplier_id, notes, discount, vat, additional_fees, cancel_reason)
VALUES
    ('NK0001', '2024-01-05', 1, 'Đơn hàng Apple đầu năm 2024', 5.0, 10.0, 50000, NULL),
    ('NK0002', '2024-01-15', 2, 'Nhập Samsung quý 1/2024', 3.0, 10.0, 30000, NULL),
    ('NK0003', '2024-01-25', 3, 'Nhập Oppo tháng 1/2024', 2.0, 10.0, 25000, NULL),
    ('NK0004', '2024-02-05', 4, 'Nhập Xiaomi đợt 1 tháng 2', 4.0, 10.0, 35000, NULL),
    ('NK0005', '2024-02-15', 5, 'Đơn hàng Sony quý 1', 3.5, 10.0, 40000, NULL),
    ('NK0006', '2024-02-25', 6, 'Nhập Asus tháng 2/2024', 2.5, 10.0, 30000, NULL),
    ('NK0007', '2024-03-05', 7, 'Nhập Dell đợt 1 tháng 3', 5.0, 10.0, 45000, NULL),
    ('NK0008', '2024-03-15', 8, 'Nhập Huawei tháng 3/2024', 3.0, 10.0, 35000, NULL),
    ('NK0009', '2024-03-25', 9, 'Đơn hàng LG tháng 3', 2.0, 10.0, 25000, NULL),
    ('NK0010', '2024-04-05', 10, 'Nhập Lenovo đợt 1 tháng 4', 4.0, 10.0, 40000, NULL),
    ('NK0011', '2024-04-10', 1, 'Nhập Apple bổ sung tháng 4', 5.0, 10.0, 50000, NULL),
    ('NK0012', '2024-04-15', 2, 'Nhập Samsung bổ sung quý 2', 3.0, 10.0, 35000, NULL),
    ('NK0013', '2024-04-20', 3, 'Nhập Oppo tháng 4/2024', 2.5, 10.0, 30000, NULL),
    ('NK0014', '2024-04-25', 4, 'Nhập Xiaomi đợt 2 tháng 4', 4.0, 10.0, 40000, NULL),
    ('NK0015', '2024-04-30', 5, 'Đơn hàng Sony cuối tháng 4', 3.5, 10.0, 45000, NULL);

-- Thêm dữ liệu vào bảng invoice_item (chi tiết phiếu nhập)
INSERT INTO invoice_item (product_id, product_code, product_name, brand, quantity, price, payment_status, invoice_id)
VALUES
    -- Phiếu NK0001
    (7, 'SP0007', 'iPhone 15 Pro Max', 'Apple', 20, 27990000, 'ĐÃ THANH TOÁN', 1),
    (19, 'SP0019', 'MacBook Pro 16 M3 Max', 'Apple', 10, 75990000, 'ĐÃ THANH TOÁN', 1),
    (12, 'SP0012', 'iPad Pro M2 12.9', 'Apple', 15, 26990000, 'ĐÃ THANH TOÁN', 1),

    -- Phiếu NK0002
    (6, 'SP0006', 'Samsung Galaxy S23 Ultra', 'Samsung', 15, 23990000, 'ĐÃ THANH TOÁN', 2),
    (13, 'SP0013', 'Samsung Galaxy Tab S9 Ultra', 'Samsung', 10, 21990000, 'ĐÃ THANH TOÁN', 2),

    -- Phiếu NK0003
    (3, 'SP0003', 'Oppo Find X5 Pro', 'Oppo', 15, 15990000, 'ĐÃ THANH TOÁN', 3),

    -- Phiếu NK0004
    (1, 'SP0001', 'Xiaomi Redmi Note 12 Pro', 'Xiaomi', 30, 6990000, 'ĐÃ THANH TOÁN', 4),
    (8, 'SP0008', 'Xiaomi 14 Ultra', 'Xiaomi', 15, 20990000, 'ĐÃ THANH TOÁN', 4),
    (10, 'SP0010', 'Xiaomi Pad 6 Pro', 'Xiaomi', 20, 11990000, 'ĐÃ THANH TOÁN', 4),

    -- Phiếu NK0005
    (4, 'SP0004', 'Vivo X90 Pro', 'Vivo', 20, 17990000, 'ĐÃ THANH TOÁN', 5),

    -- Phiếu NK0006
    (21, 'SP0021', 'Asus ROG Zephyrus G16', 'Asus', 10, 41990000, 'ĐÃ THANH TOÁN', 6),

    -- Phiếu NK0007
    (20, 'SP0020', 'Dell XPS 15', 'Dell', 12, 46990000, 'ĐÃ THANH TOÁN', 7),

    -- Phiếu NK0008
    (9, 'SP0009', 'Huawei MatePad Pro 11', 'Huawei', 15, 13490000, 'ĐÃ THANH TOÁN', 8),

    -- Phiếu NK0009
    (5, 'SP0005', 'Honor Magic5 Pro', 'Honor', 15, 16990000, 'CHỜ THANH TOÁN', 9),

    -- Phiếu NK0010
    (14, 'SP0014', 'Lenovo Tab P12 Pro', 'Lenovo', 15, 14490000, 'ĐÃ THANH TOÁN', 10),
    (17, 'SP0017', 'Lenovo ThinkPad X1 Carbon', 'Lenovo', 10, 38990000, 'ĐÃ THANH TOÁN', 10),

    -- Phiếu NK0011
    (7, 'SP0007', 'iPhone 15 Pro Max', 'Apple', 15, 27990000, 'ĐÃ THANH TOÁN', 11),

    -- Phiếu NK0012
    (6, 'SP0006', 'Samsung Galaxy S23 Ultra', 'Samsung', 10, 23990000, 'ĐÃ THANH TOÁN', 12),

    -- Phiếu NK0013
    (3, 'SP0003', 'Oppo Find X5 Pro', 'Oppo', 10, 15990000, 'CHỜ THANH TOÁN', 13),
    (11, 'SP0011', 'Realme Pad 2', 'Realme', 20, 6990000, 'CHỜ THANH TOÁN', 13),

    -- Phiếu NK0014
    (1, 'SP0001', 'Xiaomi Redmi Note 12 Pro', 'Xiaomi', 25, 6990000, 'CHỜ THANH TOÁN', 14),

    -- Phiếu NK0015
    (15, 'SP0015', 'HP Spectre x360', 'HP', 8, 32990000, 'CHỜ THANH TOÁN', 15),
    (16, 'SP0016', 'Acer Predator Helios 300', 'Acer', 8, 29990000, 'CHỜ THANH TOÁN', 15);

-- Thêm dữ liệu vào bảng invoice_history (lịch sử thanh toán)
INSERT INTO invoice_history (invoice_id, method, amount, paid_at)
VALUES
    -- Thanh toán cho phiếu NK0001
    (1, 'Chuyển khoản', 559800000 * 0.5, '2024-01-05 14:30:00'),
    (1, 'Chuyển khoản', 559800000 * 0.5, '2024-01-20 10:15:00'),

    -- Thanh toán cho phiếu NK0002
    (2, 'Chuyển khoản', 359850000 * 0.7, '2024-01-15 11:45:00'),
    (2, 'Tiền mặt', 359850000 * 0.3, '2024-01-30 16:20:00'),

    -- Thanh toán cho phiếu NK0003
    (3, 'Chuyển khoản', 239850000, '2024-01-25 09:30:00'),

    -- Thanh toán cho phiếu NK0004
    (4, 'Chuyển khoản', 209700000 * 0.3, '2024-02-05 13:15:00'),
    (4, 'Chuyển khoản', 209700000 * 0.7, '2024-02-15 14:40:00'),

    -- Thanh toán cho phiếu NK0005
    (5, 'Tiền mặt', 359800000, '2024-02-15 10:10:00'),

    -- Thanh toán cho phiếu NK0006
    (6, 'Chuyển khoản', 419900000, '2024-02-28 15:30:00'),

    -- Thanh toán cho phiếu NK0007
    (7, 'Chuyển khoản', 563880000 * 0.6, '2024-03-07 09:45:00'),
    (7, 'Tiền mặt', 563880000 * 0.4, '2024-03-20 14:15:00'),

    -- Thanh toán cho phiếu NK0008
    (8, 'Chuyển khoản', 202350000, '2024-03-18 11:20:00'),

    -- Thanh toán cho phiếu NK0010
    (10, 'Chuyển khoản', 217350000 * 0.4, '2024-04-06 10:30:00'),
    (10, 'Chuyển khoản', 217350000 * 0.6, '2024-04-15 16:45:00'),

    -- Thanh toán cho phiếu NK0011
    (11, 'Tiền mặt', 419850000, '2024-04-12 13:10:00'),

    -- Thanh toán cho phiếu NK0012
    (12, 'Chuyển khoản', 239900000, '2024-04-18 14:25:00');
