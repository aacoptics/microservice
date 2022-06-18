package com.aacoptics.sep.mapper;

import com.aacoptics.sep.entity.po.SepClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper

public interface SepClientMapper extends BaseMapper<SepClient> {

}