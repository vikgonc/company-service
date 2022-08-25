package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.repository.CarRepository;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.common.dto.OrderCarDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.exception.NotFoundException;
import com.zuzex.common.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
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

    @Value("${kafka.order-topic}")
    private String orderTopic;

    @Override
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
        Car carAfterSave = carRepository.save(
                Car.builder()
                        .price(orderCarDto.getPrice())
                        .status(Status.ORDER_CREATED)
                        .model(modelService.findModelById(orderCarDto.getModelId()))
                        .build()
        );
        OrderDto orderDto = new OrderDto(carAfterSave.getId(), orderCarDto.getOrderDescription());
        sendNewOrderInFactoryService(orderDto);

        return carAfterSave;
    }

    @Override
    public Car setCarStatusById(Long id, Status status) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setStatus(status);
                    return carRepository.save(car);
                })
                .orElseThrow(() -> new NotFoundException("Such car is not found"));
    }

    @Override
    public Car buyCarById(Long id) {
        Car carForSale = carRepository.findByIdAndStatus(id, Status.ON_SALE)
                .orElseThrow(() -> new NotFoundException(CAR_NOT_FOUND));
        carForSale.setStatus(Status.SOLD);

        return carRepository.save(carForSale);
    }

    private void sendNewOrderInFactoryService(OrderDto orderDto) {
        ListenableFuture<SendResult<String, OrderDto>> sendResult = kafkaTemplate.send(orderTopic, orderDto);
        sendResult.addCallback(new KafkaSendCallback<>() {

            @Override
            public void onSuccess(SendResult<String, OrderDto> result) {
                log.info("Message \"{}\" sent successful", orderDto);
            }

            @Override
            public void onFailure(@NonNull KafkaProducerException ex) {
                log.info("Message \"{}\" failed for reason: {}", orderDto, ex.getMessage());
                ProducerRecord<String, OrderDto> failedProducerRecord = ex.getFailedProducerRecord();
                Long invalidCarId = failedProducerRecord.value().getCarId();

                Car fixedCarRecord = setCarStatusById(invalidCarId, Status.PROCESSING_FAILED);
                log.info("Consistency returned: {}", fixedCarRecord);
            }
        });
    }
}
