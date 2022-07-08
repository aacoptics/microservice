package com.aacoptics.wlg.report.service;


import com.taobao.api.ApiException;

public interface DingTalkNotificationService {


    /**
     * 推送每日产出报告到钉钉群
     *
     * 消息样例：
     * WLG项目汇总产出数据 (X月X日）		主题:WLG项目汇总产出数据 (X月X日）
     * 1、单日产出数据		固定格式：1、单日产出数据
     * 计划产出（颗）：3,936		每日产出报表中当日所有项目JHHDCHANCHU之和或者是月度汇总报表中当日所有项目JHHDCHANCHU之和
     * 实际外观检产出（颗）：0		月度汇总报表中当日所有项目SJHDCHANCHU之和
     * 实际转镀膜入库（颗）：0		月度汇总报表中当日所有项目SJHDCHANCHU之和
     * 实际产出达成率(实际外观检产出/计划产出）：0.0% （4/5清明节，只开模压工序，无转出，同时现在项目要求芯厚分档，按照现在4个分档要求，数量最多的一档也需3天流转）		实际转镀膜入库（颗）/计划产出（颗）
     * 2、累计产出数据		固定格式：2、累计产出数据
     * 计划累计产出（颗）：18,071		月度汇总报表中所有项目JHLEIJI之和
     * 实际累计产出（颗）：6,243		月度汇总报表中所有项目SHIJILEI之和
     * 实际累计入库（颗）：6,243		月度汇总报表中所有项目SHIJILEI之和
     * 实际累计产出达成率（实际累计产出/计划累计产出）：34.5% 		实际累计入库（颗）/计划累计产出（颗）
     *
     * 项目：H1507062（Lion）		固定格式：项目：XXX（月度汇总中所有项目都要显示）
     * 开机状况：2寸量产：2台；调试：3台；总计5台；
     * 1、单日产出数据		固定格式：1、单日产出数据
     * 计划生产模数（模）：191		月份汇总报表中该项目当日JHTOURU数值
     * 实际生产模数（模）：143		月份汇总报表中该项目当日SJCHANCHU数值
     * 计划产出(颗)：2,077		月份汇总报表中该项目当日JHHDCHANCHU数值
     * 预估模压产出（颗）：0		单个项目报表中该项目当日SJTOURU*SJXUESHU数据*预估直通率报表中该项目当日的预估直通率
     * 实际外观检产出（颗）：0		月度汇总报表中当日该项目SJHDCHANCHU数值
     * 实际转镀膜入库（颗）：0		月度汇总报表中当日该项目SJHDCHANCHU数值
     * 实际产出达成率(实际外观检产出/计划产出）：0%		该项目的实际转镀膜入库（颗）/计划产出（颗）
     * 2、累计产出数据		固定格式：2、累计产出数据
     * 计划累计产出（颗）： 8,774		月度汇总报表中当日该项目JHLEIJI数值
     * 实际累计产出（颗）：3,965		月度汇总报表中当日该项目SHIJILEIJI数值
     * 实际累计入库（颗）：3,965		月度汇总报表中当日该项目SHIJILEIJI数值
     * 实际累计产出达成率（实际累计产出/计划累计产出）：45.2%		该项目的实际累计入库（颗）/计划累计产出（颗）
     *
     * @throws ApiException
     */
    void sendProductionDayDataNotification(String groupType) throws ApiException;

    /**
     * 以图片形式推送WLG生产数据
     *
     * @param groupType
     * @throws ApiException
     */
    void sendWLGProductionDayDataImageNotification(String groupType) throws ApiException;


    /**
     * 以图片形式推送G+P数据
     *
     * @param groupType
     * @throws ApiException
     */
    void sendGPProductionDayDataImageNotification(String groupType) throws ApiException;
}
