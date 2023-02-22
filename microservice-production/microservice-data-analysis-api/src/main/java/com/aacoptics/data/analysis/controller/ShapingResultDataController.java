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
import org.apache.commons.lang.StringUtils;
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
                queryParams.getDepartment(),
                queryParams.getLensNumber(),
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
                    row.createCell(0).setCellValue(p.getDepartment());
                    row.createCell(1).setCellValue(p.getCategory());
                    row.createCell(2).setCellValue(p.getLensNumber());
                    row.createCell(3).setCellValue(p.getProject());
                    row.createCell(4).setCellValue(p.getPartName());
                    row.createCell(5).setCellValue(p.getMaterial());
                    row.createCell(6).setCellValue(p.getMoldNo());
                    row.createCell(7).setCellValue(p.getMoldType());
                    row.createCell(8).setCellValue(p.getCoreThickness());
                    row.createCell(9).setCellValue(p.getCoreThicknessRange());
                    row.createCell(10).setCellValue(p.getR1VectorHeight());
                    row.createCell(11).setCellValue(p.getR1VectorHeightRange());
                    row.createCell(12).setCellValue(p.getR2VectorHeight());
                    row.createCell(13).setCellValue(p.getR2VectorHeightRange());
                    row.createCell(14).setCellValue(p.getOuterDiameterEcc());
                    row.createCell(15).setCellValue(p.getKanheEcc());
                    row.createCell(16).setCellValue(p.getFaceEcc());
                    row.createCell(17).setCellValue(p.getAnnealingProcess());
                    row.createCell(18).setCellValue(p.getBpKanheRoundness());
                    row.createCell(19).setCellValue(p.getDmpKanheRoundness());
                    row.createCell(20).setCellValue(p.getOuterDiameterAverage());
                    row.createCell(21).setCellValue(p.getOuterDiameterRange());
                    row.createCell(22).setCellValue(p.getOuterDiameterRoundness());
                    row.createCell(23).setCellValue(p.getOuterDiameterShrinkage());
                    row.createCell(24).setCellValue(p.getOuterDiameterRoughness());
                    row.createCell(25).setCellValue(p.getR1Flatness());
                    row.createCell(26).setCellValue(p.getR2Flatness());
                    row.createCell(27).setCellValue(p.getR1SplitAverage());
                    row.createCell(28).setCellValue(p.getR2SplitAverage());
                    row.createCell(29).setCellValue(p.getWftR1());
                    row.createCell(30).setCellValue(p.getWftR2());
                    if (StringUtils.isEmpty(p.getWftR1Pic())) {
                        row.createCell(31).setCellValue("");
                    } else {
                        row.createCell(31).setCellValue(picPath + p.getWftR1Pic().substring(0, p.getWftR1Pic().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getWftR2Pic())) {
                        row.createCell(32).setCellValue("");
                    } else {
                        row.createCell(32).setCellValue(picPath + p.getWftR2Pic().substring(0, p.getWftR2Pic().indexOf(".")));
                    }
                    row.createCell(33).setCellValue(p.getWftConsistency());
                    row.createCell(34).setCellValue(p.getWftMaxAs());
                    row.createCell(35).setCellValue(p.getWftStability());
                    row.createCell(36).setCellValue(p.getCftR1());
                    row.createCell(37).setCellValue(p.getCftR2());
                    if (StringUtils.isEmpty(p.getCftR1Pic())) {
                        row.createCell(38).setCellValue("");
                    } else {
                        row.createCell(38).setCellValue(picPath + p.getCftR1Pic().substring(0, p.getCftR1Pic().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getCftR2Pic())) {
                        row.createCell(39).setCellValue("");
                    } else {
                        row.createCell(39).setCellValue(picPath + p.getCftR2Pic().substring(0, p.getCftR2Pic().indexOf(".")));
                    }
                    row.createCell(40).setCellValue(p.getCftConsistency());
                    row.createCell(41).setCellValue(p.getCftMaxAs());
                    if (StringUtils.isEmpty(p.getCoatingTrend())) {
                        row.createCell(42).setCellValue("");
                    } else {
                        row.createCell(42).setCellValue(picPath + p.getCoatingTrend().substring(0, p.getCoatingTrend().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getCfsrR1())) {
                        row.createCell(43).setCellValue("");
                    } else {
                        row.createCell(43).setCellValue(picPath + p.getCfsrR1().substring(0, p.getCfsrR1().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getCfsrR2())) {
                        row.createCell(44).setCellValue("");
                    } else {
                        row.createCell(44).setCellValue(picPath + p.getCfsrR2().substring(0, p.getCfsrR2().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getCfsrR1R2())) {
                        row.createCell(45).setCellValue("");
                    } else {
                        row.createCell(45).setCellValue(picPath + p.getCfsrR1R2().substring(0, p.getCfsrR1R2().indexOf(".")));
                    }
                    row.createCell(46).setCellValue(p.getBurr());
                    row.createCell(47).setCellValue(p.getWeldline());
                    row.createCell(48).setCellValue(p.getAppearanceProblem());
                    if (StringUtils.isEmpty(p.getAppearanceImg())) {
                        row.createCell(49).setCellValue("");
                    } else {
                        row.createCell(49).setCellValue(picPath + p.getAppearanceImg().substring(0, p.getAppearanceImg().indexOf(".")));
                    }
                    row.createCell(50).setCellValue(p.getRemarks());
                    row.createCell(51).setCellValue(p.getAbcFilesNo());
                    row.createCell(52).setCellValue(p.getStructureNo());
                    row.createCell(53).setCellValue(p.getMoldTypeNo());
                    row.createCell(54).setCellValue(p.getMoldCost());
                    row.createCell(55).setCellValue(p.getEvtTime());
                    row.createCell(56).setCellValue(p.getDvtTime());
                    row.createCell(57).setCellValue(p.getEvtDvtTime());
                    row.createCell(58).setCellValue(p.getEvtCost());
                    row.createCell(59).setCellValue(p.getDvtCost());
                    row.createCell(60).setCellValue(p.getEvtDvtCost());
                    row.createCell(61).setCellValue(p.getProjectMassProduction());
                    row.createCell(62).setCellValue(p.getMtfAvgYield());
                    row.createCell(63).setCellValue(p.getMassProductionTime());
                    row.createCell(64).setCellValue(p.getMassProductionShipment());
                    row.createCell(65).setCellValue(p.getProjectInitiationTime());
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
        // 删除图片
        ShapingResultData srd = shapingResultDataService.getById(id);
        boolean flag = shapingResultDataService.delete(id);
        FtpUtil.connect();
        FtpUtil.changeWorkingDirectory("shapingResultData");
        FtpUtil.deleteFile(srd.getWftR1Pic().substring(0, srd.getWftR1Pic().indexOf(".")));
        FtpUtil.deleteFile(srd.getWftR2Pic().substring(0, srd.getWftR2Pic().indexOf(".")));
        FtpUtil.deleteFile(srd.getCftR1Pic().substring(0, srd.getCftR1Pic().indexOf(".")));
        FtpUtil.deleteFile(srd.getCftR2Pic().substring(0, srd.getCftR2Pic().indexOf(".")));
        FtpUtil.deleteFile(srd.getCoatingTrend().substring(0, srd.getCoatingTrend().indexOf(".")));
        FtpUtil.deleteFile(srd.getCfsrR1().substring(0, srd.getCfsrR1().indexOf(".")));
        FtpUtil.deleteFile(srd.getCfsrR2().substring(0, srd.getCfsrR2().indexOf(".")));
        FtpUtil.deleteFile(srd.getCfsrR1R2().substring(0, srd.getCfsrR1R2().indexOf(".")));
        FtpUtil.deleteFile(srd.getAppearanceImg().substring(0, srd.getAppearanceImg().indexOf(".")));
        System.out.println(srd.getAppearanceImg());
        System.out.println("删除成功。。。");
        return Result.success(flag);
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

    @ApiOperation(value = "获取事业部", notes = "获取事业部")
    @GetMapping(value = "/getDepartment")
    public Result getDepartment() {
        return Result.success(shapingResultDataService.getDepartment());
    }

    @ApiOperation(value = "获取镜片数", notes = "获取镜片数")
    @GetMapping(value = "/getLensNumber")
    public Result getLensNumber() {
        return Result.success(shapingResultDataService.getLensNumber());
    }

    @ApiOperation(value = "清空数据", notes = "清空数据")
    @DeleteMapping(value = "/deleteData")
    public Result deleteData() {
        shapingResultDataService.deleteData();
        return Result.success();
    }


}
