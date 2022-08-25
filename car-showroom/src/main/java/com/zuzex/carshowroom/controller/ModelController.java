package com.zuzex.carshowroom.controller;

import com.zuzex.carshowroom.model.Model;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.common.dto.ModelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/models")
public class ModelController {

    private final ModelService modelService;

    @GetMapping
    public List<Model> findAllModels() {
        return modelService.findAllModels();
    }

    @GetMapping("/{id}")
    public Model findModelById(@PathVariable Long id) {
        return modelService.findModelById(id);
    }

    @PostMapping("/create")
    public Model createNewModel(@RequestBody ModelDto modelDto) {
        return modelService.createNewModel(modelDto);
    }
}
