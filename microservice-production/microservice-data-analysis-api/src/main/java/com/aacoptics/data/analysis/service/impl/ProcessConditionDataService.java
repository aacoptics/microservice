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
import org.apache.poi.ss.usermodel.Workbook;
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
        Workbook workbook = ExcelUtil.getWorkbook(in);
        List<String[]> excelDataList = ExcelUtil.read(workbook).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产工艺条件数据表".equals(title)) {
            throw new BusinessException("Excel模板错误，请确认!");
        }
        int line = 4;
        try{
            for (int i = 4; i < excelDataList.size(); i++) {
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
                String moldNo = dataArray[6];
                ProcessConditionData processConditionData = this.getProcessConditionData(project, partName);
                if (processConditionData == null) {
                    processConditionData = new ProcessConditionData();
                }

                String moldType = dataArray[7];
                String mfMoldTemp = ExcelUtil.handleDecimal(dataArray[8], 0);
                String mfMaterialTemp = ExcelUtil.handleDecimal(dataArray[9], 0);
                String mfJetVelocity = ExcelUtil.handleDecimal(dataArray[10], 0);
                String mfVpSwitch = ExcelUtil.handleDecimal(dataArray[11], 0);
                String mfHoldPressure1 = ExcelUtil.handleDecimal(dataArray[12], 0);
                String mfHoldTime1 = ExcelUtil.handleDecimal(dataArray[13], 0);
                String mfHoldPressure2 = ExcelUtil.handleDecimal(dataArray[14], 0);
                String mfHoldTime2 = ExcelUtil.handleDecimal(dataArray[15], 0);
                String mfHoldPressure3 = ExcelUtil.handleDecimal(dataArray[16], 0);
                String mfHoldTime3 = ExcelUtil.handleDecimal(dataArray[17], 0);
                String mfHoldPressure4 = ExcelUtil.handleDecimal(dataArray[18], 0);
                String mfHoldTime4 = ExcelUtil.handleDecimal(dataArray[19], 0);
                String mfHoldPressure5 = ExcelUtil.handleDecimal(dataArray[20], 0);
                String mfHoldTime5 = ExcelUtil.handleDecimal(dataArray[21], 0);
                String mfHoldPressure6 = ExcelUtil.handleDecimal(dataArray[22], 0);
                String mfHoldTime6 = ExcelUtil.handleDecimal(dataArray[23], 0);
                String mfCoolingTime = ExcelUtil.handleDecimal(dataArray[24], 0);
                String moldTemp = ExcelUtil.handleDecimal(dataArray[25], 0);
                String materialTemp = ExcelUtil.handleDecimal(dataArray[26], 0);
                String jetVelocity = ExcelUtil.handleDecimal(dataArray[27], 0);
                String vpSwitch = ExcelUtil.handleDecimal(dataArray[28], 0);
                String holdPressure1 = ExcelUtil.handleDecimal(dataArray[29], 0);
                String holdTime1 = ExcelUtil.handleDecimal(dataArray[30], 0);
                String holdPressure2 = ExcelUtil.handleDecimal(dataArray[31], 0);
                String holdTime2 = ExcelUtil.handleDecimal(dataArray[32], 0);
                String holdPressure3 = ExcelUtil.handleDecimal(dataArray[33], 0);
                String holdTime3 = ExcelUtil.handleDecimal(dataArray[34], 0);
                String holdPressure4 = ExcelUtil.handleDecimal(dataArray[35], 0);
                String holdTime4 = ExcelUtil.handleDecimal(dataArray[36], 0);
                String holdPressure5 = ExcelUtil.handleDecimal(dataArray[37], 0);
                String holdTime5 = ExcelUtil.handleDecimal(dataArray[38], 0);
                String holdPressure6 = ExcelUtil.handleDecimal(dataArray[39], 0);
                String holdTime6 = ExcelUtil.handleDecimal(dataArray[40], 0);
                //String holdPressureVelocity = ExcelUtil.handleDecimal(dataArray[41], 0);
                String platenPosition = ExcelUtil.handleDecimal(dataArray[41], 0);
                String openingSpeed = ExcelUtil.handleDecimal(dataArray[42], 0);
                String ejectionSpeed = ExcelUtil.handleDecimal(dataArray[43], 0);
                String coolingTime = ExcelUtil.handleDecimal(dataArray[44], 0);
                String clampingForce = ExcelUtil.handleDecimal(dataArray[45], 0);
                String passivation = dataArray[46];

                // 设置参数
                processConditionData.setDepartment(department);
                processConditionData.setCategory(category);
                processConditionData.setLensNumber(lensNumber);
                processConditionData.setProject(project);
                processConditionData.setPartName(partName);
                processConditionData.setMaterial(material);
                processConditionData.setMoldNo(moldNo);
                processConditionData.setMoldType(moldType);
                processConditionData.setMfMoldTemp(mfMoldTemp);
                processConditionData.setMfMaterialTemp(mfMaterialTemp);
                processConditionData.setMfJetVelocity(mfJetVelocity);
                processConditionData.setMfVpSwitch(mfVpSwitch);
                processConditionData.setMfHoldPressure1(mfHoldPressure1);
                processConditionData.setMfHoldPressure2(mfHoldPressure2);
                processConditionData.setMfHoldPressure3(mfHoldPressure3);
                processConditionData.setMfHoldPressure4(mfHoldPressure4);
                processConditionData.setMfHoldPressure5(mfHoldPressure5);
                processConditionData.setMfHoldPressure6(mfHoldPressure6);
                processConditionData.setMfHoldTime1(mfHoldTime1);
                processConditionData.setMfHoldTime2(mfHoldTime2);
                processConditionData.setMfHoldTime3(mfHoldTime3);
                processConditionData.setMfHoldTime4(mfHoldTime4);
                processConditionData.setMfHoldTime5(mfHoldTime5);
                processConditionData.setMfHoldTime6(mfHoldTime6);
                processConditionData.setMfCoolingTime(mfCoolingTime);
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
                //processConditionData.setHoldPressureVelocity(holdPressureVelocity);
                processConditionData.setPlatenPosition(platenPosition);
                processConditionData.setOpeningSpeed(openingSpeed);
                processConditionData.setEjectionSpeed(ejectionSpeed);
                processConditionData.setCoolingTime(coolingTime);
                processConditionData.setClampingForce(clampingForce);
                processConditionData.setPassivation(passivation);

                this.saveOrUpdate(processConditionData);
            }
        }catch (Exception e){
            throw new BusinessException("第" + line + "行保存数据失败！" + e.getMessage());
        }finally {
            // 上传之后强制同步数据
            processConditionDataMapper.syncData();
            processConditionDataMapper.syncMoldType();
        }
    }

    @Override
    public IPage<ProcessConditionData> getDataByConditions(Page<ProcessConditionData> iPage, String category, String project,
                                                           String partName, String material, String department, String lensNumber) {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material)
                .eq(StringUtils.isNotBlank(department), "department", department)
                .eq(StringUtils.isNotBlank(lensNumber), "lens_number", lensNumber);
        IPage<ProcessConditionData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<ProcessConditionData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .eq(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .eq(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .eq(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial())
                .eq(StringUtils.isNotBlank(queryParams.getDepartment()), "department", queryParams.getDepartment())
                .eq(StringUtils.isNotBlank(queryParams.getLensNumber()), "lens_number", queryParams.getLensNumber());
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

    @Override
    public List<ProcessConditionData> getCategory() {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("category").select("category");
        List<ProcessConditionData> processConditionDatas = processConditionDataMapper.selectList(queryWrapper);
        return processConditionDatas;
    }

    @Override
    public List<ProcessConditionData> getProject() {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("project").select("project");
        List<ProcessConditionData> processConditionDatas = processConditionDataMapper.selectList(queryWrapper);
        return processConditionDatas;
    }

    @Override
    public List<ProcessConditionData> getPartName() {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("part_name").select("part_name");
        List<ProcessConditionData> processConditionDatas = processConditionDataMapper.selectList(queryWrapper);
        return processConditionDatas;
    }

    @Override
    public List<ProcessConditionData> getMaterial() {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("material").select("material");
        List<ProcessConditionData> processConditionDatas = processConditionDataMapper.selectList(queryWrapper);
        return processConditionDatas;
    }

    @Override
    public List<ProcessConditionData> getMoldNo() {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("mold_no").select("mold_no");
        List<ProcessConditionData> processConditionDatas = processConditionDataMapper.selectList(queryWrapper);
        return processConditionDatas;
    }

    @Override
    public List<ProcessConditionData> getDepartment() {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("department").select("department");
        List<ProcessConditionData> processConditionDatas = processConditionDataMapper.selectList(queryWrapper);
        return processConditionDatas;
    }

    @Override
    public List<ProcessConditionData> getLensNumber() {
        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("lens_number").select("lens_number");
        List<ProcessConditionData> processConditionDatas = processConditionDataMapper.selectList(queryWrapper);
        return processConditionDatas;
    }

    @Override
    public void deleteData() {
        processConditionDataMapper.deleteData();
    }

    private ProcessConditionData getProcessConditionData(String project, String partName) {

        QueryWrapper<ProcessConditionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName);

        List<ProcessConditionData> processConditionDataList = processConditionDataMapper.selectList(queryWrapper);
        if (processConditionDataList != null && processConditionDataList.size() > 0) {
            return processConditionDataList.get(0);
        }
        return null;
    }


}
