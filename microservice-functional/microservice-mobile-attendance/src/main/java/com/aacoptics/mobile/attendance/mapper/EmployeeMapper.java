package com.aacoptics.mobile.attendance.mapper;

import com.aacoptics.mobile.attendance.entity.po.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}