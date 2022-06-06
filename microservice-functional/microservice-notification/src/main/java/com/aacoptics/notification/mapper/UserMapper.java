package com.aacoptics.notification.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserMapper {

    @DS("user_db")
    List<Map<String, Object>> findAllUsers(Map<String, Object> param);
}