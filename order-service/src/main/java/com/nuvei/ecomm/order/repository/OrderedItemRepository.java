package com.nuvei.ecomm.order.repository;

import com.nuvei.ecomm.order.model.Order;
import com.nuvei.ecomm.order.model.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {

    List<OrderedItem> findByOrderNumber(Order orderNumber);
}
