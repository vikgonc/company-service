package com.zuzex.common.dto;

import lombok.Data;

@Data
public class OrderCarDto {
    private long modelId;
    private Double price;
    private String orderDescription;
}
