package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.repository.CarRepository;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.common.dto.CarDto;
import com.zuzex.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final KafkaTemplate<String, Long> kafkaTemplate;

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
    public Car createNewCar(CarDto carDto) {
        kafkaTemplate.send("car-order", 231L);
        return null;
    }
}
