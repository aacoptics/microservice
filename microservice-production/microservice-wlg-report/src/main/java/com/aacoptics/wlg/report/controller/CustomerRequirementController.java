package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.entity.form.CustomerRequirementForm;
import com.aacoptics.wlg.report.entity.form.CustomerRequirementQueryForm;
import com.aacoptics.wlg.report.entity.param.CustomerRequirementQueryParam;
import com.aacoptics.wlg.report.entity.po.CustomerRequirement;
import com.aacoptics.wlg.report.service.CustomerRequirementService;
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
@RequestMapping("/customerRequirement")
@Api("customerRequirement")
@Slf4j
public class CustomerRequirementController {

    @Autowired
    CustomerRequirementService customerRequirementService;


    @ApiOperation(value = "客户需求Excel上传", notes = "客户需求Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        customerRequirementService.importCustomerRequirementExcel(file.getInputStream());
        return Result.success();
    }


    @ApiOperation(value = "获取客户需求", notes = "获取客户需求")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(customerRequirementService.getAll());
    }


    @ApiOperation(value = "搜索客户需求", notes = "根据条件搜索客户需求信息")
    @ApiImplicitParam(name = "customerRequirementQueryForm", value = "客户需求查询参数", required = true, dataType = "CustomerRequirementQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody CustomerRequirementQueryForm customerRequirementQueryForm) {
        log.debug("query with name:{}", customerRequirementQueryForm);
        return Result.success(customerRequirementService.queryCustomerRequirementByCondition(customerRequirementQueryForm.getPage(), customerRequirementQueryForm.toParam(CustomerRequirementQueryParam.class)));
    }


    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadWorkHourRecordTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/customerRequirement.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("客户需求模板", "UTF-8"));
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



    @ApiOperation(value = "新增客户需求", notes = "新增一个客户需求信息")
    @ApiImplicitParam(name = "customerRequirementForm", value = "新增客户需求form表单", required = true, dataType = "CustomerRequirementForm")
    @PostMapping
    public Result add(@Valid @RequestBody CustomerRequirementForm customerRequirementForm) {
        log.debug("name:{}", customerRequirementForm);
        CustomerRequirement personPlacementDay = customerRequirementForm.toPo(CustomerRequirement.class);
        return Result.success(customerRequirementService.add(personPlacementDay));
    }

    @ApiOperation(value = "删除客户需求", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "客户需求ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(customerRequirementService.delete(id));
    }

    @ApiOperation(value = "修改客户需求", notes = "修改指定客户需求信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "客户需求ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "customerRequirementForm", value = "客户需求实体", required = true, dataType = "CustomerRequirementForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody CustomerRequirementForm customerRequirementForm) {
        CustomerRequirement personPlacementDay = customerRequirementForm.toPo(id, CustomerRequirement.class);
        return Result.success(customerRequirementService.update(personPlacementDay));
    }

    @ApiOperation(value = "获取客户需求", notes = "获取指定客户需求信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "客户需求ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(customerRequirementService.get(id));
    }
}