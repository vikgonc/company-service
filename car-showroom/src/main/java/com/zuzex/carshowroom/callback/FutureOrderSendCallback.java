package com.zuzex.carshowroom.callback;

import com.google.common.util.concurrent.FutureCallback;
import com.zuzex.carshowroom.mapper.OrderMapper;
import com.zuzex.carshowroom.service.CarService;
import com.zuzex.common.dto.OrderResultDto;
import com.zuzex.common.grpc.dto.CommonDto;
import com.zuzex.common.model.EventStatus;
import com.zuzex.common.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FutureOrderSendCallback implements FutureCallback<CommonDto.OrderResultDto> {

    @Lazy
    private final CarService carService;
    private final OrderMapper orderMapper;

    @Override
    public void onSuccess(CommonDto.OrderResultDto result) {
        OrderResultDto resultDto = orderMapper.grpcOrderResultDtoToOrderResultDto(result);
        log.info("Result of save order: {}", resultDto);

        if (resultDto.getEventStatus().equals(EventStatus.SUCCESS)) {
            acceptChanges(resultDto);
        } else {
            declineChanges(resultDto);
        }
    }

    @Override
    public void onFailure(@NonNull Throwable ex) {
        log.info("Something went wrong... {}", ex.getMessage());
    }

    private void acceptChanges(OrderResultDto orderResultDto) {
        carService.setCarStatusById(orderResultDto.getCarId(), Status.ORDER_CREATED)
                .doOnNext(carDto -> log.info("Order created: '{}'", carDto))
                .subscribe();
    }

    private void declineChanges(OrderResultDto orderResultDto) {
        carService.setCarStatusById(orderResultDto.getCarId(), Status.ORDER_CANCELED)
                .doOnNext(carDto -> log.info("Order canceled: '{}'", carDto))
                .subscribe();
    }
}
