package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.MoldData;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.aacoptics.data.analysis.exception.BusinessException;
import com.aacoptics.data.analysis.mapper.MoldDataMapper;
import com.aacoptics.data.analysis.mapper.StructureDataMapper;
import com.aacoptics.data.analysis.service.IMoldDataService;
import com.aacoptics.data.analysis.service.IStructureDataService;
import com.aacoptics.data.analysis.util.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class MoldDataService extends ServiceImpl<MoldDataMapper, MoldData> implements IMoldDataService {

    @Autowired
    MoldDataMapper moldDataMapper;


    @Override
    public void importMoldDataExcel(InputStream in) throws Exception {
        List<String[]> excelDataList = ExcelUtil.read(in).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产模具数据表".equals(title)) {
            throw new BusinessException("Excel模板错误，请确认!");
        }
        for (int i = 3; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if (dataArray == null || dataArray.length == 0) {
                break;
            }

            String category = dataArray[0];
            String project = dataArray[1];
            String partName = dataArray[2];
            String material = dataArray[3];

            if (StringUtils.isEmpty(category)) {
                throw new BusinessException("第" + (i + 1) + "行，类别不能为空");
            }
            if (StringUtils.isEmpty(project)) {
                throw new BusinessException("第" + (i + 1) + "行，项目名不能为空");
            }
            if (StringUtils.isEmpty(partName)) {
                throw new BusinessException("第" + (i + 1) + "行，零件名称不能为空");
            }
            if (StringUtils.isEmpty(material)) {
                throw new BusinessException("第" + (i + 1) + "行，材料不能为空");
            }

            MoldData moldData = this.getMoldData(category, project, partName, material);
            if (moldData == null) {
                moldData = new MoldData();
            }


            String moldType = dataArray[4];
            String moldCorePassivation = dataArray[5];
            String runnerType = dataArray[6];
            String firstRunner = dataArray[7];
            String secondRunner = dataArray[8];
            String thirdRunner = dataArray[9];
            String partingSurface = dataArray[10];
            String splitPosition = dataArray[11];
            String gateType = dataArray[12];
            String gateWidth = dataArray[13];
            String gateThickness = dataArray[14];
            String gateR1Thickness = dataArray[15];
            String gateR2Thickness = dataArray[16];
            String moldOpeningType = dataArray[17];

            // 设置参数
            moldData.setCategory(category);
            moldData.setProject(project);
            moldData.setPartName(partName);
            moldData.setMaterial(material);
            moldData.setMoldType(moldType);
            moldData.setMoldCorePassivation(moldCorePassivation);
            moldData.setRunnerType(runnerType);
            moldData.setFirstRunner(firstRunner);
            moldData.setSecondRunner(secondRunner);
            moldData.setThirdRunner(thirdRunner);
            moldData.setPartingSurface(partingSurface);
            moldData.setSplitPosition(splitPosition);
            moldData.setGateType(gateType);
            moldData.setGateWidth(gateWidth);
            moldData.setGateThickness(gateThickness);
            moldData.setGateR1Thickness(gateR1Thickness);
            moldData.setGateR2Thickness(gateR2Thickness);
            moldData.setMoldOpeningType(moldOpeningType);

            this.saveOrUpdate(moldData);

        }

    }

    @Override
    public IPage<MoldData> getDataByConditions(Page<MoldData> iPage, String category, String project, String partName, String material) {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(category), "category", category)
                .like(StringUtils.isNotBlank(project), "project", project)
                .like(StringUtils.isNotBlank(partName), "part_name", partName)
                .like(StringUtils.isNotBlank(material), "material", material);
        IPage<MoldData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<MoldData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .like(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .like(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .like(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial());
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(MoldData moldData) {
        return this.updateById(moldData);
    }

    private MoldData getMoldData(String category, String project, String partName, String material) {

        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material);

        List<MoldData> moldDataList = moldDataMapper.selectList(queryWrapper);
        if (moldDataList != null && moldDataList.size() > 0) {
            return moldDataList.get(0);
        }
        return null;
    }
}
