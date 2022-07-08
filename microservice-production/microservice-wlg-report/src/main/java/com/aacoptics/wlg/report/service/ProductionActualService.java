package com.aacoptics.wlg.report.service;


import com.aacoptics.wlg.report.entity.param.ProductionActualQueryParam;
import com.aacoptics.wlg.report.entity.po.ProductionActual;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ProductionActualService {
    /**
     * 解析生产报表Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importProductionActualExcel(String fileName, InputStream in) throws Exception;


    /**
     * 获取所有生产报表
     *
     * @return
     */
    List<ProductionActual> getAll();


    /**
     * 根据条件查询生产报表
     *
     * @return
     */
    IPage<ProductionActual> query(Page page, ProductionActualQueryParam productionActualQueryParam);


    /**
     * 查询存在的记录
     *
     * @param mold 模具
     * @param projectName 项目
     * @param actualDate 实际生产日期
     * @return
     */
    ProductionActual queryProductionActual(String projectName, String product,
                                           String mold, String cycle, LocalDate actualDate);


    /**
     * 根据条件查询生产报表
     *
     * @return
     */
    IPage<Map<String, Object>> queryProductionActualByCondition(Page page, ProductionActualQueryParam productionActualQueryParam);
}
