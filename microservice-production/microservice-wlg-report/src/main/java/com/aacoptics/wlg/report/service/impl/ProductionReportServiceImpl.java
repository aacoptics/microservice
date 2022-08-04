package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.param.ProductionDayReportQueryParam;
import com.aacoptics.wlg.report.entity.param.ProductionMonthReportQueryParam;
import com.aacoptics.wlg.report.entity.param.ProductionProjectReportQueryParam;
import com.aacoptics.wlg.report.exception.BusinessException;
import com.aacoptics.wlg.report.mapper.ProductionReportMapper;
import com.aacoptics.wlg.report.service.ProductionReportService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductionReportServiceImpl implements ProductionReportService {

    @Autowired
    private ProductionReportMapper productionReportMapper;


    @Override
    public List<Map<String, Object>> queryProductionMonthReportByCondition(ProductionMonthReportQueryParam productionMonthReportQueryParam) {
        String projectName = productionMonthReportQueryParam.getProjectName();

        LocalDate dateStart = productionMonthReportQueryParam.getDateStart();
        LocalDate dateEnd = productionMonthReportQueryParam.getDateEnd();
        //获取行转列表头
        List<String> reportDateList = productionReportMapper.findProductionReportDateByCondition(projectName, null, null,
                dateStart, dateEnd);
        if (reportDateList == null || reportDateList.size() == 0) {
            throw new BusinessException("所选条件不存在数据，请确认");
        }

        StringBuffer selectDateColumn = new StringBuffer(); // 例：[2022-03-01] as '2022-03-01'
        StringBuffer selectColumn = new StringBuffer(); // 例：sum([2022-03-01]) as '2022-03-01'
        StringBuffer selectVarcharColumn = new StringBuffer(); //例：cast(floor([2022-03-01]) as varchar(50)) as '2022-03-01'
        StringBuffer selectSumVarcharColumn = new StringBuffer(); //例：cast(floor(sum([2022-03-01])) as varchar(50)) as '2022-03-01'
        StringBuffer pivotIn = new StringBuffer(); //例 [2022-03-01]
        StringBuffer selectRateColumn = new StringBuffer(); //例 cast(ISNULL(TA.[2022-03-01], 0) / TP.[2022-03-01] *100 as varchar(50)) +'%' as '2022-03-01'
        StringBuffer selectSumRateColumn = new StringBuffer(); //例 cast(ISNULL(sum(TA.[2022-03-01]), 0) / sum(TP.[2022-03-01]) *100 as varchar(50)) +'%' as '2022-03-01'
        StringBuffer selectJHCHANCHUColumn = new StringBuffer(); //计划模压产出片数(PCS)
        StringBuffer selectJHLINGLIAOColumn = new StringBuffer(); //计划后道领料(PCS)
        StringBuffer selectJHHDCHANCHUColumn = new StringBuffer(); //计划后道产出（颗)

        for (int i = 0; i < reportDateList.size(); i++) {
            String reportDate = reportDateList.get(i);
            if (i == 0) {
                selectDateColumn.append("[" + reportDate + "] as '" + reportDate + "'");
                selectColumn.append("sum([" + reportDate + "]) as '" + reportDate + "'");
                selectVarcharColumn.append("cast(floor(ROUND([" + reportDate + "], 0)) as varchar(50)) as '" + reportDate + "'");
                selectSumVarcharColumn.append("cast(floor(ROUND(sum([" + reportDate + "]), 0)) as varchar(50)) as '" + reportDate + "'");
                selectRateColumn.append("cast(cast(ISNULL(TA.[" + reportDate + "], 0) / TP.[" + reportDate + "] *100 as decimal(18, 2)) as varchar(50)) +'%' as '" + reportDate + "'");
                selectSumRateColumn.append("cast(cast(ISNULL(sum(TA.[" + reportDate + "]), 0) / sum(TP.[" + reportDate + "]) *100 as decimal(18, 2)) as varchar(50)) +'%' as '" + reportDate + "'");
                pivotIn.append("[" + reportDate + "]");

                selectJHCHANCHUColumn.append("SUM(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "], 0)) as '" + reportDate + "'");
                selectJHLINGLIAOColumn.append("SUM(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "], 0)) as '" + reportDate + "'");
                selectJHHDCHANCHUColumn.append("SUM(ROUND(TEMP_JHXUESHU.[" + reportDate + "] * TEMP_JHTOURU.[" + reportDate + "]* TEMP_MBLIANGLV.[" + reportDate + "]* TEMP_JHXNLIANGLV.[" + reportDate + "]  * TEMP_JHHDLIANGLV.[" + reportDate + "], 0)) as '" + reportDate + "'");
            } else {
                selectDateColumn.append(",[" + reportDate + "] as '" + reportDate + "'");
                selectColumn.append(", sum([" + reportDate + "]) as '" + reportDate + "'");
                selectVarcharColumn.append(", cast(floor(ROUND([" + reportDate + "], 0)) as varchar(50)) as '" + reportDate + "'");
                selectSumVarcharColumn.append(", cast(floor(ROUND(sum([" + reportDate + "]), 0)) as varchar(50)) as '" + reportDate + "'");
                selectRateColumn.append(", cast(cast(ISNULL(TA.[" + reportDate + "], 0) / TP.[" + reportDate + "] *100 as decimal(18, 2)) as varchar(50)) +'%' as '" + reportDate + "'");
                selectSumRateColumn.append(", cast(cast(ISNULL(sum(TA.[" + reportDate + "]), 0) / sum(TP.[" + reportDate + "]) *100 as decimal(18, 2)) as varchar(50)) +'%' as '" + reportDate + "'");
                pivotIn.append(", [" + reportDate + "]");

                selectJHCHANCHUColumn.append(", SUM(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "], 0)) as '" + reportDate + "'");
                selectJHLINGLIAOColumn.append(", SUM(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "], 0)) as '" + reportDate + "'");
                selectJHHDCHANCHUColumn.append(", SUM(ROUND(TEMP_JHXUESHU.[" + reportDate + "] * TEMP_JHTOURU.[" + reportDate + "]* TEMP_MBLIANGLV.[" + reportDate + "]* TEMP_JHXNLIANGLV.[" + reportDate + "]  * TEMP_JHHDLIANGLV.[" + reportDate + "], 0)) as '" + reportDate + "'");
            }
        }

        //统计汇总
        StringBuffer selectJHHDCHANCHUSumColumn = new StringBuffer(); //计划后道产出（颗)
        StringBuffer selectJHCHANCHUSumColumn = new StringBuffer(); //计划模压产出片数(PCS)
        StringBuffer selectJHLINGLIAOSumColumn = new StringBuffer(); //计划后道领料(PCS)
        for(int i=0; i<reportDateList.size(); i++) {
            String reportDate = reportDateList.get(i);
            if (i == 0) {
                selectJHHDCHANCHUSumColumn.append("ISNULL(ROUND(TEMP_JHXUESHU.[" + reportDate + "] * TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "] * TEMP_JHHDLIANGLV.[" + reportDate + "],0), 0) ");
                selectJHCHANCHUSumColumn.append("ISNULL(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "], 0), 0) ");
                selectJHLINGLIAOSumColumn.append("ISNULL(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "], 0), 0) ");
            } else
            {
                selectJHHDCHANCHUSumColumn.append(" + ISNULL(ROUND(TEMP_JHXUESHU.[" + reportDate + "] * TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "] * TEMP_JHHDLIANGLV.[" + reportDate + "],0), 0) ");
                selectJHCHANCHUSumColumn.append(" + ISNULL(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "], 0), 0) ");
                selectJHLINGLIAOSumColumn.append(" + ISNULL(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "], 0), 0) ");
            }
        }
        selectJHHDCHANCHUSumColumn = new StringBuffer("SUM(" + selectJHHDCHANCHUSumColumn + ")");
        selectJHCHANCHUSumColumn = new StringBuffer("SUM(" + selectJHCHANCHUSumColumn + ")");
        selectJHLINGLIAOSumColumn = new StringBuffer("SUM(" + selectJHLINGLIAOSumColumn + ")");

        List<Map<String, Object>> productionMonthList = productionReportMapper.findProductionMonthReportByCondition(
                projectName,
                selectDateColumn.toString(),
                selectColumn.toString(),
                pivotIn.toString(),
                selectVarcharColumn.toString(),
                selectSumVarcharColumn.toString(),
                selectRateColumn.toString(),
                selectSumRateColumn.toString(),
                selectJHCHANCHUColumn.toString(),
                selectJHLINGLIAOColumn.toString(),
                selectJHHDCHANCHUColumn.toString(),
                selectJHHDCHANCHUSumColumn.toString(),
                selectJHCHANCHUSumColumn.toString(),
                selectJHLINGLIAOSumColumn.toString(),
                dateStart,
                dateEnd);

        return productionMonthList;

    }


    @Override
    public JSONArray queryProductionMonthReportTitleByCondition(ProductionMonthReportQueryParam productionMonthReportQueryParam) {

        String projectName = productionMonthReportQueryParam.getProjectName();

        LocalDate dateStart = productionMonthReportQueryParam.getDateStart();
        LocalDate dateEnd = productionMonthReportQueryParam.getDateEnd();
        //获取行转列表头
        List<String> planDateList = productionReportMapper.findProductionReportDateByCondition(projectName, null, null,
                dateStart, dateEnd);

        JSONArray tableColumnJsonArray = new JSONArray();

        JSONObject seqJsonObject = new JSONObject();
        seqJsonObject.put("prop", "seq");
        seqJsonObject.put("label", "序号");
        seqJsonObject.put("fixed", "left");
        seqJsonObject.put("minWidth", "70");
        tableColumnJsonArray.add(seqJsonObject);

        JSONObject projectNameJsonObject = new JSONObject();
        projectNameJsonObject.put("prop", "projectName");
        projectNameJsonObject.put("label", "项目");
        projectNameJsonObject.put("fixed", "left");
        projectNameJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(projectNameJsonObject);

//        JSONObject codeJsonObject = new JSONObject();
//        codeJsonObject.put("prop", "code");
//        codeJsonObject.put("label", "条件代码");
//        codeJsonObject.put("fixed", "left");
//        codeJsonObject.put("minWidth", "150");
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
    public List<Map<String, Object>> queryProductionDayReportByCondition(ProductionDayReportQueryParam productionDayReportQueryParam) {

        String projectName = productionDayReportQueryParam.getProjectName();
        String mold = productionDayReportQueryParam.getMold();
        String cycle = productionDayReportQueryParam.getCycle();

        LocalDate dateStart = productionDayReportQueryParam.getDateStart();
        LocalDate dateEnd = productionDayReportQueryParam.getDateEnd();

        List<Map<String, Object>> productionDayList = productionReportMapper.findProductionDayReportByCondition(
                projectName,
                mold,
                cycle,
                dateStart,
                dateEnd);

        return productionDayList;
    }


    @Override
    public List<Map<String, Object>> queryProductionProjectReportByCondition(ProductionProjectReportQueryParam productionProjectReportQueryParam) {

        String projectName = productionProjectReportQueryParam.getProjectName();

        String cycle = productionProjectReportQueryParam.getCycle();

        String mold = productionProjectReportQueryParam.getMold();

        LocalDate dateStart = productionProjectReportQueryParam.getDateStart();
        LocalDate dateEnd = productionProjectReportQueryParam.getDateEnd();

        //获取表头
        List<String> reportDateList = productionReportMapper.findProductionReportDateByCondition(projectName, mold, cycle,
                dateStart, dateEnd);
        if (reportDateList == null || reportDateList.size() == 0) {
            throw new BusinessException("所选条件不存在数据，请确认");
        }

        StringBuffer selectDateColumn = new StringBuffer();
        StringBuffer selectColumn = new StringBuffer();
        StringBuffer pivotIn = new StringBuffer();

        StringBuffer selectVarcharColumn = new StringBuffer();
        StringBuffer selectSumVarcharColumn = new StringBuffer();
        StringBuffer selectJHCHANCHUColumn = new StringBuffer(); //计划模压产出片数(PCS)
        StringBuffer selectJHLINGLIAOColumn = new StringBuffer(); //计划后道领料(PCS)
        StringBuffer selectJHHDCHANCHUColumn = new StringBuffer(); //计划后道产出（颗)
        StringBuffer selectJHZHITONGLVColumn = new StringBuffer(); //计划后道直通率
        StringBuffer selectSJLIANGLVColumn = new StringBuffer(); //实际模压成型良率
        StringBuffer selectSJZHITONGLVColumn = new StringBuffer(); //实际后道直通率
        StringBuffer selectDCLVColumn = new StringBuffer(); //模次产出达成率 //入库达成率

        for (int i = 0; i < reportDateList.size(); i++) {
            String reportDate = reportDateList.get(i);
            if (i == 0) {
                selectDateColumn.append("[" + reportDate + "] as '" + reportDate + "'");
                selectColumn.append("sum([" + reportDate + "]) as '" + reportDate + "'");
                pivotIn.append("[" + reportDate + "]");
                selectVarcharColumn.append("case when code in ('JHXNLIANGLV', 'MBLIANGLV', 'JHHDLIANGLV', 'JHZHITONGLV', 'SJXNLIANGLV', 'SJHDLIANGLV', 'SJLIANGLV', 'SJZHITONGLV', 'CHANCHUDCL', 'RUKUDCL') " +
                        "   then cast(cast([" + reportDate + "] * 100 as decimal(18, 2)) as varchar(50)) + '%'" +
                        "   else cast(floor(ROUND([" + reportDate + "], 0)) as varchar(50)) end '" + reportDate + "'");
                selectSumVarcharColumn.append("case when code in ('JHXNLIANGLV', 'MBLIANGLV', 'JHHDLIANGLV', 'JHZHITONGLV', 'SJXNLIANGLV', 'SJHDLIANGLV', 'SJLIANGLV', 'SJZHITONGLV', 'CHANCHUDCL', 'RUKUDCL') " +
                        "   then cast(cast(sum([" + reportDate + "])/count(1) * 100 as decimal(18, 2)) as varchar(50)) + '%'" +
                        "   else cast(floor(ROUND(sum([" + reportDate + "]), 0)) as varchar(50)) end '" + reportDate + "'");

                selectJHCHANCHUColumn.append("TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "] as '" + reportDate + "'");
                selectJHLINGLIAOColumn.append("TEMP_JHCHANCHU.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "] as '" + reportDate + "'");
                selectJHHDCHANCHUColumn.append("TEMP_JHXUESHU.[" + reportDate + "] * TEMP_JHLINGLIAO.[" + reportDate + "] * TEMP_JHHDLIANGLV.[" + reportDate + "] as '" + reportDate + "'");
                selectJHZHITONGLVColumn.append("TEMP_JHXNLIANGLV.[" + reportDate + "] * TEMP_JHHDLIANGLV.[" + reportDate + "] as '" + reportDate + "'");
                selectSJLIANGLVColumn.append("ISNULL(TCC.[" + reportDate + "] / TPA.[" + reportDate + "], 0) as '" + reportDate + "'");
                selectSJZHITONGLVColumn.append("TPA.[" + reportDate + "] * TPB.[" + reportDate + "] as '" + reportDate + "'");
                selectDCLVColumn.append("TPA.[" + reportDate + "] / TPB.[" + reportDate + "] as '" + reportDate + "'");
            } else {
                selectDateColumn.append(",[" + reportDate + "] as '" + reportDate + "'");
                selectColumn.append(", sum([" + reportDate + "]) as '" + reportDate + "'");
                pivotIn.append(", [" + reportDate + "]");
                selectVarcharColumn.append(", case when code in ('JHXNLIANGLV', 'MBLIANGLV', 'JHHDLIANGLV', 'JHZHITONGLV', 'SJXNLIANGLV', 'SJHDLIANGLV', 'SJLIANGLV', 'SJZHITONGLV', 'CHANCHUDCL', 'RUKUDCL') " +
                        "   then cast(cast([" + reportDate + "] * 100 as decimal(18, 2)) as varchar(50)) + '%'" +
                        "   else cast(floor(ROUND([" + reportDate + "], 0)) as varchar(50)) end '" + reportDate + "'");
                selectSumVarcharColumn.append(", case when code in ('JHXNLIANGLV', 'MBLIANGLV', 'JHHDLIANGLV', 'JHZHITONGLV', 'SJXNLIANGLV', 'SJHDLIANGLV', 'SJLIANGLV', 'SJZHITONGLV', 'CHANCHUDCL', 'RUKUDCL') " +
                        "   then cast(cast(sum([" + reportDate + "])/count(1) * 100 as decimal(18, 2)) as varchar(50)) + '%'" +
                        "   else cast(floor(ROUND(sum([" + reportDate + "]), 0)) as varchar(50)) end '" + reportDate + "'");
                selectJHCHANCHUColumn.append(", TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "] as '" + reportDate + "'");
                selectJHLINGLIAOColumn.append(", TEMP_JHCHANCHU.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "] as '" + reportDate + "'");
                selectJHHDCHANCHUColumn.append(", TEMP_JHXUESHU.[" + reportDate + "] * TEMP_JHLINGLIAO.[" + reportDate + "] * TEMP_JHHDLIANGLV.[" + reportDate + "] as '" + reportDate + "'");
                selectJHZHITONGLVColumn.append(", TEMP_JHXNLIANGLV.[" + reportDate + "] * TEMP_JHHDLIANGLV.[" + reportDate + "] as '" + reportDate + "'");
                selectSJLIANGLVColumn.append(", ISNULL(TCC.[" + reportDate + "] / TPA.[" + reportDate + "], 0) as '" + reportDate + "'");
                selectSJZHITONGLVColumn.append(", TPA.[" + reportDate + "] * TPB.[" + reportDate + "] as '" + reportDate + "'");
                selectDCLVColumn.append(", TPA.[" + reportDate + "] / TPB.[" + reportDate + "] as '" + reportDate + "'");
            }
        }

        //统计汇总
        StringBuffer selectJHHDCHANCHUSumColumn = new StringBuffer(); //计划后道产出（颗)
        StringBuffer selectJHCHANCHUSumColumn = new StringBuffer(); //计划模压产出片数(PCS)
        StringBuffer selectJHLINGLIAOSumColumn = new StringBuffer(); //计划后道领料(PCS)
        for(int i=0; i<reportDateList.size(); i++) {
            String reportDate = reportDateList.get(i);
            if (i == 0) {
                selectJHHDCHANCHUSumColumn.append("ISNULL(ROUND(TEMP_JHXUESHU.[" + reportDate + "] * TEMP_JHLINGLIAO.[" + reportDate + "] * TEMP_JHHDLIANGLV.[" + reportDate + "],0), 0) ");
                selectJHCHANCHUSumColumn.append("ISNULL(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "], 0), 0) ");
                selectJHLINGLIAOSumColumn.append("ISNULL(ROUND(TEMP_JHCHANCHU.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "], 0), 0) ");
            } else
            {
                selectJHHDCHANCHUSumColumn.append(" + ISNULL(ROUND(TEMP_JHXUESHU.[" + reportDate + "] * TEMP_JHLINGLIAO.[" + reportDate + "] * TEMP_JHHDLIANGLV.[" + reportDate + "],0), 0) ");
                selectJHCHANCHUSumColumn.append(" + ISNULL(ROUND(TEMP_JHTOURU.[" + reportDate + "] * TEMP_MBLIANGLV.[" + reportDate + "], 0), 0) ");
                selectJHLINGLIAOSumColumn.append(" + ISNULL(ROUND(TEMP_JHCHANCHU.[" + reportDate + "] * TEMP_JHXNLIANGLV.[" + reportDate + "], 0), 0) ");
            }
        }

        List<Map<String, Object>> productionDayList = productionReportMapper.findProductionProjectReportByCondition(
                projectName,
                mold,
                cycle,
                selectDateColumn.toString(),
                selectColumn.toString(),
                pivotIn.toString(),
                selectVarcharColumn.toString(),
                selectSumVarcharColumn.toString(),
                selectJHCHANCHUColumn.toString(),
                selectJHLINGLIAOColumn.toString(),
                selectJHHDCHANCHUColumn.toString(),
                selectJHZHITONGLVColumn.toString(),
                selectSJLIANGLVColumn.toString(),
                selectSJZHITONGLVColumn.toString(),
                selectDCLVColumn.toString(),
                selectJHHDCHANCHUSumColumn.toString(),
                selectJHCHANCHUSumColumn.toString(),
                selectJHLINGLIAOSumColumn.toString(),
                dateStart,
                dateEnd);

        return productionDayList;
    }


    @Override
    public JSONArray queryProductionProjectReportTitleByCondition(ProductionProjectReportQueryParam productionProjectReportQueryParam) {

        String projectName = productionProjectReportQueryParam.getProjectName();
        String cycle = productionProjectReportQueryParam.getCycle();

        String mold = productionProjectReportQueryParam.getMold();

        LocalDate dateStart = productionProjectReportQueryParam.getDateStart();
        LocalDate dateEnd = productionProjectReportQueryParam.getDateEnd();
        //获取行转列表头
        List<String> productionDateList = productionReportMapper.findProductionReportDateByCondition(projectName, mold, cycle,
                dateStart, dateEnd);

        JSONArray tableColumnJsonArray = new JSONArray();

        JSONObject seqJsonObject = new JSONObject();
        seqJsonObject.put("prop", "seq");
        seqJsonObject.put("label", "序号");
        seqJsonObject.put("fixed", "left");
        seqJsonObject.put("minWidth", "70");
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
        moldJsonObject.put("minWidth", "70");
        tableColumnJsonArray.add(moldJsonObject);

        JSONObject cycleJsonObject = new JSONObject();
        cycleJsonObject.put("prop", "cycle");
        cycleJsonObject.put("label", "周期");
        cycleJsonObject.put("fixed", "left");
        cycleJsonObject.put("minWidth", "70");
        tableColumnJsonArray.add(cycleJsonObject);

//        JSONObject codeJsonObject = new JSONObject();
//        codeJsonObject.put("prop", "code");
//        codeJsonObject.put("label", "条件代码");
//        codeJsonObject.put("fixed", "left");
//        codeJsonObject.put("minWidth", "150");
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

        for (String planDate : productionDateList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("prop", planDate);
            jsonObject.put("label", planDate);
            jsonObject.put("minWidth", "110");
            tableColumnJsonArray.add(jsonObject);
        }

        return tableColumnJsonArray;
    }

    @Override
    public List<Map<String, Object>> queryProductionDayDataByDate(LocalDate monthStart, LocalDate productionDate) {
        return  productionReportMapper.findProductionDayDataByDate(monthStart, productionDate);
    }

    @Override
    public List<String> findProductionReportDateByCondition(String projectName, String mold, String cycle, LocalDate dateStart, LocalDate dateEnd) {
       return productionReportMapper.findProductionReportDateByCondition(projectName, mold, cycle,
                dateStart, dateEnd);
    }


    @Override
    public List<Map<String, Object>> findProductionDayReportDataByDate(LocalDate monthStart, LocalDate monthEnd, LocalDate productionDate) {
        return  productionReportMapper.findProductionDayReportDataByDate(monthStart, monthEnd, productionDate);
    }

    @Override
    public List<Map<String, Object>> findCustomerRequirementDataByDate(LocalDate monthStart, LocalDate productionDate) {
        return  productionReportMapper.findCustomerRequirementDataByDate(monthStart, productionDate);
    }
    @Override
    public List<Map<String, Object>> findProductionCurrentDayDataByDate(LocalDate productionDate) {
        return  productionReportMapper.findProductionCurrentDayDataByDate(productionDate);
    }


    @Override
    public List<Map<String, Object>> findTargetDeliveryDataByDate(LocalDate monthStart, LocalDate productionDate) {
        return  productionReportMapper.findTargetDeliveryDataByDate(monthStart, productionDate);
    }

    @Override
    public List<String> findItemNumberListByDate(LocalDate monthStart, LocalDate productionDate) {
        return  productionReportMapper.findItemNumberListByDate(monthStart, productionDate);
    }
    @Override
    public Map<String, String> findProjectNameItemNumberMap(LocalDate monthStart, LocalDate productionDate) {
        List<Map<String, Object>> projectNameItemNumberList = productionReportMapper.findProjectNameItemNumberMap(monthStart, productionDate);
        Map<String, String> projectNameItemNumberMap = new HashMap<>();
        if(projectNameItemNumberList != null && projectNameItemNumberList.size() > 0)
        {
            for(Map<String, Object> map : projectNameItemNumberList)
            {
                projectNameItemNumberMap.put(map.get("project_name")+"", map.get("item_number")+"");
            }
        }

        return  projectNameItemNumberMap;
    }

    @Override
    public Map<String, String> findProjectMapList(List<String> businessProjectList) {
        List<Map<String, Object>> projectMapList = productionReportMapper.findProjectMapList(businessProjectList);
        Map<String, String> projectMap = new HashMap<>();
        if(projectMapList != null && projectMapList.size() > 0)
        {
            for(Map<String, Object> map : projectMapList)
            {
                projectMap.put(map.get("internal_project")+"", map.get("business_project")+"");
            }
        }

        return  projectMap;
    }


    @Override
    public List<Map<String, Object>> findProductionSummaryDataByDate(LocalDate monthStart, LocalDate productionDate) {
        return  productionReportMapper.findProductionSummaryDataByDate(monthStart, productionDate);
    }

    @Override
    public List<Map<String, Object>> findDeliveryDataByDate(LocalDate monthStart, LocalDate productionDate, List<String> itemNumberList) {
        return  productionReportMapper.findDeliveryDataByDate(monthStart, productionDate, itemNumberList);
    }

    @Override
    public List<Map<String, Object>> findWarehouseBalanceDataByDate(LocalDateTime shiftStart, LocalDateTime shiftEnd, List<String> projectNameList) {
        return  productionReportMapper.findWarehouseBalanceDataByDate(shiftStart, shiftEnd, projectNameList);
    }
}
