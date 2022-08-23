package com.zuzex.factory.service.Impl;

import com.zuzex.factory.model.Order;
import com.zuzex.factory.repository.OrderRepository;
import com.zuzex.factory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

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

    @KafkaListener(topics = "car-order")
    public void hello(Long id){
        System.out.println(id);
    }
}
