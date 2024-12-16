package com.nuvei.ecomm.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.zip.DeflaterOutputStream;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private String description;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    @Enumerated(EnumType.STRING)
    private DiscountOn discountOn;
    private Integer minAge;
    private Integer maxAge;
    private Double percentageOff;
    private Double packPrice;
    private Double minUnit;
    private Double maxUnit;
    @Enumerated(EnumType.STRING)
    private DiscountStatus discountStatus;
    private Date startFrom;
    private Date endTill;

}
