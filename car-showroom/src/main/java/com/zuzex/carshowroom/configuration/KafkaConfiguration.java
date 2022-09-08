package com.zuzex.carshowroom.configuration;

import com.zuzex.common.configuration.BaseKafkaConfiguration;
import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.dto.CarStatusResultDto;
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
    public KafkaTemplate<String, CarStatusResultDto> carStatusResultKafkaTemplate(ProducerFactory<String, CarStatusResultDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CarStatusDto> carStatusKafkaListenerContainerFactory(
            ConsumerFactory<String, CarStatusDto> consumerFactory, KafkaTemplate<String, CarStatusResultDto> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, CarStatusDto> factory = kafkaListenerContainerFactory(consumerFactory);
        factory.setReplyTemplate(kafkaTemplate);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, CarStatusDto> carStatusConsumerFactory() {
        return consumerFactory(bootstrapServer, consumerGroupId, autoOffsetReset, CarStatusDto.class);
    }

    @Bean
    public ProducerFactory<String, CarStatusResultDto> carStatusProducerFactory() {
        return producerFactory(bootstrapServer);
    }
}
