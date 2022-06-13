package com.aacoptics.notification.mapper;

import com.aacoptics.notification.entity.po.Robot;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@DS("msg_db")
public interface RobotMapper extends BaseMapper<Robot> {

}