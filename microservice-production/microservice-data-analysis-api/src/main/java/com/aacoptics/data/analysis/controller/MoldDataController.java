package com.aacoptics.data.analysis.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.MoldData;
import com.aacoptics.data.analysis.exception.WlgReportErrorType;
import com.aacoptics.data.analysis.service.IMoldDataService;
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
@RequestMapping("/moldData")
@Api("MoldDataController")
@Slf4j
public class MoldDataController {

    @Resource
    IMoldDataService moldDataService;

    /**
     * 导出模板
     *
     * @param response
     */
    @PostMapping("/exportTemplate")
    public void downloadMoldDataTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/moldData.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("模具数据模板", "UTF-8"));
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

    @ApiOperation(value = "模具数据Excel上传", notes = "模具数据Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {
        moldDataService.importMoldDataExcel(file.getInputStream());
        return Result.success();
    }

    /**
     * 根据条件查询条件数据
     *
     * @param queryParams
     * @return
     */
    @ApiOperation(value = "条件搜索模具数据", notes = "条件搜索模具数据")
    @PostMapping("/getDataByConditions")
    public Result getDataByConditions(@RequestBody QueryParams queryParams) {
        Integer page = queryParams.getCurrent();
        Integer size = queryParams.getSize();
        Page<MoldData> iPage = new Page<>(page, size);
        IPage<MoldData> res = moldDataService.getDataByConditions(iPage,
                queryParams.getCategory(),
                queryParams.getProject(),
                queryParams.getPartName(),
                queryParams.getMaterial());
        if (res.getTotal() == 0) {
            return Result.fail(WlgReportErrorType.BUSINESS_EXCEPTION, "查询数据为空！");
        }
        return Result.success(res);
    }


    @ApiOperation(value = "导出模具数据Excel", notes = "导出模具数据Excel")
    @PostMapping(value = "/exportExcel")
    public void exportMoldDataExcel(@RequestBody QueryParams queryParams, HttpServletResponse response) throws Exception {
        XSSFWorkbook wb = null;
        try {
            // 根据查询条件获取数据
            List<MoldData> datas = moldDataService.getAllDataByConditions(queryParams);
            // 读取模板
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/moldData.xlsx");
            wb = (XSSFWorkbook) WorkbookFactory.create(inputStream);
            XSSFSheet sheet = wb.getSheetAt(0);

            if (datas != null && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    MoldData p = datas.get(i);
                    // 获取行
                    XSSFRow row = sheet.getRow(i + 3);
                    if (row == null) {
                        row = sheet.createRow(i + 3);
                    }
                    row.createCell(0).setCellValue(p.getCategory());
                    row.createCell(1).setCellValue(p.getProject());
                    row.createCell(2).setCellValue(p.getPartName());
                    row.createCell(3).setCellValue(p.getMaterial());
                    row.createCell(4).setCellValue(p.getMoldType());
                    row.createCell(5).setCellValue(p.getMoldCorePassivation());
                    row.createCell(6).setCellValue(p.getRunnerType());
                    row.createCell(7).setCellValue(p.getFirstRunner());
                    row.createCell(8).setCellValue(p.getSecondRunner());
                    row.createCell(9).setCellValue(p.getThirdRunner());
                    row.createCell(10).setCellValue(p.getPartingSurface());
                    row.createCell(11).setCellValue(p.getSplitPosition());
                    row.createCell(12).setCellValue(p.getGateType());
                    row.createCell(13).setCellValue(p.getGateWidth());
                    row.createCell(14).setCellValue(p.getGateThickness());
                    row.createCell(15).setCellValue(p.getGateR1Thickness());
                    row.createCell(16).setCellValue(p.getGateR2Thickness());
                    row.createCell(17).setCellValue(p.getMoldOpeningType());
                }
            }

        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw e;
        }
        ExcelUtil.exportXlsx(response, wb, "模具数据.xlsx");
    }


    @ApiOperation(value = "删除模具数据", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "结构数据ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(moldDataService.delete(id));
    }


    @ApiOperation(value = "更新模具数据", notes = "修改指定位置的数据")
    @PostMapping(value = "/update")
    public Result update(@RequestBody MoldData moldData) {
        return Result.success(moldDataService.update(moldData));
    }

    @ApiOperation(value = "获取类别", notes = "获取类别")
    @GetMapping(value = "/getCategory")
    public Result getCategory() {
        return Result.success(moldDataService.getCategory());
    }

    @ApiOperation(value = "获取项目", notes = "获取项目")
    @GetMapping(value = "/getProject")
    public Result getProject() {
        return Result.success(moldDataService.getProject());
    }

    @ApiOperation(value = "获取零件名称", notes = "获取零件名称")
    @GetMapping(value = "/getPartName")
    public Result getPartName() {
        return Result.success(moldDataService.getPartName());
    }

    @ApiOperation(value = "获取材料", notes = "获取材料")
    @GetMapping(value = "/getMaterial")
    public Result getMaterial() {
        return Result.success(moldDataService.getMaterial());
    }
}
