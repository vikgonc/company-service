package com.zuzex.carshowroom.mapper;

import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.dto.OrderResultDto;
import com.zuzex.common.grpc.dto.CommonDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    CommonDto.OrderDto orderDtoToGrpcOrderDto(OrderDto orderDto);

    OrderResultDto grpcOrderResultDtoToOrderResultDto(CommonDto.OrderResultDto orderResultDto);
}
