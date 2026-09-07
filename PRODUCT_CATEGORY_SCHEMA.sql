-- Add category relationship to products table
-- Note: categories table is expected to already exist (created via ddl-auto in dev,
-- or should be created before running this migration in prod).

ALTER TABLE products
    ADD COLUMN category_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id);

CREATE INDEX idx_product_category_id ON products (category_id);
