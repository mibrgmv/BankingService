create type account_type as enum (
    'SAVINGS',
    'DEBIT',
    'CREDIT'
);

create table if not exists accounts (
    id                   serial primary key,
    owner_id             integer not null references clients,
    bank_id              integer not null references banks,
    balance              numeric not null,
    is_suspicious        boolean not null,
    account_type         account_type not null,
    limit_for_suspicious numeric not null,
    interest_rate        numeric not null,
    credit_limit         numeric,
    end_date             date
);
