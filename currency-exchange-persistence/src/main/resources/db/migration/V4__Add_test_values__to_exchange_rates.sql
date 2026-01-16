-- Czyścimy starą próbę, jeśli baza nie jest transakcyjna (opcjonalnie w SQLite)
DELETE FROM exchange_rates;

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
VALUES
    ((SELECT id FROM currencies WHERE code = 'USD'), (SELECT id FROM currencies WHERE code = 'EUR'), 0.94),
    ((SELECT id FROM currencies WHERE code = 'USD'), (SELECT id FROM currencies WHERE code = 'RUB'), 92.50),
    ((SELECT id FROM currencies WHERE code = 'EUR'), (SELECT id FROM currencies WHERE code = 'PLN'), 4.35),
    ((SELECT id FROM currencies WHERE code = 'USD'), (SELECT id FROM currencies WHERE code = 'PLN'), 4.02),
    ((SELECT id FROM currencies WHERE code = 'GBP'), (SELECT id FROM currencies WHERE code = 'EUR'), 1.17),
    ((SELECT id FROM currencies WHERE code = 'USD'), (SELECT id FROM currencies WHERE code = 'JPY'), 148.50),
    ((SELECT id FROM currencies WHERE code = 'CHF'), (SELECT id FROM currencies WHERE code = 'EUR'), 1.05),
    ((SELECT id FROM currencies WHERE code = 'USD'), (SELECT id FROM currencies WHERE code = 'CAD'), 1.35),
    ((SELECT id FROM currencies WHERE code = 'GBP'), (SELECT id FROM currencies WHERE code = 'PLN'), 5.10),
    ((SELECT id FROM currencies WHERE code = 'EUR'), (SELECT id FROM currencies WHERE code = 'USD'), 1.08);