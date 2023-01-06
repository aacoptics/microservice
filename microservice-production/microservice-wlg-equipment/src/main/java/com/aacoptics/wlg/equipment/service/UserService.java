package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.EquipmentQueryParam;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * 从EKP数据库获取用户列表
     *
     *
     * @return
     */
    List<String> findUserByName(String userNameInput);

}
