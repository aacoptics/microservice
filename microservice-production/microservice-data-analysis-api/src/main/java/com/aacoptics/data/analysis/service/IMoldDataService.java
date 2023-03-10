package com.aacoptics.data.analysis.service;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.MoldData;
import com.aacoptics.data.analysis.entity.po.MoldFlowData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IMoldDataService extends IService<MoldData> {

    /**
     * 解析生产汇总Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importMoldDataExcel(InputStream in) throws Exception;


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
    IPage<MoldData> getDataByConditions(Page<MoldData> iPage,
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
    List<MoldData> getAllDataByConditions(QueryParams queryParams);


    /**
     * 根据id删除指定对象
     *
     * @param id
     */
    boolean delete(Long id);


    /**
     * 更新数据
     *
     * @param moldData
     * @return
     */
    boolean update(MoldData moldData);

    /**
     *  获取类别
     * @return
     */
    List<MoldData> getCategory();

    /**
     *  获取项目
     * @return
     */
    List<MoldData> getProject();

    /**
     *  获取零件名称
     * @return
     */
    List<MoldData> getPartName();

    /**
     *  获取材料
     * @return
     */
    List<MoldData> getMaterial();


    /**
     * 获取事业部
     * @return
     */
    List<MoldData> getDepartment();


    /**
     *  获取镜片数
     * @return
     */
    List<MoldData> getLensNumber();

    /**
     *  删除数据
     */
    void deleteData();

}
