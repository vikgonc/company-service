package com.zuzex.carshowroom.service;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.model.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarService {

    Flux<Car> findAllCars();

    Flux<Car> findAllCarsOnSale();

    Mono<Car> findCarById(Long id);

    Mono<Car> orderNewCar(OrderCarDto orderCarDto);

    Mono<Car> setCarStatusById(Long id, Status status);

    Mono<Car> buyCarById(Long id);
}
