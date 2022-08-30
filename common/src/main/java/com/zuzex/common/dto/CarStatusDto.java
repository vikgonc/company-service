package com.zuzex.common.dto;

import com.zuzex.common.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarStatusDto {

    private long orderId;
    private long carId;
    private Status status;
}
