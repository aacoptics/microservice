package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.aacoptics.data.analysis.exception.BusinessException;
import com.aacoptics.data.analysis.mapper.StructureDataMapper;
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
public class StructureDataService extends ServiceImpl<StructureDataMapper, StructureData> implements IStructureDataService {

    @Autowired
    StructureDataMapper structureDataMapper;


    @Override
    public void importStructureDataExcel(InputStream in) throws Exception {
        List<String[]> excelDataList = ExcelUtil.read(in).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产结构数据表".equals(title)) {
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

            StructureData structureData = this.getStructureData(category, project, partName, material);
            if (structureData == null) {
                structureData = new StructureData();
            }


            String coreThickness = dataArray[4];
            String maxWallThickness = dataArray[5];
            String minWallThickness = dataArray[6];
            String maxCoreRatio = dataArray[7];
            String maxMinRatio = dataArray[8];
            String outerDiameter = dataArray[9];
            String edgeThickness = dataArray[10];
            String wholeMinWallThickness = dataArray[11];
            String wholeMaxWallThickness = dataArray[12];
            String wholeMaxMinRatio = dataArray[13];
            String wholeDiameterThicknessRatio = dataArray[14];
            String maxAngleR1 = dataArray[15];
            String maxAngleR2 = dataArray[16];

            String r1MaxHeightDifference = dataArray[17];
            String r2MaxHeightDifference = dataArray[18];

            String r1R2Distance = dataArray[19];
            String middlePartThickness = dataArray[20];
            String bottomDiameterDistance = dataArray[21];
            String mechanismDiameterThicknessRatio = dataArray[22];
            String r1KanheAngle = dataArray[23];
            String r1KanheHeight = dataArray[24];
            String r2KanheAngle = dataArray[25];
            String r2KanheHeight = dataArray[26];
            String r1Srtm = dataArray[27];
            String r2Srtm = dataArray[28];
            String outerDiameterSrtm = dataArray[29];
            String assemblyDrawing = dataArray[30];

            // 设置参数
            structureData.setCategory(category);
            structureData.setProject(project);
            structureData.setPartName(partName);
            structureData.setMaterial(material);
            structureData.setCoreThicknessLens(coreThickness);
            structureData.setMaxWallThickness(maxWallThickness);
            structureData.setMinWallThickness(minWallThickness);
            structureData.setMaxCoreRatio(maxCoreRatio);
            structureData.setMaxMinRatio(maxMinRatio);
            structureData.setOuterDiameter(outerDiameter);
            structureData.setEdgeThickness(edgeThickness);
            structureData.setWholeMaxMinRatio(wholeMaxMinRatio);
            structureData.setWholeDiameterThicknessRatio(wholeDiameterThicknessRatio);
            structureData.setWholeMaxWallThickness(wholeMaxWallThickness);
            structureData.setWholeMinWallThickness(wholeMinWallThickness);
            structureData.setMaxAngleR1(maxAngleR1);
            structureData.setMaxAngleR2(maxAngleR2);
            structureData.setR1MaxHeightDifference(r1MaxHeightDifference);
            structureData.setR2MaxHeightDifference(r2MaxHeightDifference);
            structureData.setR1R2Distance(r1R2Distance);
            structureData.setMiddlePartThickness(middlePartThickness);
            structureData.setBottomDiameterDistance(bottomDiameterDistance);
            structureData.setMechanismDiameterThicknessRatio(mechanismDiameterThicknessRatio);
            structureData.setR1KanheAngle(r1KanheAngle);
            structureData.setR1KanheHeight(r1KanheHeight);
            structureData.setR2KanheAngle(r2KanheAngle);
            structureData.setR2KanheHeight(r2KanheHeight);
            structureData.setR1Srtm(r1Srtm);
            structureData.setR2Srtm(r2Srtm);
            structureData.setOuterDiameterSrtm(outerDiameterSrtm);
            structureData.setAssemblyDrawing(assemblyDrawing);
            this.saveOrUpdate(structureData);

        }

    }

    @Override
    public IPage<StructureData> getDataByConditions(Page<StructureData> iPage, String category, String project, String partName, String material) {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(category), "category", category)
                .like(StringUtils.isNotBlank(project), "project", project)
                .like(StringUtils.isNotBlank(partName), "part_name", partName)
                .like(StringUtils.isNotBlank(material), "material", material);
        IPage<StructureData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<StructureData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .like(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .like(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .like(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial());
        List<StructureData> structureDatas = structureDataMapper.selectList(queryWrapper);
        return structureDatas;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(StructureData structureData) {
        return this.updateById(structureData);
    }

    private StructureData getStructureData(String category, String project, String partName, String material) {

        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material);

        List<StructureData> structureDataList = structureDataMapper.selectList(queryWrapper);
        if (structureDataList != null && structureDataList.size() > 0) {
            return structureDataList.get(0);
        }
        return null;
    }
}
