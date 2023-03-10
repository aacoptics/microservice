package com.aacoptics.data.analysis.mapper;

import com.aacoptics.data.analysis.entity.po.StructureData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StructureDataMapper extends BaseMapper<StructureData> {

    @Update("TRUNCATE TABLE t_structure_data")
    void deleteData();
}
