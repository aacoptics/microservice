package com.aacoptics.budget.report.controller;

import com.aacoptics.budget.report.entity.form.BudgetUploadLogQueryForm;
import com.aacoptics.budget.report.entity.param.BudgetUploadLogQueryParam;
import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.aacoptics.budget.report.exception.BusinessException;
import com.aacoptics.budget.report.service.BudgetUploadLogService;
import com.aacoptics.common.core.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/budgetUploadLog")
@Api("budgetUploadLog")
@Slf4j
public class BudgetUploadLogController {

    @Autowired
    BudgetUploadLogService budgetUploadLogService;


    @ApiOperation(value = "搜索上传日志", notes = "根据条件搜索上传日志信息")
    @ApiImplicitParam(name = "budgetUploadLogQueryForm", value = "上传日志查询参数", required = true, dataType = "BudgetUploadLogQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody BudgetUploadLogQueryForm budgetUploadLogQueryForm) {
        log.debug("query with name:{}", budgetUploadLogQueryForm);
        return Result.success(budgetUploadLogService.query(budgetUploadLogQueryForm.getPage(), budgetUploadLogQueryForm.toParam(BudgetUploadLogQueryParam.class)));
    }


    @ApiOperation(value = "删除上传日志", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "上传日志ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(budgetUploadLogService.delete(id));
    }

    @ApiOperation(value = "获取上传日志", notes = "获取指定上传日志信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "上传日志ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(budgetUploadLogService.get(id));
    }


    /**
     * Excel原件下载
     * @param response
     */
    @GetMapping("/downloadExcel/{id}")
    public void downloadExcel(@PathVariable Long id, HttpServletResponse response) throws IOException {
        try {
           BudgetUploadLog budgetUploadLog = budgetUploadLogService.get(id);
           if(budgetUploadLog == null)
           {
               throw new BusinessException("文件已不存在");
           }
           byte[] excelImage = budgetUploadLog.getExcelImage();

            if(excelImage == null)
            {
                throw new BusinessException("文件已不存在");
            }
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(budgetUploadLog.getExcelName(), "UTF-8"));
            out.write(excelImage);
            out.close();
            out.flush();
        } catch (IOException e) {
            log.error("文件下载异常", e);
            throw e;
        }
    }
}