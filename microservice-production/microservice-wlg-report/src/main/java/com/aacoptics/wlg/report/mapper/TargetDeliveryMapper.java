package com.aacoptics.wlg.report.mapper;

import com.aacoptics.wlg.report.entity.po.TargetDelivery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TargetDeliveryMapper extends BaseMapper<TargetDelivery> {

    List<String> findDeliveryDateByMonth(@Param("projectName") String projectName,
                                     @Param("deliveryDateStart") LocalDate deliveryDateStart,
                                     @Param("deliveryDateEnd") LocalDate deliveryDateEnd);


    List<Map<String, Object>> findTargetDeliveryByMonth(@Param("selectColumn") String selectColumn,
                                                  @Param("pivotIn") String pivotIn,
                                                  @Param("projectName") String projectName,
                                                  @Param("deliveryDateStart") LocalDate deliveryDateStart,
                                                  @Param("deliveryDateEnd") LocalDate deliveryDateEnd);

}
