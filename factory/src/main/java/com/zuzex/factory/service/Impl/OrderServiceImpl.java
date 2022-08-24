package com.zuzex.factory.service.Impl;

import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.model.Status;
import com.zuzex.factory.model.Order;
import com.zuzex.factory.repository.OrderRepository;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> findAll() {
        Iterable<Order> allOrders = orderRepository.findAll();
        return IterableUtils.toList(allOrders);
    }

    @Override
    @KafkaListener(topics = "car-order")
    public void createOrder(OrderDto orderDto) {
        log.info("Message \"{}\" received", orderDto);

        Order orderForSave = Order.builder()
                .carId(orderDto.getCarId())
                .status(Status.ORDER_CREATED)
                .description(orderDto.getOrderDescription())
                .build();
        Order orderAfterSave = orderRepository.save(orderForSave);

        log.info("Order \"{}\" saved", orderAfterSave);
    }

    @Override
    public Order assembleCar(Long orderId) {
        return null;
    }

    @Override
    public Order deliverCar(Long orderId) {
        return null;
    }
}
