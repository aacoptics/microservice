package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.po.ExceptionSubClass;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.ExceptionSubClassMapper;
import com.aacoptics.wlg.equipment.service.ExceptionSubClassService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class ExceptionSubClassServiceImpl extends ServiceImpl<ExceptionSubClassMapper, ExceptionSubClass> implements ExceptionSubClassService {

    @Resource
    ExceptionSubClassMapper exceptionSubClassMapper;


    @Override
    public boolean add(ExceptionSubClass exceptionSubClass) {
        //校验是否存在
        QueryWrapper<ExceptionSubClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sub_class", exceptionSubClass.getSubClass());
        queryWrapper.eq("exception_type_id", exceptionSubClass.getExceptionTypeId());

        Integer selectCount = exceptionSubClassMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        boolean isSuccess = this.save(exceptionSubClass);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ExceptionSubClass exceptionSubClass) {
        //校验是否存在
        QueryWrapper<ExceptionSubClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sub_class", exceptionSubClass.getSubClass());
        queryWrapper.eq("exception_type_id", exceptionSubClass.getExceptionTypeId());
        queryWrapper.ne("id", exceptionSubClass.getId());

        Integer selectCount = exceptionSubClassMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        boolean isSuccess = this.updateById(exceptionSubClass);
        return isSuccess;
    }


    @Override
    public ExceptionSubClass get(Long id) {
        ExceptionSubClass exceptionSubClass = this.getById(id);
        if (Objects.isNull(exceptionSubClass)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }
        return exceptionSubClass;
    }


}
