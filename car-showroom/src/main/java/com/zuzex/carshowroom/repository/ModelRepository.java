package com.zuzex.carshowroom.repository;

import com.zuzex.carshowroom.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {
}
