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

        String[] titleArray = excelDataList.get(0);//?????????

        Result itemTypeResult = dataDictProvider.getDataDictList(DataDictConstants.ITEM_TYPE);
        HashMap<String, String> itemTypeMap = new HashMap<String, String>();
        if(itemTypeResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)itemTypeResult.getData();
            itemTypeMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("??????" + DataDictConstants.ITEM_TYPE + "?????????????????????" + itemTypeResult.getMsg());
        }

        String mchNameTitle = titleArray[1];
        String specTitle = titleArray[2];
        String typeVersionTitle = titleArray[3];
        String maintenanceItemTitle = titleArray[5];
        if ((!"????????????".equals(mchNameTitle)) || (!"??????".equals(specTitle))|| (!"??????".equals(typeVersionTitle))|| (!"?????????".equals(maintenanceItemTitle))) {
            throw new BusinessException("Excel????????????????????????!");
        }

        for (int i = 1; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if(dataArray == null || dataArray.length == 0)
            {
                break;
            }
            String mchName = dataArray[1]; //????????????
            String spec = dataArray[2]; //??????
            String typeVersion = dataArray[3]; //??????
            String maintenancePeriodStr = dataArray[4]; //????????????
            String maintenanceItem = dataArray[5]; //?????????
            String itemTypeDesc = dataArray[6]; //???????????????
            String maintenanceItemStandard = dataArray[7]; //?????????????????????
            String minValue = dataArray[8]; //???????????????
            String maxValue = dataArray[9]; //???????????????
            String theoreticalValue = dataArray[10]; //?????????

            if (StringUtils.isEmpty(mchName)) {
                throw new BusinessException("???" + (i + 1) + "???????????????????????????");
            }
            if (StringUtils.isEmpty(spec)) {
                throw new BusinessException("???" + (i + 1) + "?????????????????????");
            }
            if (StringUtils.isEmpty(typeVersion)) {
                throw new BusinessException("???" + (i + 1) + "?????????????????????");
            }
            if(StringUtils.isEmpty(itemTypeDesc))
            {
                throw new BusinessException("???" + (i + 1) + "??????????????????????????????");
            }

            Long maintenancePeriod = Long.valueOf(maintenancePeriodStr);
            if(maintenancePeriod < 1)
            {
                throw new BusinessException("???" + (i + 1) + "?????????????????????????????????1");
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
                //????????????????????????
                Integer equipmentCount = equipmentService.findEquipmentCountList(mchName, spec, typeVersion);
                if(equipmentCount == 0)
                {
                    throw new BusinessException("??????????????????" + mchName + "????????????" + spec + "????????????" + typeVersion + "?????????????????????????????????");
                }

                //??????????????????
                MaintenanceMain maintenanceMain = this.getByMchNameAndSpecAndTypeVersion(mchName, spec, typeVersion);
                if (maintenanceMain == null) {
                    maintenanceMain = new MaintenanceMain();
                    maintenanceMain.setMchName(mchName);
                    maintenanceMain.setSpec(spec);
                    maintenanceMain.setTypeVersion(typeVersion);
                }
                maintenanceMain.setMaintenancePeriod(maintenancePeriod);
                this.saveOrUpdate(maintenanceMain);

                //???????????????
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
                if(StringUtils.isNotEmpty(minValue)) {
                    maintenanceItemObject.setMinValue(new BigDecimal(minValue));
                }
                if(StringUtils.isNotEmpty(maxValue)) {
                    maintenanceItemObject.setMaxValue(new BigDecimal(maxValue));
                }
                maintenanceItemObject.setTheoreticalValue(theoreticalValue);

                maintenanceItemService.saveOrUpdate(maintenanceItemObject);

            }catch (Exception exception)
            {
                log.error("???{}???????????????{}", i, excelDataList.get(i));
                log.error("????????????", exception);
                throw new BusinessException("??????" + (i+1) + "?????????????????????" + exception.getClass().getSimpleName() + "???"+ exception.getMessage());
            }
        }
    }

    @Override
    public boolean add(MaintenanceMain maintenanceMain) {
        if(maintenanceMain.getMaintenancePeriod() < 1)
        {
            throw new BusinessException("??????????????????????????????1???????????????");
        }

        //??????????????????
        QueryWrapper<MaintenanceMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", maintenanceMain.getMchName());
        queryWrapper.eq("spec", maintenanceMain.getSpec());
        queryWrapper.eq("type_version", maintenanceMain.getTypeVersion());

        Integer selectCount = maintenanceMainMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("????????????????????????????????????");
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
            throw new BusinessException("??????????????????????????????1???????????????");
        }
        //??????????????????
        QueryWrapper<MaintenanceMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", maintenanceMain.getMchName());
        queryWrapper.eq("spec", maintenanceMain.getSpec());
        queryWrapper.eq("type_version", maintenanceMain.getTypeVersion());
        queryWrapper.ne("id", maintenanceMain.getId());

        Integer selectCount = maintenanceMainMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("????????????????????????????????????");
        }

        boolean isSuccess = this.updateById(maintenanceMain);
        return isSuccess;
    }


    @Override
    public MaintenanceMain get(Long id) {
        MaintenanceMain maintenanceMain = this.getById(id);
        if (Objects.isNull(maintenanceMain)) {
            throw new BusinessException("ID???" + id + "?????????????????????");
        }

        //???????????????
        QueryWrapper<MaintenanceItem> maintenanceItemQueryWrapper = new QueryWrapper<MaintenanceItem>();
        maintenanceItemQueryWrapper.eq( "maintenance_main_id", id);

        maintenanceItemQueryWrapper.orderByAsc("maintenance_item");
        List<MaintenanceItem> maintenanceItemList = maintenanceItemMapper.selectList(maintenanceItemQueryWrapper);


        maintenanceMain.setMaintenanceItemList(maintenanceItemList);

        return maintenanceMain;
    }

    @Override
    public MaintenanceMain getByMchNameAndSpecAndTypeVersion(String mchName, String spec, String typeVersion) {
        //??????????????????
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
