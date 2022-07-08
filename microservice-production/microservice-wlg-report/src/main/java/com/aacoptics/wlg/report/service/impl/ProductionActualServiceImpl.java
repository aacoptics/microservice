package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.param.ProductionActualQueryParam;
import com.aacoptics.wlg.report.entity.po.ProductionActual;
import com.aacoptics.wlg.report.exception.BusinessException;
import com.aacoptics.wlg.report.mapper.ProductionActualMapper;
import com.aacoptics.wlg.report.service.ProductionActualService;
import com.aacoptics.wlg.report.util.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
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
public class ProductionActualServiceImpl extends ServiceImpl<ProductionActualMapper, ProductionActual> implements ProductionActualService {

    @Autowired
    private ProductionActualMapper productionActualMapper;

    @Override
    @Transactional
    public void importProductionActualExcel(String fileName, InputStream in) throws IOException, InvalidFormatException {
        LocalDate currentLocalDate = LocalDate.now();


        log.info("开始导入生产报表：" + fileName);
        String[] fileNameArray = fileName.split("生产");
        String projectName = fileNameArray[0];
        if (projectName.contains("-")) {
            fileNameArray = fileName.split("-");
            projectName = fileNameArray[0];
        }

        List<String[]> excelDataList = ExcelUtil.read(in).get(0);

        String[] titleArray = excelDataList.get(1);//标题行

        String dateTitle = titleArray[0];
        String productTitle = titleArray[1];
        if ((!"日期".equals(dateTitle)) || (!"产品名称".equals(productTitle))) {
            throw new BusinessException("Excel模板错误，请确认");
        }

        for (int i = 2; i < excelDataList.size(); i++) {
            try {
                String[] dataArray = excelDataList.get(i);
                if(dataArray == null || dataArray.length == 0)
                {
                    break;
                }

                String actualDateStr = dataArray[0]; //日期
                if ("合计".equals(actualDateStr)) {
                    continue;
                }

                String product = dataArray[1]; //产品名称
                String mold = dataArray[2]; //模具
                String cycle = dataArray[3]; //周期

                String estimateHoleQtyStr = dataArray[20];//实际预估收穴数
                String moldPressInputQtyStr = dataArray[23];// 实际模压投入片数（PCS)
                String moldPressOutputQtyStr = dataArray[27];// 实际模压产出片数(PCS)
                String afterAcquisitionQtyStr = dataArray[32];// 实际后道领料(PCS)
                String performanceYieldStr = dataArray[34];// 实际性能良率
                String afterInputQtyStr = dataArray[37];// 后道实际投料(PCS)
                String afterOutputQtyStr = dataArray[47];// 实际后道产出（颗)
                String inventoryQtyStr = dataArray[50];// 实际入库（转镀膜）
                String afterYieldStr = dataArray[59];// 实际后道良率

                if (StringUtils.isEmpty(actualDateStr)) {
                    throw new BusinessException("第" + (i + 1) + "行日期不能为空");
                }
                if (StringUtils.isEmpty(projectName)) {
                    throw new BusinessException("第" + (i + 1) + "项目不能为空");
                }
                if (StringUtils.isEmpty(mold)) {
                    throw new BusinessException("第" + (i + 1) + "模具不能为空");
                }
                if (StringUtils.isEmpty(cycle)) {
                    throw new BusinessException("第" + (i + 1) + "周期不能为空");
                }

                Long estimateHoleQty = null;
                if (StringUtils.isNotEmpty(estimateHoleQtyStr)) {
                    estimateHoleQty = stringToLong(estimateHoleQtyStr);
                }
                Long moldPressInputQty = null;
                if (StringUtils.isNotEmpty(moldPressInputQtyStr)) {
                    moldPressInputQty = stringToLong(moldPressInputQtyStr);
                }
                Long moldPressOutputQty = null;
                if (StringUtils.isNotEmpty(moldPressOutputQtyStr)) {
                    moldPressOutputQty = stringToLong(moldPressOutputQtyStr);
                }
                Long afterAcquisitionQty = null;
                if (StringUtils.isNotEmpty(afterAcquisitionQtyStr)) {
                    afterAcquisitionQty = stringToLong(afterAcquisitionQtyStr);
                }
                BigDecimal performanceYield = null;
                if (StringUtils.isNotEmpty(performanceYieldStr)) {
                    performanceYield = new BigDecimal(performanceYieldStr);
                }
                Long afterInputQty = null;
                if (StringUtils.isNotEmpty(afterInputQtyStr)) {
                    afterInputQty = stringToLong(afterInputQtyStr);
                }
                Long afterOutputQty = null;
                if (StringUtils.isNotEmpty(afterOutputQtyStr)) {
                    afterOutputQty = stringToLong(afterOutputQtyStr);
                }
                Long inventoryQty = null;
                if (StringUtils.isNotEmpty(inventoryQtyStr)) {
                    inventoryQty = stringToLong(inventoryQtyStr);
                }
                BigDecimal afterYield = null;
                if (StringUtils.isNotEmpty(afterYieldStr)) {
                    afterYield = new BigDecimal(afterYieldStr);
                }

                LocalDate actualDate = null;
                try {
                    actualDate = LocalDate.parse(actualDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    log.error("日期格式错误", e);
                    throw new BusinessException("日期【" + actualDateStr + "】格式错误" + e.getMessage());
                }

                //只能对是过去七天（包括当天）的数据进行写入或修改，不可对将来时间的数据进行写入和修改
                ProductionActual productionActual = null;
                if (currentLocalDate.minusDays(7).isAfter(actualDate) && currentLocalDate.plusDays(1).isBefore(actualDate)) {
                    continue;
                }
                else
                {
                    productionActual = this.queryProductionActual(projectName, product, mold, cycle, actualDate);
                    if (productionActual == null) {
                        productionActual = new ProductionActual();
                    }
                }


                productionActual.setMold(mold);
                productionActual.setCycle(cycle);
                productionActual.setProjectName(projectName);
                productionActual.setProduct(product);
                productionActual.setActualDate(actualDate);
                productionActual.setEstimateHoleQty(estimateHoleQty);
                productionActual.setMoldPressInputQty(moldPressInputQty);
                productionActual.setMoldPressOutputQty(moldPressOutputQty);
                productionActual.setAfterAcquisitionQty(afterAcquisitionQty);
                productionActual.setPerformanceYield(performanceYield);
                productionActual.setAfterInputQty(afterInputQty);
                productionActual.setAfterOutputQty(afterOutputQty);
                productionActual.setInventoryQty(inventoryQty);
                productionActual.setAfterYield(afterYield);

                this.saveOrUpdate(productionActual);
            } catch (Exception e) {
                log.error("第{}数据异常：{}", i, excelDataList.get(i));
                log.error("异常信息", e);
                throw new BusinessException("第【" + (i+1) + "】行数据异常，" + e.getClass().getSimpleName() + "，"+ e.getMessage());
            }
        }

        log.info("完成导入生产报表：" + fileName);
    }

    @Override
    public List<ProductionActual> getAll() {
        return this.list();
    }

    @Override
    public IPage<ProductionActual> query(Page page, ProductionActualQueryParam productionActualQueryParam) {
        QueryWrapper<ProductionActual> queryWrapper = productionActualQueryParam.build();

        queryWrapper.eq(StringUtils.isNotBlank(productionActualQueryParam.getProjectName()), "project_name", productionActualQueryParam.getProjectName());
        queryWrapper.eq(StringUtils.isNotBlank(productionActualQueryParam.getMold()), "mold", productionActualQueryParam.getMold());
        queryWrapper.eq(StringUtils.isNotBlank(productionActualQueryParam.getCycle()), "cycle", productionActualQueryParam.getCycle());

        if (productionActualQueryParam.getActualDateStart() != null) {
            queryWrapper.ge("actual_date", productionActualQueryParam.getActualDateStart());
        }
        if (productionActualQueryParam.getActualDateEnd() != null) {
            queryWrapper.le("actual_date", productionActualQueryParam.getActualDateEnd());
        }
        queryWrapper.orderByAsc("project_name");
        queryWrapper.orderByAsc("mold");
        queryWrapper.orderByAsc("cycle");
        queryWrapper.orderByAsc("actual_date");
        return this.page(page, queryWrapper);
    }


    @Override
    public ProductionActual queryProductionActual(String projectName, String product,
                                                  String mold, String cycle, LocalDate actualDate) {
        QueryWrapper<ProductionActual> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", projectName);
        queryWrapper.eq("product", product);
        queryWrapper.eq("mold", mold);
        queryWrapper.eq("cycle", cycle);
        queryWrapper.eq("actual_date", actualDate);

        List<ProductionActual> productionActualList = productionActualMapper.selectList(queryWrapper);
        if (productionActualList != null && productionActualList.size() > 0) {
            return productionActualList.get(0);
        }
        return null;
    }

    @Override
    public IPage<Map<String, Object>> queryProductionActualByCondition(Page page, ProductionActualQueryParam productionActualQueryParam) {

        page.addOrder(OrderItem.asc("project_name"));
        page.addOrder(OrderItem.asc("product"));
        page.addOrder(OrderItem.asc("actual_date"));
        page.addOrder(OrderItem.asc("mold"));
        page.addOrder(OrderItem.asc("cycle"));

        IPage<Map<String, Object>> productionActuals = productionActualMapper.queryProductionActualByCondition(page,
                productionActualQueryParam.getProjectName(),
                productionActualQueryParam.getProduct(),
                productionActualQueryParam.getMold(),
                productionActualQueryParam.getCycle(),
                productionActualQueryParam.getActualDateStart(),
                productionActualQueryParam.getActualDateEnd());

        return productionActuals;
    }

    /**
     * 字符串转整数
     *
     * @param sourceValue
     * @return
     */
    private Long stringToLong(String sourceValue) throws Exception{
        if (StringUtils.isEmpty(sourceValue)) {
            return null;
        }
        Long targetValue = null;
        try {
            if (sourceValue.contains(".")) {
                Double doubleValue = Double.valueOf(sourceValue);
                targetValue = doubleValue.longValue();
            } else {
                targetValue = Long.valueOf(sourceValue);
            }
        } catch (Exception e) {
            log.error("值【{}】不是数字", sourceValue);
            throw new Exception("值【" + sourceValue +"】不是数字");
        }
        return targetValue;
    }
}
