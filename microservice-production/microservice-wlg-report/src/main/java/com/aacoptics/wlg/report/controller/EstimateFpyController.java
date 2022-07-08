package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.entity.form.EstimateFpyQueryForm;
import com.aacoptics.wlg.report.entity.param.EstimateFpyQueryParam;
import com.aacoptics.wlg.report.service.EstimateFpyService;
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
@RequestMapping("/estimateFpy")
@Api("estimateFpy")
@Slf4j
public class EstimateFpyController {

    @Autowired
    EstimateFpyService estimateFpyService;


    @ApiOperation(value = "预估直通率Excel上传", notes = "预估直通率Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        estimateFpyService.importEstimateFpyExcel(file.getInputStream());
        return Result.success();
    }


    @ApiOperation(value = "获取预估直通率", notes = "获取预估直通率")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(estimateFpyService.getAll());
    }


    @ApiOperation(value = "搜索预估直通率", notes = "根据条件搜索预估直通率信息")
    @ApiImplicitParam(name = "estimateFpyQueryForm", value = "预估直通率查询参数", required = true, dataType = "EstimateFpyQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody EstimateFpyQueryForm estimateFpyQueryForm) {
        log.debug("query with name:{}", estimateFpyQueryForm);
        return Result.success(estimateFpyService.queryEstimateFpyByCondition(estimateFpyQueryForm.getPage(), estimateFpyQueryForm.toParam(EstimateFpyQueryParam.class)));
    }


    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadWorkHourRecordTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/estimateFpy.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("预估直通率模板", "UTF-8"));
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