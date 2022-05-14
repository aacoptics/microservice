package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.param.PositionQueryParam;
import com.aacoptics.organization.entity.po.Position;

import java.util.List;

public interface IPositionService {

    /**
     * 获取职位
     */
    Position get(Long id);

    /**
     * 新增职位
     */
    boolean add(Position position);

    /**
     * 查询职位
     */
    List<Position> query(PositionQueryParam positionQueryParam);

    /**
     * 更新职位信息
     */
    boolean update(Position position);

    /**
     * 根据id删除职位
     */
    boolean delete(Long id);

}
