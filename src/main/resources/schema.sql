CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS product_list
(
    id            UUID DEFAULT uuid_generate_v4(),
    customer_id   VARCHAR(255),
    product_entry JSON DEFAULT '{}',
    created_at    TIMESTAMP, --NOT NULL DEFAULT LOCALTIMESTAMP,
    updated_at    TIMESTAMP
    );


ALTER TABLE product_list
DROP CONSTRAINT IF EXISTS pk_product_list;

ALTER TABLE product_list
    ADD CONSTRAINT pk_product_list PRIMARY KEY (id);