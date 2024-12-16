package com.nuvei.ecomm.order.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "\"ORDER\"")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private double totalPrice;
    private double discountedPrice;

}
