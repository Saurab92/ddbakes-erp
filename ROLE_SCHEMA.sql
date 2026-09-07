-- Roles Table Schema
-- Note: an `active` column has been added to the originally supplied DDL to
-- support the Activate/Deactivate Role API.
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_name` (`name`),
  KEY `fk_role_created_by` (`created_by`),
  KEY `fk_role_updated_by` (`updated_by`),
  CONSTRAINT `fk_role_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_role_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- If the table already exists without the `active` column, run:
-- ALTER TABLE `roles` ADD COLUMN `active` tinyint(1) NOT NULL DEFAULT '1' AFTER `description`;

CREATE INDEX idx_role_name ON roles(name);
CREATE INDEX idx_role_active ON roles(active);
