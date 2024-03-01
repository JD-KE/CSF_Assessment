-- TODO Task 3
drop database if exists productorders;

create database productorders;

use productorders;

create table orders (
    order_id char(26) not null,
    date timestamp default current_timestamp,
    name varchar(64) not null,
    address varchar(128) not null,
    priority BOOLEAN default 0,
    comments text,


    primary key(order_id)
);

create table lineitems (
    item_id int auto_increment,
    product_id char(24) not null,
    name varchar(128) not null,
    quantity int ,
    price decimal(9,2),
    order_id char(26) not null,

    primary key(item_id),
    constraint fk_order_id foreign key(order_id) references orders(order_id)
);


