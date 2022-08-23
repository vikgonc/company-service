package com.zuzex.factory.service.Impl;

import com.zuzex.factory.model.Order;
import com.zuzex.factory.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order assembleCar(Long orderId) {
        return null;
    }

    @Override
    public Order deliverCar(Long orderId) {
        return null;
    }
}
