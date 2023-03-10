package com.aacoptics.budget.report.controller;

import com.aacoptics.budget.report.entity.form.ResearchBudgetQueryForm;
import com.aacoptics.budget.report.entity.param.ResearchBudgetQueryParam;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.service.ResearchBudgetService;
import com.aacoptics.budget.report.util.ExcelUtil;
import com.aacoptics.common.core.vo.Result;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/researchBudget")
@Api("researchBudget")
@Slf4j
public class ResearchBudgetController {

    @Autowired
    ResearchBudgetService researchBudgetService;


    @ApiOperation(value = "搜索上传研发预算", notes = "根据条件搜索上传研发预算信息")
    @ApiImplicitParam(name = "researchBudgetQueryForm", value = "上传研发预算查询参数", required = true, dataType = "ResearchBudgetQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody ResearchBudgetQueryForm researchBudgetQueryForm) {
        log.debug("query with name:{}", researchBudgetQueryForm);
        return Result.success(researchBudgetService.query(researchBudgetQueryForm.getPage(), researchBudgetQueryForm.toParam(ResearchBudgetQueryParam.class)));
    }


    @ApiOperation(value = "删除上传研发预算", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "上传研发预算ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(researchBudgetService.delete(id));
    }

    @ApiOperation(value = "获取上传研发预算", notes = "获取指定上传研发预算信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "上传研发预算ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(researchBudgetService.get(id));
    }


    /**
     * Excel模板下载
     *
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/2023年研发费用预算模版_xx事业部_20221014 v4.xlsx");
            if (inputStream == null) {
                throw new BusinessException("模板不存在");
            }
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("2023年研发费用预算模版_xx事业部_20221014", "UTF-8"));
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


    @ApiOperation(value = "研发费用预算Excel上传", notes = "研发费用预算Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        String originalFilename = file.getOriginalFilename();
        researchBudgetService.importExcel(originalFilename, file, file.getInputStream());
        return Result.success();
    }


    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PostMapping(value = "/exportExcel")
    public void exportExcel(@Valid @RequestBody ResearchBudgetQueryForm researchBudgetQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", researchBudgetQueryForm);
        Map<String, Object> budgetDataMap = researchBudgetService.query(researchBudgetQueryForm.getPage(), researchBudgetQueryForm.toParam(ResearchBudgetQueryParam.class));


        JSONArray titleJsonArray = (JSONArray) budgetDataMap.get("columns");
        List<Map<String, Object>> researchBudgetList = (List<Map<String, Object>>) budgetDataMap.get("data");

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("研发费用预算合计报表");

        XSSFRow titleRow = wbSheet.createRow(0);
        for (int i = 0; i < titleJsonArray.size(); i++) {
            JSONObject titleJson = titleJsonArray.getJSONObject(i);
            titleRow.createCell(i).setCellValue(titleJson.getString("label"));
        }

        try {
            if (researchBudgetList != null && researchBudgetList.size() > 0) {
                for (int i = 0; i < researchBudgetList.size(); i++) {
                    Map<String, Object> researchBudgetMap = researchBudgetList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(i + 1);

                    for (int j = 0; j < titleJsonArray.size(); j++) {
                        String prop = titleJsonArray.getJSONObject(j).getString("prop");

                        if (j <= 4) {
                            dataRow.createCell(j).setCellValue(researchBudgetMap.get(prop) != null ? researchBudgetMap.get(prop) + "" : "");
                        } else {
                            if (researchBudgetMap.containsKey(prop) && researchBudgetMap.get(prop) != null) {
                                dataRow.createCell(j).setCellValue(Double.valueOf(researchBudgetMap.get(prop) + ""));
                            } else {
                                dataRow.createCell(j).setCellType(CellType.BLANK);
                            }
                        }
                    }
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[]{256 * 10, 256 * 15, 256 * 25, 256 * 15, 256 * 10});

        } catch (Exception exception) {
            log.error("导出研发费用预算合计报表异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "研发费用预算合计报表.xlsx");
    }

}