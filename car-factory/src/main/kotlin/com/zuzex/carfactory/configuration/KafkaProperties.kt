package com.zuzex.carfactory.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "kafka")
data class KafkaProperties(
    val bootstrapServer: String,
    val consumerGroupId: String,
    val autoOffsetReset: String
)
