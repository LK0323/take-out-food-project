package com.lk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lk.reggie.dto.SetmealDto;
import com.lk.reggie.eneity.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时保存套餐和菜品的关联信息
     *
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐和菜品的关联信息
     *
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 修改套餐以及套餐和菜品关联信息
     *
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
}
