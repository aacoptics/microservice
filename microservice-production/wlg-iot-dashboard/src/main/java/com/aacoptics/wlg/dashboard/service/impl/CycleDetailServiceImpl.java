package com.aacoptics.wlg.dashboard.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.aacoptics.wlg.dashboard.entity.param.CycleDetailParam;
import com.aacoptics.wlg.dashboard.entity.po.CycleDetail;
import com.aacoptics.wlg.dashboard.entity.po.InputReport;
import com.aacoptics.wlg.dashboard.mapper.CycleDetailMapper;
import com.aacoptics.wlg.dashboard.service.CycleDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableList;
import com.spire.xls.Worksheet;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aacoptics.wlg.dashboard.util.ExcelUtil.objectToExcelExportParam;
import static com.aacoptics.wlg.dashboard.util.ExcelUtil.updateBorder;

@Service
@Slf4j
public class CycleDetailServiceImpl extends ServiceImpl<CycleDetailMapper, CycleDetail> implements CycleDetailService {

    @Override
    public boolean update(CycleDetail cycleDetail) {
        return this.updateById(cycleDetail);
    }


    @Override
    public IPage<CycleDetail> query(Page page, CycleDetailParam cycleDetailParam) {
        QueryWrapper<CycleDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(null != cycleDetailParam.getMachineNames() && cycleDetailParam.getMachineNames().size() > 0, "machine_name", cycleDetailParam.getMachineNames());
        queryWrapper.ge(null != cycleDetailParam.getStartTime(), "start_time", cycleDetailParam.getStartTime())
                .le(null != cycleDetailParam.getEndTime(), "start_time", cycleDetailParam.getEndTime())
                .orderByDesc("id");
        return this.page(page, queryWrapper);
    }

    @Override
    public String exportExcel(CycleDetailParam cycleDetailParam) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        long currentTimeMillis = System.currentTimeMillis();
        String fileName = "wlg-cycle_detail-" + currentTimeMillis + ".xlsx";
        QueryWrapper<CycleDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(null != cycleDetailParam.getMachineNames() && cycleDetailParam.getMachineNames().size() > 0, "machine_name", cycleDetailParam.getMachineNames());
        queryWrapper.ge(null != cycleDetailParam.getStartTime(), "start_time", cycleDetailParam.getStartTime())
                .le(null != cycleDetailParam.getEndTime(), "start_time", cycleDetailParam.getEndTime());
        List<CycleDetail> cycleDetails = this.list(queryWrapper);
        if(cycleDetails.size() > 0){
            List<Map<String, Object>> exportParamList = new ArrayList<>();
            exportParamList.add(objectToExcelExportParam("模造机模次明细", "模造机模次明细", cycleDetails, CycleDetail.class));
            Workbook workbook = ExcelExportUtil.exportExcel(exportParamList, ExcelType.XSSF); // 写入workbook
            updateBorder(workbook);
            FileOutputStream out = new FileOutputStream(tempDir + "/" + fileName);
            workbook.write(out);
            out.close();
            workbook.close();
            return tempDir + "/" + fileName;
        }
        return null;
    }
}