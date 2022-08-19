--liquibase formatted sql

--changeset vikgonc:insert_values_in_models
insert into models(name, description)
values ('Volkswagen',
        'Das auto');

insert into models(name, description)
values ('ВАЗ',
        'Бюджетное решение');

insert into models(name)
values ('Honda Accord');
--rollback ;
