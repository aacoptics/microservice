package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.EquipmentStatus;
import com.aacoptics.wlg.equipment.entity.param.EquipmentQueryParam;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.EquipmentMapper;
import com.aacoptics.wlg.equipment.service.EquipmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    @Resource
    EquipmentMapper equipmentMapper;


    @Override
    public List<Map<String, Object>> findWlgEquipmentByEAM() {

        //1 资产使用性质为1（生产测试类设备）
        //2 资产位置信息所在城市为重庆
        List<Map<String, Object>> wlgEquipmentList = equipmentMapper.findEquipmentByEAM("1", "CQ");

        return wlgEquipmentList;
    }

    @Override
    public void saveEquipment(Map<String, Object> equipmentMap) {
        if (equipmentMap == null || equipmentMap.size() == 0) {
            log.warn("equipmentMap为空，不能保存");
            return;
        }
        Equipment equipment = new Equipment();
        equipment.setMchCode(equipmentMap.get("MCH_CODE") != null ? equipmentMap.get("MCH_CODE")+"" : null);
        equipment.setMchName(equipmentMap.get("MCH_NAME") != null ? equipmentMap.get("MCH_NAME")+"" : null);
        equipment.setSpec(equipmentMap.get("SPEC") != null ? equipmentMap.get("SPEC")+"" : null);
        equipment.setTypeVersion(equipmentMap.get("TYPE_VERSION") != null ? equipmentMap.get("TYPE_VERSION")+"" : null);
        equipment.setAssetGeneralCode(equipmentMap.get("ASSET_GENERAL_CODE") != null ? equipmentMap.get("ASSET_GENERAL_CODE")+"" : null);
        equipment.setAssetGeneralDesc(equipmentMap.get("ASSET_GENERAL_DESC") != null ? equipmentMap.get("ASSET_GENERAL_DESC")+"" : null);
        equipment.setAssetClassifyCode(equipmentMap.get("ASSET_CLASSIFY_CODE") != null ? equipmentMap.get("ASSET_CLASSIFY_CODE")+"" : null);
        equipment.setAssetClassifyDesc(equipmentMap.get("ASSET_CLASSIFY_DESC") != null ? equipmentMap.get("ASSET_CLASSIFY_DESC")+"" : null);
        equipment.setMajorBigCode(equipmentMap.get("MAJOR_BIG_CODE") != null ? equipmentMap.get("MAJOR_BIG_CODE")+"" : null);
        equipment.setMajorBigDesc(equipmentMap.get("MAJOR_BIG_DESC") != null ? equipmentMap.get("MAJOR_BIG_DESC")+"" : null);
        equipment.setMajorSmallCode(equipmentMap.get("MAJOR_SMALL_CODE") != null ? equipmentMap.get("MAJOR_SMALL_CODE")+"" : null);
        equipment.setMajorSmallDesc(equipmentMap.get("MAJOR_SMALL_DESC") != null ? equipmentMap.get("MAJOR_SMALL_DESC")+"" : null);
        equipment.setFactoryNo(equipmentMap.get("FACTORY_NO") != null ? equipmentMap.get("FACTORY_NO")+"" : null);
        equipment.setLocationNo(equipmentMap.get("LOCATION_NO") != null ? equipmentMap.get("LOCATION_NO")+"" : null);
        equipment.setAreaCode(equipmentMap.get("AREA_CODE") != null ? equipmentMap.get("AREA_CODE")+"" : null);
        equipment.setAreaName(equipmentMap.get("AREA_NAME") != null ? equipmentMap.get("AREA_NAME")+"" : null);
        equipment.setFactoryId(equipmentMap.get("FACTORY_ID") != null ? equipmentMap.get("FACTORY_ID")+"" : null);
        equipment.setFactoryName(equipmentMap.get("FACTORY_NAME") != null ? equipmentMap.get("FACTORY_NAME")+"" : null);
        equipment.setBuildingId(equipmentMap.get("BUILDING_ID") != null ? equipmentMap.get("BUILDING_ID")+"" : null);
        equipment.setBuildingName(equipmentMap.get("BUILDING_NAME") != null ? equipmentMap.get("BUILDING_NAME")+"" : null);
        equipment.setFloorCode(equipmentMap.get("FLOOR_CODE") != null ? equipmentMap.get("FLOOR_CODE")+"" : null);
        equipment.setFloorName(equipmentMap.get("FLOOR_NAME") != null ? equipmentMap.get("FLOOR_NAME")+"" : null);
        equipment.setAssetManagerId(equipmentMap.get("ASSET_MANAGER_ID") != null ? equipmentMap.get("ASSET_MANAGER_ID")+"" : null);
        equipment.setMchManagerId(equipmentMap.get("MCH_MANAGER_ID") != null ? equipmentMap.get("MCH_MANAGER_ID")+"" : null);
        equipment.setDutyPersonId(equipmentMap.get("DUTY_PERSON_ID") != null ? equipmentMap.get("DUTY_PERSON_ID")+"" : null);
        equipment.setDutyPersonConnect(equipmentMap.get("DUTY_PERSON_CONNECT") != null ? equipmentMap.get("DUTY_PERSON_CONNECT")+"" : null);
        equipment.setDeptManagerId(equipmentMap.get("DEPT_MANAGER_ID") != null ? equipmentMap.get("DEPT_MANAGER_ID")+"" : null);
        equipment.setDeptDirectorId(equipmentMap.get("DEPT_DIRECTOR_ID") != null ? equipmentMap.get("DEPT_DIRECTOR_ID")+"" : null);


        LocalDateTime currentDateTime = LocalDateTime.now();
        equipment.setLastInspectionDatetime(currentDateTime);
        equipment.setLastMaintenanceDatetime(currentDateTime);
        equipment.setStatus(EquipmentStatus.NORMAL); //设备初始状态为正常
        this.save(equipment);
    }

    @Override
    public void saveWlgEquipmentFormEAM() {
        log.info("开始从EAM获取WLG设备，并保存到WLGIOT数据库");
        List<Map<String, Object>> wlgEquipmentList = this.findWlgEquipmentByEAM();
        if(wlgEquipmentList == null || wlgEquipmentList.size() == 0)
        {
            log.info("获取WLG设备为空");
            return;
        }
        for(int i=0; i<wlgEquipmentList.size(); i++)
        {
            Map<String, Object> equipmentMap = wlgEquipmentList.get(i);
            this.saveEquipment(equipmentMap);
        }
        log.info("完成从EAM获取WLG设备，并保存到WLGIOT数据库");
    }

    @Override
    public IPage<Equipment> query(Page page, EquipmentQueryParam equipmentQueryParam) {
        QueryWrapper<Equipment> queryWrapper = equipmentQueryParam.build();
        queryWrapper.like(StringUtils.isNotBlank(equipmentQueryParam.getMchCode()), "mch_code", equipmentQueryParam.getMchCode());
        queryWrapper.like(StringUtils.isNotBlank(equipmentQueryParam.getMchName()), "mch_name", equipmentQueryParam.getMchName());
        queryWrapper.like(StringUtils.isNotBlank(equipmentQueryParam.getSpec()), "spec", equipmentQueryParam.getSpec());
        queryWrapper.like(StringUtils.isNotBlank(equipmentQueryParam.getTypeVersion()), "type_version", equipmentQueryParam.getTypeVersion());
        queryWrapper.like(StringUtils.isNotBlank(equipmentQueryParam.getFactoryNo()), "factory_no", equipmentQueryParam.getFactoryNo());
        queryWrapper.like(StringUtils.isNotBlank(equipmentQueryParam.getLocationNo()), "location_no", equipmentQueryParam.getLocationNo());
        queryWrapper.eq(StringUtils.isNotBlank(equipmentQueryParam.getAssetManagerId()), "asset_manager_id", equipmentQueryParam.getAssetManagerId());
        queryWrapper.eq(StringUtils.isNotBlank(equipmentQueryParam.getMchManagerId()), "mch_manager_id", equipmentQueryParam.getMchManagerId());
        queryWrapper.eq(StringUtils.isNotBlank(equipmentQueryParam.getDutyPersonId()), "duty_person_connect", equipmentQueryParam.getDutyPersonId());

        queryWrapper.orderByAsc("mch_code");
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean add(Equipment equipment) {
        boolean isSuccess = this.save(equipment);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(Equipment equipment) {

        boolean isSuccess = this.updateById(equipment);
        return isSuccess;
    }


    @Override
    public Equipment get(Long id) {
        Equipment equipment = this.getById(id);
        if (Objects.isNull(equipment)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }
        return equipment;
    }

    @Override
    public List<String> findMchNameList() {
        return equipmentMapper.findMchNameList();
    }

    @Override
    public List<String> findSpecListByMchName(EquipmentQueryParam equipmentQueryParam) {
        String mchName = equipmentQueryParam.getMchName();
        return equipmentMapper.findSpecListByMchName(mchName);
    }

    @Override
    public List<String> findTypeVersionListByMchNameAndSpec(EquipmentQueryParam equipmentQueryParam) {
        String mchName = equipmentQueryParam.getMchName();
        String spec = equipmentQueryParam.getSpec();

        return equipmentMapper.findTypeVersionListByMchNameAndSpec(mchName, spec);
    }

    @Override
    public List<Equipment> findEquipmentList(String mchName, String spec, String typeVersion) {
        QueryWrapper<Equipment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", mchName);
        queryWrapper.eq("spec",spec);
        queryWrapper.eq("type_version", typeVersion);

        queryWrapper.orderByAsc("mch_code");

       return equipmentMapper.selectList(queryWrapper);
    }
}
