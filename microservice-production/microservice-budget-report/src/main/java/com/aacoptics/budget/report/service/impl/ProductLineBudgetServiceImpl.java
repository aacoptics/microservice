package com.aacoptics.budget.report.service.impl;


import com.aacoptics.budget.report.constants.BudgetTypeConstants;
import com.aacoptics.budget.report.constants.UploadLogStatusConstants;
import com.aacoptics.budget.report.entity.param.ProductLineBudgetQueryParam;
import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.aacoptics.budget.report.entity.po.ProductLineBudget;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.mapper.ProductLineBudgetMapper;
import com.aacoptics.budget.report.service.BudgetUploadLogService;
import com.aacoptics.budget.report.service.ProductLineBudgetService;
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
import java.util.*;

@Service
@Slf4j
public class ProductLineBudgetServiceImpl extends ServiceImpl<ProductLineBudgetMapper, ProductLineBudget> implements ProductLineBudgetService {


    @Resource
    private BudgetUploadLogService budgetUploadLogService;

    @Resource
    private ProductLineBudgetMapper productLineBudgetMapper;


    private String getCurrentUsername() {
        return StringUtils.defaultIfBlank(UserContextHolder.getInstance().getUsername(), "IoT");
    }


    @Override
    public Map<String, Object> query(Page page, ProductLineBudgetQueryParam productLineBudgetQueryParam) {
        Long uploadLogId = productLineBudgetQueryParam.getUploadLogId();
        if (uploadLogId == null) {
            throw new BusinessException("上传日志ID不能为空");
        }
        //获取存在的年份数据
        List<Integer> yearList = productLineBudgetMapper.findProductLineBudgetAllYearByUploadLogId(uploadLogId);
        if (yearList == null || yearList.size() == 0) {
            throw new BusinessException("数据不存在");
        }
        //构建表头
        JSONArray titleJsonArray = this.createTableTitle(yearList);

        //构建查询列
        String selectColumn = this.createSelectColumn(yearList);

        List<Map<String, Object>> productLineBudgetList = productLineBudgetMapper.findProductLineBudgetByUploadLogId(
                selectColumn, productLineBudgetQueryParam.getUploadLogId(), yearList.get(0), yearList.get(1));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("columns", titleJsonArray);
        resultMap.put("data", productLineBudgetList);

        return resultMap;
    }


