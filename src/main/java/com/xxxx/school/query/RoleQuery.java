package com.xxxx.school.query;

import com.xxxx.school.base.BaseQuery;

public class RoleQuery extends BaseQuery {
    private String roleName; // 角色名称

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
