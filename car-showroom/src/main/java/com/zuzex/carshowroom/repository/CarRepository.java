package com.zuzex.carshowroom.repository;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.common.model.Status;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarRepository extends ReactiveCrudRepository<Car, Long> {

    Flux<Car> findAllByStatusEquals(Status status);

    Mono<Car> findByIdAndStatus(Long id, Status status);
}
