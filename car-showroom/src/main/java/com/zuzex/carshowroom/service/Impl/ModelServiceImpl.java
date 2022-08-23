package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.model.Model;
import com.zuzex.carshowroom.repository.ModelRepository;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.common.dto.ModelDto;
import com.zuzex.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;

    @Override
    public List<Model> findAll() {
        Iterable<Model> allModels = modelRepository.findAll();
        return IterableUtils.toList(allModels);
    }

    @Override
    public Model findById(Long id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Such model not found"));
    }

    @Override
    public Model createNewModel(ModelDto modelDto) {
        Model modelToSave = Model.builder()
                .brand(modelDto.getBrand())
                .modelName(modelDto.getModelName())
                .description(modelDto.getDescription())
                .build();
        return modelRepository.save(modelToSave);
    }
}
