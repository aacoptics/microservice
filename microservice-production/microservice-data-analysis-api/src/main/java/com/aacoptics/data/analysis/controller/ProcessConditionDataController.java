package com.aacoptics.data.analysis.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.ProcessConditionData;
import com.aacoptics.data.analysis.exception.WlgReportErrorType;
import com.aacoptics.data.analysis.service.IProcessConditionDataService;
import com.aacoptics.data.analysis.util.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/processConditionData")
@Api("ProcessConditionDataController")
@Slf4j
public class ProcessConditionDataController {

    @Resource
    IProcessConditionDataService processConditionDataService;

    /**
     * 导出模板
     *
     * @param response
     */
    @PostMapping("/exportTemplate")
    public void downloadProcessConditionDataTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/processConditionData.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("工艺条件数据模板", "UTF-8"));
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) {
                    out.write(buffer, 0, b);
                }
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (IOException e) {
            log.error("模板下载异常", e);
            throw e;
        }
    }

    @ApiOperation(value = "工艺条件数据Excel上传", notes = "工艺条件数据Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {
        processConditionDataService.importProcessConditionDataExcel(file.getInputStream());
        return Result.success();
    }

    /**
     * 根据条件查询条件数据
     *
     * @return
     */
    @ApiOperation(value = "条件搜索工艺条件数据", notes = "条件搜索工艺条件数据")
    @PostMapping("/getDataByConditions")
    public Result getDataByConditions(@RequestBody QueryParams queryParams) {
        Integer page = queryParams.getCurrent();
        Integer size = queryParams.getSize();
        Page<ProcessConditionData> iPage = new Page<>(page, size);
        IPage<ProcessConditionData> res = processConditionDataService.getDataByConditions(iPage,
                queryParams.getCategory(),
                queryParams.getProject(),
                queryParams.getPartName(),
                queryParams.getMaterial(),
                queryParams.getMoldNo());
        if (res.getTotal() == 0) {
            return Result.fail(WlgReportErrorType.BUSINESS_EXCEPTION, "查询数据为空！");
        }
        return Result.success(res);
    }


    @ApiOperation(value = "导出工艺条件数据Excel", notes = "导出工艺条件数据Excel")
    @PostMapping(value = "/exportExcel")
    public void exportProcessConditionDataExcel(@RequestBody QueryParams queryParams, HttpServletResponse response) throws Exception {
        XSSFWorkbook wb = null;
        try {
            // 根据查询条件获取数据
            List<ProcessConditionData> datas = processConditionDataService.getAllDataByConditions(queryParams);
            // 读取模板
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/processConditionData.xlsx");
            wb = (XSSFWorkbook) WorkbookFactory.create(inputStream);
            XSSFSheet sheet = wb.getSheetAt(0);

            if (datas != null && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    ProcessConditionData p = datas.get(i);
                    // 获取行
                    XSSFRow row = sheet.getRow(i + 4);
                    if (row == null) {
                        row = sheet.createRow(i + 4);
                    }
                    row.createCell(0).setCellValue(p.getCategory());
                    row.createCell(1).setCellValue(p.getProject());
                    row.createCell(2).setCellValue(p.getMoldNo());
                    row.createCell(3).setCellValue(p.getPartName());
                    row.createCell(4).setCellValue(p.getMaterial());

                    row.createCell(5).setCellValue(p.getMfMoldTemp());
                    row.createCell(6).setCellValue(p.getMfMaterialTemp());
                    row.createCell(7).setCellValue(p.getMfJetVelocity());
                    row.createCell(8).setCellValue(p.getMfVpSwitch());
                    row.createCell(9).setCellValue(p.getMfHoldPressure1());
                    row.createCell(10).setCellValue(p.getMfHoldTime1());
                    row.createCell(11).setCellValue(p.getMfHoldPressure2());
                    row.createCell(12).setCellValue(p.getMfHoldTime2());
                    row.createCell(13).setCellValue(p.getMfHoldPressure3());
                    row.createCell(14).setCellValue(p.getMfHoldTime3());
                    row.createCell(15).setCellValue(p.getMfHoldPressure4());
                    row.createCell(16).setCellValue(p.getMfHoldTime4());
                    row.createCell(17).setCellValue(p.getMfHoldPressure5());
                    row.createCell(18).setCellValue(p.getMfHoldTime5());
                    row.createCell(19).setCellValue(p.getMfHoldPressure6());
                    row.createCell(20).setCellValue(p.getMfHoldTime6());


                    row.createCell(21).setCellValue(p.getMoldTemp());
                    row.createCell(22).setCellValue(p.getMaterialTemp());
                    row.createCell(23).setCellValue(p.getJetVelocity());
                    row.createCell(24).setCellValue(p.getVpSwitch());
                    row.createCell(25).setCellValue(p.getHoldPressure1());
                    row.createCell(26).setCellValue(p.getHoldTime1());
                    row.createCell(27).setCellValue(p.getHoldPressure2());
                    row.createCell(28).setCellValue(p.getHoldTime2());
                    row.createCell(29).setCellValue(p.getHoldPressure3());
                    row.createCell(30).setCellValue(p.getHoldTime3());
                    row.createCell(31).setCellValue(p.getHoldPressure4());
                    row.createCell(32).setCellValue(p.getHoldTime4());
                    row.createCell(33).setCellValue(p.getHoldPressure5());
                    row.createCell(34).setCellValue(p.getHoldTime5());
                    row.createCell(35).setCellValue(p.getHoldPressure6());
                    row.createCell(36).setCellValue(p.getHoldTime6());
                    row.createCell(37).setCellValue(p.getHoldPressureVelocity());
                    row.createCell(38).setCellValue(p.getPlatenPosition());
                    row.createCell(39).setCellValue(p.getOpeningSpeed());
                    row.createCell(40).setCellValue(p.getEjectionSpeed());
                    row.createCell(41).setCellValue(p.getCoolingTime());
                    row.createCell(42).setCellValue(p.getClampingForce());
                    row.createCell(43).setCellValue(p.getPassivation());
                }
            }

        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw e;
        }
        ExcelUtil.exportXlsx(response, wb, "工艺条件数据.xlsx");
    }


    @ApiOperation(value = "删除工艺条件数据", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "工艺条件数据ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(processConditionDataService.delete(id));
    }

    @ApiOperation(value = "更新工艺条件数据", notes = "修改指定位置的数据")
    @PostMapping(value = "/update")
    public Result update(@RequestBody ProcessConditionData processConditionData) {
        return Result.success(processConditionDataService.update(processConditionData));
    }

}
