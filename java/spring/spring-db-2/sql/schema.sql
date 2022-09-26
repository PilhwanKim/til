drop table if exists item CASCADE;
create table item
(
    id        bigint auto_increment,
    item_name varchar(10),
    price     integer,
    quantity  integer,
    primary key (id)
);
