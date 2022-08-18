package com.zuzex.common.model;

import lombok.Data;

import java.util.List;

@Data
public class Order {

    private Long id;
    private List<Car> cars;
    private Status status;
}
