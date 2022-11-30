package com.aacoptics.okr.core.mapper;

import com.aacoptics.okr.core.entity.po.ObjectiveDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ObjectiveDetailMapper extends BaseMapper<ObjectiveDetail> {
    List<ObjectiveDetail> selectByEmployNoAndPeriod(@Param("employNo") String employNo, @Param("period") Long period);

    ObjectiveDetail listAlignByOId(@Param("id") Long id);

    ObjectiveDetail listAlignByKId(@Param("id") Long id);

    ObjectiveDetail listAlignedByOId(@Param("id") Long id);

    ObjectiveDetail listAlignedByKId(@Param("id") Long id);
}