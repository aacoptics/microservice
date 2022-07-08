package com.aacoptics.wlg.report.mapper;

import com.aacoptics.wlg.report.entity.po.MoldUse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MoldUseMapper extends BaseMapper<MoldUse> {

    List<String> findMoldDateByMonth(@Param("projectName") String projectName,
                                     @Param("moldDateStart") LocalDate moldDateStart,
                                     @Param("moldDateEnd") LocalDate moldDateEnd);


    List<Map<String, Object>> findMoldUseByMonth(@Param("selectColumn") String selectColumn,
                                                  @Param("pivotIn") String pivotIn,
                                                  @Param("projectName") String projectName,
                                                  @Param("moldDateStart") LocalDate moldDateStart,
                                                  @Param("moldDateEnd") LocalDate moldDateEnd);

}
