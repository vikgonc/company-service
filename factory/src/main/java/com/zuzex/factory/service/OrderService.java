package com.zuzex.factory.service;

import com.zuzex.factory.model.Order;

import java.util.List;

public interface OrderService {

    public List<Order> findAll();

    Order assembleCar(Long orderId);

    Order deliverCar(Long orderId);
}
