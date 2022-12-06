package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.DataDictConstants;
import com.aacoptics.wlg.equipment.constant.PeriodUnitConstants;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceItem;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.MaintenanceItemMapper;
import com.aacoptics.wlg.equipment.mapper.MaintenanceMainMapper;
import com.aacoptics.wlg.equipment.provider.DataDictProvider;
import com.aacoptics.wlg.equipment.service.MaintenanceItemService;
import com.aacoptics.wlg.equipment.service.MaintenanceMainService;
import com.aacoptics.wlg.equipment.util.DataDictUtil;
import com.aacoptics.wlg.equipment.util.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class MaintenanceMainServiceImpl extends ServiceImpl<MaintenanceMainMapper, MaintenanceMain> implements MaintenanceMainService {

    @Resource
    MaintenanceMainMapper maintenanceMainMapper;

    @Resource
    MaintenanceItemMapper maintenanceItemMapper;

    @Resource
    MaintenanceItemService maintenanceItemService;

    @Resource
    EquipmentServiceImpl equipmentService;

    @Resource
    DataDictProvider dataDictProvider;

    @Override
    public IPage<MaintenanceMain> query(Page page, MaintenanceQueryParam maintenanceQueryParam) {
        QueryWrapper<MaintenanceMain> queryWrapper = maintenanceQueryParam.build();
        queryWrapper.like(StringUtils.isNotBlank(maintenanceQueryParam.getMchName()), "mch_name", maintenanceQueryParam.getMchName());
        queryWrapper.like(StringUtils.isNotBlank(maintenanceQueryParam.getSpec()), "spec", maintenanceQueryParam.getSpec());
        queryWrapper.like(StringUtils.isNotBlank(maintenanceQueryParam.getTypeVersion()), "type_version", maintenanceQueryParam.getTypeVersion());
        queryWrapper.orderByAsc("mch_name");
        queryWrapper.orderByAsc("spec");
        queryWrapper.orderByAsc("type_version");

        return this.page(page, queryWrapper);
    }

    @Override
    public List<MaintenanceMain> queryMaintenanceDataByCondition(MaintenanceQueryParam maintenanceQueryParam) {

        List<MaintenanceMain> maintenanceMainAndItemList = maintenanceMainMapper.findMaintenanceMainAndItemList(maintenanceQueryParam);

        return maintenanceMainAndItemList;
    }

    @Transactional
    @Override
    public void importMaintenanceExcel(InputStream in) throws Exception {
        List<String[]> excelDataList = ExcelUtil.read(in).get(0);

        String[] titleArray = excelDataList.get(0);//标题行

        Result itemTypeResult = dataDictProvider.getDataDictList(DataDictConstants.ITEM_TYPE);
        HashMap<String, String> itemTypeMap = new HashMap<String, String>();
        if(itemTypeResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)itemTypeResult.getData();
            itemTypeMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("获取" + DataDictConstants.ITEM_TYPE + "数据字典失败，" + itemTypeResult.getMsg());
        }

        String mchNameTitle = titleArray[1];
        String specTitle = titleArray[2];
        String typeVersionTitle = titleArray[3];
        String maintenanceItemTitle = titleArray[5];
        if ((!"设备名称".equals(mchNameTitle)) || (!"规格".equals(specTitle))|| (!"型号".equals(typeVersionTitle))|| (!"保养项".equals(maintenanceItemTitle))) {
            throw new BusinessException("Excel模板错误，请确认!");
        }

        for (int i = 1; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if(dataArray == null || dataArray.length == 0)
            {
                break;
            }
            String mchName = dataArray[1]; //设备名称
            String spec = dataArray[2]; //规格
            String typeVersion = dataArray[3]; //型号
            String maintenancePeriodStr = dataArray[4]; //保养周期
            String maintenanceItem = dataArray[5]; //保养项
            String itemTypeDesc = dataArray[6]; //保养项类型
            String maintenanceItemStandard = dataArray[7]; //保养项判断标准
            String minValue = dataArray[8]; //起始范围值
            String maxValue = dataArray[9]; //截至范围值
            String theoreticalValue = dataArray[10]; //理论值

            if (StringUtils.isEmpty(mchName)) {
                throw new BusinessException("第" + (i + 1) + "行设备名称不能为空");
            }
            if (StringUtils.isEmpty(spec)) {
                throw new BusinessException("第" + (i + 1) + "行规格不能为空");
            }
            if (StringUtils.isEmpty(typeVersion)) {
                throw new BusinessException("第" + (i + 1) + "行型号不能为空");
            }
            if(StringUtils.isEmpty(itemTypeDesc))
            {
                throw new BusinessException("第" + (i + 1) + "行保养项类型不能为空");
            }

            Long maintenancePeriod = Long.valueOf(maintenancePeriodStr);
            if(maintenancePeriod < 1)
            {
                throw new BusinessException("第" + (i + 1) + "行保养周期必须大于等于1");
            }
            String itemType = "";
            Set<String> keys = itemTypeMap.keySet();
            for(String key : keys)
            {
                String value = itemTypeMap.get(key);
                if(itemTypeDesc.equals(value))
                {
                    itemType = key;
                }
            }
            try {
                //判断是否存在是否
                Integer equipmentCount = equipmentService.findEquipmentCountList(mchName, spec, typeVersion);
                if(equipmentCount == 0)
                {
                    throw new BusinessException("设备名称为【" + mchName + "】规格【" + spec + "】型号【" + typeVersion + "】的设备不存在，请确认");
                }

                //保存保养设备
                MaintenanceMain maintenanceMain = this.getByMchNameAndSpecAndTypeVersion(mchName, spec, typeVersion);
                if (maintenanceMain == null) {
                    maintenanceMain = new MaintenanceMain();
                    maintenanceMain.setMchName(mchName);
                    maintenanceMain.setSpec(spec);
                    maintenanceMain.setTypeVersion(typeVersion);
                }
                maintenanceMain.setMaintenancePeriod(maintenancePeriod);
                this.saveOrUpdate(maintenanceMain);

                //保存保养项
                Long maintenanceMainId = maintenanceMain.getId();
                MaintenanceItem maintenanceItemObject = this.getItemByMaintenanceItemName(maintenanceMainId, maintenanceItem);
                if(maintenanceItemObject == null)
                {
                    maintenanceItemObject = new MaintenanceItem();
                    maintenanceItemObject.setMaintenanceMainId(maintenanceMainId);
                }
                maintenanceItemObject.setMaintenanceItem(maintenanceItem);
                maintenanceItemObject.setItemType(itemType);
                maintenanceItemObject.setMaintenanceItemStandard(maintenanceItemStandard);
                maintenanceItemObject.setMinValue(new BigDecimal(minValue));
                maintenanceItemObject.setMaxValue(new BigDecimal(maxValue));
                maintenanceItemObject.setTheoreticalValue(theoreticalValue);

                maintenanceItemService.saveOrUpdate(maintenanceItemObject);

            }catch (Exception exception)
            {
                log.error("第{}数据异常：{}", i, excelDataList.get(i));
                log.error("异常信息", exception);
                throw new BusinessException("第【" + (i+1) + "】行数据异常，" + exception.getClass().getSimpleName() + "，"+ exception.getMessage());
            }
        }
    }

    @Override
    public boolean add(MaintenanceMain maintenanceMain) {
        if(maintenanceMain.getMaintenancePeriod() < 1)
        {
            throw new BusinessException("保养周期必须大于等于1，请确认！");
        }

        //校验是否存在
        QueryWrapper<MaintenanceMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", maintenanceMain.getMchName());
        queryWrapper.eq("spec", maintenanceMain.getSpec());
        queryWrapper.eq("type_version", maintenanceMain.getTypeVersion());

        Integer selectCount = maintenanceMainMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        maintenanceMain.setPeriodUnit(PeriodUnitConstants.WEEK);
        boolean isSuccess = this.save(maintenanceMain);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        MaintenanceMain maintenanceMain = this.getById(id);
        if(maintenanceMain != null) {
            List<MaintenanceItem> maintenanceItemList = maintenanceMain.getMaintenanceItemList();
            if (maintenanceItemList != null && maintenanceItemList.size() > 0) {
                for (MaintenanceItem maintenanceItem : maintenanceItemList) {
                    maintenanceItemMapper.deleteById(maintenanceItem.getId());
                }
            }
        }
        return this.removeById(id);
    }

    @Override
    public boolean update(MaintenanceMain maintenanceMain) {
        if(maintenanceMain.getMaintenancePeriod() < 1)
        {
            throw new BusinessException("保养周期必须大于等于1，请确认！");
        }
        //校验是否存在
        QueryWrapper<MaintenanceMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", maintenanceMain.getMchName());
        queryWrapper.eq("spec", maintenanceMain.getSpec());
        queryWrapper.eq("type_version", maintenanceMain.getTypeVersion());
        queryWrapper.ne("id", maintenanceMain.getId());

        Integer selectCount = maintenanceMainMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        boolean isSuccess = this.updateById(maintenanceMain);
        return isSuccess;
    }


    @Override
    public MaintenanceMain get(Long id) {
        MaintenanceMain maintenanceMain = this.getById(id);
        if (Objects.isNull(maintenanceMain)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        //查询保养项
        QueryWrapper<MaintenanceItem> maintenanceItemQueryWrapper = new QueryWrapper<MaintenanceItem>();
        maintenanceItemQueryWrapper.eq( "maintenance_main_id", id);

        maintenanceItemQueryWrapper.orderByAsc("maintenance_item");
        List<MaintenanceItem> maintenanceItemList = maintenanceItemMapper.selectList(maintenanceItemQueryWrapper);


        maintenanceMain.setMaintenanceItemList(maintenanceItemList);

        return maintenanceMain;
    }

    @Override
    public MaintenanceMain getByMchNameAndSpecAndTypeVersion(String mchName, String spec, String typeVersion) {
        //校验是否存在
        QueryWrapper<MaintenanceMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", mchName);
        queryWrapper.eq("spec", spec);
        queryWrapper.eq("type_version", typeVersion);

        MaintenanceMain maintenanceMain = maintenanceMainMapper.selectOne(queryWrapper);

        return maintenanceMain;
    }

    @Override
    public MaintenanceItem getItemByMaintenanceItemName(Long maintenanceMainId, String maintenanceItem) {
        QueryWrapper<MaintenanceItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("maintenance_item", maintenanceItem);
        queryWrapper.eq("maintenance_main_id", maintenanceMainId);

        MaintenanceItem maintenanceItemObject = maintenanceItemMapper.selectOne(queryWrapper);

        return maintenanceItemObject;
    }
}
