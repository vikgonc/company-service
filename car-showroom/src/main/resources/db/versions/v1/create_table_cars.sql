--liquibase formatted sql

--changeset vikgonc:create_table_cars
create table cars
(
    id       bigint not null primary key generated by default as identity,
    on_sale  boolean not null,
    model_id bigint not null,
    foreign key (model_id)
        references models (id)
);
--rollback drop table cars;
