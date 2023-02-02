package com.aacoptics.notification.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.aacoptics.notification.entity.po.*;
import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.exception.BusinessException;
import com.aacoptics.notification.mapper.FeishuUserMapper;
import com.aacoptics.notification.provider.FeishuApi;
import com.aacoptics.notification.provider.XxlJobProvider;
import com.aacoptics.notification.service.*;
import com.spire.xls.Worksheet;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
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

    @Resource
    FeishuUserMapper feishuUserMapper;

    @Resource
    UmsContentFeishuMsgHistoryService umsContentFeishuMsgHistoryService;

    @Test
    public void Test123(){
        FeishuUser feishuUser = feishuService.getFeishuUser("60054916");

        List<String> ids = new ArrayList<>();
        ids.add("60054916");
        ids.add("80000219");

        List<String> test = feishuUserMapper.getFeishuUserIds(ids);

        String asd = "";
    }


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
//                    fileResult = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID,
//                            chatId,
//                            FeishuService.MSG_TYPE_FILE,
//                            JSONUtil.createObj().set("file_key", fileKey));
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
    public void TestDeleteMessage(){
        String[] test = new String[]{
                "2301180003323",
                "2301180003324",
                "2301180003325",
                "2301180003326",
                "2301180003327",
                "2301190003370",
                "2301190003371",
                "2301190003372",
                "2301190003373",
                "2301190003374",
                "2301190003375",
                "2301190003376",
                "2301190003377",
                "2301190003378",
                "2301190003379",
                "2301190003380",
                "2301190003381",
                "2301190003382",
                "2301190003383",
                "2301190003384",
                "2301190003385",
                "2301190003386",
                "2301190003387",
                "2301190003388",
                "2301190003389",
                "2301190003390",
                "2301190003391",
                "2301190003392",
                "2301190003393",
                "2301190003394",
                "2301190003395",
                "2301190003396",
                "2301190003397",
                "2301190003398",
                "2301190003399",
                "2301190003400",
                "2301190003401",
                "2301190003402",
                "2301190003403",
                "2301190003404",
                "2301190003405",
                "2301190003406",
                "2301190003407",
                "2301190003408",
                "2301190003409",
                "2301190003410",
                "2301190003411",
                "2301190003412",
                "2301190003413",
                "2301190003414",
                "2301190003415",
                "2301190003416",
                "2301190003417",
                "2301190003418",
                "2301190003419",
                "2301190003420",
                "2301190003421",
                "2301190003422",
                "2301190003423",
                "2301190003424",
                "2301190003425",
                "2301190003426",
                "2301190003427",
                "2301190003428",
                "2301190003429",
                "2301190003430",
                "2301190003431",
                "2301190003432",
                "2301190003433",
                "2301190003434",
                "2301190003435",
                "2301190003436",
                "2301190003437",
                "2301190003438",
                "2301190003439",
                "2301190003440",
                "2301190003441",
                "2301190003442",
                "2301190003443",
                "2301190003444",
                "2301190003445",
                "2301190003446",
                "2301190003447",
                "2301190003448",
                "2301190003449",
                "2301190003450",
                "2301190003451",
                "2301190003452",
                "2301190003453",
                "2301190003454",
                "2301190003455",
                "2301190003456",
                "2301190003457",
                "2301190003458",
                "2301190003459",
                "2301190003460",
                "2301190003461",
                "2301190003462",
                "2301190003463",
                "2301190003464",
                "2301190003465",
                "2301190003466",
                "2301190003467",
                "2301190003468",
                "2301190003469",
                "2301190003470",
                "2301190003471",
                "2301190003472",
                "2301190003473",
                "2301190003474",
                "2301190003475",
                "2301190003476",
                "2301190003477",
                "2301190003478",
                "2301190003479",
                "2301190003480",
                "2301190003481",
                "2301190003482",
                "2301190003483",
                "2301190003484",
                "2301190003485",
                "2301190003486",
                "2301190003487",
                "2301190003488",
                "2301190003489",
                "2301190003490",
                "2301190003491",
                "2301190003492",
                "2301190003493",
                "2301190003494",
                "2301190003495",
                "2301190003496",
                "2301190003497",
                "2301190003498",
                "2301190003499",
                "2301190003500",
                "2301190003501",
                "2301190003502",
                "2301190003503",
                "2301190003504",
                "2301190003505",
                "2301190003506",
                "2301190003507",
                "2301190003508",
                "2301190003509",
                "2301190003510",
                "2301190003511",
                "2301190003512",
                "2301190003513",
                "2301190003514",
                "2301190003515",
                "2301190003516",
                "2301190003517",
                "2301190003518",
                "2301190003519",
                "2301190003520",
                "2301190003521",
                "2301190003522",
                "2301190003523",
                "2301190003524",
                "2301190003525",
                "2301190003526",
                "2301190003527",
                "2301190003528",
                "2301190003529",
                "2301190003530",
                "2301190003531",
                "2301190003532",
                "2301190003533",
                "2301190003534",
                "2301190003535",
                "2301190003536",
                "2301190003537",
                "2301190003538",
                "2301190003539",
                "2301190003540",
                "2301190003541",
                "2301190003542",
                "2301190003543",
                "2301190003544",
                "2301190003545",
                "2301190003546",
                "2301190003547",
                "2301190003548",
                "2301190003549",
                "2301190003550",
                "2301190003551",
                "2301190003552",
                "2301190003553",
                "2301190003554",
                "2301190003555",
                "2301190003556",
                "2301190003557",
                "2301190003558",
                "2301190003559",
                "2301190003560",
                "2301190003561",
                "2301190003562",
                "2301190003563",
                "2301190003564",
                "2301190003565",
                "2301190003566",
                "2301190003567",
                "2301190003568",
                "2301190003569",
                "2301190003570",
                "2301190003571",
                "2301190003572",
                "2301190003573",
                "2301190003574",
                "2301190003575",
                "2301190003576",
                "2301190003577",
                "2301190003578",
                "2301190003579",
                "2301190003580",
                "2301190003581",
                "2301190003582",
                "2301190003583",
                "2301190003584",
                "2301190003585",
                "2301190003586",
                "2301190003587",
                "2301190003588",
                "2301190003589",
                "2301190003590",
                "2301190003591",
                "2301190003592",
                "2301190003593",
                "2301190003594"
        };

        for (String s : test) {
            try {
                umsContentFeishuMsgHistoryService.deleteMessageByBatchId(s);
            }
            catch(Exception err){
                log.error(err.getMessage());
            }
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
        notificationEntity.setBatchId("22090716491");
        notificationEntity.setPlanKey("ums_sop_gross_rate_qas");
        Robot robot = new Robot();
        robot.setId(1560167307305877506L);
        List<Robot> robots = new ArrayList<>();
        robots.add(robot);
        notificationEntity.setMsgTypeInfo(robots);
        sendMessageService.sendHandledMessage(notificationEntity);


    }

    @Test
    public void test222() {
        String sql = "SELECT t.id\n" +
                "             , t.job_group\n" +
                "             , t.job_desc\n" +
                "             , t.add_time\n" +
                "             , t.update_time\n" +
                "             , t.author\n" +
                "             , t.alarm_email\n" +
                "             , t.schedule_type\n" +
                "             , t.schedule_conf\n" +
                "             , t.misfire_strategy\n" +
                "             , t.executor_route_strategy\n" +
                "             , t.executor_handler\n" +
                "             , t.executor_param\n" +
                "             , t.executor_block_strategy\n" +
                "             , t.executor_timeout\n" +
                "             , t.executor_fail_retry_count\n" +
                "             , t.glue_type\n" +
                "             , t.glue_source\n" +
                "             , t.glue_remark\n" +
                "             , t.glue_updatetime\n" +
                "             , t.child_jobid\n" +
                "             , t.trigger_status\n" +
                "             , t.trigger_last_time\n" +
                "             , t.trigger_next_time\n" +
                "             , JSON_VALUE(executor_param, '$.planKey')              as 'plan_key'\n" +
                "             , a.id                                                 as notification_id\n" +
                "             , xxl_job_id\n" +
                "             , product_line\n" +
                "             , remark\n" +
                "             , online_time\n" +
                "             , responsible_person\n" +
                "             , responsible_person_name\n" +
                "             , it_person\n" +
                "             , job_status\n" +
                "             , subscription_enabled\n" +
                "             , execute_time\n" +
                "             , job_environment\n" +
                "             , notification_no\n" +
                "             , push_type\n" +
                "             , field_name\n" +
                "             , ISNULL((select approve_status\n" +
                "                       from notification_job_subscription\n" +
                "                       where notification_job_id = a.id\n" +
                "                         and subscription_person = '60054916'), 0) as subscription_status\n" +
                "        from (SELECT id\n" +
                "                   , job_group\n" +
                "                   , job_desc\n" +
                "                   , add_time\n" +
                "                   , update_time\n" +
                "                   , author\n" +
                "                   , alarm_email\n" +
                "                   , schedule_type\n" +
                "                   , schedule_conf\n" +
                "                   , misfire_strategy\n" +
                "                   , executor_route_strategy\n" +
                "                   , executor_handler\n" +
                "                   , executor_param\n" +
                "                   , executor_block_strategy\n" +
                "                   , executor_timeout\n" +
                "                   , executor_fail_retry_count\n" +
                "                   , glue_type\n" +
                "                   , glue_source\n" +
                "                   , glue_remark\n" +
                "                   , glue_updatetime\n" +
                "                   , child_jobid\n" +
                "                   , trigger_status\n" +
                "                   , trigger_last_time\n" +
                "                   , trigger_next_time\n" +
                "              FROM xxl_job_info\n" +
                "              where executor_handler = 'RobotHandle') t\n" +
                "                 join notification_job_info a\n" +
                "                      on t.id = a.xxl_job_id\n" +
                "        where job_environment = 'QAS'";
        try {
            Statement test = CCJSqlParserUtil.parse(sql);
            String asd = "";
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }


    }
}