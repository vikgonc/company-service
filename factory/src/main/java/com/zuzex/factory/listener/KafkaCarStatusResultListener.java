package com.zuzex.factory.listener;

import com.zuzex.common.dto.CarStatusResultDto;
import com.zuzex.common.model.EventStatus;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${kafka.car-status-result-topic}", containerFactory = "carStatusResultKafkaListenerContainerFactory")
public class KafkaCarStatusResultListener {

    private final OrderService orderService;

    @KafkaHandler
    public void handleCarStatusResult(CarStatusResultDto carStatusResultDto) {
        log.info("Result of save car status: {}", carStatusResultDto);

        if (carStatusResultDto.getEventStatus().equals(EventStatus.SUCCESS)) {
            log.info("Transaction success");
        } else {
            orderService.revertOrderStatusById(carStatusResultDto.getOrderId());
            log.info("Order status reverted");
        }
    }
}
