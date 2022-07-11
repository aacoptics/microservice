package com.aacoptics.mold.toollife.service.impl;

import com.aacoptics.mold.toollife.consumer.MoldConsumer;
import com.aacoptics.mold.toollife.dao.ToolInfoMapper;
import com.aacoptics.mold.toollife.entity.ToolInfo;
import com.aacoptics.mold.toollife.entity.ToolInfoHistory;
import com.aacoptics.mold.toollife.entity.ToolMachineNo;
import com.aacoptics.mold.toollife.entity.UpdateSheetForm;
import com.aacoptics.mold.toollife.service.ToolInfoHistoryService;
import com.aacoptics.mold.toollife.service.ToolInfoService;
import com.aacoptics.mold.toollife.util.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ToolInfoServiceImpl extends ServiceImpl<ToolInfoMapper, ToolInfo> implements ToolInfoService {

    @Autowired
    ToolInfoMapper toolInfoMapper;

    @Autowired
    ToolInfoHistoryService toolInfoHistoryService;


    @Override
    public String phaseExcelData(InputStream in) {
        List<String[]> excelDataList;
        List<ToolInfo> toolInfos = new ArrayList<>();
        String monitorNo = "";
        try {
            excelDataList = ExcelUtil.read(in).get(0);
            String workpiece = excelDataList.get(3)[2];
            monitorNo = excelDataList.get(3)[12];
            if (StringUtils.isBlank(monitorNo))
                return "";
            String material = excelDataList.get(16)[17];
            Pattern isSwo = Pattern.compile("^O?([\\d]{3,5})+([\\S]*)?$");
            String tempRoute = "";
            for (String[] strings : excelDataList) {
                String programName = strings[1].trim();
                if (!StringUtils.isBlank(programName) & isSwo.matcher(programName).matches()) {
                    if (programName.indexOf("O") != 0) {
                        programName = "O" + programName;
                    }
                    ToolInfo toolInfo = new ToolInfo();
                    toolInfo.setWorkpiece(workpiece)
                            .setMonitorNo(monitorNo)
                            .setMaterial(material);
                    if (!StringUtils.isBlank(strings[0])) {
                        toolInfo.setRoute(strings[0]);
                        tempRoute = strings[0];
                    } else {
                        toolInfo.setRoute(tempRoute);
                    }
                    toolInfo.setProgramName(programName)
                            .setToolName(strings[5])
                            .setToolDiameter(strings[7])
                            .setToolNo(strings[8])
                            .setType(strings[10])
                            .setMargin(strings[12])
                            .setToolValidLength(strings[13])
                            .setMaxDepth(strings[15])
                            .setWorkTime(strings[18])
                            .setCutDepth(strings[19])
                            .setFeed(strings[20])
                            .setSpeed(strings[21])
                            .setRemark(strings[22])
                            .setCreateDateTime(LocalDateTime.now());
                    if (!strings[14].equals("#N/A"))
                        toolInfo.setBrand(strings[14]);

                    List<ToolInfo> ToolInfoInDb = getToolInfo(toolInfo.getMonitorNo(), toolInfo.getProgramName());
                    if (ToolInfoInDb.size() > 0) {
                        toolInfo.setId(ToolInfoInDb.get(0).getId());
                        this.updateById(toolInfo);
                    } else {
                        this.save(toolInfo);
                    }
                    toolInfos.add(toolInfo);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return monitorNo;
    }

    @Override
    public List<ToolInfo> getToolInfo(String monitorNo, String programName) {
        QueryWrapper<ToolInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("monitor_no", monitorNo)
                .eq("program_name", programName);
        return this.list(wrapper);
    }

    @Override
    public List<ToolInfo> getToolInfo(String monitorNo) {
        QueryWrapper<ToolInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("monitor_no", monitorNo);
        List<ToolInfo> toolInfos = list(wrapper);
        for (ToolInfo toolInfo : toolInfos) {
            String matToolCode = toolInfo.getMatToolCode();
            if(StringUtils.isNotBlank(matToolCode)) {
                int actualLifeNumber = toolInfoMapper.getActualLifeByToolCode(matToolCode);
                if(actualLifeNumber == 0) {
                    toolInfo.setActualLife("");
                } else {
                    toolInfo.setActualLife(actualLifeNumber + "");
                }
                if(StringUtils.isNotBlank(toolInfo.getLifeSalvage())) {
                    int leftLifeNumber = Integer.parseInt(toolInfo.getLifeSalvage()) - actualLifeNumber;
                    toolInfo.setLeftLife(leftLifeNumber + "");
                }


            }
            toolInfo.SetMoldMatInfo();
        }
        return toolInfos;
    }

    @Override
    public List<ToolInfo> getToolInfoByMonitorNoAndMachineNo(String monitorNo, String machineNo) {
        QueryWrapper<ToolInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("monitor_no", monitorNo).eq("machine_no", machineNo);
        List<ToolInfo> toolInfos = list(wrapper);
        for (ToolInfo toolInfo : toolInfos) {
            String matToolCode = toolInfo.getMatToolCode();
            if(StringUtils.isNotBlank(matToolCode)) {
                int actualLifeNumber = toolInfoMapper.getActualLifeByToolCode(matToolCode);
                if(actualLifeNumber == 0) {
                    toolInfo.setActualLife("");
                } else {
                    toolInfo.setActualLife(actualLifeNumber + "");
                }
                int leftLifeNumber = Integer.parseInt(toolInfo.getLifeSalvage()) - actualLifeNumber;
                toolInfo.setLeftLife(leftLifeNumber + "");
            }
            toolInfo.SetMoldMatInfo();
        }
        return toolInfos;
    }

    @Override
    public List<ToolMachineNo> getToolMachineNo(String monitorNo) {
        List<ToolInfo> totalToolInfoList = getToolInfo(monitorNo);
        List<ToolMachineNo> totalMachineNoList = new ArrayList<ToolMachineNo>();
        for(ToolInfo toolInfo : totalToolInfoList) {
            ToolMachineNo toolMachineNo = new ToolMachineNo();
            toolMachineNo.setId(toolInfo.getId());
            toolMachineNo.setMachineNo(toolInfo.getMachineNo());
            totalMachineNoList.add(toolMachineNo);
        }
        return totalMachineNoList;
    }



    @Transactional
    @Override
    public boolean updateToolLifeInfo(List<ToolInfo> toolInfos) {
        try {
            for (ToolInfo toolInfo : toolInfos) {
                //如果刀柄编码不为空，将该条记录插入到history表中 ，然后再更新sheet表
//                if (!StringUtils.isBlank(toolInfo.getMachineNo()) ||
//                        (toolInfo.getMatInfo() != null &&
//                                !StringUtils.isBlank(toolInfo.getMatInfo().getHandleCode()))) {
//                    updateInfo(toolInfo);
//                }
                QueryWrapper<ToolInfo> wrapper = new QueryWrapper<>();
                wrapper.eq("id", toolInfo.getId());
                ToolInfo toolInfoQuery = getOne(wrapper);
                String matHandleCode = toolInfoQuery.getMatHandleCode();
                String matToolCode = toolInfoQuery.getMatToolCode();

                if (!StringUtils.isBlank(matHandleCode)) {
                    //如果修改后的刀具编码跟原来的相同 那就跳过该条数据 执行下一个循环
                    if(matToolCode.equals(toolInfo.getMatInfo().getToolCode())) {
                        continue;
                    }
                    ToolInfoHistory toolInfoHistory = new ToolInfoHistory();
                    toolInfoHistory.setWorkpiece(toolInfo.getWorkpiece());
                    toolInfoHistory.setMonitorNo(toolInfo.getMonitorNo());
                    toolInfoHistory.setMaterial(toolInfo.getMaterial());
                    toolInfoHistory.setRoute(toolInfo.getRoute());
                    toolInfoHistory.setProgramName(toolInfo.getProgramName());
                    toolInfoHistory.setToolDiameter(toolInfo.getToolDiameter());
                    toolInfoHistory.setToolNo(toolInfo.getToolNo());
                    toolInfoHistory.setType(toolInfo.getType());
                    toolInfoHistory.setMargin(toolInfo.getMargin());
                    toolInfoHistory.setToolValidLength(toolInfo.getToolValidLength());
                    toolInfoHistory.setBrand(toolInfo.getBrand());
                    toolInfoHistory.setMaxDepth(toolInfo.getMaxDepth());
                    toolInfoHistory.setWorkTime(toolInfo.getWorkTime());
                    toolInfoHistory.setCutDepth(toolInfo.getCutDepth());
                    toolInfoHistory.setFeed(toolInfo.getFeed());
                    toolInfoHistory.setRemark(toolInfo.getRemark());
                    toolInfoHistory.setCreateDateTime(toolInfo.getCreateDateTime());
                    toolInfoHistory.setMachineNo(toolInfo.getMachineNo());
                    toolInfoHistory.setToolName(toolInfo.getToolName());
//                    toolInfoHistory.setMatHandleCode(toolInfo.getMatHandleCode());
//                    toolInfoHistory.setMatToolCode(toolInfo.getMatToolCode());
//                    toolInfoHistory.setMatCode(toolInfo.getMatCode());
//                    toolInfoHistory.setMatName(toolInfo.getMatName());
                    toolInfoHistory.setMatHandleCode(toolInfo.getMatInfo().getHandleCode());
                    toolInfoHistory.setMatToolCode(toolInfo.getMatInfo().getToolCode());
                    toolInfoHistory.setMatCode(toolInfo.getMatInfo().getMatCode());
                    toolInfoHistory.setMatName(toolInfo.getMatInfo().getMatName());
                    toolInfoHistory.setLifeSalvage(toolInfo.getLifeSalvage());
                    toolInfoHistory.setMaintenanceDateTime(toolInfo.getMaintenanceDateTime());
                    toolInfoHistoryService.addToolInfoHistory(toolInfoHistory);

                    updateInfo(toolInfo);
                } else {
                    updateInfo(toolInfo);
                }


            }
            return true;
        } catch (Exception err) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean addMachineToolLifeInfo(UpdateSheetForm updateSheetForm) {
        try {
            for (ToolInfo toolInfo : updateSheetForm.getToolInfos()) {
                toolInfo.setMatInfo(null)
                        .setMatHandleCode(null)
                        .setMatToolCode(null)
                        .setMatCode(null)
                        .setMatName(null)
                        .setLifeSalvage(null)
                        .setMachineNo(updateSheetForm.getMachineNo());
                this.save(toolInfo);
            }
            return true;
        } catch (Exception err) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    private void updateInfo(ToolInfo toolInfo) {
        UpdateWrapper<ToolInfo> updateWrapper = new UpdateWrapper<>();
        if (toolInfo.getMatInfo() != null) {
            updateWrapper.set("mat_handle_code", toolInfo.getMatInfo().getHandleCode())
                    .set("mat_tool_code", toolInfo.getMatInfo().getToolCode())
                    .set("mat_code", toolInfo.getMatInfo().getMatCode())
                    .set("mat_name", toolInfo.getMatInfo().getMatName())
                    .set("life_salvage", toolInfo.getMatInfo().getLifeSalvage())
                    .set("maintenance_date_time", new Date());
        }
        updateWrapper.set("machine_no", toolInfo.getMachineNo())
                .eq("id", toolInfo.getId());
        update(updateWrapper);
    }

    public Map<String, Boolean> getToolMaintainStatus(List<String> monitorNos) {
        Map<String, Boolean> res = new HashMap<>();
        if (monitorNos.size() == 0) {
            return res;
        }
        Set<String> monitorNoSet = new HashSet<>(monitorNos);
        List<ToolInfo> toolInfos = toolInfoMapper.getToolMaintainStatus(monitorNoSet);
        for (String monitorNo : monitorNoSet) {
            List<ToolInfo> temp = toolInfos.stream().filter(toolInfo -> toolInfo.getMonitorNo().equals(monitorNo)).collect(Collectors.toList());
            if (temp.size() > 0 && temp.get(0).getMachineNo() != null) {
                res.put(monitorNo, true);
            } else {
                res.put(monitorNo, false);
            }
        }
        return res;
    }

    public Map<String, Object> getAbnormalToolLifeRatio() {
        List<Map<String, Object>> abnormalToolLifeRatioList = new ArrayList<>();
        abnormalToolLifeRatioList = toolInfoMapper.getAbnormalToolLifeRatio();
        List<String> matCode = new ArrayList<>();
        List<Double> ratio = new ArrayList<>();
        for(Map<String, Object> abnormalToolLifeRatio : abnormalToolLifeRatioList) {
            matCode.add(abnormalToolLifeRatio.get("mat_code").toString());
            ratio.add(Double.parseDouble(abnormalToolLifeRatio.get("lifeRatio").toString()));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("matCode", matCode);
        result.put("ratio", ratio);
        return result;
    }

    public List<Map<String, Object>> getAbnormalQty() {
        List<Map<String, Object>> abnormalQtyMapList = toolInfoMapper.getAbnormalQty();
        int totalQty = 0;
        for(Map<String, Object> abnormalQtyMap : abnormalQtyMapList) {
            totalQty = totalQty + (int) abnormalQtyMap.get("abnormal_qty");
        }
        for(Map<String, Object> abnormalQtyMap : abnormalQtyMapList) {
            double ratio = (((int) abnormalQtyMap.get("abnormal_qty")*1.0) / totalQty) * 100;
            BigDecimal bd = new BigDecimal(ratio);
            double ratioNew = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String ratioString = ratioNew + "%";
            abnormalQtyMap.put("ratio", ratioString);
        }
        return abnormalQtyMapList;
    }

    public List<Map<String, Object>> getMachineStatus() {
        return MoldConsumer.machineStatus;
    }
}