-- Schema for the purchases table.
CREATE TABLE `purchases` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `purchase_date` date NOT NULL,
  `invoice_number` varchar(100) DEFAULT NULL,
  `supplier_id` bigint NOT NULL,
  `remarks` varchar(500) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_purchase_supplier` (`supplier_id`),
  CONSTRAINT `fk_purchase_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
