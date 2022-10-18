package com.aacoptics.ldap.sync.mapper;

import com.aacoptics.ldap.sync.entity.po.SapOrgEtMdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SapOrgEtMdataMapper extends BaseMapper<SapOrgEtMdata> {
}