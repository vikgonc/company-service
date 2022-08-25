package com.zuzex.carshowroom.service;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.model.Status;

import java.util.List;

public interface CarService {

    List<Car> findAllCars();

    List<Car> findAllCarsOnSale();

    Car findCarById(Long id);

    Car orderNewCar(OrderCarDto orderCarDto);

    Car setCarStatusById(Long id, Status status);

    Car buyCarById(Long id);
}
