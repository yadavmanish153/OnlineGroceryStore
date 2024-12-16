package com.nuvei.ecomm.order.service;

import com.nuvei.ecomm.order.controller.OrderController;
import com.nuvei.ecomm.order.dto.CreateOrderRequest;
import com.nuvei.ecomm.order.dto.CreateOrderResponse;
import com.nuvei.ecomm.order.dto.Item;
import com.nuvei.ecomm.order.exception.ItemExpiredException;
import com.nuvei.ecomm.order.exception.OrderNotFoundException;
import com.nuvei.ecomm.order.model.ItemType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderController orderController;

    @Test
    public void generateReceiptByNullOrderNumberTest(){
        String orderNumber = null;
        String expectedRetult = "Order with Order number null not found.";
        try{
            String actualResult = orderService.generateReceipt(orderNumber);
        } catch (OrderNotFoundException ex){
            Assertions.assertEquals(expectedRetult,ex.getErrorMessage());
        }
    }

    @Test
    public void generateReceiptForFreshBreadTest() throws ParseException {
        String expectedRetult = """
        Receipt:
        Item: Bread --> Quantity: 4, weight : 0,0
        Actual Price: 4,0 Euro
        Discounted Price: 4,0 Euro
        ------------------------------
        Total order Price: 4,0 Euro
        Total price after discount: 4,0""";

        LocalDate currentDate = LocalDate.now();

        Date manufacturingDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .itemName("Bread")
                .itemType(ItemType.BREAD.toString())
                .manufacturedDate(manufacturingDate)
                .quantity(4)
                .actualPrice(4).build());
        ResponseEntity<CreateOrderResponse> responseEntity  = orderController.createOrder(CreateOrderRequest.builder()
                .items(items).build());

        String orderNumber = responseEntity.getBody().getOrderNumber();
        String actualResult = orderService.generateReceipt(orderNumber);
        Assertions.assertEquals(expectedRetult,actualResult);
    }

    @Test
    public void generateReceiptFor3DaysOldBreadTest() throws ParseException {
        String expectedRetult = """
        Receipt:
        Item: Bread --> Quantity: 4, weight : 0,0
        Actual Price: 4,0 Euro
        Discount applied: Buy 1 get 2 free on 2-3 days aged bread
        Discounted Price: 2,0 Euro
        ------------------------------
        Total order Price: 4,0 Euro
        Total price after discount: 2,0""";

        LocalDate currentDate = LocalDate.now();
        LocalDate dateSixDaysAgo = currentDate.minusDays(3);

        Date manufacturingDate = Date.from(dateSixDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .itemName("Bread")
                .itemType(ItemType.BREAD.toString())
                .manufacturedDate(manufacturingDate)
                .quantity(4)
                .actualPrice(4).build());
        ResponseEntity<CreateOrderResponse> responseEntity  = orderController.createOrder(CreateOrderRequest.builder()
                .items(items).build());

        String orderNumber = responseEntity.getBody().getOrderNumber();
        String actualResult = orderService.generateReceipt(orderNumber);
        Assertions.assertEquals(expectedRetult,actualResult);
    }

    @Test
    public void generateReceiptFor6DaysOldBreadTest() throws ParseException {
        String expectedRetult = """
        Receipt:
        Item: Bread --> Quantity: 4, weight : 0,0
        Actual Price: 4,0 Euro
        Discount applied: Buy 1 get 3 free on 4-6 days aged bread
        Discounted Price: 1,0 Euro
        ------------------------------
        Total order Price: 4,0 Euro
        Total price after discount: 1,0""";

        LocalDate currentDate = LocalDate.now();
        LocalDate dateSixDaysAgo = currentDate.minusDays(6);

        Date manufacturingDate = Date.from(dateSixDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .itemName("Bread")
                .itemType(ItemType.BREAD.toString())
                .manufacturedDate(manufacturingDate)
                .quantity(4)
                .actualPrice(4).build());
        ResponseEntity<CreateOrderResponse> responseEntity  = orderController.createOrder(CreateOrderRequest.builder()
                .items(items).build());

        String orderNumber = responseEntity.getBody().getOrderNumber();
        String actualResult = orderService.generateReceipt(orderNumber);
        Assertions.assertEquals(expectedRetult,actualResult);
    }

    @Test
    public void generateReceiptForMoreThan6DaysOldBreadTest() throws ParseException {
        String expectedRetult = "Bread has expired. Please update your order.";

        LocalDate currentDate = LocalDate.now();
        LocalDate dateSixDaysAgo = currentDate.minusDays(7);

        Date manufacturingDate = Date.from(dateSixDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .itemName("Bread")
                .itemType(ItemType.BREAD.toString())
                .manufacturedDate(manufacturingDate)
                .quantity(4)
                .actualPrice(4).build());
        ResponseEntity<CreateOrderResponse> responseEntity  = orderController.createOrder(CreateOrderRequest.builder()
                .items(items).build());

        String orderNumber = responseEntity.getBody().getOrderNumber();
        try{
            orderService.generateReceipt(orderNumber);
        } catch (ItemExpiredException ex){
            Assertions.assertEquals(expectedRetult,ex.getErrorMessage());
        }
    }

    @Test
    public void generateReceiptForVegetablesUnder100g() throws ParseException {
        String expectedRetult = """
                Receipt:
                Item: Vegetable --> Quantity: 4, weight : 88,0
                Actual Price: 10,0 Euro
                Discount applied: 5% off on all vegetables up to 100g
                Discounted Price: 9,5 Euro
                ------------------------------
                Total order Price: 10,0 Euro
                Total price after discount: 9,5""";

        LocalDate currentDate = LocalDate.now();
        LocalDate dateSixDaysAgo = currentDate.minusDays(7);

        Date manufacturingDate = Date.from(dateSixDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .itemName("Vegetable")
                .itemType(ItemType.VEGETABLES.toString())
                .manufacturedDate(manufacturingDate)
                .quantity(4)
                .weight(88)
                .actualPrice(10).build());
        ResponseEntity<CreateOrderResponse> responseEntity  = orderController.createOrder(CreateOrderRequest.builder()
                .items(items).build());

        String orderNumber = responseEntity.getBody().getOrderNumber();
        String actualResult = orderService.generateReceipt(orderNumber);
        Assertions.assertEquals(expectedRetult,actualResult);
    }

    @Test
    public void generateReceiptForVegetablesWeightBetween100gTo500g() throws ParseException {
        String expectedRetult = """
                Receipt:
                Item: Vegetable --> Quantity: 4, weight : 120,0
                Actual Price: 10,0 Euro
                Discount applied: 7% off on all vegetables between 100 to 500 g
                Discounted Price: 9,3 Euro
                ------------------------------
                Total order Price: 10,0 Euro
                Total price after discount: 9,3""";

        LocalDate currentDate = LocalDate.now();
        LocalDate dateSixDaysAgo = currentDate.minusDays(7);

        Date manufacturingDate = Date.from(dateSixDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .itemName("Vegetable")
                .itemType(ItemType.VEGETABLES.toString())
                .manufacturedDate(manufacturingDate)
                .quantity(4)
                .weight(120)
                .actualPrice(10).build());
        ResponseEntity<CreateOrderResponse> responseEntity  = orderController.createOrder(CreateOrderRequest.builder()
                .items(items).build());

        String orderNumber = responseEntity.getBody().getOrderNumber();
        String actualResult = orderService.generateReceipt(orderNumber);
        Assertions.assertEquals(expectedRetult,actualResult);
    }

    @Test
    public void generateReceiptForVegetablesWeightMoreThan500g() throws ParseException {
        String expectedRetult = """
                Receipt:
                Item: Vegetable --> Quantity: 4, weight : 520,0
                Actual Price: 10,0 Euro
                Discount applied: 10% off on all vegetables above 500g
                Discounted Price: 9,0 Euro
                ------------------------------
                Total order Price: 10,0 Euro
                Total price after discount: 9,0""";

        LocalDate currentDate = LocalDate.now();
        LocalDate dateSixDaysAgo = currentDate.minusDays(7);

        Date manufacturingDate = Date.from(dateSixDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .itemName("Vegetable")
                .itemType(ItemType.VEGETABLES.toString())
                .manufacturedDate(manufacturingDate)
                .quantity(4)
                .weight(520)
                .actualPrice(10).build());
        ResponseEntity<CreateOrderResponse> responseEntity  = orderController.createOrder(CreateOrderRequest.builder()
                .items(items).build());

        String orderNumber = responseEntity.getBody().getOrderNumber();
        String actualResult = orderService.generateReceipt(orderNumber);
        Assertions.assertEquals(expectedRetult,actualResult);
    }

    @Test
    public void generateReceiptForBeerLessThanPack() throws ParseException {
        String expectedRetult = """
                Receipt:
                Item: Beer --> Quantity: 4, weight : 520,0
                Actual Price: 4,0 Euro
                Discounted Price: 4,0 Euro
                ------------------------------
                Total order Price: 4,0 Euro
                Total price after discount: 4,0""";

        LocalDate currentDate = LocalDate.now();
        LocalDate dateSixDaysAgo = currentDate.minusDays(7);

        Date manufacturingDate = Date.from(dateSixDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .itemName("Beer")
                .itemType(ItemType.BEER.toString())
                .manufacturedDate(manufacturingDate)
                .quantity(4)
                .weight(520)
                .actualPrice(4).build());
        ResponseEntity<CreateOrderResponse> responseEntity  = orderController.createOrder(CreateOrderRequest.builder()
                .items(items).build());

        String orderNumber = responseEntity.getBody().getOrderNumber();
        String actualResult = orderService.generateReceipt(orderNumber);
        Assertions.assertEquals(expectedRetult,actualResult);
    }

    @Test
    public void generateReceiptForBeerMoreThanPack() throws ParseException {
        String expectedRetult = """
                Receipt:
                Item: Beer --> Quantity: 8, weight : 520,0
                Actual Price: 8,0 Euro
                Discount applied: Special price for beer pack of 6
                Discounted Price: 6,0 Euro
                ------------------------------
                Total order Price: 8,0 Euro
                Total price after discount: 6,0""";

        LocalDate currentDate = LocalDate.now();
        LocalDate dateSixDaysAgo = currentDate.minusDays(7);

        Date manufacturingDate = Date.from(dateSixDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .itemName("Beer")
                .itemType(ItemType.BEER.toString())
                .manufacturedDate(manufacturingDate)
                .quantity(8)
                .weight(520)
                .actualPrice(8).build());
        ResponseEntity<CreateOrderResponse> responseEntity  = orderController.createOrder(CreateOrderRequest.builder()
                .items(items).build());

        String orderNumber = responseEntity.getBody().getOrderNumber();
        String actualResult = orderService.generateReceipt(orderNumber);
        Assertions.assertEquals(expectedRetult,actualResult);
    }


}