package com.nuvei.ecomm.order.repository;

import com.nuvei.ecomm.order.model.Discount;
import com.nuvei.ecomm.order.model.DiscountStatus;
import com.nuvei.ecomm.order.model.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByItemType(ItemType itemType);
    List<Discount> findByDiscountStatus(DiscountStatus discountStatus);
    List<Discount> findByItemTypeAndDiscountStatus(ItemType itemType, DiscountStatus discountStatus);

}
