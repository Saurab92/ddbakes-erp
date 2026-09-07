-- Create issues table
CREATE TABLE IF NOT EXISTS issues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_date DATE NOT NULL,
    department_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    reason VARCHAR(255),
    remarks VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT fk_issue_department FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_issue_person FOREIGN KEY (person_id) REFERENCES persons(id),
    INDEX idx_issue_date (issue_date),
    INDEX idx_created_by (created_by)
);

-- Create issue_items table
CREATE TABLE IF NOT EXISTS issue_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(12, 3) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT fk_issue_item_issue FOREIGN KEY (issue_id) REFERENCES issues(id),
    CONSTRAINT fk_issue_item_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT chk_issue_item_quantity CHECK (quantity > 0),
    INDEX idx_issue_id (issue_id),
    INDEX idx_product_id (product_id)
);
