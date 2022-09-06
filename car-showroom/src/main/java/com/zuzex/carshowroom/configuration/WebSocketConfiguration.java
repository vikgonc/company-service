package com.zuzex.carshowroom.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebSocketConfiguration {

    private final WebSocketHandler webSocketHandler;

    @Bean
    public HandlerMapping handlerMapping() {
        String path = "/showroom/online";
        Map<String, WebSocketHandler> map = Map.of(path, webSocketHandler);
        return new SimpleUrlHandlerMapping(map, 1);
    }

    @Bean
    public HandlerAdapter wsHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
