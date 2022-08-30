package com.zuzex.carshowroom.mapper;

import com.zuzex.carshowroom.model.Model;
import com.zuzex.common.dto.ModelDto;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public Model modelDtoToModel(ModelDto modelDto) {
        return Model.builder()
                .brand(modelDto.getBrand())
                .modelName(modelDto.getModelName())
                .description(modelDto.getDescription())
                .build();
    }
}
