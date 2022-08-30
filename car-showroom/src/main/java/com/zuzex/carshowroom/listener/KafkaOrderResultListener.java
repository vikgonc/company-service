package com.zuzex.carshowroom.listener;

import com.zuzex.carshowroom.service.CarService;
import com.zuzex.common.dto.OrderResultDto;
import com.zuzex.common.model.EventStatus;
import com.zuzex.common.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${kafka.order-result-topic}", containerFactory = "orderResultKafkaListenerContainerFactory")
public class KafkaOrderResultListener {

    private final CarService carService;

    @KafkaHandler
    public void handleOrderResult(OrderResultDto orderResultDto) {
        log.info("Result of save order: {}", orderResultDto);

        if (orderResultDto.getEventStatus().equals(EventStatus.SUCCESS)) {
            carService.setCarStatusById(orderResultDto.getCarId(), Status.ORDER_CREATED);
            log.info("Order created");
        } else {
            carService.setCarStatusById(orderResultDto.getCarId(), Status.ORDER_CANCELED);
            log.info("Order canceled");
        }
    }
}
