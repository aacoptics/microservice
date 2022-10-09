package com.aacoptics.data.analysis.mapper;

import com.aacoptics.data.analysis.entity.po.AllData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AllDataMapper extends BaseMapper<AllData> {
    Page<AllData> getAllDataByConditionsWithPage(Page<AllData> page,
                                         @Param("category") String category,
                                         @Param("project") String project,
                                         @Param("partName") String partName,
                                         @Param("material") String material);

    List<AllData> getAllDataByConditions(@Param("category") String category,
                             @Param("project") String project,
                             @Param("partName") String partName,
                             @Param("material") String material);
}
