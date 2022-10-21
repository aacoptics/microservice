package com.aacoptics.budget.report.service.impl;


import com.aacoptics.budget.report.constants.BudgetTypeConstants;
import com.aacoptics.budget.report.constants.UploadLogStatusConstants;
import com.aacoptics.budget.report.entity.param.ProductionCostBudgetQueryParam;
import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.aacoptics.budget.report.entity.po.ProductionCostBudget;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.mapper.ProductionCostBudgetMapper;
import com.aacoptics.budget.report.service.BudgetUploadLogService;
import com.aacoptics.budget.report.service.ProductionCostBudgetService;
import com.aacoptics.budget.report.util.ExcelUtil;
import com.aacoptics.common.core.util.UserContextHolder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
public class ProductionCostBudgetServiceImpl extends ServiceImpl<ProductionCostBudgetMapper, ProductionCostBudget> implements ProductionCostBudgetService {


    @Resource
    private BudgetUploadLogService budgetUploadLogService;

    @Resource
    private ProductionCostBudgetMapper productionCostBudgetMapper;


    private String getCurrentUsername() {
        return StringUtils.defaultIfBlank(UserContextHolder.getInstance().getUsername(), "IoT");
    }


    @Override
    public Map<String, Object> query(Page page, ProductionCostBudgetQueryParam productionCostBudgetQueryParam) {
        Long uploadLogId = productionCostBudgetQueryParam.getUploadLogId();
        if (uploadLogId == null) {
            throw new BusinessException("上传日志ID不能为空");
        }
        //获取存在的年份数据
        List<Integer> yearList = productionCostBudgetMapper.findProductionCostBudgetAllYearByUploadLogId(uploadLogId);
        if (yearList == null || yearList.size() == 0) {
            throw new BusinessException("数据不存在");
        }
        //构建表头
        JSONArray titleJsonArray = this.createTableTitle(yearList);

        //构建查询列
        String selectColumn = this.createSelectColumn(yearList);

        List<Map<String, Object>> productionCostBudgetList = productionCostBudgetMapper.findProductionCostBudgetByUploadLogId(
                selectColumn, productionCostBudgetQueryParam.getUploadLogId(), yearList.get(0), yearList.get(1));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("columns", titleJsonArray);
        resultMap.put("data", productionCostBudgetList);

        return resultMap;
    }


    /**
     * 创建查询列
     *
     * @param yearList
     * @return
     */
    private String createSelectColumn(List<Integer> yearList) {
        StringBuffer selectColumn = new StringBuffer(); // 例：temp_year_01.month_01_value month_01_value

        for (int i = 0; i < yearList.size(); i++) {
            Integer year = yearList.get(i);
            for (int j = 1; j <= 12; j++) {
                String monthStr = String.format("%02d", j);
                String columnName = year + monthStr;
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_value as '" + columnName + "'");
            }
            String ytdColumnName = year + "YTD";
            selectColumn.append(", temp_year_" + (i + 1) + ".ytd_value as '" + ytdColumnName + "'");

            String ytgColumnName = year + "YTG";
            selectColumn.append(", temp_year_" + (i + 1) + ".ytg_value as '" + ytgColumnName + "'");

            String columnName = year + "小计";
            selectColumn.append(", temp_year_" + (i + 1) + ".year_value as '" + columnName + "'");
        }

        return selectColumn.toString();
    }

