create table if not exists banks (
    id serial primary key,
    name varchar(255) not null,
    debit_interest_rate numeric not null,
    savings_interest_rate numeric not null,
    credit_commission numeric not null,
    credit_limit numeric not null,
    suspicious_account_limit numeric not null
);