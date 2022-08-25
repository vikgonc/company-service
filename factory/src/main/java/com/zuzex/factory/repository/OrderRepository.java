package com.zuzex.factory.repository;

import com.zuzex.common.model.Status;
import com.zuzex.factory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndStatus(Long id, Status status);
}
