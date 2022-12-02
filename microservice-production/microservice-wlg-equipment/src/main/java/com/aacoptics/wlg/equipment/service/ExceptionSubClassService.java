package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.ExceptionSubClass;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ExceptionSubClassService extends IService<ExceptionSubClass> {


    /**
     * 更新异常子类信息
     *
     * @param exceptionSubClass
     */
    boolean update(ExceptionSubClass exceptionSubClass);

    /**
     * 根据id删除异常子类
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增异常子类
     *
     * @param exceptionSubClass
     * @return
     */
    boolean add(ExceptionSubClass exceptionSubClass);



    /**
     * 获取异常子类
     *
     * @param id
     * @return
     */
    ExceptionSubClass get(Long id);


}
