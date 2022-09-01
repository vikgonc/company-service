package com.zuzex.carshowroom.configuration;

import com.zuzex.carshowroom.mapper.CarReadConvertor;
import com.zuzex.carshowroom.mapper.CarWriteConvertor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class R2dbcConfiguration {

    @Bean
    public R2dbcCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new CarReadConvertor());
        converters.add(new CarWriteConvertor());
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }
}
