package com.aacoptics.wlg.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.dashboard.entity.param.CycleDetailParam;
import com.aacoptics.wlg.dashboard.entity.po.CycleDetail;
import com.aacoptics.wlg.dashboard.service.CycleDetailService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/CycleDetail")
@Api("CycleDetail")
@Slf4j
public class CycleDetailController {

    @Resource()
    CycleDetailService cycleDetailService;


    @ApiOperation(value = "修改信息", notes = "修改指定信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "cycleDetail", value = "实体", required = true, dataType = "CycleDetail")})
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody CycleDetail cycleDetail) {
        cycleDetail.setId(id);
        return Result.success(cycleDetailService.update(cycleDetail));
    }

    @ApiOperation(value = "搜索", notes = "根据条件查询信息")
    @ApiImplicitParam(name = "cycleDetailParam", value = "查询参数", required = true, dataType = "CycleDetailParam")
    @ApiResponses(@ApiResponse(code = 200, message = "处理成功", response = Result.class))
    @PostMapping(value = "/conditions")
    public Result search(@Valid @RequestBody CycleDetailParam cycleDetailParam) {
        log.debug("search with cycleDetailParam:{}", cycleDetailParam);
        return Result.success(cycleDetailService.query(cycleDetailParam.getPage(), cycleDetailParam));
    }

    @PostMapping("/downloadExcel")
    @ApiOperation(value = "下载Excel", notes = "下载Excel")
    public void downloadLocal(@RequestBody CycleDetailParam cycleDetailParam, HttpServletResponse response) {
        OutputStream os = null;
        InputStream is = null;
        try {
            File file = cycleDetailService.exportExcel(cycleDetailParam);
            log.info(file.getPath());
            String filename = file.getName();
            os = response.getOutputStream();
            response.reset();
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
            is = Files.newInputStream(file.toPath());
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
        }
    }
}