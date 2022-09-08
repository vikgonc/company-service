package com.zuzex.factory.service;

import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.dto.OrderResultDto;
import com.zuzex.common.grpc.dto.CommonDto;
import com.zuzex.common.grpc.service.CommonServiceGrpc;
import com.zuzex.common.model.EventStatus;
import com.zuzex.factory.mapper.OrderMapper;
import com.zuzex.factory.model.Order;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class GrpcOrderCarService extends CommonServiceGrpc.CommonServiceImplBase {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Override
    public void createNewCarOrder(CommonDto.OrderDto request, StreamObserver<CommonDto.OrderResultDto> responseObserver) {
        OrderDto orderDto = orderMapper.grpcOrderDtoToOrderDto(request);
        log.info("Message '{}' received", orderDto);

        try {
            Order orderAfterSave
                    = orderService.createNewOrderByCarIdAndDescription(orderDto.getCarId(), orderDto.getOrderDescription());
            log.info("Order '{}' saved", orderAfterSave);

            responseObserver.onNext(orderMapper.orderResultDtoToGrpcOrderResultDto(
                    OrderResultDto.builder()
                            .carId(orderAfterSave.getCarId())
                            .eventStatus(EventStatus.SUCCESS)
                            .build()));
        } catch (Exception ex) {
            log.info("Something went wrong... {}", ex.getMessage());

            responseObserver.onNext(orderMapper.orderResultDtoToGrpcOrderResultDto(
                    OrderResultDto.builder()
                            .carId(orderDto.getCarId())
                            .eventStatus(EventStatus.FAILED)
                            .build()));
        } finally {
            responseObserver.onCompleted();
        }
    }
}
