package com.xxxx.school.controller;

import com.xxxx.school.base.BaseController;
import com.xxxx.school.base.ResultInfo;
import com.xxxx.school.query.RoleQuery;
import com.xxxx.school.service.RoleService;
import com.xxxx.school.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("role")
@Controller
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 加载角色下拉框 : js/user/add.update.js:10行
     * @param userId
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<Role,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    @GetMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }
}
