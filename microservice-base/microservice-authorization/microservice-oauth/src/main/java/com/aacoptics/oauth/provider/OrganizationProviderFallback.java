package com.aacoptics.oauth.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.oauth.entity.Role;
import com.aacoptics.oauth.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class OrganizationProviderFallback implements OrganizationProvider {

    @Override
    public Result<User> getUserByUniqueId(String uniqueId) {
        return Result.success(new User());
    }

    @Override
    public Result<Set<Role>> queryRolesByUserId(Long userId) {
        return Result.success(new HashSet<>());
    }
}
