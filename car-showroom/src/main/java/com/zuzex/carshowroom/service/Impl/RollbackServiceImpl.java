package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.RollbackService;
import com.zuzex.common.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RollbackServiceImpl implements RollbackService {

    @Lazy
    private final CarService carService;

    @Override
    public void acceptCarOrderChanges(Long carId) {
        carService.setCarStatusById(carId, Status.ORDER_CREATED)
                .doOnNext(carDto -> log.info("Order created: '{}'", carDto))
                .subscribe();
    }

    @Override
    public void declineCarOrderChanges(Long carId) {
        carService.setCarStatusById(carId, Status.ORDER_CANCELED)
                .doOnNext(carDto -> log.info("Order canceled: '{}'", carDto))
                .subscribe();
    }
}
