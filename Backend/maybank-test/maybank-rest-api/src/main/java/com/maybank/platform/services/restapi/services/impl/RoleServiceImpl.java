package com.maybank.platform.services.restapi.services.impl;

import com.maybank.platform.services.restapi.annotation.EnablePerformanceLogger;
import com.maybank.platform.services.restapi.dao.RoleRepository;
import com.maybank.platform.services.restapi.model.Role;
import com.maybank.platform.services.restapi.services.RoleService;
import com.maybank.platform.services.util.RedisDbKeyUtils;
import com.maybank.platform.services.util.RedisUtil;
import com.maybank.platform.services.util.enums.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RedisUtil redisUtil;

    @Override
    @EnablePerformanceLogger
    public Optional<Role> findByName(ERole name){
        return roleRepository.findByName(name);
    }

    @Override
    @EnablePerformanceLogger
    public List<Role> getAllRoles() {
        String key = RedisDbKeyUtils.getRedisCacheRestApiKey("roles");
        if(redisUtil.exists(key)) {
            return redisUtil.getList(key, Role.class);
        } else {
            List<Role> roleList = roleRepository.findAll();
            redisUtil.put(key, roleList);
            return roleList;
        }
    }
}
