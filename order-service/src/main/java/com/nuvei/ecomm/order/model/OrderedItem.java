package com.nuvei.ecomm.order.model;

import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.ast.Or;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderNumber")
    private Order orderNumber;

    private String itemName;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    private Date manufacturedDate;
    private Integer quantity;
    private double weight;
    private double actualPrice;
    private double discountedPrice;
}
