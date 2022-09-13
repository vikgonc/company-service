package com.zuzex.carfactory.callback

import com.zuzex.carfactory.service.OrderService
import com.zuzex.common.callback.BaseKafkaSendCallback
import com.zuzex.common.dto.CarStatusDto
import mu.KotlinLogging
import org.springframework.context.annotation.Lazy
import org.springframework.kafka.core.KafkaProducerException
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class KafkaCarStatusSendCallback(@Lazy private val orderService: OrderService) :
    BaseKafkaSendCallback<String, CarStatusDto>() {

    override fun onFailure(@NonNull ex: KafkaProducerException) {
        val failedProducerRecord = ex.getFailedProducerRecord<String, CarStatusDto>().value()
        log.info("Message '$failedProducerRecord' failed for reason: ${ex.message}")
        orderService.revertOrderStatusById(failedProducerRecord.carId)
            .also { log.info("Consistency returned: $it") }
    }
}
