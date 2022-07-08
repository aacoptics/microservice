package com.aacoptics.wlg.report.service;



import com.aacoptics.wlg.report.entity.param.EstimateFpyQueryParam;
import com.aacoptics.wlg.report.entity.po.EstimateFpy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EstimateFpyService {
    /**
     * 解析模具使用情况Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importEstimateFpyExcel(InputStream in) throws Exception;


    /**
     * 获取所有模具使用情况
     *
     * @return
     */
    List<EstimateFpy> getAll();


    /**
     * 根据条件查询模具使用情况
     *
     * @return
     */
    IPage<EstimateFpy> query(Page page, EstimateFpyQueryParam estimateFpyQueryParam);


    /**
     * 查询存在的记录
     *
     * @param mold
     * @param projectName
     * @param fpyDate
     * @return
     */
    EstimateFpy queryEstimateFpy(String mold, String cycle,
                                                     String projectName,
                                                     LocalDate fpyDate);


    /**
     * 根据条件查询模具使用情况
     *
     * @return
     */
    IPage<Map<String, Object>> queryEstimateFpyByCondition(Page page, EstimateFpyQueryParam estimateFpyQueryParam);
}
