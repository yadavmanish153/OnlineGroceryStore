package com.nuvei.ecomm.order.service;

import com.nuvei.ecomm.order.model.Discount;
import com.nuvei.ecomm.order.model.DiscountStatus;
import com.nuvei.ecomm.order.model.ItemType;
import com.nuvei.ecomm.order.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public Discount save(Discount discount){
        return discountRepository.save(discount);
    }

    public Optional<Discount> getDiscount(Long discountId){
        return discountRepository.findById(discountId);
    }

    public List<Discount> getAllDiscount(){
        return discountRepository.findAll();
    }

    public List<Discount> getActiveDiscountOnItemType(ItemType itemType){
        return discountRepository.findByItemTypeAndDiscountStatus(itemType, DiscountStatus.ACTIVE);
    }


}
