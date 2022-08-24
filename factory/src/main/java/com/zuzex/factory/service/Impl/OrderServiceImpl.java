package com.zuzex.factory.service.Impl;

import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.exception.NotFoundException;
import com.zuzex.common.model.Status;
import com.zuzex.factory.model.Order;
import com.zuzex.factory.repository.OrderRepository;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
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
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, CarStatusDto> kafkaTemplate;

    @Override
    public List<Order> findAll() {
        Iterable<Order> allOrders = orderRepository.findAll();
        return IterableUtils.toList(allOrders);
    }

    @Override
    @KafkaListener(topics = "orders")
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
    @Transactional
    public Order assembleOrder(Long orderId) {
        Order orderToAssemble = orderRepository.findByIdAndStatus(orderId, Status.ORDER_CREATED)
                .orElseThrow(() -> new NotFoundException("Such order not found"));
        orderToAssemble.setStatus(Status.CAR_ASSEMBLED);
        Order assembledOrder = orderRepository.save(orderToAssemble);

        CarStatusDto carStatusDto = new CarStatusDto(assembledOrder.getCarId(), Status.CAR_ASSEMBLED);
        createNewStatusMessageInShowroomService(carStatusDto);

        return assembledOrder;
    }

    @Override
    @Transactional
    public Order deliverOrder(Long orderId) {
        Order orderToDeliver = orderRepository.findByIdAndStatus(orderId, Status.CAR_ASSEMBLED)
                .orElseThrow(() -> new NotFoundException("Such order not found"));
        orderToDeliver.setStatus(Status.ORDER_COMPLETED);
        Order deliveredOrder = orderRepository.save(orderToDeliver);

        CarStatusDto carStatusDto = new CarStatusDto(deliveredOrder.getCarId(), Status.ON_SALE);
        createNewStatusMessageInShowroomService(carStatusDto);

        return deliveredOrder;
    }

    private void createNewStatusMessageInShowroomService(CarStatusDto carStatusDto) {
        ListenableFuture<SendResult<String, CarStatusDto>> sendResult = kafkaTemplate.send("car-status", carStatusDto);
        try {
            sendResult.get(3, TimeUnit.SECONDS);
            log.info("Message \"{}\" sent successful", carStatusDto);
        } catch (Exception e) {
            log.info("Message \"{}\" failed for reason: {}", carStatusDto, e.getMessage());
            throw new KafkaException(e.getMessage());
        }
    }
}
