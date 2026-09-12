-- Join table mapping products to suppliers (many-to-many).
-- One product can be sourced from multiple suppliers, and one supplier
-- can supply multiple products.
CREATE TABLE product_suppliers (
    product_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,

    PRIMARY KEY (product_id, supplier_id),

    CONSTRAINT fk_product_suppliers_product
        FOREIGN KEY (product_id) REFERENCES products(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_product_suppliers_supplier
        FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_product_suppliers_supplier_id ON product_suppliers(supplier_id);
