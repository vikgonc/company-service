package com.zuzex.factory.service.Impl;

import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.exception.NotFoundException;
import com.zuzex.common.model.Status;
import com.zuzex.factory.model.Order;
import com.zuzex.factory.repository.OrderRepository;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_NOT_FOUND = "Such order is not found";

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, CarStatusDto> kafkaTemplate;

    @Value("${kafka.car-status-topic}")
    private String carStatusTopic;

    @Override
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
    public Order assembleOrderById(Long id) {
        return changeOrderStatusById(id, Status.ORDER_CREATED, Status.CAR_ASSEMBLED, Status.CAR_ASSEMBLED);
    }

    @Override
    @Transactional
    public Order deliverOrderById(Long id) {
        return changeOrderStatusById(id, Status.CAR_ASSEMBLED, Status.ORDER_COMPLETED, Status.ON_SALE);
    }

    private Order changeOrderStatusById(Long id, Status oldOrderStatus, Status newOrderStatus, Status newCarStatus) {
        Order orderAfterSave = orderRepository.findByIdAndStatus(id, oldOrderStatus)
                .map(order -> {
                    order.setStatus(newOrderStatus);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND));
        CarStatusDto carStatusDto = new CarStatusDto(orderAfterSave.getCarId(), newCarStatus);
        sendNewStatusMessageInShowroomService(carStatusDto);

        return orderAfterSave;
    }

    private void sendNewStatusMessageInShowroomService(CarStatusDto carStatusDto) {
        ListenableFuture<SendResult<String, CarStatusDto>> sendResult = kafkaTemplate.send(carStatusTopic, carStatusDto);
        sendResult.addCallback(new KafkaSendCallback<>() {

            @Override
            public void onSuccess(SendResult<String, CarStatusDto> result) {
                log.info("Message \"{}\" sent successful", carStatusDto);
            }

            @Override
            public void onFailure(@NonNull KafkaProducerException ex) {
                log.info("Message \"{}\" failed for reason: {}", carStatusDto, ex.getMessage());
                ProducerRecord<String, CarStatusDto> failedProducerRecord = ex.getFailedProducerRecord();
                Long invalidOrderId = failedProducerRecord.value().getCarId();

                Order fixedOrderRecord = orderRepository.findById(invalidOrderId)
                        .map(order -> {
                            Status newStatus;
                            if (order.getStatus().equals(Status.CAR_ASSEMBLED)) {
                                newStatus = Status.ORDER_CREATED;
                            } else {
                                newStatus = Status.CAR_ASSEMBLED;
                            }
                            order.setStatus(newStatus);
                            return orderRepository.save(order);
                        })
                        .orElse(null);
                log.info("Consistency returned: {}", fixedOrderRecord);
            }
        });
    }
}
