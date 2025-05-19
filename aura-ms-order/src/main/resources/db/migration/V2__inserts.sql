INSERT INTO MS_ORDER (CLIENT_ID, ITEMS, DT_CREATE, STATUS, TOTAL_AMOUNT, PAYMENT_ID)
VALUES (1, '[{"sku": "SMGX20-BLK", "quantity": 1}]', '2023-10-01 10:00:00', 'ABERTO', 999.99, 1),
       (1, '[{"sku": "ULTP-15-SLV", "quantity": 1}]', '2023-10-02 11:00:00', 'FECHADO_COM_SUCESSO', 2499.99, 2),
       (1, '[{"sku": "HDPHN-WL-BLK", "quantity": 1}]', '2023-10-03 12:00:00', 'FECHADO_SEM_ESTOQUE', 149.99, 3),
       (1, '[{"sku": "TV55-4K-SMART", "quantity": 1}]', '2023-10-04 13:00:00', 'FECHADO_SEM_CREDITO', 1899.99, 4),
       (1, '[{"sku": "CAM-DSLR-PRO", "quantity": 1}]', '2023-10-05 14:00:00', 'ABERTO', 1299.50, 5);