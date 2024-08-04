create table if not exists clients (
   id            serial primary key,
   first_name    varchar(255) not null,
   last_name     varchar(255) not null,
   date_of_birth date         not null,
   address       varchar(255) default NULL::character varying,
   passport      varchar(255) default NULL::character varying
);