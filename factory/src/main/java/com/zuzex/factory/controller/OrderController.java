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
    public List<Order> findAll() {
        return orderService.findAll();
    }

    @PostMapping("/assemble/{orderId}")
    public Order assembleOrder(@PathVariable Long orderId) {
        return orderService.assembleOrder(orderId);
    }

    @PostMapping("/deliver/{orderId}")
    public Order deliverOrder(@PathVariable Long orderId) {
        return orderService.deliverOrder(orderId);
    }
}
