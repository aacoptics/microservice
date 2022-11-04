package com.aacoptics.data.analysis.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.ShapingResultData;
import com.aacoptics.data.analysis.exception.WlgReportErrorType;
import com.aacoptics.data.analysis.service.IShapingResultDataService;
import com.aacoptics.data.analysis.util.ExcelUtil;
import com.aacoptics.data.analysis.util.FtpUtil;
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
@RequestMapping("/shapingResultData")
@Api("ShapingResultDataController")
@Slf4j
public class ShapingResultDataController {
    @Resource
    IShapingResultDataService shapingResultDataService;

    /**
     * 导出模板
     *
     * @param response
     */
    @PostMapping("/exportTemplate")
    public void downloadShapingResultDataTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/shapingResultData.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("成型结果数据模板", "UTF-8"));
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


    @ApiOperation(value = "成型结果数据Excel上传", notes = "成型结果数据Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {
        shapingResultDataService.importShapingResultDataExcel(file.getInputStream());
        return Result.success();
    }

    /**
     * 根据条件查询条件数据
     *
     * @param queryParams
     * @return
     */
    @ApiOperation(value = "条件搜索成型结果数据", notes = "条件搜索成型结果数据")
    @PostMapping("/getDataByConditions")
    public Result getDataByConditions(@RequestBody QueryParams queryParams) {
        Integer page = queryParams.getCurrent();
        Integer size = queryParams.getSize();
        Page<ShapingResultData> iPage = new Page<>(page, size);
        IPage<ShapingResultData> res = shapingResultDataService.getDataByConditions(iPage,
                queryParams.getCategory(),
                queryParams.getProject(),
                queryParams.getPartName(),
                queryParams.getMaterial(),
                queryParams.getMoldNo(),
                queryParams.getSearchType(),
                queryParams.getStartValue(),
                queryParams.getEndValue());
        if (res.getTotal() == 0) {
            return Result.fail(WlgReportErrorType.BUSINESS_EXCEPTION, "查询数据为空！");
        }
        return Result.success(res);
    }


    @ApiOperation(value = "导出成型结果数据Excel", notes = "导出成型结果数据Excel")
    @PostMapping(value = "/exportExcel")
    public void exportShapingResultDataExcel(@RequestBody QueryParams queryParams, HttpServletResponse response) throws Exception {
        XSSFWorkbook wb = null;
        String picPath = "ftp://" + FtpUtil.getUserName() + ":" + FtpUtil.getPassword()
                + "@" + FtpUtil.getFtpHostIp() + ":" + FtpUtil.getFtpPort() + "/" + "shapingResultData/";
        try {
            // 根据查询条件获取数据
            List<ShapingResultData> datas = shapingResultDataService.getAllDataByConditions(queryParams);
            // 读取模板
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/shapingResultData.xlsx");
            wb = (XSSFWorkbook) WorkbookFactory.create(inputStream);
            XSSFSheet sheet = wb.getSheetAt(0);

            if (datas != null && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    ShapingResultData p = datas.get(i);
                    // 获取行
                    XSSFRow row = sheet.getRow(i + 3);
                    if (row == null) {
                        row = sheet.createRow(i + 3);
                    }
                    row.createCell(0).setCellValue(p.getCategory());
                    row.createCell(1).setCellValue(p.getProject());
                    row.createCell(2).setCellValue(p.getMoldNo());
                    row.createCell(3).setCellValue(p.getPartName());
                    row.createCell(4).setCellValue(p.getMaterial());
                    row.createCell(5).setCellValue(p.getCoreThickness());
                    row.createCell(6).setCellValue(p.getCoreThicknessRange());
                    row.createCell(7).setCellValue(p.getR1VectorHeight());
                    row.createCell(8).setCellValue(p.getR1VectorHeightRange());
                    row.createCell(9).setCellValue(p.getR2VectorHeight());
                    row.createCell(10).setCellValue(p.getR2VectorHeightRange());
                    row.createCell(11).setCellValue(p.getOuterDiameterEcc());
                    row.createCell(12).setCellValue(p.getKanheEcc());
                    row.createCell(13).setCellValue(p.getFaceEcc());
                    row.createCell(14).setCellValue(p.getAnnealingProcess());
                    row.createCell(15).setCellValue(p.getBpKanheRoundness());
                    row.createCell(16).setCellValue(p.getDmpKanheRoundness());
                    row.createCell(17).setCellValue(p.getOuterDiameterAverage());
                    row.createCell(18).setCellValue(p.getOuterDiameterRange());
                    row.createCell(19).setCellValue(p.getOuterDiameterRoundness());
                    row.createCell(20).setCellValue(p.getOuterDiameterShrinkage());
                    row.createCell(21).setCellValue(p.getOuterDiameterRoughness());
                    row.createCell(22).setCellValue(p.getR1Flatness());
                    row.createCell(23).setCellValue(p.getR2Flatness());
                    row.createCell(24).setCellValue(p.getR1SplitAverage());
                    row.createCell(25).setCellValue(p.getR2SplitAverage());
                    row.createCell(26).setCellValue(p.getWftR1());
                    row.createCell(27).setCellValue(p.getWftR2());
                    row.createCell(28).setCellValue(p.getWftConsistency());
                    row.createCell(29).setCellValue(p.getWftMaxAs());
                    row.createCell(30).setCellValue(p.getWftStability());
                    row.createCell(31).setCellValue(p.getCftR1());
                    row.createCell(32).setCellValue(p.getCftR2());
                    row.createCell(33).setCellValue(p.getCftConsistency());
                    row.createCell(34).setCellValue(p.getCftMaxAs());
                    row.createCell(35).setCellValue(picPath + p.getCoatingTrend().substring(0, p.getCoatingTrend().indexOf(".")));
                    row.createCell(36).setCellValue(picPath + p.getCfsrR1().substring(0, p.getCfsrR1().indexOf(".")));
                    row.createCell(37).setCellValue(picPath + p.getCfsrR2().substring(0, p.getCfsrR2().indexOf(".")));
                    row.createCell(38).setCellValue(picPath + p.getCfsrR1R2().substring(0, p.getCfsrR1R2().indexOf(".")));
                    row.createCell(39).setCellValue(p.getBurr());
                    row.createCell(40).setCellValue(p.getWeldline());
                    row.createCell(41).setCellValue(p.getAppearanceProblem());
                    row.createCell(42).setCellValue(picPath + p.getAppearanceImg().substring(0, p.getAppearanceImg().indexOf(".")));
                    row.createCell(43).setCellValue(p.getRemarks());
                }
            }

        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw e;
        }
        ExcelUtil.exportXlsx(response, wb, "成型结果数据.xlsx");
    }

    @ApiOperation(value = "删除成型结果数据", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "成型结果数据ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(shapingResultDataService.delete(id));
    }

    @ApiOperation(value = "更新成型结果数据", notes = "修改指定位置的数据")
    @PostMapping(value = "/update")
    public Result update(@RequestBody ShapingResultData shapingResultData) {
        return Result.success(shapingResultDataService.update(shapingResultData));
    }

    @ApiOperation(value = "获取类别", notes = "获取类别")
    @GetMapping(value = "/getCategory")
    public Result getCategory() {
        return Result.success(shapingResultDataService.getCategory());
    }

    @ApiOperation(value = "获取项目", notes = "获取项目")
    @GetMapping(value = "/getProject")
    public Result getProject() {
        return Result.success(shapingResultDataService.getProject());
    }

    @ApiOperation(value = "获取零件名称", notes = "获取零件名称")
    @GetMapping(value = "/getPartName")
    public Result getPartName() {
        return Result.success(shapingResultDataService.getPartName());
    }

    @ApiOperation(value = "获取材料", notes = "获取材料")
    @GetMapping(value = "/getMaterial")
    public Result getMaterial() {
        return Result.success(shapingResultDataService.getMaterial());
    }

    @ApiOperation(value = "获取模具序号", notes = "获取模具序号")
    @GetMapping(value = "/getMoldNo")
    public Result getMoldNo() {
        return Result.success(shapingResultDataService.getMoldNo());
    }

}
