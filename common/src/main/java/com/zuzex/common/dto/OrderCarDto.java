package com.zuzex.common.dto;

import lombok.Data;

@Data
public class OrderCarDto {

    private Double price;
    private Long modelId;
    private String orderDescription;
}
