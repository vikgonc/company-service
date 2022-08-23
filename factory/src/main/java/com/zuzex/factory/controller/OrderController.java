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
    public List<Order> findAll(){
        return orderService.findAll();
    }

    @PostMapping("/{orderId}")
    public Order assembleCar(@PathVariable Long orderId) {
        return orderService.assembleCar(orderId);
    }

    @PostMapping("/{orderId}")
    public Order deliverCar(@PathVariable Long orderId) {
        return orderService.deliverCar(orderId);
    }
}
