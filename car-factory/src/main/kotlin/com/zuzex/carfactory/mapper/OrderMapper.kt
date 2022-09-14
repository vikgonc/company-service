package com.zuzex.carfactory.mapper

import com.zuzex.common.dto.OrderDto
import com.zuzex.common.grpc.dto.CommonDto
import com.zuzex.common.model.EventStatus
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface OrderMapper {

    fun grpcOrderDtoToOrderDto(orderDto: CommonDto.OrderDto): OrderDto

    fun toGrpcOrderResultDto(carId: Long?, eventStatus: EventStatus): CommonDto.OrderResultDto
}
