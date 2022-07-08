package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.entity.form.ProductionActualQueryForm;
import com.aacoptics.wlg.report.entity.param.ProductionActualQueryParam;
import com.aacoptics.wlg.report.service.ProductionActualService;
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
@RequestMapping("/productionActual")
@Api("productionActual")
@Slf4j
public class ProductionActualController {

    @Autowired
    ProductionActualService productionActualService;


    @ApiOperation(value = "生产报表Excel上传", notes = "生产报表Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();

        productionActualService.importProductionActualExcel(fileName, file.getInputStream());
        return Result.success();
    }


    @ApiOperation(value = "获取生产报表", notes = "获取生产报表")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(productionActualService.getAll());
    }


    @ApiOperation(value = "搜索生产报表", notes = "根据条件搜索生产报表信息")
    @ApiImplicitParam(name = "productionActualQueryForm", value = "生产报表查询参数", required = true, dataType = "ProductionActualQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody ProductionActualQueryForm productionActualQueryForm) {
        log.debug("query with name:{}", productionActualQueryForm);
        return Result.success(productionActualService.queryProductionActualByCondition(productionActualQueryForm.getPage(),
                productionActualQueryForm.toParam(ProductionActualQueryParam.class)));
    }

    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadWorkHourRecordTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/productionActual.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("生产报表模板", "UTF-8"));
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