insert into bank_account (account_number, currency)
values
    ('40702810000000000001', 'KGS'),
    ('40702810000000000002', 'KZT'),
    ('40702810000000000003', 'EUR'),
    ('40702810000000000004', 'USD')
on conflict (currency) do nothing;

-- Example exchange rates (to KGS) for today; adjust as needed.
insert into exchange_rate (from_currency, rate, rate_date)
values
    ('KZT', 0.20, current_date),
    ('EUR', 95.00, current_date),
    ('USD', 88.00, current_date)
on conflict (from_currency, rate_date) do nothing;

