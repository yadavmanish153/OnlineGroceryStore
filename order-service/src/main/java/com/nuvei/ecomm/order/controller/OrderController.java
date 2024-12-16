package com.nuvei.ecomm.order.controller;

import com.nuvei.ecomm.order.dto.CreateOrderRequest;
import com.nuvei.ecomm.order.dto.CreateOrderResponse;
import com.nuvei.ecomm.order.dto.Item;
import com.nuvei.ecomm.order.exception.MissingOrderNumberException;
import com.nuvei.ecomm.order.model.ItemType;
import com.nuvei.ecomm.order.model.Order;
import com.nuvei.ecomm.order.model.OrderStatus;
import com.nuvei.ecomm.order.model.OrderedItem;
import com.nuvei.ecomm.order.service.OrderService;
import com.nuvei.ecomm.order.service.OrderedItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    List<String> items = Arrays.asList("BREAD", "BEER", "VEGETABLES");
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderedItemService orderedItemService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest){
        log.info("Start of OrderController createOrder method");
        if(!isCreateOrderRequestValidated(createOrderRequest))
            throw new IllegalArgumentException("Invalid request data");

        Order order = orderService.saveOrder(Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .status(OrderStatus.NEW).build());

        if(order != null && !order.getOrderNumber().isBlank()) {
            List<Item> items = createOrderRequest.getItems();

            // Save ordered items
            items.forEach(item -> orderedItemService.saveOrderedItem(
                    OrderedItem.builder()
                            .orderNumber(order)
                            .itemName(item.getItemName())
                            .itemType(ItemType.valueOf(item.getItemType().toUpperCase()))
                            .manufacturedDate(item.getManufacturedDate())
                            .quantity(item.getQuantity())
                            .weight(item.getWeight())
                            .actualPrice(item.getActualPrice())
                            .discountedPrice(item.getDiscountedPrice())
                            .build()
            ));

            order.setStatus(OrderStatus.CREATED);
            orderService.updateOrder(order);
        }

        log.info("End of OrderController createOrder method");
        return ResponseEntity.ok(CreateOrderResponse.builder().orderNumber(order.getOrderNumber())
                .message("Order has been created successfully.").build());
    }

    public boolean isCreateOrderRequestValidated(CreateOrderRequest createOrderRequest){
        boolean result = createOrderRequest != null
            && createOrderRequest.getItems() != null
            && createOrderRequest.getItems().stream().anyMatch(item ->
                item != null
                && items.contains(item.getItemType())
                && item.getActualPrice() >= 0.0);
        return result;
    }

    @GetMapping("/generateReceipt")
    public ResponseEntity<String> generateReceipt(String orderNum){
        if(orderNum == null || orderNum.isBlank()){
            throw new MissingOrderNumberException("Order number is missing");
        }
        return ResponseEntity.ok().body(orderService.generateReceipt(orderNum));


    }
}
