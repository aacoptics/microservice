package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.entity.form.DownloadTemplateForm;
import com.aacoptics.wlg.report.entity.form.ProductionSummaryForm;
import com.aacoptics.wlg.report.entity.form.ProductionSummaryQueryForm;
import com.aacoptics.wlg.report.entity.param.ProductionSummaryQueryParam;
import com.aacoptics.wlg.report.entity.po.ProductionSummary;
import com.aacoptics.wlg.report.service.ProductionSummaryService;
import com.alibaba.fastjson.JSONArray;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productionSummary")
@Api("productionSummary")
@Slf4j
public class ProductionSummaryController {

    @Autowired
    ProductionSummaryService productionSummaryService;


    @ApiOperation(value = "生产汇总Excel上传", notes = "生产汇总Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        productionSummaryService.importProductionSummaryExcel(file.getInputStream());
        return Result.success();
    }


    @ApiOperation(value = "获取生产汇总", notes = "获取生产汇总")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(productionSummaryService.getAll());
    }


    @ApiOperation(value = "搜索生产汇总", notes = "根据条件搜索生产汇总信息")
    @ApiImplicitParam(name = "productionSummaryQueryForm", value = "生产汇总查询参数", required = true, dataType = "ProductionSummaryQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody ProductionSummaryQueryForm productionSummaryQueryForm) {
        log.debug("query with name:{}", productionSummaryQueryForm);
        List<Map<String, Object>> queryResult = productionSummaryService.queryProductionSummaryByCondition(productionSummaryQueryForm.getPage(), productionSummaryQueryForm.toParam(ProductionSummaryQueryParam.class));
        JSONArray titleResult = productionSummaryService.queryProductionSummaryTitleByMonth(productionSummaryQueryForm.toParam(ProductionSummaryQueryParam.class));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("queryResult", queryResult);
        resultMap.put("titleResult", titleResult);
        return Result.success(resultMap);
    }


    /**
     * Excel模板下载
     * @param response
     */
    @PostMapping("/downloadTemplate")
    public void downloadWorkHourRecordTemplate(HttpServletResponse response, @RequestBody DownloadTemplateForm downloadTemplateForm) throws IOException {
        try {
            InputStream inputStream = null;
            String type = downloadTemplateForm.getType();
            if("actual".equals(type))
            {
                inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/productionActualSummary.xlsx");
            }
            else
            {
                inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/productionTargetSummary.xlsx");
            }
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("生产汇总模板", "UTF-8"));
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



    @ApiOperation(value = "新增生产汇总", notes = "新增一个生产汇总信息")
    @ApiImplicitParam(name = "productionSummaryForm", value = "新增生产汇总form表单", required = true, dataType = "ProductionSummaryForm")
    @PostMapping
    public Result add(@Valid @RequestBody ProductionSummaryForm productionSummaryForm) {
        log.debug("name:{}", productionSummaryForm);
        ProductionSummary personPlacementDay = productionSummaryForm.toPo(ProductionSummary.class);
        return Result.success(productionSummaryService.add(personPlacementDay));
    }

    @ApiOperation(value = "删除生产汇总", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "生产汇总ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(productionSummaryService.delete(id));
    }

    @ApiOperation(value = "修改生产汇总", notes = "修改指定生产汇总信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "生产汇总ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "productionSummaryForm", value = "生产汇总实体", required = true, dataType = "ProductionSummaryForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody ProductionSummaryForm productionSummaryForm) {
        ProductionSummary personPlacementDay = productionSummaryForm.toPo(id, ProductionSummary.class);
        return Result.success(productionSummaryService.update(personPlacementDay));
    }

    @ApiOperation(value = "获取生产汇总", notes = "获取指定生产汇总信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "生产汇总ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(productionSummaryService.get(id));
    }
}