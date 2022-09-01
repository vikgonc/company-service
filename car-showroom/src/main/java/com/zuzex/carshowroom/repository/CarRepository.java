package com.zuzex.carshowroom.repository;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.common.model.Status;
import lombok.NonNull;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarRepository extends ReactiveCrudRepository<Car, Long> {

    @Override
    @Query("SELECT cars.*,m.id as model_id, m.brand, m.model_name, m.description " +
            "FROM cars JOIN models m ON m.id = cars.model_id")
    @NonNull Flux<Car> findAll();

    @Query("SELECT cars.*,m.id as model_id, m.brand, m.model_name, m.description " +
            "FROM cars JOIN models m ON m.id = cars.model_id " +
            "WHERE cars.status = $1")
    Flux<Car> findAllByStatusEquals(Status status);

    @Query("SELECT cars.*,m.id as model_id, m.brand, m.model_name, m.description " +
            "FROM cars JOIN models m ON m.id = cars.model_id " +
            "WHERE cars.id = $1 AND cars.status = $2")
    Mono<Car> findByIdAndStatus(Long id, Status status);
}
