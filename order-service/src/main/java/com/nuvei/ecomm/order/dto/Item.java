package com.nuvei.ecomm.order.dto;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Item {

    private String itemName;
    private String itemType;
    private Date manufacturedDate;
    private Integer quantity;
    private double weight;
    private double actualPrice;
    private boolean isDiscountApplied;
    private double discountedPrice;
}
