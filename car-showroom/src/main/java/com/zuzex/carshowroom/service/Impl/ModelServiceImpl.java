package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.mapper.ModelMapper;
import com.zuzex.carshowroom.model.Model;
import com.zuzex.carshowroom.repository.ModelRepository;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.common.dto.ModelDto;
import com.zuzex.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Model> findAllModels() {
        return modelRepository.findAll();
    }

    @Override
    public Model findModelById(Long id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Such model not found"));
    }

    @Override
    public Model createNewModel(ModelDto modelDto) {
        return modelRepository.save(modelMapper.modelDtoToModel(modelDto));
    }
}
