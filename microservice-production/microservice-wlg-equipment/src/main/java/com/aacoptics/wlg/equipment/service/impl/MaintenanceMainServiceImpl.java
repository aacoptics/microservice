package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.PeriodUnitConstants;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceItem;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.MaintenanceItemMapper;
import com.aacoptics.wlg.equipment.mapper.MaintenanceMainMapper;
import com.aacoptics.wlg.equipment.service.MaintenanceMainService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MaintenanceMainServiceImpl extends ServiceImpl<MaintenanceMainMapper, MaintenanceMain> implements MaintenanceMainService {

    @Resource
    MaintenanceMainMapper maintenanceMainMapper;

    @Resource
    MaintenanceItemMapper maintenanceItemMapper;

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
    public boolean add(MaintenanceMain maintenanceMain) {
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

        maintenanceMain.setPeriodUnit(PeriodUnitConstants.MONTH);
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

}
