package com.lk.reggie.dto;

import com.lk.reggie.eneity.Dish;
import com.lk.reggie.eneity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
