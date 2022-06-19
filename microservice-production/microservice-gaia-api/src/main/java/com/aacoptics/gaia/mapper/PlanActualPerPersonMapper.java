package com.aacoptics.gaia.mapper;

import com.aacoptics.gaia.entity.po.PlanActualPerPerson;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface PlanActualPerPersonMapper extends BaseMapper<PlanActualPerPerson> {

    List<PlanActualPerPerson> getPlanInfoByTime(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}
