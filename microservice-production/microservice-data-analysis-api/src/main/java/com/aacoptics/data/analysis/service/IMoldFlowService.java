package com.aacoptics.data.analysis.service;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.MoldFlowData;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IMoldFlowService extends IService<MoldFlowData> {
    /**
     * 解析生产汇总Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importMoldFlowExcel(InputStream in) throws Exception;


    /**
     * 条件分页查询
     *
     * @param iPage
     * @param category
     * @param project
     * @param partName
     * @param material
     * @return
     */
    IPage<MoldFlowData> getDataByConditions(Page<MoldFlowData> iPage,
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
    List<MoldFlowData> getAllDataByConditions(QueryParams queryParams);


    /**
     * 根据id删除指定对象
     *
     * @param id
     */
    boolean delete(Long id);


    /**
     * 更新数据
     *
     * @param moldFlowData
     * @return
     */
    boolean update(MoldFlowData moldFlowData);

    /**
     * 获取类别
     *
     * @return
     */
    List<MoldFlowData> getCategory();

    /**
     * 获取项目
     *
     * @return
     */
    List<MoldFlowData> getProject();

    /**
     * 获取零件名称
     *
     * @return
     */
    List<MoldFlowData> getPartName();

    /**
     * 获取材料
     *
     * @return
     */
    List<MoldFlowData> getMaterial();


    /**
     * 获取事业部
     *
     * @return
     */
    List<MoldFlowData> getDepartment();


    /**
     * 获取镜片数
     *
     * @return
     */
    List<MoldFlowData> getLensNumber();


    /**
     *  清空数据表
     */
    void deleteData();

}
