package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.DataDictConstants;
import com.aacoptics.wlg.equipment.entity.form.InspectionShiftForm;
import com.aacoptics.wlg.equipment.entity.param.InspectionQueryParam;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.InspectionItemMapper;
import com.aacoptics.wlg.equipment.mapper.InspectionMainMapper;
import com.aacoptics.wlg.equipment.mapper.InspectionShiftMapper;
import com.aacoptics.wlg.equipment.provider.DataDictProvider;
import com.aacoptics.wlg.equipment.service.EquipmentService;
import com.aacoptics.wlg.equipment.service.InspectionItemService;
import com.aacoptics.wlg.equipment.service.InspectionMainService;
import com.aacoptics.wlg.equipment.service.InspectionShiftService;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class InspectionMainServiceImpl extends ServiceImpl<InspectionMainMapper, InspectionMain> implements InspectionMainService {

    @Resource
    InspectionMainMapper inspectionMainMapper;

    @Resource
    InspectionItemMapper inspectionItemMapper;

    @Resource
    InspectionShiftMapper inspectionShiftMapper;

    @Resource
    EquipmentService equipmentService;

    @Resource
    InspectionItemService inspectionItemService;

    @Resource
    InspectionShiftService inspectionShiftService;

    @Resource
    DataDictProvider dataDictProvider;

    @Override
    public IPage<InspectionMain> query(Page page, InspectionQueryParam inspectionQueryParam) {
        QueryWrapper<InspectionMain> queryWrapper = inspectionQueryParam.build();
        queryWrapper.like(StringUtils.isNotBlank(inspectionQueryParam.getMchName()), "mch_name", inspectionQueryParam.getMchName());
        queryWrapper.like(StringUtils.isNotBlank(inspectionQueryParam.getSpec()), "spec", inspectionQueryParam.getSpec());
        queryWrapper.like(StringUtils.isNotBlank(inspectionQueryParam.getTypeVersion()), "type_version", inspectionQueryParam.getTypeVersion());
        queryWrapper.orderByAsc("mch_name");
        queryWrapper.orderByAsc("spec");
        queryWrapper.orderByAsc("type_version");

        return this.page(page, queryWrapper);
    }


    @Override
    public List<InspectionMain> queryInspectionDataByCondition(InspectionQueryParam inspectionQueryParam) {

        List<InspectionMain> inspectionMainAndItemList = inspectionMainMapper.findInspectionMainAndItemList(inspectionQueryParam);

        return inspectionMainAndItemList;
    }

    @Transactional
    @Override
    public void importInspectionExcel(InputStream in) throws Exception {
        List<List<String[]>> sheetList = ExcelUtil.read(in);

        if (sheetList.size() < 2) {
            throw new BusinessException("Excel????????????????????????!");
        }

        List<String[]> inspectionItemDataList = sheetList.get(0);
        this.importInspectionItem(inspectionItemDataList);

        List<String[]> inspectionShiftDataList = sheetList.get(1);
        this.importInspectionShift(inspectionShiftDataList);
    }


    /**
     * ???????????????
     * @param excelDataList
     */
    private void importInspectionItem(List<String[]> excelDataList)
    {
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

        String[] titleArray = excelDataList.get(0);//?????????

        String mchNameTitle = titleArray[1];
        String specTitle = titleArray[2];
        String typeVersionTitle = titleArray[3];
        String checkItemTitle = titleArray[4];
        if ((!"????????????".equals(mchNameTitle)) || (!"??????".equals(specTitle))|| (!"??????".equals(typeVersionTitle))|| (!"?????????".equals(checkItemTitle))) {
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
            String checkItem = dataArray[4]; //?????????
            String itemTypeDesc = dataArray[5]; //???????????????
            String checkItemStandard = dataArray[6]; //?????????????????????
            String minValue = dataArray[7]; //???????????????
            String maxValue = dataArray[8]; //???????????????
            String theoreticalValue = dataArray[9]; //?????????

            if (StringUtils.isEmpty(mchName)) {
                throw new BusinessException("???" + (i + 1) + "???????????????????????????");
            }
            if (StringUtils.isEmpty(spec)) {
                throw new BusinessException("???" + (i + 1) + "?????????????????????");
            }
            if (StringUtils.isEmpty(typeVersion)) {
                throw new BusinessException("???" + (i + 1) + "?????????????????????");
            }
            if (StringUtils.isEmpty(checkItem)) {
                throw new BusinessException("???" + (i + 1) + "????????????????????????");
            }
            if(StringUtils.isEmpty(itemTypeDesc))
            {
                throw new BusinessException("???" + (i + 1) + "??????????????????????????????");
            }
            if(itemTypeMap != null && !itemTypeMap.containsValue(itemTypeDesc))
            {
                throw new BusinessException("???" + (i + 1) + "?????????????????????" + itemTypeDesc + "????????????");
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

//            if (StringUtils.isEmpty(checkItemStandard)) {
//                throw new BusinessException("???" + (i + 1) + "????????????????????????????????????");
//            }
//            if (StringUtils.isEmpty(minValue)) {
//                throw new BusinessException("???" + (i + 1) + "??????????????????????????????");
//            }
//            if (StringUtils.isEmpty(maxValue)) {
//                throw new BusinessException("???" + (i + 1) + "??????????????????????????????");
//            }


            try {
                //????????????????????????
                Integer equipmentCount = equipmentService.findEquipmentCountList(mchName, spec, typeVersion);
                if(equipmentCount == 0)
                {
                    throw new BusinessException("??????????????????" + mchName + "????????????" + spec + "????????????" + typeVersion + "?????????????????????????????????");
                }

                //??????????????????
                InspectionMain inspectionMain = this.getByMchNameAndSpecAndTypeVersion(mchName, spec, typeVersion);
                if (inspectionMain == null) {
                    inspectionMain = new InspectionMain();
                    inspectionMain.setMchName(mchName);
                    inspectionMain.setSpec(spec);
                    inspectionMain.setTypeVersion(typeVersion);
                }
                this.saveOrUpdate(inspectionMain);

                //???????????????
                Long inspectionMainId = inspectionMain.getId();
                InspectionItem inspectionItem = this.getItemByMaintenanceItemName(inspectionMainId, checkItem);
                if(inspectionItem == null)
                {
                    inspectionItem = new InspectionItem();
                    inspectionItem.setInspectionMainId(inspectionMainId);
                }
                inspectionItem.setCheckItem(checkItem);
                inspectionItem.setItemType(itemType);
                inspectionItem.setCheckItemStandard(checkItemStandard);
                if(StringUtils.isNotEmpty(minValue)) {
                    inspectionItem.setMinValue(new BigDecimal(minValue));
                }
                if(StringUtils.isNotEmpty(maxValue)) {
                    inspectionItem.setMaxValue(new BigDecimal(maxValue));
                }
                inspectionItem.setTheoreticalValue(theoreticalValue);

                inspectionItemService.saveOrUpdate(inspectionItem);

            }catch (Exception exception)
            {
                log.error("???{}???????????????{}", i, excelDataList.get(i));
                log.error("????????????", exception);
                throw new BusinessException("??????" + (i+1) + "?????????????????????" + exception.getClass().getSimpleName() + "???"+ exception.getMessage());
            }
        }
    }


    /**
     * ??????????????????
     *
     * @param excelDataList
     */
    private void importInspectionShift(List<String[]> excelDataList)
    {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String[] titleArray = excelDataList.get(0);//?????????

        String mchNameTitle = titleArray[1];
        String specTitle = titleArray[2];
        String typeVersionTitle = titleArray[3];
        String shiftTitle = titleArray[4];
        if ((!"????????????".equals(mchNameTitle)) || (!"??????".equals(specTitle))|| (!"??????".equals(typeVersionTitle))|| (!"????????????".equals(shiftTitle))) {
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
            String shift = dataArray[4]; //??????
            String startTime = dataArray[5]; //????????????
            String endTime = dataArray[6]; //????????????

            if (StringUtils.isEmpty(mchName)) {
                throw new BusinessException("???" + (i + 1) + "???????????????????????????");
            }
            if (StringUtils.isEmpty(spec)) {
                throw new BusinessException("???" + (i + 1) + "?????????????????????");
            }
            if (StringUtils.isEmpty(typeVersion)) {
                throw new BusinessException("???" + (i + 1) + "?????????????????????");
            }
            if (StringUtils.isEmpty(shift)) {
                throw new BusinessException("???" + (i + 1) + "?????????????????????");
            }
            if (StringUtils.isEmpty(startTime)) {
                throw new BusinessException("???" + (i + 1) + "???????????????????????????");
            }
            if (StringUtils.isEmpty(endTime)) {
                throw new BusinessException("???" + (i + 1) + "???????????????????????????");
            }

            try {
                LocalTime.parse(startTime, timeFormatter);
            } catch (Exception e)
            {
                throw new BusinessException("???" + (i + 1) + "??????????????????" + startTime  + "???????????????????????????HH:mm:ss??????");
            }
            try {
                LocalTime.parse(endTime, timeFormatter);
            } catch (Exception e)
            {
                throw new BusinessException("???" + (i + 1) + "??????????????????" + endTime + "???????????????????????????HH:mm:ss??????");
            }

            try {
                //????????????????????????
                Integer equipmentCount = equipmentService.findEquipmentCountList(mchName, spec, typeVersion);
                if(equipmentCount == 0)
                {
                    throw new BusinessException("??????????????????" + mchName + "????????????" + spec + "????????????" + typeVersion + "?????????????????????????????????");
                }

                //??????????????????
                InspectionMain inspectionMain = this.getByMchNameAndSpecAndTypeVersion(mchName, spec, typeVersion);
                if (inspectionMain == null) {
                    inspectionMain = new InspectionMain();
                    inspectionMain.setMchName(mchName);
                    inspectionMain.setSpec(spec);
                    inspectionMain.setTypeVersion(typeVersion);
                }
                this.saveOrUpdate(inspectionMain);

                //???????????????
                Long inspectionMainId = inspectionMain.getId();
                InspectionShift inspectionShift = this.getInspectionShiftByShift(inspectionMainId, shift);
                if(inspectionShift == null)
                {
                    inspectionShift = new InspectionShift();
                    inspectionShift.setInspectionMainId(inspectionMainId);
                }
                inspectionShift.setShift(shift);
                inspectionShift.setStartTime(startTime);
                inspectionShift.setEndTime(endTime);

                inspectionShiftService.saveOrUpdate(inspectionShift);

            }catch (Exception exception)
            {
                log.error("???{}???????????????{}", i, excelDataList.get(i));
                log.error("????????????", exception);
                throw new BusinessException("??????" + (i+1) + "?????????????????????" + exception.getClass().getSimpleName() + "???"+ exception.getMessage());
            }
        }
    }

    @Override
    public boolean add(InspectionMain inspectionMain) {
        //??????????????????
        QueryWrapper<InspectionMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", inspectionMain.getMchName());
        queryWrapper.eq("spec", inspectionMain.getSpec());
        queryWrapper.eq("type_version", inspectionMain.getTypeVersion());

        Integer selectCount = inspectionMainMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("????????????????????????????????????");
        }

        boolean isSuccess = this.save(inspectionMain);
        return isSuccess;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        InspectionMain inspectionMain = this.get(id);
        if(inspectionMain != null)
        {
            List<InspectionItem> inspectionItemList = inspectionMain.getInspectionItemList();
            if(inspectionItemList != null && inspectionItemList.size() > 0)
            {
                for(InspectionItem inspectionItem : inspectionItemList)
                {
                    inspectionItemMapper.deleteById(inspectionItem.getId());
                }
            }
            List<InspectionShift> inspectionShiftList = inspectionMain.getInspectionShiftList();
            if(inspectionShiftList != null && inspectionShiftList.size()>0)
            {
                for(InspectionShift inspectionShift : inspectionShiftList)
                {
                    inspectionShiftMapper.deleteById(inspectionShift.getId());
                }
            }
        }
        return this.removeById(id);
    }

    @Override
    public boolean update(InspectionMain inspectionMain) {
        //??????????????????
        QueryWrapper<InspectionMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", inspectionMain.getMchName());
        queryWrapper.eq("spec", inspectionMain.getSpec());
        queryWrapper.eq("type_version", inspectionMain.getTypeVersion());
        queryWrapper.ne("id", inspectionMain.getId());

        Integer selectCount = inspectionMainMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("????????????????????????????????????");
        }

        boolean isSuccess = this.updateById(inspectionMain);
        return isSuccess;
    }


    @Override
    public InspectionMain get(Long id) {
        InspectionMain inspectionMain = this.getById(id);
        if (Objects.isNull(inspectionMain)) {
            throw new BusinessException("ID???" + id + "?????????????????????");
        }

        //???????????????
        QueryWrapper<InspectionItem> inspectionItemQueryWrapper = new QueryWrapper<InspectionItem>();
        inspectionItemQueryWrapper.eq( "inspection_main_id", id);

        inspectionItemQueryWrapper.orderByAsc("check_item");
        List<InspectionItem> inspectionItemList = inspectionItemMapper.selectList(inspectionItemQueryWrapper);

        //??????????????????
        QueryWrapper<InspectionShift> inspectionShiftQueryWrapper = new QueryWrapper<InspectionShift>();
        inspectionShiftQueryWrapper.eq( "inspection_main_id", id);

        inspectionShiftQueryWrapper.orderByAsc("shift");
        List<InspectionShift> inspectionShiftList = inspectionShiftMapper.selectList(inspectionShiftQueryWrapper);

        inspectionMain.setInspectionItemList(inspectionItemList);
        inspectionMain.setInspectionShiftList(inspectionShiftList);

        return inspectionMain;
    }



    @Override
    public InspectionMain getByMchNameAndSpecAndTypeVersion(String mchName, String spec, String typeVersion) {
        //??????????????????
        QueryWrapper<InspectionMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", mchName);
        queryWrapper.eq("spec", spec);
        queryWrapper.eq("type_version", typeVersion);

        InspectionMain inspectionMain = inspectionMainMapper.selectOne(queryWrapper);

        return inspectionMain;
    }

    @Override
    public InspectionItem getItemByMaintenanceItemName(Long inspectionMainId, String checkItem) {
        QueryWrapper<InspectionItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_item", checkItem);
        queryWrapper.eq("inspection_main_id", inspectionMainId);

        InspectionItem inspectionItemObject = inspectionItemMapper.selectOne(queryWrapper);

        return inspectionItemObject;
    }


    @Override
    public InspectionShift getInspectionShiftByShift(Long inspectionMainId, String shift) {
        QueryWrapper<InspectionShift> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shift", shift);
        queryWrapper.eq("inspection_main_id", inspectionMainId);

        InspectionShift inspectionShift = inspectionShiftMapper.selectOne(queryWrapper);

        return inspectionShift;
    }


}
