package com.nuvei.ecomm.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

public enum DiscountType {

    BUY_X_GET_Y,
    PERCENTAGE_OFF,
    PACK_X_6,
    EXPIRED

}
