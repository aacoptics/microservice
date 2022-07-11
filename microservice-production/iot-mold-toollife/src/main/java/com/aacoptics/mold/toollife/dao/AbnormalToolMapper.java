package com.aacoptics.mold.toollife.dao;

import com.aacoptics.mold.toollife.entity.AbnormalTool;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AbnormalToolMapper extends BaseMapper<AbnormalTool> {

    @Select("<script>"
            +"select email from tb_mold_abnormal_email_info where enable = 1"
            +"</script>")
    List<String> getAbnormalEmailList();
}
