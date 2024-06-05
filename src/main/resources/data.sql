-- Populate users table with sample data using prepared statement
INSERT INTO users (name, email, phone, address)
VALUES (?, ?, ?, ?)

-- Insert sample data
PREPARE insert_user (VARCHAR(255), VARCHAR(255), VARCHAR(255), VARCHAR(255));
INSERT INTO users (name, email, phone, address)
VALUES ('John Doe', 'john.doe@example.com', '+1234567890', '123 Main St'),
       ('Jane Smith', 'jane.smith@example.com', '+9876543210', '456 Elm St'),
       ('Alice Brown', 'alice.brown@example.com', '+0123456789', '789 Oak St');
DEALLOCATE PREPARE insert_user;

