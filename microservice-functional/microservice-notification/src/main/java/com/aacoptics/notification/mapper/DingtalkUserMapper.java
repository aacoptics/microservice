package com.aacoptics.notification.mapper;


import com.aacoptics.notification.entity.po.DingtalkUser;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yan Shangqi
 * @since 2021-06-03
 */
@Repository
@Mapper
@DS("user_db")
public interface DingtalkUserMapper extends BaseMapper<DingtalkUser> {
}
