//package com.zuzex.carshowroom.configuration;
//
//import com.zuzex.common.configuration.BaseKafkaConfiguration;
//import com.zuzex.common.dto.CarStatusDto;
//import com.zuzex.common.dto.CarStatusResultDto;
//import com.zuzex.common.dto.OrderDto;
//import com.zuzex.common.dto.OrderResultDto;
//import lombok.Setter;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//@Setter
//@Configuration
//@ConfigurationProperties(prefix = "kafka")
//public class KafkaConfiguration extends BaseKafkaConfiguration {
//
//    private String bootstrapServer;
//    private String consumerGroupId;
//    private String autoOffsetReset;
//
//    @Bean
//    public KafkaTemplate<String, OrderDto> orderKafkaTemplate(ProducerFactory<String, OrderDto> producerFactory) {
//        return new KafkaTemplate<>(producerFactory);
//    }
//
//    @Bean
//    public KafkaTemplate<String, CarStatusResultDto> carStatusResultKafkaTemplate(ProducerFactory<String, CarStatusResultDto> producerFactory) {
//        return new KafkaTemplate<>(producerFactory);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, CarStatusDto> carStatusKafkaListenerContainerFactory(
//            ConsumerFactory<String, CarStatusDto> consumerFactory, KafkaTemplate<String, CarStatusResultDto> kafkaTemplate) {
//        ConcurrentKafkaListenerContainerFactory<String, CarStatusDto> factory = kafkaListenerContainerFactory(consumerFactory);
//        factory.setReplyTemplate(kafkaTemplate);
//
//        return factory;
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, OrderResultDto> orderResultKafkaListenerContainerFactory(
//            ConsumerFactory<String, OrderResultDto> consumerFactory) {
//        return kafkaListenerContainerFactory(consumerFactory);
//    }
//
//    @Bean
//    public ConsumerFactory<String, CarStatusDto> carStatusConsumerFactory() {
//        return consumerFactory(bootstrapServer, consumerGroupId, autoOffsetReset, CarStatusDto.class);
//    }
//
//    @Bean
//    public ConsumerFactory<String, OrderResultDto> orderResultConsumerFactory() {
//        return consumerFactory(bootstrapServer, consumerGroupId, autoOffsetReset, OrderResultDto.class);
//    }
//
//    @Bean
//    public ProducerFactory<String, OrderDto> orderProducerFactory() {
//        return producerFactory(bootstrapServer);
//    }
//
//    @Bean
//    public ProducerFactory<String, CarStatusResultDto> carStatusProducerFactory() {
//        return producerFactory(bootstrapServer);
//    }
//}
