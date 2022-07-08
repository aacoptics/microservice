package com.aacoptics.wlg.report.service;



import com.aacoptics.wlg.report.entity.param.MoldUseQueryParam;
import com.aacoptics.wlg.report.entity.po.MoldUse;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MoldUseService {
    /**
     * 解析模具使用情况Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importMoldUseExcel(InputStream in) throws Exception;


    /**
     * 获取所有模具使用情况
     *
     * @return
     */
    List<MoldUse> getAll();


    /**
     * 根据条件查询模具使用情况
     *
     * @return
     */
    IPage<MoldUse> query(Page page, MoldUseQueryParam moldUseQueryParam);

    /**
     * 根据条件查询模具使用情况
     *
     * @return
     */
    List<Map<String, Object>> queryMoldUseByMonth(MoldUseQueryParam moldUseQueryParam);


    /**
     * 根据条件查询模具使用情况标题
     *
     * @return
     */
    JSONArray queryMoldUseTitleByMonth(MoldUseQueryParam moldUseQueryParam);


    /**
     * 查询存在的记录
     *
     * @param code
     * @param projectName
     * @param moldDate
     * @return
     */
    MoldUse queryMoldUseByCodeAndProjectAndDate(String code,
                                                     String projectName,
                                                     LocalDate moldDate);
}
