package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.po.Image;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.ImageMapper;
import com.aacoptics.wlg.equipment.service.ImageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Resource
    ImageMapper imageMapper;

    @Override
    public boolean add(Image image) {
        boolean isSuccess = this.save(image);
        if(isSuccess == false)
        {
            throw new BusinessException("新增维修图片失败");
        }

        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(Image image) {

        boolean isSuccess = this.updateById(image);
        return isSuccess;
    }


    @Override
    public Image get(Long id) {
        Image image = this.getById(id);
        if (Objects.isNull(image)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        return image;
    }

}
