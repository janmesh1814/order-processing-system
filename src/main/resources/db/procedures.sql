CREATE PROCEDURE reserve_stock(
    IN p_product_id VARCHAR(50),
    IN p_quantity INT,
    OUT success BOOLEAN
)
BEGIN
    DECLARE current_qty INT;

    SELECT quantity INTO current_qty
    FROM inventory
    WHERE product_id = p_product_id;

    IF current_qty IS NULL OR current_qty < p_quantity THEN
        SET success = FALSE;
    ELSE
        UPDATE inventory
        SET quantity = quantity - p_quantity
        WHERE product_id = p_product_id;
        SET success = TRUE;
    END IF;
END;