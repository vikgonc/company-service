package com.zuzex.carshowroom.service.Impl;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.zuzex.carshowroom.callback.FutureOrderSendCallback;
import com.zuzex.carshowroom.dto.CarDto;
import com.zuzex.carshowroom.mapper.CarMapper;
import com.zuzex.carshowroom.mapper.OrderMapper;
import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.repository.CarRepository;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.common.aop.TimeTrackable;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.exception.NotFoundException;
import com.zuzex.common.grpc.dto.CommonDto;
import com.zuzex.common.grpc.service.CommonServiceGrpc;
import com.zuzex.common.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executor;

import static com.zuzex.common.util.ResponseConstant.CAR_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelService modelService;
    private final CarMapper carMapper;
    private final OrderMapper orderMapper;
    private final CommonServiceGrpc.CommonServiceFutureStub grpcStub;
    private final FutureOrderSendCallback orderCallback;
    private final Executor grpcCallbackExecutor;

    @Override
    @TimeTrackable
    public Flux<CarDto> findAllCars() {
        return createCarFluxDto(carRepository.findAll());
    }

    @Override
    public Flux<CarDto> findAllCarsOnSale() {
        return createCarFluxDto(carRepository.findAllByStatusEquals(Status.ON_SALE));
    }

    @Override
    public Mono<CarDto> findCarById(Long id) {
        return createCarMonoDto(carRepository.findById(id))
                .switchIfEmpty(Mono.error(new NotFoundException(CAR_NOT_FOUND)));
    }

    @Override
    @Transactional
    public Mono<CarDto> orderNewCar(OrderCarDto orderCarDto) {
        return modelService.findModelById(orderCarDto.getModelId())
                .flatMap(model -> carRepository.save(carMapper.orderCarDtoToCar(orderCarDto, Status.PENDING))
                        .map(car -> {
                            sendNewOrderEvent(carMapper.orderCarDtoToOrderDto(car.getId(), orderCarDto));
                            return carMapper.carToCatDto(car, model);
                        }))
                .switchIfEmpty(Mono.error(new NotFoundException(CAR_NOT_FOUND)));
    }

    @Override
    public Mono<CarDto> setCarStatusById(Long id, Status status) {
        return createCarMonoDto(carRepository.findById(id)
                .flatMap(car -> carRepository.save(car.toBuilder().status(status).build())))
                .switchIfEmpty(Mono.error(new NotFoundException(CAR_NOT_FOUND)));
    }

    @Override
    public Mono<CarDto> buyCarById(Long id) {
        return createCarMonoDto(carRepository.findByIdAndStatus(id, Status.ON_SALE)
                .flatMap(car -> carRepository.save(car.toBuilder()
                        .status(Status.SOLD)
                        .build())))
                .switchIfEmpty(Mono.error(new NotFoundException(CAR_NOT_FOUND)));
    }

    private void sendNewOrderEvent(OrderDto orderDto) {
        ListenableFuture<CommonDto.OrderResultDto> carOrderResult = grpcStub.createNewCarOrder(orderMapper.orderDtoToGrpcOrderDto(orderDto));
        Futures.addCallback(carOrderResult, orderCallback, grpcCallbackExecutor);
    }

    private Mono<CarDto> createCarMonoDto(Mono<Car> corePublisher) {
        return corePublisher.flatMap(car ->
                modelService.findModelById(car.getModelId())
                        .map(model -> carMapper.carToCatDto(car, model))
        );
    }

    private Flux<CarDto> createCarFluxDto(Flux<Car> corePublisher) {
        return corePublisher.flatMap(car ->
                modelService.findModelById(car.getModelId())
                        .map(model -> carMapper.carToCatDto(car, model))
        );
    }
}
