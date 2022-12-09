package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.DataDictConstants;
import com.aacoptics.wlg.equipment.entity.form.*;
import com.aacoptics.wlg.equipment.entity.param.InspectionQueryParam;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.provider.DataDictProvider;
import com.aacoptics.wlg.equipment.service.InspectionItemService;
import com.aacoptics.wlg.equipment.service.InspectionMainService;
import com.aacoptics.wlg.equipment.service.InspectionShiftService;
import com.aacoptics.wlg.equipment.util.DataDictUtil;
import com.aacoptics.wlg.equipment.util.ExcelUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/inspectionManagement")
@Api("inspectionManagement")
@Slf4j
public class InspectionController {

    @Autowired
    InspectionMainService inspectionMainService;

    @Autowired
    InspectionItemService inspectionItemService;

    @Autowired
    InspectionShiftService inspectionShiftService;

    @Autowired
    DataDictProvider dataDictProvider;

    @ApiOperation(value = "搜索点检", notes = "根据条件搜索点检信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "点检查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody InspectionQueryForm inspectionQueryForm) {
        log.debug("query with name:{}", inspectionQueryForm);
        return Result.success(inspectionMainService.query(inspectionQueryForm.getPage(), inspectionQueryForm.toParam(InspectionQueryParam.class)));
    }

    @ApiOperation(value = "新增点检", notes = "新增一个点检信息")
    @ApiImplicitParam(name = "InspectionForm", value = "新增点检form表单", required = true, dataType = "InspectionMainForm")
    @PostMapping
    public Result add(@Valid @RequestBody InspectionMainForm inspectionMainForm) {
        log.debug("name:{}", inspectionMainForm);
        InspectionMain inspectionMain = inspectionMainForm.toPo(InspectionMain.class);
        return Result.success(inspectionMainService.add(inspectionMain));
    }

    @ApiOperation(value = "删除点检", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(inspectionMainService.delete(id));
    }

    @ApiOperation(value = "修改点检", notes = "修改指定点检信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "点检ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionForm", value = "点检实体", required = true, dataType = "InspectionMainForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody InspectionMainForm inspectionMainForm) {
        InspectionMain inspectionMain = inspectionMainForm.toPo(id, InspectionMain.class);
        return Result.success(inspectionMainService.update(inspectionMain));
    }

    @ApiOperation(value = "获取点检", notes = "获取指定点检信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(inspectionMainService.get(id));
    }


    @ApiOperation(value = "新增点检项", notes = "新增一个点检项信息")
    @ApiImplicitParam(name = "InspectionItemForm", value = "新增点检form表单", required = true, dataType = "InspectionItemForm")
    @PostMapping(value = "/addInspectionItem")
    public Result addInspectionItem(@Valid @RequestBody InspectionItemForm inspectionItemForm) {
        log.debug("name:{}", inspectionItemForm);
        InspectionItem inspectionItem = inspectionItemForm.toPo(InspectionItem.class);
        return Result.success(inspectionItemService.add(inspectionItem));
    }

    @ApiOperation(value = "删除点检项", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteInspectionItem/{id}")
    public Result deleteInspectionItem(@PathVariable Long id) {
        return Result.success(inspectionItemService.delete(id));
    }

    @ApiOperation(value = "修改点检", notes = "修改指定点检项信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "点检ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionItemForm", value = "点检项实体", required = true, dataType = "InspectionItemForm")
    })
    @PutMapping(value = "/updateInspectionItem/{id}")
    public Result updateInspectionItem(@PathVariable Long id, @Valid @RequestBody InspectionItemForm inspectionItemForm) {
        InspectionItem inspectionItem = inspectionItemForm.toPo(id, InspectionItem.class);
        return Result.success(inspectionItemService.update(inspectionItem));
    }


    @ApiOperation(value = "新增点检班次", notes = "新增一个点检班次信息")
    @ApiImplicitParam(name = "InspectionItemForm", value = "新增点检form表单", required = true, dataType = "InspectionItemForm")
    @PostMapping(value = "/addInspectionShift")
    public Result addInspectionShift(@Valid @RequestBody InspectionShiftForm inspectionShiftForm) {
        log.debug("name:{}", inspectionShiftForm);
        InspectionShift inspectionShift = inspectionShiftForm.toPo(InspectionShift.class);
        return Result.success(inspectionShiftService.add(inspectionShift));
    }

    @ApiOperation(value = "删除点检班次", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteInspectionShift/{id}")
    public Result deleteInspectionShift(@PathVariable Long id) {
        return Result.success(inspectionShiftService.delete(id));
    }

    @ApiOperation(value = "修改点检班次", notes = "修改指定点检班次信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "点检ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionItemForm", value = "点检班次实体", required = true, dataType = "InspectionItemForm")
    })
    @PutMapping(value = "/updateInspectionShift/{id}")
    public Result updateInspectionShift(@PathVariable Long id, @Valid @RequestBody InspectionShiftForm inspectionShiftForm) {
        InspectionShift inspectionShift = inspectionShiftForm.toPo(id, InspectionShift.class);
        return Result.success(inspectionShiftService.update(inspectionShift));
    }


