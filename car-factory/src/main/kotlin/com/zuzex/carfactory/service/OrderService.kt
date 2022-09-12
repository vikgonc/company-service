package com.zuzex.carfactory.service

import com.zuzex.carfactory.model.Order

interface OrderService {

    fun findAllOrders(): List<Order>

    fun findOrderById(id: Long): Order

    fun createNewOrderByCarIdAndDescription(carId: Long, description: String?): Order

    fun changeOrderStatusById(id: Long, action: String): Order

    fun revertOrderStatusById(id: Long): Order
}
