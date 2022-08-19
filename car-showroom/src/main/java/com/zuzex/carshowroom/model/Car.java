package com.zuzex.carshowroom.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "on_sale")
    private Boolean isOnSale;

    @ManyToOne
    private Model model;
}
