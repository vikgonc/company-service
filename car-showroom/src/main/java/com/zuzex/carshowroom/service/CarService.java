package com.zuzex.carshowroom.service;

import com.zuzex.carshowroom.dto.CarDto;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.model.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarService {

    Flux<CarDto> findAllCars();

    Flux<CarDto> findAllCarsOnSale();

    Mono<CarDto> findCarById(Long id);

    Mono<CarDto> orderNewCar(OrderCarDto orderCarDto);

    Mono<CarDto> setCarStatusById(Long id, Status status);

    Mono<CarDto> buyCarById(Long id);
}
