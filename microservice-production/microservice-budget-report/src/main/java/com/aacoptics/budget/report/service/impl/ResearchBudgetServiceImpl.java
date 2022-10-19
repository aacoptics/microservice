package com.aacoptics.budget.report.service.impl;


import com.aacoptics.budget.report.constants.UploadLogStatusConstants;
import com.aacoptics.budget.report.entity.param.ResearchBudgetQueryParam;
import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.aacoptics.budget.report.entity.po.ResearchBudget;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.mapper.ResearchBudgetMapper;
import com.aacoptics.budget.report.service.BudgetUploadLogService;
import com.aacoptics.budget.report.service.ResearchBudgetService;
import com.aacoptics.budget.report.util.ExcelUtil;
import com.aacoptics.common.core.util.UserContextHolder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class ResearchBudgetServiceImpl extends ServiceImpl<ResearchBudgetMapper, ResearchBudget> implements ResearchBudgetService {


    @Resource
    private BudgetUploadLogService budgetUploadLogService;

    @Resource
    private ResearchBudgetMapper researchBudgetMapper;


    private String getCurrentUsername() {
        return StringUtils.defaultIfBlank(UserContextHolder.getInstance().getUsername(), "IoT");
    }


    @Override
    public Map<String, Object> query(Page page, ResearchBudgetQueryParam researchBudgetQueryParam) {
        Long uploadLogId = researchBudgetQueryParam.getUploadLogId();
        if(uploadLogId == null)
        {
            throw new BusinessException("上传日志ID不能为空");
        }
        //获取存在的年份数据
        List<Integer>  yearList = researchBudgetMapper.findResearchBudgetAllYearByUploadLogId(uploadLogId);
        if(yearList == null || yearList.size() == 0)
        {
            throw new BusinessException("数据不存在");
        }
        //构建表头
        JSONArray titleJsonArray = this.createTableTitle(yearList);

        //构建查询列
        String selectColumn = this.createSelectColumn(yearList);

        List<Map<String, Object>> researchBudgetList = researchBudgetMapper.findResearchBudgetByUploadLogId(
                selectColumn, researchBudgetQueryParam.getUploadLogId(), yearList.get(0), yearList.get(1));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("columns", titleJsonArray);
        resultMap.put("data", researchBudgetList);

        return resultMap;
    }


    /**
     * 创建查询列
     * @param yearList
     * @return
     */
    private String createSelectColumn(List<Integer> yearList)
    {
        StringBuffer selectColumn = new StringBuffer(); // 例：temp_year_01.month_01_value month_01_value

        for(int i=0; i<yearList.size(); i++)
        {
            Integer year = yearList.get(i);
            for(int j=1; j<=12; j++)
            {
                String monthStr = String.format("%02d", j);
                String columnName = year + monthStr;
                selectColumn.append(", temp_year_" + (i+1) + ".month_"+ monthStr +"_value as '" + columnName + "'");
            }
            for(int k=1; k<=4; k++)
            {
                String columnName = year + "Q" + k;
                selectColumn.append(", temp_year_" + (i+1) + ".q"+ k +"_value as '" + columnName + "'");
            }
            String columnName = year + "全年";
            selectColumn.append(", temp_year_" + (i+1) + ".year_value as '" + columnName + "'");
        }

        return selectColumn.toString();
    }

    /**
     * 创建前端页面表头列
     *
     * @param yearList
     * @return
     */
    private JSONArray createTableTitle(List<Integer> yearList)
    {
        JSONArray titleJsonArray = new JSONArray();
        JSONObject businessDivisionColumnJsonObject = new JSONObject();
        businessDivisionColumnJsonObject.put("prop", "businessDivision");
        businessDivisionColumnJsonObject.put("label", "事业部");
        businessDivisionColumnJsonObject.put("minWidth", "150");
        titleJsonArray.add(businessDivisionColumnJsonObject);

        JSONObject productLineColumnJsonObject = new JSONObject();
        productLineColumnJsonObject.put("prop", "productLine");
        productLineColumnJsonObject.put("label", "产品线");
        productLineColumnJsonObject.put("minWidth", "120");
        titleJsonArray.add(productLineColumnJsonObject);

        JSONObject dataVersionColumnJsonObject = new JSONObject();
        dataVersionColumnJsonObject.put("prop", "dataVersion");
        dataVersionColumnJsonObject.put("label", "数据版本");
        dataVersionColumnJsonObject.put("minWidth", "150");
        titleJsonArray.add(dataVersionColumnJsonObject);

        JSONObject itemSeqColumnJsonObject = new JSONObject();
        itemSeqColumnJsonObject.put("prop", "itemSeq");
        itemSeqColumnJsonObject.put("label", "科目序号");
        itemSeqColumnJsonObject.put("minWidth", "120");
        titleJsonArray.add(itemSeqColumnJsonObject);

        JSONObject rowNoColumnJsonObject = new JSONObject();
        rowNoColumnJsonObject.put("prop", "rowNo");
        rowNoColumnJsonObject.put("label", "行项目号");
        rowNoColumnJsonObject.put("minWidth", "120");
        titleJsonArray.add(rowNoColumnJsonObject);

        JSONObject costItemColumnJsonObject = new JSONObject();
        costItemColumnJsonObject.put("prop", "costItem");
        costItemColumnJsonObject.put("label", "费用项目");
        costItemColumnJsonObject.put("minWidth", "260");
        titleJsonArray.add(costItemColumnJsonObject);

        JSONObject costTypeColumnJsonObject = new JSONObject();
        costTypeColumnJsonObject.put("prop", "costType");
        costTypeColumnJsonObject.put("label", "固定/变动费用");
        costTypeColumnJsonObject.put("minWidth", "150");
        titleJsonArray.add(costTypeColumnJsonObject);

        JSONObject unitColumnJsonObject = new JSONObject();
        unitColumnJsonObject.put("prop", "unit");
        unitColumnJsonObject.put("label", "单位");
        unitColumnJsonObject.put("minWidth", "120");
        titleJsonArray.add(unitColumnJsonObject);

        for(Integer year : yearList)
        {
            for(int i=1; i<=12; i++) {
                String columnName = year + String.format("%02d", i);
                JSONObject valueJsonObject = new JSONObject();
                valueJsonObject.put("prop", columnName);
                valueJsonObject.put("label", columnName);
                valueJsonObject.put("minWidth", "120");
                titleJsonArray.add(valueJsonObject);
            }
            for(int j=1; j<4; j++)
            {
                String columnName = year + "Q" + j;
                JSONObject valueJsonObject = new JSONObject();
                valueJsonObject.put("prop", columnName);
                valueJsonObject.put("label", columnName);
                valueJsonObject.put("minWidth", "120");
                titleJsonArray.add(valueJsonObject);
            }

            String columnName = year + "全年";
            JSONObject valueJsonObject = new JSONObject();
            valueJsonObject.put("prop", columnName);
            valueJsonObject.put("label", columnName);
            valueJsonObject.put("minWidth", "120");
            titleJsonArray.add(valueJsonObject);
        }

        return titleJsonArray;
    }

    @Override
    public boolean add(ResearchBudget researchBudget) {
        boolean isSuccess = this.save(researchBudget);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ResearchBudget researchBudget) {

        boolean isSuccess = this.updateById(researchBudget);
        return isSuccess;
    }


    @Override
    public ResearchBudget get(Long id) {
        ResearchBudget researchBudget = this.getById(id);
        if (Objects.isNull(researchBudget)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        return researchBudget;
    }



    @Transactional
    @Override
    public void importExcel(String originalFilename, InputStream in) throws Exception {
        List<List<String[]>> sheetList = ExcelUtil.read(in);

        List<String[]> excelDataList = sheetList.get(1);


        String[] titleArray = excelDataList.get(1);//标题行

        String businessDivisionTitle = titleArray[0];
        String productLineTitle = titleArray[1];
        String dataVersionTitle = titleArray[2];
        if ((!"事业部".equals(businessDivisionTitle)) || (!"产品线".equals(productLineTitle))|| (!"数据版本".equals(dataVersionTitle))) {
            throw new BusinessException("Excel模板错误，请确认!");
        }
        //获取事业部，产品线用于判断唯一性
        String[] dataRow = excelDataList.get(2);
        String tempBusinessDivision = dataRow[0]; //事业部
        String tempProductLine = dataRow[1]; //产品线

        QueryWrapper<ResearchBudget> queryWrapper = new QueryWrapper();
        queryWrapper.eq("business_division", tempBusinessDivision);
        queryWrapper.eq("product_line", tempProductLine);
        queryWrapper.orderByAsc("item_seq");
        ResearchBudget oldResearchBudget = researchBudgetMapper.selectOne(queryWrapper);
        //1 删除存在的相同事业部，产品线数据
        Long oldUploadLogId = oldResearchBudget.getUploadLogId();

        QueryWrapper<ResearchBudget> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("upload_log_id", oldUploadLogId);
        researchBudgetMapper.delete(deleteWrapper);
        //2 更新上传日志记录状态
        BudgetUploadLog oldUploadBudgetUploadLog = budgetUploadLogService.get(oldUploadLogId);
        oldUploadBudgetUploadLog.setStatus(UploadLogStatusConstants.NO);
        budgetUploadLogService.update(oldUploadBudgetUploadLog);

        //3 保存上传日志
        BudgetUploadLog budgetUploadLog = new BudgetUploadLog();
        budgetUploadLog.setExcelName(originalFilename);
        budgetUploadLog.setType("研发费用预算");
        budgetUploadLog.setUploadTime(LocalDateTime.now());
        budgetUploadLog.setUploadUser(this.getCurrentUsername());
        budgetUploadLog.setStatus(UploadLogStatusConstants.YES);
        budgetUploadLogService.add(budgetUploadLog);

        String firstYear = titleArray[9].substring(0, 4);
        String secondYear = titleArray[25].substring(0, 4);

        //4 保存数据
        for (int i = 2; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if (dataArray == null || dataArray.length == 0) {
                break;
            }
            String businessDivision = dataArray[0]; //事业部
            String productLine = dataArray[1]; //产品线
            String dataVersion = dataArray[2]; //数据版本
            String itemSeq = dataArray[3]; //科目序号
            String rowNo = dataArray[4]; //行项目号
            String costItem = dataArray[5]; //费用项目
            String costType = dataArray[6]; //"固定/变动费用"
            String unit = dataArray[7]; //单位

            for(int j=1; j<=2; j++)
            {
                ResearchBudget researchBudget = new ResearchBudget();
                if(j == 1) {
                    researchBudget.setYear(Integer.valueOf(firstYear));
                }else if(j ==2)
                {
                    researchBudget.setYear(Integer.valueOf(secondYear));
                }

                String month01Value = dataArray[7 + 1 * j]; //预算信息
                String month02Value = dataArray[7 + 2 * j]; //预算信息
                String month03Value = dataArray[7 + 3 * j]; //预算信息
                String month04Value = dataArray[7 + 4 * j]; //预算信息
                String month05Value = dataArray[7 + 5 * j]; //预算信息
                String month06Value = dataArray[7 + 6 * j]; //预算信息
                String month07Value = dataArray[7 + 7 * j]; //预算信息
                String month08Value = dataArray[7 + 8 * j]; //预算信息
                String month09Value = dataArray[7 + 9 * j]; //预算信息
                String month10Value = dataArray[7 + 10 * j]; //预算信息
                String month11Value = dataArray[7 + 11 * j]; //预算信息
                String month12Value = dataArray[7 + 12 * j]; //预算信息
                String q1Value = dataArray[7 + 13 * j]; //预算信息
                String q2Value = dataArray[7 + 14 * j]; //预算信息
                String q3Value = dataArray[7 + 15 * j]; //预算信息
                String q4Value = dataArray[7 + 16 * j]; //预算信息
                String yearValue = dataArray[7 + 17 * j]; //预算信息

                researchBudget.setBusinessDivision(businessDivision);
                researchBudget.setProductLine(productLine);
                researchBudget.setDataVersion(dataVersion);
                researchBudget.setItemSeq(Integer.valueOf(itemSeq));
                researchBudget.setRowNo(rowNo);
                researchBudget.setCostItem(costItem);
                researchBudget.setCostType(costType);
                researchBudget.setUnit(unit);

                researchBudget.setMonth01Value(new BigDecimal(month01Value));
                researchBudget.setMonth02Value(new BigDecimal(month02Value));
                researchBudget.setMonth03Value(new BigDecimal(month03Value));
                researchBudget.setMonth04Value(new BigDecimal(month04Value));
                researchBudget.setMonth05Value(new BigDecimal(month05Value));
                researchBudget.setMonth06Value(new BigDecimal(month06Value));
                researchBudget.setMonth07Value(new BigDecimal(month07Value));
                researchBudget.setMonth08Value(new BigDecimal(month08Value));
                researchBudget.setMonth09Value(new BigDecimal(month09Value));
                researchBudget.setMonth10Value(new BigDecimal(month10Value));
                researchBudget.setMonth11Value(new BigDecimal(month11Value));
                researchBudget.setMonth12Value(new BigDecimal(month12Value));

                researchBudget.setQ1Value(new BigDecimal(q1Value));
                researchBudget.setQ2Value(new BigDecimal(q2Value));
                researchBudget.setQ3Value(new BigDecimal(q3Value));
                researchBudget.setQ4Value(new BigDecimal(q4Value));
                researchBudget.setYearValue(new BigDecimal(yearValue));


                researchBudget.setUploadLogId(budgetUploadLog.getId());
                this.add(researchBudget);
            }
        }

    }

}
