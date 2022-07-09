package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.param.TargetDeliveryQueryParam;
import com.aacoptics.wlg.report.entity.po.TargetDelivery;
import com.aacoptics.wlg.report.exception.BusinessException;
import com.aacoptics.wlg.report.mapper.TargetDeliveryMapper;
import com.aacoptics.wlg.report.service.CustomerRequirementService;
import com.aacoptics.wlg.report.service.TargetDeliveryService;
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
public class TargetDeliveryServiceImpl extends ServiceImpl<TargetDeliveryMapper, TargetDelivery> implements TargetDeliveryService {

    @Autowired
    private TargetDeliveryMapper targetDeliveryMapper;


    @Autowired
    private CustomerRequirementService customerRequirementService;

    @Override
    @Transactional
    public void importTargetDeliveryExcel(InputStream in) throws IOException, InvalidFormatException {
        LocalDate currentLocalDate = LocalDate.now();

        List<String[]> excelDataList = ExcelUtil.read(in).get(0);

        String[] titleArray = excelDataList.get(0);//标题行

        String projectNameTitle = titleArray[0];
        String itemNumberTitle = titleArray[1];
        if ((!"项目".equals(projectNameTitle)) || (!"物料号".equals(itemNumberTitle))) {
            throw new BusinessException("Excel模板错误，请确认");
        }

        for (int i = 1; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if(dataArray == null || dataArray.length == 0)
            {
                break;
            }
            String projectName = dataArray[0]; //项目
            String itemNumber = dataArray[1]; //，物料号
            String itemDescription = dataArray[2]; //物料描述

            if (StringUtils.isEmpty(projectName)) {
                throw new BusinessException("第" + (i + 1) + "行，项目不能为空");
            }
            if (StringUtils.isEmpty(itemNumber)) {
                throw new BusinessException("第" + (i + 1) + "行，物料号不能为空");
            }
            if (StringUtils.isEmpty(itemDescription)) {
                throw new BusinessException("第" + (i + 1) + "行，物料描述不能为空");
            }
//            int count = customerRequirementService.queryCustomerRequirementCountByProjectName(projectName);
//            if(count == 0)
//            {
//                throw new BusinessException("第" + (i + 1) + "行，项目【" + projectName + "】对应的客户需求不存在，请先导入客户需求数据！");
//            }

            for (int j = 3; j < dataArray.length; j++) {

                try {
                    String deliveryDateStr = titleArray[j];//日期
                    String deliveryQtyStr = dataArray[j]; //数量
                    if (StringUtils.isEmpty(deliveryQtyStr)) {
                        continue;
                    }
                    Long deliveryQty = Long.valueOf(deliveryQtyStr);

                    LocalDate deliveryDate = null;
                    try {
                        deliveryDate = LocalDate.parse(deliveryDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception e) {
                        log.error("日期格式错误", e);
                        throw new BusinessException("日期格式错误" + e.getMessage());
                    }

                    // 目标交货只可覆盖前一天、当日和将来数值，其它日期不能修改或删除
                    TargetDelivery targetDelivery = null;
//                    if (currentLocalDate.minusDays(1).isAfter(deliveryDate)) {
//                        continue;
//                    }
//                    else
//                    {
                        targetDelivery = this.queryTargetDeliveryByItemNumberAndProjectAndDate(itemNumber, projectName, deliveryDate);
                        if (targetDelivery == null) {
                            targetDelivery = new TargetDelivery();
                        }
//                    }

                    targetDelivery.setItemNumber(itemNumber);
                    targetDelivery.setItemDescription(itemDescription);
                    targetDelivery.setProjectName(projectName);
                    targetDelivery.setDeliveryDate(deliveryDate);
                    targetDelivery.setDeliveryQty(deliveryQty);

                    this.saveOrUpdate(targetDelivery);
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
    public List<TargetDelivery> getAll() {
        return this.list();
    }

    @Override
    public IPage<TargetDelivery> query(Page page, TargetDeliveryQueryParam targetDeliveryQueryParam) {
        QueryWrapper<TargetDelivery> queryWrapper = targetDeliveryQueryParam.build();
        queryWrapper.eq(StringUtils.isNotBlank(targetDeliveryQueryParam.getProjectName()), "project_name", targetDeliveryQueryParam.getProjectName());

        if (targetDeliveryQueryParam.getDeliveryDateStart() != null) {
            queryWrapper.ge("delivery_date", targetDeliveryQueryParam.getDeliveryDateStart());
        }
        if (targetDeliveryQueryParam.getDeliveryDateEnd() != null) {
            queryWrapper.le("delivery_date", targetDeliveryQueryParam.getDeliveryDateEnd());
        }
        queryWrapper.orderByAsc("project_name");
        queryWrapper.orderByAsc("delivery_date");
        return this.page(page, queryWrapper);
    }


    @Override
    public List<Map<String, Object>> queryTargetDeliveryByMonth(TargetDeliveryQueryParam TargetDeliveryQueryParam) {

        String projectName = TargetDeliveryQueryParam.getProjectName();
        LocalDate deliveryDateStart = TargetDeliveryQueryParam.getDeliveryDateStart();
        LocalDate deliveryDateEnd = TargetDeliveryQueryParam.getDeliveryDateEnd();
        //获取行转列表头
        List<String> deliveryDateList = targetDeliveryMapper.findDeliveryDateByMonth(projectName, deliveryDateStart, deliveryDateEnd);
        if(deliveryDateList == null || deliveryDateList.size() == 0)
        {
            throw new BusinessException("所选条件不存在数据，请确认！");
        }

        StringBuffer selectColumn = new StringBuffer();
        StringBuffer pivotIn = new StringBuffer();
        for (int i = 0; i < deliveryDateList.size(); i++) {
            String deliveryDate = deliveryDateList.get(i);
            if (i == 0) {
                selectColumn.append("max([" + deliveryDate + "]) as '" + deliveryDate + "'");
                pivotIn.append("[" + deliveryDate + "]");
            } else {
                selectColumn.append(", max([" + deliveryDate + "]) as '" + deliveryDate + "'");
                pivotIn.append(", [" + deliveryDate + "]");
            }
        }

        List<Map<String, Object>> TargetDeliveryList = targetDeliveryMapper.findTargetDeliveryByMonth(selectColumn.toString(), pivotIn.toString(), projectName, deliveryDateStart, deliveryDateEnd);

        return TargetDeliveryList;

    }


    @Override
    public JSONArray queryTargetDeliveryTitleByMonth(TargetDeliveryQueryParam targetDeliveryQueryParam) {

        String projectName = targetDeliveryQueryParam.getProjectName();
        LocalDate deliveryDateStart = targetDeliveryQueryParam.getDeliveryDateStart();
        LocalDate deliveryDateEnd = targetDeliveryQueryParam.getDeliveryDateEnd();
        //获取行转列表头
        List<String> deliveryDateList = targetDeliveryMapper.findDeliveryDateByMonth(projectName, deliveryDateStart, deliveryDateEnd);

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


        JSONObject itemNumberJsonObject = new JSONObject();
        itemNumberJsonObject.put("prop", "itemNumber");
        itemNumberJsonObject.put("label", "物料号");
        itemNumberJsonObject.put("fixed", "left");
        itemNumberJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(itemNumberJsonObject);

        JSONObject itemDescriptionJsonObject = new JSONObject();
        itemDescriptionJsonObject.put("prop", "itemDescription");
        itemDescriptionJsonObject.put("label", "物料描述");
        itemDescriptionJsonObject.put("fixed", "left");
        itemDescriptionJsonObject.put("minWidth", "120");
        tableColumnJsonArray.add(itemDescriptionJsonObject);


        for (String deliveryDate : deliveryDateList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("prop", deliveryDate);
            jsonObject.put("label", deliveryDate);
            jsonObject.put("minWidth", "110");
            tableColumnJsonArray.add(jsonObject);
        }

        return tableColumnJsonArray;
    }

    @Override
    public TargetDelivery queryTargetDeliveryByItemNumberAndProjectAndDate(String itemNumber, String projectName, LocalDate deliveryDate) {
        QueryWrapper<TargetDelivery> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_number", itemNumber);
        queryWrapper.eq("project_name", projectName);
        queryWrapper.eq("delivery_date", deliveryDate);

        List<TargetDelivery> targetDeliveryList = targetDeliveryMapper.selectList(queryWrapper);
        if (targetDeliveryList != null && targetDeliveryList.size() > 0) {
            return targetDeliveryList.get(0);
        }
        return null;
    }
}
