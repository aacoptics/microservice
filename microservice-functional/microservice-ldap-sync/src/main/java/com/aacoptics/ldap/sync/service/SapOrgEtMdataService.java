package com.aacoptics.ldap.sync.service;


import com.aacoptics.ldap.sync.entity.po.SapOrgEtMdata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface SapOrgEtMdataService extends IService<SapOrgEtMdata> {

    List<SapOrgEtMdata> listAll();
}
