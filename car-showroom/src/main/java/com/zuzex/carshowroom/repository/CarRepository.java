package com.zuzex.carshowroom.repository;

import com.zuzex.carshowroom.model.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, Long> {
}
