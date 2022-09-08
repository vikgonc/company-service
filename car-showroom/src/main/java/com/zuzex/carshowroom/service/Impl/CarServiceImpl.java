package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.dto.CarDto;
import com.zuzex.carshowroom.mapper.CarMapper;
import com.zuzex.carshowroom.mapper.OrderMapper;
import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.repository.CarRepository;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.carshowroom.service.RollbackService;
import com.zuzex.common.aop.TimeTrackable;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.dto.OrderResultDto;
import com.zuzex.common.exception.NotFoundException;
import com.zuzex.common.grpc.dto.CommonDto;
import com.zuzex.common.grpc.service.CommonServiceGrpc;
import com.zuzex.common.model.EventStatus;
import com.zuzex.common.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

import static com.zuzex.common.util.ResponseConstant.CAR_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelService modelService;
    private final CarMapper carMapper;
    private final OrderMapper orderMapper;
    private final CommonServiceGrpc.CommonServiceBlockingStub grpcStub;
    private final RollbackService rollbackService;

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
    public Mono<CarDto> orderNewCar(OrderCarDto orderCarDto) {
        return modelService.findModelById(orderCarDto.getModelId())
                .flatMap(model -> carRepository.save(carMapper.orderCarDtoToCar(orderCarDto, Status.PENDING))
                        .map(car -> {
                            sendNewOrderEvent(carMapper.toOrderDto(car.getId(), orderCarDto.getOrderDescription()));
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
        try {
            CommonDto.OrderResultDto carOrderResult = grpcStub
                    .withDeadlineAfter(10, TimeUnit.SECONDS)
                    .createNewCarOrder(orderMapper.orderDtoToGrpcOrderDto(orderDto));

            OrderResultDto resultDto = orderMapper.grpcOrderResultDtoToOrderResultDto(carOrderResult);
            log.info("Result of send car order: {}", resultDto);

            if (resultDto.getEventStatus().equals(EventStatus.SUCCESS)) {
                rollbackService.acceptCarOrderChanges(resultDto.getCarId());
            } else {
                rollbackService.declineCarOrderChanges(resultDto.getCarId());
            }
        } catch (Exception ex) {
            log.error("Request send error");
            rollbackService.declineCarOrderChanges(orderDto.getCarId());
        }
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
