package com.zuzex.carshowroom.mapper;

import com.zuzex.carshowroom.model.Car;
import com.zuzex.carshowroom.model.Model;
import com.zuzex.common.model.Status;
import io.r2dbc.spi.Row;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class CarReadConvertor implements Converter<Row, Car> {

    @Override
    public Car convert(@NonNull Row source) {
        Model model = Model.builder()
                .id(source.get("model_id", Long.class))
                .brand(source.get("brand", String.class))
                .modelName(source.get("model_name", String.class))
                .description(source.get("description", String.class))
                .build();

        return Car.builder()
                .id(source.get("id", Long.class))
                .price(source.get("price", Double.class))
                .status(Status.valueOf(source.get("status", String.class)))
                .model(model)
                .build();
    }
}
