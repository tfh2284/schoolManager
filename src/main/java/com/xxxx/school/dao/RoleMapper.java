package com.xxxx.school.dao;

import com.xxxx.school.base.BaseMapper;
import com.xxxx.school.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    // 查询所有角色列表 只需要userId与roleId
    List<Map<Role,Object>> queryAllRoles(Integer userId);

    // 通过角色名查询角色记录
    public Role selectByRoleName(String roleName);
}