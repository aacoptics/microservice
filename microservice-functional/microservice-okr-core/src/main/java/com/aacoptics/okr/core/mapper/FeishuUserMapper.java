package com.aacoptics.okr.core.mapper;

import com.aacoptics.okr.core.entity.po.FeishuUser;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
@DS("user_db")
public interface FeishuUserMapper extends BaseMapper<FeishuUser> {

    List<String> employeeNoToLead(String employeeNo);

    List<String> employeeNoToSameLevel(String employeeNo);

}
