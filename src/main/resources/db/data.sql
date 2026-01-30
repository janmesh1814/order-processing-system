INSERT INTO inventory (product_id, quantity)
VALUES ('product-1', 10)
    ON DUPLICATE KEY UPDATE quantity = VALUES(quantity);

INSERT INTO inventory (product_id, quantity)
VALUES ('product-2', 0)
    ON DUPLICATE KEY UPDATE quantity = VALUES(quantity);