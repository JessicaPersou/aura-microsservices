CREATE TABLE PRODUCT (
                         ID BIGSERIAL PRIMARY KEY,
                         NAME VARCHAR(100) NOT NULL,
                         SKU VARCHAR(50) NOT NULL UNIQUE,
                         PRICE DECIMAL(10, 2) NOT NULL,
                         DESCRIPTION TEXT
);

CREATE INDEX IDX_PRODUCT_SKU ON PRODUCT (SKU);