package com.zuzex.common.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KafkaTemplateException extends RuntimeException {

    public KafkaTemplateException(String message) {
        super(message);
    }
}
