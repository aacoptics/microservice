package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.po.CycleDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CycleDetailMapper extends BaseMapper<CycleDetail> {

}