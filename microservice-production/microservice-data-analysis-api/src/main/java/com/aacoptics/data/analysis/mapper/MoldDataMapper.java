package com.aacoptics.data.analysis.mapper;

import com.aacoptics.data.analysis.entity.po.MoldData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MoldDataMapper extends BaseMapper<MoldData> {
//    @Update("UPDATE t_mold_data tmd INNER JOIN t_structure_data tsd \n" +
//            "SET tmd.department=tsd.department,tmd.lens_number=tsd.lens_number,tmd.category=tsd.category,tmd.material=tsd.material\n" +
//            "WHERE tmd.project=tsd.project AND tmd.part_name=tsd.part_name")
    @Update("UPDATE tmd\n" +
            "SET tmd.department=tsd.department,tmd.lens_number=tsd.lens_number,tmd.category=tsd.category,tmd.material=tsd.material\n" +
            "FROM t_mold_data tmd,`t_structure_data` tsd \n" +
            "WHERE tmd.project=tsd.project AND tmd.part_name=tsd.part_name")
    void syncData();


    @Update("TRUNCATE TABLE t_mold_data")
    void deleteData();
}
