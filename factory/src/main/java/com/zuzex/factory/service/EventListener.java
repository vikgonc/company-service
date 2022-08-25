package com.zuzex.factory.service;

import com.zuzex.common.dto.OrderDto;

public interface EventListener {

    void handleNewOrder(OrderDto orderDto);
}
