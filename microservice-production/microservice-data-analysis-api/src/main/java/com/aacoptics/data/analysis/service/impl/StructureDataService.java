package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.ShapingResultData;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.aacoptics.data.analysis.exception.BusinessException;
import com.aacoptics.data.analysis.mapper.*;
import com.aacoptics.data.analysis.service.IStructureDataService;
import com.aacoptics.data.analysis.util.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StructureDataService extends ServiceImpl<StructureDataMapper, StructureData> implements IStructureDataService {

    @Autowired
    StructureDataMapper structureDataMapper;
    @Autowired
    MoldDataMapper moldDataMapper;
    @Autowired
    MoldFlowMapper moldFlowMapper;
    @Autowired
    private ProcessConditionDataMapper processConditionDataMapper;
    @Autowired
    private ShapingResultDataMapper shapingResultDataMapper;


    @Override
    public void importStructureDataExcel(InputStream in) throws Exception {
        Workbook workbook = ExcelUtil.getWorkbook(in);
        List<String[]> excelDataList = ExcelUtil.read(workbook).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产结构数据表".equals(title)) {
            throw new BusinessException("Excel模板错误，请确认!");
        }

        // 提取excel中的图片并保存
        String filePathPrefix = "structureData";
        Map<String, PictureData> pictures = ExcelUtil.getPictures(workbook);
        Map<String, String> pathsMap = ExcelUtil.saveImg(pictures, filePathPrefix);
        int line = 3;
        try{
            for (int i = 3; i < excelDataList.size(); i++) {
                line = i + 1;
                String[] dataArray = excelDataList.get(i);
                if (dataArray == null || dataArray.length == 0) {
                    break;
                }
                String department = dataArray[0];
                String category = dataArray[1];
                String lensNumber = dataArray[2];
                String project = dataArray[3];
                String partName = dataArray[4];
                String material = dataArray[5];

                if (StringUtils.isEmpty(department)) {
                    throw new BusinessException("事业部不能为空");
                }
                if (StringUtils.isEmpty(category)) {
                    throw new BusinessException("类别不能为空");
                }
                if (StringUtils.isEmpty(lensNumber)) {
                    throw new BusinessException("镜片数不能为空");
                }
                if (StringUtils.isEmpty(project)) {
                    throw new BusinessException("项目名不能为空");
                }
                if (StringUtils.isEmpty(partName)) {
                    throw new BusinessException("零件名称不能为空");
                }
                if (StringUtils.isEmpty(material)) {
                    throw new BusinessException("材料不能为空");
                }

                StructureData structureData = this.getStructureData(project, partName);
                if (structureData == null) {
                    structureData = new StructureData();
                }


                String structureSchemesNo = ExcelUtil.handleDecimal(dataArray[6], 1);
                String coreThickness = ExcelUtil.handleDecimal(dataArray[7], 1);
                String maxWallThickness = ExcelUtil.handleDecimal(dataArray[8], 1);
                String minWallThickness = ExcelUtil.handleDecimal(dataArray[9], 1);
                String maxCoreRatio = ExcelUtil.handleDecimal(dataArray[10], 1);
                String maxMinRatio = ExcelUtil.handleDecimal(dataArray[11], 1);
                String opticsMaxAngleR1 = ExcelUtil.handleDecimal(dataArray[12], 1);
                String opticsMaxAngleR2 = ExcelUtil.handleDecimal(dataArray[13], 1);
                String outerDiameter = ExcelUtil.handleDecimal(dataArray[14], 1);
                String edgeThickness = ExcelUtil.handleDecimal(dataArray[15], 1);
                String wholeMinWallThickness = ExcelUtil.handleDecimal(dataArray[16], 1);
                String wholeMaxWallThickness = ExcelUtil.handleDecimal(dataArray[17], 1);
                String wholeMaxMinRatio = ExcelUtil.handleDecimal(dataArray[18], 1);
                String wholeDiameterThicknessRatio = ExcelUtil.handleDecimal(dataArray[19], 1);
                String maxAngleR1 = ExcelUtil.handleDecimal(dataArray[20], 1);
                String maxAngleR2 = ExcelUtil.handleDecimal(dataArray[21], 1);
                String r1MaxHeightDifference = ExcelUtil.handleDecimal(dataArray[22], 1);
                String r2MaxHeightDifference = ExcelUtil.handleDecimal(dataArray[23], 1);
                String r1R2Distance = ExcelUtil.handleDecimal(dataArray[24], 1);
                String middlePartThickness = ExcelUtil.handleDecimal(dataArray[25], 1);
                String bottomDiameterDistance = ExcelUtil.handleDecimal(dataArray[26], 1);
                String mechanismDiameterThicknessRatio = ExcelUtil.handleDecimal(dataArray[27], 1);
                String r1KanheAngle = ExcelUtil.handleDecimal(dataArray[28], 1);
                String r1KanheHeight = ExcelUtil.handleDecimal(dataArray[29], 1);
                String r2KanheAngle = ExcelUtil.handleDecimal(dataArray[30], 1);
                String r2KanheHeight = ExcelUtil.handleDecimal(dataArray[31], 1);
                String r1Srtm = dataArray[32];
                String r2Srtm = dataArray[33];
                String r1SplitPosition = dataArray[34];
                String r2SplitPosition = dataArray[35];
                String outerDiameterSrtm = dataArray[36];
                String partSurfaceLiftRatio = dataArray[37];
                String mechanismTrou = dataArray[38];
                String assemblyDrawing = pathsMap.get(i + "_" + 39); // 图片路径
                if (assemblyDrawing == null) {
                    assemblyDrawing = "";
                }

                // 设置参数
                structureData.setDepartment(department);
                structureData.setCategory(category);
                structureData.setLensNumber(lensNumber);
                structureData.setProject(project);
                structureData.setPartName(partName);
                structureData.setMaterial(material);
                structureData.setStructureSchemesNo(structureSchemesNo);
                structureData.setCoreThicknessLens(coreThickness);
                structureData.setMaxWallThickness(maxWallThickness);
                structureData.setMinWallThickness(minWallThickness);
                structureData.setMaxCoreRatio(maxCoreRatio);
                structureData.setMaxMinRatio(maxMinRatio);
                structureData.setOpticsMaxAngleR1(opticsMaxAngleR1);
                structureData.setOpticsMaxAngleR2(opticsMaxAngleR2);
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
                structureData.setR1SplitPosition(r1SplitPosition);
                structureData.setR2SplitPosition(r2SplitPosition);
                structureData.setOuterDiameterSrtm(outerDiameterSrtm);
                structureData.setPartSurfaceLiftRatio(partSurfaceLiftRatio);
                structureData.setMechanismTrou(mechanismTrou);
                structureData.setAssemblyDrawing(assemblyDrawing);
                this.saveOrUpdate(structureData);
            }
        }catch (Exception e){
            throw new BusinessException("第" + line + "行保存数据失败！" + e.getMessage());
        }finally {
            // 强制数据同步
            moldDataMapper.syncData();
            moldFlowMapper.syncData();
            processConditionDataMapper.syncData();
            shapingResultDataMapper.syncData();
        }

    }

    @Override
    public IPage<StructureData> getDataByConditions(Page<StructureData> iPage, String category, String project,
                                                    String partName, String material, String department, String lensNumber,
                                                    String searchType, String startValue, String endValue) {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material)
                .eq(StringUtils.isNotBlank(department), "department", department)
                .eq(StringUtils.isNotBlank(lensNumber), "lens_number", lensNumber);
        if (StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(startValue) && StringUtils.isNotBlank(endValue)) {
            queryWrapper.between(searchType, Float.valueOf(startValue), Float.valueOf(endValue));
        }
        IPage<StructureData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<StructureData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .eq(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .eq(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .eq(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial())
                .eq(StringUtils.isNotBlank(queryParams.getDepartment()), "department", queryParams.getDepartment())
                .eq(StringUtils.isNotBlank(queryParams.getLensNumber()), "lens_number", queryParams.getLensNumber());
        if (StringUtils.isNotBlank(queryParams.getSearchType())
                && StringUtils.isNotBlank(queryParams.getStartValue())
                && StringUtils.isNotBlank(queryParams.getEndValue())) {
            queryWrapper.between(queryParams.getSearchType(), Float.valueOf(queryParams.getStartValue()), Float.valueOf(queryParams.getEndValue()));
        }
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

    @Override
    public List<StructureData> getCategory() {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("category").select("category");
        List<StructureData> structureDatas = structureDataMapper.selectList(queryWrapper);
        return structureDatas;
    }

    @Override
    public List<StructureData> getProject() {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("project").select("project");
        List<StructureData> structureDatas = structureDataMapper.selectList(queryWrapper);
        return structureDatas;
    }

    @Override
    public List<StructureData> getPartName() {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("part_name").select("part_name");
        List<StructureData> structureDatas = structureDataMapper.selectList(queryWrapper);
        return structureDatas;
    }

    @Override
    public List<StructureData> getMaterial() {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("material").select("material");
        List<StructureData> structureDatas = structureDataMapper.selectList(queryWrapper);
        return structureDatas;
    }

    @Override
    public List<StructureData> getDepartment() {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("department").select("department");
        List<StructureData> structureDatas = structureDataMapper.selectList(queryWrapper);
        return structureDatas;
    }

    @Override
    public List<StructureData> getLensNumber() {
        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("lens_number").select("lens_number");
        List<StructureData> structureDatas = structureDataMapper.selectList(queryWrapper);
        return structureDatas;
    }

    @Override
    public void deleteData() {
        structureDataMapper.deleteData();
    }

    private StructureData getStructureData(String project, String partName) {

        QueryWrapper<StructureData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName);

        List<StructureData> structureDataList = structureDataMapper.selectList(queryWrapper);
        if (structureDataList != null && structureDataList.size() > 0) {
            return structureDataList.get(0);
        }
        return null;
    }
}
