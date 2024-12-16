package com.nuvei.ecomm.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MissingOrderNumberException extends RuntimeException{

    private static final long serialVersionUID  =1L;
    private String errorMessage;
}
