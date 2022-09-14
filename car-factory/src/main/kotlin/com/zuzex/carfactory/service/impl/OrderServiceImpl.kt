package com.zuzex.carfactory.service.impl

import com.zuzex.carfactory.callback.KafkaCarStatusSendCallback
import com.zuzex.carfactory.model.Order
import com.zuzex.carfactory.repository.OrderRepository
import com.zuzex.carfactory.service.OrderService
import com.zuzex.common.dto.CarStatusDto
import com.zuzex.common.dto.ChangeStatusDto
import com.zuzex.common.exception.NotFoundException
import com.zuzex.common.model.Action
import com.zuzex.common.model.Status
import com.zuzex.common.util.ResponseConstant.ACTION_NOT_FOUND
import com.zuzex.common.util.ResponseConstant.ORDER_NOT_FOUND
import com.zuzex.common.util.ResponseConstant.STATUS_NOT_FOUND
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val kafkaTemplate: KafkaTemplate<String, CarStatusDto>,
    private val kafkaSendCallback: KafkaCarStatusSendCallback
) : OrderService {

    @Value("\${kafka.car-status-topic}")
    private lateinit var carStatusTopic: String

    override fun findAllOrders(): List<Order> = orderRepository.findAll()

    override fun findOrderById(id: Long): Order =
        orderRepository.findByIdOrNull(id) ?: throw NotFoundException(ORDER_NOT_FOUND)

    override fun createNewOrderByCarIdAndDescription(carId: Long, description: String?): Order =
        orderRepository.save(Order(status = Status.ORDER_CREATED, carId = carId, description = description))

    @Transactional
    override fun changeOrderStatusById(id: Long, action: String): Order =
        when (stringToAction(action)) {
            Action.ASSEMBLE -> assembleOrderById(id)
            Action.DELIVER -> deliverOrderById(id)
        }

    override fun revertOrderStatusById(id: Long): Order =
        orderRepository.findByIdOrNull(id)
            ?.let {
                when (it.status) {
                    Status.CAR_ASSEMBLED -> orderRepository.save(it.copy(status = Status.ORDER_CREATED))
                    Status.ORDER_CREATED -> orderRepository.save(it.copy(status = Status.CAR_ASSEMBLED))
                    else -> throw NotFoundException(STATUS_NOT_FOUND)
                }
            }
            ?: throw NotFoundException(ORDER_NOT_FOUND)

    private fun stringToAction(action: String): Action =
        enumValues<Action>().find { it.name.equals(action, ignoreCase = true) }
            ?: throw NotFoundException(ACTION_NOT_FOUND)

    private fun assembleOrderById(id: Long): Order =
        changeOrderStatusByIdAndSend(
            ChangeStatusDto(
                id,
                Status.ORDER_CREATED,
                Status.CAR_ASSEMBLED,
                Status.CAR_ASSEMBLED
            )
        )

    private fun deliverOrderById(id: Long): Order =
        changeOrderStatusByIdAndSend(
            ChangeStatusDto(
                id,
                Status.CAR_ASSEMBLED,
                Status.ORDER_COMPLETED,
                Status.ON_SALE
            )
        )

    private fun changeOrderStatusByIdAndSend(params: ChangeStatusDto): Order =
        orderRepository.findByIdAndStatus(params.orderId, params.currentOrderStatus)
            ?.let {
                sendNewStatusEvent(CarStatusDto(params.orderId, it.carId, params.newCarStatus))
                orderRepository.save(it.copy(status = params.newOrderStatus))
            }
            ?: throw NotFoundException(ORDER_NOT_FOUND)

    private fun sendNewStatusEvent(carStatusDto: CarStatusDto) {
        kafkaTemplate.send(carStatusTopic, carStatusDto).addCallback(kafkaSendCallback)
    }
}
