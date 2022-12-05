package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.ExceptionTypeQueryParam;
import com.aacoptics.wlg.equipment.entity.po.ExceptionType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ExceptionTypeService extends IService<ExceptionType> {

    /**
     * 根据条件查询异常分类信息
     *
     * @return
     */
    IPage<ExceptionType> query(Page page, ExceptionTypeQueryParam exceptionTypeQueryParam);


    /**
     * 更新异常分类信息
     *
     * @param exceptionType
     */
    boolean update(ExceptionType exceptionType);

    /**
     * 根据id删除异常分类
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增异常分类
     *
     * @param exceptionType
     * @return
     */
    boolean add(ExceptionType exceptionType);



    /**
     * 获取异常分类设备
     *
     * @param id
     * @return
     */
    ExceptionType get(Long id);


}
