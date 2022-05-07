package com.aacoptics.oauth.service;

import com.aacoptics.oauth.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface IRoleService {

    Set<Role> queryUserRolesByUserId(Long userId);

}
