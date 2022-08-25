package com.zuzex.carshowroom.controller;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.common.dto.OrderCarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @GetMapping
    public List<Car> findAllCars() {
        return carService.findAllCars();
    }

    @GetMapping("/sale")
    public List<Car> findAllCarsOnSale() {
        return carService.findAllCarsOnSale();
    }

    @GetMapping("/{id}")
    public Car findCarById(@PathVariable Long id) {
        return carService.findCarById(id);
    }

    @PostMapping("/order")
    public Car orderNewCar(@RequestBody OrderCarDto orderCarDto) {
        return carService.orderNewCar(orderCarDto);
    }

    @PostMapping("/buy/{id}")
    public Car buyCarById(@PathVariable Long id) {
        return carService.buyCarById(id);
    }
}
