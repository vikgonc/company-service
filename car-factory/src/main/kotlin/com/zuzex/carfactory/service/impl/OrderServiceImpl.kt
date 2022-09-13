package com.zuzex.carfactory.service.impl

import com.zuzex.carfactory.model.Order
import com.zuzex.carfactory.repository.OrderRepository
import com.zuzex.carfactory.service.OrderService
import com.zuzex.common.exception.NotFoundException
import com.zuzex.common.util.ResponseConstant.ORDER_NOT_FOUND
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(private val orderRepository: OrderRepository) : OrderService {

    override fun findAllOrders(): List<Order> = orderRepository.findAll()

    override fun findOrderById(id: Long): Order =
        orderRepository.findByIdOrNull(id) ?: throw NotFoundException(ORDER_NOT_FOUND)

    override fun createNewOrderByCarIdAndDescription(carId: Long, description: String?): Order {
        TODO("Not yet implemented")
    }

    override fun changeOrderStatusById(id: Long, action: String): Order {
        TODO("Not yet implemented")
    }

    override fun revertOrderStatusById(id: Long): Order {
        TODO("Not yet implemented")
    }
}
