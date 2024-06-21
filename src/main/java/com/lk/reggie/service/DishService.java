package com.lk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lk.reggie.dto.DishDto;
import com.lk.reggie.eneity.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品，同时加入菜品和口味，操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品和口味信息
    public void updateWithFlavor(DishDto dishDto);

}
