package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.dto.CarDto;
import com.zuzex.carshowroom.service.SocketCarDtoService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@Service
public class SocketCarDtoServiceImpl implements SocketCarDtoService {

    private final EmitterProcessor<CarDto> processor = EmitterProcessor.create();

    @Override
    public void onNext(CarDto next) {
        processor.onNext(next);
    }

    @Override
    public Flux<CarDto> getMessages() {
        return processor.publish().autoConnect();
    }
}
