package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.mapper.CarMapper;
import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.repository.CarRepository;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.common.aop.TimeTrackable;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.exception.NotFoundException;
import com.zuzex.common.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private static final String CAR_NOT_FOUND = "Such car is not found";

    private final CarRepository carRepository;
    private final ModelService modelService;
    private final KafkaTemplate<String, OrderDto> kafkaTemplate;
    private final KafkaSendCallback<String, OrderDto> kafkaSendCallback;
    private final CarMapper carMapper;

    @Value("${kafka.order-topic}")
    private String orderTopic;

    @Override
    @TimeTrackable
    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findAllCarsOnSale() {
        return carRepository.findAllByStatusEquals(Status.ON_SALE);
    }

    @Override
    public Car findCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CAR_NOT_FOUND));
    }

    @Override
    @Transactional
    public Car orderNewCar(OrderCarDto orderCarDto) {
        Car carAfterSave = carRepository.save(carMapper.orderCarDtoToCar(orderCarDto, Status.PENDING, modelService.findModelById(orderCarDto.getModelId())));
        sendNewOrderEvent(carMapper.orderCarDtoToOrderDto(carAfterSave.getId(), orderCarDto));

        return carAfterSave;
    }

    @Override
    public Car setCarStatusById(Long id, Status status) {
        return carRepository.findById(id)
                .map(car -> carRepository.save(car.toBuilder()
                        .status(status)
                        .build()))
                .orElseThrow(() -> new NotFoundException("Such car is not found"));
    }

    @Override
    public Car buyCarById(Long id) {
        return carRepository.findByIdAndStatus(id, Status.ON_SALE)
                .map(car -> carRepository.save(car.toBuilder()
                        .status(Status.SOLD)
                        .build()))
                .orElseThrow(() -> new NotFoundException(CAR_NOT_FOUND));
    }

    private void sendNewOrderEvent(OrderDto orderDto) {
        ListenableFuture<SendResult<String, OrderDto>> sendResult = kafkaTemplate.send(orderTopic, orderDto);
        sendResult.addCallback(kafkaSendCallback);
    }
}
