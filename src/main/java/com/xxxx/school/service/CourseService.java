package com.xxxx.school.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.school.base.BaseService;
import com.xxxx.school.dao.CourseMapper;
import com.xxxx.school.query.CourseQuery;
import com.xxxx.school.utils.AssertUtil;
import com.xxxx.school.utils.PhoneUtil;
import com.xxxx.school.vo.Course;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CourseService extends BaseService<Course,Integer> {

    @Resource
    private CourseMapper courseMapper;


    /**
     * 查询
          select  *  from  tableName  limit  [begin,]  count;
          解析：begin：记录的开始行数， 即偏移量(可以理解为下标)；可以不写，默认从0开始，而不是1.
               count：每页的最大记录数。

          三个特定查询条件
          前端user.js（49行）中绑定搜索按钮 表格重载 设定异步数据接口的额外参数
          后端map接受进行分页
     * @param query
     * @return
     */
    public Map<String,Object> queryCourseByParams(CourseQuery query){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(),query.getLimit());
        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.selectByParams(query));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加
     *      判断id为空，则为添加操作
     *      后端传递courseId主键，查询数据
     *      参数校验，设置默认值，判断受影响行数
     * @param course
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCourse(Course course){
        /* 参数校验 */
        checkCourseParams(course.getCourseName(), course.getCourseType(), course.getTeacherName(), course.getTeacherPhone(),null);
        /* 2. 设置参数的默认值 */
        course.setIsValid(1);
        course.setCreateDate(new Date());
        course.setUpdateDate(new Date());
        /*// 设置默认密码
        course.setUserPwd(Md5Util.encode("123456"));*/
        /* 3. 执行添加操作，判断受影响的行数 */
        AssertUtil.isTrue(courseMapper.insertSelective(course) < 1, "用户添加失败！");
        /* 用户角色关联 */
        /**
         * 课程ID
         *  userId
         * 角色ID
         *  roleIds
         */
        //relationCourseRole(course.getCourseId(), course.getRoleIds());
    }


    /**
     * 课程角色关联
     *  添加操作
     *      原始角色不存在
     *          1. 不添加新的角色记录    不操作用户角色表
     *          2. 添加新的角色记录      给指定用户绑定相关的角色记录
     *
     *  更新操作
     *      原始角色不存在
     *          1. 不添加新的角色记录     不操作用户角色表
     *          2. 添加新的角色记录       给指定用户绑定相关的角色记录
     *      原始角色存在
     *          1. 添加新的角色记录       判断已有的角色记录不添加，添加没有的角色记录
     *          2. 清空所有的角色记录     删除用户绑定角色记录
     *          3. 移除部分角色记录       删除不存在的角色记录，存在的角色记录保留
     *          4. 移除部分角色，添加新的角色    删除不存在的角色记录，存在的角色记录保留，添加新的角色
     *
     *   如何进行角色分配？？？
     *      判断用户对应的角色记录存在，先将用户原有的角色记录删除，再添加新的角色记录
     *
     *  删除操作
     *      删除指定用户绑定的角色记录
     *
     * @param courseId  课程ID
     * @param roleIds 角色ID
     * @return
     */
    /*private void relationUserRole(Integer courseId, String roleIds) {
        // 通过用户ID查询角色记录
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        // 判断角色记录是否存在
        if (count > 0) {
            // 如果角色记录存在，则删除该用户对应的角色记录
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "用户角色分配失败！");
        }
        // 判断角色ID是否存在，如果存在，则添加该用户对应的角色记录
        if (StringUtils.isNotBlank(roleIds)){
            // 将用户角色数据设置到集合中，执行批量添加
            List<UserRole> userRoleList = new ArrayList<>();
            // 将角色ID字符串转换成数组
            String[] roleIdsArray = roleIds.split(",");
            // 遍历数组，得到对应的用户角色对象，并设置到集合中
            for (String roleId : roleIdsArray) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoleList.add(userRole);
            }
            // 批量添加用户角色记录
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(), "用户角色分配失败！");
        }
    }*/

    /**
     * 更新用户
     *  1. 参数校验
     *      判断用户ID是否为空，且数据存在
     *      课程名userName     非空，唯一性
     *      所属院系          非空
     *      教师名        非空，格式正确
     *  2. 设置参数的默认值
     *      updateDate        系统当前时间
     *  3. 执行更新操作，判断受影响的行数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCourse(Course course){
        // 判断ID是否为空
        AssertUtil.isTrue(course.getCourseId() == null ,"待更新记录不存在");
        // 通过id查询数据
        Course temp = courseMapper.selectByPrimaryKey(course.getCourseId());
        AssertUtil.isTrue(temp == null,"待更新记录不存在");
        checkCourseParams(course.getCourseName(), course.getCourseType(),course.getTeacherName(), course.getTeacherPhone(), course.getCourseId());
        // 设置默认值
        course.setUpdateDate(new Date());
        // 执行更新操作，判断受影响的行数
        AssertUtil.isTrue(courseMapper.updateByPrimaryKeySelective(course) != 1, "用户更新失败！");

        /* 用户角色关联 */
        /**
         * 用户ID
         *  userId
         * 角色ID
         *  roleIds
         */
        //relationUserRole(course.getCourseId(), course.getRoleIds());
    }

    /**
     *  参数校验
     *        课程名courseName     非空，唯一性
     *        院系          非空
     *        手机号TeacherPhone       非空，格式正确
     */
    private void checkCourseParams(String courseName, String courseType, String teacherName,String teacherPhone,Integer courseId) {
        AssertUtil.isTrue(StringUtils.isBlank(courseName), "课程名不能为空！");
        // 判断课程名的唯一性
        // 通过课程名查询对象
        Course temp = courseMapper.queryCourseByName(courseName);
        // 如果用户对象为空，则表示用户名可用；如果用户对象不为空，则表示用户名不可用
        // 如果是添加操作，数据库中无数据，只要通过名称查到数据，则表示用户名被占用
        // 如果是修改操作，数据库中有对应的记录，通过用户名查到数据，可能是当前记录本身，也可能是别的记录
        // 如果用户名存在，且与当前修改记录不是同一个，则表示其他记录占用了该用户名，不可用
        AssertUtil.isTrue(null != temp && !(temp.getCourseId().equals(courseId)), "课程名已存在，请重新输入！");
        // 所属院系非空
        AssertUtil.isTrue(StringUtils.isBlank(courseType), "所属院系不能为空！");
        // 教师名 非空
        AssertUtil.isTrue(StringUtils.isBlank(teacherName), "教师名不能为空！");

        // 手机号 格式判断
        AssertUtil.isTrue(!PhoneUtil.isMobile(teacherPhone), "手机号格式不正确！");
    }

    /**
     * 用户删除
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids){
        // 判断ids是否为空，长度大于0
        AssertUtil.isTrue(ids == null || ids.length == 0,"待删除记录不存在");
        // 执行删除操作，判断受影响的行数
        AssertUtil.isTrue(courseMapper.deleteBatch(ids) != ids.length, "用户删除失败！");
        /*// 遍历用户ID数组
        for (Integer userId : ids){
            // 通过用户ID查询对应的用户角色记录
            Integer count  = userRoleMapper.countUserRoleByUserId(userId);
            // 判断用户角色记录是否存在
            if (count > 0) {
                //  通过用户ID删除对应的用户角色记录
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "删除用户失败！");
            }
        }*/
    }
}