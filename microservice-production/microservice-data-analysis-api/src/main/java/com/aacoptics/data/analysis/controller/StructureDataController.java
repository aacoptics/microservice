package com.aacoptics.data.analysis.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.aacoptics.data.analysis.exception.WlgReportErrorType;
import com.aacoptics.data.analysis.service.IStructureDataService;
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
@RequestMapping("/structureData")
@Api("StructureDataController")
@Slf4j
public class StructureDataController {

    @Resource
    IStructureDataService structureDataService;

    /**
     * 导出模板
     *
     * @param response
     */
    @PostMapping("/exportTemplate")
    public void downloadStructureDataTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/structureData.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("结构数据模板", "UTF-8"));
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

    @ApiOperation(value = "结构数据Excel上传", notes = "结构数据Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {
        structureDataService.importStructureDataExcel(file.getInputStream());
        return Result.success();
    }

    /**
     * 根据条件查询条件数据
     *
     * @param queryParams
     * @return
     */
    @ApiOperation(value = "条件搜索结构数据", notes = "条件搜索结构数据")
    @PostMapping("/getDataByConditions")
    public Result getDataByConditions(@RequestBody QueryParams queryParams) {
        Integer page = queryParams.getCurrent();
        Integer size = queryParams.getSize();
        Page<StructureData> iPage = new Page<>(page, size);
        IPage<StructureData> res = structureDataService.getDataByConditions(iPage,
                queryParams.getCategory(),
                queryParams.getProject(),
                queryParams.getPartName(),
                queryParams.getMaterial(),
                queryParams.getSearchType(),
                queryParams.getStartValue(),
                queryParams.getEndValue());
        if (res.getTotal() == 0) {
            return Result.fail(WlgReportErrorType.BUSINESS_EXCEPTION, "查询数据为空！");
        }
        return Result.success(res);
    }


    @ApiOperation(value = "导出结构数据Excel", notes = "导出结构数据Excel")
    @PostMapping(value = "/exportExcel")
    public void exportStructureDataExcel(@RequestBody QueryParams queryParams, HttpServletResponse response) throws Exception {
        XSSFWorkbook wb = null;
        String picPath = "ftp://" + FtpUtil.getUserName() + ":" + FtpUtil.getPassword()
                + "@" + FtpUtil.getFtpHostIp() + ":" + FtpUtil.getFtpPort() + "/" + "structureData/";
        try {
            // 根据查询条件获取数据
            List<StructureData> datas = structureDataService.getAllDataByConditions(queryParams);
            // 读取模板
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/structureData.xlsx");
            wb = (XSSFWorkbook) WorkbookFactory.create(inputStream);
            XSSFSheet sheet = wb.getSheetAt(0);

            if (datas != null && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    StructureData p = datas.get(i);
                    // 获取行
                    XSSFRow row = sheet.getRow(i + 3);
                    if (row == null) {
                        row = sheet.createRow(i + 3);
                    }
                    row.createCell(0).setCellValue(p.getCategory());
                    row.createCell(1).setCellValue(p.getProject());
                    row.createCell(2).setCellValue(p.getPartName());
                    row.createCell(3).setCellValue(p.getMaterial());
                    row.createCell(4).setCellValue(p.getCoreThicknessLens());
                    row.createCell(5).setCellValue(p.getMaxWallThickness());
                    row.createCell(6).setCellValue(p.getMinWallThickness());
                    row.createCell(7).setCellValue(p.getMaxCoreRatio());
                    row.createCell(8).setCellValue(p.getMaxMinRatio());
                    row.createCell(9).setCellValue(p.getOuterDiameter());
                    row.createCell(10).setCellValue(p.getEdgeThickness());
                    row.createCell(11).setCellValue(p.getWholeMinWallThickness());
                    row.createCell(12).setCellValue(p.getWholeMaxWallThickness());
                    row.createCell(13).setCellValue(p.getWholeMaxMinRatio());
                    row.createCell(14).setCellValue(p.getWholeDiameterThicknessRatio());
                    row.createCell(15).setCellValue(p.getMaxAngleR1());
                    row.createCell(16).setCellValue(p.getMaxAngleR2());
                    row.createCell(17).setCellValue(p.getR1MaxHeightDifference());
                    row.createCell(18).setCellValue(p.getR2MaxHeightDifference());
                    row.createCell(19).setCellValue(p.getR1R2Distance());
                    row.createCell(20).setCellValue(p.getMiddlePartThickness());
                    row.createCell(21).setCellValue(p.getBottomDiameterDistance());
                    row.createCell(22).setCellValue(p.getMechanismDiameterThicknessRatio());
                    row.createCell(23).setCellValue(p.getR1KanheAngle());
                    row.createCell(24).setCellValue(p.getR1KanheHeight());
                    row.createCell(25).setCellValue(p.getR2KanheAngle());
                    row.createCell(26).setCellValue(p.getR2KanheHeight());
                    row.createCell(27).setCellValue(p.getR1Srtm());
                    row.createCell(28).setCellValue(p.getR2Srtm());
                    row.createCell(29).setCellValue(p.getOuterDiameterSrtm());
                    row.createCell(30).setCellValue(picPath + p.getAssemblyDrawing().substring(0, p.getAssemblyDrawing().indexOf(".")));
                }
            }

        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw e;
        }
        ExcelUtil.exportXlsx(response, wb, "结构数据.xlsx");
    }


    @ApiOperation(value = "删除结构数据", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "结构数据ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(structureDataService.delete(id));
    }


    @ApiOperation(value = "更新结构数据", notes = "修改指定位置的数据")
    @PostMapping(value = "/update")
    public Result update(@RequestBody StructureData structureData) {
        return Result.success(structureDataService.update(structureData));
    }

    @ApiOperation(value = "获取类别", notes = "获取类别")
    @GetMapping(value = "/getCategory")
    public Result getCategory() {
        return Result.success(structureDataService.getCategory());
    }

    @ApiOperation(value = "获取项目", notes = "获取项目")
    @GetMapping(value = "/getProject")
    public Result getProject() {
        return Result.success(structureDataService.getProject());
    }

    @ApiOperation(value = "获取零件名称", notes = "获取零件名称")
    @GetMapping(value = "/getPartName")
    public Result getPartName() {
        return Result.success(structureDataService.getPartName());
    }

    @ApiOperation(value = "获取材料", notes = "获取材料")
    @GetMapping(value = "/getMaterial")
    public Result getMaterial() {
        return Result.success(structureDataService.getMaterial());
    }


}
