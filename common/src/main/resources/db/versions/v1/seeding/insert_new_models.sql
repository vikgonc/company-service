--liquibase formatted sql

--changeset vikgonc:insert_values_in_models
insert into models(brand,
                   model_name,
                   description)
values ('Volkswagen',
        'Juke',
        'Das auto');

insert into models(brand,
                   model_name,
                   description)
values ('ВАЗ',
        '2107',
        'Классика, бюджетное решение');

insert into models(brand,
                   model_name)
values ('Porsche',
        'Cayman');
--rollback delete from models;
