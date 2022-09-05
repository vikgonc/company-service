package com.zuzex.carshowroom.listener;

import com.zuzex.carshowroom.dto.CarDto;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.carshowroom.service.SocketCarDtoService;
import com.zuzex.common.dto.CarStatusDto;
import com.zuzex.common.dto.CarStatusResultDto;
import com.zuzex.common.model.EventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${kafka.car-status-topic}", containerFactory = "carStatusKafkaListenerContainerFactory")
public class KafkaCarStatusListener {

    private final CarService carService;
    private final SocketCarDtoService socketCarDtoService;

    @KafkaHandler
    @SendTo("${kafka.car-status-result-topic}")
    public CarStatusResultDto handleCarStatus(CarStatusDto carStatusDto) {
        try {
            log.info("Message '{}' received", carStatusDto);

            return carService.setCarStatusById(carStatusDto.getCarId(), carStatusDto.getStatus())
                    .doOnNext(carDto -> {
                        log.info("Car '{}' saved", carDto);
                        notifyWebSocketClient(carDto);
                    })
                    .thenReturn(CarStatusResultDto.builder()
                            .orderId(carStatusDto.getOrderId())
                            .eventStatus(EventStatus.SUCCESS)
                            .build())
                    .block();

        } catch (Exception e) {
            log.info("Something went wrong... {}", e.getMessage());

            return CarStatusResultDto.builder()
                    .orderId(carStatusDto.getOrderId())
                    .eventStatus(EventStatus.FAILED)
                    .build();
        }
    }

    private void notifyWebSocketClient(CarDto updatedCar) {
        socketCarDtoService.onNext(updatedCar);
        log.info("Websocket client notified");
    }
}
