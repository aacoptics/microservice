package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.param.FanucCheckItemThresholdParam;
import com.aacoptics.fanuc.dashboard.entity.po.FanucCheckItemThreshold;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FanucCheckItemThresholdService extends IService<FanucCheckItemThreshold> {

    /**
     * 根据条件查询检查项阈值信息
     *
     * @return
     */
    IPage<FanucCheckItemThreshold> query(Page page, FanucCheckItemThresholdParam fanucCheckItemThresholdParam);


    /**
     * 更新检查项阈值信息
     *
     * @param fanucCheckItemThreshold
     */
    boolean update(FanucCheckItemThreshold fanucCheckItemThreshold);

    /**
     * 根据id删除检查项阈值
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增检查项阈值
     *
     * @param fanucCheckItemThreshold
     * @return
     */
    boolean add(FanucCheckItemThreshold fanucCheckItemThreshold);



    /**
     * 获取检查项阈值设备
     *
     * @param id
     * @return
     */
    FanucCheckItemThreshold get(Long id);


    /**
     * 获取检查项阈值
     *
     * @return
     */
    List<FanucCheckItemThreshold> findFanucCheckItemThresholdList();


}
