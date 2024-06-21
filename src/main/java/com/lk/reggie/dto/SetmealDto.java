package com.lk.reggie.dto;

import com.lk.reggie.eneity.Setmeal;
import com.lk.reggie.eneity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
