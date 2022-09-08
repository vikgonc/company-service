package com.zuzex.carshowroom.mapper;

import com.zuzex.carshowroom.dto.CarDto;
import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.model.Model;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "car.id", target = "id")
    CarDto carToCatDto(Car car, Model model);

    Car orderCarDtoToCar(OrderCarDto orderCarDto, Status status);

    OrderDto toOrderDto(Long carId, String orderDescription);
}
