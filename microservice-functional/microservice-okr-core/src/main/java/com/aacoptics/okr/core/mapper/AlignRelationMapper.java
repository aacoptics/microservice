package com.aacoptics.okr.core.mapper;

import com.aacoptics.okr.core.entity.po.AlignRelation;
import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AlignRelationMapper extends BaseMapper<AlignRelation> {

    List<AlignRelation> getAlignCountInfo(@Param("objectiveId") Long objectiveId);

    List<AlignRelation> getAlignedCountInfo(@Param("objectiveId") Long objectiveId);
    List<AlignRelation> listAlignedByOId(@Param("objectiveId") Long objectiveId);

}