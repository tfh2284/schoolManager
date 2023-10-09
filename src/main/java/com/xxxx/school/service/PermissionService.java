package com.xxxx.school.service;

import com.xxxx.school.base.BaseService;
import com.xxxx.school.dao.PermissionMapper;
import com.xxxx.school.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     *  通过查询用户拥有的角色，拥有的资源 得到用户拥有的资源列表
     */
    public List<String> queryUserHasRoleHasPermissionByUserId(Integer userId){
        return permissionMapper.queryUserHasRoleHasPermissionByUserId(userId);
    }
}
