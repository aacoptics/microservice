package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.param.MoldUseQueryParam;
import com.aacoptics.wlg.report.entity.po.MoldUse;
import com.aacoptics.wlg.report.exception.BusinessException;
import com.aacoptics.wlg.report.mapper.MoldUseMapper;
import com.aacoptics.wlg.report.service.MoldUseService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MoldUseServiceImpl extends ServiceImpl<MoldUseMapper, MoldUse> implements MoldUseService {

    @Autowired
    private MoldUseMapper moldUseMapper;

    @Override
    @Transactional
    public void importMoldUseExcel(InputStream in) throws IOException, InvalidFormatException {
        LocalDate currentLocalDate = LocalDate.now();

        List<String[]> excelDataList = ExcelUtil.read(in).get(0);

        String[] titleArray = excelDataList.get(0);//标题行

        String statusTitle = titleArray[0];
        String codeTitle = titleArray[1];
        if ((!"状态".equals(statusTitle)) || (!"条件代码".equals(codeTitle))) {
            throw new BusinessException("Excel模板错误，请确认");
        }

        for (int i = 1; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if(dataArray == null || dataArray.length == 0)
            {
                break;
            }
            String status = dataArray[0]; //状态
            String code = dataArray[1]; //条件代码
            String projectName = dataArray[2]; //项目

            if (StringUtils.isEmpty(status)) {
                throw new BusinessException("第" + (i + 1) + "行状态不能为空");
            }
            if (StringUtils.isEmpty(code)) {
                throw new BusinessException("第" + (i + 1) + "条件代码不能为空");
            }
            if (StringUtils.isEmpty(projectName)) {
                throw new BusinessException("第" + (i + 1) + "项目不能为空");
            }


            for (int j = 4; j < dataArray.length; j++) {

                try {
                    String moldDateStr = titleArray[j];//日期
                    String moldQtyStr = dataArray[j]; //数量
                    if (StringUtils.isEmpty(moldQtyStr)) {
                        continue;
                    }
                    Long moldQty = Long.valueOf(moldQtyStr);

                    LocalDate moldDate = null;
                    try {
                        moldDate = LocalDate.parse(moldDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception e) {
                        log.error("日期格式错误", e);
                        throw new BusinessException("日期格式错误" + e.getMessage());
                    }

                    // 模具使用情况只可覆盖前一天、当日和将来数值，其它日期不能修改或删除
                    MoldUse moldUse = null;
                    if (currentLocalDate.minusDays(1).isAfter(moldDate)) {
                        continue;
                    }
                    else
                    {
                        moldUse = this.queryMoldUseByCodeAndProjectAndDate(code, projectName, moldDate);
                        if (moldUse == null) {
                            moldUse = new MoldUse();
                        }
                    }

                    moldUse.setCode(code);
                    moldUse.setStatus(status);
                    moldUse.setProjectName(projectName);
                    moldUse.setMoldDate(moldDate);
                    moldUse.setMoldQty(moldQty);

                    this.saveOrUpdate(moldUse);
                }catch (Exception exception)
                {
                    log.error("第{}数据异常：{}", i, excelDataList.get(i));
                    log.error("异常信息", exception);
                    throw new BusinessException("第【" + (i+1) + "】行数据异常，" + exception.getClass().getSimpleName() + "，"+ exception.getMessage());
                }
            }
        }
    }

    @Override
    public List<MoldUse> getAll() {
        return this.list();
    }

    @Override
    public IPage<MoldUse> query(Page page, MoldUseQueryParam moldUseQueryParam) {
        QueryWrapper<MoldUse> queryWrapper = moldUseQueryParam.build();
        queryWrapper.eq(StringUtils.isNotBlank(moldUseQueryParam.getProjectName()), "project_name", moldUseQueryParam.getProjectName());

        if (moldUseQueryParam.getMoldDateStart() != null) {
            queryWrapper.ge("mold_date", moldUseQueryParam.getMoldDateStart());
        }
        if (moldUseQueryParam.getMoldDateEnd() != null) {
            queryWrapper.le("mold_date", moldUseQueryParam.getMoldDateEnd());
        }
        queryWrapper.orderByAsc("project_name");
        queryWrapper.orderByAsc("mold_date");
        return this.page(page, queryWrapper);
    }


    @Override
    public List<Map<String, Object>> queryMoldUseByMonth(MoldUseQueryParam moldUseQueryParam) {

        String projectName = moldUseQueryParam.getProjectName();
        LocalDate moldDateStart = moldUseQueryParam.getMoldDateStart();
        LocalDate moldDateEnd = moldUseQueryParam.getMoldDateEnd();
        //获取行转列表头
        List<String> moldDateList = moldUseMapper.findMoldDateByMonth(projectName, moldDateStart, moldDateEnd);
        if(moldDateList == null || moldDateList.size() == 0)
        {
            throw new BusinessException("所选条件不存在数据，请确认");
        }

        StringBuffer selectColumn = new StringBuffer();
        StringBuffer pivotIn = new StringBuffer();
        for (int i = 0; i < moldDateList.size(); i++) {
            String moldDate = moldDateList.get(i);
            if (i == 0) {
                selectColumn.append("max([" + moldDate + "]) as '" + moldDate + "'");
                pivotIn.append("[" + moldDate + "]");
            } else {
                selectColumn.append(", max([" + moldDate + "]) as '" + moldDate + "'");
                pivotIn.append(", [" + moldDate + "]");
            }
        }

        List<Map<String, Object>> moldUseList = moldUseMapper.findMoldUseByMonth(selectColumn.toString(), pivotIn.toString(), projectName, moldDateStart, moldDateEnd);

        return moldUseList;

    }


    @Override
    public JSONArray queryMoldUseTitleByMonth(MoldUseQueryParam moldUseQueryParam) {

        String projectName = moldUseQueryParam.getProjectName();
        LocalDate moldDateStart = moldUseQueryParam.getMoldDateStart();
        LocalDate moldDateEnd = moldUseQueryParam.getMoldDateEnd();
        //获取行转列表头
        List<String> moldDateList = moldUseMapper.findMoldDateByMonth(projectName, moldDateStart, moldDateEnd);

        JSONArray tableColumnJsonArray = new JSONArray();

        JSONObject seqJsonObject = new JSONObject();
        seqJsonObject.put("prop", "seq");
        seqJsonObject.put("label", "序号");
        seqJsonObject.put("fixed", "left");
        seqJsonObject.put("minWidth", "80");
        tableColumnJsonArray.add(seqJsonObject);

//        JSONObject codeJsonObject = new JSONObject();
//        codeJsonObject.put("prop", "code");
//        codeJsonObject.put("label", "条件代码");
//        codeJsonObject.put("fixed", "left");
//        codeJsonObject.put("minWidth", "100");
//        tableColumnJsonArray.add(codeJsonObject);

        JSONObject statusJsonObject = new JSONObject();
        statusJsonObject.put("prop", "status");
        statusJsonObject.put("label", "状态");
        statusJsonObject.put("fixed", "left");
        statusJsonObject.put("minWidth", "120");
        tableColumnJsonArray.add(statusJsonObject);

        JSONObject projectNameJsonObject = new JSONObject();
        projectNameJsonObject.put("prop", "projectName");
        projectNameJsonObject.put("label", "项目");
        projectNameJsonObject.put("fixed", "left");
        projectNameJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(projectNameJsonObject);

        JSONObject maxQtyJsonObject = new JSONObject();
        maxQtyJsonObject.put("prop", "maxQty");
        maxQtyJsonObject.put("label", "最大值");
        maxQtyJsonObject.put("fixed", "left");
        maxQtyJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(maxQtyJsonObject);

        for (String moldDate : moldDateList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("prop", moldDate);
            jsonObject.put("label", moldDate);
            jsonObject.put("minWidth", "110");
            tableColumnJsonArray.add(jsonObject);
        }

        return tableColumnJsonArray;
    }

    @Override
    public MoldUse queryMoldUseByCodeAndProjectAndDate(String code, String projectName, LocalDate moldDate) {
        QueryWrapper<MoldUse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        queryWrapper.eq("project_name", projectName);
        queryWrapper.eq("mold_date", moldDate);

        List<MoldUse> moldUseList = moldUseMapper.selectList(queryWrapper);
        if (moldUseList != null && moldUseList.size() > 0) {
            return moldUseList.get(0);
        }
        return null;
    }
}
