package com.aacoptics.wlg.report.service;



import com.aacoptics.wlg.report.entity.param.TargetDeliveryQueryParam;
import com.aacoptics.wlg.report.entity.po.TargetDelivery;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TargetDeliveryService {
    /**
     * 解析目标交货数据Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importTargetDeliveryExcel(InputStream in) throws Exception;


    /**
     * 获取所有目标交货数据
     *
     * @return
     */
    List<TargetDelivery> getAll();


    /**
     * 根据条件查询目标交货数据
     *
     * @return
     */
    IPage<TargetDelivery> query(Page page, TargetDeliveryQueryParam targetDeliveryQueryParam);

    /**
     * 根据条件查询目标交货数据
     *
     * @return
     */
    List<Map<String, Object>> queryTargetDeliveryByMonth(TargetDeliveryQueryParam targetDeliveryQueryParam);


    /**
     * 根据条件查询目标交货数据标题
     *
     * @return
     */
    JSONArray queryTargetDeliveryTitleByMonth(TargetDeliveryQueryParam targetDeliveryQueryParam);


    /**
     * 查询存在的记录
     *
     * @param code
     * @param projectName
     * @param deliveryDate
     * @return
     */
    TargetDelivery queryTargetDeliveryByItemNumberAndProjectAndDate(String code,
                                                     String projectName,
                                                     LocalDate deliveryDate);
}
