package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.repository.CarRepository;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.common.dto.CarDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.exception.NotFoundException;
import com.zuzex.common.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelService modelService;
    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    @Override
    public List<Car> findAll() {
        Iterable<Car> allCars = carRepository.findAll();
        return IterableUtils.toList(allCars);
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Such car not found"));
    }

    @Override
    @Transactional
    public Car createNewCar(CarDto carDto) {
        Car carForSave = Car.builder()
                .price(carDto.getPrice())
                .status(Status.ORDER_CREATED)
                .model(modelService.findById(carDto.getModelId()))
                .build();
        Car carAfterSave = carRepository.save(carForSave);

        OrderDto orderDto = new OrderDto(carAfterSave.getId(), carDto.getOrderDescription());
        createNewOrderInFactoryService(orderDto);

        return carAfterSave;
    }

    private void createNewOrderInFactoryService(OrderDto orderDto) {
        ListenableFuture<SendResult<String, OrderDto>> sendResult = kafkaTemplate.send("car-order", orderDto);
        try {
            sendResult.get(3, TimeUnit.SECONDS);
            log.info("Message \"{}\" sent successful", orderDto);
        } catch (Exception e) {
            log.info("Message \"{}\" failed for reason: {}", orderDto, e.getMessage());
            throw new KafkaException(e.getMessage());
        }
    }
}
