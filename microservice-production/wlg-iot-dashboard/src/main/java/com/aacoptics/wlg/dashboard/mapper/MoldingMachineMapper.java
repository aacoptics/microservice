package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.po.MoldingMachine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MoldingMachineMapper extends BaseMapper<MoldingMachine> {

}