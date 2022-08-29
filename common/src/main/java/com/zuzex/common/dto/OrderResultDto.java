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
public class OrderResultDto {

    private long carId;
    private EventStatus eventStatus;
}
