package com.zuzex.carshowroom.repository;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.common.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findAllByStatusEquals(Status status);

    Optional<Car> findByIdAndStatus(Long id, Status status);
}
