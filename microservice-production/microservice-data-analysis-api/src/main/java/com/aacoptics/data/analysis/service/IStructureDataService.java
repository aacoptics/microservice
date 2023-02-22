package com.aacoptics.data.analysis.service;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IStructureDataService extends IService<StructureData> {
    /**
     * 解析生产汇总Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importStructureDataExcel(InputStream in) throws Exception;


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
    IPage<StructureData> getDataByConditions(Page<StructureData> iPage,
                                                    String category,
                                                    String project,
                                                    String partName,
                                                    String material,
                                                    String department,
                                                    String lensNumber,
                                                    String searchType,
                                                    String startValue,
                                                    String endValue);

    /**
     * 根据查询条件获取所有数据
     *
     * @param queryParams
     * @return
     */
    List<StructureData> getAllDataByConditions(QueryParams queryParams);


    /**
     *  根据id删除指定对象
     * @param id
     */
    boolean delete(Long id);


    /**
     * 更新数据
     * @param structureData
     * @return
     */
    boolean update(StructureData structureData);

    /**
     *  获取类别
     * @return
     */
    List<StructureData> getCategory();

    /**
     *  获取项目
     * @return
     */
    List<StructureData> getProject();

    /**
     *  获取零件名称
     * @return
     */
    List<StructureData> getPartName();

    /**
     *  获取材料
     * @return
     */
    List<StructureData> getMaterial();

    /**
     * 获取事业部
     *
     * @return
     */
    List<StructureData> getDepartment();


    /**
     * 获取镜片数
     *
     * @return
     */
    List<StructureData> getLensNumber();

    /**
     *  清空数据表
     */
    void deleteData();

}
