package com.aacoptics.sale.mapper;

import com.aacoptics.sale.entity.Content;
import com.aacoptics.sale.entity.ProductContent;
import com.aacoptics.sale.entity.SalesUser;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface SendSalesDataMapper extends BaseMapper<Content> {

    @DS("jiraDB")
    List<Content> getSalesContent();

    @DS("jiraDB")
    List<Content> getDeleteTodoTask();

    @DS("jiraDB")
    List<SalesUser> getSendUsersByType(@Param("tabType") String tabType);

    @DS("jiraDB")
    void updateSalesContentSendFlag(@Param("id") Integer id, @Param("flag") String flag);

    @DS("jiraDB")
    void updateSalesContentDingtalkFlag(@Param("id") Integer id, @Param("dingtalkFlag") String dingtalkFlag);

    @DS("jiraDB")
    List<Map<String, String>> getSalesDataBatch();

    @DS("jiraDB")
    List<ProductContent> getSalesProductContentByBatch(@Param("batchId") String batchId);

    @DS("jiraDB")
    void updateSalesProductContentSendFlag(@Param("batchId") String batchId);

    @DS("jiraDB")
    String getUrlByTabType(@Param("tabType") String tabType);

    @DS("jiraDB")
    List<Map<String, Object>> getRobotUrlByTabType(@Param("tabType") String tabType);


    @DS("jiraDB")
    List<Map<String, String>> getSendHistoryByBatchAndRobot(@Param("batchId") String batchId, @Param("robotId") Integer robotId);

    @DS("jiraDB")
    void saveSendHistory(@Param("batchId") String batchId,
                         @Param("robotId") Integer robotId,
                         @Param("result") String result,
                         @Param("message") String message);

}
