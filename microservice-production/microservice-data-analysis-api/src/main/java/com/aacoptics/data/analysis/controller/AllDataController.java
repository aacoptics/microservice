package com.aacoptics.data.analysis.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.AllData;
import com.aacoptics.data.analysis.exception.WlgReportErrorType;
import com.aacoptics.data.analysis.service.IAllDataService;
import com.aacoptics.data.analysis.util.ExcelUtil;
import com.aacoptics.data.analysis.util.FtpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/allData")
@Api("AllDataController")
@Slf4j
public class AllDataController {

    @Resource
    IAllDataService allDataService;

    /**
     * 根据条件查询条件数据
     *
     * @param queryParams
     * @return
     */
    @ApiOperation(value = "条件搜索关联数据", notes = "条件搜索关联数据")
    @PostMapping("/getDataByConditions")
    public Result getDataByConditions(@RequestBody QueryParams queryParams) {
        Integer page = queryParams.getCurrent();
        Integer size = queryParams.getSize();
        Page<AllData> iPage = new Page<>(page, size);
        IPage<AllData> res = allDataService.getAllDataByConditionsWithPage(iPage,
                queryParams.getCategory(),
                queryParams.getProject(),
                queryParams.getPartName(),
                queryParams.getMaterial(),
                queryParams.getDepartment(),
                queryParams.getLensNumber());
        if (res.getTotal() == 0) {
            return Result.fail(WlgReportErrorType.BUSINESS_EXCEPTION, "查询数据为空！");
        }
        return Result.success(res);
    }


