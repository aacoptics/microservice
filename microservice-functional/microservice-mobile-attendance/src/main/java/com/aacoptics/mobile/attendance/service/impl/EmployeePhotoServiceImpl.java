package com.aacoptics.mobile.attendance.service.impl;

import com.aacoptics.mobile.attendance.entity.po.Employee;
import com.aacoptics.mobile.attendance.entity.po.EmployeePhoto;
import com.aacoptics.mobile.attendance.mapper.EmployeeMapper;
import com.aacoptics.mobile.attendance.mapper.EmployeePhotoMapper;
import com.aacoptics.mobile.attendance.service.EmployeePhotoService;
import com.aacoptics.mobile.attendance.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class EmployeePhotoServiceImpl extends ServiceImpl<EmployeePhotoMapper, EmployeePhoto> implements EmployeePhotoService {
    @Resource
    private EmployeePhotoMapper employeePhotoMapper;

    @Override
    public List<EmployeePhoto> getPhotoByCode(String code){
        QueryWrapper<EmployeePhoto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Code", code);
        return this.list(queryWrapper);
    }

    @Override
    public List<EmployeePhoto> listPhotoNeedToUpload(){
        return employeePhotoMapper.listPhotoNeedToUpload();
    }


}