package com.lk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lk.reggie.eneity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
