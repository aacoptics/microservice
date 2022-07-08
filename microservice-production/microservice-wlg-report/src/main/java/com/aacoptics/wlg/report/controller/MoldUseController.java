package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.entity.form.MoldUseQueryForm;
import com.aacoptics.wlg.report.entity.param.MoldUseQueryParam;
import com.aacoptics.wlg.report.service.MoldUseService;
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
@RequestMapping("/moldUse")
@Api("moldUse")
@Slf4j
public class MoldUseController {

    @Autowired
    MoldUseService moldUseService;


    @ApiOperation(value = "模具使用情况Excel上传", notes = "模具使用情况Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        moldUseService.importMoldUseExcel(file.getInputStream());
        return Result.success();
    }


    @ApiOperation(value = "获取模具使用情况", notes = "获取模具使用情况")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(moldUseService.getAll());
    }


    @ApiOperation(value = "搜索模具使用情况", notes = "根据条件搜索模具使用情况信息")
    @ApiImplicitParam(name = "moldUseQueryForm", value = "模具使用情况查询参数", required = true, dataType = "MoldUseQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody MoldUseQueryForm moldUseQueryForm) {
        log.debug("query with name:{}", moldUseQueryForm);
        return Result.success(moldUseService.query(moldUseQueryForm.getPage(), moldUseQueryForm.toParam(MoldUseQueryParam.class)));
    }

    @ApiOperation(value = "搜索模具使用情况列转列表头", notes = "搜索模具使用情况列转列表头")
    @ApiImplicitParam(name = "moldUseQueryForm", value = "模具使用情况查询参数", required = true, dataType = "MoldUseQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryMoldUseTitleByMonth")
    public Result queryMoldUseTitleByMonth(@Valid @RequestBody MoldUseQueryForm moldUseQueryForm) {
        log.debug("query with name:{}", moldUseQueryForm);
        return Result.success(moldUseService.queryMoldUseTitleByMonth(moldUseQueryForm.toParam(MoldUseQueryParam.class)));
    }


    @ApiOperation(value = "搜索模具使用情况", notes = "根据条件搜索模具使用情况信息")
    @ApiImplicitParam(name = "moldUseQueryForm", value = "模具使用情况查询参数", required = true, dataType = "MoldUseQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryMoldUseByMonth")
    public Result queryMoldUseByMonth(@Valid @RequestBody MoldUseQueryForm moldUseQueryForm) {
        log.debug("query with name:{}", moldUseQueryForm);
        return Result.success(moldUseService.queryMoldUseByMonth(moldUseQueryForm.toParam(MoldUseQueryParam.class)));
    }

    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadWorkHourRecordTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/moldUse.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("模具使用情况模板", "UTF-8"));
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