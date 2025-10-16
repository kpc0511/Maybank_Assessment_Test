INSERT INTO `maybank-master`.roles
(id, name, create_by, create_date, status, update_by, update_date, version)
VALUES
(1, 'ROLE_USER', 'admin', '2024-10-02 12:00:00', 0, 'admin', NULL, 1),
(2, 'ROLE_MODERATOR', 'admin', '2024-10-02 12:00:00', 0, 'admin', NULL, 1),
(3, 'ROLE_ADMIN', 'admin', '2024-10-02 12:00:00', 0, 'admin', NULL, 1);

INSERT INTO `maybank-master`.customer
(customer_id, customer_name, status, version, create_date, create_by)
VALUES
(222, 'John Doe', 0, 1, '2024-10-02 12:00:00', 'John Doe'),
(333, 'Jane Smith', 0, 1, '2024-10-02 12:01:00', 'Jane Smith'),
(444, 'Michael Brown', 0, 1, '2024-10-02 12:02:00', 'Michael Brown');


INSERT INTO `maybank-master`.account
(create_by, create_date, status, version, account_number, customer_id)
VALUES
('John Doe', '2024-10-02 12:00:00', 0, 1, '8872838283', 222),
('John Doe', '2024-10-02 12:00:00', 0, 1, '8872838299', 222),
('John Doe', '2024-10-02 12:00:00', 0, 1, '6872838260', 333);

INSERT INTO `maybank-master`.users (id, create_by, create_date, status, update_by, update_date, version, display_name, email, enabled, `language`, last_login_date, password, phone, remark, username)
VALUES(15515695387119616, 'adminuser8', '2024-10-02 10:02:41.535000', NULL, NULL, NULL, NULL, 'displayName', 'adminuser8@gmail.com', 1, NULL, NULL, '$2a$10$swQAjqVFkybSZCTOoolM9.VKPX83.t.jRr52rgDaqgn12nBu7OCEu', '1232131321312', 'remark', 'adminuser8');

INSERT INTO `maybank-master`.user_roles (user_id, role_id) VALUES(15515695387119616, 3);