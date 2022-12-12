package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.param.ExceptionTypeQueryParam;
import com.aacoptics.wlg.equipment.entity.po.ExceptionSubClass;
import com.aacoptics.wlg.equipment.entity.po.ExceptionType;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.ExceptionSubClassMapper;
import com.aacoptics.wlg.equipment.mapper.ExceptionTypeMapper;
import com.aacoptics.wlg.equipment.service.ExceptionTypeService;
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
public class ExceptionTypeServiceImpl extends ServiceImpl<ExceptionTypeMapper, ExceptionType> implements ExceptionTypeService {

    @Resource
    ExceptionTypeMapper exceptionTypeMapper;

    @Resource
    ExceptionSubClassMapper exceptionSubClassMapper;

    @Override
    public IPage<ExceptionType> query(Page page, ExceptionTypeQueryParam exceptionTypeQueryParam) {
        QueryWrapper<ExceptionType> queryWrapper = exceptionTypeQueryParam.build();
        queryWrapper.like(StringUtils.isNotBlank(exceptionTypeQueryParam.getExceptionType()), "exception_type", exceptionTypeQueryParam.getExceptionType());
        queryWrapper.orderByAsc("exception_type");

        return this.page(page, queryWrapper);
    }

    @Override
    public boolean add(ExceptionType exceptionType) {
        //校验是否存在
        QueryWrapper<ExceptionType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exception_type", exceptionType.getExceptionType());

        Integer selectCount = exceptionTypeMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        boolean isSuccess = this.save(exceptionType);
        return isSuccess;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        ExceptionType exceptionType = this.get(id);
        if(exceptionType != null)
        {
            List<ExceptionSubClass> exceptionSubClassList = exceptionType.getExceptionSubClassList();
            if(exceptionSubClassList != null && exceptionSubClassList.size() > 0)
            {
                for(ExceptionSubClass exceptionSubClass : exceptionSubClassList)
                {
                    exceptionSubClassMapper.deleteById(exceptionSubClass.getId());
                }
            }
        }
        return this.removeById(id);
    }

    @Override
    public boolean update(ExceptionType exceptionType) {
        //校验是否存在
        QueryWrapper<ExceptionType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exception_type", exceptionType.getExceptionType());
        queryWrapper.ne("id", exceptionType.getId());

        Integer selectCount = exceptionTypeMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        boolean isSuccess = this.updateById(exceptionType);
        return isSuccess;
    }


    @Override
    public ExceptionType get(Long id) {
        ExceptionType exceptionType = this.getById(id);
        if (Objects.isNull(exceptionType)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        //查询异常子类
        QueryWrapper<ExceptionSubClass> exceptionSubClassQueryWrapper = new QueryWrapper<ExceptionSubClass>();
        exceptionSubClassQueryWrapper.eq( "exception_type_id", id);

        exceptionSubClassQueryWrapper.orderByAsc("sub_class");
        List<ExceptionSubClass> exceptionSubClassList = exceptionSubClassMapper.selectList(exceptionSubClassQueryWrapper);

        exceptionType.setExceptionSubClassList(exceptionSubClassList);

        return exceptionType;
    }

    @Override
    public List<ExceptionType> findExceptionTypeList() {
        QueryWrapper<ExceptionType> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("exception_type");

        return exceptionTypeMapper.selectList(queryWrapper);
    }
}
