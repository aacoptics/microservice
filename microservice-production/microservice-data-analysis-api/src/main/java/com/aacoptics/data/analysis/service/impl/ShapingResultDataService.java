package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class ShapingResultDataService extends ServiceImpl<ShapingResultDataMapper, ShapingResultData> implements IShapingResultDataService {

    @Autowired
    private ShapingResultDataMapper shapingResultDataMapper;

    @Override
    public void importShapingResultDataExcel(InputStream in) throws Exception {
        List<String[]> excelDataList = ExcelUtil.read(in).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产成型结果数据表".equals(title)) {
            throw new BusinessException("Excel模板错误，请确认!");
        }
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

            String coreThickness = dataArray[5];
            String coreThicknessRange = dataArray[6];
            String r1VectorHeight = dataArray[7];
            String r1VectorHeightRange = dataArray[8];
            String r2VectorHeight = dataArray[9];
            String r2VectorHeightRange = dataArray[10];
            String outerDiameterEcc = dataArray[11];
            String kanheEcc = dataArray[12];
            String faceEcc = dataArray[13];
            String annealingProcess = dataArray[14];
            String bpKanheRoundness = dataArray[15];
            String dmpKanheRoundness = dataArray[16];
            String outerDiameterAverage = dataArray[17];
            String outerDiameterRange = dataArray[18];
            String outerDiameterRoundness = dataArray[19];
            String outerDiameterShrinkage = dataArray[20];
            String outerDiameterRoughness = dataArray[21];
            String r1Flatness = dataArray[22];
            String r2Flatness = dataArray[23];
            String r1SplitAverage = dataArray[24];
            String r2SplitAverage = dataArray[25];
            String wftR1 = dataArray[26];
            String wftR2 = dataArray[27];
            String wftConsistency = dataArray[28];
            String wftMaxAs = dataArray[29];
            String wftStability = dataArray[30];
            String cftR1 = dataArray[31];
            String cftR2 = dataArray[32];
            String cftConsistency = dataArray[33];
            String cftMaxAs = dataArray[34];
            String coatingTrend = dataArray[35];
            String cfsrR1 = dataArray[36];
            String cfsrR2 = dataArray[37];
            String cfsrR1R2 = dataArray[38];
            String burr = dataArray[39];
            String weldline = dataArray[40];
            String appearanceProblem = dataArray[41];
            String appearanceImg = dataArray[42];
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
    public IPage<ShapingResultData> getDataByConditions(Page<ShapingResultData> iPage, String category, String project, String partName, String material, String moldNo) {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(category), "category", category)
                .like(StringUtils.isNotBlank(project), "project", project)
                .like(StringUtils.isNotBlank(partName), "part_name", partName)
                .like(StringUtils.isNotBlank(material), "material", material)
                .like(StringUtils.isNotBlank(moldNo), "mold_no", moldNo);
        IPage<ShapingResultData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<ShapingResultData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<ShapingResultData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .like(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .like(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .like(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial())
                .like(StringUtils.isNotBlank(queryParams.getMoldNo()), "mold_no", queryParams.getMoldNo());
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
