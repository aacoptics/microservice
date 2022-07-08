package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.param.EstimateFpyQueryParam;
import com.aacoptics.wlg.report.entity.po.EstimateFpy;
import com.aacoptics.wlg.report.exception.BusinessException;
import com.aacoptics.wlg.report.mapper.EstimateFpyMapper;
import com.aacoptics.wlg.report.service.EstimateFpyService;
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
public class EstimateFpyServiceImpl extends ServiceImpl<EstimateFpyMapper, EstimateFpy> implements EstimateFpyService {

    @Autowired
    private EstimateFpyMapper estimateFpyMapper;

    @Override
    @Transactional
    public void importEstimateFpyExcel(InputStream in) throws IOException, InvalidFormatException {
        LocalDate currentLocalDate = LocalDate.now();

        List<String[]> excelDataList = ExcelUtil.read(in).get(0);

        String[] titleArray = excelDataList.get(0);//标题行

        String statusTitle = titleArray[0];
        String codeTitle = titleArray[1];
        if ((!"日期".equals(statusTitle)) || (!"项目名".equals(codeTitle))) {
            throw new BusinessException("Excel模板错误，请确认");
        }

        for (int i = 1; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if(dataArray == null || dataArray.length == 0)
            {
                break;
            }

            String fpyDateStr = dataArray[0]; //日期
            String projectName = dataArray[1]; //项目
            String mold = dataArray[2]; //模具
            String cycle = dataArray[3]; //周期
            String fpyStr = dataArray[4];//预估直通率
            String estimateBalanceStr = dataArray[5];//WLG预估结存

            if (StringUtils.isEmpty(fpyDateStr)) {
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
            if (StringUtils.isEmpty(fpyStr)) {
                break;
            }
            BigDecimal fpy = new BigDecimal(fpyStr);
            BigDecimal estimateBalance = new BigDecimal(estimateBalanceStr);

            LocalDate fpyDate = null;
            try {
                fpyDate = LocalDate.parse(fpyDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                log.error("日期格式错误", e);
                throw new BusinessException("日期格式错误" + e.getMessage());
            }

            EstimateFpy estimateFpy = null;
            if (currentLocalDate.minusDays(1).isAfter(fpyDate)) {
                continue;
            }
            else
            {
                estimateFpy = this.queryEstimateFpy(mold, cycle, projectName, fpyDate);
                if (estimateFpy == null) {
                    estimateFpy = new EstimateFpy();
                }
            }

            estimateFpy.setMold(mold);
            estimateFpy.setCycle(cycle);
            estimateFpy.setProjectName(projectName);
            estimateFpy.setFpyDate(fpyDate);
            estimateFpy.setFpy(fpy);
            estimateFpy.setEstimateBalance(estimateBalance);

            this.saveOrUpdate(estimateFpy);
        }
    }

    @Override
    public List<EstimateFpy> getAll() {
        return this.list();
    }

    @Override
    public IPage<EstimateFpy> query(Page page, EstimateFpyQueryParam estimateFpyQueryParam) {
        QueryWrapper<EstimateFpy> queryWrapper = estimateFpyQueryParam.build();

        queryWrapper.eq(StringUtils.isNotBlank(estimateFpyQueryParam.getProjectName()), "project_name", estimateFpyQueryParam.getProjectName());
        queryWrapper.eq(StringUtils.isNotBlank(estimateFpyQueryParam.getMold()), "mold", estimateFpyQueryParam.getMold());
        queryWrapper.eq(StringUtils.isNotBlank(estimateFpyQueryParam.getCycle()), "cycle", estimateFpyQueryParam.getCycle());

        if (estimateFpyQueryParam.getFpyDateStart() != null) {
            queryWrapper.ge("fpy_date", estimateFpyQueryParam.getFpyDateStart());
        }
        if (estimateFpyQueryParam.getFpyDateEnd() != null) {
            queryWrapper.le("fpy_date", estimateFpyQueryParam.getFpyDateEnd());
        }
        queryWrapper.orderByAsc("project_name");
        queryWrapper.orderByAsc("mold");
        queryWrapper.orderByAsc("cycle");
        queryWrapper.orderByAsc("fpy_date");
        return this.page(page, queryWrapper);
    }


    @Override
    public EstimateFpy queryEstimateFpy(String mold, String cycle, String projectName, LocalDate fpyDate) {
        QueryWrapper<EstimateFpy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mold", mold);
        queryWrapper.eq("cycle", cycle);
        queryWrapper.eq("project_name", projectName);
        queryWrapper.eq("fpy_date", fpyDate);

        List<EstimateFpy> estimateFpyList = estimateFpyMapper.selectList(queryWrapper);
        if (estimateFpyList != null && estimateFpyList.size() > 0) {
            return estimateFpyList.get(0);
        }
        return null;
    }

    @Override
    public IPage<Map<String, Object>> queryEstimateFpyByCondition(Page page, EstimateFpyQueryParam estimateFpyQueryParam) {

        page.addOrder(OrderItem.asc("project_name"));
        page.addOrder(OrderItem.asc("mold"));
        page.addOrder(OrderItem.asc("cycle"));
        page.addOrder(OrderItem.asc("fpy_date"));

        IPage<Map<String, Object>> estimateFpys = estimateFpyMapper.queryEstimateFpyByCondition(page,
                estimateFpyQueryParam.getProjectName(),
                estimateFpyQueryParam.getMold(),
                estimateFpyQueryParam.getCycle(),
                estimateFpyQueryParam.getFpyDateStart(),
                estimateFpyQueryParam.getFpyDateEnd());

        return estimateFpys;
    }
}
