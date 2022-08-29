package com.zuzex.factory.configuration;

import com.zuzex.common.configuration.BaseKafkaConfiguration;
import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.dto.CarStatusResultDto;
import com.zuzex.common.dto.OrderDto;
import com.zuzex.common.dto.OrderResultDto;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfiguration extends BaseKafkaConfiguration {

    private String bootstrapServer;
    private String consumerGroupId;
    private String autoOffsetReset;

    @Bean
    public KafkaTemplate<String, CarStatusDto> carStatusKafkaTemplate(ProducerFactory<String, CarStatusDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, OrderResultDto> orderResultKafkaTemplate(ProducerFactory<String, OrderResultDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDto>
    orderKafkaListenerContainerFactory(ConsumerFactory<String, OrderDto> consumerFactory, KafkaTemplate<String, OrderResultDto> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, OrderDto> factory = kafkaListenerContainerFactory(consumerFactory);
        factory.setReplyTemplate(kafkaTemplate);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CarStatusResultDto>
    carStatusResultKafkaListenerContainerFactory(ConsumerFactory<String, CarStatusResultDto> consumerFactory) {
        return kafkaListenerContainerFactory(consumerFactory);
    }

    @Bean
    public ConsumerFactory<String, OrderDto> orderConsumerFactory() {
        return consumerFactory(bootstrapServer, consumerGroupId, autoOffsetReset, OrderDto.class);
    }

    @Bean
    public ConsumerFactory<String, CarStatusResultDto> carStatusResultConsumerFactory() {
        return consumerFactory(bootstrapServer, consumerGroupId, autoOffsetReset, CarStatusResultDto.class);
    }

    @Bean
    public ProducerFactory<String, CarStatusDto> carStatusProducerFactory() {
        return producerFactory(bootstrapServer);
    }

    @Bean
    public ProducerFactory<String, OrderResultDto> orderResultProducerFactory() {
        return producerFactory(bootstrapServer);
    }
}
