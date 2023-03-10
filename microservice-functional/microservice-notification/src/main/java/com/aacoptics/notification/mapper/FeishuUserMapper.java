package com.aacoptics.notification.mapper;

import com.aacoptics.notification.entity.po.FeishuUser;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
@DS("user_db")
public interface FeishuUserMapper extends BaseMapper<FeishuUser> {

    @DS("user_db")
    List<String> getFeishuUserIds(@Param("employeeNos") List<String> employeeNos);

}