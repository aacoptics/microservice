package com.aacoptics.czech.service;

import com.aacoptics.czech.entity.MachineRemark;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface MachineRemarkService extends IService<MachineRemark> {
    /**
     * 根据机台号保存备注信息
     * @param machineNumber
     * @param content
     * @return
     */
    boolean saveRemark(String machineNumber, String content);

    /**
     * 修改备注信息
     * @param machineRemark
     * @param newContent
     * @return
     */
    void updateRemark(MachineRemark machineRemark, String newContent);

    /**
     * 删除备注信息
     * @param machineRemark
     */
    void deleteRemark(MachineRemark machineRemark);

    /**
     * 根据机台号获取备注信息
     * @param machineNumber
     * @return
     */
    List<Map<String, String>> getRemarkByMachineNumber(String machineNumber);
}
