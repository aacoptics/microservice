package com.aacoptics.notification.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.po.DingtalkUser;
import com.aacoptics.notification.entity.po.XxlGroupInfo;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.entity.po.XxlJobResult;
import com.aacoptics.notification.provider.XxlJobProvider;
import com.aacoptics.notification.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
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
    XxlJobInfoService xxlJobInfoService;

    @Resource
    XxlGroupInfoService xxlGroupInfoService;

    @Resource
    DingtalkUserService dingtalkUserService;

    @Resource
    FeishuService feishuService;


    @Test
    public void Test1() {

        JSONObject text = new JSONObject();
        text.set("content", "**管理费用预算与实际费用统计（测试）** \n;时间：2022年3月;您负责部门费用如下;当月实际费用：￥14,616 K;当月预算金额：￥12,103 K;GAP节省费用：￥1,487 K;");
        text.set("tag", "lark_md");
        JSONArray elements = new JSONArray();
        JSONObject element = new JSONObject();
        element.set("tag", "div");
        element.set("text", text);
        elements.add(element);

        JSONObject config = new JSONObject();
        config.set("wide_screen_mode", true);

        JSONObject card = new JSONObject();
        card.set("config", config);
        card.set("elements", elements);
        feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_USER_ID, "7c84331d", FeishuService.MSG_TYPE_INTERACTIVE, card);

    }

    @Test
    public void Test() {

        List<DingtalkUser> dingtalkUsers = dingtalkUserService.GetUsersInfoFromDingtalk("60054916");
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
}