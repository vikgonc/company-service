package com.zuzex.carshowroom.service;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.dto.OrderCarDto;

import java.util.List;

public interface CarService {

    List<Car> findAll();

    List<Car> findAllOnSale();

    Car findById(Long id);

    Car createNew(OrderCarDto orderCarDto);

    Car buy(Long id);

    void setStatus(CarStatusDto carStatusDto);
}
