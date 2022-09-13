package com.zuzex.carfactory.configuration

import com.zuzex.common.configuration.BaseKafkaConfiguration
import com.zuzex.common.dto.CarStatusDto
import com.zuzex.common.dto.CarStatusResultDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaConfiguration(private val kafkaProperties: KafkaProperties) : BaseKafkaConfiguration() {

    @Bean
    fun carStatusKafkaTemplate(producerFactory: ProducerFactory<String, CarStatusDto>) = KafkaTemplate(producerFactory)

    @Bean
    fun carStatusResultKafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, CarStatusResultDto>
    ): ConcurrentKafkaListenerContainerFactory<String, CarStatusResultDto> =
        kafkaListenerContainerFactory(consumerFactory)

    @Bean
    fun carStatusResultConsumerFactory(): ConsumerFactory<String, CarStatusResultDto> =
        consumerFactory(
            kafkaProperties.bootstrapServer,
            kafkaProperties.consumerGroupId,
            kafkaProperties.autoOffsetReset,
            CarStatusResultDto::class.java
        )

    @Bean
    fun carStatusProducerFactory(): ProducerFactory<String, CarStatusDto> =
        producerFactory(kafkaProperties.bootstrapServer)
}
