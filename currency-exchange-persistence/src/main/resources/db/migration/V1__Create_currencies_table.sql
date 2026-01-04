create table if not exists currencies (
    id integer primary key, --integer + primary key -> serial (alias for internal rowid)
    code char(3) unique not null,
    full_name varchar(256) not null,
    sign varchar(4)
);