package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.MoldData;
import com.aacoptics.data.analysis.entity.po.MoldFlowData;
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
import org.apache.poi.ss.usermodel.Workbook;
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
        Workbook workbook = ExcelUtil.getWorkbook(in);
        List<String[]> excelDataList = ExcelUtil.read(workbook).get(0);
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
            String firstRunner = ExcelUtil.handleDecimal(dataArray[7],1);
            String secondRunner = ExcelUtil.handleDecimal(dataArray[8],1);
            String thirdRunner = ExcelUtil.handleDecimal(dataArray[9],1);
            String partingSurface = ExcelUtil.handleDecimal(dataArray[10],1);
            String splitPosition = ExcelUtil.handleDecimal(dataArray[11],1);
            String gateType = dataArray[12];
            String gateWidth = ExcelUtil.handleDecimal(dataArray[13],2);
            String gateThickness = ExcelUtil.handleDecimal(dataArray[14],2);
            String gateR1Thickness = ExcelUtil.handleDecimal(dataArray[15],2);
            String gateR2Thickness = ExcelUtil.handleDecimal(dataArray[16],2);
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
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material);
        IPage<MoldData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<MoldData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .eq(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .eq(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .eq(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial());
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

    @Override
    public List<MoldData> getCategory() {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("category").select("category");
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
    }

    @Override
    public List<MoldData> getProject() {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("project").select("project");
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
    }

    @Override
    public List<MoldData> getPartName() {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("part_name").select("part_name");
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
    }

    @Override
    public List<MoldData> getMaterial() {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("material").select("material");
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
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
