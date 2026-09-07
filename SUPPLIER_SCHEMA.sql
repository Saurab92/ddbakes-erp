-- Schema for the suppliers table.
CREATE TABLE suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(150),
    address VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT NULL,

    CONSTRAINT fk_supplier_created_by
        FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_supplier_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(id)
);

CREATE INDEX idx_supplier_name ON suppliers(name);
CREATE INDEX idx_supplier_active ON suppliers(active);
