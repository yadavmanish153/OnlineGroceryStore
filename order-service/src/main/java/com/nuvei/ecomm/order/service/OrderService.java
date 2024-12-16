package com.nuvei.ecomm.order.service;

import com.nuvei.ecomm.order.exception.ItemExpiredException;
import com.nuvei.ecomm.order.exception.OrderNotFoundException;
import com.nuvei.ecomm.order.model.*;
import com.nuvei.ecomm.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderedItemService orderedItemService;
    @Autowired
    private DiscountService discountService;

    public Order saveOrder(Order order) {
        log.info("Start saveOrder method. Order details: "+order.toString());
        try {
            order = orderRepository.save(order);
            log.info("End saveOrder method. Order details: "+order.toString());
            return order;
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            throw new RuntimeException("Exception orrured while creating order:");
        }
    }

    public Order getOrderByOrderNum(String orderNumber){
        try{
            return orderRepository.findByOrderNumber(orderNumber);
        } catch (Exception ex){
            log.debug(ex.getMessage());
            throw new RuntimeException("Exception occured while fetching order details for order number: "+orderNumber);
        }
    }

    public Order updateOrder(Order order) {
        log.info("Start updateOrder method. Order details: "+order.toString());
        try {
            order = orderRepository.save(order);
            log.info("End updateOrder method. Order details: "+order.toString());
            return order;
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            throw new RuntimeException("Exception orrured while updating order:");
        }
    }

    public String generateReceipt(String orderNum) {
        StringBuilder receipt = new StringBuilder("Receipt:\n");
        double totalCost = 0.0;
        Order order = getOrderByOrderNum(orderNum);

        if(order != null){
            List<OrderedItem> orderedItemList = orderedItemService.getItemsByOrderNum(order);
            double totalPrice = 0.0;
            double totalPriceAfterDiscount = 0.0;
            for(OrderedItem item : orderedItemList){
                double itemActualPrice = item.getActualPrice();
                double discountedPrice = itemActualPrice;
                receipt.append(String.format("Item: %s --> Quantity: %d, weight : %.1f\n",item.getItemName(), item.getQuantity(), item.getWeight()));
                receipt.append(String.format("Actual Price: %.1f Euro\n", item.getActualPrice()));
                List<Discount> discountList = discountService.getActiveDiscountOnItemType(item.getItemType());
                if(discountList.size() > 0){
                    discountedPrice = calculateDiscount(receipt, item, discountList);
                    receipt.append(String.format("Discounted Price: %.1f Euro\n",discountedPrice));
                }
                totalPrice = totalPrice + itemActualPrice;
                totalPriceAfterDiscount = totalPriceAfterDiscount + discountedPrice;
                receipt.append("------------------------------\n");
            }
            receipt.append(String.format("Total order Price: %.1f Euro\nTotal price after discount: %.1f",totalPrice,totalPriceAfterDiscount));
        } else {
            throw new OrderNotFoundException(String.format("Order with Order number %s not found.", orderNum));
        }
        return receipt.toString();
    }

    private double calculateDiscount(StringBuilder receipt, OrderedItem item, List<Discount> discountList) {
        for(Discount discount : discountList){
            if(DiscountType.BUY_X_GET_Y.equals(discount.getDiscountType())){
                if(DiscountOn.MANUFACTURED_DATE.equals(discount.getDiscountOn())){

                    LocalDate manufacturingLocalDate = item.getManufacturedDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    LocalDate currentDate = LocalDate.now();

                    long age = ChronoUnit.DAYS.between(manufacturingLocalDate, currentDate);
                    if(age >= discount.getMinAge().intValue() && age <= discount.getMaxAge().intValue()){
                        receipt.append(String.format("Discount applied: %s\n",discount.getDescription()));
                        return item.getQuantity()%(discount.getMinUnit().intValue() + discount.getMaxUnit().intValue()) == 0 ?
                                item.getQuantity()/(discount.getMinUnit().intValue() + discount.getMaxUnit().intValue()) * item.getActualPrice()/item.getQuantity() :
                                (item.getQuantity()/(discount.getMinUnit().intValue() + discount.getMaxUnit().intValue()) +1) * item.getActualPrice()/item.getQuantity() ;
                    }
                }
            } else if(DiscountType.PERCENTAGE_OFF.equals(discount.getDiscountType())){
                if(DiscountOn.WEIGHT.equals(discount.getDiscountOn())){
                    if(item.getWeight() > discount.getMinUnit() && (discount.getMaxUnit().doubleValue() ==0.0 || item.getWeight() <= discount.getMaxUnit())){
                       receipt.append(String.format("Discount applied: %s\n",discount.getDescription()));
                       return item.getActualPrice() - (item.getActualPrice() * discount.getPercentageOff() / 100);
                    }
                } else if(DiscountOn.QUANTITY.equals(discount.getDiscountOn())){
                    //No rules defined
                }
            } else if(DiscountType.PACK_X_6.equals(discount.getDiscountType())){
                if(item.getQuantity() >= discount.getMinUnit().intValue()){
                    receipt.append(String.format("Discount applied: %s\n",discount.getDescription()));
                    double singleItemPrice = item.getActualPrice()/item.getQuantity();
                    return item.getQuantity()/discount.getMinUnit().intValue() * discount.getPackPrice() + item.getQuantity()%6 * singleItemPrice;
                }
            } else if (DiscountType.EXPIRED.equals(discount.getDiscountType())) {
                    if(DiscountOn.MANUFACTURED_DATE.equals(discount.getDiscountOn())){
                        LocalDate manufacturingLocalDate = item.getManufacturedDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        LocalDate currentDate = LocalDate.now();

                        long age = ChronoUnit.DAYS.between(manufacturingLocalDate, currentDate);
                        if(age >= discount.getMinAge().intValue()){
                            throw new ItemExpiredException(String.format("%s has expired. Please update your order.", item.getItemName()));
                        }
                    }
            }
        }
        return item.getActualPrice();
    }


}
