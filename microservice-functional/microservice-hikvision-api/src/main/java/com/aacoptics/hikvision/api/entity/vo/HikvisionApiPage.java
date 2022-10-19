package com.aacoptics.hikvision.api.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HikvisionApiPage<T> implements Serializable {

    private Integer pageSize;

    private Integer total;

    private Integer totalPage;

    private Integer pageNo;

    private List<T> list;
}
