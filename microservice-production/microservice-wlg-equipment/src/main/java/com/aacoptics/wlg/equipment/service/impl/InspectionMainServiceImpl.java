package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.form.InspectionShiftForm;
import com.aacoptics.wlg.equipment.entity.param.InspectionQueryParam;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import com.aacoptics.wlg.equipment.entity.po.InspectionShift;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.InspectionItemMapper;
import com.aacoptics.wlg.equipment.mapper.InspectionMainMapper;
import com.aacoptics.wlg.equipment.mapper.InspectionShiftMapper;
import com.aacoptics.wlg.equipment.service.InspectionMainService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class InspectionMainServiceImpl extends ServiceImpl<InspectionMainMapper, InspectionMain> implements InspectionMainService {

    @Resource
    InspectionMainMapper inspectionMainMapper;

    @Resource
    InspectionItemMapper inspectionItemMapper;

    @Resource
    InspectionShiftMapper inspectionShiftMapper;

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


}
