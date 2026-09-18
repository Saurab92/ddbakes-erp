-- Schema for designations, employees, and employee_bank_accounts tables.
-- An employee belongs to a department and a designation, and can have one
-- or more bank accounts (with at most one marked as primary).

CREATE TABLE `designations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employees` (
  `id` bigint NOT NULL AUTO_INCREMENT,

  `employee_code` varchar(50) NOT NULL,

  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) DEFAULT NULL,

  `email` varchar(150) DEFAULT NULL,
  `mobile_number` varchar(20) DEFAULT NULL,

  `address` text,

  `aadhaar_number` varchar(20) DEFAULT NULL,
  `pan_number` varchar(20) DEFAULT NULL,

  `department_id` bigint DEFAULT NULL,
  `designation_id` bigint DEFAULT NULL,

  `joining_date` date NOT NULL,
  `last_date` date DEFAULT NULL,

  `active` tinyint(1) NOT NULL DEFAULT '1',

  `daily_working_hours` decimal(4,2) NOT NULL DEFAULT '8.00',

  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,

  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_employee_code` (`employee_code`),

  KEY `idx_employee_department` (`department_id`),
  KEY `idx_employee_designation` (`designation_id`),

  CONSTRAINT `fk_employee_department`
    FOREIGN KEY (`department_id`)
    REFERENCES `departments` (`id`),

  CONSTRAINT `fk_employee_designation`
    FOREIGN KEY (`designation_id`)
    REFERENCES `designations` (`id`),

  CONSTRAINT `chk_daily_working_hours`
    CHECK (`daily_working_hours` > 0)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employee_bank_accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,

  `employee_id` bigint NOT NULL,

  `account_holder_name` varchar(150) NOT NULL,
  `account_number` varchar(50) NOT NULL,
  `ifsc_code` varchar(20) NOT NULL,
  `bank_name` varchar(150) DEFAULT NULL,

  `primary_account` tinyint(1) NOT NULL DEFAULT '1',
  `active` tinyint(1) NOT NULL DEFAULT '1',

  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,

  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,

  PRIMARY KEY (`id`),

  KEY `idx_bank_employee` (`employee_id`),

  CONSTRAINT `fk_bank_employee`
    FOREIGN KEY (`employee_id`)
    REFERENCES `employees` (`id`)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
