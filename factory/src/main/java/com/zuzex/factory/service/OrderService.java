package com.zuzex.factory.service;

import com.zuzex.common.dto.OrderDto;
import com.zuzex.factory.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAll();

    Order findById(Long id);

    void createOrder(OrderDto orderDto);

    Order assemble(Long orderId);

    Order deliver(Long orderId);
}
