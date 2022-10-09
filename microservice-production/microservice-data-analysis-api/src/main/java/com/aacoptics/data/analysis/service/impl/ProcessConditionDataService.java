package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.ProcessConditionData;
import com.aacoptics.data.analysis.exception.BusinessException;
import com.aacoptics.data.analysis.mapper.ProcessConditionDataMapper;
import com.aacoptics.data.analysis.service.IProcessConditionDataService;
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
public class ProcessConditionDataService extends ServiceImpl<ProcessConditionDataMapper, ProcessConditionData> implements IProcessConditionDataService {

    @Autowired
    private ProcessConditionDataMapper processConditionDataMapper;


    @Override
    public void importProcessConditionDataExcel(InputStream in) throws Exception {
        List<String[]> excelDataList = ExcelUtil.read(in).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产工艺条件数据表".equals(title)) {
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

            ProcessConditionData processConditionData = this.getProcessConditionData(category, project, partName, material, moldNo);
            if (processConditionData == null) {
                processConditionData = new ProcessConditionData();
            }

            String moldTemp = dataArray[5];
            String materialTemp = dataArray[6];
            String jetVelocity = dataArray[7];
            String vpSwitch = dataArray[8];
            String holdPressure1 = dataArray[9];
            String holdPressure2 = dataArray[10];
            String holdPressure3 = dataArray[11];
            String holdPressure4 = dataArray[12];
            String holdPressure5 = dataArray[13];
            String holdPressure6 = dataArray[14];
            String holdTime1 = dataArray[15];
            String holdTime2 = dataArray[16];
            String holdTime3 = dataArray[17];
            String holdTime4 = dataArray[18];
            String holdTime5 = dataArray[19];
            String holdTime6 = dataArray[20];
            String holdPressureVelocity = dataArray[21];
            String platenPosition = dataArray[22];
            String openingSpeed = dataArray[23];
            String ejectionSpeed = dataArray[24];
            String coolingTime = dataArray[25];
            String clampingForce = dataArray[26];
            String passivation = dataArray[27];

            // 设置参数
            processConditionData.setCategory(category);
            processConditionData.setProject(project);
            processConditionData.setMoldNo(moldNo);
            processConditionData.setPartName(partName);
            processConditionData.setMaterial(material);
            processConditionData.setMoldTemp(moldTemp);
            processConditionData.setMaterialTemp(materialTemp);
            processConditionData.setJetVelocity(jetVelocity);
            processConditionData.setVpSwitch(vpSwitch);
            processConditionData.setHoldPressure1(holdPressure1);
            processConditionData.setHoldPressure2(holdPressure2);
            processConditionData.setHoldPressure3(holdPressure3);
            processConditionData.setHoldPressure4(holdPressure4);
            processConditionData.setHoldPressure5(holdPressure5);
            processConditionData.setHoldPressure6(holdPressure6);
            processConditionData.setHoldTime1(holdTime1);
            processConditionData.setHoldTime2(holdTime2);
            processConditionData.setHoldTime3(holdTime3);
            processConditionData.setHoldTime4(holdTime4);
            processConditionData.setHoldTime5(holdTime5);
            processConditionData.setHoldTime6(holdTime6);
            processConditionData.setHoldPressureVelocity(holdPressureVelocity);
            processConditionData.setPlatenPosition(platenPosition);
            processConditionData.setOpeningSpeed(openingSpeed);
            processConditionData.setEjectionSpeed(ejectionSpeed);
            processConditionData.setCoolingTime(coolingTime);
            processConditionData.setClampingForce(clampingForce);
            processConditionData.setPassivation(passivation);

            this.saveOrUpdate(processConditionData);

        }

    }

    @Override
    public IPage<ProcessConditionData> getDataByConditions(Page<ProcessConditionData> iPage, String category, String project, String partName, String material, String moldNo) {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(category), "category", category)
                .like(StringUtils.isNotBlank(project), "project", project)
                .like(StringUtils.isNotBlank(partName), "part_name", partName)
                .like(StringUtils.isNotBlank(material), "material", material)
                .like(StringUtils.isNotBlank(moldNo), "mold_no", moldNo);
        IPage<ProcessConditionData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<ProcessConditionData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .like(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .like(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .like(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial())
                .like(StringUtils.isNotBlank(queryParams.getMoldNo()), "mold_no", queryParams.getMoldNo());
        List<ProcessConditionData> processConditionDatas = processConditionDataMapper.selectList(queryWrapper);
        return processConditionDatas;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ProcessConditionData processConditionData) {
        return this.updateById(processConditionData);
    }

    private ProcessConditionData getProcessConditionData(String category, String project, String partName, String material, String moldNo) {

        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material)
                .eq(StringUtils.isNotBlank(moldNo), "mold_no", moldNo);

        List<ProcessConditionData> processConditionDataList = processConditionDataMapper.selectList(queryWrapper);
        if (processConditionDataList != null && processConditionDataList.size() > 0) {
            return processConditionDataList.get(0);
        }
        return null;
    }


}
