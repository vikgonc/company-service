package com.zuzex.carshowroom.controller;

import com.zuzex.carshowroom.dto.CarDto;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.common.dto.OrderCarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @GetMapping
    public Flux<CarDto> findAllCars() {
        return carService.findAllCars();
    }

    @GetMapping("/sale")
    public Flux<CarDto> findAllCarsOnSale() {
        return carService.findAllCarsOnSale();
    }

    @GetMapping("/{id}")
    public Mono<CarDto> findCarById(@PathVariable Long id) {
        return carService.findCarById(id);
    }

    @PostMapping("/order")
    public Mono<CarDto> orderNewCar(@RequestBody OrderCarDto orderCarDto) {
        return carService.orderNewCar(orderCarDto);
    }

    @PostMapping("/buy/{id}")
    public Mono<CarDto> buyCarById(@PathVariable Long id) {
        return carService.buyCarById(id);
    }
}
