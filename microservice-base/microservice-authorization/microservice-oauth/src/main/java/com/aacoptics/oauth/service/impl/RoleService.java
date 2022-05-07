package com.aacoptics.oauth.service.impl;

import com.aacoptics.oauth.entity.Role;
import com.aacoptics.oauth.provider.OrganizationProvider;
import com.aacoptics.oauth.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public Set<Role> queryUserRolesByUserId(Long userId) {
        return organizationProvider.queryRolesByUserId(userId).getData();
    }

}
