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
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MoldFlowService extends ServiceImpl<MoldFlowMapper, MoldFlowData> implements IMoldFlowService {

    @Autowired
    MoldFlowMapper moldFlowMapper;

    @Override
    public void importMoldFlowExcel(InputStream in) throws Exception {
        Workbook workbook = ExcelUtil.getWorkbook(in);
        List<String[]> excelDataList = ExcelUtil.read(workbook).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产模流数据表".equals(title)) {
            throw new BusinessException("Excel模板错误，请确认!");
        }
        // 提取excel中的图片并保存
        String filePathPrefix = "moldFlowData";
        Map<String, PictureData> pictures = ExcelUtil.getPictures(workbook);
        Map<String, String> pathsMap = ExcelUtil.saveImg(pictures, filePathPrefix);
        for (int i = 3; i < excelDataList.size(); i++) {
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
                throw new BusinessException("第" + (i + 1) + "行，事业部不能为空");
            }
            if (StringUtils.isEmpty(category)) {
                throw new BusinessException("第" + (i + 1) + "行，类别不能为空");
            }
            if (StringUtils.isEmpty(lensNumber)) {
                throw new BusinessException("第" + (i + 1) + "行，镜片数不能为空");
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

            MoldFlowData moldFlowData = this.getMoldFlowData(category, project, partName, material, department, lensNumber);
            if (moldFlowData == null) {
                moldFlowData = new MoldFlowData();
            }


            String moldType = dataArray[6];
            String runnerType = dataArray[7];
            String moldDiameterRate = ExcelUtil.handleDecimal(dataArray[8], 2);
            String flowFrontTemperature = ExcelUtil.handleDecimal(dataArray[9], 2);
            String vpChangePressure = ExcelUtil.handleDecimal(dataArray[10], 2);
            String simulateWireLength = ExcelUtil.handleDecimal(dataArray[11], 2);
            String wholePercent = ExcelUtil.handleDecimal(dataArray[12], 2);
            String effectiveR1 = ExcelUtil.handleDecimal(dataArray[13], 2);
            String effectiveR2 = ExcelUtil.handleDecimal(dataArray[14], 2);
            String ridgeR1 = ExcelUtil.handleDecimal(dataArray[15], 2);
            String ridgeR2 = ExcelUtil.handleDecimal(dataArray[16], 2);
            String refractiveR1 = ExcelUtil.handleDecimal(dataArray[17], 0);
            String refractiveR2 = ExcelUtil.handleDecimal(dataArray[18], 0);
            String refractivePicR1 = pathsMap.get(i + "_" + 19);
            if (refractivePicR1 == null) {
                refractivePicR1 = "";
            }
            String refractivePicR2 = pathsMap.get(i + "_" + 20);
            if (refractivePicR2 == null) {
                refractivePicR2 = "";
            }
            String preFrontR1 =  pathsMap.get(i + "_" + 21);
            if(preFrontR1 == null){
                preFrontR1 = "";
            }
            String preFrontR2 =  pathsMap.get(i + "_" + 22);
            if(preFrontR2 == null){
                preFrontR2 = "";
            }
            String competitorName = dataArray[23];
            String competitorLink = dataArray[24];
            String assemblyDrawing = pathsMap.get(i + "_" + 25); // 图片路径
            if (assemblyDrawing == null) {
                assemblyDrawing = "";
            }

            // 设置参数

            moldFlowData.setDepartment(department);
            moldFlowData.setCategory(category);
            moldFlowData.setLensNumber(lensNumber);
            moldFlowData.setProject(project);
            moldFlowData.setPartName(partName);
            moldFlowData.setMaterial(material);
            moldFlowData.setMoldType(moldType);
            moldFlowData.setRunnerType(runnerType);
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
            moldFlowData.setPreFrontR1(preFrontR1);
            moldFlowData.setPreFrontR2(preFrontR2);
            moldFlowData.setRefractivePicR1(refractivePicR1);
            moldFlowData.setRefractivePicR2(refractivePicR2);
            moldFlowData.setCompetitorName(competitorName);
            moldFlowData.setCompetitorLink(competitorLink);
            moldFlowData.setAssemblyDrawing(assemblyDrawing);

            this.saveOrUpdate(moldFlowData);

            // 上传之后强制同步数据
            moldFlowMapper.syncData();
            moldFlowMapper.syncMoldType();

        }

    }

    @Override
    public IPage<MoldFlowData> getDataByConditions(Page<MoldFlowData> iPage, String category, String project,
                                                   String partName, String material, String department, String lensNumber) {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material)
                .eq(StringUtils.isNotBlank(department), "department", department)
                .eq(StringUtils.isNotBlank(lensNumber), "lens_number", lensNumber);
        IPage<MoldFlowData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<MoldFlowData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .eq(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .eq(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .eq(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial())
                .eq(StringUtils.isNotBlank(queryParams.getDepartment()), "department", queryParams.getDepartment())
                .eq(StringUtils.isNotBlank(queryParams.getLensNumber()), "lens_number", queryParams.getLensNumber());
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

    @Override
    public List<MoldFlowData> getCategory() {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("category").select("category");
        List<MoldFlowData> moldFlowDatas = moldFlowMapper.selectList(queryWrapper);
        return moldFlowDatas;
    }

    @Override
    public List<MoldFlowData> getProject() {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("project").select("project");
        List<MoldFlowData> moldFlowDatas = moldFlowMapper.selectList(queryWrapper);
        return moldFlowDatas;
    }

    @Override
    public List<MoldFlowData> getPartName() {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("part_name").select("part_name");
        List<MoldFlowData> moldFlowDatas = moldFlowMapper.selectList(queryWrapper);
        return moldFlowDatas;
    }

    @Override
    public List<MoldFlowData> getMaterial() {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("material").select("material");
        List<MoldFlowData> moldFlowDatas = moldFlowMapper.selectList(queryWrapper);
        return moldFlowDatas;
    }

    @Override
    public List<MoldFlowData> getDepartment() {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("department").select("department");
        List<MoldFlowData> moldFlowDatas = moldFlowMapper.selectList(queryWrapper);
        return moldFlowDatas;
    }

    @Override
    public List<MoldFlowData> getLensNumber() {
        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("lens_number").select("lens_number");
        List<MoldFlowData> moldFlowDatas = moldFlowMapper.selectList(queryWrapper);
        return moldFlowDatas;
    }

    private MoldFlowData getMoldFlowData(String category, String project, String partName,
                                         String material, String department, String lensNumber) {

        QueryWrapper<MoldFlowData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material)
                .eq(StringUtils.isNotBlank(department), "department", department)
                .eq(StringUtils.isNotBlank(lensNumber), "lens_number", lensNumber);

        List<MoldFlowData> moldFlowDataList = moldFlowMapper.selectList(queryWrapper);
        if (moldFlowDataList != null && moldFlowDataList.size() > 0) {
            return moldFlowDataList.get(0);
        }
        return null;
    }
}
