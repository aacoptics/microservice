package com.aacoptics.sep.mapper;

import com.aacoptics.sep.entity.po.SepClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SepClientMapper extends BaseMapper<SepClient> {

    List<SepClient> getHardwareKey(@Param("computerName") String computerName);
}