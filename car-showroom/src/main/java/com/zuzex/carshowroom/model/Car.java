package com.zuzex.carshowroom.model;

import com.zuzex.common.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("cars")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Car {

    @Id
    @Column("id")
    private Long id;

    @Column("price")
    private Double price;

    @Column("status")
    private Status status;

    private Model model;
}
