package com.zuzex.common.dto;

import com.zuzex.common.model.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarStatusResultDto {
    long orderId;
    EventStatus eventStatus;
}
