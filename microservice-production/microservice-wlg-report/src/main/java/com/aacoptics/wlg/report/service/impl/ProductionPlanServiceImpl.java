package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.param.ProductionPlanQueryParam;
import com.aacoptics.wlg.report.entity.po.ProductionPlan;
import com.aacoptics.wlg.report.exception.BusinessException;
import com.aacoptics.wlg.report.mapper.ProductionPlanMapper;
import com.aacoptics.wlg.report.service.ProductionPlanService;
import com.aacoptics.wlg.report.util.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductionPlanServiceImpl extends ServiceImpl<ProductionPlanMapper, ProductionPlan> implements ProductionPlanService {

    @Autowired
    private ProductionPlanMapper productionPlanMapper;

    @Override
    @Transactional
    public void importProductionPlanExcel(String fileName, InputStream in) throws IOException, InvalidFormatException {
        LocalDate currentLocalDate = LocalDate.now();

        String[] fileNameArray = fileName.split("计划");
        String projectName = fileNameArray[0];
        if (projectName.contains("-")) {
            fileNameArray = fileName.split("-");
            projectName = fileNameArray[0];
        }

        List<String[]> excelDataList = ExcelUtil.read(in).get(0);

        String[] titleArray = excelDataList.get(1);//标题行

        String moldTitle = titleArray[1];
        String cycleTitle = titleArray[2];
        if ((!"模具".equals(moldTitle)) || (!"周期".equals(cycleTitle))) {
            throw new BusinessException("Excel模板错误，请确认");
        }

        for (int i = 2; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if(dataArray == null || dataArray.length == 0)
            {
                break;
            }

            String mold = dataArray[1]; //模具
            String cycle = dataArray[2]; //周期
            String name = dataArray[4]; //项目2
            String code = dataArray[5]; //条件代码

            if (StringUtils.isEmpty(mold)) {
                throw new BusinessException("第" + (i + 1) + "行模具不能为空");
            }
            if (StringUtils.isEmpty(cycle)) {
                throw new BusinessException("第" + (i + 1) + "行周期不能为空");
            }
            if (StringUtils.isEmpty(name)) {
                throw new BusinessException("第" + (i + 1) + "行项目2不能为空");
            }
            if (StringUtils.isEmpty(code)) {
                throw new BusinessException("第" + (i + 1) + "行条件代码不能为空");
            }


            for (int j = 7; j < dataArray.length; j++) {
                String planDateStr = titleArray[j];//日期
                String planValueStr = dataArray[j]; //数量
                if (StringUtils.isEmpty(planDateStr)) {
                    break;
                }
                if (StringUtils.isEmpty(planValueStr)) {
                    continue;
                }
                BigDecimal planValue = new BigDecimal(planValueStr);

                LocalDate planDate = null;
                try {
                    planDate = LocalDate.parse(planDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    log.error("日期格式错误", e);
                    throw new BusinessException("日期格式错误" + e.getMessage());
                }

                ProductionPlan productionPlan = null;
                if (currentLocalDate.isAfter(planDate)) {
                    continue;
                }
                else {
                    productionPlan = this.queryProductionPlan(projectName, mold, cycle, code, planDate);
                    if (productionPlan == null) {
                        productionPlan = new ProductionPlan();
                    }
                }


                productionPlan.setMold(mold);
                productionPlan.setName(name);
                productionPlan.setCycle(cycle);
                productionPlan.setCode(code);
                productionPlan.setProjectName(projectName);
                productionPlan.setPlanDate(planDate);
                productionPlan.setPlanValue(planValue);

                this.saveOrUpdate(productionPlan);
            }
        }
    }

    @Override
    public List<ProductionPlan> getAll() {
        return this.list();
    }

    @Override
    public IPage<ProductionPlan> query(Page page, ProductionPlanQueryParam productionPlanQueryParam) {
        QueryWrapper<ProductionPlan> queryWrapper = productionPlanQueryParam.build();

        if (productionPlanQueryParam.getPlanDateStart() != null) {
            queryWrapper.ge("plan_date", productionPlanQueryParam.getPlanDateStart());
        }
        if (productionPlanQueryParam.getPlanDateEnd() != null) {
            queryWrapper.le("plan_date", productionPlanQueryParam.getPlanDateEnd());
        }
        queryWrapper.orderByAsc("project_name");
        queryWrapper.orderByAsc("plan_date");
        return this.page(page, queryWrapper);
    }


    @Override
    public List<Map<String, Object>> queryProductionPlanByMonth(ProductionPlanQueryParam productionPlanQueryParam) {
        String projectName = productionPlanQueryParam.getProjectName();
        String mold = productionPlanQueryParam.getMold();
        String cycle = productionPlanQueryParam.getCycle();

        LocalDate planDateStart = productionPlanQueryParam.getPlanDateStart();
        LocalDate planDateEnd = productionPlanQueryParam.getPlanDateEnd();
        //获取行转列表头
        List<String> planDateList = productionPlanMapper.findPlanDateByMonth(projectName,
                mold, cycle, planDateStart, planDateEnd);
        if (planDateList == null || planDateList.size() == 0) {
            throw new BusinessException("所选条件不存在数据，请确认");
        }

        StringBuffer selectColumn = new StringBuffer();
        StringBuffer pivotIn = new StringBuffer();
        StringBuffer selectVarcharColumn = new StringBuffer();
        StringBuffer selectJHCHANCHUColumn = new StringBuffer(); //计划模压产出片数(PCS)
        StringBuffer selectJHLINGLIAOColumn = new StringBuffer(); //计划后道领料(PCS)
        StringBuffer selectJHHDCHANCHUColumn = new StringBuffer(); //计划后道产出（颗)
        StringBuffer selectJHZHITONGLVColumn = new StringBuffer(); //计划后道直通率
        for (int i = 0; i < planDateList.size(); i++) {
            String planDate = planDateList.get(i);
            if (i == 0) {
                selectColumn.append("max([" + planDate + "]) as '" + planDate + "'");
                pivotIn.append("[" + planDate + "]");
                selectVarcharColumn.append("case when code in ('JHXNLIANGLV', 'MBLIANGLV', 'JHHDLIANGLV', 'JHZHITONGLV') " +
                        "   then cast(cast([" + planDate + "] * 100 as decimal(18, 2)) as varchar(50)) + '%'" +
                        "   else cast(FLOOR(ROUND([" + planDate + "], 0)) as varchar(50)) end '" + planDate + "'");
                selectJHCHANCHUColumn.append("TEMP_JHTOURU.[" + planDate + "] * TEMP_MBLIANGLV.[" + planDate + "] as '" + planDate + "'");
                selectJHLINGLIAOColumn.append("TEMP_JHCHANCHU.[" + planDate + "] * TEMP_JHXNLIANGLV.[" + planDate + "] as '" + planDate + "'");
                selectJHHDCHANCHUColumn.append("TEMP_JHXUESHU.[" + planDate + "] * TEMP_JHLINGLIAO.[" + planDate + "] * TEMP_JHHDLIANGLV.[" + planDate + "] as '" + planDate + "'");
                selectJHZHITONGLVColumn.append("TEMP_JHXNLIANGLV.[" + planDate + "] * TEMP_JHHDLIANGLV.[" + planDate + "] as '" + planDate + "'");
            } else {
                selectColumn.append(", max([" + planDate + "]) as '" + planDate + "'");
                pivotIn.append(", [" + planDate + "]");
                selectVarcharColumn.append(", case when code in ('JHXNLIANGLV', 'MBLIANGLV', 'JHHDLIANGLV', 'JHZHITONGLV') " +
                        "   then cast(cast([" + planDate + "] * 100 as decimal(18, 2)) as varchar(50)) + '%'" +
                        "   else cast(FLOOR(ROUND([" + planDate + "], 0)) as varchar(50)) end '" + planDate + "'");
                selectJHCHANCHUColumn.append(", TEMP_JHTOURU.[" + planDate + "] * TEMP_MBLIANGLV.[" + planDate + "] as '" + planDate + "'");
                selectJHLINGLIAOColumn.append(", TEMP_JHCHANCHU.[" + planDate + "] * TEMP_JHXNLIANGLV.[" + planDate + "] as '" + planDate + "'");
                selectJHHDCHANCHUColumn.append(", TEMP_JHXUESHU.[" + planDate + "] * TEMP_JHLINGLIAO.[" + planDate + "] * TEMP_JHHDLIANGLV.[" + planDate + "] as '" + planDate + "'");
                selectJHZHITONGLVColumn.append(", TEMP_JHXNLIANGLV.[" + planDate + "] * TEMP_JHHDLIANGLV.[" + planDate + "] as '" + planDate + "'");
            }
        }

        //统计汇总
        StringBuffer selectJHHDCHANCHUSumColumn = new StringBuffer(); //计划后道产出（颗)
        StringBuffer selectJHCHANCHUSumColumn = new StringBuffer(); //计划模压产出片数(PCS)
        StringBuffer selectJHLINGLIAOSumColumn = new StringBuffer(); //计划后道领料(PCS)
        for(int i=0; i<planDateList.size(); i++) {
            String planDate = planDateList.get(i);
            if (i == 0) {
                selectJHHDCHANCHUSumColumn.append("ISNULL(ROUND(TEMP_JHXUESHU.[" + planDate + "] * TEMP_JHLINGLIAO.[" + planDate + "] * TEMP_JHHDLIANGLV.[" + planDate + "],0), 0) ");
                selectJHCHANCHUSumColumn.append("ISNULL(ROUND(TEMP_JHTOURU.[" + planDate + "] * TEMP_MBLIANGLV.[" + planDate + "], 0), 0) ");
                selectJHLINGLIAOSumColumn.append("ISNULL(ROUND(TEMP_JHCHANCHU.[" + planDate + "] * TEMP_JHXNLIANGLV.[" + planDate + "], 0), 0) ");
            } else
            {
                selectJHHDCHANCHUSumColumn.append(" + ISNULL(ROUND(TEMP_JHXUESHU.[" + planDate + "] * TEMP_JHLINGLIAO.[" + planDate + "] * TEMP_JHHDLIANGLV.[" + planDate + "],0), 0) ");
                selectJHCHANCHUSumColumn.append(" + ISNULL(ROUND(TEMP_JHTOURU.[" + planDate + "] * TEMP_MBLIANGLV.[" + planDate + "], 0), 0) ");
                selectJHLINGLIAOSumColumn.append(" + ISNULL(ROUND(TEMP_JHCHANCHU.[" + planDate + "] * TEMP_JHXNLIANGLV.[" + planDate + "], 0), 0) ");
            }
        }

//        page.addOrder(OrderItem.asc("cycle")).addOrder(OrderItem.asc("code"));
        List<Map<String, Object>> productionPlanList = productionPlanMapper.findProductionPlanByMonth(
                projectName,
                mold,
                cycle,
                selectColumn.toString(),
                pivotIn.toString(),
                selectVarcharColumn.toString(),
                selectJHCHANCHUColumn.toString(),
                selectJHLINGLIAOColumn.toString(),
                selectJHHDCHANCHUColumn.toString(),
                selectJHZHITONGLVColumn.toString(),
                selectJHHDCHANCHUSumColumn.toString(),
                selectJHCHANCHUSumColumn.toString(),
                selectJHLINGLIAOSumColumn.toString(),
                planDateStart,
                planDateEnd);

        return productionPlanList;

    }


    @Override
    public JSONArray queryProductionPlanTitleByMonth(ProductionPlanQueryParam productionPlanQueryParam) {

        String projectName = productionPlanQueryParam.getProjectName();
        String mold = productionPlanQueryParam.getMold();
        String cycle = productionPlanQueryParam.getCycle();

        LocalDate planDateStart = productionPlanQueryParam.getPlanDateStart();
        LocalDate planDateEnd = productionPlanQueryParam.getPlanDateEnd();
        //获取行转列表头
        List<String> planDateList = productionPlanMapper.findPlanDateByMonth(projectName,
                mold, cycle, planDateStart, planDateEnd);

        JSONArray tableColumnJsonArray = new JSONArray();

        JSONObject seqJsonObject = new JSONObject();
        seqJsonObject.put("prop", "seq");
        seqJsonObject.put("label", "序号");
        seqJsonObject.put("fixed", "left");
        seqJsonObject.put("minWidth", "80");
        tableColumnJsonArray.add(seqJsonObject);

        JSONObject projectNameJsonObject = new JSONObject();
        projectNameJsonObject.put("prop", "projectName");
        projectNameJsonObject.put("label", "项目");
        projectNameJsonObject.put("fixed", "left");
        projectNameJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(projectNameJsonObject);

        JSONObject moldJsonObject = new JSONObject();
        moldJsonObject.put("prop", "mold");
        moldJsonObject.put("label", "模具");
        moldJsonObject.put("fixed", "left");
        moldJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(moldJsonObject);

        JSONObject cycleJsonObject = new JSONObject();
        cycleJsonObject.put("prop", "cycle");
        cycleJsonObject.put("label", "周期");
        cycleJsonObject.put("fixed", "left");
        cycleJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(cycleJsonObject);

//        JSONObject codeJsonObject = new JSONObject();
//        codeJsonObject.put("prop", "code");
//        codeJsonObject.put("label", "条件代码");
//        codeJsonObject.put("fixed", "left");
//        codeJsonObject.put("minWidth", "120");
//        tableColumnJsonArray.add(codeJsonObject);

        JSONObject statusJsonObject = new JSONObject();
        statusJsonObject.put("prop", "name");
        statusJsonObject.put("label", "项目2");
        statusJsonObject.put("fixed", "left");
        statusJsonObject.put("minWidth", "160");
        tableColumnJsonArray.add(statusJsonObject);


        JSONObject maxQtyJsonObject = new JSONObject();
        maxQtyJsonObject.put("prop", "sumQty");
        maxQtyJsonObject.put("label", "汇总");
        maxQtyJsonObject.put("fixed", "left");
        maxQtyJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(maxQtyJsonObject);

        for (String planDate : planDateList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("prop", planDate);
            jsonObject.put("label", planDate);
            jsonObject.put("minWidth", "110");
            tableColumnJsonArray.add(jsonObject);
        }

        return tableColumnJsonArray;
    }

    @Override
    public ProductionPlan queryProductionPlan(String projectName,
                                              String mold,
                                              String cycle,
                                              String code,
                                              LocalDate planDate) {
        QueryWrapper<ProductionPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", projectName);
        queryWrapper.eq("mold", mold);
        queryWrapper.eq("cycle", cycle);
        queryWrapper.eq("code", code);
        queryWrapper.eq("plan_date", planDate);

        List<ProductionPlan> productionPlanList = productionPlanMapper.selectList(queryWrapper);
        if (productionPlanList != null && productionPlanList.size() > 0) {
            return productionPlanList.get(0);
        }
        return null;
    }
}
