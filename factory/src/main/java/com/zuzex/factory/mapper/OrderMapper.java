package com.zuzex.factory.mapper;

import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.model.Status;
import com.zuzex.factory.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public CarStatusDto orderToCarStatusDto(long orderId, Order order, Status status) {
        return CarStatusDto.builder()
                .orderId(orderId)
                .carId(order.getCarId())
                .status(status)
                .build();
    }
}
