package com.aacoptic.sep.service;


import com.aacoptic.sep.entity.form.QueryForm;
import com.aacoptic.sep.entity.po.SepClient;
import com.aacoptics.common.core.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface SepClientService extends IService<SepClient> {

    List<SepClient> getHardwareKey(String computerName);

    Result ChangeGroup(QueryForm queryForm);
}