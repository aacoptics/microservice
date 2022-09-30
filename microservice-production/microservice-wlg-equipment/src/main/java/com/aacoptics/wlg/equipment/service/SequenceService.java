package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.Sequence;

public interface SequenceService {




    /**
     * 更新序列号信息
     *
     * @param sequence
     */
    boolean update(Sequence sequence);

    /**
     * 根据id删除序列号
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增序列号
     *
     * @param sequence
     * @return
     */
    boolean add(Sequence sequence);



    /**
     * 获取序列号
     *
     * @param id
     * @return
     */
    Sequence get(Long id);


    /**
     * 获取下一个序列号
     *
     * @param sequenceName
     * @return
     */
    Long getNextNumberByName(String sequenceName);

    /**
     * 创建序列号
     */
    
    Sequence createSequence(String sequenceName,
                            String description,
                            Long initialValue,
                            Long incrementValue,
                            Long maxValue);




}