    /**
     * 创建前端页面表头列
     *
     * @param yearList
     * @return
     */
    private JSONArray createTableTitle(List<Integer> yearList) {
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
        rowNoColumnJsonObject.put("prop", "category1");
        rowNoColumnJsonObject.put("label", "分类1");
        rowNoColumnJsonObject.put("minWidth", "200");
        titleJsonArray.add(rowNoColumnJsonObject);

        JSONObject costItemColumnJsonObject = new JSONObject();
        costItemColumnJsonObject.put("prop", "category2");
        costItemColumnJsonObject.put("label", "分类2");
        costItemColumnJsonObject.put("minWidth", "270");
        titleJsonArray.add(costItemColumnJsonObject);

        JSONObject costTypeColumnJsonObject = new JSONObject();
        costTypeColumnJsonObject.put("prop", "category3");
        costTypeColumnJsonObject.put("label", "分类3");
        costTypeColumnJsonObject.put("minWidth", "180");
        titleJsonArray.add(costTypeColumnJsonObject);

        JSONObject unitColumnJsonObject = new JSONObject();
        unitColumnJsonObject.put("prop", "unit");
        unitColumnJsonObject.put("label", "单位");
        unitColumnJsonObject.put("minWidth", "120");
        titleJsonArray.add(unitColumnJsonObject);

        JSONObject validationColumnJsonObject = new JSONObject();
        validationColumnJsonObject.put("prop", "validation");
        validationColumnJsonObject.put("label", "校验");
        validationColumnJsonObject.put("minWidth", "120");
        titleJsonArray.add(validationColumnJsonObject);

        for (Integer year : yearList) {
            for (int i = 1; i <= 12; i++) {
                String columnName = year + String.format("%02d", i);
                JSONObject valueJsonObject = new JSONObject();
                valueJsonObject.put("prop", columnName);
                valueJsonObject.put("label", columnName);
                valueJsonObject.put("minWidth", "120");
                titleJsonArray.add(valueJsonObject);
            }
            String ytdColumnName = year + "YTD";
            JSONObject ytdValueJsonObject = new JSONObject();
            ytdValueJsonObject.put("prop", ytdColumnName);
            ytdValueJsonObject.put("label", ytdColumnName);
            ytdValueJsonObject.put("minWidth", "120");
            titleJsonArray.add(ytdValueJsonObject);

            String ytgColumnName = year + "YTD";
            JSONObject ytgValueJsonObject = new JSONObject();
            ytgValueJsonObject.put("prop", ytgColumnName);
            ytgValueJsonObject.put("label", ytgColumnName);
            ytgValueJsonObject.put("minWidth", "120");
            titleJsonArray.add(ytgValueJsonObject);

            String columnName = year + "小计";
            JSONObject valueJsonObject = new JSONObject();
            valueJsonObject.put("prop", columnName);
            valueJsonObject.put("label", columnName);
            valueJsonObject.put("minWidth", "120");
            titleJsonArray.add(valueJsonObject);
        }

        return titleJsonArray;
    }

    @Override
    public boolean add(ProductionCostBudget productionCostBudget) {
        boolean isSuccess = this.save(productionCostBudget);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ProductionCostBudget productionCostBudget) {

        boolean isSuccess = this.updateById(productionCostBudget);
        return isSuccess;
    }


