package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceItemForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceMainForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceQueryForm;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceItem;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderAndItemVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.service.MaintenanceItemService;
import com.aacoptics.wlg.equipment.service.MaintenanceMainService;
import com.aacoptics.wlg.equipment.util.ExcelUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/maintenanceManagement")
@Api("maintenanceManagement")
@Slf4j
public class MaintenanceController {

    @Autowired
    MaintenanceMainService maintenanceMainService;

    @Autowired
    MaintenanceItemService maintenanceItemService;


    @ApiOperation(value = "搜索保养", notes = "根据条件搜索保养信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "保养查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody MaintenanceQueryForm MaintenanceQueryForm) {
        log.debug("query with name:{}", MaintenanceQueryForm);
        return Result.success(maintenanceMainService.query(MaintenanceQueryForm.getPage(), MaintenanceQueryForm.toParam(MaintenanceQueryParam.class)));
    }

    @ApiOperation(value = "新增保养", notes = "新增一个保养信息")
    @ApiImplicitParam(name = "MaintenanceForm", value = "新增保养form表单", required = true, dataType = "MaintenanceMainForm")
    @PostMapping
    public Result add(@Valid @RequestBody MaintenanceMainForm maintenanceMainForm) {
        log.debug("name:{}", maintenanceMainForm);
        MaintenanceMain maintenanceMain = maintenanceMainForm.toPo(MaintenanceMain.class);
        return Result.success(maintenanceMainService.add(maintenanceMain));
    }

    @ApiOperation(value = "删除保养", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(maintenanceMainService.delete(id));
    }

    @ApiOperation(value = "修改保养", notes = "修改指定保养信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "保养ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "MaintenanceForm", value = "保养实体", required = true, dataType = "MaintenanceMainForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody MaintenanceMainForm maintenanceMainForm) {
        MaintenanceMain maintenanceMain = maintenanceMainForm.toPo(id, MaintenanceMain.class);
        return Result.success(maintenanceMainService.update(maintenanceMain));
    }

    @ApiOperation(value = "获取保养", notes = "获取指定保养信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(maintenanceMainService.get(id));
    }


    @ApiOperation(value = "新增保养项", notes = "新增一个保养项信息")
    @ApiImplicitParam(name = "MaintenanceItemForm", value = "新增保养form表单", required = true, dataType = "MaintenanceItemForm")
    @PostMapping(value = "/addMaintenanceItem")
    public Result addMaintenanceItem(@Valid @RequestBody MaintenanceItemForm maintenanceItemForm) {
        log.debug("name:{}", maintenanceItemForm);
        MaintenanceItem maintenanceItem = maintenanceItemForm.toPo(MaintenanceItem.class);
        return Result.success(maintenanceItemService.add(maintenanceItem));
    }

