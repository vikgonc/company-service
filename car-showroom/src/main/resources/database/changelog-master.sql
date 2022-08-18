--liquibase formatted sql

--changeset vikgonc:create_table_users
create table users
(
    username varchar(255) not null primary key,
    password varchar(255) not null,
    role     varchar(255) not null
);
--rollback drop table users;
