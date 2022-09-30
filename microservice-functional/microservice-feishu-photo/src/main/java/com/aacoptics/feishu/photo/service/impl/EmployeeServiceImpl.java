package com.aacoptics.feishu.photo.service.impl;

import com.aacoptics.feishu.photo.entity.po.Employee;
import com.aacoptics.feishu.photo.mapper.EmployeeMapper;
import com.aacoptics.feishu.photo.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public boolean checkEmployeeExist(String code) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("Code")
                .eq("Code", code)
                .eq("EmpStatus", 1);
        return this.count(queryWrapper) > 0;
    }
}