package com.zuzex.carfactory.controller

import com.zuzex.carfactory.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(private val orderService: OrderService) {

    @GetMapping
    fun findAllOrders() = orderService.findAllOrders()

    @GetMapping("/{id}")
    fun findOrderById(@PathVariable id: Long) = orderService.findOrderById(id)

    @PostMapping("/action/{id}")
    fun changeOrderStatus(@PathVariable id: Long, @RequestParam action: String) =
        orderService.changeOrderStatusById(id, action)
}
