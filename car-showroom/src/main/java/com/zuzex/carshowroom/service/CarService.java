package com.zuzex.carshowroom.service;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.common.dto.CarDto;

import java.util.List;

public interface CarService {

    List<Car> findAll();

    Car findById(Long id);

    Car createNewCar(CarDto carDto);
}
