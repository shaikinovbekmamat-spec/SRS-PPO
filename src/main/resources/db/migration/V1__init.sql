create table if not exists pos_terminal (
    id bigserial primary key,
    device_code varchar(64) not null unique,
    location varchar(255),
    status varchar(16) not null default 'ACTIVE'
);

create table if not exists bank_account (
    id bigserial primary key,
    account_number varchar(64) not null,
    currency varchar(8) not null,
    constraint uq_bank_account_currency unique (currency)
);

-- "transaction" is a reserved keyword in many SQL dialects, use pos_transaction.
create table if not exists pos_transaction (
    id bigserial primary key,
    mpc_transaction_id varchar(128) not null unique,
    terminal_id bigint not null references pos_terminal(id),
    oper_date_time timestamp not null,
    currency varchar(8) not null,
    amount numeric(19, 2) not null,
    card_number varchar(32) not null,
    status varchar(16) not null default 'NEW'
);

create index if not exists idx_pos_transaction_oper_date_time on pos_transaction(oper_date_time);
create index if not exists idx_pos_transaction_terminal_currency on pos_transaction(terminal_id, currency);
create index if not exists idx_pos_transaction_status on pos_transaction(status);

create table if not exists posting (
    id bigserial primary key,
    transaction_id bigint not null unique references pos_transaction(id),
    bank_account_id bigint not null references bank_account(id),
    currency varchar(8) not null,
    amount numeric(19, 2) not null,
    posting_status varchar(16) not null default 'COMPLETED',
    error_message text,
    created_at timestamp not null default now()
);

create index if not exists idx_posting_created_at on posting(created_at);
create index if not exists idx_posting_status on posting(posting_status);

create table if not exists exchange_rate (
    id bigserial primary key,
    from_currency varchar(8) not null,
    rate numeric(19, 6) not null,
    rate_date date not null,
    constraint uq_exchange_rate unique (from_currency, rate_date)
);

create index if not exists idx_exchange_rate_date on exchange_rate(rate_date);

create table if not exists mpc_poll_run (
    id bigserial primary key,
    started_at timestamp not null default now(),
    finished_at timestamp,
    status varchar(16) not null,
    requested_date date,
    received_count int not null default 0,
    error_message text
);

