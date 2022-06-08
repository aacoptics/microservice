package com.aacoptics.notification.mapper;

import com.aacoptics.notification.entity.UmsContent;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UmsContentMapper extends BaseMapper<UmsContent> {
}