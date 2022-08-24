package com.zuzex.factory.service;

import com.zuzex.common.dto.OrderDto;
import com.zuzex.factory.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAll();

    void createOrder(OrderDto orderDto);

    Order assembleOrder(Long orderId);

    Order deliverOrder(Long orderId);
}
