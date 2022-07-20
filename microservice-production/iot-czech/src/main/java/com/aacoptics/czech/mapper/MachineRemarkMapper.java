package com.aacoptics.czech.mapper;

import com.aacoptics.czech.entity.MachineRemark;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MachineRemarkMapper extends BaseMapper<MachineRemark> {
    List<Map<String, String>> getRemarkByMachineNumber(@Param("machineNumber") String machineNumber);
}
