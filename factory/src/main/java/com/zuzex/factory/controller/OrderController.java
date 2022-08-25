package com.zuzex.factory.controller;

import com.zuzex.factory.model.Order;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> findAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{id}")
    public Order findOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }

    @PostMapping("/assemble/{id}")
    public Order assembleOrderById(@PathVariable Long id) {
        return orderService.assembleOrderById(id);
    }

    @PostMapping("/deliver/{id}")
    public Order deliverOrderById(@PathVariable Long id) {
        return orderService.deliverOrderById(id);
    }
}