    /**
     * 创建查询列
     *
     * @param yearList
     * @return
     */
    private String createSelectColumn(List<Integer> yearList) {
        StringBuffer selectColumn = new StringBuffer(); // 例：temp_year_1.month_01_phone_value month_01_phone_value

        for (int i = 0; i < yearList.size(); i++) {
            Integer year = yearList.get(i);
            for (int j = 1; j <= 12; j++) {
                String monthStr = String.format("%02d", j);
                String columnName = year + monthStr;
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_phone_value as '" + columnName + "手机类'");
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_tv_value as '" + columnName + "TV'");
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_watch_value as '" + columnName + "手表'");
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_ar_vr_value as '" + columnName + "AR/VR'");
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_laptop_value as '" + columnName + "笔电'");
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_tablet_value as '" + columnName + "平板'");
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_vehicle_value as '" + columnName + "车载'");
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_iot_other_value as '" + columnName + "IOT&Other'");
                selectColumn.append(", temp_year_" + (i + 1) + ".month_" + monthStr + "_total_value as '" + columnName + "小计'");

                if(j%3 == 0)
                {
                    String qColumnName = year + "Q" + j/3;
                    selectColumn.append(", temp_year_" + (i + 1) + ".q" + j/3 + "_phone_value as '" + qColumnName + "手机类'");
                    selectColumn.append(", temp_year_" + (i + 1) + ".q" + j/3 + "_tv_value as '" + qColumnName + "TV'");
                    selectColumn.append(", temp_year_" + (i + 1) + ".q" + j/3 + "_watch_value as '" + qColumnName + "手表'");
                    selectColumn.append(", temp_year_" + (i + 1) + ".q" + j/3 + "_ar_vr_value as '" + qColumnName + "AR/VR'");
                    selectColumn.append(", temp_year_" + (i + 1) + ".q" + j/3 + "_laptop_value as '" + qColumnName + "笔电'");
                    selectColumn.append(", temp_year_" + (i + 1) + ".q" + j/3 + "_tablet_value as '" + qColumnName + "平板'");
                    selectColumn.append(", temp_year_" + (i + 1) + ".q" + j/3 + "_vehicle_value as '" + qColumnName + "车载'");
                    selectColumn.append(", temp_year_" + (i + 1) + ".q" + j/3 + "_iot_other_value as '" + qColumnName + "IOT&Other'");
                    selectColumn.append(", temp_year_" + (i + 1) + ".q" + j/3 + "_total_value as '" + qColumnName + "小计'");
                }
            }

            String columnName = year + "Yr";
            selectColumn.append(", temp_year_" + (i + 1) + ".year_phone_value as '" + columnName + "手机类'");
            selectColumn.append(", temp_year_" + (i + 1) + ".year_tv_value as '" + columnName + "TV'");
            selectColumn.append(", temp_year_" + (i + 1) + ".year_watch_value as '" + columnName + "手表'");
            selectColumn.append(", temp_year_" + (i + 1) + ".year_ar_vr_value as '" + columnName + "AR/VR'");
            selectColumn.append(", temp_year_" + (i + 1) + ".year_laptop_value as '" + columnName + "笔电'");
            selectColumn.append(", temp_year_" + (i + 1) + ".year_tablet_value as '" + columnName + "平板'");
            selectColumn.append(", temp_year_" + (i + 1) + ".year_vehicle_value as '" + columnName + "车载'");
            selectColumn.append(", temp_year_" + (i + 1) + ".year_iot_other_value as '" + columnName + "IOT&Other'");
            selectColumn.append(", temp_year_" + (i + 1) + ".year_total_value as '" + columnName + "小计'");
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


        JSONObject costItemColumnJsonObject = new JSONObject();
        costItemColumnJsonObject.put("prop", "costItem");
        costItemColumnJsonObject.put("label", "科目");
        costItemColumnJsonObject.put("minWidth", "260");
        titleJsonArray.add(costItemColumnJsonObject);


        JSONObject unitColumnJsonObject = new JSONObject();
        unitColumnJsonObject.put("prop", "unit");
        unitColumnJsonObject.put("label", "单位");
        unitColumnJsonObject.put("minWidth", "120");
        titleJsonArray.add(unitColumnJsonObject);


        List<String> suffixList = new ArrayList<>();
        suffixList.add("手机类");
        suffixList.add("TV");
        suffixList.add("手表");
        suffixList.add("AR/VR");
        suffixList.add("笔电");
        suffixList.add("平板");
        suffixList.add("车载");
        suffixList.add("IOT&Other");
        suffixList.add("小计");

        for (Integer year : yearList) {
            for (int i = 1; i <= 12; i++) {
                String columnName = year + String.format("%02d", i);
                for(String suffix : suffixList)
                {
                    JSONObject valueJsonObject = new JSONObject();
                    valueJsonObject.put("prop", columnName+suffix);
                    valueJsonObject.put("label", columnName+suffix);
                    valueJsonObject.put("minWidth", "170");
                    titleJsonArray.add(valueJsonObject);
                }
                if(i%3 == 0)
                {
                    String qColumnName = year + "Q" + i/3;
                    for(String suffix : suffixList) {
                        JSONObject valueJsonObject = new JSONObject();
                        valueJsonObject.put("prop", qColumnName+suffix);
                        valueJsonObject.put("label", qColumnName+suffix);
                        valueJsonObject.put("minWidth", "170");
                        titleJsonArray.add(valueJsonObject);
                    }
                }
            }

            String columnName = year + "Yr";
            for(String suffix : suffixList) {
                JSONObject valueJsonObject = new JSONObject();
                valueJsonObject.put("prop", columnName+suffix);
                valueJsonObject.put("label", columnName+suffix);
                valueJsonObject.put("minWidth", "170");
                titleJsonArray.add(valueJsonObject);
            }
        }

        return titleJsonArray;
    }

    @Override
    public boolean add(ProductLineBudget productLineBudget) {
        boolean isSuccess = this.save(productLineBudget);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ProductLineBudget productLineBudget) {

        boolean isSuccess = this.updateById(productLineBudget);
        return isSuccess;
    }


    @Override
    public ProductLineBudget get(Long id) {
        ProductLineBudget productLineBudget = this.getById(id);
        if (Objects.isNull(productLineBudget)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        return productLineBudget;
    }


    @Transactional
    @Override
    public void importExcel(String originalFilename, MultipartFile file,  InputStream in) throws Exception {
        List<List<String[]>> sheetList = ExcelUtil.read(in);

        List<String[]> excelDataList = sheetList.get(0);


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
        String[] dataRow = excelDataList.get(3);
        String tempBusinessDivision = dataRow[0]; //事业部
        String tempProductLine = dataRow[1]; //产品线

        ProductLineBudget oldProductLineBudget = productLineBudgetMapper.findByBusinessDivisionAndProductLine(tempBusinessDivision, tempProductLine);
        if (oldProductLineBudget != null) {
            //1 删除存在的相同事业部，产品线数据
            Long oldUploadLogId = oldProductLineBudget.getUploadLogId();

            QueryWrapper<ProductLineBudget> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("upload_log_id", oldUploadLogId);
            productLineBudgetMapper.delete(deleteWrapper);
            //2 更新上传日志记录状态
            BudgetUploadLog oldUploadBudgetUploadLog = budgetUploadLogService.get(oldUploadLogId);
            oldUploadBudgetUploadLog.setStatus(UploadLogStatusConstants.NO);
            budgetUploadLogService.update(oldUploadBudgetUploadLog);
        }

        //3 保存上传日志
        BudgetUploadLog budgetUploadLog = new BudgetUploadLog();
        budgetUploadLog.setExcelName(originalFilename);
        budgetUploadLog.setType(BudgetTypeConstants.PRODUCT_LINE_BUDGET);
        budgetUploadLog.setUploadTime(LocalDateTime.now());
        budgetUploadLog.setUploadUser(this.getCurrentUsername());
        budgetUploadLog.setStatus(UploadLogStatusConstants.YES);
        budgetUploadLog.setExcelImage(excelImageData);
        budgetUploadLogService.add(budgetUploadLog);

        excelImageInputStream.close();

        String firstYear = titleArray[9].substring(0, 4);
        String secondYear = titleArray[159].substring(0, 4);

        //4 保存数据
        for (int i = 3; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if (dataArray == null || dataArray.length == 0) {
                break;
            }
            String businessDivision = dataArray[0]; //事业部
            String productLine = dataArray[1]; //产品线
            String dataVersion = dataArray[2]; //数据版本
            String itemSeq = dataArray[3]; //科目序号
            String costItem = dataArray[4]; //费用项目
            String unit = dataArray[5]; //单位

            for (int j = 1; j <= 2; j++) {
                ProductLineBudget productLineBudget = new ProductLineBudget();
                if (j == 1) {
                    productLineBudget.setYear(Integer.valueOf(firstYear));
                } else if (j == 2) {
                    productLineBudget.setYear(Integer.valueOf(secondYear));
                }


                String month01PhoneValue = dataArray[5 + 1 * j]; //手机类
                String month01TvValue = dataArray[5 + 2 * j]; //TV
                String month01WatchValue = dataArray[5 + 3 * j]; //手表
                String month01ArVrValue = dataArray[5 + 4 * j]; //AR/VR
                String month01LaptopValue = dataArray[5 + 5 * j]; //笔电
                String month01TabletValue = dataArray[5 + 6 * j]; //平板
                String month01VehicleValue = dataArray[5 + 7 * j]; //车载
                String month01IotOtherValue = dataArray[5 + 8 * j]; //IoT&Others
                String month01TotalValue = dataArray[5 + 9 * j]; //小计


                String month02PhoneValue = dataArray[5 + 10 * j]; //手机类
                String month02TvValue = dataArray[5 + 11 * j]; //TV
                String month02WatchValue = dataArray[5 + 12 * j]; //手表
                String month02ArVrValue = dataArray[5 + 13 * j]; //AR/VR
                String month02LaptopValue = dataArray[5 + 14 * j]; //笔电
                String month02TabletValue = dataArray[5 + 15 * j]; //平板
                String month02VehicleValue = dataArray[5 + 16 * j]; //车载
                String month02IotOtherValue = dataArray[5 + 17 * j]; //IoT&Others
                String month02TotalValue = dataArray[5 + 18 * j]; //小计


                String month03PhoneValue = dataArray[5 + 19 * j]; //手机类
                String month03TvValue = dataArray[5 + 20 * j]; //TV
                String month03WatchValue = dataArray[5 + 21 * j]; //手表
                String month03ArVrValue = dataArray[5 + 22 * j]; //AR/VR
                String month03LaptopValue = dataArray[5 + 23 * j]; //笔电
                String month03TabletValue = dataArray[5 + 24 * j]; //平板
                String month03VehicleValue = dataArray[5 + 25 * j]; //车载
                String month03IotOtherValue = dataArray[5 + 26 * j]; //IoT&Others
                String month03TotalValue = dataArray[5 + 27 * j]; //小计

                String q1PhoneValue = dataArray[5 + 28 * j]; //手机类
                String q1TvValue = dataArray[5 + 29 * j]; //TV
                String q1WatchValue = dataArray[5 + 30 * j]; //手表
                String q1ArVrValue = dataArray[5 + 31 * j]; //AR/VR
                String q1LaptopValue = dataArray[5 + 32 * j]; //笔电
                String q1TabletValue = dataArray[5 + 33 * j]; //平板
                String q1VehicleValue = dataArray[5 + 34 * j]; //车载
                String q1IotOtherValue = dataArray[5 + 35 * j]; //IoT&Others
                String q1TotalValue = dataArray[5 + 36 * j]; //小计

                String month04PhoneValue = dataArray[5 + 37 * j]; //手机类
                String month04TvValue = dataArray[5 + 38 * j]; //TV
                String month04WatchValue = dataArray[5 + 39 * j]; //手表
                String month04ArVrValue = dataArray[5 + 40 * j]; //AR/VR
                String month04LaptopValue = dataArray[5 + 41 * j]; //笔电
                String month04TabletValue = dataArray[5 + 42 * j]; //平板
                String month04VehicleValue = dataArray[5 + 43 * j]; //车载
                String month04IotOtherValue = dataArray[5 + 44 * j]; //IoT&Others
                String month04TotalValue = dataArray[5 + 45 * j]; //小计

                String month05PhoneValue = dataArray[5 + 46 * j]; //手机类
                String month05TvValue = dataArray[5 + 47 * j]; //TV
                String month05WatchValue = dataArray[5 + 48 * j]; //手表
                String month05ArVrValue = dataArray[5 + 49 * j]; //AR/VR
                String month05LaptopValue = dataArray[5 + 50 * j]; //笔电
                String month05TabletValue = dataArray[5 + 51 * j]; //平板
                String month05VehicleValue = dataArray[5 + 52 * j]; //车载
                String month05IotOtherValue = dataArray[5 + 53 * j]; //IoT&Others
                String month05TotalValue = dataArray[5 + 54 * j]; //小计


                String month06PhoneValue = dataArray[5 + 55 * j]; //手机类
                String month06TvValue = dataArray[5 + 56 * j]; //TV
                String month06WatchValue = dataArray[5 + 57 * j]; //手表
                String month06ArVrValue = dataArray[5 + 58 * j]; //AR/VR
                String month06LaptopValue = dataArray[5 + 59 * j]; //笔电
                String month06TabletValue = dataArray[5 + 60 * j]; //平板
                String month06VehicleValue = dataArray[5 + 61 * j]; //车载
                String month06IotOtherValue = dataArray[5 + 62 * j]; //IoT&Others
                String month06TotalValue = dataArray[5 + 63 * j]; //小计

                String q2PhoneValue = dataArray[5 + 64 * j]; //手机类
                String q2TvValue = dataArray[5 + 65 * j]; //TV
                String q2WatchValue = dataArray[5 + 66 * j]; //手表
                String q2ArVrValue = dataArray[5 + 67 * j]; //AR/VR
                String q2LaptopValue = dataArray[5 + 68 * j]; //笔电
                String q2TabletValue = dataArray[5 + 69 * j]; //平板
                String q2VehicleValue = dataArray[5 + 70 * j]; //车载
                String q2IotOtherValue = dataArray[5 + 71 * j]; //IoT&Others
                String q2TotalValue = dataArray[5 + 72 * j]; //小计

                String month07PhoneValue = dataArray[5 + 73 * j]; //手机类
                String month07TvValue = dataArray[5 + 74 * j]; //TV
                String month07WatchValue = dataArray[5 + 75 * j]; //手表
                String month07ArVrValue = dataArray[5 + 76 * j]; //AR/VR
                String month07LaptopValue = dataArray[5 + 77 * j]; //笔电
                String month07TabletValue = dataArray[5 + 78 * j]; //平板
                String month07VehicleValue = dataArray[5 + 79 * j]; //车载
                String month07IotOtherValue = dataArray[5 + 80 * j]; //IoT&Others
                String month07TotalValue = dataArray[5 + 81 * j]; //小计

                String month08PhoneValue = dataArray[5 + 82 * j]; //手机类
                String month08TvValue = dataArray[5 + 83 * j]; //TV
                String month08WatchValue = dataArray[5 + 84 * j]; //手表
                String month08ArVrValue = dataArray[5 + 85 * j]; //AR/VR
                String month08LaptopValue = dataArray[5 + 86 * j]; //笔电
                String month08TabletValue = dataArray[5 + 87 * j]; //平板
                String month08VehicleValue = dataArray[5 + 88 * j]; //车载
                String month08IotOtherValue = dataArray[5 + 89 * j]; //IoT&Others
                String month08TotalValue = dataArray[5 + 90 * j]; //小计

                String month09PhoneValue = dataArray[5 + 91 * j]; //手机类
                String month09TvValue = dataArray[5 + 92 * j]; //TV
                String month09WatchValue = dataArray[5 + 93 * j]; //手表
                String month09ArVrValue = dataArray[5 + 94 * j]; //AR/VR
                String month09LaptopValue = dataArray[5 + 95 * j]; //笔电
                String month09TabletValue = dataArray[5 + 96 * j]; //平板
                String month09VehicleValue = dataArray[5 + 97 * j]; //车载
                String month09IotOtherValue = dataArray[5 + 98 * j]; //IoT&Others
                String month09TotalValue = dataArray[5 + 99 * j]; //小计

                String q3PhoneValue = dataArray[5 + 100 * j]; //手机类
                String q3TvValue = dataArray[5 + 101 * j]; //TV
                String q3WatchValue = dataArray[5 + 102 * j]; //手表
                String q3ArVrValue = dataArray[5 + 103 * j]; //AR/VR
                String q3LaptopValue = dataArray[5 + 104 * j]; //笔电
                String q3TabletValue = dataArray[5 + 105 * j]; //平板
                String q3VehicleValue = dataArray[5 + 106 * j]; //车载
                String q3IotOtherValue = dataArray[5 + 107 * j]; //IoT&Others
                String q3TotalValue = dataArray[5 + 108 * j]; //小计

                String month10PhoneValue = dataArray[5 + 109 * j]; //手机类
                String month10TvValue = dataArray[5 + 110 * j]; //TV
                String month10WatchValue = dataArray[5 + 111 * j]; //手表
                String month10ArVrValue = dataArray[5 + 112 * j]; //AR/VR
                String month10LaptopValue = dataArray[5 + 113 * j]; //笔电
                String month10TabletValue = dataArray[5 + 114 * j]; //平板
                String month10VehicleValue = dataArray[5 + 115 * j]; //车载
                String month10IotOtherValue = dataArray[5 + 116 * j]; //IoT&Others
                String month10TotalValue = dataArray[5 + 117 * j]; //小计

                String month11PhoneValue = dataArray[5 + 118 * j]; //手机类
                String month11TvValue = dataArray[5 + 119 * j]; //TV
                String month11WatchValue = dataArray[5 + 120 * j]; //手表
                String month11ArVrValue = dataArray[5 + 121 * j]; //AR/VR
                String month11LaptopValue = dataArray[5 + 122 * j]; //笔电
                String month11TabletValue = dataArray[5 + 123 * j]; //平板
                String month11VehicleValue = dataArray[5 + 124 * j]; //车载
                String month11IotOtherValue = dataArray[5 + 125 * j]; //IoT&Others
                String month11TotalValue = dataArray[5 + 126 * j]; //小计

                String month12PhoneValue = dataArray[5 + 127 * j]; //手机类
                String month12TvValue = dataArray[5 + 128 * j]; //TV
                String month12WatchValue = dataArray[5 + 129 * j]; //手表
                String month12ArVrValue = dataArray[5 + 130 * j]; //AR/VR
                String month12LaptopValue = dataArray[5 + 131 * j]; //笔电
                String month12TabletValue = dataArray[5 + 132 * j]; //平板
                String month12VehicleValue = dataArray[5 + 133 * j]; //车载
                String month12IotOtherValue = dataArray[5 + 134 * j]; //IoT&Others
                String month12TotalValue = dataArray[5 + 135 * j]; //小计

                String q4PhoneValue = dataArray[5 + 136 * j]; //手机类
                String q4TvValue = dataArray[5 + 137 * j]; //TV
                String q4WatchValue = dataArray[5 + 138 * j]; //手表
                String q4ArVrValue = dataArray[5 + 139 * j]; //AR/VR
                String q4LaptopValue = dataArray[5 + 140 * j]; //笔电
                String q4TabletValue = dataArray[5 + 141 * j]; //平板
                String q4VehicleValue = dataArray[5 + 142 * j]; //车载
                String q4IotOtherValue = dataArray[5 + 143 * j]; //IoT&Others
                String q4TotalValue = dataArray[5 + 144 * j]; //小计

                String yearPhoneValue = dataArray[5 + 145 * j]; //手机类
                String yearTvValue = dataArray[5 + 146 * j]; //TV
                String yearWatchValue = dataArray[5 + 147 * j]; //手表
                String yearArVrValue = dataArray[5 + 148 * j]; //AR/VR
                String yearLaptopValue = dataArray[5 + 149 * j]; //笔电
                String yearTabletValue = dataArray[5 + 150 * j]; //平板
                String yearVehicleValue = dataArray[5 + 151 * j]; //车载
                String yearIotOtherValue = dataArray[5 + 152 * j]; //IoT&Others
                String yearTotalValue = dataArray[5 + 153 * j]; //小计

                productLineBudget.setBusinessDivision(businessDivision);
                productLineBudget.setProductLine(productLine);
                productLineBudget.setDataVersion(dataVersion);
                productLineBudget.setItemSeq(Integer.valueOf(itemSeq));
                productLineBudget.setCostItem(costItem);
                productLineBudget.setUnit(unit);

                productLineBudget.setMonth01PhoneValue(new BigDecimal(month01PhoneValue));
                productLineBudget.setMonth01TvValue(new BigDecimal(month01TvValue));
                productLineBudget.setMonth01WatchValue(new BigDecimal(month01WatchValue));
                productLineBudget.setMonth01ArVrValue(new BigDecimal(month01ArVrValue));
                productLineBudget.setMonth01LaptopValue(new BigDecimal(month01LaptopValue));
                productLineBudget.setMonth01TabletValue(new BigDecimal(month01TabletValue));
                productLineBudget.setMonth01VehicleValue(new BigDecimal(month01VehicleValue));
                productLineBudget.setMonth01IotOtherValue(new BigDecimal(month01IotOtherValue));
                productLineBudget.setMonth01TotalValue(new BigDecimal(month01TotalValue));

                productLineBudget.setMonth02PhoneValue(    new BigDecimal(month02PhoneValue));
                productLineBudget.setMonth02TvValue(       new BigDecimal(month02TvValue));
                productLineBudget.setMonth02WatchValue(    new BigDecimal(month02WatchValue));
                productLineBudget.setMonth02ArVrValue(     new BigDecimal(month02ArVrValue));
                productLineBudget.setMonth02LaptopValue(   new BigDecimal(month02LaptopValue));
                productLineBudget.setMonth02TabletValue(   new BigDecimal(month02TabletValue));
                productLineBudget.setMonth02VehicleValue(  new BigDecimal(month02VehicleValue));
                productLineBudget.setMonth02IotOtherValue( new BigDecimal(month02IotOtherValue));
                productLineBudget.setMonth02TotalValue(    new BigDecimal(month02TotalValue));

                productLineBudget.setMonth03PhoneValue(    new BigDecimal(month03PhoneValue));
                productLineBudget.setMonth03TvValue(       new BigDecimal(month03TvValue));
                productLineBudget.setMonth03WatchValue(    new BigDecimal(month03WatchValue));
                productLineBudget.setMonth03ArVrValue(     new BigDecimal(month03ArVrValue));
                productLineBudget.setMonth03LaptopValue(   new BigDecimal(month03LaptopValue));
                productLineBudget.setMonth03TabletValue(   new BigDecimal(month03TabletValue));
                productLineBudget.setMonth03VehicleValue(  new BigDecimal(month03VehicleValue));
                productLineBudget.setMonth03IotOtherValue( new BigDecimal(month03IotOtherValue));
                productLineBudget.setMonth03TotalValue(    new BigDecimal(month03TotalValue));

                productLineBudget.setQ1PhoneValue(    new BigDecimal(q1PhoneValue));
                productLineBudget.setQ1TvValue(       new BigDecimal(q1TvValue));
                productLineBudget.setQ1WatchValue(    new BigDecimal(q1WatchValue));
                productLineBudget.setQ1ArVrValue(     new BigDecimal(q1ArVrValue));
                productLineBudget.setQ1LaptopValue(   new BigDecimal(q1LaptopValue));
                productLineBudget.setQ1TabletValue(   new BigDecimal(q1TabletValue));
                productLineBudget.setQ1VehicleValue(  new BigDecimal(q1VehicleValue));
                productLineBudget.setQ1IotOtherValue( new BigDecimal(q1IotOtherValue));
                productLineBudget.setQ1TotalValue(    new BigDecimal(q1TotalValue));

                productLineBudget.setMonth04PhoneValue(    new BigDecimal(month04PhoneValue));
                productLineBudget.setMonth04TvValue(       new BigDecimal(month04TvValue));
                productLineBudget.setMonth04WatchValue(    new BigDecimal(month04WatchValue));
                productLineBudget.setMonth04ArVrValue(     new BigDecimal(month04ArVrValue));
                productLineBudget.setMonth04LaptopValue(   new BigDecimal(month04LaptopValue));
                productLineBudget.setMonth04TabletValue(   new BigDecimal(month04TabletValue));
                productLineBudget.setMonth04VehicleValue(  new BigDecimal(month04VehicleValue));
                productLineBudget.setMonth04IotOtherValue( new BigDecimal(month04IotOtherValue));
                productLineBudget.setMonth04TotalValue(    new BigDecimal(month04TotalValue));

                productLineBudget.setMonth05PhoneValue(    new BigDecimal(month05PhoneValue));
                productLineBudget.setMonth05TvValue(       new BigDecimal(month05TvValue));
                productLineBudget.setMonth05WatchValue(    new BigDecimal(month05WatchValue));
                productLineBudget.setMonth05ArVrValue(     new BigDecimal(month05ArVrValue));
                productLineBudget.setMonth05LaptopValue(   new BigDecimal(month05LaptopValue));
                productLineBudget.setMonth05TabletValue(   new BigDecimal(month05TabletValue));
                productLineBudget.setMonth05VehicleValue(  new BigDecimal(month05VehicleValue));
                productLineBudget.setMonth05IotOtherValue( new BigDecimal(month05IotOtherValue));
                productLineBudget.setMonth05TotalValue(    new BigDecimal(month05TotalValue));

                productLineBudget.setMonth06PhoneValue(    new BigDecimal(month06PhoneValue));
                productLineBudget.setMonth06TvValue(       new BigDecimal(month06TvValue));
                productLineBudget.setMonth06WatchValue(    new BigDecimal(month06WatchValue));
                productLineBudget.setMonth06ArVrValue(     new BigDecimal(month06ArVrValue));
                productLineBudget.setMonth06LaptopValue(   new BigDecimal(month06LaptopValue));
                productLineBudget.setMonth06TabletValue(   new BigDecimal(month06TabletValue));
                productLineBudget.setMonth06VehicleValue(  new BigDecimal(month06VehicleValue));
                productLineBudget.setMonth06IotOtherValue( new BigDecimal(month06IotOtherValue));
                productLineBudget.setMonth06TotalValue(    new BigDecimal(month06TotalValue));


                productLineBudget.setQ2PhoneValue(    new BigDecimal(q2PhoneValue));
                productLineBudget.setQ2TvValue(       new BigDecimal(q2TvValue));
                productLineBudget.setQ2WatchValue(    new BigDecimal(q2WatchValue));
                productLineBudget.setQ2ArVrValue(     new BigDecimal(q2ArVrValue));
                productLineBudget.setQ2LaptopValue(   new BigDecimal(q2LaptopValue));
                productLineBudget.setQ2TabletValue(   new BigDecimal(q2TabletValue));
                productLineBudget.setQ2VehicleValue(  new BigDecimal(q2VehicleValue));
                productLineBudget.setQ2IotOtherValue( new BigDecimal(q2IotOtherValue));
                productLineBudget.setQ2TotalValue(    new BigDecimal(q2TotalValue));

                productLineBudget.setMonth07PhoneValue(    new BigDecimal(month07PhoneValue));
                productLineBudget.setMonth07TvValue(       new BigDecimal(month07TvValue));
                productLineBudget.setMonth07WatchValue(    new BigDecimal(month07WatchValue));
                productLineBudget.setMonth07ArVrValue(     new BigDecimal(month07ArVrValue));
                productLineBudget.setMonth07LaptopValue(   new BigDecimal(month07LaptopValue));
                productLineBudget.setMonth07TabletValue(   new BigDecimal(month07TabletValue));
                productLineBudget.setMonth07VehicleValue(  new BigDecimal(month07VehicleValue));
                productLineBudget.setMonth07IotOtherValue( new BigDecimal(month07IotOtherValue));
                productLineBudget.setMonth07TotalValue(    new BigDecimal(month07TotalValue));

                productLineBudget.setMonth08PhoneValue(    new BigDecimal(month08PhoneValue));
                productLineBudget.setMonth08TvValue(       new BigDecimal(month08TvValue));
                productLineBudget.setMonth08WatchValue(    new BigDecimal(month08WatchValue));
                productLineBudget.setMonth08ArVrValue(     new BigDecimal(month08ArVrValue));
                productLineBudget.setMonth08LaptopValue(   new BigDecimal(month08LaptopValue));
                productLineBudget.setMonth08TabletValue(   new BigDecimal(month08TabletValue));
                productLineBudget.setMonth08VehicleValue(  new BigDecimal(month08VehicleValue));
                productLineBudget.setMonth08IotOtherValue( new BigDecimal(month08IotOtherValue));
                productLineBudget.setMonth08TotalValue(    new BigDecimal(month08TotalValue));

                productLineBudget.setMonth09PhoneValue(    new BigDecimal(month09PhoneValue));
                productLineBudget.setMonth09TvValue(       new BigDecimal(month09TvValue));
                productLineBudget.setMonth09WatchValue(    new BigDecimal(month09WatchValue));
                productLineBudget.setMonth09ArVrValue(     new BigDecimal(month09ArVrValue));
                productLineBudget.setMonth09LaptopValue(   new BigDecimal(month09LaptopValue));
                productLineBudget.setMonth09TabletValue(   new BigDecimal(month09TabletValue));
                productLineBudget.setMonth09VehicleValue(  new BigDecimal(month09VehicleValue));
                productLineBudget.setMonth09IotOtherValue( new BigDecimal(month09IotOtherValue));
                productLineBudget.setMonth09TotalValue(    new BigDecimal(month09TotalValue));

                productLineBudget.setQ3PhoneValue(    new BigDecimal(q3PhoneValue));
                productLineBudget.setQ3TvValue(       new BigDecimal(q3TvValue));
                productLineBudget.setQ3WatchValue(    new BigDecimal(q3WatchValue));
                productLineBudget.setQ3ArVrValue(     new BigDecimal(q3ArVrValue));
                productLineBudget.setQ3LaptopValue(   new BigDecimal(q3LaptopValue));
                productLineBudget.setQ3TabletValue(   new BigDecimal(q3TabletValue));
                productLineBudget.setQ3VehicleValue(  new BigDecimal(q3VehicleValue));
                productLineBudget.setQ3IotOtherValue( new BigDecimal(q3IotOtherValue));
                productLineBudget.setQ3TotalValue(    new BigDecimal(q3TotalValue));

                productLineBudget.setMonth10PhoneValue(    new BigDecimal(month10PhoneValue));
                productLineBudget.setMonth10TvValue(       new BigDecimal(month10TvValue));
                productLineBudget.setMonth10WatchValue(    new BigDecimal(month10WatchValue));
                productLineBudget.setMonth10ArVrValue(     new BigDecimal(month10ArVrValue));
                productLineBudget.setMonth10LaptopValue(   new BigDecimal(month10LaptopValue));
                productLineBudget.setMonth10TabletValue(   new BigDecimal(month10TabletValue));
                productLineBudget.setMonth10VehicleValue(  new BigDecimal(month10VehicleValue));
                productLineBudget.setMonth10IotOtherValue( new BigDecimal(month10IotOtherValue));
                productLineBudget.setMonth10TotalValue(    new BigDecimal(month10TotalValue));

                productLineBudget.setMonth11PhoneValue(    new BigDecimal(month11PhoneValue));
                productLineBudget.setMonth11TvValue(       new BigDecimal(month11TvValue));
                productLineBudget.setMonth11WatchValue(    new BigDecimal(month11WatchValue));
                productLineBudget.setMonth11ArVrValue(     new BigDecimal(month11ArVrValue));
                productLineBudget.setMonth11LaptopValue(   new BigDecimal(month11LaptopValue));
                productLineBudget.setMonth11TabletValue(   new BigDecimal(month11TabletValue));
                productLineBudget.setMonth11VehicleValue(  new BigDecimal(month11VehicleValue));
                productLineBudget.setMonth11IotOtherValue( new BigDecimal(month11IotOtherValue));
                productLineBudget.setMonth11TotalValue(    new BigDecimal(month11TotalValue));

                productLineBudget.setMonth12PhoneValue(    new BigDecimal(month12PhoneValue));
                productLineBudget.setMonth12TvValue(       new BigDecimal(month12TvValue));
                productLineBudget.setMonth12WatchValue(    new BigDecimal(month12WatchValue));
                productLineBudget.setMonth12ArVrValue(     new BigDecimal(month12ArVrValue));
                productLineBudget.setMonth12LaptopValue(   new BigDecimal(month12LaptopValue));
                productLineBudget.setMonth12TabletValue(   new BigDecimal(month12TabletValue));
                productLineBudget.setMonth12VehicleValue(  new BigDecimal(month12VehicleValue));
                productLineBudget.setMonth12IotOtherValue( new BigDecimal(month12IotOtherValue));
                productLineBudget.setMonth12TotalValue(    new BigDecimal(month12TotalValue));


                productLineBudget.setQ4PhoneValue(    new BigDecimal(q4PhoneValue));
                productLineBudget.setQ4TvValue(       new BigDecimal(q4TvValue));
                productLineBudget.setQ4WatchValue(    new BigDecimal(q4WatchValue));
                productLineBudget.setQ4ArVrValue(     new BigDecimal(q4ArVrValue));
                productLineBudget.setQ4LaptopValue(   new BigDecimal(q4LaptopValue));
                productLineBudget.setQ4TabletValue(   new BigDecimal(q4TabletValue));
                productLineBudget.setQ4VehicleValue(  new BigDecimal(q4VehicleValue));
                productLineBudget.setQ4IotOtherValue( new BigDecimal(q4IotOtherValue));
                productLineBudget.setQ4TotalValue(    new BigDecimal(q4TotalValue));

                productLineBudget.setYearPhoneValue(    new BigDecimal(yearPhoneValue));
                productLineBudget.setYearTvValue(       new BigDecimal(yearTvValue));
                productLineBudget.setYearWatchValue(    new BigDecimal(yearWatchValue));
                productLineBudget.setYearArVrValue(     new BigDecimal(yearArVrValue));
                productLineBudget.setYearLaptopValue(   new BigDecimal(yearLaptopValue));
                productLineBudget.setYearTabletValue(   new BigDecimal(yearTabletValue));
                productLineBudget.setYearVehicleValue(  new BigDecimal(yearVehicleValue));
                productLineBudget.setYearIotOtherValue( new BigDecimal(yearIotOtherValue));
                productLineBudget.setYearTotalValue(    new BigDecimal(yearTotalValue));


                productLineBudget.setUploadLogId(budgetUploadLog.getId());
                this.add(productLineBudget);
            }
        }

    }

}
