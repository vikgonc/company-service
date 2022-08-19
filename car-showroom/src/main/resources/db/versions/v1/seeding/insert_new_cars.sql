--liquibase formatted sql

--changeset vikgonc:insert_values_in_cars
insert into cars(on_sale, model_id)
values (true,
        1);

insert into cars(on_sale, model_id)
values (true,
        2);

insert into cars(on_sale, model_id)
values (true,
        3);
--rollback ;
