package com.zuzex.carshowroom.dto;

import com.zuzex.carshowroom.model.Model;
import com.zuzex.common.model.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDto {
    private Long id;
    private Double price;
    private Status status;
    private Model model;
}
