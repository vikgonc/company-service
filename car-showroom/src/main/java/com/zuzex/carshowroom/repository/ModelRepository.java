package com.zuzex.carshowroom.repository;

import com.zuzex.carshowroom.model.Model;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ModelRepository extends ReactiveCrudRepository<Model, Long> {
}
