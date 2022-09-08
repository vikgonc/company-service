package com.zuzex.factory.mapper;

import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.grpc.dto.CommonDto;
import com.zuzex.common.model.EventStatus;
import com.zuzex.common.model.Status;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    CarStatusDto toCarStatusDto(Long orderId, Long carId, Status status);

    OrderDto grpcOrderDtoToOrderDto(CommonDto.OrderDto orderDto);

    CommonDto.OrderResultDto toGrpcOrderResultDto(Long carId, EventStatus eventStatus);
}
