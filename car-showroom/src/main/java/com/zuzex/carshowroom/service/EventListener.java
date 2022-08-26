package com.zuzex.carshowroom.service;

import com.zuzex.common.dto.CarStatusDto;

import java.io.IOException;

public interface EventListener {

    void handleCarStatus(CarStatusDto carStatusDto) throws IOException;
}
