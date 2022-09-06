package com.zuzex.factory.service.Impl;

import com.zuzex.common.aop.TimeTrackable;
import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.exception.NotFoundException;
import com.zuzex.common.model.Action;
import com.zuzex.common.model.Status;
import com.zuzex.factory.mapper.OrderMapper;
import com.zuzex.factory.model.Order;
import com.zuzex.factory.repository.OrderRepository;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Arrays;
import java.util.List;

import static com.zuzex.common.util.ResponseConstant.ACTION_NOT_FOUND;
import static com.zuzex.common.util.ResponseConstant.ORDER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, CarStatusDto> kafkaTemplate;
    private final KafkaSendCallback<String, CarStatusDto> kafkaSendCallback;
    private final OrderMapper orderMapper;

    @Value("${kafka.car-status-topic}")
    private String carStatusTopic;

    @Override
    @TimeTrackable
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND));
    }

    @Override
    public Order createNewOrderByCarIdAndDescription(Long carId, String description) {
        return orderRepository.save(
                Order.builder()
                        .carId(carId)
                        .status(Status.ORDER_CREATED)
                        .description(description)
                        .build()
        );
    }

    @Override
    @Transactional
    public Order changeOrderStatusById(Long id, String action) {
        Action validatedAction = stringToAction(action);

        if (validatedAction.equals(Action.ASSEMBLE)) {
            return assembleOrderById(id);
        } else {
            return deliverOrderById(id);
        }
    }

    @Override
    public Order revertOrderStatusById(Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    if (order.getStatus().equals(Status.CAR_ASSEMBLED)) {
                        order.setStatus(Status.ORDER_CREATED);
                    } else {
                        order.setStatus(Status.CAR_ASSEMBLED);
                    }
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND));
    }

    private Action stringToAction(String action) {
        return Arrays.stream(Action.values())
                .filter(item -> item.name().equalsIgnoreCase(action))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ACTION_NOT_FOUND));
    }

    private Order assembleOrderById(Long id) {
        return changeOrderStatusByIdAndSend(id, Status.ORDER_CREATED, Status.CAR_ASSEMBLED, Status.CAR_ASSEMBLED);
    }

    private Order deliverOrderById(Long id) {
        return changeOrderStatusByIdAndSend(id, Status.CAR_ASSEMBLED, Status.ORDER_COMPLETED, Status.ON_SALE);
    }

    private Order changeOrderStatusByIdAndSend(Long id, Status oldOrderStatus, Status newOrderStatus, Status newCarStatus) {
        Order orderAfterSave = orderRepository.findByIdAndStatus(id, oldOrderStatus)
                .map(order -> orderRepository.save(order.toBuilder()
                        .status(newOrderStatus)
                        .build()))
                .orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND));
        sendNewStatusEvent(orderMapper.orderToCarStatusDto(id, orderAfterSave, newCarStatus));

        return orderAfterSave;
    }

    private void sendNewStatusEvent(CarStatusDto carStatusDto) {
        ListenableFuture<SendResult<String, CarStatusDto>> sendResult = kafkaTemplate.send(carStatusTopic, carStatusDto);
        sendResult.addCallback(kafkaSendCallback);
    }
}
