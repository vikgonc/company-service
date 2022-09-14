package com.zuzex.carfactory.listener

import com.zuzex.carfactory.service.OrderService
import com.zuzex.common.dto.CarStatusResultDto
import com.zuzex.common.model.EventStatus
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
@KafkaListener(
    topics = ["\${kafka.car-status-result-topic}"],
    containerFactory = "carStatusResultKafkaListenerContainerFactory"
)
class KafkaCarStatusResultListener(private val orderService: OrderService) {

    @KafkaHandler
    fun handleCarStatusResult(carStatusResultDto: CarStatusResultDto) {
        log.info("Result of save car status: $carStatusResultDto")
        if (carStatusResultDto.eventStatus == EventStatus.SUCCESS)
            log.info("Transaction success")
        else
            orderService.revertOrderStatusById(carStatusResultDto.orderId)
                .also { log.info("Order status reverted: $it") }
    }
}
