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
public class ChangeStatusDto {
    long orderId;
    Status currentOrderStatus;
    Status newOrderStatus;
    Status newCarStatus;
}
