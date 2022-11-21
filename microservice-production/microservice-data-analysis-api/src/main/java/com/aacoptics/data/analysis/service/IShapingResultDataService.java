package com.aacoptics.data.analysis.service;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.ProcessConditionData;
import com.aacoptics.data.analysis.entity.po.ShapingResultData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IShapingResultDataService extends IService<ShapingResultData> {
    /**
     * 解析生产汇总Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importShapingResultDataExcel(InputStream in) throws Exception;


    /**
     * 条件分页查询
     *
     * @param iPage
     * @param category
     * @param project
     * @param partName
     * @param material
     * @param moldNo
     * @return
     */
    IPage<ShapingResultData> getDataByConditions(Page<ShapingResultData> iPage,
                                                    String category,
                                                    String project,
                                                    String partName,
                                                    String material,
                                                    String moldNo,
                                                    String searchType,
                                                    String startValue,
                                                    String endValue);

    /**
     * 根据查询条件获取所有数据
     *
     * @param queryParams
     * @return
     */
    List<ShapingResultData> getAllDataByConditions(QueryParams queryParams);


    /**
     *  根据id删除指定对象
     * @param id
     */
    boolean delete(Long id);


    /**
     * 更新数据
     * @param shapingResultData
     * @return
     */
    boolean update(ShapingResultData shapingResultData);

    /**
     *  获取类别
     * @return
     */
    List<ShapingResultData> getCategory();

    /**
     *  获取项目
     * @return
     */
    List<ShapingResultData> getProject();

    /**
     *  获取零件名称
     * @return
     */
    List<ShapingResultData> getPartName();

    /**
     *  获取材料
     * @return
     */
    List<ShapingResultData> getMaterial();

    /**
     *  获取模具序号
     * @return
     */
    List<ShapingResultData> getMoldNo();
}
