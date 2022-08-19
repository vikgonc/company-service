--liquibase formatted sql

--changeset vikgonc:1
create table users
(
    username varchar(255) not null primary key,
    password varchar(255) not null,
    email    varchar(255) not null,
    role     varchar(255) not null
);
--rollback drop table users;
