package com.lk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lk.reggie.eneity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
