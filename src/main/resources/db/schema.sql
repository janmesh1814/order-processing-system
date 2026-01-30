CREATE TABLE IF NOT EXISTS inventory (
                                         product_id VARCHAR(50) PRIMARY KEY,
    quantity INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );