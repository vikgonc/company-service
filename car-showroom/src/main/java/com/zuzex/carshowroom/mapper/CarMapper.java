package com.zuzex.carshowroom.mapper;

import com.zuzex.carshowroom.dto.CarDto;
import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.model.Model;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.model.Status;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    //todo mapstruct
    public CarDto carToCatDto(Car car, Model model) {
        return CarDto.builder()
                .id(car.getId())
                .price(car.getPrice())
                .status(car.getStatus())
                .model(model)
                .build();
    }

    public Car orderCarDtoToCar(OrderCarDto orderCarDto, Status status) {
        return Car.builder()
                .price(orderCarDto.getPrice())
                .status(status)
                .modelId(orderCarDto.getModelId())
                .build();
    }

    public OrderDto orderCarDtoToOrderDto(long carId, OrderCarDto orderCarDto) {
        return OrderDto.builder()
                .carId(carId)
                .orderDescription(orderCarDto.getOrderDescription())
                .build();
    }
}
