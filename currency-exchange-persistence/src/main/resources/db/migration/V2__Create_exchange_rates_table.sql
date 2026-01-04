create table if not exists exchange_rates(
    id integer primary key,
    base_currency_id int not null references currencies(id),
    target_currency_id int not null references currencies(id),
    rate decimal(16,6) not null,
    unique (base_currency_id, target_currency_id)
);