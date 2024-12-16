package com.nuvei.ecomm.order.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CreateOrderRequest {

    private String orderNumber;
    private String status;
    private double actualPrice;
    private double discountedPrice;
    private List<Item> items;

}
