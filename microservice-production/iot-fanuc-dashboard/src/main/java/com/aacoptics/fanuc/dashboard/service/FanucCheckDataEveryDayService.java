package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.FanucCheckDataEveryDay;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface FanucCheckDataEveryDayService extends IService<FanucCheckDataEveryDay> {

    /**
     * 查询注塑机每日点检表
     *
     * @param startTime
     * @param endTime
     * @param machineNameList
     * @param moldFileNameList
     * @return
     */
    List<FanucCheckDataEveryDay> getFanucCheckDataEveryDay(List<String> machineNameList, List<String> moldFileNameList, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取所有机台
     *
     * @return
     */
    List<String> getAllMachineName();

    /**
     * 获取所有项目
     *
     * @return
     */
    List<String> getAllMoldFileName();
}
