package com.aacoptics.mobile.attendance.mapper;

import com.aacoptics.mobile.attendance.entity.po.Employee;
import com.aacoptics.mobile.attendance.entity.po.EmployeePhoto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EmployeePhotoMapper extends BaseMapper<EmployeePhoto> {
    List<EmployeePhoto> listPhotoNeedToUpload();
}