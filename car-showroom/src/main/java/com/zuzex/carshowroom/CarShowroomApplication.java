package com.zuzex.carshowroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zuzex.carshowroom", "com.zuzex.common"})
public class CarShowroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarShowroomApplication.class, args);
    }
}
