package com.aacoptics.wlg.equipment.mapper;

import com.aacoptics.wlg.equipment.entity.po.MessageHistory;
import com.aacoptics.wlg.equipment.entity.po.Sequence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MessageHistoryMapper extends BaseMapper<MessageHistory> {



}
