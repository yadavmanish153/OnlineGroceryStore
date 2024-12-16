package com.nuvei.ecomm.order.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CreateOrderResponse {

    private String message;
    private String orderNumber;
}
