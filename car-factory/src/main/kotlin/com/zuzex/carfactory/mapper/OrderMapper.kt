package com.zuzex.carfactory.mapper

import com.zuzex.common.dto.OrderDto
import com.zuzex.common.grpc.dto.CommonDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface OrderMapper {

    fun grpcOrderDtoToOrderDto(orderDto: CommonDto.OrderDto): OrderDto
}
