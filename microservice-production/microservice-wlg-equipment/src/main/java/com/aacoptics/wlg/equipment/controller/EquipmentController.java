package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.DataDictConstants;
import com.aacoptics.wlg.equipment.entity.form.EquipmentForm;
import com.aacoptics.wlg.equipment.entity.form.EquipmentQueryForm;
import com.aacoptics.wlg.equipment.entity.param.EquipmentQueryParam;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.provider.DataDictProvider;
import com.aacoptics.wlg.equipment.service.EquipmentService;
import com.aacoptics.wlg.equipment.util.DataDictUtil;
import com.aacoptics.wlg.equipment.util.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipmentManagement")
@Api("equipmentManagement")
@Slf4j
public class EquipmentController {

    @Autowired
    EquipmentService equipmentService;

    @Autowired
    DataDictProvider dataDictProvider;

    @ApiOperation(value = "搜索设备", notes = "根据条件搜索设备信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "设备查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody EquipmentQueryForm equipmentQueryForm) {
        log.debug("query with name:{}", equipmentQueryForm);
        return Result.success(equipmentService.query(equipmentQueryForm.getPage(), equipmentQueryForm.toParam(EquipmentQueryParam.class)));
    }



    @ApiOperation(value = "搜索设备名称", notes = "根据条件搜索设备名称")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "设备查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findMchNameList")
    public Result findMchNameList() {
        return Result.success(equipmentService.findMchNameList());
    }


    @ApiOperation(value = "搜索设备规格", notes = "根据条件搜索设备规格")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "设备规格查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findSpecListByMchName")
    public Result findSpecListByMchName(@Valid @RequestBody EquipmentQueryForm equipmentQueryForm) {
        log.debug("query with name:{}", equipmentQueryForm);
        return Result.success(equipmentService.findSpecListByMchName(equipmentQueryForm.toParam(EquipmentQueryParam.class)));
    }


    @ApiOperation(value = "搜索设备型号", notes = "根据条件搜索设备型号")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "设备型号查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findTypeVersionListByMchNameAndSpec")
    public Result findTypeVersionListByMchNameAndSpec(@Valid @RequestBody EquipmentQueryForm equipmentQueryForm) {
        log.debug("query with name:{}", equipmentQueryForm);
        return Result.success(equipmentService.findTypeVersionListByMchNameAndSpec(equipmentQueryForm.toParam(EquipmentQueryParam.class)));
    }


    @ApiOperation(value = "新增设备", notes = "新增一个设备信息")
    @ApiImplicitParam(name = "equipmentForm", value = "新增设备form表单", required = true, dataType = "EquipmentForm")
    @PostMapping
    public Result add(@Valid @RequestBody EquipmentForm equipmentForm) {
        log.debug("name:{}", equipmentForm);
        Equipment equipment = equipmentForm.toPo(Equipment.class);
        return Result.success(equipmentService.add(equipment));
    }

    @ApiOperation(value = "删除设备", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "设备ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(equipmentService.delete(id));
    }

    @ApiOperation(value = "修改设备", notes = "修改指定设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "equipmentForm", value = "设备实体", required = true, dataType = "EquipmentForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody EquipmentForm equipmentForm) {
        Equipment equipment = equipmentForm.toPo(id, Equipment.class);
        //只更新设备编号
        String equipNumber = equipment.getEquipNumber();
        String equipDuty = equipment.getEquipDuty() != null ? equipment.getEquipDuty().trim() : null;
        String equipDutyManager = equipmentForm.getEquipDutyManager() != null ? equipmentForm.getEquipDutyManager().trim() : null;

        Equipment equipmentTarget = equipmentService.get(equipment.getId());
        equipmentTarget.setEquipNumber(equipNumber);
        equipmentTarget.setEquipDuty(equipDuty);
        equipmentTarget.setEquipDutyManager(equipDutyManager);
        equipmentTarget.setEquipCategory(equipmentForm.getEquipCategory());
        equipmentTarget.setStatus(equipmentForm.getStatus());
        equipmentTarget.setSectionType(equipmentForm.getSectionType());

        return Result.success(equipmentService.update(equipmentTarget));
    }

    @ApiOperation(value = "获取设备", notes = "获取指定设备信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "设备ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(equipmentService.get(id));
    }


    @ApiOperation(value = "获取设备", notes = "获取指定设备信息")
    @ApiImplicitParam(paramType = "path", name = "mchCode", value = "设备编码", example = "0", required = true, dataType = "String")
    @PostMapping(value = "findEquipmentByMchCode")
    public Result findEquipmentByMchCode(@RequestBody String requestBody) {
        log.debug("get with id:{}", requestBody);
        if(StringUtils.isEmpty(requestBody))
        {
            throw new BusinessException("参数不能为空");
        }
        JSONObject jsonObject = JSON.parseObject(requestBody);
        String mchCode = jsonObject.getString("mchCode");
        if(StringUtils.isEmpty(mchCode))
        {
            throw new BusinessException("设备编码不能为空");
        }
        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        if(equipment == null)
        {
            throw new BusinessException("设备编码【" + mchCode + "】不存在，请确认！");
        }

        return Result.success(equipment);
    }


    @ApiOperation(value = "导出设备Excel", notes = "导出设备Excel")
    @PostMapping(value = "/exportEquipmentExcel")
    public void exportEquipmentExcel(@Valid @RequestBody EquipmentQueryForm equipmentQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", equipmentQueryForm);
        List<Equipment> equipmentList = equipmentService.queryEquipmentByCondition(equipmentQueryForm.toParam(EquipmentQueryParam.class));

        //获取数据字典值
        //设备状态
        Result equipmentStatusResult = dataDictProvider.getDataDictList(DataDictConstants.EQUIPMENT_STATUS);
        HashMap<String, String> equipmentStatusMap = new HashMap<String, String>();
        if(equipmentStatusResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)equipmentStatusResult.getData();
            equipmentStatusMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("获取" + DataDictConstants.EQUIPMENT_STATUS + "数据字典失败，" + equipmentStatusResult.getMsg());
        }

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("设备清单");

        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("资产编码");
        titleRow.createCell(2).setCellValue("资产名称");
        titleRow.createCell(3).setCellValue("规格");
        titleRow.createCell(4).setCellValue("型号");
        titleRow.createCell(5).setCellValue("设备编号");
        titleRow.createCell(6).setCellValue("设备负责人");
        titleRow.createCell(7).setCellValue("设备负责人经理");
        titleRow.createCell(8).setCellValue("设备属性");
        titleRow.createCell(9).setCellValue("状态");
        titleRow.createCell(10).setCellValue("资产状态编码");
        titleRow.createCell(11).setCellValue("资产状态");
        titleRow.createCell(12).setCellValue("资产使用性质编码");
        titleRow.createCell(13).setCellValue("资产使用性质名称");
        titleRow.createCell(14).setCellValue("资产分类编码");
        titleRow.createCell(15).setCellValue("资产分类名称");
        titleRow.createCell(16).setCellValue("专业大类编码");
        titleRow.createCell(17).setCellValue("专业大类名称");
        titleRow.createCell(18).setCellValue("专业小类编码");
        titleRow.createCell(19).setCellValue("专业小类名称");
        titleRow.createCell(20).setCellValue("出厂编码");
        titleRow.createCell(21).setCellValue("位置编码");
        titleRow.createCell(22).setCellValue("地区编码");
        titleRow.createCell(23).setCellValue("地区名称");
        titleRow.createCell(24).setCellValue("厂区编码");
        titleRow.createCell(25).setCellValue("厂区名称");
        titleRow.createCell(26).setCellValue("楼栋编码");
        titleRow.createCell(27).setCellValue("楼栋名称");
        titleRow.createCell(28).setCellValue("楼层编码");
        titleRow.createCell(29).setCellValue("楼层名称");
        titleRow.createCell(30).setCellValue("资产管理员");
        titleRow.createCell(31).setCellValue("设备管理员");
        titleRow.createCell(32).setCellValue("责任人");
        titleRow.createCell(33).setCellValue("部门经理");
        titleRow.createCell(34).setCellValue("部门总监");
        titleRow.createCell(35).setCellValue("部门VP");
        titleRow.createCell(36).setCellValue("最后点检日期");
        titleRow.createCell(37).setCellValue("最后保养日期");

        try {
            if (equipmentList != null && equipmentList.size() > 0) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < equipmentList.size(); i++) {
                    Equipment equipment = equipmentList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(i + 1);
                    dataRow.createCell(0).setCellValue(i + 1);
                    dataRow.createCell(1).setCellValue(equipment.getMchCode() != null ? equipment.getMchCode() + "" : "");
                    dataRow.createCell(2).setCellValue(equipment.getMchName() != null ? equipment.getMchName() + "" : "");
                    dataRow.createCell(3).setCellValue(equipment.getSpec() != null ? equipment.getSpec() + "" : "");
                    dataRow.createCell(4).setCellValue(equipment.getTypeVersion() != null ? equipment.getTypeVersion() + "" : "");
                    dataRow.createCell(5).setCellValue(equipment.getEquipNumber() != null ? equipment.getEquipNumber() + "" : "");
                    dataRow.createCell(6).setCellValue(equipment.getEquipDuty() != null ? equipment.getEquipDuty() + "" : "");
                    dataRow.createCell(7).setCellValue(equipment.getEquipDutyManager() != null ? equipment.getEquipDutyManager() + "" : "");
                    dataRow.createCell(8).setCellValue(equipment.getEquipCategory() != null ? equipment.getEquipCategory() + "" : "");
                    //状态通过数据字典翻译
                    String equipmentStatus = equipment.getStatus() != null ? equipment.getStatus() + "" : "";
                    if(StringUtils.isNotEmpty(equipmentStatus))
                    {
                        if(equipmentStatusMap.containsKey(equipmentStatus))
                        {
                            equipmentStatus = equipmentStatusMap.get(equipmentStatus);
                        }
                    }
                    dataRow.createCell(9).setCellValue(equipmentStatus);
                    dataRow.createCell(10).setCellValue(equipment.getEquipStateDb() != null ? equipment.getEquipStateDb() + "" : "");
                    dataRow.createCell(11).setCellValue(equipment.getEquipState() != null ? equipment.getEquipState() + "" : "");
                    dataRow.createCell(12).setCellValue(equipment.getAssetGeneralCode() != null ? equipment.getAssetGeneralCode() + "" : "");
                    dataRow.createCell(13).setCellValue(equipment.getAssetGeneralDesc() != null ? equipment.getAssetGeneralDesc() + "" : "");
                    dataRow.createCell(14).setCellValue(equipment.getAssetClassifyCode() != null ? equipment.getAssetClassifyCode() + "" : "");
                    dataRow.createCell(15).setCellValue(equipment.getAssetClassifyDesc() != null ? equipment.getAssetClassifyDesc() + "" : "");
                    dataRow.createCell(16).setCellValue(equipment.getMajorBigCode() != null ? equipment.getMajorBigCode() + "" : "");
                    dataRow.createCell(17).setCellValue(equipment.getMajorBigDesc() != null ? equipment.getMajorBigDesc() + "" : "");
                    dataRow.createCell(18).setCellValue(equipment.getMajorSmallCode() != null ? equipment.getMajorSmallCode() + "" : "");
                    dataRow.createCell(19).setCellValue(equipment.getMajorSmallDesc() != null ? equipment.getMajorSmallDesc() + "" : "");
                    dataRow.createCell(20).setCellValue(equipment.getFactoryNo() != null ? equipment.getFactoryNo() + "" : "");
                    dataRow.createCell(21).setCellValue(equipment.getLocationNo() != null ? equipment.getLocationNo() + "" : "");
                    dataRow.createCell(22).setCellValue(equipment.getAreaCode() != null ? equipment.getAreaCode() + "" : "");
                    dataRow.createCell(23).setCellValue(equipment.getAreaName() != null ? equipment.getAreaName() + "" : "");
                    dataRow.createCell(24).setCellValue(equipment.getFactoryId() != null ? equipment.getFactoryId() + "" : "");
                    dataRow.createCell(25).setCellValue(equipment.getFactoryName() != null ? equipment.getFactoryName() + "" : "");
                    dataRow.createCell(26).setCellValue(equipment.getBuildingId() != null ? equipment.getBuildingId() + "" : "");
                    dataRow.createCell(27).setCellValue(equipment.getBuildingName() != null ? equipment.getBuildingName() + "" : "");
                    dataRow.createCell(28).setCellValue(equipment.getFloorCode() != null ? equipment.getFloorCode() + "" : "");
                    dataRow.createCell(29).setCellValue(equipment.getFloorName() != null ? equipment.getFloorName() + "" : "");
                    dataRow.createCell(30).setCellValue(equipment.getAssetManagerId() != null ? equipment.getAssetManagerId() + "" : "");
                    dataRow.createCell(31).setCellValue(equipment.getMchManagerId() != null ? equipment.getMchManagerId() + "" : "");
                    dataRow.createCell(32).setCellValue(equipment.getDutyPersonId() != null ? equipment.getDutyPersonId() + "" : "");
                    dataRow.createCell(33).setCellValue(equipment.getDeptManagerId() != null ? equipment.getDeptManagerId() + "" : "");
                    dataRow.createCell(34).setCellValue(equipment.getDeptDirectorId() != null ? equipment.getDeptDirectorId() + "" : "");
                    dataRow.createCell(35).setCellValue(equipment.getVicePresidentId() != null ? equipment.getVicePresidentId() + "" : "");
                    dataRow.createCell(36).setCellValue(equipment.getLastInspectionDatetime() != null ? equipment.getLastInspectionDatetime().format(dateTimeFormatter) : "");
                    dataRow.createCell(37).setCellValue(equipment.getLastMaintenanceDatetime() != null ? equipment.getLastMaintenanceDatetime().format(dateTimeFormatter) : "");

                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*15, 256*15, 256*20, 256*20, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15,
                    256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*20, 256*20});

        } catch (Exception exception)
        {
            log.error("导出设备清单异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "设备清单.xlsx");
    }


    /**
     * Excel模板下载
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/equipment.xlsx");
            if(inputStream == null)
            {
                throw new BusinessException("模板不存在");
            }
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("设备清单导入模板", "UTF-8"));
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



    @ApiOperation(value = "设备清单数据Excel上传", notes = "设备清单数据Excel上传")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadExcel")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) throws Exception {

        equipmentService.importEquipmentExcel(file.getInputStream());
        return Result.success();
    }
}