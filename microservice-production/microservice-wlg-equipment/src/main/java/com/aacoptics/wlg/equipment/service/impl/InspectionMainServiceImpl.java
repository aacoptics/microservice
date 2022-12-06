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
            throw new BusinessException("Excel模板错误，请确认!");
        }

        List<String[]> inspectionItemDataList = sheetList.get(0);
        this.importInspectionItem(inspectionItemDataList);

        List<String[]> inspectionShiftDataList = sheetList.get(1);
        this.importInspectionShift(inspectionShiftDataList);
    }


    /**
     * 导入点检项
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
            log.error("获取" + DataDictConstants.ITEM_TYPE + "数据字典失败，" + itemTypeResult.getMsg());
        }

        String[] titleArray = excelDataList.get(0);//标题行

        String mchNameTitle = titleArray[1];
        String specTitle = titleArray[2];
        String typeVersionTitle = titleArray[3];
        String checkItemTitle = titleArray[4];
        if ((!"设备名称".equals(mchNameTitle)) || (!"规格".equals(specTitle))|| (!"型号".equals(typeVersionTitle))|| (!"点检项".equals(checkItemTitle))) {
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
            String checkItem = dataArray[4]; //点检项
            String itemTypeDesc = dataArray[5]; //点检项类型
            String checkItemStandard = dataArray[6]; //点检项判断标准
            String minValue = dataArray[7]; //起始范围值
            String maxValue = dataArray[8]; //截至范围值
            String theoreticalValue = dataArray[9]; //理论值

            if (StringUtils.isEmpty(mchName)) {
                throw new BusinessException("第" + (i + 1) + "行设备名称不能为空");
            }
            if (StringUtils.isEmpty(spec)) {
                throw new BusinessException("第" + (i + 1) + "行规格不能为空");
            }
            if (StringUtils.isEmpty(typeVersion)) {
                throw new BusinessException("第" + (i + 1) + "行型号不能为空");
            }
            if (StringUtils.isEmpty(checkItem)) {
                throw new BusinessException("第" + (i + 1) + "行点检项不能为空");
            }
            if(StringUtils.isEmpty(itemTypeDesc))
            {
                throw new BusinessException("第" + (i + 1) + "行点检项类型不能为空");
            }
            if(itemTypeMap != null && !itemTypeMap.containsValue(itemTypeDesc))
            {
                throw new BusinessException("第" + (i + 1) + "行点检项类型【" + itemTypeDesc + "】不存在");
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

            if (StringUtils.isEmpty(checkItemStandard)) {
                throw new BusinessException("第" + (i + 1) + "行点检项判断标准不能为空");
            }
//            if (StringUtils.isEmpty(minValue)) {
//                throw new BusinessException("第" + (i + 1) + "行起始范围值不能为空");
//            }
//            if (StringUtils.isEmpty(maxValue)) {
//                throw new BusinessException("第" + (i + 1) + "行截至范围值不能为空");
//            }


            try {
                //判断是否存在是否
                Integer equipmentCount = equipmentService.findEquipmentCountList(mchName, spec, typeVersion);
                if(equipmentCount == 0)
                {
                    throw new BusinessException("设备名称为【" + mchName + "】规格【" + spec + "】型号【" + typeVersion + "】的设备不存在，请确认");
                }

                //保存保养设备
                InspectionMain inspectionMain = this.getByMchNameAndSpecAndTypeVersion(mchName, spec, typeVersion);
                if (inspectionMain == null) {
                    inspectionMain = new InspectionMain();
                    inspectionMain.setMchName(mchName);
                    inspectionMain.setSpec(spec);
                    inspectionMain.setTypeVersion(typeVersion);
                }
                this.saveOrUpdate(inspectionMain);

                //保存保养项
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
                inspectionItem.setMinValue(new BigDecimal(minValue));
                inspectionItem.setMaxValue(new BigDecimal(maxValue));
                inspectionItem.setTheoreticalValue(theoreticalValue);

                inspectionItemService.saveOrUpdate(inspectionItem);

            }catch (Exception exception)
            {
                log.error("第{}数据异常：{}", i, excelDataList.get(i));
                log.error("异常信息", exception);
                throw new BusinessException("第【" + (i+1) + "】行数据异常，" + exception.getClass().getSimpleName() + "，"+ exception.getMessage());
            }
        }
    }


    /**
     * 导入点检班次
     *
     * @param excelDataList
     */
    private void importInspectionShift(List<String[]> excelDataList)
    {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String[] titleArray = excelDataList.get(0);//标题行

        String mchNameTitle = titleArray[1];
        String specTitle = titleArray[2];
        String typeVersionTitle = titleArray[3];
        String shiftTitle = titleArray[4];
        if ((!"设备名称".equals(mchNameTitle)) || (!"规格".equals(specTitle))|| (!"型号".equals(typeVersionTitle))|| (!"点检班次".equals(shiftTitle))) {
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
            String shift = dataArray[4]; //班次
            String startTime = dataArray[5]; //开始时间
            String endTime = dataArray[6]; //结束时间

            if (StringUtils.isEmpty(mchName)) {
                throw new BusinessException("第" + (i + 1) + "行设备名称不能为空");
            }
            if (StringUtils.isEmpty(spec)) {
                throw new BusinessException("第" + (i + 1) + "行规格不能为空");
            }
            if (StringUtils.isEmpty(typeVersion)) {
                throw new BusinessException("第" + (i + 1) + "行型号不能为空");
            }
            if (StringUtils.isEmpty(shift)) {
                throw new BusinessException("第" + (i + 1) + "行班次不能为空");
            }
            if (StringUtils.isEmpty(startTime)) {
                throw new BusinessException("第" + (i + 1) + "行开始时间不能为空");
            }
            if (StringUtils.isEmpty(endTime)) {
                throw new BusinessException("第" + (i + 1) + "行结束时间不能为空");
            }

            try {
                LocalTime.parse(startTime, timeFormatter);
            } catch (Exception e)
            {
                throw new BusinessException("第" + (i + 1) + "行开始时间【" + startTime  + "】格式错误，应该为HH:mm:ss格式");
            }
            try {
                LocalTime.parse(endTime, timeFormatter);
            } catch (Exception e)
            {
                throw new BusinessException("第" + (i + 1) + "行结束时间【" + endTime + "】格式错误，应该为HH:mm:ss格式");
            }

            try {
                //判断是否存在是否
                Integer equipmentCount = equipmentService.findEquipmentCountList(mchName, spec, typeVersion);
                if(equipmentCount == 0)
                {
                    throw new BusinessException("设备名称为【" + mchName + "】规格【" + spec + "】型号【" + typeVersion + "】的设备不存在，请确认");
                }

                //保存保养设备
                InspectionMain inspectionMain = this.getByMchNameAndSpecAndTypeVersion(mchName, spec, typeVersion);
                if (inspectionMain == null) {
                    inspectionMain = new InspectionMain();
                    inspectionMain.setMchName(mchName);
                    inspectionMain.setSpec(spec);
                    inspectionMain.setTypeVersion(typeVersion);
                }
                this.saveOrUpdate(inspectionMain);

                //保存保养项
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
                log.error("第{}数据异常：{}", i, excelDataList.get(i));
                log.error("异常信息", exception);
                throw new BusinessException("第【" + (i+1) + "】行数据异常，" + exception.getClass().getSimpleName() + "，"+ exception.getMessage());
            }
        }
    }

    @Override
    public boolean add(InspectionMain inspectionMain) {
        //校验是否存在
        QueryWrapper<InspectionMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", inspectionMain.getMchName());
        queryWrapper.eq("spec", inspectionMain.getSpec());
        queryWrapper.eq("type_version", inspectionMain.getTypeVersion());

        Integer selectCount = inspectionMainMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
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
        //校验是否存在
        QueryWrapper<InspectionMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mch_name", inspectionMain.getMchName());
        queryWrapper.eq("spec", inspectionMain.getSpec());
        queryWrapper.eq("type_version", inspectionMain.getTypeVersion());
        queryWrapper.ne("id", inspectionMain.getId());

        Integer selectCount = inspectionMainMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        boolean isSuccess = this.updateById(inspectionMain);
        return isSuccess;
    }


    @Override
    public InspectionMain get(Long id) {
        InspectionMain inspectionMain = this.getById(id);
        if (Objects.isNull(inspectionMain)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        //查询点检项
        QueryWrapper<InspectionItem> inspectionItemQueryWrapper = new QueryWrapper<InspectionItem>();
        inspectionItemQueryWrapper.eq( "inspection_main_id", id);

        inspectionItemQueryWrapper.orderByAsc("check_item");
        List<InspectionItem> inspectionItemList = inspectionItemMapper.selectList(inspectionItemQueryWrapper);

        //查询点检班次
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
        //校验是否存在
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