    @ApiOperation(value = "删除保养项", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteMaintenanceItem/{id}")
    public Result deleteMaintenanceItem(@PathVariable Long id) {
        return Result.success(maintenanceItemService.delete(id));
    }

    @ApiOperation(value = "修改保养", notes = "修改指定保养项信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "保养ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "MaintenanceItemForm", value = "保养项实体", required = true, dataType = "MaintenanceItemForm")
    })
    @PutMapping(value = "/updateMaintenanceItem/{id}")
    public Result updateMaintenanceItem(@PathVariable Long id, @Valid @RequestBody MaintenanceItemForm maintenanceItemForm) {
        MaintenanceItem maintenanceItem = maintenanceItemForm.toPo(id, MaintenanceItem.class);
        return Result.success(maintenanceItemService.update(maintenanceItem));
    }



    @ApiOperation(value = "导出保养维护Excel", notes = "导出保养维护Excel")
    @PostMapping(value = "/exportMaintenanceExcel")
    public void exportMaintenanceExcel(@Valid @RequestBody MaintenanceQueryForm maintenanceQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", maintenanceQueryForm);
        List<MaintenanceMain> maintenanceMainList = maintenanceMainService.queryMaintenanceDataByCondition(maintenanceQueryForm.toParam(MaintenanceQueryParam.class));

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("保养维护数据");

        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("设备名称");
        titleRow.createCell(2).setCellValue("规格");
        titleRow.createCell(3).setCellValue("型号");
        titleRow.createCell(4).setCellValue("保养周期(月)");
        titleRow.createCell(5).setCellValue("保养项");
        titleRow.createCell(6).setCellValue("保养项判断标准");
        titleRow.createCell(7).setCellValue("起始范围值");
        titleRow.createCell(8).setCellValue("截至范围值");
        titleRow.createCell(9).setCellValue("更新人");
        titleRow.createCell(10).setCellValue("更新时间");
        titleRow.createCell(11).setCellValue("创建人");
        titleRow.createCell(12).setCellValue("创建时间");

        try {
            if (maintenanceMainList != null && maintenanceMainList.size() > 0) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                int rowNumber = 1;
                for (int i = 0; i < maintenanceMainList.size(); i++) {
                    MaintenanceMain maintenanceMain = maintenanceMainList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(rowNumber++);
                    dataRow.createCell(0).setCellValue(rowNumber - 1);
                    dataRow.createCell(1).setCellValue(maintenanceMain.getMchName() != null ? maintenanceMain.getMchName() + "" : "");
                    dataRow.createCell(2).setCellValue(maintenanceMain.getSpec() != null ? maintenanceMain.getSpec() + "" : "");
                    dataRow.createCell(3).setCellValue(maintenanceMain.getTypeVersion() != null ? maintenanceMain.getTypeVersion() + "" : "");
                    dataRow.createCell(4).setCellValue(maintenanceMain.getMaintenancePeriod() != null ? maintenanceMain.getMaintenancePeriod() + "" : "");

                    List<MaintenanceItem> maintenanceItemList = maintenanceMain.getMaintenanceItemList();

                    for(int j=0; j<maintenanceItemList.size(); j++)
                    {
                        MaintenanceItem maintenanceItem = maintenanceItemList.get(j);
                        if(j != 0)
                        {
                            dataRow = wbSheet.createRow(rowNumber++);
                            dataRow.createCell(0).setCellValue(rowNumber - 1);
                        }
                        dataRow.createCell(5).setCellValue(maintenanceItem.getMaintenanceItem() != null ? maintenanceItem.getMaintenanceItem() + "" : "");
                        dataRow.createCell(6).setCellValue(maintenanceItem.getMaintenanceItemStandard() != null ? maintenanceItem.getMaintenanceItemStandard() + "" : "");
                        if(maintenanceItem.getMinValue() != null) {
                            dataRow.createCell(7).setCellValue(Double.valueOf(maintenanceItem.getMinValue() + ""));
                        }else{
                            dataRow.createCell(7).setCellType(CellType.BLANK);
                        }
                        if(maintenanceItem.getMaxValue() != null) {
                            dataRow.createCell(8).setCellValue(Double.valueOf(maintenanceItem.getMaxValue() + ""));
                        }else{
                            dataRow.createCell(8).setCellType(CellType.BLANK);
                        }
                        dataRow.createCell(9).setCellValue(maintenanceItem.getUpdatedBy() != null ? maintenanceItem.getUpdatedBy() + "" : "");
                        dataRow.createCell(10).setCellValue(maintenanceItem.getUpdatedTime() != null ? maintenanceItem.getUpdatedTime().format(dateTimeFormatter) + "" : "");
                        dataRow.createCell(11).setCellValue(maintenanceItem.getCreatedBy() != null ? maintenanceItem.getCreatedBy() + "" : "");
                        dataRow.createCell(12).setCellValue(maintenanceItem.getCreatedTime() != null ? maintenanceItem.getCreatedTime().format(dateTimeFormatter) + "" : "");
                    }

                    //合并主表单元格
                    for(int k=1; k<=4; k++) {
                        if(maintenanceItemList.size() <= 1)
                        {
                            continue;
                        }
                        ExcelUtil.mergeRegion(wbSheet, rowNumber-maintenanceItemList.size(), rowNumber-1, k, k);
                    }
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*20, 256*15, 256*20, 256*15, 256*20, 256*20, 256*15, 256*15, 256*15,
                    256*20, 256*15, 256*20});

        } catch (Exception exception)
        {
            log.error("导出保养维护数据异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "保养维护数据.xlsx");
    }



    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/maintenance.xlsx");
            if(inputStream == null)
            {
                throw new BusinessException("模板不存在");
            }
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("设备保养导入模板", "UTF-8"));
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


    @ApiOperation(value = "保养数据Excel上传", notes = "保养数据Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        maintenanceMainService.importMaintenanceExcel(file.getInputStream());
        return Result.success();
    }

}