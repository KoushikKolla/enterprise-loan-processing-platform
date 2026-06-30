CREATE TABLE customers (

                             id BIGINT AUTO_INCREMENT PRIMARY KEY,

                             customer_number VARCHAR(20) NOT NULL UNIQUE,

                             first_name VARCHAR(100) NOT NULL,

                             last_name VARCHAR(100) NOT NULL,

                             email VARCHAR(255) NOT NULL UNIQUE,

                             mobile_number VARCHAR(15) NOT NULL UNIQUE,

                             pan_number VARCHAR(10) NOT NULL UNIQUE,

                             aadhaar_number VARCHAR(12) NOT NULL UNIQUE,

                             date_of_birth DATE NOT NULL,

                             employment_type VARCHAR(30) NOT NULL,

                             annual_income DECIMAL(15,2) NOT NULL,

                             status VARCHAR(20) NOT NULL,

                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                 ON UPDATE CURRENT_TIMESTAMP

  );