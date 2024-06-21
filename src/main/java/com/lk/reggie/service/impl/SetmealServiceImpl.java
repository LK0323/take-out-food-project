package com.lk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lk.reggie.common.CustomException;
import com.lk.reggie.dto.SetmealDto;
import com.lk.reggie.eneity.Setmeal;
import com.lk.reggie.eneity.SetmealDish;
import com.lk.reggie.mapper.SetmealMapper;
import com.lk.reggie.service.SetmealDishService;
import com.lk.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时保存套餐和菜品的关联信息
     *
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联信息
     *
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        if (count > 0) {
            //若不能删除，抛出业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //若能删除，先删除套餐表中数据--setmeal
        this.removeByIds(ids);
        //再删除关系表中数据--setmeal_dish
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(lambdaQueryWrapper);
    }

    /**
     * 修改套餐以及套餐和菜品关联信息
     *
     * @param setmealDto
     */
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        //更新setmeal表
        this.updateById(setmealDto);
        //更新setmeal_dish表
        // 删除操作
        Long setmealId = setmealDto.getId();

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealId);

        setmealDishService.remove(queryWrapper);

        // 新填操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }
}
