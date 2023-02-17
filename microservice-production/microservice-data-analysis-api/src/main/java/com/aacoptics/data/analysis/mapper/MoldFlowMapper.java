package com.aacoptics.data.analysis.mapper;

import com.aacoptics.data.analysis.entity.po.MoldFlowData;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MoldFlowMapper extends BaseMapper<MoldFlowData> {
//    @Update("UPDATE t_mold_flow tmf INNER JOIN t_structure_data tsd\n" +
//            "SET tmf.department=tsd.department,tmf.lens_number=tsd.lens_number,tmf.category=tsd.category,tmf.material=tsd.material\n" +
//            "WHERE tmf.project=tsd.project AND tmf.part_name=tsd.part_name")
    @Update("UPDATE tmf\n" +
            "SET tmf.department=tsd.department,tmf.lens_number=tsd.lens_number,tmf.category=tsd.category,tmf.material=tsd.material\n" +
            "FROM t_mold_flow tmf,t_structure_data tsd\n" +
            "WHERE tmf.project=tsd.project AND tmf.part_name=tsd.part_name")
    void syncData();

//    @Update("UPDATE t_mold_flow tmf INNER JOIN t_mold_data tmd\n" +
//            "SET tmf.mold_type=tmd.mold_type\n" +
//            "WHERE tmf.project=tmd.project AND tmf.part_name=tmd.part_name")
    @Update("UPDATE tmf\n" +
            "SET tmf.mold_type=tmd.mold_type\n" +
            "FROM t_mold_flow tmf,t_mold_data tmd\n" +
            "WHERE tmf.project=tmd.project AND tmf.part_name=tmd.part_name")
    void syncMoldType();
}
