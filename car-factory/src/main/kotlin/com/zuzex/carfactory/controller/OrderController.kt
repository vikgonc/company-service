package com.zuzex.carfactory.controller

import com.zuzex.carfactory.model.Order
import com.zuzex.carfactory.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(val orderService: OrderService) {

    @GetMapping
    fun findAllOrders(): List<Order> {
        return orderService.findAllOrders()
    }

    @GetMapping("/{id}")
    fun findOrderById(@PathVariable id: Long): Order {
        return orderService.findOrderById(id)
    }

    @PostMapping("/action/{id}")
    fun changeOrderStatus(@PathVariable id: Long, @RequestParam action: String): Order {
        return orderService.changeOrderStatusById(id, action)
    }
}
