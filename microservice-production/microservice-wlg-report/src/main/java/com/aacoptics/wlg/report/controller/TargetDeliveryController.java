package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.entity.form.TargetDeliveryQueryForm;
import com.aacoptics.wlg.report.entity.param.TargetDeliveryQueryParam;
import com.aacoptics.wlg.report.service.TargetDeliveryService;
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
@RequestMapping("/targetDelivery")
@Api("targetDelivery")
@Slf4j
public class TargetDeliveryController {

    @Autowired
    TargetDeliveryService targetDeliveryService;


    @ApiOperation(value = "目标交货Excel上传", notes = "目标交货Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        targetDeliveryService.importTargetDeliveryExcel(file.getInputStream());
        return Result.success();
    }


    @ApiOperation(value = "获取目标交货", notes = "获取目标交货")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(targetDeliveryService.getAll());
    }


    @ApiOperation(value = "搜索目标交货", notes = "根据条件搜索目标交货信息")
    @ApiImplicitParam(name = "targetDeliveryQueryForm", value = "目标交货查询参数", required = true, dataType = "TargetDeliveryQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody TargetDeliveryQueryForm targetDeliveryQueryForm) {
        log.debug("query with name:{}", targetDeliveryQueryForm);
        return Result.success(targetDeliveryService.query(targetDeliveryQueryForm.getPage(), targetDeliveryQueryForm.toParam(TargetDeliveryQueryParam.class)));
    }

    @ApiOperation(value = "搜索目标交货列转列表头", notes = "搜索目标交货列转列表头")
    @ApiImplicitParam(name = "targetDeliveryQueryForm", value = "目标交货查询参数", required = true, dataType = "TargetDeliveryQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryTargetDeliveryTitleByMonth")
    public Result queryTargetDeliveryTitleByMonth(@Valid @RequestBody TargetDeliveryQueryForm targetDeliveryQueryForm) {
        log.debug("query with name:{}", targetDeliveryQueryForm);
        return Result.success(targetDeliveryService.queryTargetDeliveryTitleByMonth(targetDeliveryQueryForm.toParam(TargetDeliveryQueryParam.class)));
    }


    @ApiOperation(value = "搜索目标交货", notes = "根据条件搜索目标交货信息")
    @ApiImplicitParam(name = "targetDeliveryQueryForm", value = "目标交货查询参数", required = true, dataType = "TargetDeliveryQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryTargetDeliveryByMonth")
    public Result queryTargetDeliveryByMonth(@Valid @RequestBody TargetDeliveryQueryForm targetDeliveryQueryForm) {
        log.debug("query with name:{}", targetDeliveryQueryForm);
        return Result.success(targetDeliveryService.queryTargetDeliveryByMonth(targetDeliveryQueryForm.toParam(TargetDeliveryQueryParam.class)));
    }

    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadWorkHourRecordTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/targetDelivery.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("目标交货模板", "UTF-8"));
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