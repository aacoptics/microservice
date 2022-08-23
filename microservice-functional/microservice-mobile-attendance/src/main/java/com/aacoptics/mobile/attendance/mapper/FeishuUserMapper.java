package com.aacoptics.mobile.attendance.mapper;

import com.aacoptics.mobile.attendance.entity.po.FeishuUser;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@DS("user_db")
public interface FeishuUserMapper extends BaseMapper<FeishuUser> {


}