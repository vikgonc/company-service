package com.zuzex.carfactory.service

import com.zuzex.carfactory.mapper.OrderMapper
import com.zuzex.common.grpc.dto.CommonDto
import com.zuzex.common.grpc.service.CommonServiceGrpc
import com.zuzex.common.model.EventStatus
import io.grpc.stub.StreamObserver
import mu.KotlinLogging
import org.lognet.springboot.grpc.GRpcService

private val log = KotlinLogging.logger {}

@GRpcService
class GrpcOrderCarService(
    private val orderService: OrderService,
    private val mapper: OrderMapper
) : CommonServiceGrpc.CommonServiceImplBase() {

    override fun createNewCarOrder(
        request: CommonDto.OrderDto,
        responseObserver: StreamObserver<CommonDto.OrderResultDto>
    ) {
        val orderDto = mapper.grpcOrderDtoToOrderDto(request)
        log.info { "Message '$orderDto' received" }
        try {
            orderService.createNewOrderByCarIdAndDescription(orderDto.carId, orderDto.orderDescription)
                .also {
                    log.info("Order '$it' saved")
                    responseObserver.onNext(mapper.toGrpcOrderResultDto(it.carId, EventStatus.SUCCESS))
                }
        } catch (ex: Exception) {
            log.info("Something went wrong... ${ex.message}")
            responseObserver.onNext(mapper.toGrpcOrderResultDto(orderDto.carId, EventStatus.FAILED))
        } finally {
            responseObserver.onCompleted()
        }
    }
}
