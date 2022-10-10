package com.aacoptics.wlg.dashboard.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.json.JSONUtil;
import com.aacoptics.common.core.util.UserContextHolder;
import com.aacoptics.wlg.dashboard.entity.InputReport;
import com.aacoptics.wlg.dashboard.entity.MarkdownGroupMessage;
import com.aacoptics.wlg.dashboard.entity.MoldingMachineParamData;
import com.aacoptics.wlg.dashboard.mapper.InputReportMapper;
import com.aacoptics.wlg.dashboard.mapper.MoldingMachineParamDataMapper;
import com.aacoptics.wlg.dashboard.provider.DingTalkApi;
import com.aacoptics.wlg.dashboard.service.IFeishuService;
import com.aacoptics.wlg.dashboard.service.InputReportService;
import com.aacoptics.wlg.dashboard.service.MoldingMachineParamDataService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.google.common.collect.ImmutableList;
import com.spire.xls.Worksheet;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.aacoptics.wlg.dashboard.util.ExcelUtil.objectToExcelExportParam;
import static com.aacoptics.wlg.dashboard.util.ExcelUtil.updateBorder;

@Service
@Slf4j
public class InputReportServiceImpl extends ServiceImpl<InputReportMapper, InputReport> implements InputReportService {

    @Resource
    InputReportMapper inputReportMapper;

    @Resource
    DingTalkApi dingTalkApi;

    @Resource
    FeishuService feishuService;

    @Override
    public List<InputReport> getByDateAndMachineName(InputReport inputReport) {
        return inputReportMapper.getByDateAndMachineName(inputReport.getStartTime(), inputReport.getEndTime(), inputReport.getMachineNames());
    }

    @Override
    public void updateOutPutInfo(InputReport inputReport) {
        UpdateWrapper<InputReport> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("broken_ok", inputReport.getBrokenOk());
        updateWrapper.set("broken_ng", inputReport.getBrokenNg());
        updateWrapper.set("output_qty", inputReport.getOutputQty());
        updateWrapper.set("update_time", LocalDateTime.now());
        updateWrapper.set("abnormal_reason", inputReport.getAbnormalReason());
        updateWrapper.set("update_user", inputReport.getUpdateUser());
        updateWrapper.eq("id", inputReport.getId());
        this.update(updateWrapper);
    }

    @Override
    public void SendPicNotification() throws IOException, ApiException {
        String tempDir = System.getProperty("java.io.tmpdir");
        long currentTimeMillis = System.currentTimeMillis();
        String fileName = "wlg-input-report-" + currentTimeMillis + ".xlsx";
        String imageFileName = "wlg-input-report-" + currentTimeMillis + ".png";
        QueryWrapper<InputReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("end_time", LocalDateTime.now().minusHours(2))
                .isNotNull("end_wafer_id")
                .orderByAsc("model_name", "machine_name");

        InputReport sumInfo = inputReportMapper.getTwoHourSum();

        List<InputReport> report = this.list(queryWrapper);

        if(report.size() > 0){
            DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
            String startTime = df.format(report.get(0).getStartTime());
            String endTime = df.format(report.get(0).getEndTime());

            List<Map<String, Object>> exportParamList = new ArrayList<>();

            exportParamList.add(objectToExcelExportParam("模造机两小时投入产出", "模造机两小时投入产出", report, InputReport.class));

            Workbook workbook = ExcelExportUtil.exportExcel(exportParamList, ExcelType.XSSF); // 写入workbook
            updateBorder(workbook);
            FileOutputStream out = new FileOutputStream(tempDir + "/" + fileName);
            workbook.write(out);
            out.close();
            workbook.close();
            // Excel转图片
            com.spire.xls.Workbook spireXlsWorkbook = new com.spire.xls.Workbook();
            spireXlsWorkbook.loadFromFile(tempDir + "/" + fileName);
            Worksheet worksheet = spireXlsWorkbook.getWorksheets().get(0);
            worksheet.saveToImage(tempDir + "/" + imageFileName);

            FeishuSendMessageByChat("WLG 模造投入产出", "两小时投入产出汇总", ImmutableList.<Tuple2<String, String>>builder()
                            .add(Tuples.of(startTime + "-" + endTime + "\n投入：" + sumInfo.getInputQty() + "   产出：" + sumInfo.getOutputQty() + "\n明细如下：", tempDir + "/" + imageFileName))
                            .build(),
                    tempDir + "/" + fileName);



            //获取token
//            OapiGettokenResponse oapiGettokenResponse = dingTalkApi.getAccessToken();
//            String accessToken = oapiGettokenResponse.getAccessToken();
//
//            //上传图片
//            String mediaId = dingTalkApi.uploadMedia(accessToken, "image", tempDir + "/" + imageFileName);
//            if (StringUtils.isEmpty(mediaId)) {
//                log.error("上传图片到钉钉异常" + tempDir + "/" + imageFileName);
//                return;
//            }
//
//            MarkdownGroupMessage markdownGroupMessage = new MarkdownGroupMessage();
//            markdownGroupMessage.setTitle("两小时投入产出汇总");
//            markdownGroupMessage.addBlobContent(startTime + "-" + endTime);
//            markdownGroupMessage.addBlobContent("投入：" + sumInfo.getInputQty() + "   产出：" + sumInfo.getOutputQty());
//            markdownGroupMessage.addContent("明细如下：");
//            markdownGroupMessage.addContent("![明细](" + mediaId + ")");
//
//            Map<String, String> resultMap = dingTalkApi.sendGroupRobotMessage("https://oapi.dingtalk.com/robot/send?access_token=bcf308c4ee97a16d9265365d27001de7f42794d9018702fd253c2d1b28bc442a", "两小时投入产出汇总", markdownGroupMessage.toString());
//            String result = resultMap.get("result");
//            String message = resultMap.get("message");
        }
    }

    private boolean FeishuSendMessageByChat(String chatName, String title, List<Tuple2<String, String>> imageFilePaths, String excelFilePaths) throws IOException {
        final String chatId = feishuService.fetchChatIdByRobot(chatName);
        List<Tuple2<String, String>> imageFileKeys = new ArrayList<>();
        for (Tuple2<String, String> imageFilePath : imageFilePaths)
            imageFileKeys.add(Tuples.of(imageFilePath.getT1(), feishuService.fetchUploadMessageImageKey(imageFilePath.getT2())));
        String fileKey = feishuService.fetchUploadFileKey(IFeishuService.FILE_TYPE_XLS, excelFilePaths, 0);


        boolean resultByImage = feishuService.sendMessage(IFeishuService.RECEIVE_ID_TYPE_CHAT_ID,
                chatId,
                IFeishuService.MSG_TYPE_POST,
                feishuService.getImagePostMessage(title, imageFileKeys, Collections.singletonList("all")));
        boolean resultByFile = feishuService.sendMessage(IFeishuService.RECEIVE_ID_TYPE_CHAT_ID,
                chatId,
                IFeishuService.MSG_TYPE_FILE,
                JSONUtil.createObj().set("file_key", fileKey));
        return resultByImage && resultByFile;
    }
}