    @ApiOperation(value = "导出点检维护Excel", notes = "导出点检维护Excel")
    @PostMapping(value = "/exportInspectionExcel")
    public void exportInspectionExcel(@Valid @RequestBody InspectionQueryForm inspectionQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", inspectionQueryForm);
        //点检项类型
        Result itemTypeResult = dataDictProvider.getDataDictList(DataDictConstants.ITEM_TYPE);
        HashMap<String, String> itemTypeMap = new HashMap<String, String>();
        if(itemTypeResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)itemTypeResult.getData();
            itemTypeMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("获取" + DataDictConstants.ITEM_TYPE + "数据字典失败，" + itemTypeResult.getMsg());
        }

        List<InspectionMain> inspectionMainList = inspectionMainService.queryInspectionDataByCondition(inspectionQueryForm.toParam(InspectionQueryParam.class));
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("点检项维护数据");
        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("设备名称");
        titleRow.createCell(2).setCellValue("规格");
        titleRow.createCell(3).setCellValue("型号");
        titleRow.createCell(4).setCellValue("点检项");
        titleRow.createCell(5).setCellValue("点检项类型");
        titleRow.createCell(6).setCellValue("起始范围值");
        titleRow.createCell(7).setCellValue("截至范围值");
        titleRow.createCell(8).setCellValue("点检项判断标准");
        titleRow.createCell(9).setCellValue("理论值");
        titleRow.createCell(10).setCellValue("更新人");
        titleRow.createCell(11).setCellValue("更新时间");
        titleRow.createCell(12).setCellValue("创建人");
        titleRow.createCell(13).setCellValue("创建时间");

        //创建工作表
        XSSFSheet wbShiftSheet = workbook.createSheet("点检班次维护数据");
        XSSFRow shiftSheetTitleRow = wbShiftSheet.createRow(0);
        shiftSheetTitleRow.createCell(0).setCellValue("序号");
        shiftSheetTitleRow.createCell(1).setCellValue("设备名称");
        shiftSheetTitleRow.createCell(2).setCellValue("规格");
        shiftSheetTitleRow.createCell(3).setCellValue("型号");
        shiftSheetTitleRow.createCell(4).setCellValue("班次");
        shiftSheetTitleRow.createCell(5).setCellValue("开始时间");
        shiftSheetTitleRow.createCell(6).setCellValue("结束时间");
        shiftSheetTitleRow.createCell(7).setCellValue("更新人");
        shiftSheetTitleRow.createCell(8).setCellValue("更新时间");
        shiftSheetTitleRow.createCell(9).setCellValue("创建人");
        shiftSheetTitleRow.createCell(10).setCellValue("创建时间");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            //导出点检项表格
            if (inspectionMainList != null && inspectionMainList.size() > 0) {
                int rowNumber = 1;
                for (int i = 0; i < inspectionMainList.size(); i++) {
                    InspectionMain inspectionMain = inspectionMainList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(rowNumber++);
                    dataRow.createCell(0).setCellValue(rowNumber - 1);
                    dataRow.createCell(1).setCellValue(inspectionMain.getMchName() != null ? inspectionMain.getMchName() + "" : "");
                    dataRow.createCell(2).setCellValue(inspectionMain.getSpec() != null ? inspectionMain.getSpec() + "" : "");
                    dataRow.createCell(3).setCellValue(inspectionMain.getTypeVersion() != null ? inspectionMain.getTypeVersion() + "" : "");

                    List<InspectionItem> inspectionItemList = inspectionMain.getInspectionItemList();

                    for(int j=0; j<inspectionItemList.size(); j++)
                    {
                        InspectionItem inspectionItem = inspectionItemList.get(j);
                        if(j != 0)
                        {
                            dataRow = wbSheet.createRow(rowNumber++);
                            dataRow.createCell(0).setCellValue(rowNumber - 1);
                        }
                        dataRow.createCell(4).setCellValue(inspectionItem.getCheckItem() != null ? inspectionItem.getCheckItem() + "" : "");

                        String itemType = inspectionItem.getItemType() != null ? inspectionItem.getItemType() + "" : "";
                        if(StringUtils.isNotEmpty(itemType))
                        {
                            if(itemTypeMap.containsKey(itemType))
                            {
                                itemType = itemTypeMap.get(itemType);
                            }
                        }
                        dataRow.createCell(5).setCellValue(itemType);
                        if(inspectionItem.getMinValue() != null) {
                            dataRow.createCell(6).setCellValue(Double.valueOf(inspectionItem.getMinValue() + ""));
                        }else{
                            dataRow.createCell(6).setCellType(CellType.BLANK);
                        }
                        if(inspectionItem.getMaxValue() != null) {
                            dataRow.createCell(7).setCellValue(Double.valueOf(inspectionItem.getMaxValue() + ""));
                        }else{
                            dataRow.createCell(7).setCellType(CellType.BLANK);
                        }
                        dataRow.createCell(8).setCellValue(inspectionItem.getCheckItemStandard() != null ? inspectionItem.getCheckItemStandard() + "" : "");
                        dataRow.createCell(9).setCellValue(inspectionItem.getTheoreticalValue() != null ? inspectionItem.getTheoreticalValue() + "" : "");
                        dataRow.createCell(10).setCellValue(inspectionItem.getUpdatedBy() != null ? inspectionItem.getUpdatedBy() + "" : "");
                        dataRow.createCell(11).setCellValue(inspectionItem.getUpdatedTime() != null ? inspectionItem.getUpdatedTime().format(dateTimeFormatter) + "" : "");
                        dataRow.createCell(12).setCellValue(inspectionItem.getCreatedBy() != null ? inspectionItem.getCreatedBy() + "" : "");
                        dataRow.createCell(13).setCellValue(inspectionItem.getCreatedTime() != null ? inspectionItem.getCreatedTime().format(dateTimeFormatter) + "" : "");
                    }

                    //合并主表单元格
                    for(int k=1; k<=3; k++) {
                        if(inspectionItemList.size() <= 1)
                        {
                            continue;
                        }
                        ExcelUtil.mergeRegion(wbSheet, rowNumber-inspectionItemList.size(), rowNumber-1, k, k);
                    }
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*20, 256*15, 256*20, 256*20, 256*15, 256*20, 256*15, 256*15, 256*15, 256*15,
                    256*20, 256*15, 256*20});


