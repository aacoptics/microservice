package com.aacoptics.feishu.photo.mapper;

import com.aacoptics.feishu.photo.entity.po.FeishuUser;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@DS("user_db")
public interface FeishuUserMapper extends BaseMapper<FeishuUser> {


}