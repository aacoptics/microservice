package com.aacoptics.budget.report.controller;

import com.aacoptics.budget.report.entity.form.ProductLineBudgetQueryForm;
import com.aacoptics.budget.report.entity.form.ResearchBudgetQueryForm;
import com.aacoptics.budget.report.entity.param.ProductLineBudgetQueryParam;
import com.aacoptics.budget.report.entity.param.ResearchBudgetQueryParam;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.service.ProductLineBudgetService;
import com.aacoptics.budget.report.util.ExcelUtil;
import com.aacoptics.common.core.vo.Result;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productLineBudget")
@Api("productLineBudget")
@Slf4j
public class ProductLineBudgetController {

    @Autowired
    ProductLineBudgetService productLineBudgetService;


    @ApiOperation(value = "搜索上传产品线P&L预算", notes = "根据条件搜索上传产品线P&L预算信息")
    @ApiImplicitParam(name = "productLineBudgetQueryForm", value = "上传产品线P&L预算查询参数", required = true, dataType = "ProductLineBudgetQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody ProductLineBudgetQueryForm productLineBudgetQueryForm) {
        log.debug("query with name:{}", productLineBudgetQueryForm);
        return Result.success(productLineBudgetService.query(productLineBudgetQueryForm.getPage(), productLineBudgetQueryForm.toParam(ProductLineBudgetQueryParam.class)));
    }


    @ApiOperation(value = "删除上传产品线P&L预算", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "上传产品线P&L预算ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(productLineBudgetService.delete(id));
    }

    @ApiOperation(value = "获取上传产品线P&L预算", notes = "获取指定上传产品线P&L预算信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "上传产品线P&L预算ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(productLineBudgetService.get(id));
    }


    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/2023年财务产品线P&L预算模板_xx事业部_20221014 v4.xlsx");
            if(inputStream == null)
            {
                throw new BusinessException("模板不存在");
            }
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("2023年财务产品线P&L预算模板_xx事业部_20221014", "UTF-8"));
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


    @ApiOperation(value = "产品线P&L费用预算Excel上传", notes = "产品线P&L费用预算Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        String originalFilename = file.getOriginalFilename();
        productLineBudgetService.importExcel(originalFilename, file, file.getInputStream());
        return Result.success();
    }


    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PostMapping(value = "/exportExcel")
    public void exportExcel(@Valid @RequestBody ProductLineBudgetQueryForm productLineBudgetQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", productLineBudgetQueryForm);
        Map<String, Object> budgetDataMap = productLineBudgetService.query(productLineBudgetQueryForm.getPage(), productLineBudgetQueryForm.toParam(ProductLineBudgetQueryParam.class));


        JSONArray titleJsonArray = (JSONArray) budgetDataMap.get("columns");
        List<Map<String, Object>> productLineBudgetList = (List<Map<String, Object>>) budgetDataMap.get("data");

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("产品线P&L预算合计报表");

        XSSFRow titleRow = wbSheet.createRow(0);
        for (int i = 0; i < titleJsonArray.size(); i++) {
            JSONObject titleJson = titleJsonArray.getJSONObject(i);
            titleRow.createCell(i).setCellValue(titleJson.getString("label"));
        }


        try {
            if (productLineBudgetList != null && productLineBudgetList.size() > 0) {
                for (int i = 0; i < productLineBudgetList.size(); i++) {
                    Map<String, Object> productLineBudgetMap = productLineBudgetList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(i + 1);

                    for (int j = 0; j < titleJsonArray.size(); j++) {
                        String prop = titleJsonArray.getJSONObject(j).getString("prop");

                        XSSFCell dataCell = dataRow.createCell(j);
                        if (j <= 2) {
                            dataCell.setCellValue(productLineBudgetMap.get(prop) != null ? productLineBudgetMap.get(prop) + "" : "");
                        } else {
                            if("%".equals(productLineBudgetMap.get("unit")))
                            {
                                XSSFCellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
                                dataCell.setCellStyle(cellStyle);

                                if (productLineBudgetMap.containsKey(prop) && productLineBudgetMap.get(prop) != null) {
                                    dataCell.setCellValue(Double.valueOf(productLineBudgetMap.get(prop) + ""));
                                } else {
                                    dataCell.setCellType(CellType.BLANK);
                                }
                            }
                            else {
                                if (productLineBudgetMap.containsKey(prop) && productLineBudgetMap.get(prop) != null) {
                                    dataCell.setCellValue(new BigDecimal(productLineBudgetMap.get(prop) + "").setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
                                } else {
                                    dataCell.setCellType(CellType.BLANK);
                                }
                            }
                        }
                    }
                }
            }
            int[] columnWidthArray = new int[titleJsonArray.size()+1];
            for(int i=0; i<titleJsonArray.size(); i++)
            {
                if(i == 0)
                {
                    columnWidthArray[i] = 256*10;
                }else if(i == 1)
                {
                    columnWidthArray[i] = 256*25;
                }
                else
                {
                    columnWidthArray[i] = 256*15;
                }
            }
            ExcelUtil.setSheetColumnWidth(wbSheet, columnWidthArray);

        } catch (Exception exception) {
            log.error("导出产品线P&L预算合计报表异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "产品线P&L预算合计报表.xlsx");
    }

}