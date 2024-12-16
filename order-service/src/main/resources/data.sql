-- Discount data
INSERT INTO Discount (discount_Type, description, item_Type, discount_on, min_age, max_age, percentage_off, pack_price, min_unit, max_Unit, discount_status, start_from, end_till) VALUES
    ('PERCENTAGE_OFF', '5% off on all vegetables up to 100g', 'VEGETABLES', 'WEIGHT', 0, 0, 5.0, NULL, 0.0, 100.0, 'ACTIVE', '2024-12-01 00:00:00', '2024-12-31 23:59:59'),
    ('PERCENTAGE_OFF', '7% off on all vegetables between 100 to 500 g', 'VEGETABLES', 'WEIGHT', 0, 0, 7.0, NULL, 100.0, 500.0, 'ACTIVE', '2024-12-01 00:00:00', '2024-12-31 23:59:59'),
    ('PERCENTAGE_OFF', '10% off on all vegetables above 500g', 'VEGETABLES', 'WEIGHT', 0, 0, 10.0, NULL, 500.0, 0.0, 'ACTIVE', '2024-12-01 00:00:00', '2024-12-31 23:59:59'),
    ('PACK_X_6', 'Special price for beer pack of 6', 'BEER', 'QUANTITY', 0, 0, NULL, 4.0, 6.0, NULL, 'ACTIVE', '2024-12-01 00:00:00', '2024-12-31 23:59:59'),
    ('BUY_X_GET_Y', 'Buy 1 get 2 free on 2-3 days aged bread', 'BREAD', 'MANUFACTURED_DATE', 2, 3, NULL, NULL, 1.0, 2.0, 'ACTIVE', '2024-12-01 00:00:00', '2024-12-31 23:59:59'),
    ('BUY_X_GET_Y', 'Buy 1 get 3 free on 4-6 days aged bread', 'BREAD', 'MANUFACTURED_DATE', 4, 6, NULL, NULL, 1.0, 3.0, 'ACTIVE', '2024-12-01 00:00:00', '2024-12-31 23:59:59'),
    ('EXPIRED', 'Item expired', 'BREAD', 'MANUFACTURED_DATE', 6, 0, NULL, NULL, 1.0, 2.0, 'ACTIVE', '2024-12-01 00:00:00', '2024-12-31 23:59:59');