            //导出班次表格
            if (inspectionMainList != null && inspectionMainList.size() > 0) {
                int shfitRowNumber = 1;
                for (int i = 0; i < inspectionMainList.size(); i++) {
                    InspectionMain inspectionMain = inspectionMainList.get(i);
                    XSSFRow dataRow = wbShiftSheet.createRow(shfitRowNumber++);
                    dataRow.createCell(0).setCellValue(shfitRowNumber - 1);
                    dataRow.createCell(1).setCellValue(inspectionMain.getMchName() != null ? inspectionMain.getMchName() + "" : "");
                    dataRow.createCell(2).setCellValue(inspectionMain.getSpec() != null ? inspectionMain.getSpec() + "" : "");
                    dataRow.createCell(3).setCellValue(inspectionMain.getTypeVersion() != null ? inspectionMain.getTypeVersion() + "" : "");

                    List<InspectionShift> inspectionShiftList = inspectionMain.getInspectionShiftList();

                    for(int j=0; j<inspectionShiftList.size(); j++)
                    {
                        InspectionShift inspectionShift = inspectionShiftList.get(j);
                        if(j != 0)
                        {
                            dataRow = wbShiftSheet.createRow(shfitRowNumber++);
                            dataRow.createCell(0).setCellValue(shfitRowNumber - 1);
                        }
                        dataRow.createCell(4).setCellValue(inspectionShift.getShift() != null ? inspectionShift.getShift() + "" : "");
                        dataRow.createCell(5).setCellValue(inspectionShift.getStartTime() != null ? inspectionShift.getStartTime() + "" : "");
                        dataRow.createCell(6).setCellValue(inspectionShift.getEndTime() != null ? inspectionShift.getEndTime() + "" : "");
                        dataRow.createCell(7).setCellValue(inspectionShift.getUpdatedBy() != null ? inspectionShift.getUpdatedBy() + "" : "");
                        dataRow.createCell(8).setCellValue(inspectionShift.getUpdatedTime() != null ? inspectionShift.getUpdatedTime().format(dateTimeFormatter) + "" : "");
                        dataRow.createCell(9).setCellValue(inspectionShift.getCreatedBy() != null ? inspectionShift.getCreatedBy() + "" : "");
                        dataRow.createCell(10).setCellValue(inspectionShift.getCreatedTime() != null ? inspectionShift.getCreatedTime().format(dateTimeFormatter) + "" : "");
                    }

                    //合并主表单元格
                    for(int k=1; k<=3; k++) {
                        if(inspectionShiftList.size() <= 1)
                        {
                            continue;
                        }
                        ExcelUtil.mergeRegion(wbShiftSheet, shfitRowNumber-inspectionShiftList.size(), shfitRowNumber-1, k, k);
                    }
                }
            }

            ExcelUtil.setSheetColumnWidth(wbShiftSheet, new int[] {256*10, 256*20, 256*15, 256*20, 256*20, 256*20, 256*15, 256*15,
                    256*20, 256*15, 256*20});

        } catch (Exception exception)
        {
            log.error("导出点检维护数据异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "点检维护数据.xlsx");
    }



    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/inspection.xlsx");
            if(inputStream == null)
            {
                throw new BusinessException("模板不存在");
            }
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("设备点检导入模板", "UTF-8"));
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



    @ApiOperation(value = "点检数据Excel上传", notes = "点检数据Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        inspectionMainService.importInspectionExcel(file.getInputStream());
        return Result.success();
    }
}