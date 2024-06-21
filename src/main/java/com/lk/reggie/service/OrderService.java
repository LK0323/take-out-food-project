package com.lk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lk.reggie.eneity.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     *
     * @param orders
     */
    public void submit(Orders orders);
}
