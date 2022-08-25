package com.zuzex.factory.service.Impl;

import com.zuzex.common.dto.OrderDto;
import com.zuzex.factory.model.Order;
import com.zuzex.factory.service.EventListener;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${kafka.order-topic}")
public class KafkaEventListenerImpl implements EventListener {

    private final OrderService orderService;

    @Override
    @KafkaHandler
    public void handleNewOrder(OrderDto orderDto) {
        log.info("Message \"{}\" received", orderDto);

        Order orderAfterSave
                = orderService.createNewOrderByCarIdAndDescription(orderDto.getCarId(), orderDto.getOrderDescription());

        log.info("Order \"{}\" saved", orderAfterSave);
    }
}
