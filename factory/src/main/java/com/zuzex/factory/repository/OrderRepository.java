package com.zuzex.factory.repository;

import com.zuzex.factory.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
