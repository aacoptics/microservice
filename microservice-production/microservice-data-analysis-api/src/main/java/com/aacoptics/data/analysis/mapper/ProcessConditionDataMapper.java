package com.aacoptics.data.analysis.mapper;

import com.aacoptics.data.analysis.entity.po.ProcessConditionData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface ProcessConditionDataMapper extends BaseMapper<ProcessConditionData> {
    @Update("UPDATE t_process_condition_data pcd INNER JOIN t_structure_data tsd\n" +
            "SET pcd.department=tsd.department,pcd.lens_number=tsd.lens_number,pcd.category=tsd.category,pcd.material=tsd.material\n" +
            "WHERE pcd.project=tsd.project AND pcd.part_name=tsd.part_name")
//    @Update("UPDATE pcd\n" +
//            "SET pcd.department=tsd.department,pcd.lens_number=tsd.lens_number,pcd.category=tsd.category,pcd.material=tsd.material\n" +
//            "FROM t_process_condition_data pcd,t_structure_data tsd\n" +
//            "WHERE pcd.project=tsd.project AND pcd.part_name=tsd.part_name")
    void syncData();

    @Update("UPDATE t_process_condition_data pcd INNER JOIN t_mold_data tmd\n" +
            "SET pcd.mold_type=tmd.mold_type\n" +
            "WHERE pcd.project=tmd.project AND pcd.part_name=tmd.part_name")
//    @Update("UPDATE pcd\n" +
//            "SET pcd.mold_type=tmd.mold_type\n" +
//            "FROM t_process_condition_data pcd,t_mold_data tmd\n" +
//            "WHERE pcd.project=tmd.project AND pcd.part_name=tmd.part_name")
    void syncMoldType();
}
