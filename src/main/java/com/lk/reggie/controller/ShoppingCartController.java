package com.lk.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lk.reggie.common.BaseContext;
import com.lk.reggie.common.R;
import com.lk.reggie.eneity.ShoppingCart;
import com.lk.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            //添加的为菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加的为套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null) {
            //如果存在，在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //如果不存在，则添加购物车，数量默认为一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车...");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }

    /**
     * 数量减少
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        // 设置用户
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        // 基本查找条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        Long dishId = shoppingCart.getDishId();

        // 判断菜品或者套餐

        if (dishId != null) {
            // 购物车为菜品
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            // 购物车为套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // 判断个数，如果为正数，减一；如果为0，抛出异常
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        Integer number = shoppingCartServiceOne.getNumber();

        if (number != 0) {
            shoppingCartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        } else {
            shoppingCartService.removeById(shoppingCartServiceOne);
        }

        return R.success(shoppingCartServiceOne);
    }
}
