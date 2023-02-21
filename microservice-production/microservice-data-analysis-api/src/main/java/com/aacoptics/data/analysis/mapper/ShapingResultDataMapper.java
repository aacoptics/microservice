package com.aacoptics.data.analysis.mapper;

import com.aacoptics.data.analysis.entity.po.ShapingResultData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ShapingResultDataMapper extends BaseMapper<ShapingResultData> {
//    @Update("UPDATE t_shaping_result_data srd INNER JOIN t_structure_data tsd\n" +
//            "SET srd.department=tsd.department,srd.lens_number=tsd.lens_number,srd.category=tsd.category,srd.material=tsd.material,srd.structure_no=tsd.structure_schemes_no\n" +
//            "WHERE srd.project=tsd.project AND srd.part_name=tsd.part_name")
    @Update("UPDATE srd\n" +
            "SET srd.department=tsd.department,srd.lens_number=tsd.lens_number,srd.category=tsd.category,srd.material=tsd.material,srd.structure_no=tsd.structure_schemes_no\n" +
            "FROM t_shaping_result_data srd,t_structure_data tsd\n" +
            "WHERE srd.project=tsd.project AND srd.part_name=tsd.part_name")
    void syncData();

//    @Update("UPDATE t_shaping_result_data srd INNER JOIN t_mold_data tmd\n" +
//            "SET srd.mold_type=tmd.mold_type,srd.mold_type_no=tmd.mold_type_total\n" +
//            "WHERE srd.project=tmd.project AND srd.part_name=tmd.part_name")
    @Update("UPDATE srd\n" +
            "SET srd.mold_type=tmd.mold_type,srd.mold_type_no=tmd.mold_type_total\n" +
            "FROM t_shaping_result_data srd,t_mold_data tmd\n" +
            "WHERE srd.project=tmd.project AND srd.part_name=tmd.part_name")
    void syncMoldType();
}
