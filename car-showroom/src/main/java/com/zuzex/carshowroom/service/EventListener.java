package com.zuzex.carshowroom.service;

import com.zuzex.common.dto.CarStatusDto;

public interface EventListener {

    void handleCarStatus(CarStatusDto carStatusDto);
}
