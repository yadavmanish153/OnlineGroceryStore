package com.nuvei.ecomm.order.service;

import com.nuvei.ecomm.order.model.Order;
import com.nuvei.ecomm.order.model.OrderedItem;
import com.nuvei.ecomm.order.repository.OrderedItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedItemService {

    private static final Logger log = LoggerFactory.getLogger(OrderedItemService.class);

    @Autowired
    private OrderedItemRepository orderedItemRepository;

    public String saveOrderedItem(OrderedItem orderedItem){
        OrderedItem savedOrderedItem = orderedItemRepository.save(orderedItem);
        if(savedOrderedItem != null){
            return "Item added";
        }
        return "Item not added in order.";
    }

    public List<OrderedItem> getItemsByOrderNum(Order order){
        try{
            List<OrderedItem> listOrderedItem = orderedItemRepository.findByOrderNumber(order);
            return listOrderedItem;
        } catch (Exception ex){
            log.debug(ex.getMessage());
            throw new RuntimeException("Exception occured while fetching Ordered Items details for order number: "+order.getOrderNumber());
        }
    }
}
