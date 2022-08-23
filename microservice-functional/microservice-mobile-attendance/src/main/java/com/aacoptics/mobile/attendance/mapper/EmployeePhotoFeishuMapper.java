package com.aacoptics.mobile.attendance.mapper;

import com.aacoptics.mobile.attendance.entity.po.EmployeePhoto;
import com.aacoptics.mobile.attendance.entity.po.EmployeePhotoFeishu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EmployeePhotoFeishuMapper extends BaseMapper<EmployeePhotoFeishu> {

}