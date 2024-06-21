package com.lk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lk.reggie.common.BaseContext;
import com.lk.reggie.common.R;
import com.lk.reggie.eneity.OrderDetail;
import com.lk.reggie.eneity.Orders;
import com.lk.reggie.dto.OrdersDto;
import com.lk.reggie.service.OrderService;
import com.lk.reggie.service.impl.OrderDetailServiceImpl;
import com.lk.reggie.service.impl.ShoppingCartServiceImpl;
import com.lk.reggie.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailServiceImpl orderDetailService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据:{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 查看手机版历史纪录
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> pagePhone(int page, int pageSize) {

        // 新创返回类型Page
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        // 用户ID
        Long currentId = BaseContext.getCurrentId();

        // 原条件写入
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, currentId);
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo, queryWrapper);

        // 普通赋值
        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");

        // 订单赋值
        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> ordersDtoList = records.stream().map((item) -> {

            // 新创内部元素
            OrdersDto ordersDto = new OrdersDto();

            // 普通值赋值
            BeanUtils.copyProperties(item, ordersDto);

            // 菜单详情赋值
            Long itemId = item.getId();

            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, itemId);

            int count = orderDetailService.count(orderDetailLambdaQueryWrapper);

            List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailLambdaQueryWrapper);

            //ordersDto.setSumNum(count);

            ordersDto.setOrderDetails(orderDetailList);

            return ordersDto;
        }).collect(Collectors.toList());

        // 完成dishDtoPage的results的内容封装
        ordersDtoPage.setRecords(ordersDtoList);

        return R.success(ordersDtoPage);
    }
}
