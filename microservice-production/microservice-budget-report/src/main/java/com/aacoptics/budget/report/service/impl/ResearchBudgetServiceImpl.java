package com.aacoptics.budget.report.service.impl;


import com.aacoptics.budget.report.constants.BudgetTypeConstants;
import com.aacoptics.budget.report.constants.UploadLogStatusConstants;
import com.aacoptics.budget.report.entity.param.ResearchBudgetQueryParam;
import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.aacoptics.budget.report.entity.po.ProductLinePermission;
import com.aacoptics.budget.report.entity.po.ResearchBudget;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.mapper.ResearchBudgetMapper;
import com.aacoptics.budget.report.service.BudgetUploadLogService;
import com.aacoptics.budget.report.service.ProductLinePermissionService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

    @Resource
    private ProductLinePermissionService productLinePermissionService;


    private String getCurrentUsername() {
        return StringUtils.defaultIfBlank(UserContextHolder.getInstance().getUsername(), "IoT");
    }


    @Override
    public Map<String, Object> query(Page page, ResearchBudgetQueryParam researchBudgetQueryParam) {
        Long uploadLogId = researchBudgetQueryParam.getUploadLogId();
        if(uploadLogId == null)
        {
            return this.findByCondition(researchBudgetQueryParam.getBusinessDivision(), researchBudgetQueryParam.getProductLineList());
        }
       return this.findByUploadLogId(uploadLogId);

    }


    @Override
    public Map<String, Object> findByCondition(String businessDivision, List<String> productLineList) {
        //????????????????????????
        String username = this.getCurrentUsername();

        List<ProductLinePermission> productLinePermissionList =  productLinePermissionService.getByUserCode(username);
        boolean verificationPermission = false;
        if(productLinePermissionList.size() > 0)
        {
            verificationPermission = true;
        }

        //???????????????????????????
        List<Integer>  yearList = researchBudgetMapper.findResearchBudgetAllYearByCondition(businessDivision,
                productLineList);
        if(yearList == null || yearList.size() == 0)
        {
            throw new BusinessException("?????????????????????????????????????????????");
        }
        //????????????
        JSONArray titleJsonArray = this.createReportTableTitle(yearList);

        //???????????????
        String selectColumn = this.createReportSelectColumn(yearList);

        List<Map<String, Object>> researchBudgetList = researchBudgetMapper.findResearchBudgetByCondition(
                selectColumn, businessDivision, productLineList,
                yearList.get(0), yearList.get(1), verificationPermission, username);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("columns", titleJsonArray);
        resultMap.put("data", researchBudgetList);

        return resultMap;
    }

    @Override
    public Map<String, Object> findByUploadLogId(Long uploadLogId) {
        if(uploadLogId == null)
        {
            throw new BusinessException("????????????ID????????????");
        }
        //???????????????????????????
        List<Integer>  yearList = researchBudgetMapper.findResearchBudgetAllYearByUploadLogId(uploadLogId);
        if(yearList == null || yearList.size() == 0)
        {
            throw new BusinessException("???????????????");
        }
        //????????????
        JSONArray titleJsonArray = this.createTableTitle(yearList);

        //???????????????
        String selectColumn = this.createSelectColumn(yearList);

        List<Map<String, Object>> researchBudgetList = researchBudgetMapper.findResearchBudgetByUploadLogId(
                selectColumn, uploadLogId, yearList.get(0), yearList.get(1));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("columns", titleJsonArray);
        resultMap.put("data", researchBudgetList);

        return resultMap;
    }


    /**
     * ???????????????
     * @param yearList
     * @return
     */
    private String createSelectColumn(List<Integer> yearList)
    {
        StringBuffer selectColumn = new StringBuffer(); // ??????temp_year_01.month_01_value month_01_value

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
            String columnName = year + "??????";
            selectColumn.append(", temp_year_" + (i+1) + ".year_value as '" + columnName + "'");
        }

        return selectColumn.toString();
    }


    /**
     * ???????????????????????????
     * @param yearList
     * @return
     */
    private String createReportSelectColumn(List<Integer> yearList)
    {
        StringBuffer selectColumn = new StringBuffer(); // ??????temp_year_01.month_01_value month_01_value

        for(int i=0; i<yearList.size(); i++)
        {
            Integer year = yearList.get(i);
            for(int j=1; j<=12; j++)
            {
                String monthStr = String.format("%02d", j);
                String columnName = year + monthStr;
                selectColumn.append(", sum(temp_year_" + (i+1) + ".month_"+ monthStr +"_value) as '" + columnName + "'");
            }
            for(int k=1; k<=4; k++)
            {
                String columnName = year + "Q" + k;
                selectColumn.append(", sum(temp_year_" + (i+1) + ".q"+ k +"_value) as '" + columnName + "'");
            }
            String columnName = year + "??????";
            selectColumn.append(", sum(temp_year_" + (i+1) + ".year_value) as '" + columnName + "'");
        }

        return selectColumn.toString();
    }


    /**
     * ???????????????????????????
     *
     * @param yearList
     * @return
     */
    private JSONArray createTableTitle(List<Integer> yearList)
    {
        JSONArray titleJsonArray = new JSONArray();
        JSONObject businessDivisionColumnJsonObject = new JSONObject();
        businessDivisionColumnJsonObject.put("prop", "businessDivision");
        businessDivisionColumnJsonObject.put("label", "?????????");
        businessDivisionColumnJsonObject.put("minWidth", "150");
        businessDivisionColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(businessDivisionColumnJsonObject);

        JSONObject productLineColumnJsonObject = new JSONObject();
        productLineColumnJsonObject.put("prop", "productLine");
        productLineColumnJsonObject.put("label", "?????????");
        productLineColumnJsonObject.put("minWidth", "120");
        productLineColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(productLineColumnJsonObject);

        JSONObject dataVersionColumnJsonObject = new JSONObject();
        dataVersionColumnJsonObject.put("prop", "dataVersion");
        dataVersionColumnJsonObject.put("label", "????????????");
        dataVersionColumnJsonObject.put("minWidth", "150");
        dataVersionColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(dataVersionColumnJsonObject);

        JSONObject itemSeqColumnJsonObject = new JSONObject();
        itemSeqColumnJsonObject.put("prop", "itemSeq");
        itemSeqColumnJsonObject.put("label", "????????????");
        itemSeqColumnJsonObject.put("minWidth", "120");
        itemSeqColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(itemSeqColumnJsonObject);

        JSONObject rowNoColumnJsonObject = new JSONObject();
        rowNoColumnJsonObject.put("prop", "rowNo");
        rowNoColumnJsonObject.put("label", "????????????");
        rowNoColumnJsonObject.put("minWidth", "120");
        rowNoColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(rowNoColumnJsonObject);

        JSONObject costItemColumnJsonObject = new JSONObject();
        costItemColumnJsonObject.put("prop", "costItem");
        costItemColumnJsonObject.put("label", "????????????");
        costItemColumnJsonObject.put("minWidth", "260");
        costItemColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(costItemColumnJsonObject);

        JSONObject costTypeColumnJsonObject = new JSONObject();
        costTypeColumnJsonObject.put("prop", "costType");
        costTypeColumnJsonObject.put("label", "??????/????????????");
        costTypeColumnJsonObject.put("minWidth", "150");
//        costTypeColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(costTypeColumnJsonObject);

        JSONObject unitColumnJsonObject = new JSONObject();
        unitColumnJsonObject.put("prop", "unit");
        unitColumnJsonObject.put("label", "??????");
        unitColumnJsonObject.put("minWidth", "120");
//        unitColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(unitColumnJsonObject);

        this.addYearDataTitle(titleJsonArray, yearList);

        return titleJsonArray;
    }



    /**
     * ???????????????????????????????????????
     *
     * @param yearList
     * @return
     */
    private JSONArray createReportTableTitle(List<Integer> yearList)
    {
        JSONArray titleJsonArray = new JSONArray();

        JSONObject itemSeqColumnJsonObject = new JSONObject();
        itemSeqColumnJsonObject.put("prop", "itemSeq");
        itemSeqColumnJsonObject.put("label", "????????????");
        itemSeqColumnJsonObject.put("minWidth", "120");
        itemSeqColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(itemSeqColumnJsonObject);

        JSONObject rowNoColumnJsonObject = new JSONObject();
        rowNoColumnJsonObject.put("prop", "rowNo");
        rowNoColumnJsonObject.put("label", "????????????");
        rowNoColumnJsonObject.put("minWidth", "120");
        rowNoColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(rowNoColumnJsonObject);

        JSONObject costItemColumnJsonObject = new JSONObject();
        costItemColumnJsonObject.put("prop", "costItem");
        costItemColumnJsonObject.put("label", "????????????");
        costItemColumnJsonObject.put("minWidth", "260");
        costItemColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(costItemColumnJsonObject);

        JSONObject costTypeColumnJsonObject = new JSONObject();
        costTypeColumnJsonObject.put("prop", "costType");
        costTypeColumnJsonObject.put("label", "??????/????????????");
        costTypeColumnJsonObject.put("minWidth", "150");
        costTypeColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(costTypeColumnJsonObject);

        JSONObject unitColumnJsonObject = new JSONObject();
        unitColumnJsonObject.put("prop", "unit");
        unitColumnJsonObject.put("label", "??????");
        unitColumnJsonObject.put("minWidth", "120");
        unitColumnJsonObject.put("fixed", "left");
        titleJsonArray.add(unitColumnJsonObject);

        this.addYearDataTitle(titleJsonArray, yearList);

        return titleJsonArray;
    }

    private JSONArray addYearDataTitle(JSONArray titleJsonArray, List<Integer> yearList)
    {
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

            String columnName = year + "??????";
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
            throw new BusinessException("ID???" + id + "?????????????????????");
        }

        return researchBudget;
    }



    @Transactional
    @Override
    public void importExcel(String originalFilename, MultipartFile file, InputStream in) throws Exception {
        List<List<String[]>> sheetList = ExcelUtil.read(in);

        List<String[]> excelDataList = sheetList.get(1);


        String[] titleArray = excelDataList.get(1);//?????????

        String businessDivisionTitle = titleArray[0];
        String productLineTitle = titleArray[1];
        String dataVersionTitle = titleArray[2];
        if ((!"?????????".equals(businessDivisionTitle)) || (!"?????????".equals(productLineTitle))|| (!"????????????".equals(dataVersionTitle))) {
            throw new BusinessException("Excel????????????????????????!");
        }
        InputStream excelImageInputStream = file.getInputStream();

        byte[] excelImageData = new byte[(int) file.getSize()];
        excelImageInputStream.read(excelImageData);



        //????????????????????????????????????????????????
        String[] dataRow = excelDataList.get(2);
        String tempBusinessDivision = dataRow[0]; //?????????
        String tempProductLine = dataRow[1]; //?????????

        ResearchBudget oldResearchBudget = researchBudgetMapper.findByBusinessDivisionAndProductLine(tempBusinessDivision, tempProductLine);
        if(oldResearchBudget != null) {
            //1 ????????????????????????????????????????????????
            Long oldUploadLogId = oldResearchBudget.getUploadLogId();

            QueryWrapper<ResearchBudget> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("upload_log_id", oldUploadLogId);
            researchBudgetMapper.delete(deleteWrapper);
            //2 ??????????????????????????????
            BudgetUploadLog oldUploadBudgetUploadLog = budgetUploadLogService.get(oldUploadLogId);
            oldUploadBudgetUploadLog.setStatus(UploadLogStatusConstants.NO);
            budgetUploadLogService.update(oldUploadBudgetUploadLog);
        }

        //3 ??????????????????
        BudgetUploadLog budgetUploadLog = new BudgetUploadLog();
        budgetUploadLog.setBusinessDivision(tempBusinessDivision);
        budgetUploadLog.setProductLine(tempProductLine);
        budgetUploadLog.setExcelName(originalFilename);
        budgetUploadLog.setType(BudgetTypeConstants.RESEARCH_BUDGET);
        budgetUploadLog.setUploadTime(LocalDateTime.now());
        budgetUploadLog.setUploadUser(this.getCurrentUsername());
        budgetUploadLog.setStatus(UploadLogStatusConstants.YES);
        budgetUploadLog.setExcelImage(excelImageData);
        budgetUploadLogService.add(budgetUploadLog);

        excelImageInputStream.close();

        String firstYear = titleArray[9].substring(0, 4);
        String secondYear = titleArray[25].substring(0, 4);

        //4 ????????????
        for (int i = 2; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if (dataArray == null || dataArray.length == 0) {
                break;
            }
            String businessDivision = dataArray[0]; //?????????
            String productLine = dataArray[1]; //?????????
            String dataVersion = dataArray[2]; //????????????
            String itemSeq = dataArray[3]; //????????????
            String rowNo = dataArray[4]; //????????????
            String costItem = dataArray[5]; //????????????
            String costType = dataArray[6]; //"??????/????????????"
            String unit = dataArray[7]; //??????

            for(int j=1; j<=2; j++)
            {
                ResearchBudget researchBudget = new ResearchBudget();
                if(j == 1) {
                    researchBudget.setYear(Integer.valueOf(firstYear));
                }else if(j ==2)
                {
                    researchBudget.setYear(Integer.valueOf(secondYear));
                }

                String month01Value = dataArray[7 + 1 * j]; //????????????
                String month02Value = dataArray[7 + 2 * j]; //????????????
                String month03Value = dataArray[7 + 3 * j]; //????????????
                String month04Value = dataArray[7 + 4 * j]; //????????????
                String month05Value = dataArray[7 + 5 * j]; //????????????
                String month06Value = dataArray[7 + 6 * j]; //????????????
                String month07Value = dataArray[7 + 7 * j]; //????????????
                String month08Value = dataArray[7 + 8 * j]; //????????????
                String month09Value = dataArray[7 + 9 * j]; //????????????
                String month10Value = dataArray[7 + 10 * j]; //????????????
                String month11Value = dataArray[7 + 11 * j]; //????????????
                String month12Value = dataArray[7 + 12 * j]; //????????????
                String q1Value = dataArray[7 + 13 * j]; //????????????
                String q2Value = dataArray[7 + 14 * j]; //????????????
                String q3Value = dataArray[7 + 15 * j]; //????????????
                String q4Value = dataArray[7 + 16 * j]; //????????????
                String yearValue = dataArray[7 + 17 * j]; //????????????

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
