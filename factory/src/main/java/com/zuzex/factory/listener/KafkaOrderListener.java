package com.zuzex.factory.listener;

import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.dto.OrderResultDto;
import com.zuzex.common.model.EventStatus;
import com.zuzex.factory.model.Order;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderListener {

    private final OrderService orderService;

    @SendTo("${kafka.order-result-topic}")
    @KafkaListener(topics = "${kafka.order-topic}", containerFactory = "orderKafkaListenerContainerFactory")
    public OrderResultDto handleNewOrder(OrderDto orderDto) {
        log.info("Message '{}' received", orderDto);
        try {
            Order orderAfterSave
                    = orderService.createNewOrderByCarIdAndDescription(orderDto.getCarId(), orderDto.getOrderDescription());
            log.info("Order '{}' saved", orderAfterSave);

            return OrderResultDto.builder()
                    .carId(orderDto.getCarId())
                    .eventStatus(EventStatus.SUCCESS)
                    .build();
        } catch (Exception e) {
            log.info("Something went wrong... {}", e.getMessage());

            return OrderResultDto.builder()
                    .carId(orderDto.getCarId())
                    .eventStatus(EventStatus.FAILED)
                    .build();
        }
    }
}
