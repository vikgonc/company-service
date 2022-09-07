package com.zuzex.carshowroom.mapper;

import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.dto.OrderResultDto;
import com.zuzex.common.grpc.dto.CommonDto;
import com.zuzex.common.model.EventStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public CommonDto.OrderDto orderDtoToGrpcOrderDto(OrderDto orderDto) {
        return CommonDto.OrderDto.newBuilder()
                .setCarId(orderDto.getCarId())
                .setOrderDescription(orderDto.getOrderDescription())
                .build();
    }

    public OrderResultDto grpcOrderResultDtoToOrderResultDto(CommonDto.OrderResultDto orderResultDto) {
        return OrderResultDto.builder()
                .carId(orderResultDto.getCarId())
                .eventStatus(EventStatus.valueOf(orderResultDto.getEventStatus()))
                .build();
    }
}
