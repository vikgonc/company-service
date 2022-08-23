package com.zuzex.carshowroom.controller;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.common.dto.CarDto;
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
    public List<Car> findAllModels() {
        return carService.findAll();
    }

    @GetMapping("/{id}")
    public Car findModelById(@PathVariable Long id) {
        return carService.findById(id);
    }

    @PostMapping
    public Car createNewModel(@RequestBody CarDto carDto) {
        return carService.createNewCar(carDto);
    }
}
