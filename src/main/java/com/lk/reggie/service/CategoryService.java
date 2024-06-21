package com.lk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lk.reggie.eneity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
