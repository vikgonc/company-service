package com.zuzex.carshowroom.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.EventListener;
import com.zuzex.carshowroom.service.ShowroomSocket;
import com.zuzex.common.aop.TimeTrackable;
import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.util.ThrowingConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${kafka.car-status-topic}")
public class KafkaEventListenerImpl implements EventListener {

    private final CarService carService;
    private final ShowroomSocket showroomWebSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @KafkaHandler
    @TimeTrackable
    public void handleCarStatus(CarStatusDto carStatusDto) throws IOException {
        log.info("Message \"{}\" received", carStatusDto);

        Car savedCar = carService.setCarStatusById(carStatusDto.getCarId(), carStatusDto.getStatus());
        notifyWebSocketClient(savedCar);

        log.info("Car \"{}\" saved", savedCar);
    }

    private void notifyWebSocketClient(Car updatedCar) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(updatedCar);

        showroomWebSocketHandler.getActiveSessions().stream()
                .filter(Objects::nonNull)
                .forEach(throwingConsumerWrapper(activeSession
                        -> activeSession.sendMessage(new TextMessage(jsonResponse))));

    }

    private static <T> Consumer<T> throwingConsumerWrapper(
            ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
