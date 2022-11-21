package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.ProcessConditionData;
import com.aacoptics.data.analysis.entity.po.ShapingResultData;
import com.aacoptics.data.analysis.exception.BusinessException;
import com.aacoptics.data.analysis.mapper.ShapingResultDataMapper;
import com.aacoptics.data.analysis.service.IShapingResultDataService;
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
public class ShapingResultDataService extends ServiceImpl<ShapingResultDataMapper, ShapingResultData> implements IShapingResultDataService {

    @Autowired
    private ShapingResultDataMapper shapingResultDataMapper;

    @Override
    public void importShapingResultDataExcel(InputStream in) throws Exception {
        Workbook workbook = ExcelUtil.getWorkbook(in);
        List<String[]> excelDataList = ExcelUtil.read(workbook).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产成型结果数据表".equals(title)) {
            throw new BusinessException("Excel模板错误，请确认!");
        }

        // 提取excel中的图片并保存
        String filePathPrefix = "shapingResultData";
        Map<String, PictureData> pictures = ExcelUtil.getPictures(workbook);
        Map<String, String> pathsMap = ExcelUtil.saveImg(pictures, filePathPrefix);
        for (int i = 3; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if (dataArray == null || dataArray.length == 0) {
                break;
            }

            String category = dataArray[0];
            String project = dataArray[1];
            String moldNo = dataArray[2];
            String partName = dataArray[3];
            String material = dataArray[4];

            if (StringUtils.isEmpty(category)) {
                throw new BusinessException("第" + (i + 1) + "行，类别不能为空");
            }
            if (StringUtils.isEmpty(project)) {
                throw new BusinessException("第" + (i + 1) + "行，项目名不能为空");
            }
            if (StringUtils.isEmpty(moldNo)) {
                throw new BusinessException("第" + (i + 1) + "行，模具序号不能为空");
            }
            if (StringUtils.isEmpty(partName)) {
                throw new BusinessException("第" + (i + 1) + "行，零件名称不能为空");
            }
            if (StringUtils.isEmpty(material)) {
                throw new BusinessException("第" + (i + 1) + "行，材料不能为空");
            }

            ShapingResultData shapingResultData = this.getShapingResultData(category, project, partName, material, moldNo);
            if (shapingResultData == null) {
                shapingResultData = new ShapingResultData();
            }

            String coreThickness = ExcelUtil.handleDecimal(dataArray[5], 1);
            String coreThicknessRange = ExcelUtil.handleDecimal(dataArray[6], 1);
            String r1VectorHeight = ExcelUtil.handleDecimal(dataArray[7], 1);
            String r1VectorHeightRange = ExcelUtil.handleDecimal(dataArray[8], 1);
            String r2VectorHeight = ExcelUtil.handleDecimal(dataArray[9], 1);
            String r2VectorHeightRange = ExcelUtil.handleDecimal(dataArray[10], 1);
            String outerDiameterEcc = ExcelUtil.handleDecimal(dataArray[11], 1);
            String kanheEcc = ExcelUtil.handleDecimal(dataArray[12], 1);
            String faceEcc = ExcelUtil.handleDecimal(dataArray[13], 1);
            String annealingProcess = ExcelUtil.handleDecimal(dataArray[14], 0);
            String bpKanheRoundness = ExcelUtil.handleDecimal(dataArray[15], 1);
            String dmpKanheRoundness = ExcelUtil.handleDecimal(dataArray[16], 1);
            String outerDiameterAverage = ExcelUtil.handleDecimal(dataArray[17], 1);
            String outerDiameterRange = ExcelUtil.handleDecimal(dataArray[18], 1);
            String outerDiameterRoundness = ExcelUtil.handleDecimal(dataArray[19], 1);
            String outerDiameterShrinkage = ExcelUtil.handleDecimal(dataArray[20], 1);
            String outerDiameterRoughness = ExcelUtil.handleDecimal(dataArray[21], 1);
            String r1Flatness = ExcelUtil.handleDecimal(dataArray[22], 1);
            String r2Flatness = ExcelUtil.handleDecimal(dataArray[23], 1);
            String r1SplitAverage = ExcelUtil.handleDecimal(dataArray[24], 1);
            String r2SplitAverage = ExcelUtil.handleDecimal(dataArray[25], 1);
            String wftR1 = ExcelUtil.handleDecimal(dataArray[26], 0);
            String wftR2 = ExcelUtil.handleDecimal(dataArray[27], 0);
            String wftConsistency = ExcelUtil.handleDecimal(dataArray[28], 0);
            String wftMaxAs = ExcelUtil.handleDecimal(dataArray[29], 0);
            String wftStability = ExcelUtil.handleDecimal(dataArray[30], 0);
            String cftR1 = ExcelUtil.handleDecimal(dataArray[31], 0);
            String cftR2 = ExcelUtil.handleDecimal(dataArray[32], 0);
            String cftConsistency = ExcelUtil.handleDecimal(dataArray[33], 0);
            String cftMaxAs = ExcelUtil.handleDecimal(dataArray[34], 0);
            String coatingTrend = pathsMap.get(i + "_" + 35); // 图片路径
            if (coatingTrend == null) {
                coatingTrend = "";
            }
            String cfsrR1 = pathsMap.get(i + "_" + 36); // 图片路径
            if (cfsrR1 == null) {
                cfsrR1 = "";
            }
            String cfsrR2 = pathsMap.get(i + "_" + 37); // 图片路径
            if (cfsrR2 == null) {
                cfsrR2 = "";
            }
            String cfsrR1R2 = pathsMap.get(i + "_" + 38); // 图片路径
            if (cfsrR1R2 == null) {
                cfsrR1R2 = "";
            }
            String burr = ExcelUtil.handleDecimal(dataArray[39], 1);
            String weldline = dataArray[40];
            String appearanceProblem = dataArray[41];
            String appearanceImg = pathsMap.get(i + "_" + 42); // 图片路径
            if (appearanceImg == null) {
                appearanceImg = "";
            }
            String remarks = dataArray[43];

            // 设置参数
            shapingResultData.setCategory(category);
            shapingResultData.setProject(project);
            shapingResultData.setMoldNo(moldNo);
            shapingResultData.setPartName(partName);
            shapingResultData.setMaterial(material);
            shapingResultData.setCoreThickness(coreThickness);
            shapingResultData.setCoreThicknessRange(coreThicknessRange);
            shapingResultData.setR1VectorHeight(r1VectorHeight);
            shapingResultData.setR1VectorHeightRange(r1VectorHeightRange);
            shapingResultData.setR2VectorHeight(r2VectorHeight);
            shapingResultData.setR2VectorHeightRange(r2VectorHeightRange);
            shapingResultData.setOuterDiameterEcc(outerDiameterEcc);
            shapingResultData.setKanheEcc(kanheEcc);
            shapingResultData.setFaceEcc(faceEcc);
            shapingResultData.setAnnealingProcess(annealingProcess);
            shapingResultData.setBpKanheRoundness(bpKanheRoundness);
            shapingResultData.setDmpKanheRoundness(dmpKanheRoundness);
            shapingResultData.setOuterDiameterAverage(outerDiameterAverage);
            shapingResultData.setOuterDiameterRange(outerDiameterRange);
            shapingResultData.setOuterDiameterRoundness(outerDiameterRoundness);
            shapingResultData.setOuterDiameterRoughness(outerDiameterRoughness);
            shapingResultData.setOuterDiameterShrinkage(outerDiameterShrinkage);
            shapingResultData.setR1Flatness(r1Flatness);
            shapingResultData.setR2Flatness(r2Flatness);
            shapingResultData.setR1SplitAverage(r1SplitAverage);
            shapingResultData.setR2SplitAverage(r2SplitAverage);
            shapingResultData.setWftR1(wftR1);
            shapingResultData.setWftR2(wftR2);
            shapingResultData.setWftStability(wftStability);
            shapingResultData.setWftConsistency(wftConsistency);
            shapingResultData.setWftMaxAs(wftMaxAs);
            shapingResultData.setCftR1(cftR1);
            shapingResultData.setCftR2(cftR2);
            shapingResultData.setCftConsistency(cftConsistency);
            shapingResultData.setCftMaxAs(cftMaxAs);
            shapingResultData.setCoatingTrend(coatingTrend);
            shapingResultData.setCfsrR1(cfsrR1);
            shapingResultData.setCfsrR2(cfsrR2);
            shapingResultData.setCfsrR1R2(cfsrR1R2);
            shapingResultData.setBurr(burr);
            shapingResultData.setWeldline(weldline);
            shapingResultData.setAppearanceImg(appearanceImg);
            shapingResultData.setAppearanceProblem(appearanceProblem);
            shapingResultData.setRemarks(remarks);

            this.saveOrUpdate(shapingResultData);

        }
    }

    @Override
    public IPage<ShapingResultData> getDataByConditions(Page<ShapingResultData> iPage, String category, String project,
                                                        String partName, String material, String moldNo, String searchType,
                                                        String startValue, String endValue) {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material)
                .eq(StringUtils.isNotBlank(moldNo), "mold_no", moldNo);
        if (StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(startValue) && StringUtils.isNotBlank(endValue)) {
            queryWrapper.between(searchType, Float.valueOf(startValue), Float.valueOf(endValue));
        }
        IPage<ShapingResultData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<ShapingResultData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .eq(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .eq(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .eq(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial())
                .eq(StringUtils.isNotBlank(queryParams.getMoldNo()), "mold_no", queryParams.getMoldNo());
        if (StringUtils.isNotBlank(queryParams.getSearchType())
                && StringUtils.isNotBlank(queryParams.getStartValue())
                && StringUtils.isNotBlank(queryParams.getEndValue())) {
            queryWrapper.between(queryParams.getSearchType(), Float.valueOf(queryParams.getStartValue()), Float.valueOf(queryParams.getEndValue()));
        }
        List<ShapingResultData> shapingResultDatas = shapingResultDataMapper.selectList(queryWrapper);
        return shapingResultDatas;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ShapingResultData shapingResultData) {
        return this.updateById(shapingResultData);
    }

    @Override
    public List<ShapingResultData> getCategory() {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("category").select("category");
        List<ShapingResultData> shapingResultDatas = shapingResultDataMapper.selectList(queryWrapper);
        return shapingResultDatas;
    }

    @Override
    public List<ShapingResultData> getProject() {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("project").select("project");
        List<ShapingResultData> shapingResultDatas = shapingResultDataMapper.selectList(queryWrapper);
        return shapingResultDatas;
    }

    @Override
    public List<ShapingResultData> getPartName() {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("part_name").select("part_name");
        List<ShapingResultData> shapingResultDatas = shapingResultDataMapper.selectList(queryWrapper);
        return shapingResultDatas;
    }

    @Override
    public List<ShapingResultData> getMaterial() {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("material").select("material");
        List<ShapingResultData> shapingResultDatas = shapingResultDataMapper.selectList(queryWrapper);
        return shapingResultDatas;
    }

    @Override
    public List<ShapingResultData> getMoldNo() {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("mold_no").select("mold_no");
        List<ShapingResultData> shapingResultDatas = shapingResultDataMapper.selectList(queryWrapper);
        return shapingResultDatas;
    }

    private ShapingResultData getShapingResultData(String category, String project, String partName, String material, String moldNo) {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material)
                .eq(StringUtils.isNotBlank(moldNo), "mold_no", moldNo);

        List<ShapingResultData> shapingResultDataList = shapingResultDataMapper.selectList(queryWrapper);
        if (shapingResultDataList != null && shapingResultDataList.size() > 0) {
            return shapingResultDataList.get(0);
        }
        return null;
    }
}
