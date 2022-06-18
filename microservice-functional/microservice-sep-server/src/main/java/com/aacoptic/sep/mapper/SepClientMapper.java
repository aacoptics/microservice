package com.aacoptic.sep.mapper;

import com.aacoptic.sep.entity.po.SepClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper

public interface SepClientMapper extends BaseMapper<SepClient> {

}