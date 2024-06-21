package com.lk.reggie.dto;


import com.lk.reggie.eneity.OrderDetail;
import com.lk.reggie.eneity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
