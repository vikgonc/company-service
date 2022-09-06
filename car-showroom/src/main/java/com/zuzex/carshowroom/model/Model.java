package com.zuzex.carshowroom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("models")
@NoArgsConstructor
@AllArgsConstructor
public class Model {

    @Id
    private Long id;

    @Column("brand")
    private String brand;

    @Column("model_name")
    private String modelName;

    @Column("description")
    private String description;
}
