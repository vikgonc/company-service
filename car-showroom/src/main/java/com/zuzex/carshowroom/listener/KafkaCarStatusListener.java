package com.zuzex.carshowroom.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.ShowroomWebSocketService;
import com.zuzex.common.dto.CarStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Objects;

import static com.zuzex.common.util.ThrowingConsumer.throwingConsumerWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaCarStatusListener {

    private final CarService carService;
    private final ShowroomWebSocketService showroomWebSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.car-status-topic}", containerFactory = "carStatusKafkaListenerContainerFactory")
    public void handleCarStatus(CarStatusDto carStatusDto) throws IOException {
        log.info("Message '{}' received", carStatusDto);

        Car savedCar = carService.setCarStatusById(carStatusDto.getCarId(), carStatusDto.getStatus());
        notifyWebSocketClient(savedCar);
        log.info("Car '{}' saved", savedCar);
    }

    private void notifyWebSocketClient(Car updatedCar) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(updatedCar);

        showroomWebSocketHandler.getActiveSessions().stream()
                .filter(Objects::nonNull)
                .forEach(throwingConsumerWrapper(activeSession
                        -> activeSession.sendMessage(new TextMessage(jsonResponse))));

    }
}
