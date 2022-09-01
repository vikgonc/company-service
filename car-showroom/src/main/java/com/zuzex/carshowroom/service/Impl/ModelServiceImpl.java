package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.mapper.ModelMapper;
import com.zuzex.carshowroom.model.Model;
import com.zuzex.carshowroom.repository.ModelRepository;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.common.aop.TimeTrackable;
import com.zuzex.common.dto.ModelDto;
import com.zuzex.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;

    @Override
    @TimeTrackable
    public Flux<Model> findAllModels() {
        return modelRepository.findAll();
    }

    @Override
    public Mono<Model> findModelById(Long id) {
        return modelRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Such model not found")));
    }

    @Override
    public Mono<Model> createNewModel(ModelDto modelDto) {
        return modelRepository.save(modelMapper.modelDtoToModel(modelDto));
    }
}
