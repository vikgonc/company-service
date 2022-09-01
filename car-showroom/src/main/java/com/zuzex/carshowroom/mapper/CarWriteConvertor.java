package com.zuzex.carshowroom.mapper;

import com.zuzex.carshowroom.model.Car;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.r2dbc.core.Parameter;

import java.util.Objects;

@WritingConverter
public class CarWriteConvertor implements Converter<Car, OutboundRow> {

    @Override
    public OutboundRow convert(@NonNull Car source) {
        OutboundRow outboundRow = new OutboundRow();
        if (Objects.nonNull(source.getId())) {
            outboundRow.put("id", Parameter.from(source.getId()));
        }
        outboundRow.put("status", Parameter.from(source.getStatus()));
        outboundRow.put("price", Parameter.from(source.getPrice()));
        outboundRow.put("model_id", Parameter.from(source.getModel().getId()));

        return outboundRow;
    }
}
