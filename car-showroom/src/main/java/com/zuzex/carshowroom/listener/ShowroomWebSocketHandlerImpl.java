package com.zuzex.carshowroom.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.carshowroom.service.SocketCarDtoService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ShowroomWebSocketHandlerImpl implements WebSocketHandler {

    private final SocketCarDtoService socketCarDtoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @NonNull
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(socketCarDtoService
                .getMessages()
                .flatMap(msg -> Mono.create(monoSink -> {
                    try {
                        monoSink.success(objectMapper.writeValueAsString(msg));
                    } catch (JsonProcessingException e) {
                        monoSink.error(e);
                    }
                }))
                .cast(String.class)
                .startWith("Welcome!")
                .map(session::textMessage));
    }
}
