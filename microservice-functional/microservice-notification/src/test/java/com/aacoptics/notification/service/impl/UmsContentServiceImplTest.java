package com.aacoptics.notification.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.notification.entity.po.*;
import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.exception.BusinessException;
import com.aacoptics.notification.provider.FeishuApi;
import com.aacoptics.notification.provider.XxlJobProvider;
import com.aacoptics.notification.service.*;
import com.spire.xls.Worksheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UmsContentServiceImplTest {
    @Resource
    UmsContentService umsContentService;

    @Resource
    SendMessageService sendMessageService;

    @Resource
    XxlJobProvider xxlJobProvider;

    @Resource
    FeishuApi feishuApi;

    @Resource
    XxlJobInfoService xxlJobInfoService;

    @Resource
    XxlGroupInfoService xxlGroupInfoService;

    @Resource
    FeishuService feishuService;


    @Test
    public void Test1() {
//
//        JSONObject text = new JSONObject();
//        text.set("content", "**管理费用预算与实际费用统计（测试）** \n;时间：2022年3月;您负责部门费用如下;当月实际费用：￥14,616 K;当月预算金额：￥12,103 K;GAP节省费用：￥1,487 K;");
//        text.set("tag", "lark_md");
//        JSONArray elements = new JSONArray();
//        JSONObject element = new JSONObject();
//        element.set("tag", "div");
//        element.set("text", text);
//        elements.add(element);
//
//        JSONObject config = new JSONObject();
//        config.set("wide_screen_mode", true);
//
//        JSONObject card = new JSONObject();
//        card.set("config", config);
//        card.set("elements", elements);
//        feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_USER_ID, "7c84331d", FeishuService.MSG_TYPE_INTERACTIVE, card);

        List<UmsContent> messageBatches;
        messageBatches = umsContentService.getUmsContentByBatchId("ums_sop_ri_cost_gp_qas", "22081214105");
        for (UmsContent messageBatch : messageBatches) {
            boolean fileResult = true;
            String imageKey = null;
            JSONObject cardJson = null;
            if (!StrUtil.isBlank(messageBatch.getSendFilePath())) {
                try {
                    String tempDir = System.getProperty("java.io.tmpdir");
                    long currentTimeMillis = System.currentTimeMillis();
                    String excelFileName1 = messageBatch.getConTypeDesc() + "-" + currentTimeMillis + ".xlsx";
                    String pngExcelFileName = messageBatch.getConTypeDesc() + "-PNG-" + currentTimeMillis + ".xlsx";
                    String pngFileName = messageBatch.getConTypeDesc() + "-" + currentTimeMillis + ".png";
                    URL url = new URL(messageBatch.getSendFilePath());
                    FileUtils.copyURLToFile(url, new File(tempDir + "/" + excelFileName1));

                    if (StrUtil.isNotEmpty(messageBatch.getSendPicturePath())) {
                        url = new URL(messageBatch.getSendPicturePath());
                        FileUtils.copyURLToFile(url, new File(tempDir + "/" + pngExcelFileName));
                        com.spire.xls.Workbook spireXlsWorkbook = new com.spire.xls.Workbook();
                        spireXlsWorkbook.loadFromFile(tempDir + "/" + pngExcelFileName);
                        Worksheet worksheet = spireXlsWorkbook.getWorksheets().get(0);
                        worksheet.saveToImage(tempDir + "/" + pngFileName);
                        imageKey = feishuService.fetchUploadMessageImageKey(tempDir + "/" + pngFileName);
                    }

                    String fileKey = feishuService.fetchUploadFileKey(FeishuService.FILE_TYPE_XLS, tempDir + "/" + excelFileName1, 0);
                    String chatName = "每日毛利率飞书测试";
                    switch (messageBatch.getConType()) {
                        case "ums_sop_ri_cost_gp_qas":
                        case "ums_sop_ri_cost_lens_qas":
                            chatName = "每日毛利率飞书测试";
                            break;
                        case "ums_sop_ri_cost_lens_prd":
                        case "ums_sop_ri_cost_gp_prd":
                            chatName = "Lens每日运营指标达成汇报";
                            break;
                    }

                    cardJson = feishuApi.getMarkdownMessage("**管理费用预算与实际费用统计（测试）** \n;时间：2022年3月;您负责部门费用如下;当月实际费用：￥14,616 K;当月预算金额：￥12,103 K;GAP节省费用：￥1,487 K;", imageKey);

                    final String chatId = feishuService.fetchChatIdByRobot("我的测试群");
                    fileResult = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID,
                            chatId,
                            FeishuService.MSG_TYPE_FILE,
                            JSONUtil.createObj().set("file_key", fileKey));
                } catch (IOException err) {
                    String msg = "解析http文件异常！{" + err.getMessage() + "}";
                    log.error(msg);
                    throw new BusinessException(msg);
                }
            }
            final String chatId = feishuService.fetchChatIdByRobot("我的测试群");
            feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID,
                    chatId,
                    FeishuService.MSG_TYPE_INTERACTIVE,
                    cardJson);
        }



    }

    @Test
    public void Test() {
        List<XxlGroupInfo> xxlGroupInfoList = xxlGroupInfoService.list();

//        XxlJobInfo xxlJobInfo = new XxlJobInfo();
//        xxlJobInfo.setJobGroup(4);
//        xxlJobInfo.setJobDesc("test");
//        xxlJobInfo.setScheduleType("1");
//        xxlJobInfo.setMisfireStrategy("1");
//        xxlJobInfo.setExecutorTimeout(1);
//        xxlJobInfo.setExecutorFailRetryCount(0);
//        xxlJobInfo.setGlueType("09");
//        xxlJobInfoService.add(xxlJobInfo);

        XxlJobResult test = xxlJobProvider.triggerJob(5,
                "{\"planKey\" : \"Lens_01_rate\",  \"batchId\" : \"\", \"msgTypeInfo\" : [{\"msgType\": \"FeiShu\", \"robotUrl\": \"https://open.feishu.cn/open-apis/bot/v2/hook/5260a48e-c8ba-4cd1-8dc6-93157054264a\"}]}",
                "");
//
        XxlJobResult test1 = xxlJobProvider.triggerJob(5,
                "{\"planKey\" : \"Lens_01_rate\",  \"batchId\" : \"\", \"msgTypeInfo\" : [{\"msgType\": \"FeiShu\", \"robotUrl\": \"https://open.feishu.cn/open-apis/bot/v2/hook/5260a48e-c8ba-4cd1-8dc6-93157054264a\"}]}",
                "");
//
//        try {
////            sendMessageService.sendHandledMessage("https://open.feishu.cn/open-apis/bot/v2/hook/7166ba22-e911-45ba-83d2-25f1f3adff34", "Lens_01_rate", null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Map<String, String> keyMap = new HashMap<>();
//        String msgExpr = "TITLE=**诚瑞光学销售出货数据**@@URL=https://tableau.aacoptics.com/views/new_16407747512500/22?:refresh=y&:embed=y@@AGENT_ID=1186196480@@TABLE=tab_01_product_content@@GROUP_COLUMN=tab_product_type@@GROUP=Lens,模组,汇总@@DATE_COLUMN=title_time@@DATE_FORMAT=yyyy-MM-dd@@ATTRIBUTE=日计划出货数量：, 日实际出货数量：, 日出货数量达成：,**当月计划出货数量：** @@ATTRIBUTE_VALUE=ship_plan_qty,ship_qty, ship_qty_rate,ship_plan_amount@@ATTRIBUTE_COLUMN=ISNULL(ship_plan_qty,0) as ship_plan_qty,ISNULL(ship_qty,0) as ship_qty,ISNULL(ship_qty_rate,0) as ship_qty_rate,ISNULL(ship_plan_amount,0) as ship_plan_amount@@MSG=日期：${date}  &&t( **${group}**  &&t[${attribute}${attributeValue}  &&t]&nbsp;  &&t)";
//        String[] keyArray = msgExpr.split("@@");
////        if(keyArray.length == 0)
////        {
////            insertJobParam.put("MSG", "消息表达式属性不能为空！");
////            planDataMapper.insertPlanJob(insertJobParam);
////
////            return;
////        }
//        for(String keyTxt:keyArray)
//        {
//            String[] attrArr = keyTxt.split("=");
//            if(attrArr.length != 0)
//            {
//                keyMap.put(attrArr[0], attrArr[1]);
//            }
//        }
    }

    @Test
    public void test1() {
        try {
            if (FileUtil.exist("C:\\Users\\xkz19\\Desktop\\test.xlsx"))
                FileUtil.del("C:\\Users\\xkz19\\Desktop\\test.xlsx");
            URL url = new URL("http://10.69.76.50:8082/webroot/decision/view/report?viewlet=oa%252Foa_008_ali_travel_detail.cpt&ref_t=design&op=write&date1=2022-07-01&date2=2022-07-01&obj1=%E8%B4%A8%E9%87%8F%E9%83%A8&format=excel&extype=simple");
            FileUtils.copyURLToFile(url, new File("C:\\Users\\xkz19\\Desktop\\test.xlsx"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2() {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setBatchId("22081914480");
        notificationEntity.setPlanKey("ums_sop_ri_cost_gp_qas");
        Robot robot = new Robot();
        robot.setId(1560167307305877506L);
        List<Robot> robots = new ArrayList<>();
        robots.add(robot);
        notificationEntity.setMsgTypeInfo(robots);
        sendMessageService.sendHandledMessage(notificationEntity);


    }
}