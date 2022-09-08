package com.zuzex.carshowroom.mapper;

import com.zuzex.carshowroom.model.Model;
import com.zuzex.common.dto.ModelDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelMapper {

    Model modelDtoToModel(ModelDto modelDto);
}
