package com.zuzex.carshowroom.mapper;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.model.Model;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.model.Status;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    public Car orderCarDtoToCar(OrderCarDto orderCarDto, Status status, Model model) {
        return Car.builder()
                .price(orderCarDto.getPrice())
                .status(status)
                .model(model)
                .build();
    }

    public OrderDto orderCarDtoToOrderDto(long carId, OrderCarDto orderCarDto) {
        return OrderDto.builder()
                .carId(carId)
                .orderDescription(orderCarDto.getOrderDescription())
                .build();
    }
}
