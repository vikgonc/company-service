package com.zuzex.carshowroom.service;

import com.zuzex.carshowroom.dto.CarDto;
import reactor.core.publisher.Flux;

public interface SocketCarDtoService {

    void onNext(CarDto next);

    Flux<CarDto> getMessages();
}
