package com.aacoptics.ldap.sync.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aacoptics.ldap.sync.entity.po.SapOrgEtMdata;
import com.aacoptics.ldap.sync.mapper.SapOrgEtMdataMapper;
import com.aacoptics.ldap.sync.service.SapOrgEtMdataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class SapOrgEtMdataServiceImpl extends ServiceImpl<SapOrgEtMdataMapper, SapOrgEtMdata> implements SapOrgEtMdataService {


    @Override
    public List<SapOrgEtMdata> listAll(){
        return this.list();
    }

}