    @ApiOperation(value = "导出所有数据Excel", notes = "导出所有数据Excel")
    @PostMapping(value = "/exportExcel")
    public void exportAllDataExcel(@RequestBody QueryParams queryParams, HttpServletResponse response) throws Exception {
        XSSFWorkbook wb = null;
        String picPath = "ftp://" + FtpUtil.getUserName() + ":" + FtpUtil.getPassword()
                + "@" + FtpUtil.getFtpHostIp() + ":" + FtpUtil.getFtpPort() + "/";
        try {
            // 根据查询条件获取数据
            List<AllData> datas = allDataService.getAllDataByConditions(queryParams);
            // 读取模板
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/allData.xlsx");
            wb = (XSSFWorkbook) WorkbookFactory.create(inputStream);
            XSSFSheet sheet = wb.getSheetAt(0);

            if (datas != null && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    AllData p = datas.get(i);
                    // 获取行
                    XSSFRow row = sheet.getRow(i + 5);
                    if (row == null) {
                        row = sheet.createRow(i + 5);
                    }
                    row.createCell(0).setCellValue(p.getDepartment());
                    row.createCell(1).setCellValue(p.getCategory());
                    row.createCell(2).setCellValue(p.getLensNumber());
                    row.createCell(3).setCellValue(p.getProject());
                    row.createCell(4).setCellValue(p.getPartName());
                    row.createCell(5).setCellValue(p.getMaterial());

                    row.createCell(6).setCellValue(p.getPcMoldNo());
                    row.createCell(7).setCellValue(p.getPcMoldType());
                    row.createCell(8).setCellValue(p.getMfMoldTemp());
                    row.createCell(9).setCellValue(p.getMfMaterialTemp());
                    row.createCell(10).setCellValue(p.getMfJetVelocity());
                    row.createCell(11).setCellValue(p.getMfVpSwitch());
                    row.createCell(12).setCellValue(p.getMfHoldPressure1());
                    row.createCell(13).setCellValue(p.getMfHoldTime1());
                    row.createCell(14).setCellValue(p.getMfHoldPressure2());
                    row.createCell(15).setCellValue(p.getMfHoldTime2());
                    row.createCell(16).setCellValue(p.getMfHoldPressure3());
                    row.createCell(17).setCellValue(p.getMfHoldTime3());
                    row.createCell(18).setCellValue(p.getMfHoldPressure4());
                    row.createCell(19).setCellValue(p.getMfHoldTime4());
                    row.createCell(20).setCellValue(p.getMfHoldPressure5());
                    row.createCell(21).setCellValue(p.getMfHoldTime5());
                    row.createCell(22).setCellValue(p.getMfHoldPressure6());
                    row.createCell(23).setCellValue(p.getMfHoldTime6());
                    row.createCell(24).setCellValue(p.getMfCoolingTime()); // 模流冷却时间
                    row.createCell(25).setCellValue(p.getMoldTemp());
                    row.createCell(26).setCellValue(p.getMaterialTemp());
                    row.createCell(27).setCellValue(p.getJetVelocity());
                    row.createCell(28).setCellValue(p.getVpSwitch());
                    row.createCell(29).setCellValue(p.getHoldPressure1());
                    row.createCell(30).setCellValue(p.getHoldTime1());
                    row.createCell(31).setCellValue(p.getHoldPressure2());
                    row.createCell(32).setCellValue(p.getHoldTime2());
                    row.createCell(33).setCellValue(p.getHoldPressure3());
                    row.createCell(34).setCellValue(p.getHoldTime3());
                    row.createCell(35).setCellValue(p.getHoldPressure4());
                    row.createCell(36).setCellValue(p.getHoldTime4());
                    row.createCell(37).setCellValue(p.getHoldPressure5());
                    row.createCell(38).setCellValue(p.getHoldTime5());
                    row.createCell(39).setCellValue(p.getHoldPressure6());
                    row.createCell(40).setCellValue(p.getHoldTime6());
                    row.createCell(41).setCellValue(p.getHoldPressureVelocity());
                    row.createCell(42).setCellValue(p.getPlatenPosition());
                    row.createCell(43).setCellValue(p.getOpeningSpeed());
                    row.createCell(44).setCellValue(p.getEjectionSpeed());
                    row.createCell(45).setCellValue(p.getCoolingTime());
                    row.createCell(46).setCellValue(p.getClampingForce());
                    row.createCell(47).setCellValue(p.getPassivation());


                    row.createCell(48).setCellValue(p.getSrMoldNo());
                    row.createCell(49).setCellValue(p.getSrMoldType());
                    row.createCell(50).setCellValue(p.getCoreThickness());
                    row.createCell(51).setCellValue(p.getCoreThicknessRange());
                    row.createCell(52).setCellValue(p.getR1VectorHeight());
                    row.createCell(53).setCellValue(p.getR1VectorHeightRange());
                    row.createCell(54).setCellValue(p.getR2VectorHeight());
                    row.createCell(55).setCellValue(p.getR2VectorHeightRange());
                    row.createCell(56).setCellValue(p.getOuterDiameterEcc());
                    row.createCell(57).setCellValue(p.getKanheEcc());
                    row.createCell(58).setCellValue(p.getFaceEcc());
                    row.createCell(59).setCellValue(p.getAnnealingProcess());
                    row.createCell(60).setCellValue(p.getBpKanheRoundness());
                    row.createCell(61).setCellValue(p.getDmpKanheRoundness());
                    row.createCell(62).setCellValue(p.getOuterDiameterAverage());
                    row.createCell(63).setCellValue(p.getOuterDiameterRange());
                    row.createCell(64).setCellValue(p.getOuterDiameterRoundness());
                    row.createCell(65).setCellValue(p.getOuterDiameterShrinkage());
                    row.createCell(66).setCellValue(p.getOuterDiameterRoughness());
                    row.createCell(67).setCellValue(p.getR1Flatness());
                    row.createCell(68).setCellValue(p.getR2Flatness());
                    row.createCell(69).setCellValue(p.getR1SplitAverage());
                    row.createCell(70).setCellValue(p.getR2SplitAverage());
                    row.createCell(71).setCellValue(p.getWftR1());
                    row.createCell(72).setCellValue(p.getWftR2());
                    if (StringUtils.isEmpty(p.getWftR1Pic())) {
                        row.createCell(73).setCellValue("");
                    } else {
                        row.createCell(73).setCellValue(picPath + "shapingResultData/" + p.getWftR1Pic().substring(0, p.getWftR1Pic().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getWftR2Pic())) {
                        row.createCell(74).setCellValue("");
                    } else {
                        row.createCell(74).setCellValue(picPath + "shapingResultData/" + p.getWftR2Pic().substring(0, p.getWftR2Pic().indexOf(".")));
                    }
                    row.createCell(75).setCellValue(p.getWftConsistency());
                    row.createCell(76).setCellValue(p.getWftMaxAs());
                    row.createCell(77).setCellValue(p.getWftStability());
                    row.createCell(78).setCellValue(p.getCftR1());
                    row.createCell(79).setCellValue(p.getCftR2());
                    if (StringUtils.isEmpty(p.getCftR1Pic())) {
                        row.createCell(80).setCellValue("");
                    } else {
                        row.createCell(80).setCellValue(picPath + "shapingResultData/" + p.getCftR1Pic().substring(0, p.getCftR1Pic().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getCftR2Pic())) {
                        row.createCell(81).setCellValue("");
                    } else {
                        row.createCell(81).setCellValue(picPath + "shapingResultData/" + p.getCftR2Pic().substring(0, p.getCftR2Pic().indexOf(".")));
                    }
                    row.createCell(82).setCellValue(p.getCftConsistency());
                    row.createCell(83).setCellValue(p.getCftMaxAs());
                    if (StringUtils.isEmpty(p.getCoatingTrend())) {
                        row.createCell(84).setCellValue("");
                    } else {
                        row.createCell(84).setCellValue(picPath + "shapingResultData/" + p.getCoatingTrend().substring(0, p.getCoatingTrend().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getCfsrR1())) {
                        row.createCell(85).setCellValue("");
                    } else {
                        row.createCell(85).setCellValue(picPath + "shapingResultData/" + p.getCfsrR1().substring(0, p.getCfsrR1().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getCfsrR2())) {
                        row.createCell(86).setCellValue("");
                    } else {
                        row.createCell(86).setCellValue(picPath + "shapingResultData/" + p.getCfsrR2().substring(0, p.getCfsrR2().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getCfsrR1R2())) {
                        row.createCell(87).setCellValue("");
                    } else {
                        row.createCell(87).setCellValue(picPath + "shapingResultData/" + p.getCfsrR1R2().substring(0, p.getCfsrR1R2().indexOf(".")));
                    }
                    row.createCell(88).setCellValue(p.getBurr());
                    row.createCell(89).setCellValue(p.getWeldline());
                    row.createCell(90).setCellValue(p.getAppearanceProblem());
                    if (StringUtils.isEmpty(p.getAppearanceImg())) {
                        row.createCell(91).setCellValue("");
                    } else {
                        row.createCell(91).setCellValue(picPath + "shapingResultData/" + p.getAppearanceImg().substring(0, p.getAppearanceImg().indexOf(".")));
                    }
                    row.createCell(92).setCellValue(p.getRemarks());
                    row.createCell(93).setCellValue(p.getAbcFilesNo());
                    row.createCell(94).setCellValue(p.getStructureNo());
                    row.createCell(95).setCellValue(p.getMoldTypeNo());
                    row.createCell(96).setCellValue(p.getMoldCost());
                    row.createCell(97).setCellValue(p.getEvtTime());
                    row.createCell(98).setCellValue(p.getDvtTime());
                    row.createCell(99).setCellValue(p.getEvtDvtTime());
                    row.createCell(100).setCellValue(p.getEvtCost());
                    row.createCell(101).setCellValue(p.getDvtCost());
                    row.createCell(102).setCellValue(p.getEvtDvtCost());
                    row.createCell(103).setCellValue(p.getProjectMassProduction());


                    row.createCell(104).setCellValue(p.getCoreThicknessLens());
                    row.createCell(105).setCellValue(p.getMaxWallThickness());
                    row.createCell(106).setCellValue(p.getMinWallThickness());
                    row.createCell(107).setCellValue(p.getMaxCoreRatio());
                    row.createCell(108).setCellValue(p.getMaxMinRatio());
                    row.createCell(109).setCellValue(p.getOpticsMaxAngleR1());
                    row.createCell(110).setCellValue(p.getOpticsMaxAngleR2());
                    row.createCell(111).setCellValue(p.getOuterDiameter());
                    row.createCell(112).setCellValue(p.getEdgeThickness());
                    row.createCell(113).setCellValue(p.getWholeMinWallThickness());
                    row.createCell(114).setCellValue(p.getWholeMaxWallThickness());
                    row.createCell(115).setCellValue(p.getWholeMaxMinRatio());
                    row.createCell(116).setCellValue(p.getWholeDiameterThicknessRatio());
                    row.createCell(117).setCellValue(p.getMaxAngleR1());
                    row.createCell(118).setCellValue(p.getMaxAngleR2());
                    row.createCell(119).setCellValue(p.getR1MaxHeightDifference());
                    row.createCell(120).setCellValue(p.getR2MaxHeightDifference());
                    row.createCell(121).setCellValue(p.getR1R2Distance());
                    row.createCell(122).setCellValue(p.getMiddlePartThickness());
                    row.createCell(123).setCellValue(p.getBottomDiameterDistance());
                    row.createCell(124).setCellValue(p.getMechanismDiameterThicknessRatio());
                    row.createCell(125).setCellValue(p.getR1KanheAngle());
                    row.createCell(126).setCellValue(p.getR1KanheHeight());
                    row.createCell(127).setCellValue(p.getR2KanheAngle());
                    row.createCell(128).setCellValue(p.getR2KanheHeight());
                    row.createCell(129).setCellValue(p.getR1Srtm());
                    row.createCell(130).setCellValue(p.getR2Srtm());
                    row.createCell(131).setCellValue(p.getR1SplitPosition());
                    row.createCell(132).setCellValue(p.getR2SplitPosition());
                    row.createCell(133).setCellValue(p.getOuterDiameterSrtm());
                    row.createCell(134).setCellValue(p.getPartSurfaceLiftRatio());
                    row.createCell(135).setCellValue(p.getMechanismTrou());
                    if (StringUtils.isEmpty(p.getAssemblyDrawing())) {
                        row.createCell(136).setCellValue("");
                    } else {
                        row.createCell(136).setCellValue(picPath + "structureData/" + p.getAssemblyDrawing().substring(0, p.getAssemblyDrawing().indexOf(".")));
                    }

                    row.createCell(137).setCellValue(p.getMfMoldType());
                    row.createCell(138).setCellValue(p.getMfRunnerType());
                    row.createCell(139).setCellValue(p.getMoldDiameterRate());
                    row.createCell(140).setCellValue(p.getFlowFrontTemperature());
                    row.createCell(141).setCellValue(p.getVpChangePressure());
                    row.createCell(142).setCellValue(p.getSimulateWireLength());
                    row.createCell(143).setCellValue(p.getWholePercent());
                    row.createCell(144).setCellValue(p.getEffectiveR1());
                    row.createCell(145).setCellValue(p.getEffectiveR2());
                    row.createCell(146).setCellValue(p.getRidgeR1());
                    row.createCell(147).setCellValue(p.getRidgeR2());
                    row.createCell(148).setCellValue(p.getRefractiveR1());
                    row.createCell(149).setCellValue(p.getRefractiveR2());
                    if (StringUtils.isEmpty(p.getRefractivePicR1())) {
                        row.createCell(150).setCellValue("");

                    } else {
                        row.createCell(150).setCellValue(picPath + "moldFlowData/" + p.getRefractivePicR1().substring(0, p.getRefractivePicR1().indexOf(".")));
                    }
                    if (StringUtils.isEmpty(p.getRefractivePicR2())) {
                        row.createCell(151).setCellValue("");

                    } else {
                        row.createCell(151).setCellValue(picPath + "moldFlowData/" + p.getRefractivePicR2().substring(0, p.getRefractivePicR2().indexOf(".")));
                    }
                    row.createCell(152).setCellValue(p.getCompetitorName());
                    row.createCell(153).setCellValue(p.getCompetitorLink());
                    if (StringUtils.isEmpty(p.getCompetitorAssemblyDrawing())) {
                        row.createCell(154).setCellValue("");

                    } else {
                        row.createCell(154).setCellValue(picPath + "moldFlowData/" + p.getCompetitorAssemblyDrawing().substring(0, p.getCompetitorAssemblyDrawing().indexOf(".")));

                    }


                    row.createCell(155).setCellValue(p.getMdMoldNo());
                    row.createCell(156).setCellValue(p.getMdMoldType());
                    row.createCell(157).setCellValue(p.getMoldCorePassivation());
                    row.createCell(158).setCellValue(p.getMdRunnerType());
                    row.createCell(159).setCellValue(p.getCavityInnerDiameter());
                    row.createCell(160).setCellValue(p.getCavityInnerDiameterRange());
                    row.createCell(161).setCellValue(p.getFirstRunner());
                    row.createCell(162).setCellValue(p.getSecondRunner());
                    row.createCell(163).setCellValue(p.getThirdRunner());
                    row.createCell(164).setCellValue(p.getPartingSurface());
                    row.createCell(165).setCellValue(p.getSplitPositionR1());
                    row.createCell(166).setCellValue(p.getSplitPositionR2());
                    row.createCell(167).setCellValue(p.getGateType());
                    row.createCell(168).setCellValue(p.getGateWidth());
                    row.createCell(169).setCellValue(p.getGateThickness());
                    row.createCell(170).setCellValue(p.getGateR1Thickness());
                    row.createCell(171).setCellValue(p.getGateR2Thickness());
                    row.createCell(172).setCellValue(p.getMoldOpeningType());
                }
            }
        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw e;
        }
        ExcelUtil.exportXlsx(response, wb, "关联数据.xlsx");
    }

    @ApiOperation(value = "图片流", notes = "读取ftp服务器图片")
    @GetMapping(value = "/fileStream")
    public void toStream(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filePathPrefix = request.getParameter("filePathPrefix");
        String fileNameWithExt = request.getParameter("fileName");
        if (StringUtils.isNotEmpty(fileNameWithExt) && fileNameWithExt.indexOf(".") != -1) {
            String[] split = fileNameWithExt.split("\\.");
            String fileName = split[0];
            String ext = split[1];
            FtpUtil.connect();
            FtpUtil.changeWorkingDirectory(filePathPrefix);
            InputStream inputStream = FtpUtil.getInputStream(fileName);
            if (ext.equals("png")) {
                response.setContentType("image/png");
            } else {
                response.setContentType("image/jpeg");
            }
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());

            //创建存放文件内容的数组
            byte[] buff = new byte[1024];
            //所读取的内容使用n来接收
            int n;
            //当没有读取完时,继续读取,循环
            while ((n = inputStream.read(buff)) != -1) {
                //将字节数组的数据全部写入到输出流中
                outputStream.write(buff, 0, n);
            }
            //关流
            outputStream.close();
            inputStream.close();
        }
    }

}
