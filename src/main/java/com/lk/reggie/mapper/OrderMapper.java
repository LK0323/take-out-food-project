package com.lk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lk.reggie.eneity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
