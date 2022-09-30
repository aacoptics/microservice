package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.Image;

public interface ImageService {




    /**
     * 更新图片信息
     *
     * @param image
     */
    boolean update(Image image);

    /**
     * 根据id删除图片
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增图片
     *
     * @param image
     * @return
     */
    boolean add(Image image);



    /**
     * 获取图片
     *
     * @param id
     * @return
     */
    Image get(Long id);






}
