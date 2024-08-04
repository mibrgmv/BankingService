create type transaction_type as enum (
    'DEPOSIT',
    'WITHDRAWAL',
    'TRANSFER',
    'RECEIVING',
    'INTEREST'
);

create table transactions (
    id         serial primary key,
    account_id integer          not null references accounts,
    bank_id    integer          not null references banks,
    amount     numeric          not null,
    type       transaction_type not null,
    date       date             not null,
    is_undo    boolean          not null
);
