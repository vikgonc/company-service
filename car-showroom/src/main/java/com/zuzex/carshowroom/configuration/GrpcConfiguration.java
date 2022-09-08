package com.zuzex.carshowroom.configuration;

import com.zuzex.common.grpc.service.CommonServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Setter
@Configuration
@ConfigurationProperties(prefix = "grpc-server")
public class GrpcConfiguration {

    private String host;
    private Integer port;

    @Bean
    public CommonServiceGrpc.CommonServiceBlockingStub grpcStub() {
        var channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        return CommonServiceGrpc
                .newBlockingStub(channel);
    }

    @Bean
    public Executor grpcCallbackExecutor() {
        return Executors.newCachedThreadPool();
    }
}
