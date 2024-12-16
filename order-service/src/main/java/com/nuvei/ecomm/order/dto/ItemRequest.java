package com.nuvei.ecomm.order.dto;

import java.math.BigDecimal;

public record ItemRequest(Long itemCategoryID, String itemName, Integer quantity, double weight) {
}
