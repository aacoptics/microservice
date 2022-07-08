package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.param.ProductionSummaryQueryParam;
import com.aacoptics.wlg.report.entity.po.ProductionSummary;
import com.aacoptics.wlg.report.exception.BusinessException;
import com.aacoptics.wlg.report.mapper.ProductionSummaryMapper;
import com.aacoptics.wlg.report.service.ProductionSummaryService;
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
import java.util.Objects;

@Service
@Slf4j
public class ProductionSummaryServiceImpl extends ServiceImpl<ProductionSummaryMapper, ProductionSummary> implements ProductionSummaryService {

    @Autowired
    private ProductionSummaryMapper productionSummaryMapper;

    @Override
    @Transactional
    public void importProductionSummaryExcel(InputStream in) throws IOException, InvalidFormatException {
        LocalDate currentLocalDate = LocalDate.now();

        List<String[]> excelDataList = ExcelUtil.read(in).get(0);

        String[] titleArray = excelDataList.get(0);//标题行

        String projectNameTitle = titleArray[0];
        String productionTypeTitle = titleArray[1];
        if ((!"项目号".equals(projectNameTitle))) {
            throw new BusinessException("Excel模板错误，请确认!");
        }
        if (!"实际产出月度汇总".equals(productionTypeTitle) && !"目标产出月度汇总".equals(productionTypeTitle)) {
            throw new BusinessException("Excel模板错误，请确认!");
        }

        for (int i = 1; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if(dataArray == null || dataArray.length == 0)
            {
                break;
            }

            String projectName = dataArray[0]; //项目
            String sumQtyStr = dataArray[1]; //实际产出月度汇总

            for (int j = 2; j < dataArray.length; j++) {

                String productionDateStr = titleArray[j];//日期
                String qtyStr = dataArray[j]; //数量
                if (StringUtils.isEmpty(qtyStr)) {
                    continue;
                }


                if (StringUtils.isEmpty(projectName)) {
                    throw new BusinessException("第" + (i + 1) + "行，项目不能为空");
                }
                if (StringUtils.isEmpty(productionDateStr)) {
                    throw new BusinessException("第" + (i + 1) + "行，日期不能为空");
                }
                if (StringUtils.isEmpty(productionDateStr)) {
                    break;
                }
                BigDecimal qty = new BigDecimal(qtyStr);

                LocalDate productionDate = null;
                try {
                    productionDate = LocalDate.parse(productionDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    log.error("日期格式错误", e);
                    throw new BusinessException("日期格式错误" + e.getMessage());
                }

                ProductionSummary productionSummary = null;
//                if (currentLocalDate.isAfter(productionDate)) {
//                    continue;
//                }
//                else {
                    productionSummary = this.queryProductionSummary(projectName, productionDate);
                    if (productionSummary == null) {
                        productionSummary = new ProductionSummary();
                    }
//                }

                productionSummary.setProjectName(projectName);
                productionSummary.setProductionDate(productionDate);
                if("实际产出月度汇总".equals(productionTypeTitle))
                {
                    productionSummary.setActualQty(qty);
                }
                else if("目标产出月度汇总".equals(productionTypeTitle))
                {
                    productionSummary.setTargetQty(qty);
                }
                this.saveOrUpdate(productionSummary);
            }

        }
    }

    @Override
    public List<ProductionSummary> getAll() {
        return this.list();
    }

    @Override
    public IPage<ProductionSummary> query(Page page, ProductionSummaryQueryParam productionSummaryQueryParam) {
        QueryWrapper<ProductionSummary> queryWrapper = productionSummaryQueryParam.build();

        queryWrapper.eq(StringUtils.isNotBlank(productionSummaryQueryParam.getProjectName()), "project_name", productionSummaryQueryParam.getProjectName());

        if (productionSummaryQueryParam.getProductionDateStart() != null) {
            queryWrapper.ge("production_date", productionSummaryQueryParam.getProductionDateStart());
        }
        if (productionSummaryQueryParam.getProductionDateEnd() != null) {
            queryWrapper.le("production_date", productionSummaryQueryParam.getProductionDateEnd());
        }
        queryWrapper.orderByAsc("project_name");
        queryWrapper.orderByAsc("production_date");
        return this.page(page, queryWrapper);
    }


    @Override
    public ProductionSummary queryProductionSummary(String projectName, LocalDate productionDate) {
        QueryWrapper<ProductionSummary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", projectName);
        queryWrapper.eq("production_date", productionDate);

        List<ProductionSummary> productionSummaryList = productionSummaryMapper.selectList(queryWrapper);
        if (productionSummaryList != null && productionSummaryList.size() > 0) {
            return productionSummaryList.get(0);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryProductionSummaryByCondition(Page page, ProductionSummaryQueryParam productionSummaryQueryParam) {

        //获取行转列表头
        List<String> productionDateList = productionSummaryMapper.findProductionDateByMonth(productionSummaryQueryParam.getProjectName(),
                productionSummaryQueryParam.getProductionDateStart(), productionSummaryQueryParam.getProductionDateEnd());
        if(productionDateList == null || productionDateList.size() == 0)
        {
            throw new BusinessException("所选条件不存在数据，请确认");
        }

        StringBuffer selectColumn = new StringBuffer();
        StringBuffer pivotIn = new StringBuffer();
        for (int i = 0; i < productionDateList.size(); i++) {
            String productionDate = productionDateList.get(i);
            if (i == 0) {
                selectColumn.append("max([" + productionDate + "]) as '" + productionDate + "'");
                pivotIn.append("[" + productionDate + "]");
            } else {
                selectColumn.append(", max([" + productionDate + "]) as '" + productionDate + "'");
                pivotIn.append(", [" + productionDate + "]");
            }
        }

        List<Map<String, Object>> productionSummarys = productionSummaryMapper.queryProductionSummaryByCondition(selectColumn.toString(), pivotIn.toString(),
                productionSummaryQueryParam.getProjectName(),
                productionSummaryQueryParam.getProductionDateStart(),
                productionSummaryQueryParam.getProductionDateEnd());

        return productionSummarys;
    }


    @Override
    public boolean add(ProductionSummary productionSummary) {

        this.validationProductionSummary(productionSummary);

        boolean isSuccess = this.save(productionSummary);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ProductionSummary productionSummary) {
        this.validationProductionSummary(productionSummary);

        boolean isSuccess = this.updateById(productionSummary);
        return isSuccess;
    }


    @Override
    public ProductionSummary get(Long id) {
        ProductionSummary productionSummary = this.getById(id);
        if (Objects.isNull(productionSummary)) {
            throw new BusinessException("data not found with id:" + id);
        }
        return productionSummary;
    }



    /**
     * 校验数据
     *
     * @param productionSummary
     */
    private void validationProductionSummary(ProductionSummary productionSummary)
    {
        String projectName = productionSummary.getProjectName();
        if(StringUtils.isEmpty(projectName))
        {
            throw new BusinessException("项目不能为空");
        }
    }

    @Override
    public int queryProductionSummaryCountByProjectName(String projectName) {
        QueryWrapper<ProductionSummary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", projectName);

        Integer count = productionSummaryMapper.selectCount(queryWrapper);
        return count;
    }



    @Override
    public JSONArray queryProductionSummaryTitleByMonth(ProductionSummaryQueryParam productionSummaryQueryParam) {

        String projectName = productionSummaryQueryParam.getProjectName();
        LocalDate productionDateStart = productionSummaryQueryParam.getProductionDateStart();
        LocalDate productionDateEnd = productionSummaryQueryParam.getProductionDateEnd();
        //获取行转列表头
        List<String> productionDateList = productionSummaryMapper.findProductionDateByMonth(projectName, productionDateStart, productionDateEnd);

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

        JSONObject typeJsonObject = new JSONObject();
        typeJsonObject.put("prop", "type");
        typeJsonObject.put("label", "类型");
        typeJsonObject.put("fixed", "left");
        typeJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(typeJsonObject);

        JSONObject maxQtyJsonObject = new JSONObject();
        maxQtyJsonObject.put("prop", "sumQty");
        maxQtyJsonObject.put("label", "汇总");
        maxQtyJsonObject.put("fixed", "left");
        maxQtyJsonObject.put("minWidth", "100");
        tableColumnJsonArray.add(maxQtyJsonObject);

        for (String productionDate : productionDateList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("prop", productionDate);
            jsonObject.put("label", productionDate);
            jsonObject.put("minWidth", "110");
            tableColumnJsonArray.add(jsonObject);
        }

        return tableColumnJsonArray;
    }
}
