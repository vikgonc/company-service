package com.zuzex.carshowroom.service;

import com.zuzex.carshowroom.model.Model;
import com.zuzex.common.dto.ModelDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ModelService {

    Flux<Model> findAllModels();

    Mono<Model> findModelById(Long id);

    Mono<Model> createNewModel(ModelDto modelDto);
}
