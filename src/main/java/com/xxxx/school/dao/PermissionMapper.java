package com.xxxx.school.dao;

import com.xxxx.school.base.BaseMapper;
import com.xxxx.school.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    // 通过角色ID查询权限记录
    Integer countPermissionByRoleId(Integer roleId);

    // 通过角色ID删除权限记录
    Integer deletePermissionByRoleId(Integer roleId);

    //通过角色ID查询角色拥有的所有资源Id的集合
    List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);

    // 通过用户ID查询对应的资源列表（资源权限码）
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);
}