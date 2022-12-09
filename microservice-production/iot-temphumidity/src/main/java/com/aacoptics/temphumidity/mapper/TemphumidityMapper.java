package com.aacoptics.temphumidity.mapper;

import com.aacoptics.temphumidity.entity.SmartMeterInfo;
import com.aacoptics.temphumidity.entity.TemphumidityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TemphumidityMapper extends BaseMapper<TemphumidityInfo> {


    List<TemphumidityInfo> getTemphumidityInfoByDate(@Param("date") String date);

    List<SmartMeterInfo> getSmartMeterInfoByDate(@Param("date") String date);
}
