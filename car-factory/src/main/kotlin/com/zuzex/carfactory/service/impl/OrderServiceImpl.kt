package com.zuzex.carfactory.service.impl

import com.zuzex.carfactory.model.Order
import com.zuzex.carfactory.service.OrderService
import com.zuzex.common.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl : OrderService {
    override fun findAllOrders(): List<Order> {
        TODO("Not yet implemented")
    }

    override fun findOrderById(id: Long): Order {
       throw NotFoundException("123")
    }

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
