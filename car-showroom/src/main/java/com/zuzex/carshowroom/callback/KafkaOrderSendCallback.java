package com.zuzex.carshowroom.callback;

import com.zuzex.carshowroom.service.CarService;
import com.zuzex.common.callback.BaseKafkaSendCallback;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.model.Status;
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
public class KafkaOrderSendCallback extends BaseKafkaSendCallback<String, OrderDto> {

    @Lazy
    private final CarService carService;

    @Override
    public void onFailure(@NonNull KafkaProducerException ex) {
        log.info("Message '{}' failed for reason: {}", ex.getFailedProducerRecord().value(), ex.getMessage());
        ProducerRecord<String, OrderDto> failedProducerRecord = ex.getFailedProducerRecord();
        Long invalidCarId = failedProducerRecord.value().getCarId();

        carService.setCarStatusById(invalidCarId, Status.ORDER_CANCELED)
                .doOnNext(savedCar -> log.info("Consistency returned: {}", savedCar))
                .subscribe();
    }
}
