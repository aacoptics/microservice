package com.aacoptics.data.analysis.service;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.ProcessConditionData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IProcessConditionDataService extends IService<ProcessConditionData> {

    /**
     * 解析生产汇总Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importProcessConditionDataExcel(InputStream in) throws Exception;


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
    IPage<ProcessConditionData> getDataByConditions(Page<ProcessConditionData> iPage,
                                                    String category,
                                                    String project,
                                                    String partName,
                                                    String material,
                                                    String department,
                                                    String lensNumber);

    /**
     * 根据查询条件获取所有数据
     *
     * @param queryParams
     * @return
     */
    List<ProcessConditionData> getAllDataByConditions(QueryParams queryParams);


    /**
     *  根据id删除指定对象
     * @param id
     */
    boolean delete(Long id);


    /**
     * 更新数据
     * @param processConditionData
     * @return
     */
    boolean update(ProcessConditionData processConditionData);

    /**
     *  获取类别
     * @return
     */
    List<ProcessConditionData> getCategory();

    /**
     *  获取类别
     * @return
     */
    List<ProcessConditionData> getProject();

    /**
     *  获取零件名称
     * @return
     */
    List<ProcessConditionData> getPartName();

    /**
     *  获取材料
     * @return
     */
    List<ProcessConditionData> getMaterial();

    /**
     *  获取模具序号
     * @return
     */
    List<ProcessConditionData> getMoldNo();


    /**
     * 获取事业部
     *
     * @return
     */
    List<ProcessConditionData> getDepartment();


    /**
     * 获取镜片数
     *
     * @return
     */
    List<ProcessConditionData> getLensNumber();


    /**
     *  清空数据表
     */
    void deleteData();

}