    @Override
    public ProductionCostBudget get(Long id) {
        ProductionCostBudget productionCostBudget = this.getById(id);
        if (Objects.isNull(productionCostBudget)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        return productionCostBudget;
    }


    @Transactional
    @Override
    public void importExcel(String originalFilename, MultipartFile file,  InputStream in) throws Exception {
        List<List<String[]>> sheetList = ExcelUtil.read(in);

        List<String[]> excelDataList = sheetList.get(1);


        String[] titleArray = excelDataList.get(1);//标题行

        String businessDivisionTitle = titleArray[0];
        String productLineTitle = titleArray[1];
        String dataVersionTitle = titleArray[2];
        if ((!"事业部".equals(businessDivisionTitle)) || (!"产品线".equals(productLineTitle)) || (!"数据版本".equals(dataVersionTitle))) {
            throw new BusinessException("Excel模板错误，请确认!");
        }

        InputStream excelImageInputStream = file.getInputStream();

        byte[] excelImageData = new byte[(int) file.getSize()];
        excelImageInputStream.read(excelImageData);

        //获取事业部，产品线用于判断唯一性
        String[] dataRow = excelDataList.get(2);
        String tempBusinessDivision = dataRow[0]; //事业部
        String tempProductLine = dataRow[1]; //产品线

        ProductionCostBudget oldProductionCostBudget = productionCostBudgetMapper.findByBusinessDivisionAndProductLine(tempBusinessDivision, tempProductLine);
       if(oldProductionCostBudget != null) {
           //1 删除存在的相同事业部，产品线数据
           Long oldUploadLogId = oldProductionCostBudget.getUploadLogId();

           QueryWrapper<ProductionCostBudget> deleteWrapper = new QueryWrapper<>();
           deleteWrapper.eq("upload_log_id", oldUploadLogId);
           productionCostBudgetMapper.delete(deleteWrapper);
           //2 更新上传日志记录状态
           BudgetUploadLog oldUploadBudgetUploadLog = budgetUploadLogService.get(oldUploadLogId);
           oldUploadBudgetUploadLog.setStatus(UploadLogStatusConstants.NO);
           budgetUploadLogService.update(oldUploadBudgetUploadLog);
       }

        //3 保存上传日志
        BudgetUploadLog budgetUploadLog = new BudgetUploadLog();
        budgetUploadLog.setExcelName(originalFilename);
        budgetUploadLog.setType(BudgetTypeConstants.PRODUCTION_COST_BUDGET);
        budgetUploadLog.setUploadTime(LocalDateTime.now());
        budgetUploadLog.setUploadUser(this.getCurrentUsername());
        budgetUploadLog.setStatus(UploadLogStatusConstants.YES);
        budgetUploadLog.setExcelImage(excelImageData);
        budgetUploadLogService.add(budgetUploadLog);

        excelImageInputStream.close();

        String firstYear = titleArray[9].substring(0, 4);
        String secondYear = titleArray[24].substring(0, 4);

        //4 保存数据
        for (int i = 2; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if (dataArray == null || dataArray.length == 0) {
                break;
            }
            String businessDivision = dataArray[0]; //事业部
            if(StringUtils.isEmpty(businessDivision))
            {
                continue;
            }
            String productLine = dataArray[1]; //产品线
            String dataVersion = dataArray[2]; //数据版本
            String itemSeq = dataArray[3]; //科目序号
            String category1 = dataArray[4]; //分类1
            String category2 = dataArray[5]; //分类2
            String category3 = dataArray[6]; //分类3
            String unit = dataArray[7]; //单位
            String validation = dataArray[8]; //校验

            ProductionCostBudget firstProductionCostBudget = new ProductionCostBudget();
            firstProductionCostBudget.setYear(Integer.valueOf(firstYear));

            String month01Value = dataArray[9]; //预算信息
            String month02Value = dataArray[10]; //预算信息
            String month03Value = dataArray[11]; //预算信息
            String month04Value = dataArray[12]; //预算信息
            String month05Value = dataArray[13]; //预算信息
            String month06Value = dataArray[14]; //预算信息
            String month07Value = dataArray[15]; //预算信息
            String month08Value = dataArray[16]; //预算信息
            String month09Value = dataArray[17]; //预算信息
            String month10Value = dataArray[18]; //预算信息
            String month11Value = dataArray[19]; //预算信息
            String month12Value = dataArray[20]; //预算信息
            String ytdValue = dataArray[21]; //预算信息
            String ytgValue = dataArray[22]; //预算信息
            String yearValue = dataArray[23]; //年度小计预算信息

            firstProductionCostBudget.setBusinessDivision(businessDivision);
            firstProductionCostBudget.setProductLine(productLine);
            firstProductionCostBudget.setDataVersion(dataVersion);
            firstProductionCostBudget.setItemSeq(Integer.valueOf(itemSeq));
            firstProductionCostBudget.setCategory1(category1);
            firstProductionCostBudget.setCategory2(category2);
            firstProductionCostBudget.setCategory3(category3);
            firstProductionCostBudget.setUnit(unit);
            firstProductionCostBudget.setValidation(validation);

            firstProductionCostBudget.setMonth01Value(this.convertStringToBigDecimal(month01Value));
            firstProductionCostBudget.setMonth02Value(this.convertStringToBigDecimal(month02Value));
            firstProductionCostBudget.setMonth03Value(this.convertStringToBigDecimal(month03Value));
            firstProductionCostBudget.setMonth04Value(this.convertStringToBigDecimal(month04Value));
            firstProductionCostBudget.setMonth05Value(this.convertStringToBigDecimal(month05Value));
            firstProductionCostBudget.setMonth06Value(this.convertStringToBigDecimal(month06Value));
            firstProductionCostBudget.setMonth07Value(this.convertStringToBigDecimal(month07Value));
            firstProductionCostBudget.setMonth08Value(this.convertStringToBigDecimal(month08Value));
            firstProductionCostBudget.setMonth09Value(this.convertStringToBigDecimal(month09Value));
            firstProductionCostBudget.setMonth10Value(this.convertStringToBigDecimal(month10Value));
            firstProductionCostBudget.setMonth11Value(this.convertStringToBigDecimal(month11Value));
            firstProductionCostBudget.setMonth12Value(this.convertStringToBigDecimal(month12Value));

            firstProductionCostBudget.setYtdValue(this.convertStringToBigDecimal(ytdValue));
            firstProductionCostBudget.setYtGValue(this.convertStringToBigDecimal(ytgValue));
            firstProductionCostBudget.setYearValue(this.convertStringToBigDecimal(yearValue));

            firstProductionCostBudget.setUploadLogId(budgetUploadLog.getId());
            this.add(firstProductionCostBudget);

            //保存第二年数据
            ProductionCostBudget secondProductionCostBudget = new ProductionCostBudget();
            secondProductionCostBudget.setYear(Integer.valueOf(secondYear));


            String secondMonth01Value = dataArray[24]; //预算信息
            String secondMonth02Value = dataArray[25]; //预算信息
            String secondMonth03Value = dataArray[26]; //预算信息
            String secondMonth04Value = dataArray[27]; //预算信息
            String secondMonth05Value = dataArray[28]; //预算信息
            String secondMonth06Value = dataArray[29]; //预算信息
            String secondMonth07Value = dataArray[30]; //预算信息
            String secondMonth08Value = dataArray[31]; //预算信息
            String secondMonth09Value = dataArray[32]; //预算信息
            String secondMonth10Value = dataArray[33]; //预算信息
            String secondMonth11Value = dataArray[34]; //预算信息
            String secondMonth12Value = dataArray[35]; //预算信息
            String secondYearValue = dataArray[36]; //年度小计预算信息

            secondProductionCostBudget.setBusinessDivision(businessDivision);
            secondProductionCostBudget.setProductLine(productLine);
            secondProductionCostBudget.setDataVersion(dataVersion);
            secondProductionCostBudget.setItemSeq(Integer.valueOf(itemSeq));
            secondProductionCostBudget.setCategory1(category1);
            secondProductionCostBudget.setCategory2(category2);
            secondProductionCostBudget.setCategory3(category3);
            secondProductionCostBudget.setUnit(unit);
            secondProductionCostBudget.setValidation(validation);

            secondProductionCostBudget.setMonth01Value(this.convertStringToBigDecimal(secondMonth01Value));
            secondProductionCostBudget.setMonth02Value(this.convertStringToBigDecimal(secondMonth02Value));
            secondProductionCostBudget.setMonth03Value(this.convertStringToBigDecimal(secondMonth03Value));
            secondProductionCostBudget.setMonth04Value(this.convertStringToBigDecimal(secondMonth04Value));
            secondProductionCostBudget.setMonth05Value(this.convertStringToBigDecimal(secondMonth05Value));
            secondProductionCostBudget.setMonth06Value(this.convertStringToBigDecimal(secondMonth06Value));
            secondProductionCostBudget.setMonth07Value(this.convertStringToBigDecimal(secondMonth07Value));
            secondProductionCostBudget.setMonth08Value(this.convertStringToBigDecimal(secondMonth08Value));
            secondProductionCostBudget.setMonth09Value(this.convertStringToBigDecimal(secondMonth09Value));
            secondProductionCostBudget.setMonth10Value(this.convertStringToBigDecimal(secondMonth10Value));
            secondProductionCostBudget.setMonth11Value(this.convertStringToBigDecimal(secondMonth11Value));
            secondProductionCostBudget.setMonth12Value(this.convertStringToBigDecimal(secondMonth12Value));

            secondProductionCostBudget.setYearValue(this.convertStringToBigDecimal(secondYearValue));

            secondProductionCostBudget.setUploadLogId(budgetUploadLog.getId());
            this.add(secondProductionCostBudget);
        }
    }


    /**
     * 字符串转BigDeciaml
     * @param value
     * @return
     */
    private BigDecimal convertStringToBigDecimal(String value)
    {
        if(StringUtils.isEmpty(value))
        {
            return null;
        }
        if("null".equals(value))
        {
            return null;
        }
        return new BigDecimal(value);
    }
}
