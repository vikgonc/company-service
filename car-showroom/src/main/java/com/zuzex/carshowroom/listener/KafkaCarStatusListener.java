package com.zuzex.carshowroom.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.ShowroomWebSocketService;
import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.dto.CarStatusResultDto;
import com.zuzex.common.model.EventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Objects;

import static com.zuzex.common.util.ThrowingConsumer.throwingConsumerWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${kafka.car-status-topic}", containerFactory = "carStatusKafkaListenerContainerFactory")
public class KafkaCarStatusListener {

    private final CarService carService;
    private final ShowroomWebSocketService showroomWebSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaHandler
    @SendTo("${kafka.car-status-result-topic}")
    public CarStatusResultDto handleCarStatus(CarStatusDto carStatusDto) {
        try {
            log.info("Message '{}' received", carStatusDto);
            Car savedCar = carService.setCarStatusById(carStatusDto.getCarId(), carStatusDto.getStatus());
            notifyWebSocketClient(savedCar);
            log.info("Car '{}' saved", savedCar);

            return CarStatusResultDto.builder()
                    .orderId(carStatusDto.getOrderId())
                    .eventStatus(EventStatus.SUCCESS)
                    .build();

        } catch (Exception e) {
            log.info("Something went wrong... {}", e.getMessage());

            return CarStatusResultDto.builder()
                    .orderId(carStatusDto.getOrderId())
                    .eventStatus(EventStatus.FAILED)
                    .build();
        }
    }

    private void notifyWebSocketClient(Car updatedCar) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(updatedCar);

        showroomWebSocketHandler.getActiveSessions().stream()
                .filter(Objects::nonNull)
                .forEach(throwingConsumerWrapper(activeSession
                        -> activeSession.sendMessage(new TextMessage(jsonResponse))));

    }
}
