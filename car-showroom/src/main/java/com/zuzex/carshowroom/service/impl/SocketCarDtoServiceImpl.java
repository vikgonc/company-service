package com.zuzex.carshowroom.service.impl;

import com.zuzex.carshowroom.dto.CarDto;
import com.zuzex.carshowroom.service.SocketCarDtoService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class SocketCarDtoServiceImpl implements SocketCarDtoService {

    private final Sinks.Many<CarDto> processor = Sinks.many().multicast().directAllOrNothing();

    @Override
    public void onNext(CarDto next) {
        processor.tryEmitNext(next);
    }

    @Override
    public Flux<CarDto> getMessages() {
        return processor.asFlux();
    }
}
