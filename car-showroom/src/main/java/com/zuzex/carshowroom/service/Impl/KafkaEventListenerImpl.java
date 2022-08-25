package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.EventListener;
import com.zuzex.common.dto.CarStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${kafka.car-status-topic}")
public class KafkaEventListenerImpl implements EventListener {

    private final CarService carService;

    @Override
    @KafkaHandler
    public void handleCarStatus(CarStatusDto carStatusDto) {
        log.info("Message \"{}\" received", carStatusDto);

        Car savedCar = carService.setCarStatusById(carStatusDto.getCarId(), carStatusDto.getStatus());

        log.info("Car \"{}\" saved", savedCar);
    }
}
