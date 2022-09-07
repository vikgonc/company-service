package com.zuzex.factory.mapper;

import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.dto.OrderResultDto;
import com.zuzex.common.grpc.dto.CommonDto;
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

    public OrderDto grpcOrderDtoToOrderDto(CommonDto.OrderDto orderDto) {
        return OrderDto.builder()
                .carId(orderDto.getCarId())
                .orderDescription(orderDto.getOrderDescription())
                .build();
    }

    public CommonDto.OrderResultDto orderResultDtoToGrpcOrderResultDto(OrderResultDto orderResultDto) {
        return CommonDto.OrderResultDto.newBuilder()
                .setCarId(orderResultDto.getCarId())
                .setEventStatus(orderResultDto.getEventStatus().toString())
                .build();
    }
}
