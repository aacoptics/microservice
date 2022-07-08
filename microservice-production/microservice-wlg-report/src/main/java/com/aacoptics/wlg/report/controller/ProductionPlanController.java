package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.entity.form.ProductionPlanQueryForm;
import com.aacoptics.wlg.report.entity.param.ProductionPlanQueryParam;
import com.aacoptics.wlg.report.service.ProductionPlanService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/productionPlan")
@Api("productionPlan")
@Slf4j
public class ProductionPlanController {

    @Autowired
    ProductionPlanService productionPlanService;


    @ApiOperation(value = "生产计划Excel上传", notes = "生产计划Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        String fileName = file.getOriginalFilename();
        productionPlanService.importProductionPlanExcel(fileName, file.getInputStream());
        return Result.success();
    }


    @ApiOperation(value = "获取生产计划", notes = "获取生产计划")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(productionPlanService.getAll());
    }


    @ApiOperation(value = "搜索生产计划", notes = "根据条件搜索生产计划信息")
    @ApiImplicitParam(name = "productionPlanQueryForm", value = "生产计划查询参数", required = true, dataType = "ProductionPlanQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody ProductionPlanQueryForm productionPlanQueryForm) {
        log.debug("query with name:{}", productionPlanQueryForm);
        return Result.success(productionPlanService.query(productionPlanQueryForm.getPage(), productionPlanQueryForm.toParam(ProductionPlanQueryParam.class)));
    }

    @ApiOperation(value = "搜索生产计划列转列表头", notes = "搜索生产计划列转列表头")
    @ApiImplicitParam(name = "productionPlanQueryForm", value = "生产计划查询参数", required = true, dataType = "ProductionPlanQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryProductionPlanTitleByMonth")
    public Result queryProductionPlanTitleByMonth(@Valid @RequestBody ProductionPlanQueryForm productionPlanQueryForm) {
        log.debug("query with name:{}", productionPlanQueryForm);
        return Result.success(productionPlanService.queryProductionPlanTitleByMonth(productionPlanQueryForm.toParam(ProductionPlanQueryParam.class)));
    }


    @ApiOperation(value = "搜索生产计划", notes = "根据条件搜索生产计划信息")
    @ApiImplicitParam(name = "productionPlanQueryForm", value = "生产计划查询参数", required = true, dataType = "ProductionPlanQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryProductionPlanByMonth")
    public Result queryProductionPlanByMonth(@Valid @RequestBody ProductionPlanQueryForm productionPlanQueryForm) {
        log.debug("query with name:{}", productionPlanQueryForm);
        return Result.success(productionPlanService.queryProductionPlanByMonth(productionPlanQueryForm.toParam(ProductionPlanQueryParam.class)));
    }

    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadWorkHourRecordTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/productionPlan.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("生产计划模板", "UTF-8"));
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
}