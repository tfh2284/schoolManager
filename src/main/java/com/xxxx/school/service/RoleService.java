package com.xxxx.school.service;

import com.xxxx.school.base.BaseService;
import com.xxxx.school.dao.ModuleMapper;
import com.xxxx.school.dao.PermissionMapper;
import com.xxxx.school.dao.RoleMapper;
import com.xxxx.school.utils.AssertUtil;
import com.xxxx.school.vo.Permission;
import com.xxxx.school.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    // 查询角色
    public List<Map<Role,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色
     *  1. 参数校验
     *      角色名称    查询角色记录
     *  2. 设置参数默认值
     *      是否有效
     *  3. 执行添加操作，判断受影响的行数
     * @param role
     * @return void
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        // 参数校验
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空");
        // 通过角色名称查询角色记录
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null,"角色名称已存在，请重新输入");
        /* 设置参数默认值 */
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色添加失败！");
    }

    /**
     * 修改角色
     *      判断角色Id是否存在
     *      判断角色名称非空唯一性
     *      设置默认值
     *      判断受影响行数
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        // 判断角色Id
        AssertUtil.isTrue(role.getId() == null,"待更新记录不存在");
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp == null,"待更新记录不存在");
        // 判断角色名称
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空！");
        temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp && (!temp.getId().equals(role.getId())), "角色名称已存在，不可使用！");
        // 设置默认更新时间
        role.setUpdateDate(new Date());
        // 判断受影响行数
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "修改角色失败！");
    }


    /**
     * 删除角色
     *      根据角色id删除
     *      判断角色id
     *      设置默认值 有效值与更新事时间
     *      判断受影响行数
     *
     * 乐字节：专注线上IT培训
     * 答疑老师微信：lezijie
     * @param roleId
     * @return void
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        // 角色Id校验
        AssertUtil.isTrue(roleId == null,"待删除记录不存在");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role == null,"待删除记录不存在");
        // 设置默认值
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        // 判断受影响行数
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "角色删除失败！");
    }

    /**
     * 角色授权
     *      将对应的角色Id与资源Id添加到权限表中
     *      先将现有的权限记录删除，在添加
     *          1. 通过角色Id查询记录
     *          2. 如果权限记录存在，则删除对应的角色拥有的权限记录
     *          3. 如果有权限记录，则添加权限记录 (批量添加)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId,Integer[] mIds){
        // 通过角色Id查询对应权限记录
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        // 判断权限记录是否存在 然后删除对应角色拥有的权限记录
        if (count > 0) {
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        // 如果有权限记录，则添加权限记录
        if (mIds != null && mIds.length > 0) {
            List<Permission> permissionList = new ArrayList<>();
            for (Integer mId : mIds) {
                Permission permission = new Permission();
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList)!= permissionList.size(),"角色授权失败");
        }
    }
}
