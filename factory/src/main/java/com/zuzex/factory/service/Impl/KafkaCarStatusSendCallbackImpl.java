package com.zuzex.factory.service.Impl;

import com.zuzex.common.callback.BaseKafkaSendCallback;
import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.factory.model.Order;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaCarStatusSendCallbackImpl extends BaseKafkaSendCallback<String, CarStatusDto> {

    @Lazy
    private final OrderService orderService;

    @Override
    public void onFailure(@NonNull KafkaProducerException ex) {
        log.info("Message \"{}\" failed for reason: {}", ex.getFailedProducerRecord().value(), ex.getMessage());
        ProducerRecord<String, CarStatusDto> failedProducerRecord = ex.getFailedProducerRecord();
        Long invalidOrderId = failedProducerRecord.value().getCarId();

        Order orderAfterSave = orderService.revertOrderStatusById(invalidOrderId);
        log.info("Consistency returned: {}", orderAfterSave);
    }
}
