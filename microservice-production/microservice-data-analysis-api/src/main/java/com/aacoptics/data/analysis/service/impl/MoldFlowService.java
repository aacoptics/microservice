package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.MoldFlowData;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.aacoptics.data.analysis.exception.BusinessException;
import com.aacoptics.data.analysis.mapper.MoldFlowMapper;
import com.aacoptics.data.analysis.mapper.StructureDataMapper;
import com.aacoptics.data.analysis.service.IMoldFlowService;
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
public class MoldFlowService extends ServiceImpl<MoldFlowMapper, MoldFlowData> implements IMoldFlowService {

    @Autowired
    MoldFlowMapper moldFlowMapper;

    @Override
    public void importMoldFlowExcel(InputStream in) throws Exception {
        List<String[]> excelDataList = ExcelUtil.read(in).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产模流数据表".equals(title)) {
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

            MoldFlowData moldFlowData = this.getMoldFlowData(category, project, partName, material);
            if (moldFlowData == null) {
                moldFlowData = new MoldFlowData();
            }


            String moldType = dataArray[4];
            String moldDiameterRate = dataArray[5];
            String flowFrontTemperature = dataArray[6];
            String vpChangePressure = dataArray[7];
            String simulateWireLength = dataArray[8];
            String wholePercent = dataArray[9];
            String effectiveR1 = dataArray[10];
            String effectiveR2 = dataArray[11];
            String ridgeR1 = dataArray[12];
            String ridgeR2 = dataArray[13];
            String refractiveR1 = dataArray[14];
            String refractiveR2 = dataArray[15];
            String competitorName = dataArray[16];
            String competitorLink = dataArray[17];
            String assemblyDrawing = dataArray[18];

            // 设置参数

            moldFlowData.setCategory(category);
            moldFlowData.setProject(project);
            moldFlowData.setPartName(partName);
            moldFlowData.setMaterial(material);
            moldFlowData.setMoldType(moldType);
            moldFlowData.setMoldDiameterRate(moldDiameterRate);
            moldFlowData.setFlowFrontTemperature(flowFrontTemperature);
            moldFlowData.setVpChangePressure(vpChangePressure);
            moldFlowData.setSimulateWireLength(simulateWireLength);
            moldFlowData.setWholePercent(wholePercent);
            moldFlowData.setEffectiveR1(effectiveR1);
            moldFlowData.setEffectiveR2(effectiveR2);
            moldFlowData.setRidgeR1(ridgeR1);
            moldFlowData.setRidgeR2(ridgeR2);
            moldFlowData.setRefractiveR1(refractiveR1);
            moldFlowData.setRefractiveR2(refractiveR2);
            moldFlowData.setCompetitorName(competitorName);
            moldFlowData.setCompetitorLink(competitorLink);
            moldFlowData.setAssemblyDrawing(assemblyDrawing);

            this.saveOrUpdate(moldFlowData);

        }

    }

    @Override
    public IPage<MoldFlowData> getDataByConditions(Page<MoldFlowData> iPage, String category, String project, String partName, String material) {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(category), "category", category)
                .like(StringUtils.isNotBlank(project), "project", project)
                .like(StringUtils.isNotBlank(partName), "part_name", partName)
                .like(StringUtils.isNotBlank(material), "material", material);
        IPage<MoldFlowData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<MoldFlowData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .like(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .like(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .like(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial());
        List<MoldFlowData> structureDatas = moldFlowMapper.selectList(queryWrapper);
        return structureDatas;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(MoldFlowData moldFlowData) {
        return this.updateById(moldFlowData);
    }

    private MoldFlowData getMoldFlowData(String category, String project, String partName, String material) {

        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material);

        List<MoldFlowData> moldFlowDataList = moldFlowMapper.selectList(queryWrapper);
        if (moldFlowDataList != null && moldFlowDataList.size() > 0) {
            return moldFlowDataList.get(0);
        }
        return null;
    }
}
