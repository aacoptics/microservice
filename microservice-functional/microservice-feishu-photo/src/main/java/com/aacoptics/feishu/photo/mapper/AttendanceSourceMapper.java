package com.aacoptics.feishu.photo.mapper;

import com.aacoptics.feishu.photo.entity.po.AttendanceSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AttendanceSourceMapper extends BaseMapper<AttendanceSource> {

}