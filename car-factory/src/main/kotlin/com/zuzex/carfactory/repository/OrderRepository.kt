package com.zuzex.carfactory.repository

import com.zuzex.carfactory.model.Order
import com.zuzex.common.model.Status
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {

    fun findByIdAndStatus(id: Long, status: Status): Order?
}
