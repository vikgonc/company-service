package com.zuzex.carshowroom.repository;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.common.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends CrudRepository<Car, Long> {

    List<Car> findAllByStatusEquals(Status status);

    Optional<Car> findByIdAndStatus(Long id, Status status);
}
