package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.ItemTypeConstants;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.InspectionItemMapper;
import com.aacoptics.wlg.equipment.service.InspectionItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class InspectionItemServiceImpl extends ServiceImpl<InspectionItemMapper, InspectionItem> implements InspectionItemService {

    @Resource
    InspectionItemMapper inspectionItemMapper;


    @Override
    public boolean add(InspectionItem inspectionItem) {
        //校验是否存在
        QueryWrapper<InspectionItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_item", inspectionItem.getCheckItem());
        queryWrapper.eq("inspection_main_id", inspectionItem.getInspectionMainId());

        Integer selectCount = inspectionItemMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        if(ItemTypeConstants.RANGE.equals(inspectionItem.getItemType())) {
            if (inspectionItem.getMinValue().compareTo(inspectionItem.getMaxValue()) > 0) {
                throw new BusinessException("起始范围值必须小于或等于截至值，请确认！");
            }
        }
        else {
            if(StringUtils.isEmpty(inspectionItem.getTheoreticalValue()))
            {
                throw new BusinessException("理论值不能为空！");
            }
        }

        boolean isSuccess = this.save(inspectionItem);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(InspectionItem inspectionItem) {
        //校验是否存在
        QueryWrapper<InspectionItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_item", inspectionItem.getCheckItem());
        queryWrapper.eq("inspection_main_id", inspectionItem.getInspectionMainId());
        queryWrapper.ne("id", inspectionItem.getId());

        Integer selectCount = inspectionItemMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        if(ItemTypeConstants.RANGE.equals(inspectionItem.getItemType())) {
            if (inspectionItem.getMinValue().compareTo(inspectionItem.getMaxValue()) > 0) {
                throw new BusinessException("起始范围值必须小于或等于截至值，请确认！");
            }
        }
        else
        {
            if(StringUtils.isEmpty(inspectionItem.getTheoreticalValue()))
            {
                throw new BusinessException("理论值不能为空！");
            }
        }
        boolean isSuccess = this.updateById(inspectionItem);
        return isSuccess;
    }


    @Override
    public InspectionItem get(Long id) {
        InspectionItem inspectionItem = this.getById(id);
        if (Objects.isNull(inspectionItem)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }
        return inspectionItem;
    }


}
