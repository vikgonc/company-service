package com.zuzex.common.dto;

import com.zuzex.common.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarStatusDto {

    private Long carId;
    private Status status;
}
