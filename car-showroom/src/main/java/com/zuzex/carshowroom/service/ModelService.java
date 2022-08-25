package com.zuzex.carshowroom.service;

import com.zuzex.carshowroom.model.Model;
import com.zuzex.common.dto.ModelDto;

import java.util.List;

public interface ModelService {

    List<Model> findAllModels();

    Model findModelById(Long id);

    Model createNewModel(ModelDto modelDto);
}
