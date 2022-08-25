package com.zuzex.factory.service;

import com.zuzex.factory.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAllOrders();

    Order findOrderById(Long id);

    Order createNewOrderByCarIdAndDescription(Long carId, String description);

    Order assembleOrderById(Long id);

    Order deliverOrderById(Long id);

    Order revertOrderStatusById(Long id);
}
