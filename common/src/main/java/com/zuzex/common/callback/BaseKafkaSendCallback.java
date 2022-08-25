package com.zuzex.common.callback;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;

import java.util.Optional;

@Slf4j
public class BaseKafkaSendCallback<K, V> implements KafkaSendCallback<K, V> {

    @Override
    public void onSuccess(@Nullable SendResult<K, V> result) {
        Optional.ofNullable(result).ifPresent(sendResult -> log.info("Message \"{}\" sent successful", sendResult.getProducerRecord().value()));
    }

    @Override
    public void onFailure(@NonNull KafkaProducerException ex) {
        log.info("Message \"{}\" failed for reason: {}", ex.getFailedProducerRecord().value(), ex.getMessage());
    }
}
